package sunhang.mathkeyboard.kbdmodel.factory

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.CODE_SWITCH_EN_QWERTY
import sunhang.mathkeyboard.CODE_SWITCH_MAIN
import sunhang.mathkeyboard.ZH_COMMA
import sunhang.mathkeyboard.ZH_PERIOD
import sunhang.mathkeyboard.kbdmodel.*
import sunhang.mathkeyboard.tools.isLetter
import sunhang.mathkeyboard.tools.isShift

class QwertyZHKeyboardFactory(context: Context) : KeyboardFactory(context) {
    override fun createKey(keyInfo: KbdInfo.KeyInfo): Key {
        return if (keyInfo.type === KbdInfo.KeyType.NORMAL && isLetter(keyInfo.mainCode)) {
            UpperCaseSupportedKey(context, keyInfo)
        } else if (keyInfo.mainCode == CODE_SWITCH_EN_QWERTY) {
            ZhEnSwitchKey(context, keyInfo, true)
        } else if (keyInfo.mainCode == ZH_COMMA || keyInfo.mainCode == ZH_PERIOD) {
            FullwidthSymKey(context, keyInfo)
        } else {
            super.createKey(keyInfo)
        }
    }
}
