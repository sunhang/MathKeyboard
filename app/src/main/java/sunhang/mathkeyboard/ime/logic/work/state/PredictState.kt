package sunhang.mathkeyboard.ime.logic.work.state

import android.util.Log.i
import androidx.annotation.WorkerThread
import com.android.inputmethod.pinyin.IPinyinDecoderService
import sunhang.mathkeyboard.KEYCODE_DELETE
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.asDouble
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.ime.logic.work.LogicContext.Companion.CANDI_SIZE_IN_PAGE
import sunhang.mathkeyboard.ime.logic.work.State
import java.lang.StringBuilder

@WorkerThread
class PredictState : State {
    private var predictTotalNum = 0
    private var predictAlreadySize = 0
    private var predictionStr = StringBuilder()

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
        if (preHandle(context, msg)) {
            return
        }

        val pinyinDecoder = context.pinyinDecoder!!

        when (msg.type) {
            Msg.Logic.CHOOSE_CANDI -> {
                val (_, candi) = msg.valuePack.asDouble<Int, String>()

                predictionStr.append(candi)
                if (predictionStr.length > 3) {
                    predictionStr.delete(0, predictionStr.length - 3)
                }

                i("logic", "predictState > choose_candi > predictionStr: $predictionStr")
                // 这里是上屏的candi
                context.editorMsgPasser.passMessage(Msg.Editor.COMMIT_CANDI, candi)

                predictTotalNum = pinyinDecoder.imGetPredictsNum(predictionStr.toString())
                predictAlreadySize = 0

                getPredictionThenPassMsg(context, pinyinDecoder)
            }
            Msg.Logic.PREDICT -> {
                predictionStr.append(msg.valuePack.asSingle<String>().value)
                predictTotalNum = pinyinDecoder.imGetPredictsNum(predictionStr.toString())
                predictAlreadySize = 0

                getPredictionThenPassMsg(context, pinyinDecoder)
            }
            Msg.Logic.LOAD_MORE_CHOICES -> {
                getPredictionThenPassMsg(context, pinyinDecoder)
            }
            Msg.Logic.CODE -> {
                pinyinDecoder.imResetSearch()

                when (msg.valuePack.asSingle<Int>().value) {
                    KEYCODE_DELETE -> {
                        context.kbdUIMsgPasser.passMessage(Msg.KbdUI.CANDI_RESET)

                        context.state = IdleState()
                        context.callStateAction(msg)
                    }
                    else -> {
                        context.state = InputState()
                        context.callStateAction(msg)
                    }
                }
            }
        }
    }
}