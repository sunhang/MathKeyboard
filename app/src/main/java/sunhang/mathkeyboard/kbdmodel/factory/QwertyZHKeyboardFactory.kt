package sunhang.mathkeyboard.kbdmodel.factory

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.CODE_SWITCH_EN_QWERTY
import sunhang.mathkeyboard.CODE_SWITCH_MAIN
import sunhang.mathkeyboard.tools.isLetter
import sunhang.mathkeyboard.tools.isShift
import sunhang.mathkeyboard.kbdmodel.Key
import sunhang.mathkeyboard.kbdmodel.ShiftKey
import sunhang.mathkeyboard.kbdmodel.UpperCaseSupportedKey
import sunhang.mathkeyboard.kbdmodel.ZhEnSwitchKey

class QwertyZHKeyboardFactory(context: Context) : KeyboardFactory(context) {
    override fun createKey(keyInfo: KbdInfo.KeyInfo): Key {
        if (keyInfo.getType() === KbdInfo.KeyType.NORMAL && isLetter(keyInfo.getMainCode())) {
            return UpperCaseSupportedKey(context, keyInfo)
        } else if (keyInfo.mainCode == CODE_SWITCH_EN_QWERTY) {
            return ZhEnSwitchKey(context, keyInfo, true)
        } else {
            return super.createKey(keyInfo)
        }
    }
}