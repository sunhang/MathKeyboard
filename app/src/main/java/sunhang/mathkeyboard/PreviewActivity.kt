package sunhang.mathkeyboard

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_preview.*
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.uiLazy

class PreviewActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        setContentView(R.layout.activity_preview)

        val rootView = View.inflate(this, R.layout.ime_layout, null) as RootView
        fl.addView(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        */

//        val inputToEditor = InputToEditor().apply {
//            editorInfo = (et as EditTextForKbdDebug).editorInfo
//            currentInputConnection = (et as EditTextForKbdDebug).inputConnection
//        }

        /*
        val msgPasser =
            LogicMsgPasser(Looper.getMainLooper(), LogicContext(Editor()))

        val rootController = RootController(IMSContext(this, msgPasser), rootView)
        rootController.onCreate()
        rootController.onCreateInputViewInvoked()
        */
    }

    class EditTextForKbdDebug(context: Context?, attrs: AttributeSet?) : EditText(context, attrs) {
        val editorInfo by uiLazy { EditorInfo() }
        val inputConnection by uiLazy {
            editorInfo.packageName = this.context.packageName
            onCreateInputConnection(editorInfo)
        }
    }
}