package sunhang.mathkeyboard.kbdmodel.factory

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.CODE_SWITCH_MAIN
import sunhang.mathkeyboard.tools.isLetter
import sunhang.mathkeyboard.kbdmodel.Key
import sunhang.mathkeyboard.kbdmodel.UpperCaseSupportedKey
import sunhang.mathkeyboard.kbdmodel.ZhEnSwitchKey

class QwertyEnKeyboardFactory(context: Context) : KeyboardFactory(context) {
    override fun createKey(keyInfo: KbdInfo.KeyInfo): Key {
        if (keyInfo.type === KbdInfo.KeyType.NORMAL && isLetter(keyInfo.mainCode)) {
            return UpperCaseSupportedKey(context, keyInfo)
        } else if (keyInfo.mainCode == CODE_SWITCH_MAIN) {
            return ZhEnSwitchKey(context, keyInfo, false)
        } else {
            return super.createKey(keyInfo)
        }
    }
}