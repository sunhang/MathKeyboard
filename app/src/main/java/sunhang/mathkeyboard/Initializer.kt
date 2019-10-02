package sunhang.mathkeyboard

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.View
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.mathkeyboard.ime.logic.LogicExecutor
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgExecutor
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.kbdviews.RootView

class Initializer(context: Context) {
    val rootController: RootController
    val logicThread: HandlerThread
    val editor = Editor()
    val imsContext: IMSContext

    init {
        // 解决循环依赖的问题
        class MsgExecutorWrapper : MsgExecutor {
            var msgExecutor: MsgExecutor? = null

            override fun execute(msg: Msg) {
                msgExecutor?.execute(msg)
            }
        }

        val logicContextWrapper = MsgExecutorWrapper()
        val rootView = View.inflate(context, R.layout.ime_layout, null) as RootView

        // todo 观察[logicThread.looper]此时返回null吗？因为怀疑[Thread.isAlive]
        logicThread = HandlerThread("input-logic").apply { start() }
        imsContext = IMSContext(context, MsgPasser(Handler(logicThread.looper), logicContextWrapper))
        rootController = RootController(imsContext, rootView)

        val mainLooper = Looper.getMainLooper()
        val logicContext = LogicContext(
            MsgPasser(mainLooper, editor),
            MsgPasser(mainLooper, KbdUIExecutor(rootController))
        )

        logicContextWrapper.msgExecutor = LogicExecutor(logicContext)
    }
}