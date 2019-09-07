package sunhang.mathkeyboard.tools

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable

fun buildStateListDrawable(normalColor: Int, pressedColor: Int): StateListDrawable {
    return StateListDrawable().apply {
        addState(PRESSED, ColorDrawable(pressedColor))
        addState(NORMAL, ColorDrawable(normalColor))
    }
}

val SELECTED = intArrayOf(android.R.attr.state_selected)

val SELECTED_PRESSED = intArrayOf(android.R.attr.state_selected, android.R.attr.state_pressed)

val PRESSED = intArrayOf(android.R.attr.state_pressed)

val FOCUSED = intArrayOf(android.R.attr.state_focused)

val NORMAL = intArrayOf(android.R.attr.state_enabled)

val DISABLED = intArrayOf(-android.R.attr.state_enabled)
