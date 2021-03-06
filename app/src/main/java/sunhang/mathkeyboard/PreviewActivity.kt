package sunhang.mathkeyboard

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.inputmethod.pinyin.IPinyinDecoderService
import com.android.inputmethod.pinyin.PinyinDecoderService
import kotlinx.android.synthetic.main.activity_preview.*
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.openlibrary.uiLazy

class PreviewActivity : AppCompatActivity() {
    private val editor = Editor()
    private val logicContext = LogicContext()
    private val imsContext: IMSContext
    private val rootController: RootController

    init {
        with(generateImsContextAndController(GlobalVariable.context, logicContext.looper)) {
            imsContext = first
            rootController = second
        }

        attachEachOther(imsContext, logicContext, editor, rootController)
    }

    private val pinyinDecoderServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val pinyinDecoderService = IPinyinDecoderService.Stub.asInterface(service)
            rootController.imsContext.logicMsgPasser.passMessage(
                Msg.Logic.PINYIN_DEOCODER,
                pinyinDecoderService
            )
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        editor.editorInfo = (et as EditTextForKbdDebug).editorInfo
        editor.currentInputConnection = (et as EditTextForKbdDebug).inputConnection

        val rootView = rootController.rootView
        fl.addView(
            rootView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        rootController.onCreate()
        rootController.onCreateInputViewInvoked()

        val serviceIntent = Intent()
        serviceIntent.setClass(this, PinyinDecoderService::class.java)
        // Bind service
        bindService(serviceIntent, pinyinDecoderServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()

        unbindService(pinyinDecoderServiceConnection)
        logicContext.dispose()
    }

    class EditTextForKbdDebug(context: Context?, attrs: AttributeSet?) : EditText(context, attrs) {
        val editorInfo by uiLazy { EditorInfo() }
        val inputConnection by uiLazy {
            editorInfo.packageName = this.context.packageName
            onCreateInputConnection(editorInfo)
        }
    }
}