package sunhang.mathkeyboard.ime.logic.work.state

import androidx.annotation.WorkerThread
import sunhang.mathkeyboard.CODE_ENTER
import sunhang.mathkeyboard.KEYCODE_DELETE
import sunhang.mathkeyboard.KEYCODE_DONE
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.ime.logic.work.State

@WorkerThread
class IdleState : State {
    override fun doAction(context: LogicContext, msg: Msg) {
        val editor = context.editorMsgPasser
        val pinyinDecoder = context.pinyinDecoder!!

        when (msg.type) {
            Msg.Logic.CODE -> {
                val code = msg.valuePack.asSingle<Int>().value
                when (code) {
                    KEYCODE_DELETE, CODE_ENTER -> {
                        editor.passMessage(Msg.Editor.COMMIT_CODE, code)
                    }
                    else -> {
                        if (context.pinyinDecoderReady && context.zhPlane) {
                            pinyinDecoder.imResetSearch()

                            context.state = InputState()
                            context.callStateAction(msg)
                        } else {
                            editor.passMessage(Msg.Editor.COMMIT_CODE, code)
                        }
                    }
                }
            }
        }
    }
}
