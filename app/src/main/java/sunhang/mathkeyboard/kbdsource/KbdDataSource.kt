package sunhang.mathkeyboard.kbdsource

import android.content.Context
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import protoinfo.KbdInfo
import sunhang.mathkeyboard.files.FilePath
import sunhang.mathkeyboard.kbdinfo.*
import sunhang.mathkeyboard.kbdmodel.Keyboard
import sunhang.mathkeyboard.kbdmodel.PlaneType
import sunhang.mathkeyboard.kbdmodel.factory.KeyboardFactory
import sunhang.mathkeyboard.kbdmodel.factory.QwertyEnKeyboardFactory
import sunhang.mathkeyboard.kbdmodel.factory.QwertyZHKeyboardFactory
import sunhang.mathkeyboard.tools.debugTime
import sunhang.mathkeyboard.tools.elapsedTime
import sunhang.mathkeyboard.tools.i
import sunhang.openlibrary.fileScheduler
import java.io.File
import java.util.*

class KbdDataSource(private val context: Context) {
    private val kbdMemCache = WeakHashMap<String, Keyboard>()

    private fun getKbdProtoFromDisk(file: File): Observable<KbdInfo.KeyboardInfo> {
        return Observable.create<KbdInfo.KeyboardInfo> {
            if (file.exists()) {
                val begin = debugTime()
                val bytes = file.readBytes()
                it.onNext(KbdInfo.KeyboardInfo.parseFrom(bytes))
                elapsedTime("Parse from proto $file ==>", begin)
            }

            it.onComplete()
        }.subscribeOn(fileScheduler)
    }

    private fun getKbdProtoFromFactory(factory: InfoFactory): Observable<KbdInfo.KeyboardInfo> {
        return Observable.create<KbdInfo.KeyboardInfo> {
            val begin = debugTime()

            val kbdInfo = factory.create()
            it.onNext(kbdInfo)
            it.onComplete()

            elapsedTime("Create proto keyboard", begin)
        }.subscribeOn(Schedulers.computation())
    }

    private fun getKbdFromMemCache(key: String): Maybe<Keyboard> {
        return Maybe.create<Keyboard> { emitter ->
            kbdMemCache[key]?.let { keyboard ->
                emitter.onSuccess(keyboard)

                i("getKbdFromMemCache $key")
            }
            emitter.onComplete()
        }.subscribeOn(AndroidSchedulers.mainThread())
    }

    private fun getKbdModel(file: File, infoFactory: InfoFactory, keyboardFactory: KeyboardFactory): Maybe<Keyboard> {
        val diskProto = getKbdProtoFromDisk(file)
        val protoFromFactory = getKbdProtoFromFactory(infoFactory)
            .observeOn(fileScheduler)
            .doOnNext {
                // 缓存起来
                file.writeBytes(it.toByteArray())
                i("doOnNext in ${Thread.currentThread()}")
            }

        /**
         * kbdInfo(proto格式) -> Keyboard
         */
        val keyboardSource = Observable.concat(diskProto, protoFromFactory).firstElement()
            .observeOn(Schedulers.computation())
            .map {
                i("keyboardFactory.createKeyboard $keyboardFactory")
                keyboardFactory.createKeyboard(it)
            }.observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                kbdMemCache[keyFrom(file)] = it
            }

        val memSource = getKbdFromMemCache(keyFrom(file))

        return Maybe.concat(memSource, keyboardSource).firstElement().observeOn(AndroidSchedulers.mainThread())
    }

    fun getKbdModel(planeType: PlaneType, keyboardWidth: Int, keyboardHeight: Int): Maybe<Keyboard> {
        return when (planeType) {
            PlaneType.NUMBER -> {
                getKbdModel(
                    FilePath.keyboardNumProtoFile(keyboardWidth, keyboardHeight),
                    NumInfoFactory(context, keyboardWidth, keyboardHeight),
                    KeyboardFactory(context)
                )
            }
            PlaneType.MATH_SYMBOL_0 -> {
                getKbdModel(
                    FilePath.keyboardMathSymbol0ProtoFile(keyboardWidth, keyboardHeight),
                    MathSymbolInfo0Factory(context, keyboardWidth, keyboardHeight),
                    KeyboardFactory(context)
                )
            }
            PlaneType.MATH_SYMBOL_1 -> {
                getKbdModel(
                    FilePath.keyboardMathSymbol1ProtoFile(keyboardWidth, keyboardHeight),
                    MathSymbolInfo1Factory(context, keyboardWidth, keyboardHeight),
                    KeyboardFactory(context)
                )
            }
            PlaneType.QWERTY_ZH -> {
                getKbdModel(
                    FilePath.keyboardZhProtoFile(keyboardWidth, keyboardHeight),
                    QwertyZhInfoFactory(context, keyboardWidth, keyboardHeight),
                    QwertyZHKeyboardFactory(context)
                )
            }
            else -> {
                getKbdModel(
                    FilePath.keyboardEnProtoFile(keyboardWidth, keyboardHeight),
                    QwertyEnInfoFactory(context, keyboardWidth, keyboardHeight),
                    QwertyEnKeyboardFactory(context))
            }
        }
    }

    private fun keyFrom(file: File) = file.name.hashCode().toString()

}