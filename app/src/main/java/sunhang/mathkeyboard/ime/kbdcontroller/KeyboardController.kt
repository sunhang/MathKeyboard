package sunhang.mathkeyboard.ime.kbdcontroller

import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.InputToEditor
import sunhang.mathkeyboard.kbdmodel.Key
import sunhang.mathkeyboard.kbdmodel.Keyboard
import sunhang.mathkeyboard.kbdskin.KeyboardVisualAttributes
import sunhang.mathkeyboard.kbdsource.KbdDataSource
import sunhang.mathkeyboard.kbdviews.KeyboardView
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.screenWidth

class KeyboardController : BaseController() {
    private lateinit var keyboardView: KeyboardView
    private lateinit var imsContext: IMSContext

    override fun onCreate(imsContext: IMSContext, rootView: RootView) {
        super.onCreate(imsContext, rootView)
        this.imsContext = imsContext
        this.keyboardView = rootView.keyboardView

        val config = imsContext.imeLayoutConfig
        val context = imsContext.context
        val imeHeight = config.toolbarHeight + config.keyboardHeight

        KbdDataSource(imsContext.context).enKbdModel(context.screenWidth, imeHeight).subscribe {
            setListener(it)
            keyboardView.updateData(it)
        }.let { compositeDisposable.add(it) }
    }

    private fun setListener(keyboard: Keyboard) {
        keyboard.keys.forEach {
            it.onKeyClickedListener = onKeyClickedListener
        }
    }

    override fun useSkinAttr(skinAttri: KeyboardVisualAttributes) {
        super.useSkinAttr(skinAttri)
    }

    private val onKeyClickedListener = object : Key.OnKeyClickedListener {
        override fun onClick(code: Int, key: Key) {
            imsContext.inputToEditor.inputChar(code)
        }
    }
}