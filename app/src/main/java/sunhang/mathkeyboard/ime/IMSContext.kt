package sunhang.mathkeyboard.ime

import android.content.Context

class IMSContext(val context: Context, val inputToEditor: InputToEditor) {
    val imeLayoutConfig = IMELayoutConfig(context)
}