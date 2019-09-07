package sunhang.mathkeyboard

import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgExecutor
import sunhang.mathkeyboard.ime.logic.msg.asDouble
import sunhang.mathkeyboard.ime.logic.msg.asSingle

class KbdUIExecutor(private val rootController: RootController) : MsgExecutor {
    override fun execute(msg: Msg) {
        when (msg.type) {
            Msg.KbdUI.CANDI_RESET -> rootController.candiController.setCandis(listOf(), false)
            Msg.KbdUI.CANDI_SHOW -> {
                val (candis, hasMore) = msg.valuePack.asDouble<List<String>, Boolean>()
                rootController.candiController.setCandis(candis, hasMore)
            }
            Msg.KbdUI.CANDI_APPEND -> {
                val (candis, hasMore) = msg.valuePack.asDouble<List<String>, Boolean>()
                rootController.candiController.appendCandis(candis, hasMore)
            }
        }
    }
}

