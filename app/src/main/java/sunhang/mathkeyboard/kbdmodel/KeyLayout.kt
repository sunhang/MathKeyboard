package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.RectF
import protoinfo.KbdInfo

class KeyLayout(rectInfo: KbdInfo.RectInfo) {
    /**
     * 可点击区域
     */
    val touchRect = RectF(
        rectInfo.x,
        rectInfo.y,
        rectInfo.x + rectInfo.width,
        rectInfo.y + rectInfo.height
    )
    /**
     * 可见区域
     */
    val visualRect = RectF(touchRect).apply { inset(rectInfo.horPadding, rectInfo.verPadding) }
}