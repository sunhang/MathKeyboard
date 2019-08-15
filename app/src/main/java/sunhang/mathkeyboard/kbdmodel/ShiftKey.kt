package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import protoinfo.KbdInfo
import sunhang.mathkeyboard.R

class ShiftKey(context: Context, keyInfo: KbdInfo.KeyInfo) : IconKey(context, keyInfo) {
    private val unshiftIcon = ContextCompat.getDrawable(context, R.drawable.key_unshift)!!
    private val shiftIcon = ContextCompat.getDrawable(context, R.drawable.key_shift)!!
    private val shiftLockedIcon = ContextCompat.getDrawable(context, R.drawable.key_shift_locked)!!

    lateinit var shiftStateListener: ShiftStateListener

    init {
        onKeyClickedListener = object : OnKeyClickedListener {
            override fun onClick(code: Int, key: Key) {
                // 切换状态
                shiftState = when(shiftState) {
                    ShiftState.UNSHIFT -> ShiftState.SHIFT
                    ShiftState.SHIFT -> ShiftState.SHIFT_LOCKED
                    ShiftState.SHIFT_LOCKED -> ShiftState.UNSHIFT
                }

                shiftStateListener.onChanged(shiftState)
            }
        }
    }

    fun resetShiftState() {
        shiftState = ShiftState.UNSHIFT
    }

    private var shiftState = ShiftState.UNSHIFT

    override fun getCurrentIcon(): Drawable {
        return when(shiftState) {
            ShiftState.UNSHIFT -> unshiftIcon
            ShiftState.SHIFT -> shiftIcon
            ShiftState.SHIFT_LOCKED -> shiftLockedIcon
        }
    }

    interface ShiftStateListener {
        fun onChanged(shiftState: ShiftState)
    }

}