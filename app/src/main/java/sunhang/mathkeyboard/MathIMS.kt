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
    private lateinit var rootController : RootController
    private lateinit var inputToEditor : InputToEditor
    /**
    2019-08-17 18:51:21.716 31373-31373/? E/AndroidRuntime: FATAL EXCEPTION: main
    Process: sunhang.mathkeyboard, PID: 31373
    java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
    at android.view.ViewGroup.addViewInner(ViewGroup.java:5038)
    at android.view.ViewGroup.addView(ViewGroup.java:4869)
    at android.view.ViewGroup.addView(ViewGroup.java:4841)
    at android.inputmethodservice.InputMethodService.setInputView(InputMethodService.java:1596)
    at android.inputmethodservice.InputMethodService.updateInputViewShown(InputMethodService.java:1431)
    at android.inputmethodservice.InputMethodService.showWindowInner(InputMethodService.java:1835)
    at android.inputmethodservice.InputMethodService.showWindow(InputMethodService.java:1803)
    at android.inputmethodservice.InputMethodService$InputMethodImpl.showSoftInput(InputMethodService.java:570)
    at android.inputmethodservice.IInputMethodWrapper.executeMessage(IInputMethodWrapper.java:207)
    at com.android.internal.os.HandlerCaller$MyHandler.handleMessage(HandlerCaller.java:37)
    at android.os.Handler.dispatchMessage(Handler.java:106)
    at android.os.Looper.loop(Looper.java:201)
    at android.app.ActivityThread.main(ActivityThread.java:6815)
    at java.lang.reflect.Method.invoke(Native Method)
    at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:547)
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:873)
     */

    private val rootView by uiLazy {
        View.inflate(this@MathIMS, R.layout.ime_layout, null) as RootView
    }

    override fun onCreate() {
        super.onCreate()

        inputToEditor = InputToEditor()
        val imsContext = IMSContext(applicationContext, inputToEditor)
        rootController = RootController(imsContext, rootView)
        rootController.onCreate()
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