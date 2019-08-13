package sunhang.mathkeyboard

import sunhang.mathkeyboard.kbdsource.KbdDataSource
import sunhang.mathkeyboard.kbdviews.KeyboardView
import sunhang.mathkeyboard.kbdviews.RootView

class KeyboardController : BaseController() {
    private lateinit var keyboardView: KeyboardView

    override fun onCreate(imsContext: IMSContext, rootView: RootView) {
        super.onCreate(imsContext, rootView)
        keyboardView = rootView.keyboardView

        KbdDataSource(imsContext.context).enKbdModel().subscribe {
            keyboardView.keyboard = it
        }.let { compositeDisposable.add(it) }
    }
}