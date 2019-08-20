package sunhang.mathkeyboard.kbdinfo

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.*
import sunhang.mathkeyboard.tools.dp2Px
import java.lang.RuntimeException

class MathSymbolInfo1Factory(context: Context, kbdWidth: Int, kbdHeight: Int) :
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

        val columnCounts = arrayOf(10, 10, 10, 2, 0)

        val codes = arrayOf(
            toIntArray('（', '）', '(', ')', '【', '】', '[', ']', '｛', '｝'),
            toIntArray('θ', 'Δ', '∥', '⊥', '∠', '⌒', '⊕', '⊙', 'Ⅱ', '‖'),
            toIntArray('√', '⇒', '⇔', '∀', '∃', '∏', '⊆', '⊇', '⊂', '⊃'),
            toIntArray(CODE_PRE_PAGE, KEYCODE_DELETE),
            toIntArray()
        )

        val texts = arrayOf(
            arrayOf("（", "）", "(", ")", "【", "】", "[", "]", "｛", "｝"),
            arrayOf("θ", "Δ", "∥", "⊥", "∠", "⌒", "⊕", "⊙", "Ⅱ", "‖"),
            arrayOf("√", "⇒", "⇔", "∀", "∃", "∏", "⊆", "⊇", "⊂", "⊃"),
            arrayOf("2/2", ""),
            arrayOf()
        )

        val keyWidths = arrayOf(
            FloatArray(10) { keyWidth },
            FloatArray(10) { keyWidth },
            FloatArray(10) { keyWidth },
            FloatArray(2) { keyWidth * 1.5f },
            FloatArray(0)
        )

        val horStrips = arrayOf(
            FloatArray(10) { keyWidth },
            FloatArray(10) { keyWidth },
            FloatArray(10) { keyWidth },
            FloatArray(2) {
                when (it) {
                    0 -> keyWidth * 8.5f
                    1 -> keyWidth * 1.5f
                    else -> 0.0f
                }
            },
            FloatArray(0)
        )

        val keyTypes = arrayOf(
            Array(10) { KbdInfo.KeyType.NORMAL },
            Array(10) { KbdInfo.KeyType.NORMAL },
            Array(10) { KbdInfo.KeyType.NORMAL },
            Array(2) { KbdInfo.KeyType.FUNCTION },
            arrayOf<KbdInfo.KeyType>()
        )

        val keyColors = arrayOf(
            Array(10) { KbdInfo.KeyColor.COLOR_NORMAL },
            Array(10) { KbdInfo.KeyColor.COLOR_NORMAL },
            Array(10) { KbdInfo.KeyColor.COLOR_NORMAL },
            Array(2) { KbdInfo.KeyColor.SPECIAL },
            arrayOf<KbdInfo.KeyColor>()
        )

        val keyTextSizes = arrayOf(
            FloatArray(10) {
                // 为了区分中文英文小括号
                when (it) {
                    0, 1 -> 25.0f
                    else -> 21.0f
                }
            },
            FloatArray(10) { 23.0f },
            FloatArray(10) { 23.0f },
            FloatArray(2) {
                when (it) {
                    0 -> 20.0f
                    else -> 23.0f
                }
            },
            FloatArray(0)
        )

        // 打算用两页


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