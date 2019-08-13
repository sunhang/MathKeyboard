package sunhang.mathkeyboard

import sunhang.mathkeyboard.kbdskin.KeyboardVisualAttributes
import sunhang.mathkeyboard.kbdskin.SkinAttrUser
import sunhang.mathkeyboard.kbdsource.KbdDataSource
import sunhang.mathkeyboard.kbdviews.KeyboardView
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.screenWidth

class KeyboardController : BaseController() {
    private lateinit var keyboardView: KeyboardView

    override fun onCreate(imsContext: IMSContext, rootView: RootView) {
        super.onCreate(imsContext, rootView)
        keyboardView = rootView.keyboardView

        val config = imsContext.imeLayoutConfig
        val context = imsContext.context
        val imeHeight = config.toolbarHeight + config.keyboardHeight

        KbdDataSource(imsContext.context).enKbdModel(context.screenWidth, imeHeight).subscribe {
            keyboardView.keyboard = it
        }.let { compositeDisposable.add(it) }
    }

    override fun useSkinAttr(skinAttri: KeyboardVisualAttributes) {
        super.useSkinAttr(skinAttri)
    }

}