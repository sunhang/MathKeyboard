package sunhang.mathkeyboard.touch

import android.view.MotionEvent


fun getNewPointerIndex(me: MotionEvent): Int {
   return (me.action and MotionEvent.ACTION_POINTER_INDEX_MASK).shr(MotionEvent.ACTION_POINTER_INDEX_SHIFT)
}

fun getNewPointerId(me: MotionEvent): Int {
    return me.getPointerId(getNewPointerIndex(me))
}
