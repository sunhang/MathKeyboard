package sunhang.mathkeyboard.ime.kbdcontroller

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.ViewGroup

class MyVg(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val child = getChildAt(0)
        child.layout(0, t, r, b)
    }
}