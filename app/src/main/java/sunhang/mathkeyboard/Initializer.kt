package sunhang.mathkeyboard

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.View
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.mathkeyboard.ime.logic.Logic
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgExecutor
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.kbdviews.RootView

class Initializer(context: Context) {
    val rootController: RootController
    val rootView: RootView
    val logic: Logic
    init {
        // 解决循环依赖的问题
        class MsgExecutorWrapper : MsgExecutor {
            var msgExecutor: MsgExecutor? = null

            override fun execute(msg: Msg) {
                msgExecutor?.execute(msg)
            }
        }

        val workThread = HandlerThread("input-logic").apply { start() }

        rootView = View.inflate(context, R.layout.ime_layout, null) as RootView

        val logicContextWrapper = MsgExecutorWrapper()
        // todo 观察looper此时返回null吗？因为怀疑[Thread.isAlive]
        val logicMsgPasser = MsgPasser(Handler(workThread.looper), logicContextWrapper)
        rootController = RootController(IMSContext(context, logicMsgPasser), rootView)

        val editor = Editor()
        val mainLooper = Looper.getMainLooper()
        val logicContext = LogicContext(MsgPasser(mainLooper, editor), MsgPasser(mainLooper, KbdUIExecutor(rootController)))
        logic = Logic(workThread, editor, logicContext)

        logicContextWrapper.msgExecutor = logic.logicExecutor
    }
}