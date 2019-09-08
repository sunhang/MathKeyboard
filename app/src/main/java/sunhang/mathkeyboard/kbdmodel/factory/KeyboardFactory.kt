package sunhang.mathkeyboard.kbdmodel.factory

import android.content.Context
import protoinfo.KbdInfo
import protoinfo.KbdInfo.KeyboardInfo
import protoinfo.KbdInfo.KeyInfo
import sunhang.mathkeyboard.*
import sunhang.mathkeyboard.kbdmodel.*
import sunhang.mathkeyboard.tools.isBackslash
import sunhang.mathkeyboard.tools.isEnter
import sunhang.mathkeyboard.tools.isShift
import sunhang.mathkeyboard.tools.isSpace

open class KeyboardFactory(protected var context: Context) {
    fun createKeyboard(kbdInfo: KeyboardInfo): Keyboard {
        return kbdInfo.keysList
            .map { createKey(it) }
            .let { Keyboard(it) }
    }

    protected open fun createKey(keyInfo: KeyInfo): Key {
        return if (isBackslash(keyInfo.mainCode)) {
            BackspaceKey(context, keyInfo)
        } else if (isEnter(keyInfo.mainCode)) {
            EnterKey(context, keyInfo)
        } else if (isSpace(keyInfo.mainCode)) {
            SpaceKey(context, keyInfo)
        } else if (keyInfo.type === KbdInfo.KeyType.NORMAL) {
            Key(context, keyInfo)
        } else if (isMultiTextDisplayed(keyInfo.mainCode)) {
            Key(context, keyInfo)
        } else if (isShift(keyInfo.mainCode)) {
            return ShiftKey(context, keyInfo)
        } else {
            Key(context, keyInfo)
        }
    }

    // 按键上是否显示多个字符或者文字
    private fun isMultiTextDisplayed(code: Int): Boolean {
        return (code == CODE_SWITCH_SYMBOL
                || code == CODE_SWITCH_NUM_SODUKU
                || code == KEYCODE_APOSTROPHE
                || code == CODE_SWITCH_MAIN
                || code == CODE_SWITCH_EN_QWERTY
                || code == CODE_CLEAR
                || code == CODE_NEXT_PAGE
                || code == CODE_PRE_PAGE)
    }

    companion object {
        fun getFactory(context: Context, planeType: PlaneType): KeyboardFactory {
            return when (planeType) {
                PlaneType.QWERTY_EN -> QwertyEnKeyboardFactory(context)
                else -> QwertyEnKeyboardFactory(context)
            }
        }
    }
}
