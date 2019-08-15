package sunhang.mathkeyboard.ime

import android.view.inputmethod.EditorInfo

fun getActionId(ei: EditorInfo): Int {
    return if (ei.imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION == 0) {
        ei.imeOptions and EditorInfo.IME_MASK_ACTION
    } else {
        EditorInfo.IME_ACTION_UNSPECIFIED
    }
}
