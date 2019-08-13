package sunhang.mathkeyboard.tools

import sunhang.mathkeyboard.*

fun isShift(code: Int): Boolean {
    return code == KEYCODE_SHIFT
}

fun isEnter(code: Int): Boolean {
    return code == CODE_ENTER
}

fun isBackslash(code: Int): Boolean {
    return code == KEYCODE_DELETE
}

fun isSpace(code: Int): Boolean {
    return code == KEYCODE_SPACE
}

fun isLetter(code: Int): Boolean {
    return code >= 'a'.toInt() && code <= 'z'.toInt()
}

fun isCharacter(code: Int): Boolean {
    return code > 0
}

fun isZhCommaOrPeriod(unicode: Int): Boolean {
    return unicode == 0xff0c || unicode == 0x3002
}

//是中文分号或者中文冒号
fun isSemicolonOrColon(unicode: Int): Boolean {
    return unicode == 0xff1b || unicode == 0xff1a
}

fun isZhComma(unicode: Int): Boolean {
    return unicode == 0xff0c
}

fun isPeriod(unicode: Int): Boolean {
    return unicode == 0x3002
}

fun isZhSymbol(code: Int): Boolean {
    // Halfwidth and Fullwidth Forms
    return if (code >= 0xFF01 && code <= 0xFF65
        || code == 0xFF9F
        || code >= 0xFFE0 && code <= 0XFFEF
    ) {
        true
    } else code >= 0x3000 && code <= 0x303F

    // CJK Symbols and Punctuation

}

fun isSwitchCode(code: Int): Boolean {
    return (code == CODE_SWITCH_EN_QWERTY
            || code == CODE_SWITCH_MAIN
            || code == CODE_SWITCH_NUM_SODUKU
            || code == CODE_SWITCH_SYMBOL
            || code == CODE_BACK)
}