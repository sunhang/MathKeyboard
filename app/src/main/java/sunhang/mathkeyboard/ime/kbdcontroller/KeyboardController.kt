package sunhang.mathkeyboard.ime.kbdcontroller

import protoinfo.KbdInfo
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdmodel.*
import sunhang.mathkeyboard.kbdskin.KeyboardVisualAttributes
import sunhang.mathkeyboard.kbdsource.KbdDataSource
import sunhang.mathkeyboard.kbdviews.KeyboardView
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.screenWidth

class KeyboardController : BaseController() {
    private lateinit var keyboardView: KeyboardView
    private lateinit var imsContext: IMSContext
    private var keyboardVisualAttributes: KeyboardVisualAttributes? = null
    private var shiftState = ShiftState.UNSHIFT
    private lateinit var kbdDataSource: KbdDataSource

    override fun onCreate(imsContext: IMSContext, rootView: RootView) {
        super.onCreate(imsContext, rootView)
        this.imsContext = imsContext
        this.keyboardView = rootView.keyboardView
        kbdDataSource = KbdDataSource(imsContext.context)

        val config = imsContext.imeLayoutConfig
        val context = imsContext.context
        val imeHeight = config.keyboardHeight

        kbdDataSource.enKbdModel(context.screenWidth, imeHeight)
            .subscribe { keyboard ->
                setListener(keyboard)
                keyboardView.updateData(keyboard)
                keyboardVisualAttributes?.let {
                    setKbdVisualAttr(keyboard, it)
                }
            }.let { compositeDisposable.add(it) }
    }

    private fun setListener(keyboard: Keyboard) {
        keyboard.keys.forEach { key ->
            when (key) {
                is ShiftKey -> {
                    key.shiftStateListener = onShiftStateListener
                }
                else -> {
                    key.onKeyClickedListener = onKeyClickedListener
                }
            }
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

    private fun changeUppercaseOfKeys(shiftState: ShiftState) {
        keyboardView.keyboard.keys.forEach { key ->
            if (key is UpperCaseSupportedKey) {
                key.upperCase = shiftState != ShiftState.UNSHIFT
            }
        }
    }

    private val onKeyClickedListener = object : Key.OnKeyClickedListener {
        override fun onClick(code: Int, key: Key) {
            imsContext.inputToEditor.inputChar(code)

            if (shiftState == ShiftState.SHIFT) {
                shiftState = ShiftState.UNSHIFT
                changeUppercaseOfKeys(shiftState)
                keyboardView.keyboard.shiftKey?.resetShiftState()
            }
        }
    }

    private val onShiftStateListener = object : ShiftKey.ShiftStateListener {
        override fun onChanged(shiftState: ShiftState) {
            changeUppercaseOfKeys(shiftState)
            this@KeyboardController.shiftState = shiftState
        }

    }
}