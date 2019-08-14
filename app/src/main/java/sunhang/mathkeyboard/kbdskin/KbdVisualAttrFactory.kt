package sunhang.mathkeyboard.kbdskin

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import java.util.*

class KbdVisualAttrFactory {
    fun create(context: Context): KeyboardVisualAttributes {
        val letterkey = KeyVisualAttributes(keyBackground = getDrawable(context, "key_normal.png"),
            keyBackgroundPressed = getDrawable(context, "key_pressed.png"),
            keyLabelColor = Color.BLUE,
            keyLabelColorPressed = Color.WHITE,
            keyHintLabelColor = 0,
            keyHintLabelColorPressed = 0
            )

        val funckey = KeyVisualAttributes(keyBackground = getDrawable(context, "func_key_normal.png"),
            keyBackgroundPressed = getDrawable(context, "key_pressed.png"),
            keyLabelColor = Color.BLUE,
            keyLabelColorPressed = Color.WHITE,
            keyHintLabelColor = 0,
            keyHintLabelColorPressed = 0
        )

        return KeyboardVisualAttributes(
            wallpaper = ColorDrawable(0xFFDDDFE2.toInt()),
            topBackground = ColorDrawable(Color.argb(128, 255, 255, 255)),
            keyVisualAttr = letterkey,
            specialUil = funckey,
            funcHighLight = funckey,
            spaceKey = letterkey
        )
    }

    private fun getDrawable(context: Context, name: String): Drawable {
        val input = context.assets.open("kbd_skin/$name")
        val bitmap = BitmapFactory.decodeStream(input)
        return createNinePatchBitmap(context, bitmap)
    }
}