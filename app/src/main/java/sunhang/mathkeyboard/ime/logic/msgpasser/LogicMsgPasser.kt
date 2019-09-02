package sunhang.mathkeyboard.ime.logic.msgpasser

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.android.inputmethod.pinyin.IPinyinDecoderService
import sunhang.mathkeyboard.BuildConfig
import sunhang.mathkeyboard.ime.logic.SingleValue
import sunhang.mathkeyboard.ime.logic.asSingle
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.Msg.Logic.Companion.DISPOSE
import sunhang.mathkeyboard.ime.logic.msg.Msg.Logic.Companion.INIT
import sunhang.mathkeyboard.ime.logic.msg.Msg.Logic.Companion.PINYIN_DEOCODER
import sunhang.mathkeyboard.ime.logic.msg.Msg.Logic.Companion.PLANE_TYPE
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.kbdmodel.PlaneType
import java.lang.RuntimeException

/**
 * 线程间消息传递
 */
@MainThread
class LogicMsgPasser(logicLooper: Looper, private val logicContext: LogicContext): BaseMsgPasser() {
    override val handler = Handler(logicLooper)

    @WorkerThread
    override fun handleMsg(msg: Msg) {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() == Looper.getMainLooper().thread) {
                throw RuntimeException("The code should not run on main thread!")
            }
        }

        // todo 换成ofSingle，handleMsg提到外边去
        when (msg.type) {
            INIT -> logicContext.init()
            DISPOSE -> logicContext.dispose()
            PINYIN_DEOCODER -> {
                logicContext.pinyinDecoder = msg.valuePack.asSingle<IPinyinDecoderService>().value
            }
            PLANE_TYPE -> {
                logicContext.planeType = msg.valuePack.asSingle<PlaneType>().value
            }
            else -> logicContext.callStateAction(msg)
        }
    }

}