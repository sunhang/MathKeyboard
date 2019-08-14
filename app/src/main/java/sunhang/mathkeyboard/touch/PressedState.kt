package sunhang.mathkeyboard.touch

import android.view.MotionEvent
import sunhang.mathkeyboard.kbdmodel.Key
import sunhang.mathkeyboard.kbdmodel.NullKey
import sunhang.mathkeyboard.kbdmodel.TouchEvent

class PressedState(private val touchContext: TouchContext) {
    fun handle(event: MotionEvent) {
        val actionMasked = event.actionMasked

        // 获取当前要使用的pointerId
        when (actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                handleActionPointerDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                handleActionMove(event)
            }
            MotionEvent.ACTION_UP -> {
                handleActionUp(event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                handleActionPointerUp(event)
            }
            MotionEvent.ACTION_CANCEL -> {
                handleActionCancel(event)
            }
        }
    }

    private fun handleActionPointerDown(event: MotionEvent) {
        val oldKey = touchContext.currentTargetKey

        val newPointerIndex = getNewPointerIndex(event)
        val newPointerId = getNewPointerId(event)

        val x = event.getX(newPointerIndex)
        val y = event.getY(newPointerIndex)

        val newKey = touchContext.hitKey(x, y)
        if (newKey === touchContext.currentTargetKey) {
            newKey.onTouch(TouchEvent(TouchEvent.MOVE, event, x, y)) // 使用move
        } else {
            oldKey.onTouch(TouchEvent(TouchEvent.UP, event, x, y)) // 响应一个key
            newKey.onTouch(TouchEvent(TouchEvent.DOWN, event, x, y))
        }

        touchContext.startX = x
        touchContext.startY = y
        touchContext.currentPointerId = newPointerId
        touchContext.currentTargetKey = newKey
    }

    private fun handleActionMove(event: MotionEvent) {
        val oldKey = touchContext.currentTargetKey

        val pointerIndex = event.findPointerIndex(touchContext.currentPointerId)
        val x = event.getX(pointerIndex)
        val y = event.getY(pointerIndex)

        val newKey = touchContext.hitKey(x, y)
        val keyMotion = if (oldKey === newKey) TouchEvent.MOVE else TouchEvent.DOWN
        touchContext.dispatchKeyEvent(oldKey, newKey, keyMotion, event, x, y)
        touchContext.currentTargetKey = newKey
    }

    private fun handleActionUp(event: MotionEvent) {
        val oldKey = touchContext.currentTargetKey

        val pointerIndex = event.findPointerIndex(touchContext.currentPointerId)
        val x = event.getX(pointerIndex)
        val y = event.getY(pointerIndex)

        val newKey = touchContext.hitKey(x, y)
        touchContext.dispatchKeyEvent(oldKey, newKey, TouchEvent.UP, event, x, y)

        touchContext.resetState()
    }

    private fun handleActionPointerUp(event: MotionEvent) {
        val oldKey = touchContext.currentTargetKey

        val newPointerIndex = getNewPointerIndex(event)
        val newPointerId = getNewPointerId(event)

        val x = event.getX(newPointerIndex)
        val y = event.getY(newPointerIndex)

        if (newPointerId == touchContext.currentPointerId) {
            val newKey = touchContext.hitKey(x, y)
            touchContext.dispatchKeyEvent(oldKey, newKey, TouchEvent.UP, event, x, y)

            touchContext.resetState()
        }
    }

    private fun handleActionCancel(event: MotionEvent) {
        val oldKey = touchContext.currentTargetKey

        var newKey: Key
        var x = -1f
        var y = -1f

        // 有时候，这个ACTION_CANCEL是自己的程序模拟出来的发出来的
        try {
            val pointerIndex = event.findPointerIndex(touchContext.currentPointerId)
            x = event.getX(pointerIndex)
            y = event.getY(pointerIndex)

            newKey = touchContext.hitKey(x, y)
        } catch (e: Exception) {
            e.printStackTrace()

            newKey = NullKey
        }

        touchContext.dispatchKeyEvent(oldKey, newKey, TouchEvent.CANCEL, event, x, y)

        touchContext.resetState()
    }
}