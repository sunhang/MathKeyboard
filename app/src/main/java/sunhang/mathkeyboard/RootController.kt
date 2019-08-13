package sunhang.mathkeyboard

import sunhang.mathkeyboard.kbdviews.RootView

class RootController : BaseController(){
    private val keyboardController = KeyboardController()
    init {
        imsActions.add(keyboardController)
    }
    private lateinit var rootView: RootView
    private lateinit var imsContext: IMSContext

    override fun onCreate(imsContext: IMSContext, rootView: RootView) {
        super.onCreate(imsContext, rootView)
        this.rootView = rootView
        this.imsContext = imsContext
    }

    override fun onCreateInputViewInvoked() {
        super.onCreateInputViewInvoked()

        val topViewLp = rootView.topView.layoutParams
        val kbdViewLp = rootView.keyboardView.layoutParams
        val imeLayoutConfig = imsContext.imeLayoutConfig

        topViewLp.height = imeLayoutConfig.toolbarHeight
        kbdViewLp.height = imeLayoutConfig.keyboardHeight
    }
}