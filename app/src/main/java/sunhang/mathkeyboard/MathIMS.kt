package sunhang.mathkeyboard

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.inputmethodservice.InputMethodService
import android.os.HandlerThread
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.android.inputmethod.pinyin.IPinyinDecoderService
import com.android.inputmethod.pinyin.PinyinDecoderService
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.openlibrary.uiLazy

class MathIMS : InputMethodService() {
    private val rootController: RootController
    private val logicThread: HandlerThread
    private val editor: Editor
    private val imsContext: IMSContext

    init {
        val initializer = Initializer(GlobalVariable.context)

        rootController = initializer.rootController
        logicThread = initializer.logicThread
        editor = initializer.editor
        imsContext = initializer.imsContext
    }

    /**
     * Connection used to bind the decoding service.
     */
    private val pinyinDecoderServiceConnection by uiLazy { PinyinDecoderServiceConnection() }

    override fun onCreate() {
        super.onCreate()

        rootController.onCreate()
        startPinyinDecoderService()
    }

    override fun onCreateInputView(): View {
        rootController.onCreateInputViewInvoked()

        // 处理横竖屏切换时的问题
        return rootController.rootView.also {
            (it.parent as? ViewGroup)?.removeView(it)
        }
    }

    override fun onComputeInsets(outInsets: Insets?) {
        super.onComputeInsets(outInsets)
        rootController.onComputeInsets(outInsets)
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)

        editor.currentInputConnection = currentInputConnection
        editor.editorInfo = info
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        imsContext.logicMsgPasser.passMessage(Msg.Logic.FINISH_INPUT_VIEW)
    }

    override fun onDestroy() {
        super.onDestroy()
        // todo logic中的pinyinDecoder可以使用吗？
        unbindService(pinyinDecoderServiceConnection)
        logicThread.quitSafely()

        imsContext.dispose()
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
            rootController.imsContext.logicMsgPasser.passMessage(Msg.Logic.PINYIN_DEOCODER, pinyinDecoderService)
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }

}