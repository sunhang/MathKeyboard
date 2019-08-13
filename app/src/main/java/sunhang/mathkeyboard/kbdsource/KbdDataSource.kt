package sunhang.mathkeyboard.kbdsource

import android.content.Context
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import protoinfo.KbdInfo
import sunhang.mathkeyboard.files.FilePath
import sunhang.mathkeyboard.kbdinfo.QwertyEnInfosFactory
import sunhang.mathkeyboard.kbdmodel.Keyboard
import sunhang.mathkeyboard.kbdmodel.factory.QwertyEnKeyboardFactory
import sunhang.mathkeyboard.tools.debugTime
import sunhang.mathkeyboard.tools.elapsedTime
import sunhang.openlibrary.fileScheduler
import sunhang.openlibrary.runOnFile
import java.io.FileInputStream
import java.io.FileOutputStream

class KbdDataSource(private val context: Context) {
    private fun enKbdInfo(keyboardWidth: Int, imeHeight: Int): Maybe<KbdInfo.KeyboardInfo> {
        val diskCache = Observable.create<KbdInfo.KeyboardInfo> {
            val file = FilePath.keyboardEnProtoFile(keyboardWidth, imeHeight)
            if (file.exists()) {
                val begin = debugTime()

                val input = FileInputStream(file)
                val bytes = input.readBytes()
                input.close()
                it.onNext(KbdInfo.KeyboardInfo.parseFrom(bytes))

                elapsedTime("Parse from kbdInfo", begin)
            }

            it.onComplete()
        }.subscribeOn(fileScheduler)

        val createKbdInfo = Observable.create<KbdInfo.KeyboardInfo> {
            val begin = debugTime()

            val kbdInfo = QwertyEnInfosFactory(context).createKeyboardInfo(keyboardWidth, imeHeight)
            it.onNext(kbdInfo)
            it.onComplete()

            elapsedTime("Create en keyboard", begin)

            // 保存到文件中
            runOnFile {
                val file = FilePath.keyboardEnProtoFile(keyboardWidth, imeHeight)
                val out = FileOutputStream(file)
                kbdInfo.writeTo(out)
                out.close()
            }
        }.subscribeOn(Schedulers.computation())

        return Observable.concat(diskCache, createKbdInfo).firstElement().observeOn(AndroidSchedulers.mainThread())
    }

    fun enKbdModel(keyboardWidth: Int, imeHeight: Int): Maybe<Keyboard> {
        return enKbdInfo(keyboardWidth, imeHeight).map {
            QwertyEnKeyboardFactory(context).createKeyboard(it)
        }
    }
}