package sunhang.mathkeyboard.ime.logic.msgpasser

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import sunhang.mathkeyboard.BuildConfig
import sunhang.mathkeyboard.ime.logic.DoubleValue
import sunhang.mathkeyboard.ime.logic.NoneValue
import sunhang.mathkeyboard.ime.logic.SingleValue
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.Msg.Logic.Companion.DISPOSE
import sunhang.mathkeyboard.ime.logic.msg.Msg.Logic.Companion.INIT
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import java.lang.RuntimeException

/**
 * 线程间消息传递
 */
@MainThread
class LogicMsgPasser(logicLooper: Looper, private val logicContext: LogicContext): BaseMsgPasser() {
    override val handler = Handler(logicLooper)

    @WorkerThread
    override fun handleMsg(msg: Msg) {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() == Looper.getMainLooper().thread) {
                throw RuntimeException("The code should not run on main thread!")
            }
        }

        when (msg.type) {
            INIT -> logicContext.init()
            DISPOSE -> logicContext.dispose()
            else -> logicContext.callStateAction(msg)
        }
    }

}