package sunhang.mathkeyboard.ime.logic.work

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.android.inputmethod.pinyin.IPinyinDecoderService
import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msgpasser.EditorMsgPasser
import sunhang.mathkeyboard.ime.logic.work.state.IdleState
import sunhang.mathkeyboard.kbdmodel.PlaneType

@WorkerThread
class LogicContext @MainThread constructor(val editorMsgPasser: EditorMsgPasser) {
    var state: State? = null
    var pinyinDecoder: IPinyinDecoderService? = null
    var planeType: PlaneType? = null

    val pinyinDecoderReady get() = pinyinDecoder != null

    val zhPlane get() = planeType == PlaneType.QWERTY_ZH

    /*
    val editor = Proxy.newProxyInstance(
        Editor::class.java.classLoader,
        arrayOf(Editor::class.java)
    ) { _, method: Method?, args: Array<out Any>? ->
        runOnMain {
            val array = args ?: arrayOf()
            method?.invoke(inputToEditor, *array)
        }
    } as Editor
    */

    fun init() {
        state = IdleState()
    }

    fun dispose() {
        state = null
        pinyinDecoder = null
    }

    fun callStateAction(message: Msg) {
        state?.doAction(this, message)
    }
}