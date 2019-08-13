package sunhang.mathkeyboard.kbdviews

import android.content.Context
import android.graphics.Canvas
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

        keyboard?.let {
            drawKeyboard(canvas, it)
        }
    }

    private fun drawKeyboard(canvas: Canvas, keyboard: Keyboard) {
        keyboard.keys.forEach {
            it.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}