package sunhang.mathkeyboard.ime

import android.content.Context
import sunhang.mathkeyboard.data.KbdDb
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser
import sunhang.openlibrary.uiLazy

class IMSContext(val context: Context, val logicMsgPasser: MsgPasser) {
    val imeLayoutConfig = IMELayoutConfig(context)
    val kbdDb by uiLazy { KbdDb(context) }

    fun dispose() {
        kbdDb.dispose()
    }
}