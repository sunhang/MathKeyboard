package sunhang.mathkeyboard.kbdskin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

class KbdVisualAttrFactory {
    fun create(): KeyboardVisualAttributes {
        val key = KeyVisualAttributes(keyBackground = ColorDrawable(Color.YELLOW),
            keyBackgroundPressed = ColorDrawable(Color.DKGRAY),
            keyLabelColor = Color.BLUE,
            keyLabelColorPressed = Color.WHITE,
            keyHintLabelColor = 0,
            keyHintLabelColorPressed = 0
            )

        return KeyboardVisualAttributes(
            wallpaper = ColorDrawable(Color.LTGRAY),
            topBackground = ColorDrawable(Color.argb(128, 255, 255, 255)),
            keyVisualAttributes = key,
            specialUil = key,
            funcHighLight = key,
            spaceKey = key
        )
    }
}