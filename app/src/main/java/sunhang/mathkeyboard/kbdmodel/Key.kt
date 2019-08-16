package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import protoinfo.KbdInfo
import sunhang.mathkeyboard.base.common.guardLet
import sunhang.mathkeyboard.tools.sp2Px
import kotlin.math.roundToInt

open class Key(private val context: Context, val keyInfo: KbdInfo.KeyInfo) {
    private val paint = Paint()
    protected val keyLayout = KeyLayout(keyInfo.rectInfo)
    protected var pressed = false
    private val bounds = Rect()
    protected open val textSize = sp2Px(keyInfo.textSize)
    protected open val text: String = keyInfo.text
    protected open val baseLineRatio: Float = keyInfo.baseLine
    var normalColor = 0
    var pressedColor = 0
    var normalBackground: Drawable = ColorDrawable(Color.TRANSPARENT)
    var pressedBackground: Drawable = ColorDrawable(Color.TRANSPARENT)
    open lateinit var onKeyClickedListener: OnKeyClickedListener

    init {
        with(paint) {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
    }

    fun getMainCode() = keyInfo.getMainCode()

    open fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
        drawForeground(canvas)
    }

    fun drawBackground(canvas: Canvas) {
        val visualRect = keyLayout.visualRect

        val drawable = if (pressed) pressedBackground else normalBackground
        drawable.setBounds(
            visualRect.left.roundToInt(),
            visualRect.top.roundToInt(),
            visualRect.right.roundToInt(),
            visualRect.bottom.roundToInt()
        )
        drawable.draw(canvas)
    }

    open fun drawForeground(canvas: Canvas) {
        val visualRect = keyLayout.visualRect
        paint.textSize = textSize
        paint.color = if (pressed) pressedColor else normalColor

        val displayVerCentered = keyInfo.keyColor == KbdInfo.KeyColor.SPECIAL || baseLineRatio == 0.0f
        val y = if (displayVerCentered) {
            paint.getTextBounds(text, 0, text.length, bounds)
            visualRect.centerY() - (bounds.top + bounds.bottom) / 2
        } else {
            visualRect.top + visualRect.height() * baseLineRatio
        }

        canvas.drawText(text, visualRect.centerX(), y, paint)
    }

    val touchRect get() = keyLayout.touchRect

    open fun onTouch(touchEvent: TouchEvent) {
        when (touchEvent.keyMotion) {
            TouchEvent.DOWN -> pressed = true
            TouchEvent.CANCEL, TouchEvent.TAKEN_AWAY -> pressed = false
            TouchEvent.UP -> {
                pressed = false
                invokeOnKeyClickedListener()
            }
        }
    }

    protected fun invokeOnKeyClickedListener() {
        onKeyClickedListener.onClick(getMainCode(), this)
    }

    interface OnKeyClickedListener {
        fun onClick(code: Int, key: Key)
    }
}