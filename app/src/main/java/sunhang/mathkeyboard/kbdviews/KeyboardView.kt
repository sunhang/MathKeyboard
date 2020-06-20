package sunhang.mathkeyboard.kbdviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.opengl.GLSurfaceView
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import sunhang.mathkeyboard.kbdmodel.Keyboard
import sunhang.mathkeyboard.kbdmodel.nullobj.NullKeyboard
import sunhang.mathkeyboard.touch.TouchContext
import sunhang.openlibrary.uiLazy

class KeyboardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var keyboard: Keyboard = NullKeyboard
    private var touchContext: TouchContext? = null

    fun updateData(keyboard: Keyboard) {
        this.keyboard = keyboard
        touchContext = TouchContext(context, keyboard)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }

        drawKeyboard(canvas, keyboard)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        try {
            touchContext?.let {
                it.dispatch(event)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            touchContext = TouchContext(context, keyboard)
        }

        invalidate()
//        requestLayout()

        return true
    }

    private fun drawKeyboard(canvas: Canvas, keyboard: Keyboard) {
        keyboard.keys.forEach {
            it.onDraw(canvas)
        }
    }
}