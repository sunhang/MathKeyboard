package sunhang.mathkeyboard.ime

import android.content.Context
import sunhang.mathkeyboard.ime.logic.Input

class IMSContext(val context: Context, val input: Input) {
    val imeLayoutConfig = IMELayoutConfig(context)
}