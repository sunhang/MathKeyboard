package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.*
import protoinfo.KbdInfo
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.tools.multiplyAlpha
import sunhang.mathkeyboard.tools.sp2Px

class ZhEnSwitchKey(context: Context, keyInfo: KbdInfo.KeyInfo, val zh2En: Boolean) : Key(context, keyInfo) {
    private val strZh = context.resources.getString(R.string.key_text_zh)
    private val strEn = context.resources.getString(R.string.key_text_en)
    private val strSeparator = "/"

    private val bigTextSize = sp2Px(keyInfo.textSize)
    private val smallTextSize = bigTextSize * 0.65f

    private val bounds0 = Rect()
    private val bounds1 = Rect()
    private val textPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        textSize = sp2Px(keyInfo.textSize)
        textAlign = Paint.Align.LEFT
    }

    override fun drawForeground(canvas: Canvas) {
        val bigTextColor = if (pressed) pressedColor else normalColor
        val smallTextColor = multiplyAlpha(bigTextColor, 0.5f)

        val (leftStr, rightStr) = if (zh2En) Pair(strZh, strSeparator + strEn) else Pair(strZh + strSeparator, strEn)
        val (leftSize, rightSize) = if (zh2En) Pair(bigTextSize, smallTextSize) else Pair(smallTextSize, bigTextSize)
        val (leftColor, rightColor) =
            if (zh2En) Pair(bigTextColor, smallTextColor) else Pair(smallTextColor, bigTextColor)

        val calculateYAndBounds = { str: String, textSize: Float, bounds: Rect ->
            textPaint.textSize = textSize
            textPaint.getTextBounds(str, 0, str.length, bounds)
            (visualRect.centerY() - bounds.centerY()).toFloat()
        }

        // 计算y的位置
        val leftY = calculateYAndBounds.invoke(leftStr, leftSize, bounds0)
        val rightY = calculateYAndBounds.invoke(rightStr, rightSize, bounds1)
        val strAllWidth = bounds0.width() + bounds1.width()

        // 计算x的位置
        // 中/英时，稍微调整一下
        val adjust = bounds0.width() / 9.0f
        val leftX = (visualRect.centerX() - strAllWidth / 2).let { if (zh2En) it - adjust else it }
        val rightX = (leftX + bounds0.width()).let { if (zh2En) it + adjust else it }

        canvas.drawText(leftStr, leftX, leftY, textPaint.apply {
            textSize = leftSize
            color = leftColor
        })

        canvas.drawText(rightStr, rightX, rightY, textPaint.apply {
            textSize = rightSize
            color = rightColor
        })
    }
}
