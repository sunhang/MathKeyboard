package sunhang.mathkeyboard.ime.kbdcontroller

import android.inputmethodservice.InputMethodService
import android.opengl.GLSurfaceView
import android.os.Handler
import android.view.ViewGroup
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

    private var high = false
    private var top = 0

    override fun onCreate() {
        super.onCreate()

        keyboardController.callback = {
            high = it

            val layoutConfig = imsContext.imeLayoutConfig
            if (high) {
                (rootView.keyboardView.layoutParams as ViewGroup.MarginLayoutParams).height =
                    layoutConfig.keyboardHeight + top / 2
                rootView.findViewById<MyVg>(R.id.fl).layoutParams.height = top + layoutConfig.keyboardHeight
            } else {
                (rootView.keyboardView.layoutParams as ViewGroup.MarginLayoutParams).height =
                    layoutConfig.keyboardHeight
                rootView.findViewById<MyVg>(R.id.fl).layoutParams.height = top / 2 + layoutConfig.keyboardHeight
            }

            rootView.requestLayout()
        }

        runOnFile({
            SkinModelFactory().create(imsContext.context)
        }, {
            it?.let {
                useSkinAttr(it)
            }
        })
    }

    fun onComputeInsets(outInsets: InputMethodService.Insets?) {
        val arr = IntArray(2)
        rootView.topView.getLocationInWindow(arr)
        outInsets?.run {
            contentTopInsets = arr[1]
            visibleTopInsets = arr[1]
        }
    }

    override fun onCreateInputViewInvoked() {
        super.onCreateInputViewInvoked()

        val topViewLp = rootView.topView.layoutParams
        val kbdViewLp = rootView.keyboardView.layoutParams
        val wallpaperLp = rootView.wallpaperView.layoutParams
        val imeLayerLp = rootView.findViewById<FrameLayout>(R.id.ime_layer).layoutParams
        val layoutConfig = imsContext.imeLayoutConfig

        rootView.findViewById<MyVg>(R.id.fl).layoutParams.height = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight

//        rootView.findViewById<GLSurfaceView>(R.id.gl_sv).layoutParams.height = layoutConfig.keyboardHeight

        topViewLp.height = layoutConfig.toolbarHeight
        kbdViewLp.height = layoutConfig.keyboardHeight
        wallpaperLp.height = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight
        imeLayerLp.height = layoutConfig.toolbarHeight + layoutConfig.keyboardHeight

        top = layoutConfig.toolbarHeight * 2
        rootView.inputAreaMaxHeight =  top + layoutConfig.keyboardHeight
   }

    override fun useSkinAttr(skinModel: SkinModel) {
        super.useSkinAttr(skinModel)

//        rootView.wallpaperView.background = skinModel.keyboardVisualAttributes.wallpaper
        rootView.topView.background = skinModel.keyboardVisualAttributes.topBackground
    }
}