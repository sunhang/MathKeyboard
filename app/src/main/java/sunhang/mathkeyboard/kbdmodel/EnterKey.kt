package sunhang.mathkeyboard.kbdmodel

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import protoinfo.KbdInfo
import sunhang.mathkeyboard.R

class EnterKey(context: Context, keyInfo: KbdInfo.KeyInfo) : IconKey(context, keyInfo) {
    private val icon = ContextCompat.getDrawable(context, R.drawable.key_enter)!!

    override fun getCurrentIcon() = icon
}