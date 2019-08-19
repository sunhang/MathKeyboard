package sunhang.mathkeyboard.kbdinfo

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.tools.dp2Px

class MathSymbolInfoFactory(context: Context, kbdWidth: Int, kbdHeight: Int) :
    InfoFactory(context, kbdWidth, kbdHeight) {
    override fun createKeyboardInfo(keyboardWidth: Int, keyboardHeight: Int): KbdInfo.KeyboardInfo {
        val codes = arrayListOf<CharArray>(
            charArrayOf('≈', '≡', '≠', '＝', '≤', '≥', '＜', '＞', '≮', '≯'),
            charArrayOf('∷', '±', '＋', '－', '×', '÷', '／', '∫', '∮', '∝'),
            charArrayOf('∞', '∧', '∨', '∑', '∏', '∪', '∩', '∈', '∵', '∴'),
            charArrayOf('e', '⊥', 'Ⅱ', '‖', '∠', '⌒', '≌', '∽', '√', 'Ⅰ'),
//            charArrayOf('（', '）', '【', '】', '｛', '｝', '⊕', '⊙'),
            charArrayOf('α', 'β', 'γ', 'δ', 'ε', 'ζ', 'η', 'θ', 'Δ', '∥')
        )

        val paddingLeft = dp2Px(2.5f)
        val paddingRight = dp2Px(2.5f)
        val paddingTop = 0.0f
        val paddingBottom = 0.0f
        val keyWidth = (keyboardWidth - paddingLeft - paddingRight) / 10.0f
        val keyHeight = (keyboardHeight - paddingTop - paddingBottom) / codes.size

        // 0的话，会居中显示
        val baseLine = 0.0f
        val textSize = 23.0f

        val builder = KbdInfo.KeyboardInfo.newBuilder()

        var yOffset = paddingTop
        codes.forEachIndexed { rowIndex, chars ->
            var xOffset = paddingLeft

            chars.forEachIndexed { columnIndex, c ->
                buildKeyInfo(
                    code = c.toInt(),
                    text = c.toString(),
                    textSize = textSize,
                    baseLine = baseLine,
                    keyType = KbdInfo.KeyType.NORMAL,
                    x = xOffset,
                    y = yOffset,
                    keyWidth = keyWidth,
                    keyHeight = keyHeight,
                    keyColor = KbdInfo.KeyColor.COLOR_NORMAL,
                    keyHorPadding = keyHorPadding,
                    keyVerPadding = keyVerPadding
                ).let {
                    builder.addKeys(it)
                }
                xOffset += keyWidth
            }

            yOffset += keyHeight
        }

        return builder.build()
    }
}