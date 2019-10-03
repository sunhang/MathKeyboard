package sunhang.mathkeyboard.ime.logic.work.state

import androidx.annotation.WorkerThread
import sunhang.mathkeyboard.CODE_ENTER
import sunhang.mathkeyboard.KEYCODE_DELETE
import sunhang.mathkeyboard.KEYCODE_DONE
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.ime.logic.work.State
import sunhang.mathkeyboard.kbdmodel.PlaneType
import sunhang.mathkeyboard.tools.isLetter

@WorkerThread
class IdleState : State {
    override fun doAction(context: LogicContext, msg: Msg) {
        val editor = context.editorMsgPasser

        when (msg.type) {
            Msg.Logic.PLANE_TYPE -> {
                context.planeType = msg.valuePack.asSingle<PlaneType>().value
            }
            Msg.Logic.TEXT -> {
                val str = msg.valuePack.asSingle<String>().value
                editor.passMessage(Msg.Editor.COMMIT_TEXT, str)
            }
            Msg.Logic.CODE -> {
                val code = msg.valuePack.asSingle<Int>().value
                when (code) {
                    KEYCODE_DELETE, CODE_ENTER -> {
                        editor.passMessage(Msg.Editor.COMMIT_CODE, code)
                    }
                    else -> {
                        if (context.pinyinDecoderReady && context.zhPlane && isLetter(code)) {
                            val pinyinDecoder = context.pinyinDecoder!!
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
