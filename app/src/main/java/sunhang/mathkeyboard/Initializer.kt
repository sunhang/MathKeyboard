package sunhang.mathkeyboard

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.View
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.RootController
import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.mathkeyboard.ime.logic.LogicExecutor
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgExecutor
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser
import sunhang.mathkeyboard.ime.logic.work.LogicContext
import sunhang.mathkeyboard.kbdviews.RootView

fun generateImsContextAndController(
    globalContext: Context,
    logicLooper: Looper
): Pair<IMSContext, RootController> {
    val rootView = View.inflate(globalContext, R.layout.ime_layout, null) as RootView

    val imsContext = IMSContext(globalContext, MsgPasser(logicLooper))
    val rootController = RootController(imsContext, rootView)

    return Pair(imsContext, rootController)
}

fun attachEachOther(
    imsContext: IMSContext,
    logicContext: LogicContext,
    editor: Editor,
    rootController: RootController
) {
    // 把executor关联到msg passer中
    imsContext.logicMsgPasser.attachExecutor(logicContext.logicMsgExecutor)
    with(logicContext) {
        editorMsgPasser.attachExecutor(editor)
        kbdUIMsgPasser.attachExecutor(sunhang.mathkeyboard.KbdUIExecutor(rootController))
    }
}