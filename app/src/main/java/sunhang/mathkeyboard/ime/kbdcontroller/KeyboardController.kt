package sunhang.mathkeyboard.ime.kbdcontroller

import io.reactivex.functions.Consumer
import org.reactivestreams.Subscriber
import protoinfo.KbdInfo
import sunhang.mathkeyboard.CODE_SWITCH_NUM_SODUKU
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdmodel.*
import sunhang.mathkeyboard.kbdskin.KeyboardVisualAttributes
import sunhang.mathkeyboard.kbdsource.KbdDataSource
import sunhang.mathkeyboard.kbdviews.KeyboardView
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.screenWidth

class KeyboardController(private val imsContext: IMSContext, private val rootView: RootView) : BaseController() {
    private val context = imsContext.context
    private val keyboardView = rootView.keyboardView
    private val kbdDataSource = KbdDataSource(imsContext.context)
    private var keyboardVisualAttributes: KeyboardVisualAttributes? = null
    private var shiftState = ShiftState.UNSHIFT

    override fun onCreate() {
        super.onCreate()
        loadKeyboardData(PlaneType.QWERTY_EN)
    }

    private fun loadKeyboardData(planeType: PlaneType) {
        val config = imsContext.imeLayoutConfig
        val keyboardHeight = config.keyboardHeight

        when (planeType) {
            PlaneType.NUMBER -> kbdDataSource.numKbdModel(context.screenWidth, keyboardHeight)
            else -> kbdDataSource.enKbdModel(context.screenWidth, keyboardHeight)
        }.subscribe(keyboardConsumer()).let { compositeDisposable.add(it) }
    }

    private fun keyboardConsumer(): Consumer<Keyboard> {
        return Consumer<Keyboard>() { keyboard ->
            setListener(keyboard)
            keyboardView.updateData(keyboard)
            keyboardVisualAttributes?.let {
                setKbdVisualAttr(keyboard, it)
            }
        }
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
            if (shiftState == ShiftState.SHIFT) {
                shiftState = ShiftState.UNSHIFT
                changeUppercaseOfKeys(shiftState)
                keyboardView.keyboard.shiftKey?.resetShiftState()
            }

            when (code) {
                CODE_SWITCH_NUM_SODUKU -> {
                    loadKeyboardData(PlaneType.NUMBER)
                }
                else -> {
                    imsContext.inputToEditor.inputChar(code)
                }
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