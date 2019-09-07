package sunhang.mathkeyboard.ime.logic.work.state

import com.android.inputmethod.pinyin.IPinyinDecoderService
import sunhang.mathkeyboard.KEYCODE_DELETE
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.ime.logic.work.State

class InputState : State {
    private var offset = 0
    private val pyBuf = ByteArray(32)
    private var totalChoicesNum = 0

    companion object {
        const val CANDI_SIZE_IN_PAGE = 10
    }

    private fun getCandidatesThenPassMsg(context: LogicContext, pinyinDecoder: IPinyinDecoderService) {
        val candis = pinyinDecoder.imGetChoiceList(0, CANDI_SIZE_IN_PAGE, 0)
        context.kbdUIMsgPasser.passMessage(Msg.KbdUI.CANDI_SHOW, candis)
    }

    override fun doAction(context: LogicContext, msg: Msg) {
        when (msg.type) {
            Msg.Logic.CODE -> {
                val code = msg.valuePack.asSingle<Int>().value
                val pinyinDecoder = context.pinyinDecoder!!
                when (code) {
                    KEYCODE_DELETE -> {
                        pinyinDecoder.imDelSearch(1, false, true)
                        offset --
                        if (0 == offset) {
                            totalChoicesNum = 0
                            context.kbdUIMsgPasser.passMessage(Msg.KbdUI.CANDI_RESET)
                            context.state = IdleState()
                        } else {
                            getCandidatesThenPassMsg(context, pinyinDecoder)
                        }
                    }
                    else -> {
                        pyBuf[offset++] = code.toByte()
                        totalChoicesNum = pinyinDecoder.imSearch(pyBuf, offset)
                        getCandidatesThenPassMsg(context, pinyinDecoder)
                    }
                }
            }
        }
    }
}