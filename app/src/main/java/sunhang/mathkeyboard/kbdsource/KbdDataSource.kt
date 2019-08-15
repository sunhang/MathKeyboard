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
import sunhang.openlibrary.runOnFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.*

class KbdDataSource(private val context: Context) {
    private fun getKbdInfoFromDisk(file: File): Observable<KbdInfo.KeyboardInfo> {
        return Observable.create<KbdInfo.KeyboardInfo> {
            if (file.exists()) {
                val begin = debugTime()
                val bytes = file.readBytes()
                it.onNext(KbdInfo.KeyboardInfo.parseFrom(bytes))
                elapsedTime("Parse from kbdInfo", begin)
            }

            it.onComplete()
        }.subscribeOn(fileScheduler)
    }

    private fun getKbdInfoFromFactory(factory: InfoFactory): Observable<KbdInfo.KeyboardInfo> {
        return Observable.create<KbdInfo.KeyboardInfo> {
            val begin = debugTime()

            val kbdInfo = factory.create()
            it.onNext(kbdInfo)
            it.onComplete()

            elapsedTime("Create en keyboard", begin)
        }.subscribeOn(Schedulers.computation())
    }

    private fun kbdModel(file: File, infoFactory: InfoFactory, keyboardFactory: KeyboardFactory): Maybe<Keyboard> {
        val diskCache = getKbdInfoFromDisk(file)
        val kbdFromFactory = getKbdInfoFromFactory(infoFactory)
            .observeOn(fileScheduler)
            .doOnNext {
                // 缓存起来
                file.writeBytes(it.toByteArray())
                i("doOnNext in ${Thread.currentThread()}")
            }

        return Observable.concat(diskCache, kbdFromFactory).firstElement()
            .observeOn(Schedulers.computation())
            .map {
                keyboardFactory.createKeyboard(it)
            }.observeOn(AndroidSchedulers.mainThread())
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

}