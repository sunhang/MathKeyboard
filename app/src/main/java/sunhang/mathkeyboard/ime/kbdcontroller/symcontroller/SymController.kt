package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.view.View
import android.view.ViewGroup
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSAction
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.BaseController
import sunhang.mathkeyboard.kbdskin.SkinAttrUser
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.uiLazy

class SymController(private val imsContext: IMSContext, rootView: RootView) : BaseController(),
    IMSAction, SkinAttrUser {
    private val containter = rootView.findViewById<ViewGroup>(R.id.ime_layer)
    private val context = imsContext.context
    private val layout by uiLazy { View.inflate(context, R.layout.kbd_sym, null) }

    fun show() {
        if (layout.parent == null) {
            containter.addView(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    fun hide() {
        if (layout.parent != null) {
            containter.removeView(layout)
        }
    }
}