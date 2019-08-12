package sunhang.mathkeyboard.kbdmodel.factory

import android.content.Context
import protoinfo.KbdInfo
import sunhang.mathkeyboard.isLetter
import sunhang.mathkeyboard.isShift
import sunhang.mathkeyboard.kbdmodel.Key
import sunhang.mathkeyboard.kbdmodel.ShiftKey
import sunhang.mathkeyboard.kbdmodel.UpperCaseSupportedKey

class QwertyEnKeyboardFactory(context: Context) : KeyboardFactory(context) {
    override fun createKey(keyInfo: KbdInfo.KeyInfo): Key {
        if (isShift(keyInfo.mainCode)) {
            return ShiftKey(context, keyInfo)
        } else if (keyInfo.getType() === KbdInfo.KeyType.NORMAL && isLetter(keyInfo.getMainCode())) {
            return UpperCaseSupportedKey(context, keyInfo)
        } else {
            return super.createKey(keyInfo)
        }
    }
}