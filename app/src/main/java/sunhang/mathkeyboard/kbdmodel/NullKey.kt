package sunhang.mathkeyboard.kbdmodel

import android.graphics.Canvas
import protoinfo.KbdInfo
import sunhang.mathkeyboard.GlobalVariable

object NullKey : Key(GlobalVariable.context, KbdInfo.KeyInfo.getDefaultInstance()){
    override fun onDraw(canvas: Canvas) {
        // Do nothing
    }

    override fun onTouch(touchEvent: TouchEvent) {
        // Do nothing
    }
}