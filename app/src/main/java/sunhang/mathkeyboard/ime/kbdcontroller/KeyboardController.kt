package sunhang.mathkeyboard.ime.kbdcontroller

import io.reactivex.functions.Consumer
import protoinfo.KbdInfo
import sunhang.mathkeyboard.CODE_SWITCH_EN_QWERTY
import sunhang.mathkeyboard.CODE_SWITCH_NUM_SODUKU
import sunhang.mathkeyboard.CODE_SWITCH_SYMBOL
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdmodel.*
import sunhang.mathkeyboard.kbdskin.KeyboardVisualAttributes
import sunhang.mathkeyboard.kbdsource.KbdDataSource
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.screenWidth
import kotlin.math.roundToInt

class KeyboardController(private val imsContext: IMSContext, private val rootView: RootView) : BaseController() {
    private val context = imsContext.context
    private val keyboardView = rootView.keyboardView
    private val kbdDataSource = KbdDataSource(imsContext.context)
    private var keyboardVisualAttributes: KeyboardVisualAttributes? = null
    private var shiftState = ShiftState.UNSHIFT
    private val numKbdColumn = NumKbdColumn(imsContext, rootView)
    private var planeType: PlaneType = PlaneType.QWERTY_EN

    init {
        attach(numKbdColumn)
    }

    override fun onCreate() {
        super.onCreate()
        loadKeyboardData(PlaneType.QWERTY_EN)
    }

    private fun loadKeyboardData(planeType: PlaneType) {
        val config = imsContext.imeLayoutConfig
        val keyboardHeight = config.keyboardHeight

        val observable = when (planeType) {
            PlaneType.NUMBER -> kbdDataSource.numKbdModel(context.screenWidth, keyboardHeight)
            PlaneType.MATH_SYMBOL -> kbdDataSource.mathSymbol0KbdModel(context.screenWidth, keyboardHeight)
            else -> kbdDataSource.enKbdModel(context.screenWidth, keyboardHeight)
        }
        observable.subscribe(keyboardConsumer(planeType)).let { compositeDisposable.add(it) }
    }

    private fun keyboardConsumer(planeType: PlaneType): Consumer<Keyboard> {
        return Consumer<Keyboard>() { keyboard ->
            setListener(keyboard)
            keyboardView.updateData(keyboard)
            keyboardVisualAttributes?.let {
                setKbdVisualAttr(keyboard, it)
            }

            if (planeType == PlaneType.NUMBER) {
                numKbdColumn.showViewAlignedWithKbd(keyboard)
            } else {
                numKbdColumn.dismissView()
            }
        }
    }

    private fun NumKbdColumn.showViewAlignedWithKbd(keyboard: Keyboard) {
        val keyIndex0 = keyboard.keys[0]
        val keyIndex8 = keyboard.keys[8]
        val keyIndex12 = keyboard.keys[12]

        val keyboardHeight = imsContext.imeLayoutConfig.keyboardHeight

        val marginLeft = keyIndex12.visualRect.left.roundToInt()
        val marginTop = keyIndex0.visualRect.top.roundToInt()
        val marginBottom = keyboardHeight - keyIndex8.visualRect.bottom.roundToInt()
        val width = keyIndex12.visualRect.width().roundToInt()
        showView(
            marginLeft,
            marginTop,
            marginBottom,
            width)

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

            val planeType = when (code) {
                CODE_SWITCH_NUM_SODUKU -> PlaneType.NUMBER
                CODE_SWITCH_EN_QWERTY -> PlaneType.QWERTY_EN
                CODE_SWITCH_SYMBOL -> PlaneType.MATH_SYMBOL
                else -> null
            }

            if (planeType != null) {
                loadKeyboardData(planeType)
                this@KeyboardController.planeType = planeType
            } else {
                imsContext.inputToEditor.inputChar(code)
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