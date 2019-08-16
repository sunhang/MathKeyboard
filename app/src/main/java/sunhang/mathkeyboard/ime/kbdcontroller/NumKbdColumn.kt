package sunhang.mathkeyboard.ime.kbdcontroller

import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.uiLazy

class NumKbdColumn(private val imsContext: IMSContext, private val rootView: RootView) : BaseController() {
    private val numColumnRootView by uiLazy {
        View.inflate(imsContext.context, R.layout.kbd_num_column, null) as ScrollView
    }

    override fun onCreate() {
        super.onCreate()
    }

    fun showView() {
        if (numColumnRootView.parent != null) {
            return
        }

        rootView.findViewById<FrameLayout>(R.id.kbd_layer)
            .addView(numColumnRootView)
    }

    fun dismissView() {
        val parent = numColumnRootView.parent
        (parent as? FrameLayout)?.let {
            it.removeView(numColumnRootView)
        }
    }
}