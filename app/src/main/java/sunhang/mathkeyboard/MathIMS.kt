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
import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.mathkeyboard.ime.logic.Logic
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.uiLazy

class MathIMS : InputMethodService() {
    private val rootView: RootView
    private val logic: Logic
    private val rootController: RootController

    init {
        val initializer = Initializer(GlobalVariable.context)

        rootView = initializer.rootView
        logic = initializer.logic
        rootController = initializer.rootController
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
        val parent = rootView.parent as? ViewGroup
        parent?.let {
            it.removeView(rootView)
        }

        return rootView
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)

        logic.attachInputConnection(currentInputConnection)
        logic.attachEditorInfo(info)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
    }

    override fun onDestroy() {
        super.onDestroy()
        // todo logic中的pinyinDecoder可以使用吗？
        unbindService(pinyinDecoderServiceConnection)
        logic.dispose()
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