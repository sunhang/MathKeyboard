package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import protoinfo.KbdInfo
import kotlin.math.roundToInt

open class Key(private val context: Context, private val keyInfo: KbdInfo.KeyInfo) {
    private val paint = Paint()
    private val keyLayout = KeyLayout(keyInfo.rectInfo)
    private var pressed = false
    var normalColor = 0
    var pressedColor = 0
    var normalBackground: Drawable = ColorDrawable(Color.TRANSPARENT)
    var pressedBackground: Drawable = ColorDrawable(Color.TRANSPARENT)
    lateinit var onKeyClickedListener: OnKeyClickedListener

    init {
        with(paint) {
            textSize = keyInfo.textSize
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
        drawable.setBounds(visualRect.left.roundToInt(),
            visualRect.top.roundToInt(),
            visualRect.right.roundToInt(),
            visualRect.bottom.roundToInt())
        drawable.draw(canvas)
    }

    fun drawForeground(canvas: Canvas) {
        val visualRect = keyLayout.visualRect
        paint.color = if (pressed) pressedColor else normalColor
        canvas.drawText(keyInfo.text, visualRect.centerX(), visualRect.centerY(), paint)
    }

    val touchRect get() =  keyLayout.touchRect

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