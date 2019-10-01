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

private const val CUSTOM_BEGIN = -10000

const val CODE_SWITCH_SYMBOL = -1 + CUSTOM_BEGIN // 切换到符号键盘
const val CODE_SWITCH_NUM_SODUKU = -2 + CUSTOM_BEGIN // 切换到9宫格数字键盘
const val CODE_SWITCH_EN_QWERTY = -3 + CUSTOM_BEGIN // 切换到26键英文键盘
const val CODE_SWITCH_MAIN = -4 + CUSTOM_BEGIN // 切换到中文键盘
const val CODE_ENTER = -5 + CUSTOM_BEGIN // 回车键
const val CODE_CLEAR = -6 + CUSTOM_BEGIN // 清空
const val CODE_BACK = -7 + CUSTOM_BEGIN // 从符号键盘返回
const val CODE_NEXT_PAGE = -8 + CUSTOM_BEGIN // 下一页
const val CODE_PRE_PAGE = -9 + CUSTOM_BEGIN //  上一页
