package sunhang.mathkeyboard.touch

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import protoinfo.KbdInfo
import sunhang.mathkeyboard.kbdmodel.Key
import sunhang.mathkeyboard.kbdmodel.Keyboard
import sunhang.mathkeyboard.kbdmodel.NullKey
import sunhang.mathkeyboard.kbdmodel.TouchEvent

class TouchContext(private val context: Context, private val keyboard: Keyboard) {
    var currentTargetKey: Key = NullKey
    var currentPointerId = -1
    var touchState = TouchState.DEFAULT_STATE
    var startX: Float = 0.toFloat()
    var startY:Float = 0.toFloat()
    private var pressedState: PressedState? = null
    private var defaultState: DefaultState? = null

    fun hitKey(x: Float, y: Float): Key {
        return keyboard.keys.find { it.touchRect.contains(x, y) } ?: NullKey
    }

    fun resetState() {
        currentTargetKey = NullKey
        currentPointerId = -1

        startX = -1f
        startY = -1f

        touchState = TouchState.DEFAULT_STATE
    }

    fun dispatchKeyEvent(oldKey: Key, newKey: Key, keyMotion: Int, me: MotionEvent, x: Float, y: Float) {
        if (newKey !== oldKey) {
            oldKey.onTouch(TouchEvent(TouchEvent.TAKEN_AWAY, me, x, y))
        }

        newKey.onTouch(TouchEvent(keyMotion, me, x, y))
    }
}
