package sunhang.mathkeyboard.ime

import android.content.Context
import sunhang.mathkeyboard.ime.logic.msgpasser.LogicMsgPasser

class IMSContext(val context: Context, val logicMsgPasser: LogicMsgPasser) {
    val imeLayoutConfig = IMELayoutConfig(context)
}