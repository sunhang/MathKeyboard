package sunhang.mathkeyboard.kbdmodel.nullobj

import android.graphics.Canvas
import protoinfo.KbdInfo
import sunhang.mathkeyboard.GlobalVariable
import sunhang.mathkeyboard.kbdmodel.Key
import sunhang.mathkeyboard.kbdmodel.TouchEvent

object NullKey : Key(GlobalVariable.context, KbdInfo.KeyInfo.getDefaultInstance()){
    override fun onDraw(canvas: Canvas) {
        // Do nothing
    }

    override fun onTouch(touchEvent: TouchEvent) {
        // Do nothing
    }
}