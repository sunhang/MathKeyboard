package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import protoinfo.KbdInfo

open class Key(private val context: Context, private val keyInfo: KbdInfo.KeyInfo) {
    private val paint = Paint()
    private val keyLayout = KeyLayout(keyInfo.rectInfo)
    private var pressed = false
    var onKeyClickedListener: OnKeyClickedListener? = null

    init {
        paint.color = Color.DKGRAY
        paint.textSize = keyInfo.textSize
    }

    fun getMainCode() = keyInfo.getMainCode()

    open fun onDraw(canvas: Canvas) {
        val visualRect = keyLayout.visualRect
        canvas.drawText(keyInfo.text, visualRect.centerX(), visualRect.centerY(), paint)

        paint.color = if (pressed) Color.BLACK else Color.WHITE
        canvas.drawRect(keyLayout.visualRect, paint)
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
        onKeyClickedListener?.onClick(getMainCode(), this)
    }

    interface OnKeyClickedListener {
        fun onClick(code: Int, key: Key)
    }
}