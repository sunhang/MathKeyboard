package sunhang.mathkeyboard.touch

import android.view.MotionEvent
import sunhang.mathkeyboard.kbdmodel.TouchEvent

class DefaultState(private val touchContext: TouchContext) {
    fun handle(event: MotionEvent) {
        // 获取当前要使用的pointerId
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val newPointerIndex = getNewPointerIndex(event)
                val newPointerId = getNewPointerId(event)
                val x = event.getX(newPointerIndex)
                val y = event.getY(newPointerIndex)

                val newKey = touchContext.hitKey(x, y)
                newKey.onTouch(TouchEvent(TouchEvent.DOWN, event, x, y))

                touchContext.currentPointerId = newPointerId
                touchContext.currentTargetKey = newKey
                touchContext.startX = x
                touchContext.startY = y
                touchContext.touchState = TouchState.PRESSED_STATE
            }
            else -> touchContext.resetState()
        }
    }

}