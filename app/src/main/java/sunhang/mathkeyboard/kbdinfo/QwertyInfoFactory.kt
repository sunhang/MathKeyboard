package sunhang.mathkeyboard.kbdinfo

import android.content.Context
import android.util.Pair
import protoinfo.KbdInfo
import sunhang.mathkeyboard.KEYCODE_DELETE
import sunhang.mathkeyboard.ZH_COMMA
import sunhang.mathkeyboard.ZH_PERIOD
import sunhang.mathkeyboard.tools.dp2Px
import sunhang.mathkeyboard.tools.isEnter

abstract class QwertyInfoFactory(context: Context, kbdWidth: Int, kbdHeight: Int) : InfoFactory(context, kbdWidth, kbdHeight) {

    protected abstract val firstRow: Pair<IntArray, Array<String>>
    protected abstract val firstRowHint: Pair<IntArray, Array<String>>
    protected abstract val secondRow: Pair<IntArray, Array<String>>
    protected abstract val secondRowHint: Pair<IntArray, Array<String>>
    protected abstract val hintTextSize: Float
    protected abstract val hintTextSizeUpperCase: Float
    protected abstract val shiftPlaceCodeAndText: Pair<Int, String>
    protected abstract val thirdRow: Pair<IntArray, Array<String>>
    protected abstract val thirdRowHint: Pair<IntArray, Array<String>>
    protected abstract val fourthRowCodeTextWidthRatio: Triple<IntArray, Array<String>, FloatArray>
    protected abstract val baseLine: Float
    protected abstract val upperCaseBaseLine: Float
    protected abstract val hintBaseLine: Float
    protected abstract val hintUpperCaseBaseLine: Float
    protected abstract fun getTextSize(code: Int): Float
    protected abstract fun getTextSizeUpperCase(code: Int): Float
    protected abstract fun setupBaseLine(keyHeight: Float)


    override fun createKeyboardInfo(keyboardWidth: Int, keyboardHeight: Int): KbdInfo.KeyboardInfo {
        val paddingLeft = dp2Px(2.5f)
        val paddingRight = dp2Px(2.5f)
        val paddingTop = 0.0f
        val paddingBottom = 0.0f
        val keyWidth = (keyboardWidth - paddingLeft - paddingRight) / 10.0f
        val keyHeight = (keyboardHeight - paddingTop - paddingBottom) / 4.0f

        setupBaseLine(keyHeight)

        val builder = KbdInfo.KeyboardInfo.newBuilder()

        // 第一排
        var yOffset = paddingTop
        buildRow(
            builder, firstRow, getTextSize(0), getTextSizeUpperCase(0), baseLine, upperCaseBaseLine,
            firstRowHint, hintTextSize, hintBaseLine, hintUpperCaseBaseLine,
            keyWidth, keyHeight, paddingLeft, yOffset, null,
            keyHorPadding, keyVerPadding
        )
        // 第二排
        yOffset += keyHeight
        buildRow(
            builder, secondRow, getTextSize(0), getTextSizeUpperCase(0), baseLine, upperCaseBaseLine,
            secondRowHint, hintTextSize, hintBaseLine, hintUpperCaseBaseLine,
            keyWidth, keyHeight, paddingLeft + keyWidth / 2, yOffset, null,
            keyHorPadding, keyVerPadding
        )
        // 第三排
        yOffset += keyHeight
        buildThirdRow(builder, keyWidth, keyHeight, paddingLeft, yOffset)
        // 第四排
        yOffset += keyHeight
        buildFourthRow(builder, keyWidth, keyHeight, paddingLeft, yOffset)

        return builder.build()
    }

    private fun buildThirdRow(
        kbdInfoBuilder: KbdInfo.KeyboardInfo.Builder,
        keyWidth: Float,
        keyHeight: Float,
        paddingLeft: Float,
        yOffset: Float
    ) {
        // shiftkey
        //        kbdInfoBuilder.addProtoKeys(buildKeyInfo(Constants.KEYCODE_SHIFT, "", KbdInfo.KeyType.FUNCTION, 0, yOffset, keyWidth * 1.5f, keyHeight));
        val pair = shiftPlaceCodeAndText
        kbdInfoBuilder.addKeys(
            buildKeyInfo(
                code = pair.first,
                text = pair.second,
                textSize = getTextSize(pair.first),
                keyType = KbdInfo.KeyType.FUNCTION,
                x = paddingLeft,
                y = yOffset,
                keyWidth = keyWidth * 1.5f,
                keyHeight = keyHeight,
                keyColor = KbdInfo.KeyColor.SPECIAL,
                keyHorPadding = keyHorPadding,
                keyVerPadding = keyVerPadding
            )
        )

        buildRow(
            kbdInfoBuilder, thirdRow, getTextSize(0), getTextSizeUpperCase(0), baseLine, upperCaseBaseLine,
            thirdRowHint, hintTextSize, hintBaseLine, hintUpperCaseBaseLine,
            keyWidth, keyHeight, paddingLeft + keyWidth * 1.5f, yOffset, null, keyHorPadding, keyVerPadding
        )

        // deletekey
        kbdInfoBuilder.addKeys(
            buildKeyInfo(
                code = KEYCODE_DELETE,
                text = "",
                textSize = getTextSize(0),
                keyType = KbdInfo.KeyType.FUNCTION,
                x = paddingLeft + keyWidth * 8.5f,
                y = yOffset,
                keyWidth = keyWidth * 1.5f,
                keyHeight = keyHeight,
                keyColor = KbdInfo.KeyColor.SPECIAL,
                keyHorPadding = keyHorPadding,
                keyVerPadding = keyVerPadding
            )
        )
    }


    private fun buildFourthRow(
        kbdInfoBuilder: KbdInfo.KeyboardInfo.Builder,
        keyWidth: Float,
        keyHeight: Float,
        paddingLeft: Float,
        yOffset: Float
    ) {
        var xOffset = paddingLeft
        var realKeyWidth: Float

        val triple = fourthRowCodeTextWidthRatio
        if (triple.first.size != triple.second.size || triple.second.size != triple.third.size) {
            throw RuntimeException()
        }

        val comma: Int = ','.toInt()
        val period: Int = '.'.toInt()

        val len = triple.first.size
        for (i in 0 until len) {
            val code = triple.first[i]
            val text = triple.second[i]
            val ratio = triple.third[i]

            realKeyWidth = keyWidth * ratio

            val keyType = if (code < 0) KbdInfo.KeyType.FUNCTION else KbdInfo.KeyType.NORMAL
            val keyColor: KbdInfo.KeyColor
            if (isEnter(code)) {
                keyColor = KbdInfo.KeyColor.HIGHLIGHT
            } else {
                keyColor = if (code < 0) KbdInfo.KeyColor.SPECIAL else KbdInfo.KeyColor.COLOR_NORMAL
            }

            val baseLine = if (code == comma || code == period || code == ZH_COMMA || code == ZH_PERIOD) {
                0.58f
            } else {
                0f
            }

            kbdInfoBuilder.addKeys(
                buildKeyInfo(
                    code = code,
                    text = text,
                    textSize = getTextSize(code),
                    baseLine = baseLine,
                    keyType = keyType,
                    x = xOffset,
                    y = yOffset,
                    keyWidth = realKeyWidth,
                    keyHeight = keyHeight,
                    keyColor = keyColor,
                    keyHorPadding = keyHorPadding,
                    keyVerPadding = keyVerPadding
                )
            )

            xOffset += realKeyWidth
        }
    }
}
