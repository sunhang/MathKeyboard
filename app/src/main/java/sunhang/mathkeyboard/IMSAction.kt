package sunhang.mathkeyboard

import android.view.View
import android.view.inputmethod.EditorInfo

interface IMSAction {
    fun onCreate(imsContext: IMSContext, rootView: View)
    fun onStartInputView(info: EditorInfo, restarting: Boolean)
    fun onFinishInputView(finishingInput: Boolean)
    fun onDestroy()
}