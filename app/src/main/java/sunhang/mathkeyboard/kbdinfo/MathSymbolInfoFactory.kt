package sunhang.mathkeyboard.kbdinfo

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.*
import sunhang.mathkeyboard.tools.dp2Px
import java.lang.RuntimeException

class MathSymbolInfoFactory(context: Context, kbdWidth: Int, kbdHeight: Int) :
    InfoFactory(context, kbdWidth, kbdHeight) {

    @Suppress("MoveVariableDeclarationIntoWhen")
    private fun toIntArray(vararg args: Any): IntArray {
        return IntArray(args.size) {
            val elem = args[it]
            when (elem) {
                is Char -> elem.toInt()
                is Int -> elem
                else -> throw RuntimeException("incorrect args!")
            }
        }
    }

    override fun createKeyboardInfo(keyboardWidth: Int, keyboardHeight: Int): KbdInfo.KeyboardInfo {
        val rowCount = 5
        val paddingLeft = dp2Px(2.5f)
        val paddingRight = dp2Px(2.5f)
        val paddingTop = 0.0f
        val paddingBottom = 0.0f
        val keyWidth = (keyboardWidth - paddingLeft - paddingRight) / 10.0f
        val keyHeight = (keyboardHeight - paddingTop - paddingBottom) / rowCount

        val columnCounts = arrayOf(10, 10, 10, 9, 8)

        val codes = arrayOf(
            toIntArray('≈', '≡', '≠', '＝', '≤', '≥', '＜', '＞', '≮', '≯'),
            toIntArray('∷', '±', '＋', '－', '×', '÷', '／', '∫', '∮', '∝'),
            toIntArray('∞', '∧', '∨', '∑', '∏', '∪', '∩', '∈', '∵', '∴'),
            toIntArray(CODE_NEXT_PAGE, 'α', 'β', 'γ', 'δ', 'ε', 'ζ', 'η', KEYCODE_DELETE),
            toIntArray(CODE_SWITCH_EN_QWERTY, CODE_SWITCH_NUM_SODUKU, 'e', '≌', KEYCODE_SPACE, '∽', 'Ⅰ', CODE_ENTER)
        )

        val texts = arrayOf(
            arrayOf("≈", "≡", "≠", "＝", "≤", "≥", "＜", "＞", "≮", "≯"),
            arrayOf("∷", "±", "＋", "－", "×", "÷", "／", "∫", "∮", "∝"),
            arrayOf("∞", "∧", "∨", "∑", "∏", "∪", "∩", "∈", "∵", "∴"),
            arrayOf("1/2", "α", "β", "γ", "δ", "ε", "ζ", "η", ""),
            arrayOf("ABC", "123", "e", "≌", " ", "∽", "Ⅰ", "")
        )

        val keyWidths = arrayOf(
            FloatArray(10) { keyWidth },
            FloatArray(10) { keyWidth },
            FloatArray(10) { keyWidth },
            FloatArray(9) {
                when (it) {
                    0, 8 -> keyWidth * 1.5f
                    else -> keyWidth
                }
            },
            FloatArray(8) {
                when (it) {
                    0, 1, 4, 7 -> keyWidth * 1.5f
                    else -> keyWidth
                }
            }
        )

        val horStrips = keyWidths

        /*
        val horStrips = arrayOf(
            FloatArray(10) { keyWidth },
            FloatArray(10) { keyWidth },
            FloatArray(10) { keyWidth },
            FloatArray(9) {
                when (it) {
                    0, 8 -> keyWidth * 1.5f
                    7 -> keyWidth * 1.2f
                    else -> keyWidth
                }
            },
            FloatArray(8) {keyWidth }
        )*/

        val keyTypes = arrayOf(
            Array(10) { KbdInfo.KeyType.NORMAL },
            Array(10) { KbdInfo.KeyType.NORMAL },
            Array(10) { KbdInfo.KeyType.NORMAL },
            Array(9) {
                when (it) {
                    0, 8 -> KbdInfo.KeyType.FUNCTION
                    else -> KbdInfo.KeyType.NORMAL
                }
            },
            Array(8) {
                when (it) {
                    0, 1, 7 -> KbdInfo.KeyType.FUNCTION
                    else -> KbdInfo.KeyType.NORMAL
                }
            }
        )

        val keyColors = arrayOf(
            Array(10) { KbdInfo.KeyColor.COLOR_NORMAL },
            Array(10) { KbdInfo.KeyColor.COLOR_NORMAL },
            Array(10) { KbdInfo.KeyColor.COLOR_NORMAL },
            Array(9) {
                when (it) {
                    0, 8 -> KbdInfo.KeyColor.SPECIAL
                    else -> KbdInfo.KeyColor.COLOR_NORMAL
                }
            },
            Array(8) {
                when (it) {
                    0, 1, 7 -> KbdInfo.KeyColor.SPECIAL
                    else -> KbdInfo.KeyColor.COLOR_NORMAL
                }
            }
        )

        val keyTextSizes = arrayOf(
            FloatArray(10) { 23.0f },
            FloatArray(10) { 23.0f },
            FloatArray(10) { 23.0f },
            FloatArray(9) {
                when (it) {
                    0 -> 20.0f
                    else -> 23.0f
                }
            },
            FloatArray(8) {
                when (it) {
                    0, 1 -> 20.0f
                    7 -> 17.0f
                    else -> 23.0f
                }
            }
        )

        // 打算用两页
//        charArrayOf('（', '）', '【', '】', '｛', '｝', '⊕', '⊙')
//        charArrayOf('θ', 'Δ', '∥') '⊥', 'Ⅱ', '‖', '∠', '⌒', '√',


        // 0的话，会居中显示
        val baseLine = 0.0f
//        val textSize = 23.0f

        val builder = KbdInfo.KeyboardInfo.newBuilder()

        var yOffset = paddingTop

        (0 until rowCount).forEach { rowIndex ->
            var xOffset = paddingLeft

            (0 until columnCounts[rowIndex]).forEach { columnIndex ->
                buildKeyInfo(
                    code = codes[rowIndex][columnIndex],
                    text = texts[rowIndex][columnIndex],
                    textSize = keyTextSizes[rowIndex][columnIndex],
                    baseLine = baseLine,
                    keyType = keyTypes[rowIndex][columnIndex],
                    x = xOffset,
                    y = yOffset,
                    keyWidth = keyWidths[rowIndex][columnIndex],
                    keyHeight = keyHeight,
                    keyColor = keyColors[rowIndex][columnIndex],
                    keyHorPadding = keyHorPadding,
                    keyVerPadding = keyVerPadding
                ).let {
                    builder.addKeys(it)
                }
                xOffset += horStrips[rowIndex][columnIndex]
            }

            yOffset += keyHeight
        }

        return builder.build()
    }
}