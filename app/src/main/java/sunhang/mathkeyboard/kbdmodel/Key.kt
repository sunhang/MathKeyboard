package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import protoinfo.KbdInfo
import sunhang.mathkeyboard.tools.sp2Px

open class Key(private val context: Context, private val keyInfo: KbdInfo.KeyInfo) {
    private val paint = Paint()
    private val keyLayout = KeyLayout(keyInfo.rectInfo)

    init {
        paint.color = Color.DKGRAY
        paint.textSize = keyInfo.textSize
    }

    open fun draw(canvas: Canvas) {
        val visualRect = keyLayout.visualRect
        canvas.drawText(keyInfo.text, visualRect.centerX(), visualRect.centerY(), paint)
    }
}