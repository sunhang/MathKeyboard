package sunhang.mathkeyboard

import android.view.View
import android.view.inputmethod.EditorInfo

open class BaseController : IMSAction {
    protected val imsActions = mutableListOf<IMSAction>()

    override fun onCreate(imsContext: IMSContext, rootView: View) {
        imsActions.forEach { it.onCreate(imsContext, rootView) }
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        imsActions.forEach { it.onStartInputView(info, restarting) }
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        imsActions.forEach { it.onFinishInputView(finishingInput) }
    }

    override fun onDestroy() {
        imsActions.forEach { it.onDestroy() }
    }
}