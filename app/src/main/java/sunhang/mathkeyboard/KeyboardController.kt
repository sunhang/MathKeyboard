package sunhang.mathkeyboard

import android.view.View
import sunhang.mathkeyboard.kbdsource.KbdDataSource

class KeyboardController : BaseController() {
    private lateinit var keyboardView: View

    override fun onCreate(imsContext: IMSContext, rootView: View) {
        super.onCreate(imsContext, rootView)

        KbdDataSource(imsContext.context).enKbdModel().subscribe {

        }.let { compositeDisposable.add(it) }
    }
}