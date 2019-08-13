package sunhang.mathkeyboard.kbdviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import sunhang.mathkeyboard.R

class RootView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    lateinit var topView: View
    lateinit var keyboardView: View
    var extractViewShown = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        topView = findViewById(R.id.top_view)
        keyboardView = findViewById(R.id.kbd)
    }

    /*
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec

        val height: Int
        if (isPortrait(context)) {
            height = kbdConfig.getSearchLayerHeight() + mInputAreaMaxHeight
        } else {
            val extraHeight = if (mExtractViewShown) 0 else KeyboardConfig.getInstance().getPinyinHeight()
            height = topView!!.layoutParams.height + mKeyboardLayer!!.layoutParams.height + extraHeight
        }

        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))

        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, height)
    }*/
}