package sunhang.mathkeyboard.kbdviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import sunhang.mathkeyboard.kbdmodel.Keyboard

class KeyboardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var keyboard: Keyboard? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }

        drawBackground(canvas)
        keyboard?.let {
            drawKeyboard(canvas, it)
        }
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
    }

    private fun drawKeyboard(canvas: Canvas, keyboard: Keyboard) {
        keyboard.keys.forEach {
            it.onDraw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}