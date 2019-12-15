package sunhang.mathkeyboard.ime.kbdcontroller

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.TextView
import io.reactivex.disposables.Disposable
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdskin.SkinAttrUser
import sunhang.mathkeyboard.kbdskin.SkinModel
import sunhang.mathkeyboard.tools.dp2Px
import sunhang.openlibrary.uiLazy

// todo 需要考虑横竖屏切换
// todo 需要考虑皮肤
class PinyinComposeController(
    private val imsContext: IMSContext,
    val layer: FrameLayout
) : BaseController(), SkinAttrUser {
    private val context = imsContext.context
    private val view by uiLazy { View.inflate(context, R.layout.compose_pinyin, null) }
    private lateinit var disposable: Disposable

    override fun onCreate() {
        super.onCreate()

        disposable = imsContext.composePinyinObservable.subscribe {
            if (it) {
                showView()
            } else {
                dismissView()
            }
        }
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
        disposable.dispose()
    }

    override fun useSkinAttr(skinModel: SkinModel) {
        super.useSkinAttr(skinModel)
    }

    fun setPyCompose(pyCompose: String) {
        val tvPinyin = view.findViewById<TextView>(R.id.tv_pinyin)
        tvPinyin.text = pyCompose
        val hsv = view as HorizontalScrollView
        hsv.post {
            hsv.fullScroll(View.FOCUS_RIGHT)
        }
    }

    fun getContentHeight(): Int {
        return if (view.parent == null) 0 else view.layoutParams.height
    }

    private fun showView() {
        if (view.parent != null) {
            return
        }

        val lp =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dp2Px(30.0f).toInt())
        layer.addView(view, lp)
    }

    private fun dismissView() {
        val parent = view.parent as? FrameLayout
        parent?.let {
            it.removeView(view)
        }
    }
}