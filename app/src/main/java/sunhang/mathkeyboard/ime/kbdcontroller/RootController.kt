package sunhang.mathkeyboard.ime.kbdcontroller

import android.inputmethodservice.InputMethodService
import android.widget.FrameLayout
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdskin.*
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.runOnFile

class RootController(val imsContext: IMSContext, val rootView: RootView) : BaseController(), SkinAttrUser {
    private val keyboardController = KeyboardController(imsContext, rootView)
    private val composePinyinController = ComposePinyinController(imsContext, rootView.findViewById(R.id.layer_above_ime))
    val candiController = CandiController(imsContext, rootView)

    init {
        attach(keyboardController)
        attach(candiController)
        attach(composePinyinController)
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

    fun onComputeInsets(outInsets: InputMethodService.Insets?) {
        if (outInsets == null) {
            return
        }

        val location = IntArray(2)
        rootView.topView.getLocationInWindow(location)

        with(outInsets) {
            contentTopInsets = location[1]
            visibleTopInsets = location[1]
        }
    }

    override fun onCreateInputViewInvoked() {
        super.onCreateInputViewInvoked()

        val topViewLp = rootView.topView.layoutParams
        val kbdViewLp = rootView.keyboardView.layoutParams
        val wallpaperLp = rootView.wallpaperView.layoutParams
        val imeLayerLp = rootView.findViewById<FrameLayout>(R.id.ime_layer).layoutParams
        val layoutConfig = imsContext.imeLayoutConfig

        topViewLp.height = layoutConfig.toolbarHeight
        kbdViewLp.height = layoutConfig.keyboardHeight
        wallpaperLp.height = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight
        imeLayerLp.height = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight

        rootView.inputAreaMaxHeight = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight + composePinyinController.getContentHeight()
    }

    override fun useSkinAttr(skinModel: SkinModel) {
        super.useSkinAttr(skinModel)

        rootView.wallpaperView.background = skinModel.keyboardVisualAttributes.wallpaper
        rootView.topView.background = skinModel.keyboardVisualAttributes.topBackground
    }
}