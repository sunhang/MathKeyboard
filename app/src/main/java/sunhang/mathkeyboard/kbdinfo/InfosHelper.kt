package sunhang.mathkeyboard.kbdinfo

import android.util.Pair
import protoinfo.KbdInfo

fun buildKeyInfo(
    code: Int,
    text: String,
    textSize: Float,
    textSizeUpperCase: Float,
    baseLine: Float,
    upperCaseBaseLine: Float,
    hintCode: Int,
    hintText: String?,
    hintTextSize: Float,
    hintBaseLine: Float,
    hintUpperCaseBaseLine: Float,
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
        .setKeyColor(keyColor)
        .setHintTextSize(hintTextSize)

    if (baseLine != 0f) keyBuilder.setBaseLine(baseLine)
    if (upperCaseBaseLine != 0f) keyBuilder.setUpperCaseBaseLine(upperCaseBaseLine)

    if (hintCode != 0) keyBuilder.setHintCode(hintCode)
    if (hintText != null) keyBuilder.setHintText(hintText)

    if (hintBaseLine != 0f) keyBuilder.setHintBaseLine(hintBaseLine)
    if (hintUpperCaseBaseLine != 0f) keyBuilder.setHintUpperCaseBaseLine(hintUpperCaseBaseLine)

    val rectBuilder = KbdInfo.RectInfo.newBuilder()
    rectBuilder.setX(x).setY(y).setWidth(keyWidth).setHeight(keyHeight)
        .setHorPadding(keyHorPadding).setVerPadding(keyVerPadding)
    keyBuilder.setRectInfo(rectBuilder.build())

    return keyBuilder.build()
}

// 没有baseline
fun buildKeyInfo(
    code: Int,
    text: String,
    textSize: Float,
    textSizeUpperCase: Float,
    hintCode: Int,
    hintText: String,
    hintTextSize: Float,
    keyType: KbdInfo.KeyType,
    x: Float,
    y: Float,
    keyWidth: Float,
    keyHeight: Float,
    keyColor: KbdInfo.KeyColor,
    keyHorPadding: Float,
    keyVerPadding: Float
): KbdInfo.KeyInfo {
    return buildKeyInfo(
        code, text, textSize, textSizeUpperCase, 0f, 0f,
        hintCode, hintText, hintTextSize, 0f, 0f,
        keyType, x, y, keyWidth, keyHeight, keyColor,
        keyHorPadding, keyVerPadding
    )
}

// 没有hint,没有baseline
fun buildKeyInfo(
    code: Int,
    text: String,
    textSize: Float,
    keyType: KbdInfo.KeyType,
    x: Float,
    y: Float,
    keyWidth: Float,
    keyHeight: Float,
    keyColor: KbdInfo.KeyColor,
    keyHorPadding: Float,
    keyVerPadding: Float
): KbdInfo.KeyInfo {
    return buildKeyInfo(
        code, text, textSize, 0f,
        0, "", 0f,
        keyType, x, y, keyWidth, keyHeight, keyColor,
        keyHorPadding, keyVerPadding
    )
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

// 没有baseline
fun buildRow(
    planeBuilder: KbdInfo.KeyboardInfo.Builder,
    codes: Pair<IntArray, Array<String>>,
    textSize: Float,
    textSizeUpperCase: Float,
    hintCodes: Pair<IntArray, Array<String>>?,
    hintTextSize: Float,
    keyWidth: Float,
    keyHeight: Float,
    xOffset: Float,
    yOffset: Float,
    keyColors: Array<KbdInfo.KeyColor>,
    keyHorPadding: Float,
    keyVerPadding: Float
) {
    buildRow(
        planeBuilder, codes, textSize, textSizeUpperCase, 0f, 0f,
        hintCodes, hintTextSize, 0f, 0f,
        keyWidth, keyHeight, xOffset, yOffset, keyColors, keyHorPadding, keyVerPadding
    )
}

// 没有hint,没有baseline
fun buildRow(
    planeBuilder: KbdInfo.KeyboardInfo.Builder,
    codes: Pair<IntArray, Array<String>>,
    textSize: Float,
    textSizeUpperCase: Float,
    keyWidth: Float,
    keyHeight: Float,
    xOffset: Float,
    yOffset: Float,
    keyColors: Array<KbdInfo.KeyColor>,
    keyHorPadding: Float,
    keyVerPadding: Float
) {
    buildRow(
        planeBuilder, codes, textSize, textSizeUpperCase, null, 0f,
        keyWidth, keyHeight, xOffset, yOffset, keyColors, keyHorPadding, keyVerPadding
    )

}