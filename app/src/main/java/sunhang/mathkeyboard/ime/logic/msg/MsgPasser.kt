package sunhang.mathkeyboard.ime.logic.msg

import android.os.Handler
import android.os.Looper
import androidx.annotation.WorkerThread

class MsgPasser(private val handler: Handler) {
    private lateinit var msgExecutor: MsgExecutor

    constructor(looper: Looper) : this(Handler(looper))

    private inner class MsgRunnable(private val msg: Msg) : Runnable {

        @WorkerThread
        override fun run() {
            msgExecutor.execute(msg)
        }

    }

    fun attachExecutor(msgExecutor: MsgExecutor) {
        this.msgExecutor = msgExecutor
    }

    fun <T> passMessage(type: Msg.Type, value: T) {
        handler.post(MsgRunnable(Msg(type, SingleValue<T>(value))))
    }

    fun <T, K> passMessage(type: Msg.Type, first: T, second: K) {
        handler.post(MsgRunnable(Msg(type, DoubleValue<T, K>(first, second))))
    }

    fun passMessage(type: Msg.Type) {
        handler.post(MsgRunnable(Msg(type, NoneValue)))
    }
}