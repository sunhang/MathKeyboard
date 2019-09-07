package sunhang.mathkeyboard.kbdskin

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

class SkinModelFactory {
    fun create(context: Context): SkinModel {
        val letterkey = KeyVisualAttributes(
            keyBackground = getDrawable(context, "key_normal.png"),
            keyBackgroundPressed = getDrawable(context, "key_pressed.png"),
            keyLabelColor = 0xFF48486B.toInt(),
            keyLabelColorPressed = 0xFF48486B.toInt(),
            keyHintLabelColor = 0,
            keyHintLabelColorPressed = 0
        )

        val funckey = KeyVisualAttributes(
            keyBackground = getDrawable(context, "func_key_normal.png"),
            keyBackgroundPressed = getDrawable(context, "key_pressed.png"),
            keyLabelColor = 0xFF706D8E.toInt(),
            keyLabelColorPressed = 0xFF706D8E.toInt(),
            keyHintLabelColor = 0,
            keyHintLabelColorPressed = 0
        )

        val kbd = KeyboardVisualAttributes(
            wallpaper = ColorDrawable(0xFFDDDFE2.toInt()),
            topBackground = ColorDrawable(Color.argb(128, 255, 255, 255)),
            keyVisualAttr = letterkey,
            specialUil = funckey,
            funcHighLight = funckey,
            spaceKey = letterkey
        )

        val candiVisualAttr = CandiVisualAttr(
            normalTextColor = 0xFF706D8E.toInt(),
            highlightTextColor = 0xFF4B53E5.toInt(),
            itemPressedColor = 0x26B0AEC3.toInt(),
            backgroundColor = Color.WHITE
        )

        return SkinModel(
            candiVisualAttr = candiVisualAttr,
            keyboardVisualAttributes = kbd
        )

    }

    private fun getDrawable(context: Context, name: String): Drawable {
        val input = context.assets.open("kbd_skin/$name")
        val bitmap = BitmapFactory.decodeStream(input)
        return createNinePatchBitmap(context, bitmap)
    }
}