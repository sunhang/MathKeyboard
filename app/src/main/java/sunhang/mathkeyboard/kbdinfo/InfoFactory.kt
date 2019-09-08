package sunhang.mathkeyboard.kbdinfo

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.R

abstract class InfoFactory(protected val context: Context, private val kbdWidth: Int, private val kbdHeight: Int) {
    protected open val keyHorPadding: Float = context.resources.getDimensionPixelSize(R.dimen.key_horizontal_padding).toFloat()
    protected open val keyVerPadding: Float = context.resources.getDimensionPixelSize(R.dimen.key_vertical_padding).toFloat()

    fun create() = createKeyboardInfo(kbdWidth, kbdHeight)
    protected abstract fun createKeyboardInfo(keyboardWidth: Int, keyboardHeight: Int): KbdInfo.KeyboardInfo
}