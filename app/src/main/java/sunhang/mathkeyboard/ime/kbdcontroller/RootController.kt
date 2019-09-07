package sunhang.mathkeyboard.ime.kbdcontroller

import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdskin.*
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.runOnFile

class RootController(val imsContext: IMSContext, private val rootView: RootView) : BaseController(), SkinAttrUser {
    private val keyboardController = KeyboardController(imsContext, rootView)
    val candiController = CandiController(imsContext, rootView)

    init {
        attach(keyboardController)
        attach(candiController)
    }

    override fun onCreate() {
        super.onCreate()

        runOnFile({
            SkinModelFactory().create(imsContext.context)
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

    override fun useSkinAttr(skinModel: SkinModel) {
        super.useSkinAttr(skinModel)

        rootView.wallpaperView.background = skinModel.keyboardVisualAttributes.wallpaper
    }
}