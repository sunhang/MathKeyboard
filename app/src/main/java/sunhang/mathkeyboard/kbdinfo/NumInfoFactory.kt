package sunhang.mathkeyboard.kbdinfo

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.CODE_BACK
import sunhang.mathkeyboard.CODE_ENTER
import sunhang.mathkeyboard.CODE_SWITCH_SYMBOL
import sunhang.mathkeyboard.KEYCODE_DELETE
import sunhang.mathkeyboard.tools.dp2Px

class NumInfoFactory(context: Context, kbdWidth: Int, kbdHeight: Int) : InfoFactory(context, kbdWidth, kbdHeight) {
    override val keyHorPadding: Float = 0f
    override val keyVerPadding: Float = 0f
    private val mGeneralTextSize = 23f

    object NumKbdRatio {
        val WIDTHS = floatArrayOf(0.167f, 0.222f, 0.222f, 0.222f, 0.167f)
//        val LAST_WIDTHS = floatArrayOf(0.167f, 0.1983125f, 0.2693749999999999f, 0.1983125f, 0.167f)
    }

    override fun createKeyboardInfo(keyboardWidth: Int, keyboardHeight: Int): KbdInfo.KeyboardInfo {
        val paddingTop = dp2Px(3.toFloat())
        val paddingLeft = dp2Px(3.toFloat())
        val keyHeight = (keyboardHeight - paddingTop - paddingTop) / 4

        val kbdBuilder = KbdInfo.KeyboardInfo.newBuilder()

        val planeWidth = keyboardWidth - paddingLeft - paddingLeft
        val xoffset = planeWidth * NumKbdRatio.WIDTHS[0] + paddingLeft
        val widthRatio = floatArrayOf(
            NumKbdRatio.WIDTHS[1],
            NumKbdRatio.WIDTHS[2],
            NumKbdRatio.WIDTHS[3],
            NumKbdRatio.WIDTHS[4]
        )

        var yOffset = paddingTop
        // 第一排
        buildRow(
            kbdBuilder,
            intArrayOf('1'.toInt(), '2'.toInt(), '3'.toInt(), KEYCODE_DELETE), null,
            arrayOf("1", "2", "3", ""),
            arrayOf<KbdInfo.KeyType>(KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.FUNCTION),
            widthRatio,
            arrayOf<KbdInfo.KeyColor>(KbdInfo.KeyColor.COLOR_NORMAL,
                KbdInfo.KeyColor.COLOR_NORMAL,
                KbdInfo.KeyColor.COLOR_NORMAL,
                KbdInfo.KeyColor.SPECIAL),
            planeWidth, keyHeight, xoffset, yOffset
        )
        // 第二排
        yOffset += keyHeight
        buildRow(
            kbdBuilder,
            intArrayOf('4'.toInt(), '5'.toInt(), '6'.toInt(), '.'.toInt()), null,
            arrayOf("4", "5", "6", "."),
            arrayOf<KbdInfo.KeyType>(KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.NORMAL),
            widthRatio,
            arrayOf<KbdInfo.KeyColor>(KbdInfo.KeyColor.COLOR_NORMAL,
                KbdInfo.KeyColor.COLOR_NORMAL,
                KbdInfo.KeyColor.COLOR_NORMAL,
                KbdInfo.KeyColor.SPECIAL),
            planeWidth, keyHeight, xoffset, yOffset
        )
        // 第三排
        yOffset += keyHeight
        buildRow(
            kbdBuilder,
            intArrayOf('7'.toInt(), '8'.toInt(), '9'.toInt(), '@'.toInt()), null,
            arrayOf("7", "8", "9", "@"),
            arrayOf<KbdInfo.KeyType>(KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.NORMAL),
            widthRatio,
            arrayOf<KbdInfo.KeyColor>(KbdInfo.KeyColor.COLOR_NORMAL,
                KbdInfo.KeyColor.COLOR_NORMAL,
                KbdInfo.KeyColor.COLOR_NORMAL,
                KbdInfo.KeyColor.SPECIAL),
            planeWidth, keyHeight, xoffset, yOffset
        )

        val lastRowWidthRatio = floatArrayOf(
            NumKbdRatio.WIDTHS[0],
            NumKbdRatio.WIDTHS[1],
            NumKbdRatio.WIDTHS[2],
            NumKbdRatio.WIDTHS[3],
            NumKbdRatio.WIDTHS[4]
        )
        // 第四排
        yOffset += keyHeight
        buildRow(
            kbdBuilder,
            intArrayOf(
                CODE_SWITCH_SYMBOL,
                CODE_BACK,
                '0'.toInt(),
                ' '.toInt(),
                CODE_ENTER
            ),
            floatArrayOf(20f, mGeneralTextSize, mGeneralTextSize, mGeneralTextSize, 17f),
            arrayOf("符", "", "0", "", ""),
            arrayOf<KbdInfo.KeyType>(KbdInfo.KeyType.FUNCTION,
                KbdInfo.KeyType.FUNCTION,
                KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.NORMAL,
                KbdInfo.KeyType.FUNCTION),
            lastRowWidthRatio,
            arrayOf<KbdInfo.KeyColor>(KbdInfo.KeyColor.SPECIAL,
                KbdInfo.KeyColor.SPECIAL,
                KbdInfo.KeyColor.COLOR_NORMAL,
                KbdInfo.KeyColor.SPECIAL,
                KbdInfo.KeyColor.HIGHLIGHT),
            planeWidth, keyHeight, paddingLeft, yOffset
        )

        return kbdBuilder.build()
    }

    protected fun buildRow(
        keyboardBuilder: KbdInfo.KeyboardInfo.Builder,
        codes: IntArray,
        textSizes: FloatArray?,
        texts: Array<String>,
        keyTypes: Array<KbdInfo.KeyType>,
        keyWidthRatio: FloatArray,
        keyColors: Array<KbdInfo.KeyColor>,
        keyboardWidth: Float,
        keyHeight: Float,
        xOffset: Float,
        yOffset: Float
    ) {
        var x = xOffset
        for (i in codes.indices) {
            val keyWidth = keyboardWidth * keyWidthRatio[i]
            val textSize: Float
            if (textSizes == null || i >= textSizes.size) {
                textSize = mGeneralTextSize
            } else {
                textSize = textSizes[i]
            }

            keyboardBuilder.addKeys(
                buildKeyInfo(
                    code = codes[i],
                    text = texts[i],
                    textSize = textSize,
                    keyType = keyTypes[i],
                    x = x,
                    y = yOffset,
                    keyWidth = keyWidth,
                    keyHeight = keyHeight,
                    keyColor = keyColors[i],
                    keyHorPadding = keyHorPadding,
                    keyVerPadding = keyVerPadding
                )
            )

            x += keyWidth
        }
    }

}
