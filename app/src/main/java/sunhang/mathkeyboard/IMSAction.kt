package sunhang.mathkeyboard

import android.view.View
import android.view.inputmethod.EditorInfo
import sunhang.mathkeyboard.kbdviews.RootView

interface IMSAction {
    fun onCreate(imsContext: IMSContext, rootView: RootView)
    fun onCreateInputViewInvoked()
    fun onStartInputView(info: EditorInfo, restarting: Boolean)
    fun onFinishInputView(finishingInput: Boolean)
    fun onDestroy()
}