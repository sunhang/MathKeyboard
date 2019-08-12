package sunhang.mathkeyboard.kbdinfo

import android.graphics.Paint
import android.graphics.Rect

val bounds = Rect()
val paint = Paint()

fun adjustBaseLine(str: String, keyHeight: Float, textSize: Float): Float{
    paint.textSize = textSize
    paint.getTextBounds(str, 0, str.length, bounds)
    val baseLine: Float = (keyHeight - (bounds.top + bounds.bottom)) / 2
    val ratioBaseLine: Float = baseLine / keyHeight

    return ratioBaseLine
}