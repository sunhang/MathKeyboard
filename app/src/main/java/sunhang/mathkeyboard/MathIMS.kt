package sunhang.mathkeyboard

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.inputmethodservice.InputMethodService
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import com.android.inputmethod.pinyin.IPinyinDecoderService
import com.android.inputmethod.pinyin.PinyinDecoderService
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.InputToEditor
import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.mathkeyboard.tools.i
import sunhang.openlibrary.uiLazy

class MathIMS : InputMethodService() {
    private lateinit var rootController : RootController
    private lateinit var inputToEditor : InputToEditor
    /**
     * Connection used to bind the decoding service.
     */
    private val pinyinDecoderServiceConnection by uiLazy { PinyinDecoderServiceConnection() }

    private val rootView by uiLazy {
        View.inflate(this@MathIMS, R.layout.ime_layout, null) as RootView
    }

    override fun onCreate() {
        super.onCreate()

        inputToEditor = InputToEditor()
        val imsContext = IMSContext(applicationContext, inputToEditor)
        rootController = RootController(imsContext, rootView)
        rootController.onCreate()

        i("${startPinyinDecoderService()}")
    }

    override fun onCreateInputView(): View {
        rootController.onCreateInputViewInvoked()

        // 处理横竖屏切换时的问题
        val parent = rootView.parent as? ViewGroup
        parent?.let {
            it.removeView(rootView)
        }

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
        unbindService(pinyinDecoderServiceConnection)
    }

    private fun startPinyinDecoderService(): Boolean {
        val serviceIntent = Intent()
        serviceIntent.setClass(this, PinyinDecoderService::class.java)
        // Bind service
        return bindService(serviceIntent, pinyinDecoderServiceConnection, Context.BIND_AUTO_CREATE)
    }

    /**
     * Connection used for binding to the Pinyin decoding service.
     */
    inner class PinyinDecoderServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val pinyinDecoderService = IPinyinDecoderService.Stub.asInterface(service)
            i("pinyinDecoderService >>  $pinyinDecoderService")
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }

}