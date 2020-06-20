package sunhang.mathkeyboard.ime.kbdcontroller

import android.opengl.GLSurfaceView
import android.view.TextureView
import protoinfo.KbdInfo
import sunhang.mathkeyboard.*
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.symcontroller.SymController
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.kbdmodel.*
import sunhang.mathkeyboard.kbdskin.KeyboardVisualAttributes
import sunhang.mathkeyboard.kbdskin.SkinModel
import sunhang.mathkeyboard.kbdsource.KbdDataSource
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.screenWidth
import java.util.*
import kotlin.math.roundToInt

class KeyboardController(private val imsContext: IMSContext, private val rootView: RootView) :
    BaseController() {
    private val context = imsContext.context
    private val keyboardView = rootView.keyboardView
    private val kbdDataSource = KbdDataSource(imsContext.context)
    private var keyboardVisualAttributes: KeyboardVisualAttributes? = null
    private var shiftState = ShiftState.UNSHIFT
    private val numKbdColumn = NumKbdColumn(imsContext, rootView)
    private val planeStack = PlaneStack()
    private val symController = SymController(imsContext, rootView)

    init {
        attach(numKbdColumn)
        attach(symController)
    }

    /**
     * 为回退键盘服务
     */
    class PlaneStack {
        private val stack = LinkedList<PlaneType>()

        fun push(planeType: PlaneType) {
            stack.push(planeType)

            while (stack.size > 3) {
                stack.removeLast()
            }
        }

        fun pop(): PlaneType? {
            if (stack.size == 0) {
                return null
            }

            return stack.pop()
        }

        fun peek() = stack.peek()
    }

    override fun onCreate() {
        super.onCreate()
        symController.onSymPlaneBack = onSymPlaneBack
        loadKeyboardData(PlaneType.QWERTY_ZH)
        imsContext.logicMsgPasser.passMessage(Msg.Logic.PLANE_TYPE, PlaneType.QWERTY_ZH)
    }

    private fun loadKeyboardData(planeType: PlaneType) {
        val config = imsContext.imeLayoutConfig
        val keyboardHeight = config.keyboardHeight

        kbdDataSource.getKbdModel(planeType, context.screenWidth, keyboardHeight)
            .subscribe {
                handleKeyboard(planeType, it)
            }.let { compositeDisposable.add(it) }
    }

    private fun handleKeyboard(planeType: PlaneType, keyboard: Keyboard) {
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
            width
        )

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

    override fun useSkinAttr(skinModel: SkinModel) {
        super.useSkinAttr(skinModel)
        keyboardVisualAttributes = skinModel.keyboardVisualAttributes
        setKbdVisualAttr(keyboardView.keyboard, skinModel.keyboardVisualAttributes)
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
//            key.normalBackground = keyAttr.keyBackground
//            key.pressedBackground = keyAttr.keyBackgroundPressed
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
                CODE_SWITCH_MAIN -> PlaneType.QWERTY_ZH
                CODE_SWITCH_EN_QWERTY -> PlaneType.QWERTY_EN
//                CODE_SWITCH_MATH_SYM -> PlaneType.MATH_SYMBOL_0
                CODE_SWITCH_SYMBOL -> PlaneType.SYMBOL
                else -> null
            }

            when (planeType) {
                null -> {
                    imsContext.logicMsgPasser.passMessage(Msg.Logic.CODE, code)
                }
                PlaneType.SYMBOL -> {
                    val (x, y) = with(key.visualRect) {
                        Pair(centerX().toInt(), centerY().toInt() + rootView.keyboardView.top)
                    }
                    symController.show(x, y)
                }
                else -> {
                    loadKeyboardData(planeType)
                }
            }

            if (planeType != null) {
                planeStack.push(planeType)
                imsContext.logicMsgPasser.passMessage(Msg.Logic.PLANE_TYPE, planeType)
            }
        }
    }

    private val onSymPlaneBack = {
        planeStack.pop()
        val planeType = planeStack.peek() ?: PlaneType.QWERTY_ZH

        if (planeType != PlaneType.SYMBOL) {
            loadKeyboardData(planeType)
        }
        imsContext.logicMsgPasser.passMessage(Msg.Logic.PLANE_TYPE, planeType)
    }

    private val onShiftStateListener = object : ShiftKey.ShiftStateListener {
        override fun onChanged(shiftState: ShiftState) {
            changeUppercaseOfKeys(shiftState)
            this@KeyboardController.shiftState = shiftState
        }

    }
}