package sunhang.mathkeyboard.ime.logic

import android.os.Looper
import androidx.annotation.WorkerThread
import com.android.inputmethod.pinyin.IPinyinDecoderService
import sunhang.mathkeyboard.BuildConfig
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgExecutor
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.kbdmodel.PlaneType

class LogicExecutor(private val logicContext: LogicContext) : MsgExecutor {
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
                logicContext.pinyinDecoder = msg.valuePack.asSingle<IPinyinDecoderService>().value
            }
            else -> logicContext.callStateAction(msg)
        }
    }
}