package sunhang.mathkeyboard.kbdskin

import android.graphics.drawable.Drawable

data class KeyVisualAttributes(
    val keyBackground: Drawable,
    val keyBackgroundPressed: Drawable,
    val keyLabelColor: Int = 0,
    val keyLabelColorPressed: Int = 0,
    val keyHintLabelColor: Int = 0,
    val keyHintLabelColorPressed: Int = 0
)
