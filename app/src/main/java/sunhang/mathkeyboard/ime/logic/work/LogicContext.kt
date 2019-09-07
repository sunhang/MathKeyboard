package sunhang.mathkeyboard.ime.logic.work

import android.os.Looper
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.android.inputmethod.pinyin.IPinyinDecoderService
import sunhang.mathkeyboard.BuildConfig
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgExecutor
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser
import sunhang.mathkeyboard.ime.logic.work.state.IdleState
import sunhang.mathkeyboard.kbdmodel.PlaneType

@WorkerThread
class LogicContext @MainThread constructor(val editorMsgPasser: MsgPasser, val kbdUIMsgPasser: MsgPasser) {
    companion object {
        const val CANDI_SIZE_IN_PAGE = 20
    }

    var state: State = IdleState()
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

    fun callStateAction(msg: Msg) {
        state.doAction(this, msg)
    }
}