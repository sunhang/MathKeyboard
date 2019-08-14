package sunhang.mathkeyboard.kbdmodel

import android.view.MotionEvent

data class TouchEvent(
    val keyMotion: Int,
    val motionEvent: MotionEvent,
    val x: Float,
    val y: Float
) {
    companion object {
        const val DOWN = 0
        const val MOVE = 1
        const val UP = 2
        const val CANCEL = 3
        const val TAKEN_AWAY = 4 // 消息被别的key夺走
    }
}