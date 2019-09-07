package sunhang.mathkeyboard.ime.logic.work.state

import androidx.annotation.WorkerThread
import com.android.inputmethod.pinyin.IPinyinDecoderService
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.ime.logic.work.LogicContext.Companion.CANDI_SIZE_IN_PAGE
import sunhang.mathkeyboard.ime.logic.work.State

@WorkerThread
class PredictState : State {
    private var predictTotalNum = 0
    private var predictAlreadySize = 0

    private fun getPredictionThenPassMsg(context: LogicContext, pinyinDecoder: IPinyinDecoderService) {
        val type = if (predictAlreadySize == 0) {
            Msg.KbdUI.CANDI_SHOW
        } else {
            Msg.KbdUI.CANDI_APPEND
        }

        val remain = predictTotalNum - predictAlreadySize
        val requestSize = if (remain < CANDI_SIZE_IN_PAGE) remain else CANDI_SIZE_IN_PAGE
        val list = pinyinDecoder.imGetPredictList(predictAlreadySize, requestSize)
        predictAlreadySize += requestSize

        val hasMore = predictAlreadySize < predictTotalNum
        context.kbdUIMsgPasser.passMessage(type, list, hasMore)
    }

    override fun doAction(context: LogicContext, msg: Msg) {
        val pinyinDecoder = context.pinyinDecoder!!

        when (msg.type) {
            Msg.Logic.PREDICT -> {
                predictTotalNum = pinyinDecoder.imGetPredictsNum(msg.valuePack.asSingle<String>().value)
                predictAlreadySize = 0

                getPredictionThenPassMsg(context, pinyinDecoder)
            }
            Msg.Logic.LOAD_MORE_CHOICES -> {
                getPredictionThenPassMsg(context, pinyinDecoder)
            }
        }
    }
}