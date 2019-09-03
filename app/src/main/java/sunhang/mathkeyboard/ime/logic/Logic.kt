package sunhang.mathkeyboard.ime.logic

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.annotation.MainThread
import sunhang.mathkeyboard.base.common.InstancesContainer
import sunhang.mathkeyboard.base.common.putInstanceIntoContainer
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.openlibrary.uiLazy

@MainThread
class Logic {
    private val workThread = HandlerThread("input-logic").apply { start() }
    private val editor = Editor()
    private val logicContext by uiLazy {
        LogicContext(MsgPasser(Handler(Looper.getMainLooper()), editor))
    }
    val logicMsgPasser by uiLazy {
        // todo 观察looper此时返回null吗？因为怀疑[Thread.isAlive]
        MsgPasser(Handler(workThread.looper), logicContext)
    }

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

    fun dispose() {
        workThread.quitSafely()
    }

}

