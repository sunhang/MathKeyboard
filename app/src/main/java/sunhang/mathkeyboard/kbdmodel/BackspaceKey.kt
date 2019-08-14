package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.Canvas
import protoinfo.KbdInfo

class BackspaceKey(context: Context, keyInfo: KbdInfo.KeyInfo) : Key(context, keyInfo) {
    override fun onDraw(canvas: Canvas) {
    }

    protected fun invokeOnRepeatedClickListener(first: Boolean) {
        (onKeyClickedListener as? OnBackspaceKeyClickedListener)?.onRepeatedClick(getMainCode(), first, this)
    }

    interface OnBackspaceKeyClickedListener : OnKeyClickedListener{
        fun onRepeatedClick(code: Int, first: Boolean, key: Key)
    }
}