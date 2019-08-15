package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import androidx.core.content.ContextCompat
import protoinfo.KbdInfo
import sunhang.mathkeyboard.R

class BackspaceKey(context: Context, keyInfo: KbdInfo.KeyInfo) : IconKey(context, keyInfo) {
    private val icon = ContextCompat.getDrawable(context, R.drawable.key_delete)!!

    override fun getCurrentIcon() = icon

    protected fun invokeOnRepeatedClickListener(first: Boolean) {
        (onKeyClickedListener as? OnBackspaceKeyClickedListener)?.onRepeatedClick(getMainCode(), first, this)
    }

    interface OnBackspaceKeyClickedListener : OnKeyClickedListener{
        fun onRepeatedClick(code: Int, first: Boolean, key: Key)
    }
}