package sunhang.mathkeyboard.ime

import android.view.inputmethod.InputConnection

class InputToEditor {
    var currentInputConnection: InputConnection? = null

    fun inputChar(code: Int) {
        currentInputConnection?.commitText(convertChar(code), 1)
    }

    protected fun convertChar(code: Int): CharSequence {
        val charArray = CharArray(1)
        charArray[0] = code.toChar()
        return java.nio.CharBuffer.wrap(charArray)
    }
}
