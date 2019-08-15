package sunhang.mathkeyboard.kbdsource

import android.content.Context
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import protoinfo.KbdInfo
import sunhang.mathkeyboard.files.FilePath
import sunhang.mathkeyboard.kbdinfo.InfoFactory
import sunhang.mathkeyboard.kbdinfo.NumInfoFactory
import sunhang.mathkeyboard.kbdinfo.QwertyEnInfoFactory
import sunhang.mathkeyboard.kbdmodel.Keyboard
import sunhang.mathkeyboard.kbdmodel.factory.KeyboardFactory
import sunhang.mathkeyboard.kbdmodel.factory.QwertyEnKeyboardFactory
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

    private fun kbdModel(file: File, infoFactory: InfoFactory, keyboardFactory: KeyboardFactory): Maybe<Keyboard> {
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

    fun enKbdModel(keyboardWidth: Int, imeHeight: Int): Maybe<Keyboard> {
        return kbdModel(
            FilePath.keyboardEnProtoFile(keyboardWidth, imeHeight),
            QwertyEnInfoFactory(context, keyboardWidth, imeHeight),
            QwertyEnKeyboardFactory(context)
        )
    }

    fun numKbdModel(keyboardWidth: Int, imeHeight: Int): Maybe<Keyboard> {
        return kbdModel(
            FilePath.keyboardNumProtoFile(keyboardWidth, imeHeight),
            NumInfoFactory(context, keyboardWidth, imeHeight),
            KeyboardFactory(context)
        )
    }

    private fun keyFrom(file: File) = file.name.hashCode().toString()

}