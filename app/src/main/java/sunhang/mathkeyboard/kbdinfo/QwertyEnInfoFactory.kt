package sunhang.mathkeyboard.kbdinfo

import android.content.Context
import android.util.Pair
import sunhang.mathkeyboard.*
import sunhang.mathkeyboard.tools.isEnter
import sunhang.mathkeyboard.tools.isSwitchCode
import sunhang.mathkeyboard.tools.sp2Px
import sunhang.openlibrary.isPortrait

class QwertyEnInfoFactory(context: Context, kbdWidth: Int, kbdHeight: Int) : QwertyInfoFactory(context, kbdWidth, kbdHeight) {
    override var baseLine: Float = 0.toFloat()
    override var upperCaseBaseLine: Float = 0.toFloat()
    override var hintBaseLine: Float = 0.toFloat()
    override var hintUpperCaseBaseLine: Float = 0.toFloat()

    override val firstRowHint: Pair<IntArray, Array<String>>
        get() =
            Pair(
                intArrayOf(
                    '`'.toInt(),
                    '='.toInt(),
                    '+'.toInt(),
                    '$'.toInt(),
                    '…'.toInt(),
                    '"'.toInt(),
                    '^'.toInt(),
                    '['.toInt(),
                    ']'.toInt(),
                    '|'.toInt()
                ),
                arrayOf("`", "=", "+", "$", "…", "\"", "^", "[", "]", "|")
            )

    override val secondRowHint: Pair<IntArray, Array<String>>
        get() =
            Pair(
                intArrayOf(
                    '~'.toInt(),
                    '!'.toInt(),
                    '@'.toInt(),
                    '#'.toInt(),
                    '%'.toInt(),
                    '\''.toInt(),
                    '&'.toInt(),
                    '*'.toInt(),
                    '?'.toInt()
                ),
                arrayOf("~", "!", "@", "#", "%", "\"", "&", "*", "?")
            )

    override val hintTextSize: Float
        get() = 10f

    override val hintTextSizeUpperCase: Float
        get() = 10f

    override val thirdRowHint: Pair<IntArray, Array<String>>
        get() = Pair(
            intArrayOf('('.toInt(), ')'.toInt(), '-'.toInt(), '_'.toInt(), ':'.toInt(), ';'.toInt(), '/'.toInt()),
            arrayOf("(", ")", "-", "_", ":", ";", "/")
        )

    override val firstRow: Pair<IntArray, Array<String>>
        get() =
            Pair(
                intArrayOf(
                    'q'.toInt(),
                    'w'.toInt(),
                    'e'.toInt(),
                    'r'.toInt(),
                    't'.toInt(),
                    'y'.toInt(),
                    'u'.toInt(),
                    'i'.toInt(),
                    'o'.toInt(),
                    'p'.toInt()
                ),
                arrayOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
            )

    override val secondRow: Pair<IntArray, Array<String>>
        get() =
            Pair(
                intArrayOf(
                    'a'.toInt(),
                    's'.toInt(),
                    'd'.toInt(),
                    'f'.toInt(),
                    'g'.toInt(),
                    'h'.toInt(),
                    'j'.toInt(),
                    'k'.toInt(),
                    'l'.toInt()
                ),
                arrayOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
            )

    override val thirdRow: Pair<IntArray, Array<String>>
        get() = Pair(
            intArrayOf('z'.toInt(), 'x'.toInt(), 'c'.toInt(), 'v'.toInt(), 'b'.toInt(), 'n'.toInt(), 'm'.toInt()),
            arrayOf("z", "x", "c", "v", "b", "n", "m")
        )

    override val shiftPlaceCodeAndText: Pair<Int, String>
        get() = Pair(KEYCODE_SHIFT, "")

    override val fourthRowCodeTextWidthRatio: Triple<IntArray, Array<String>, FloatArray>
        get() {
            return Triple(
                intArrayOf(
                    CODE_SWITCH_SYMBOL,
                    CODE_SWITCH_NUM_SODUKU,
                    EN_COMMA,
                    KEYCODE_SPACE,
                    EN_PERIOD,
                    CODE_SWITCH_MAIN,
                    CODE_ENTER
                ),
                arrayOf("符", "123", String(Character.toChars(EN_COMMA)), "", String(Character.toChars(EN_PERIOD)), "", ""),
                floatArrayOf(1.5f, 1.22f, 1f, 2.56f, 1f, 1.22f, 1.5f)
            )
        }

    override fun getTextSize(code: Int): Float {
        if (code == CODE_SWITCH_MAIN) {
            return 18f
        } else if (isSwitchCode(code)) {
            return 20f
        } else if (isEnter(code)) {
            return 17f
        }

        return DEFAULT_TEXTSIZE.toFloat()
    }

    override fun setupBaseLine(keyHeight: Float) {
        val visualHeight = keyHeight - keyVerPadding * 2
        baseLine = adjustBaseLine("abcd", visualHeight, sp2Px(DEFAULT_TEXTSIZE.toFloat()))
        upperCaseBaseLine = adjustBaseLine("ASDF", visualHeight,
            sp2Px(DEFAULT_UPPERCASE_TEXTSIZE.toFloat())
        )

        if (GlobalVariable.context.isPortrait) {
            hintBaseLine = 0.3f
            hintUpperCaseBaseLine = 0.3f
        } else {
            hintBaseLine = 0.4f
            hintUpperCaseBaseLine = 0.4f
        }
    }

    override fun getTextSizeUpperCase(code: Int): Float {
        return DEFAULT_UPPERCASE_TEXTSIZE.toFloat()
    }

    companion object {
        val DEFAULT_TEXTSIZE = 23
        val DEFAULT_UPPERCASE_TEXTSIZE = 20
    }
}
