package sunhang.mathkeyboard

import protoinfo.Keyboard


abstract class Key(private val keyInfo: Keyboard.KeyInfo) {
    abstract fun draw()
}