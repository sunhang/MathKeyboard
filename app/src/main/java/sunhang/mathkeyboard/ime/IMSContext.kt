package sunhang.mathkeyboard.ime

import android.content.Context
import io.reactivex.subjects.BehaviorSubject
import sunhang.mathkeyboard.data.KbdDb
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser
import sunhang.openlibrary.uiLazy

class IMSContext(val context: Context, val logicMsgPasser: MsgPasser) {
    val imeLayoutConfig = IMELayoutConfig(context)
    val kbdDb by uiLazy { KbdDb(context) }
    val composePinyinObservable = BehaviorSubject.create<Boolean>()

    fun dispose() {
        kbdDb.dispose()
    }
}