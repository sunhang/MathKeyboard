package sunhang.mathkeyboard.ime.logic.work.state

import android.util.Log
import androidx.annotation.WorkerThread
import com.android.inputmethod.pinyin.IPinyinDecoderService
import sunhang.mathkeyboard.KEYCODE_DELETE
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.SingleValue
import sunhang.mathkeyboard.ime.logic.msg.asDouble
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.ime.logic.work.LogicContext.Companion.CANDI_SIZE_IN_PAGE
import sunhang.mathkeyboard.ime.logic.work.State
import java.lang.StringBuilder

@WorkerThread
class InputState : State {
    private var offset = 0
    private val pyBuf = ByteArray(32)
    private var totalChoicesNum = 0
    private var alreadyCandisSize = 0

    override fun doAction(context: LogicContext, msg: Msg) {
        if (preHandle(context, msg)) {
            return
        }

        val pinyinDecoder = context.pinyinDecoder!!

        when (msg.type) {
            Msg.Logic.LOAD_MORE_CHOICES -> {
                getCandidatesThenPassMsg(context, pinyinDecoder)
            }
            Msg.Logic.CHOOSE_CANDI -> chooseCandi(context, pinyinDecoder, msg.valuePack.asDouble<Int, String>().first)
            Msg.Logic.CODE -> handleCode(context, pinyinDecoder, msg.valuePack.asSingle<Int>().value)
        }
    }

    private fun getCandidatesThenPassMsg(context: LogicContext, pinyinDecoder: IPinyinDecoderService) {
        val type = if (alreadyCandisSize == 0) {
            Msg.KbdUI.CANDI_SHOW
        } else {
            Msg.KbdUI.CANDI_APPEND
        }

        val remain = totalChoicesNum - alreadyCandisSize
        val requestSize = if (remain < CANDI_SIZE_IN_PAGE) remain else CANDI_SIZE_IN_PAGE

        val candis = pinyinDecoder.imGetChoiceList(alreadyCandisSize, requestSize, 0)
        alreadyCandisSize += requestSize

        context.kbdUIMsgPasser.passMessage(type, candis, alreadyCandisSize < totalChoicesNum)
    }

    private fun getComposeThenPassMsg(context: LogicContext, pinyinDecoder: IPinyinDecoderService) {
        // todo decoded是什么意思？
        // todo 用[pyBuf]是不是更好一些
        val pyBuilder = StringBuilder(pinyinDecoder.imGetPyStr(true))
        val splStart = pinyinDecoder.imGetSplStart()

        splStart
            .slice(2 until splStart.size - 1)
            .reversed()
            .forEach {
                pyBuilder.insert(it, "'")
        }

        context.kbdUIMsgPasser.passMessage(Msg.KbdUI.COMPOSE, pyBuilder.toString())
    }

    private fun resetCompose(context: LogicContext) {
        context.kbdUIMsgPasser.passMessage(Msg.KbdUI.COMPOSE, "")
    }

    private fun chooseCandi(context: LogicContext, pinyinDecoder: IPinyinDecoderService, index: Int) {
        alreadyCandisSize = 0
        totalChoicesNum = pinyinDecoder.imChoose(index)
        val seledCandi = pinyinDecoder.imGetChoice(0)

        val composeTotalCount = pinyinDecoder.imGetSplStart()[0]
        val composeCurrentCount = pinyinDecoder.imGetFixedLen()

        if (composeCurrentCount == composeTotalCount) {
            // 发送消息给editor
            context.editorMsgPasser.passMessage(Msg.Editor.COMMIT_CANDI, seledCandi)

            // 切换到预测状态
            context.state = PredictState()
            context.callStateAction(Msg(Msg.Logic.PREDICT, SingleValue<String>(seledCandi)))
            resetCompose(context)
        } else {
            context.editorMsgPasser.passMessage(Msg.Editor.COMPOSE, seledCandi)
            getCandidatesThenPassMsg(context, pinyinDecoder)
            getComposeThenPassMsg(context, pinyinDecoder)
        }
    }

    private fun handleCode(context: LogicContext, pinyinDecoder: IPinyinDecoderService, code: Int) {
        when (code) {
            KEYCODE_DELETE -> {
                pinyinDecoder.imDelSearch(1, false, true)
                offset--
                if (0 == offset) {
                    totalChoicesNum = 0
                    alreadyCandisSize = 0
                    context.kbdUIMsgPasser.passMessage(Msg.KbdUI.CANDI_RESET)
                    context.state = IdleState()
                    resetCompose(context)
                } else {
                    alreadyCandisSize = 0
                    getCandidatesThenPassMsg(context, pinyinDecoder)
                    getComposeThenPassMsg(context, pinyinDecoder)
                }
            }
            else -> {
                pyBuf[offset++] = code.toByte()
                totalChoicesNum = pinyinDecoder.imSearch(pyBuf, offset)
                alreadyCandisSize = 0
                getCandidatesThenPassMsg(context, pinyinDecoder)

                val choice = pinyinDecoder.imGetChoice(0)
                context.editorMsgPasser.passMessage(Msg.Editor.COMPOSE, choice)
                getComposeThenPassMsg(context, pinyinDecoder)
            }
        }
    }
}