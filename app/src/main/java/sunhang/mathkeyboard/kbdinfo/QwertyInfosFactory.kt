package sunhang.mathkeyboard.kbdinfo

import android.content.Context
import android.util.Pair
import protoinfo.KbdInfo
import sunhang.mathkeyboard.KEYCODE_DELETE
import sunhang.mathkeyboard.tools.dp2Px
import sunhang.mathkeyboard.tools.isEnter

abstract class QwertyInfosFactory(context: Context) : InfosFactory(context) {

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
                pair.first, pair.second, getTextSize(pair.first), KbdInfo.KeyType.FUNCTION,
                paddingLeft, yOffset, keyWidth * 1.5f, keyHeight, KbdInfo.KeyColor.SPECIAL,
                keyHorPadding, keyVerPadding
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
                KEYCODE_DELETE, "", getTextSize(0),
                KbdInfo.KeyType.FUNCTION,
                paddingLeft + keyWidth * 8.5f, yOffset, keyWidth * 1.5f, keyHeight,
                KbdInfo.KeyColor.SPECIAL,
                keyHorPadding, keyVerPadding
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

            kbdInfoBuilder.addKeys(
                buildKeyInfo(
                    code, text, getTextSize(code),
                    keyType,
                    xOffset, yOffset, realKeyWidth, keyHeight, keyColor, keyHorPadding, keyVerPadding
                )
            )

            xOffset += realKeyWidth
        }
    }
}
