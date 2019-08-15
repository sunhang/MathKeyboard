package sunhang.mathkeyboard.kbdmodel

open class Keyboard(val keys: List<Key>) {
    val shiftKey = keys.find { it is ShiftKey } as? ShiftKey
}