package sunhang.mathkeyboard.ime.logic.work.state

import com.android.inputmethod.pinyin.IPinyinDecoderService
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.asSingle
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.kbdmodel.PlaneType

fun preHandle(context: LogicContext, msg: Msg): Boolean {
    val pinyinDecoder = context.pinyinDecoder!!
    when (msg.type) {
        Msg.Logic.FINISH_INPUT_VIEW -> handleFinishInputView(context, pinyinDecoder)
        Msg.Logic.PLANE_TYPE -> handleSwitchPlaneType(context, pinyinDecoder, msg)
        else -> return false
    }

    return true
}

private fun handleFinishInputView(context: LogicContext, pinyinDecoder: IPinyinDecoderService) {
    pinyinDecoder.imResetSearch()
    context.kbdUIMsgPasser.passMessage(Msg.KbdUI.CANDI_RESET)
    context.state = IdleState()
}

private fun handleSwitchPlaneType(
    context: LogicContext,
    pinyinDecoder: IPinyinDecoderService,
    msg: Msg
) {
    context.planeType = msg.valuePack.asSingle<PlaneType>().value
    pinyinDecoder.imResetSearch()
    context.kbdUIMsgPasser.passMessage(Msg.KbdUI.CANDI_RESET)
    context.state = IdleState()
}
