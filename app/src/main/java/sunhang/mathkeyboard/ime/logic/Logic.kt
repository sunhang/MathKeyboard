package sunhang.mathkeyboard.ime.logic

import android.os.HandlerThread
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.android.inputmethod.pinyin.IPinyinDecoderService
import sunhang.mathkeyboard.BuildConfig
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgExecutor
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.kbdmodel.PlaneType

@MainThread
class Logic(val workerThread: HandlerThread, val editor: Editor, private val logicContext: LogicContext) {
    fun attachInputConnection(inputConnection: InputConnection) {
        editor.currentInputConnection = inputConnection
    }

    fun attachEditorInfo(editorInfo: EditorInfo) {
        editor.editorInfo = editorInfo
    }

    val logicExecutor = object : MsgExecutor {
        @WorkerThread
        override fun execute(msg: Msg) {
            if (BuildConfig.DEBUG) {
                if (Thread.currentThread() == Looper.getMainLooper().thread) {
                    throw RuntimeException("The code should not run on main thread!")
                }
            }

            when (msg.type) {
//            Msg.Logic.INIT -> init()
//            Msg.Logic.DISPOSE -> dispose()
                Msg.Logic.PINYIN_DEOCODER -> {
                    logicContext.pinyinDecoder = msg.valuePack.asSingle<IPinyinDecoderService>().value
                }
                Msg.Logic.PLANE_TYPE -> {
                    logicContext.planeType = msg.valuePack.asSingle<PlaneType>().value
                }
                else -> logicContext.callStateAction(msg)
            }
        }
    }

    fun dispose() {
        workerThread.quitSafely()
    }

}

