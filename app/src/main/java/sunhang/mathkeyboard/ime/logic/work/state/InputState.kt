package sunhang.mathkeyboard.ime.logic.work.state

import sunhang.mathkeyboard.ime.logic.asSingle
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.ime.logic.work.State
import sunhang.mathkeyboard.tools.i

class InputState : State {
    private var offset = 0
    private val pyBuf = ByteArray(32)

    override fun doAction(context: LogicContext, msg: Msg) {
        when (msg.type) {
            Msg.Logic.CODE -> {
                val code = msg.valuePack.asSingle<Int>().value
                val pinyinDecoder = context.pinyinDecoder!!
                pyBuf[offset++] = code.toByte()

                val totalChoicesNum = pinyinDecoder.imSearch(pyBuf, offset)
                val candis = pinyinDecoder.imGetChoiceList(0, totalChoicesNum, 0)

                i(candis.toString())
            }
        }
    }
}