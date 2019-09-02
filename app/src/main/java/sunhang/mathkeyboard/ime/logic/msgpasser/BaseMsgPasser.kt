package sunhang.mathkeyboard.ime.logic.msgpasser

import android.os.Handler
import androidx.annotation.WorkerThread
import sunhang.mathkeyboard.ime.logic.DoubleValue
import sunhang.mathkeyboard.ime.logic.NoneValue
import sunhang.mathkeyboard.ime.logic.SingleValue
import sunhang.mathkeyboard.ime.logic.msg.Msg

abstract class BaseMsgPasser {
    abstract val handler: Handler

    abstract fun handleMsg(msg: Msg)

    private inner class MsgRunnable(private val msg: Msg) : Runnable {

        @WorkerThread
        override fun run() {
            handleMsg(msg)
        }

    }

    fun <T> passMessage(type: Int, value: T) {
        handler.post(MsgRunnable(Msg(type, SingleValue<T>(value))))
    }

    fun <T, K> passMessage(type: Int, first: T, second: K) {
        handler.post(MsgRunnable(Msg(type, DoubleValue<T, K>(first, second))))
    }

    fun passMessage(type: Int) {
        handler.post(MsgRunnable(Msg(type, NoneValue)))
    }
}