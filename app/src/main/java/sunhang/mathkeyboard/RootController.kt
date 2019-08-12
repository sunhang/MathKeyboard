package sunhang.mathkeyboard

import android.view.inputmethod.EditorInfo

class RootController : IMSAction {
    private val keyboardController = KeyboardController()

    override fun onCreate(imsContext: IMSContext) {
        keyboardController.onCreate(imsContext)
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}