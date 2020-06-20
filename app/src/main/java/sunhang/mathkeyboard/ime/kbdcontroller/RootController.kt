package sunhang.mathkeyboard.ime.kbdcontroller

import android.opengl.GLSurfaceView
import android.os.Handler
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.ime_layout.view.*
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdskin.*
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.runOnFile

class RootController(val imsContext: IMSContext, val rootView: RootView) : BaseController(), SkinAttrUser {
    private val keyboardController = KeyboardController(imsContext, rootView)
    val candiController = CandiController(imsContext, rootView)
    private val glController = GLController(imsContext, rootView)

    init {
        attach(keyboardController)
        attach(candiController)
        attach(glController)
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
        val imeLayerLp = rootView.findViewById<FrameLayout>(R.id.ime_layer).layoutParams
        val layoutConfig = imsContext.imeLayoutConfig

//        rootView.findViewById<GLSurfaceView>(R.id.gl_sv).layoutParams.height = layoutConfig.keyboardHeight

        topViewLp.height = layoutConfig.toolbarHeight
        kbdViewLp.height = layoutConfig.keyboardHeight
        wallpaperLp.height = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight
        imeLayerLp.height = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight
        rootView.inputAreaMaxHeight = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight
   }

    override fun useSkinAttr(skinModel: SkinModel) {
        super.useSkinAttr(skinModel)

//        rootView.wallpaperView.background = skinModel.keyboardVisualAttributes.wallpaper
        rootView.topView.background = skinModel.keyboardVisualAttributes.topBackground
    }
}