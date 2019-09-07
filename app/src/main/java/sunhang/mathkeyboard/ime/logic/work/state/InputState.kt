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
    private var alreadyCandisSize = 0

    companion object {
        const val CANDI_SIZE_IN_PAGE = 20
    }

    private fun getCandidatesThenPassMsg(context: LogicContext, pinyinDecoder: IPinyinDecoderService) {
        val type = if (alreadyCandisSize == 0) {
            Msg.KbdUI.CANDI_SHOW
        } else {
            Msg.KbdUI.CANDI_APPEND
        }

        val remain = totalChoicesNum - alreadyCandisSize
        val requestSize = if (remain < CANDI_SIZE_IN_PAGE) remain else CANDI_SIZE_IN_PAGE

        val candis = pinyinDecoder.imGetChoiceList(alreadyCandisSize, requestSize, 0)
        alreadyCandisSize += requestSize

        context.kbdUIMsgPasser.passMessage(type, candis, alreadyCandisSize < totalChoicesNum)
    }

    override fun doAction(context: LogicContext, msg: Msg) {
        val pinyinDecoder = context.pinyinDecoder!!

        when (msg.type) {
            Msg.Logic.LOAD_MORE_CANDI -> {
                getCandidatesThenPassMsg(context, pinyinDecoder)
            }
            Msg.Logic.CODE -> {
                val code = msg.valuePack.asSingle<Int>().value
                when (code) {
                    KEYCODE_DELETE -> {
                        pinyinDecoder.imDelSearch(1, false, true)
                        offset--
                        if (0 == offset) {
                            totalChoicesNum = 0
                            alreadyCandisSize = 0
                            context.kbdUIMsgPasser.passMessage(Msg.KbdUI.CANDI_RESET)
                            context.state = IdleState()
                        } else {
                            alreadyCandisSize = 0
                            getCandidatesThenPassMsg(context, pinyinDecoder)
                        }
                    }
                    else -> {
                        pyBuf[offset++] = code.toByte()
                        totalChoicesNum = pinyinDecoder.imSearch(pyBuf, offset)
                        alreadyCandisSize = 0
                        getCandidatesThenPassMsg(context, pinyinDecoder)
                    }
                }
            }
        }
    }
}