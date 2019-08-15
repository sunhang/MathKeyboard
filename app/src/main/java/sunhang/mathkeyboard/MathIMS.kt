package sunhang.mathkeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.InputToEditor
import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.uiLazy

class MathIMS : InputMethodService() {
    private val rootController = RootController()
    private val inputToEditor = InputToEditor()

    private val rootView by uiLazy {
        View.inflate(this@MathIMS, R.layout.ime_layout, null) as RootView
    }

    override fun onCreate() {
        super.onCreate()
        val imsContext = IMSContext(applicationContext, inputToEditor)
        rootController.onCreate(imsContext, rootView)
    }

    override fun onCreateInputView(): View {
        rootController.onCreateInputViewInvoked()
        return rootView
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        inputToEditor.currentInputConnection = currentInputConnection
        inputToEditor.editorInfo = info
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}