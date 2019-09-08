package sunhang.mathkeyboard.kbdinfo

import android.util.Pair
import protoinfo.KbdInfo

fun buildKeyInfo(
    code: Int,
    text: String,
    textSize: Float,
    textSizeUpperCase: Float = 0f,
    baseLine: Float = 0f,
    upperCaseBaseLine: Float = 0f,
    hintCode: Int = 0,
    hintText: String? = null,
    hintTextSize: Float = 0f,
    hintBaseLine: Float = 0f,
    hintUpperCaseBaseLine: Float = 0f,
    keyType: KbdInfo.KeyType,
    x: Float,
    y: Float,
    keyWidth: Float,
    keyHeight: Float,
    keyColor: KbdInfo.KeyColor,
    keyHorPadding: Float,
    keyVerPadding: Float
): KbdInfo.KeyInfo {
    val keyBuilder = KbdInfo.KeyInfo.newBuilder()
    keyBuilder.setMainCode(code)
        .setText(text)
        .setType(keyType)
        .setTextSize(textSize)
        .setTextSizeUpperCase(textSizeUpperCase)
        .setKeyColor(keyColor).hintTextSize = hintTextSize

    if (baseLine != 0f) keyBuilder.baseLine = baseLine
    if (upperCaseBaseLine != 0f) keyBuilder.upperCaseBaseLine = upperCaseBaseLine

    if (hintCode != 0) keyBuilder.hintCode = hintCode
    if (hintText != null) keyBuilder.hintText = hintText

    if (hintBaseLine != 0f) keyBuilder.hintBaseLine = hintBaseLine
    if (hintUpperCaseBaseLine != 0f) keyBuilder.hintUpperCaseBaseLine = hintUpperCaseBaseLine

    val rectBuilder = KbdInfo.RectInfo.newBuilder()
    rectBuilder.setX(x).setY(y).setWidth(keyWidth).setHeight(keyHeight)
        .setHorPadding(keyHorPadding).verPadding = keyVerPadding
    keyBuilder.rectInfo = rectBuilder.build()

    return keyBuilder.build()
}

fun buildRow(
    planeBuilder: KbdInfo.KeyboardInfo.Builder,
    codes: Pair<IntArray, Array<String>>,
    textSize: Float,
    textSizeUpperCase: Float,
    baseLine: Float,
    upperCaseBaseLine: Float,
    hintCodes: Pair<IntArray, Array<String>>?,
    hintTextSize: Float,
    hintBaseLine: Float,
    upperCaseHintBaseLine: Float,
    keyWidth: Float,
    keyHeight: Float,
    xOffset: Float,
    yOffset: Float,
    keyColors: Array<KbdInfo.KeyColor>?,
    keyHorPadding: Float,
    keyVerPadding: Float
) {
    val codesLen = codes.first.size
    val textsLen = codes.second.size
    if (codesLen != textsLen) {
        throw IllegalArgumentException()
    }

    for (i in codes.first.indices) {
        val x = xOffset + keyWidth * i

        val keyColor = if (keyColors != null && i < keyColors.size) keyColors[i] else KbdInfo.KeyColor.COLOR_NORMAL

        var hintCode = 0
        if (hintCodes != null && i < hintCodes.first.size) {
            hintCode = hintCodes.first[i]
        }
        var hintText: String? = null
        if (hintCodes != null && i < hintCodes.second.size) {
            hintText = hintCodes.second[i]
        }

        planeBuilder.addKeys(
            buildKeyInfo(
                codes.first[i], codes.second[i], textSize, textSizeUpperCase, baseLine, upperCaseBaseLine,
                hintCode, hintText, hintTextSize, hintBaseLine, upperCaseHintBaseLine,
                KbdInfo.KeyType.NORMAL, x,
                yOffset, keyWidth, keyHeight, keyColor,
                keyHorPadding, keyVerPadding
            )
        )
    }
}
