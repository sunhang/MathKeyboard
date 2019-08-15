package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import protoinfo.KbdInfo

abstract class IconKey(context: Context, keyInfo: KbdInfo.KeyInfo) : Key(context, keyInfo){
    abstract fun getCurrentIcon(): Drawable

    override fun drawForeground(canvas: Canvas) {
        super.drawForeground(canvas)
        val foreIcon = getCurrentIcon()

        val iconWidth = foreIcon.intrinsicWidth
        val iconHeight = foreIcon.intrinsicHeight
        val rect = keyLayout.visualRect

        val left = rect.centerX() - iconWidth / 2
        val top = rect.centerY() - iconHeight / 2
        val right = rect.centerX() + iconWidth / 2
        val bottom = rect.centerY() + iconHeight / 2

        foreIcon.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        foreIcon.draw(canvas)
    }
}