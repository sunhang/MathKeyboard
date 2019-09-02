package sunhang.mathkeyboard.ime.logic.work

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msgpasser.EditorMsgPasser
import sunhang.mathkeyboard.ime.logic.work.state.IdleState

@WorkerThread
class LogicContext @MainThread constructor(val editorMsgPasser: EditorMsgPasser) {
    var state: State? = null

    /*
    val editor = Proxy.newProxyInstance(
        Editor::class.java.classLoader,
        arrayOf(Editor::class.java)
    ) { _, method: Method?, args: Array<out Any>? ->
        runOnMain {
            val array = args ?: arrayOf()
            method?.invoke(inputToEditor, *array)
        }
    } as Editor
    */

    fun init() {
        state = IdleState()
    }

    fun dispose() {
    }

    fun callStateAction(message: Msg) {
        state?.doAction(this, message)
    }
}