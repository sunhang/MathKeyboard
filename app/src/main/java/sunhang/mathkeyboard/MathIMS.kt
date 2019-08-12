package sunhang.mathkeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo

class MathIMS : InputMethodService() {
    private val rootController = RootController()

    override fun onCreate() {
        super.onCreate()

        val imsContext = IMSContext(applicationContext)
        rootController.onCreate(imsContext)
    }

    override fun onCreateInputView(): View {
        return super.onCreateInputView()
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}