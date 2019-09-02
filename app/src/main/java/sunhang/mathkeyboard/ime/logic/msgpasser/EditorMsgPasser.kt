package sunhang.mathkeyboard.ime.logic.msgpasser

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import sunhang.mathkeyboard.BuildConfig
import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.mathkeyboard.ime.logic.SingleValue
import sunhang.mathkeyboard.ime.logic.msg.Msg
import java.lang.RuntimeException

class EditorMsgPasser(private val editor: Editor): BaseMsgPasser() {
    override val handler = Handler(Looper.getMainLooper())

    @MainThread
    override fun handleMsg(msg: Msg) {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != Looper.getMainLooper().thread) {
                throw RuntimeException("The code should run on main thread")
            }
        }

        when (msg.type) {
            Msg.Editor.COMMIT_CODE -> editor.commitCode((msg.valuePack as SingleValue<Int>).value)
        }
    }
}