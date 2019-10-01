package sunhang.mathkeyboard

import android.inputmethodservice.Keyboard

// Because we don't have minus unicode code point, so we do some trick.
// Definitions in android.inputmethodservice.Keyboard:
const val KEYCODE_SHIFT = Keyboard.KEYCODE_SHIFT
const val KEYCODE_MODE_CHANGE = Keyboard.KEYCODE_MODE_CHANGE
const val KEYCODE_CANCEL = Keyboard.KEYCODE_CANCEL
const val KEYCODE_DONE = Keyboard.KEYCODE_DONE
const val KEYCODE_DELETE = Keyboard.KEYCODE_DELETE
const val KEYCODE_ALT = Keyboard.KEYCODE_ALT
const val KEYCODE_SPACE: Int = ' '.toInt()
const val KEYCODE_APOSTROPHE: Int = '\''.toInt()

const val ZH_COMMA: Int = '，'.toInt()
const val ZH_PERIOD: Int = '。'.toInt()
const val EN_COMMA: Int = ','.toInt()
const val EN_PERIOD: Int = '.'.toInt()

private const val CUSTOM_BEGIN = -10000

const val CODE_SWITCH_SYMBOL = -1 + CUSTOM_BEGIN // 切换到符号键盘
const val CODE_SWITCH_NUM_SODUKU = -2 + CUSTOM_BEGIN // 切换到9宫格数字键盘
const val CODE_SWITCH_EN_QWERTY = -3 + CUSTOM_BEGIN // 切换到26键英文键盘
const val CODE_SWITCH_MAIN = -4 + CUSTOM_BEGIN // 切换到中文键盘
const val CODE_SWITCH_MATH_SYM = -5 + CUSTOM_BEGIN // 切换到数学符号键盘

/**
 * 写到这里，因为经常变动switch codes这些常量，避免变动时函数不能及时更新
 */
fun isSwitchCode(code: Int): Boolean {
    return (code == CODE_SWITCH_EN_QWERTY
            || code == CODE_SWITCH_MAIN
            || code == CODE_SWITCH_NUM_SODUKU
            || code == CODE_SWITCH_SYMBOL
            || code == CODE_SWITCH_MATH_SYM
            || code == CODE_BACK)
}


//从-10开始，给switch codes更多可添加的空间
const val CODE_ENTER = -10 + CUSTOM_BEGIN // 回车键
const val CODE_CLEAR = -11 + CUSTOM_BEGIN // 清空
const val CODE_BACK = -12 + CUSTOM_BEGIN // 从符号键盘返回
const val CODE_NEXT_PAGE = -13 + CUSTOM_BEGIN // 下一页
const val CODE_PRE_PAGE = -14 + CUSTOM_BEGIN //  上一页
