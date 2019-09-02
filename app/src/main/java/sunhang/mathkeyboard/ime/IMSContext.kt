package sunhang.mathkeyboard.ime

import android.content.Context
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser

class IMSContext(val context: Context, val logicMsgPasser: MsgPasser) {
    val imeLayoutConfig = IMELayoutConfig(context)
}