package sunhang.mathkeyboard.ime.logic.msg

import android.os.Handler
import androidx.annotation.WorkerThread

class MsgPasser(private val handler: Handler, private val msgExecutor: MsgExecutor) {
    private inner class MsgRunnable(private val msg: Msg) : Runnable {

        @WorkerThread
        override fun run() {
            msgExecutor.execute(msg)
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