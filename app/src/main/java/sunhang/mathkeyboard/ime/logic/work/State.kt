package sunhang.mathkeyboard.ime.logic.work

import sunhang.mathkeyboard.ime.logic.msg.Msg

interface State {
    fun doAction(context: LogicContext, msg: Msg)
}
