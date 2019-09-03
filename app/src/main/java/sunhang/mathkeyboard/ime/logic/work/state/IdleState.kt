package sunhang.mathkeyboard.ime.logic.work.state

import androidx.annotation.WorkerThread
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.ime.logic.work.State

@WorkerThread
class IdleState : State {
    override fun doAction(context: LogicContext, msg: Msg) {
        val editor = context.editorMsgPasser

        when (msg.type) {
            Msg.Logic.CODE -> {
                if (context.pinyinDecoderReady && context.zhPlane) {
                    val state = InputState()
                    state.doAction(context, msg)
                } else {
                    editor.passMessage(Msg.Editor.COMMIT_CODE, msg.valuePack.asSingle<Int>().value)
                }
            }
        }
    }
}