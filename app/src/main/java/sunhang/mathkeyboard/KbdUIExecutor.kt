package sunhang.mathkeyboard

import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgExecutor
import sunhang.mathkeyboard.ime.logic.msg.asSingle

class KbdUIExecutor(private val rootController: RootController) : MsgExecutor {
    override fun execute(msg: Msg) {
        when (msg.type) {
            Msg.KbdUI.CANDI_RESET -> rootController.candiController.setCandis(listOf())
            Msg.KbdUI.CANDI_SHOW -> {
                val candis = msg.valuePack.asSingle<List<String>>().value
                rootController.candiController.setCandis(candis)
            }
            Msg.KbdUI.CANDI_APPEND -> {
                val candis = msg.valuePack.asSingle<List<String>>().value
                rootController.candiController.appendCandis(candis)
            }
        }
    }
}

