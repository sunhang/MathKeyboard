package sunhang.mathkeyboard.ime.kbdcontroller

import android.view.ViewGroup
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdskin.KbdVisualAttrFactory
import sunhang.mathkeyboard.kbdskin.KeyboardVisualAttributes
import sunhang.mathkeyboard.kbdskin.SkinAttrUser
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.mathkeyboard.tools.setLayoutParamSize
import sunhang.openlibrary.runOnFile

class RootController : BaseController(), SkinAttrUser {
    private val keyboardController = KeyboardController()

    init {
        attach(keyboardController)
    }

    private lateinit var rootView: RootView
    private lateinit var imsContext: IMSContext

    override fun onCreate(imsContext: IMSContext, rootView: RootView) {
        super.onCreate(imsContext, rootView)
        this.rootView = rootView
        this.imsContext = imsContext

        runOnFile({
            KbdVisualAttrFactory().create(imsContext.context)
        }, {
            it?.let {
                useSkinAttr(it)
            }
        })
    }

    override fun onCreateInputViewInvoked() {
        super.onCreateInputViewInvoked()

        val topViewLp = rootView.topView.layoutParams
        val kbdViewLp = rootView.keyboardView.layoutParams
        val wallpaperLp = rootView.wallpaperView.layoutParams
        val layoutConfig = imsContext.imeLayoutConfig

        topViewLp.height = layoutConfig.toolbarHeight
        kbdViewLp.height = layoutConfig.keyboardHeight
        wallpaperLp.height = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight
        rootView.inputAreaMaxHeight = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight
    }

    override fun useSkinAttr(skinAttri: KeyboardVisualAttributes) {
        super.useSkinAttr(skinAttri)

        rootView.wallpaperView.background = skinAttri.wallpaper
    }
}