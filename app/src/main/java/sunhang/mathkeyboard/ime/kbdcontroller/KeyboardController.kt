package sunhang.mathkeyboard.ime.kbdcontroller

import protoinfo.KbdInfo
import sunhang.mathkeyboard.ime.IMSContext
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
    private var keyboardVisualAttributes: KeyboardVisualAttributes? = null

    override fun onCreate(imsContext: IMSContext, rootView: RootView) {
        super.onCreate(imsContext, rootView)
        this.imsContext = imsContext
        this.keyboardView = rootView.keyboardView

        val config = imsContext.imeLayoutConfig
        val context = imsContext.context
        val imeHeight = config.keyboardHeight

        KbdDataSource(imsContext.context).enKbdModel(context.screenWidth, imeHeight)
            .subscribe { keyboard ->
                setListener(keyboard)
                keyboardView.updateData(keyboard)
                keyboardVisualAttributes?.let {
                    setKbdVisualAttr(keyboard, it)
                }
            }.let { compositeDisposable.add(it) }
    }

    private fun setListener(keyboard: Keyboard) {
        keyboard.keys.forEach {
            it.onKeyClickedListener = onKeyClickedListener
        }
    }

    override fun useSkinAttr(skinAttri: KeyboardVisualAttributes) {
        super.useSkinAttr(skinAttri)
        keyboardVisualAttributes = skinAttri
        setKbdVisualAttr(keyboardView.keyboard, skinAttri)
    }

    private fun setKbdVisualAttr(keyboard: Keyboard, skinAttri: KeyboardVisualAttributes) {
        val letterKeyAttr = skinAttri.keyVisualAttr
        val specialKeyAttr = skinAttri.specialUil
        val highLightAttr = skinAttri.funcHighLight

        keyboard.keys.forEach { key ->
            val keyAttr = when (key.keyInfo.keyColor) {
                KbdInfo.KeyColor.COLOR_NORMAL -> letterKeyAttr
                KbdInfo.KeyColor.SPECIAL -> specialKeyAttr
                else -> highLightAttr
            }
            key.normalColor = keyAttr.keyLabelColor
            key.pressedColor = keyAttr.keyLabelColorPressed
            key.normalBackground = keyAttr.keyBackground
            key.pressedBackground = keyAttr.keyBackgroundPressed
        }
        keyboardView.invalidate()
    }

    private val onKeyClickedListener = object : Key.OnKeyClickedListener {
        override fun onClick(code: Int, key: Key) {
            imsContext.inputToEditor.inputChar(code)
        }
    }
}