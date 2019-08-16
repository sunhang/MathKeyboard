package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import protoinfo.KbdInfo
import sunhang.mathkeyboard.tools.dp2Px

class SpaceKey(context: Context, keyInfo: KbdInfo.KeyInfo) : Key(context, keyInfo) {
    private val paint = Paint()
    private val path = Path()

    init {
        with(paint) {
            strokeWidth = dp2Px(2f)
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
    }

    override fun drawForeground(canvas: Canvas) {
        paint.color = if (pressed) pressedColor else normalColor

        val halfWidth = dp2Px(19.0f).toInt()
        val spaceIconHeight = dp2Px(5.0f).toInt()

        val rect = keyLayout.visualRect
        val centerX = rect.centerX()
        val centerY = rect.centerY() + rect.height() / 12

        path.rewind()
        path.moveTo((centerX - halfWidth), (centerY - spaceIconHeight))
        path.lineTo((centerX - halfWidth), centerY)
        path.lineTo((centerX + halfWidth), centerY)
        path.lineTo((centerX + halfWidth), (centerY - spaceIconHeight))

        canvas.drawPath(path, paint)
    }
}

