package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.tools.sp2Px

class UpperCaseSupportedKey(context: Context, keyInfo: KbdInfo.KeyInfo) : Key(context, keyInfo) {
    var upperCase: Boolean = false

    override val textSize: Float
        get() = if (upperCase) sp2Px(keyInfo.textSizeUpperCase) else super.textSize

    override val text: String
        get() = if (upperCase) super.text.toUpperCase() else super.text

    override val baseLineRatio: Float
        get() = if (upperCase) keyInfo.upperCaseBaseLine else super.baseLineRatio

    /**
     * 是kotlin妈蛋还是我悟性不行，override的var大概率是奔着要重写set，重写了set，就不能声明lateinit了，
     * 不能lateinit了，就得给该var赋初始值，于是就赋了这个[NullOnKeyClickedListener]。
     */
    override var onKeyClickedListener: OnKeyClickedListener = NullOnKeyClickedListener()
        get() = super.onKeyClickedListener
        set(value) {
            field = value
            super.onKeyClickedListener = object : OnKeyClickedListener {
                override fun onClick(code: Int, key: Key) {
                    val transformedCode = if (upperCase) {
                        code - 0x20
                    } else {
                        code
                    }

                    field.onClick(transformedCode, key)
                }
            }
        }

    class NullOnKeyClickedListener : OnKeyClickedListener {
        override fun onClick(code: Int, key: Key) {}
    }
}