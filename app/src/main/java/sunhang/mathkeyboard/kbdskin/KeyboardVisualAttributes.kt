package sunhang.mathkeyboard.kbdskin

import android.graphics.drawable.Drawable

data class KeyboardVisualAttributes(
    val skinId: String = "",
    val wallpaper: Drawable,
    val topBackground: Drawable,
    val keyVisualAttr: KeyVisualAttributes,
    val specialUil: KeyVisualAttributes,
    val funcHighLight: KeyVisualAttributes,
    val spaceKey: KeyVisualAttributes
)






