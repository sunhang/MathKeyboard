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
import sunhang.openlibrary.fileScheduler
import sunhang.openlibrary.runOnFile
import sunhang.openlibrary.screenHeight
import sunhang.openlibrary.screenWidth
import java.io.FileInputStream
import java.io.FileOutputStream

class KbdDataSource(private val context: Context) {
    private fun enKbdInfo(): Maybe<KbdInfo.KeyboardInfo> {
        val diskCache = Observable.create<KbdInfo.KeyboardInfo> {
            val file = FilePath.keyboardEnProtoFile(context.screenWidth, context.screenHeight)
            if (file.exists()) {
                val input = FileInputStream(file)
                val bytes = input.readBytes()
                input.close()
                it.onNext(KbdInfo.KeyboardInfo.parseFrom(bytes))
            }

            it.onComplete()
        }.subscribeOn(fileScheduler)

        val createKbdInfo = Observable.create<KbdInfo.KeyboardInfo> {
            val kbdInfo = QwertyEnInfosFactory(context).createKeyboardInfo(context.screenWidth, context.screenHeight)
            it.onNext(kbdInfo)
            it.onComplete()

            // 保存到文件中
            runOnFile {
                val file = FilePath.keyboardEnProtoFile(context.screenWidth, context.screenHeight)
                val out = FileOutputStream(file)
                kbdInfo.writeTo(out)
                out.close()
            }
        }.subscribeOn(Schedulers.computation())

        return Observable.concat(diskCache, createKbdInfo).firstElement().observeOn(AndroidSchedulers.mainThread())
    }

    fun enKbdModel(): Maybe<Keyboard> {
        return enKbdInfo().map {
            QwertyEnKeyboardFactory(context).createKeyboard(it)
        }
    }
}