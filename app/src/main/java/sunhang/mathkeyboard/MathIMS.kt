package sunhang.mathkeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.uiLazy

class MathIMS : InputMethodService() {
    private val rootController = RootController()
    private val rootView by uiLazy {
        View.inflate(this@MathIMS, R.layout.ime_layout, null) as RootView
    }

    override fun onCreate() {
        super.onCreate()
        val imsContext = IMSContext(applicationContext)
        rootController.onCreate(imsContext, rootView)
    }

    override fun onCreateInputView(): View {
        rootController.onCreateInputViewInvoked()
        return rootView
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