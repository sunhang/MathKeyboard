package sunhang.mathkeyboard.ime.logic

import android.os.HandlerThread
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.annotation.MainThread
import sunhang.mathkeyboard.base.common.InstancesContainer
import sunhang.mathkeyboard.base.common.putInstanceIntoContainer
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msgpasser.EditorMsgPasser
import sunhang.mathkeyboard.ime.logic.msgpasser.LogicMsgPasser
import sunhang.mathkeyboard.ime.logic.work.LogicContext

@MainThread
class Logic {
    private val workThread = HandlerThread("input-logic")
    private val editor = Editor()
    private val logicContext: LogicContext by InstancesContainer
    val logicMsgPasser: LogicMsgPasser by InstancesContainer

    /*
    val input = Proxy.newProxyInstance(
        Input::class.java.classLoader,
        arrayOf(Input::class.java)
    ) { _, method: Method?, args: Array<out Any>? ->
        if (method == null) return@newProxyInstance null

        handler.post {
            if (logicContext.state == null) {
                logicContext.state = IdleState(logicContext)
            }

            val array = args ?: arrayOf()
            method.invoke(logicContext.state, *array)
        }
    } as Input
    */

    fun attachInputConnection(inputConnection: InputConnection) {
        editor.currentInputConnection = inputConnection
    }

    fun attachEditorInfo(editorInfo: EditorInfo) {
        editor.editorInfo = editorInfo
    }

    fun init() {
        workThread.start()
        val logicContext = LogicContext(EditorMsgPasser(editor))
        // todo 观察looper此时返回null吗？因为怀疑[Thread.isAlive]
        val msgPasser = LogicMsgPasser(workThread.looper, logicContext)

        msgPasser.passMessage(Msg.Logic.INIT)

        putInstanceIntoContainer(this::logicContext.name, logicContext)
        putInstanceIntoContainer(this::logicMsgPasser.name, msgPasser)
    }

    fun dispose() {
        logicMsgPasser.passMessage(Msg.Logic.DISPOSE)
        workThread.quitSafely()
    }

}

