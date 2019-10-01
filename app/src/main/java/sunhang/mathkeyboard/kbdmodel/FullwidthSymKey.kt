package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import protoinfo.KbdInfo

class FullwidthSymKey(context: Context, keyInfo: KbdInfo.KeyInfo) : Key(context, keyInfo){
    private val paint = Paint()
    private val bounds = Rect()

    init {
        paint.textAlign = Paint.Align.LEFT
    }

    override fun drawForeground(canvas: Canvas) {
        paint.textSize = textSize
        paint.color = if (pressed) pressedColor else normalColor

        paint.getTextBounds(keyInfo.text, 0, keyInfo.text.length, bounds)

        val x = visualRect.centerX() - bounds.centerX()
        val y = visualRect.top + visualRect.height() * baseLineRatio
        canvas.drawText(keyInfo.text, x, y, paint)
    }
}
