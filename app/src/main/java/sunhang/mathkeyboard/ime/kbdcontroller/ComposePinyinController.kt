package sunhang.mathkeyboard.ime.kbdcontroller

import android.graphics.Color
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdskin.SkinAttrUser
import sunhang.mathkeyboard.kbdskin.SkinModel
import sunhang.mathkeyboard.tools.dp2Px
import sunhang.openlibrary.uiLazy

// todo 需要考虑横竖屏切换
class ComposePinyinController(
    private val imsContext: IMSContext,
    val layer: FrameLayout
) : BaseController(), SkinAttrUser {
    private val context = imsContext.context
    private val view by uiLazy { View.inflate(context, R.layout.compose_pinyin, null) }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onCreateInputViewInvoked() {
        super.onCreateInputViewInvoked()
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun useSkinAttr(skinModel: SkinModel) {
        super.useSkinAttr(skinModel)
    }

    fun getContentHeight(): Int {
        return if (view.parent == null) 0 else view.layoutParams.height
    }

    fun showView() {
        if (view.parent != null) {
            return
        }

        view.setBackgroundColor(Color.RED)
        layer.setBackgroundColor(Color.RED)
        val lp =
            FrameLayout.LayoutParams(dp2Px(120.0f).toInt(), dp2Px(30.0f).toInt())
        layer.addView(view, lp)
    }

    fun dismissView() {
        val parent = view.parent as? FrameLayout
        parent?.let {
            it.removeView(view)
        }
    }
}