package sunhang.mathkeyboard.ime

import android.view.View
import android.view.inputmethod.EditorInfo
import sunhang.mathkeyboard.kbdviews.RootView

interface IMSAction {
    fun onCreate()
    fun onCreateInputViewInvoked()
    fun onStartInputView(info: EditorInfo, restarting: Boolean)
    fun onFinishInputView(finishingInput: Boolean)
    fun onDestroy()
}