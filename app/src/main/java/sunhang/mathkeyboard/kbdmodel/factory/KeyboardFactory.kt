package sunhang.mathkeyboard.kbdmodel.factory

import android.content.Context
import protoinfo.KbdInfo
import protoinfo.KbdInfo.KeyboardInfo
import protoinfo.KbdInfo.KeyInfo
import sunhang.mathkeyboard.*
import sunhang.mathkeyboard.kbdmodel.*
import sunhang.mathkeyboard.tools.isBackslash
import sunhang.mathkeyboard.tools.isEnter
import sunhang.mathkeyboard.tools.isSpace

open class KeyboardFactory(protected var context: Context) {
    fun createKeyboard(kbdInfo: KeyboardInfo): Keyboard {
        return kbdInfo.keysList
            .map { createKey(it) }
            .let { Keyboard(it) }
    }

    open protected fun createKey(keyInfo: KeyInfo): Key {
        return if (isBackslash(keyInfo.getMainCode())) {
            BackspaceKey(context, keyInfo)
        } else if (isEnter(keyInfo.getMainCode())) {
            EnterKey(context, keyInfo)
        } else if (isSpace(keyInfo.getMainCode())) {
            SpaceKey(context, keyInfo)
        } else if (keyInfo.getType() === KbdInfo.KeyType.NORMAL) {
            Key(context, keyInfo)
        } else if (isMultiTextDisplayed(keyInfo.getMainCode())) {
            Key(context, keyInfo)
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
                || code == CODE_CLEAR)
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
