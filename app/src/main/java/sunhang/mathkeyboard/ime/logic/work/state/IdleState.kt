package sunhang.mathkeyboard.ime.logic.work.state

import androidx.annotation.WorkerThread
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.ime.logic.work.State

@WorkerThread
class IdleState(private val logicContext: LogicContext) : State {
    override fun inputChar(code: Int) {
        logicContext.editor.commitCode(code)
    }
}