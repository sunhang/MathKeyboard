package sunhang.mathkeyboard.ime.kbdcontroller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.mathkeyboard.tools.dp2Px
import sunhang.mathkeyboard.tools.sp2Px
import sunhang.openlibrary.uiLazy
import kotlin.math.roundToInt

class NumKbdColumn(private val imsContext: IMSContext, private val rootView: RootView) : BaseController() {
    private val numColumnRootView: ScrollView by uiLazy {
        View.inflate(imsContext.context, R.layout.kbd_num_column, null) as ScrollView
    }
//    private

    override fun onCreate() {
        super.onCreate()
    }

    fun showView() {

    }

    fun dismissView() {

    }
}