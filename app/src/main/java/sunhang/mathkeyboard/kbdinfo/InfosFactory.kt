package sunhang.mathkeyboard.kbdinfo

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.R

abstract class InfosFactory(protected val context: Context) {
    protected val keyHorPadding: Float = context.resources.getDimensionPixelSize(R.dimen.key_horizontal_padding).toFloat()
    protected val keyVerPadding: Float = context.resources.getDimensionPixelSize(R.dimen.key_vertical_padding).toFloat()

    abstract fun createKeyboardInfo(keyboardWidth: Int, keyboardHeight: Int): KbdInfo.KeyboardInfo
}