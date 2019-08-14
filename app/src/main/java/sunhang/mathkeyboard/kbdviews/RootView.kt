package sunhang.mathkeyboard.kbdviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import sunhang.mathkeyboard.R

class RootView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    lateinit var topView: View
    lateinit var keyboardView: KeyboardView
    lateinit var wallpaperView: ImageView
    var inputAreaMaxHeight: Int = 0
    var extractViewShown = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        topView = findViewById(R.id.top_view)
        keyboardView = findViewById(R.id.kbd)
        wallpaperView = findViewById(R.id.iv_wallpaper)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(inputAreaMaxHeight, View.MeasureSpec.EXACTLY))
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, inputAreaMaxHeight)
    }
}