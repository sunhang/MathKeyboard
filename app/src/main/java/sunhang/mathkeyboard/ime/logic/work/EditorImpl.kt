package sunhang.mathkeyboard.ime.logic.work

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import sunhang.mathkeyboard.CODE_ENTER
import sunhang.mathkeyboard.KEYCODE_DELETE
import sunhang.mathkeyboard.base.common.guardLet
import sunhang.mathkeyboard.ime.getActionId
import sunhang.mathkeyboard.ime.logic.Editor

class EditorImpl : Editor {
    var editorInfo: EditorInfo? = null
    var currentInputConnection: InputConnection? = null

    override fun commitCode(code: Int) {
        when (code) {
            KEYCODE_DELETE -> {
                currentInputConnection?.run {
                    sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                    sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
                }
            }
            CODE_ENTER -> {
                performEditorAction()
            }
            else -> currentInputConnection?.commitText(convertChar(code), 1)
        }
    }

    private fun convertChar(code: Int): CharSequence {
        val charArray = CharArray(1)
        charArray[0] = code.toChar()
        return java.nio.CharBuffer.wrap(charArray)
    }

    private fun performEditorAction() {
        // 判空,有空则返回
        val (editorInfo, inputConn) = guardLet(editorInfo, currentInputConnection) { return }

        // 对微信输入框做了个单独处理
        // 不要考虑EditorInfo.IME_FLAG_NO_ENTER_ACTION这个字段
        val actionId = if ("com.tencent.mm" == editorInfo.packageName && (editorInfo.imeOptions and EditorInfo.IME_MASK_ACTION === EditorInfo.IME_ACTION_SEND)) {
            EditorInfo.IME_ACTION_SEND
        } else {
            getActionId(editorInfo)
        }

        if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_NONE) {
            // do nothing
        } else {
            inputConn.performEditorAction(actionId)
        }
    }
}