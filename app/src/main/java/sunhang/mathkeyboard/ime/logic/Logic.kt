package sunhang.mathkeyboard.ime.logic

import android.os.Handler
import android.os.HandlerThread
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.annotation.MainThread
import sunhang.mathkeyboard.base.common.InstancesContainer
import sunhang.mathkeyboard.base.common.putInstanceIntoContainer
import sunhang.mathkeyboard.ime.logic.work.EditorImpl
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.ime.logic.work.state.IdleState
import java.lang.reflect.Method
import java.lang.reflect.Proxy

@MainThread
class Logic {
    private val workThread = HandlerThread("input-logic")
    private val handler: Handler by InstancesContainer
    private val editorImpl = EditorImpl()
    private val logicContext = LogicContext(editorImpl)

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

    fun attachInputConnection(inputConnection: InputConnection) {
        editorImpl.currentInputConnection = inputConnection
    }

    fun attachEditorInfo(editorInfo: EditorInfo) {
        editorImpl.editorInfo = editorInfo
    }

    fun init() {
        workThread.start()
        // todo 观察looper此时返回null吗？因为怀疑[Thread.isAlive]
        val handler = Handler(workThread.looper)
        putInstanceIntoContainer(this::handler.name, handler)
    }

    fun dispose() {
        workThread.quitSafely()
    }

}

