package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import protoinfo.KbdInfo

open class Key(private val context: Context, private val keyInfo: KbdInfo.KeyInfo) {
    open fun draw() {}
}