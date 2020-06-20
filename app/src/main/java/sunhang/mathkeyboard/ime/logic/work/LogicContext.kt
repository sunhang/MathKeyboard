package sunhang.mathkeyboard.ime.logic.work

import android.os.HandlerThread
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
class LogicContext @MainThread constructor() {
    companion object {
        const val CANDI_SIZE_IN_PAGE = 20
    }

    // todo 观察[logicThread.looper]此时返回null吗？因为怀疑[Thread.isAlive]
    private val logicThread = HandlerThread("input-logic").apply { start() }
    val looper: Looper get() = logicThread.looper

    val editorMsgPasser = MsgPasser(Looper.getMainLooper())
    val kbdUIMsgPasser = MsgPasser(Looper.getMainLooper())
    val logicMsgExecutor by lazy { LogicMsgExecutor(this) }
    var state: State = IdleState()
    var pinyinDecoder: IPinyinDecoderService? = null
    var planeType: PlaneType? = null

    val pinyinDecoderReady get() = pinyinDecoder != null

    val zhPlane get() = planeType == PlaneType.QWERTY_ZH

    fun dispose() {
        logicThread.quitSafely()
    }

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

    class LogicMsgExecutor(private val logicContext: LogicContext) : MsgExecutor {
        @WorkerThread
        override fun execute(msg: Msg) {
            if (BuildConfig.DEBUG) {
                if (Thread.currentThread() == Looper.getMainLooper().thread) {
                    throw RuntimeException("The code should not run on main thread!")
                }
            }

            when (msg.type) {
//            Msg.Logic.INIT -> init()
//            Msg.Logic.DISPOSE -> dispose()
                Msg.Logic.PINYIN_DEOCODER -> {
                    logicContext.pinyinDecoder =
                        msg.valuePack.asSingle<IPinyinDecoderService>().value
                }
                else -> logicContext.callStateAction(msg)
            }
        }
    }
}