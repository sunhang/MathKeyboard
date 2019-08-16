package sunhang.mathkeyboard.ime.kbdcontroller

import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.base.common.CloneableNinePatchDrawable
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdskin.KeyboardVisualAttributes
import sunhang.mathkeyboard.kbdviews.NumColumnView
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.uiLazy

class NumKbdColumn(private val imsContext: IMSContext, private val rootView: RootView) : BaseController() {
    private val numColumnRootView by uiLazy {
        View.inflate(imsContext.context, R.layout.kbd_num_column, null) as ScrollView
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun useSkinAttr(skinAttri: KeyboardVisualAttributes) {
        super.useSkinAttr(skinAttri)
        val attr = skinAttri.keyVisualAttr
        numColumnRootView.background = if (attr.keyBackground is CloneableNinePatchDrawable) {
            attr.keyBackground.makeClone()
        } else {
            attr.keyBackground
        }

        with(numColumnRootView.findViewById<NumColumnView>(R.id.num_column)) {
            textColor = attr.keyLabelColor
            pressedTextColor = attr.keyLabelColorPressed
            linePaint.color = attr.keyLabelColor
        }
    }

    fun showView(marginLeft: Int, marginTop: Int, marginBottom: Int, width: Int) {
        if (numColumnRootView.parent != null) {
            return
        }

        val lp = FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.MATCH_PARENT)
        lp.leftMargin = marginLeft
        lp.topMargin = marginTop
        lp.bottomMargin = marginBottom

        rootView.findViewById<FrameLayout>(R.id.kbd_layer)
            .addView(numColumnRootView, lp)
    }

    fun dismissView() {
        val parent = numColumnRootView.parent
        (parent as? FrameLayout)?.let {
            it.removeView(numColumnRootView)
        }
    }
}