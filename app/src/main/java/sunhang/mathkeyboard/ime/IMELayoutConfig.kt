package sunhang.mathkeyboard.ime

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import sunhang.openlibrary.getScreenWidthOfPortraitMode
import kotlin.math.roundToInt

class IMELayoutConfig(private val context: Context) : KbdResizeAction {
    private companion object {
        const val DEFAULT_TOOLBAR_HEIGHT_RATIO_PORT: Float = 0.11944444444444445f
        const val DEFAULT_KEYBOARD_HEIGHT_RATIO_PORT: Float = 0.6444444444444445f

        const val DEFAULT_TOOLBAR_HEIGHT_RATIO_LAND: Float = 0.08888888888888889f
        const val DEFAULT_KEYBOARD_HEIGHT_RATIO_LAND: Float = 0.4888888888888889f
    }

    private val prefPort: Pref
    private val prefLand: Pref

    private val _toolbarHeightPort: Value

    private val _keyboardHeightPort: Value

    private val _toolbarHeightLand: Value

    private val _keyboardHeightLand: Value

    private fun isPort() = context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val toolbarHeight: Int
        get() = (if (isPort()) _toolbarHeightPort else _toolbarHeightLand).getValue()

    val keyboardHeight: Int
        get() = (if (isPort()) _keyboardHeightPort else _keyboardHeightLand).getValue()

    val inputAreaMaxHeight: Int
        get() = if (isPort()) {
            _toolbarHeightPort.getMaxValue() + _keyboardHeightPort.getMaxValue()
        } else {
            _toolbarHeightLand.getMaxValue() + _keyboardHeightLand.getMaxValue()
        }

    val topViewAndKbdDefaultHeightWhenPort =
        (getScreenWidthOfPortraitMode(context) * (DEFAULT_TOOLBAR_HEIGHT_RATIO_PORT + DEFAULT_KEYBOARD_HEIGHT_RATIO_PORT)).roundToInt()

    init {
        val sp: SharedPreferences = context.getSharedPreferences("keyboard_size", Context.MODE_PRIVATE)

        prefPort = Pref("height_port", sp)
        prefLand = Pref("height_land", sp)

        // 返回屏幕在竖屏时的宽度
        val screenWidthWhenPort: Int = getScreenWidthOfPortraitMode(context)
        val defaultToolbarHeightPort: Int = (DEFAULT_TOOLBAR_HEIGHT_RATIO_PORT * screenWidthWhenPort).roundToInt()
        val defaultKeyboardHeightPort: Int = (DEFAULT_KEYBOARD_HEIGHT_RATIO_PORT * screenWidthWhenPort).roundToInt()
        val defaultToolbarHeightLand: Int = (DEFAULT_TOOLBAR_HEIGHT_RATIO_LAND * screenWidthWhenPort).roundToInt()
        val defaultKeyboardHeightLand: Int = (DEFAULT_KEYBOARD_HEIGHT_RATIO_LAND * screenWidthWhenPort).roundToInt()

        _toolbarHeightPort = Value(prefPort, defaultToolbarHeightPort)
        _keyboardHeightPort = Value(prefPort, defaultKeyboardHeightPort)
        _toolbarHeightLand = Value(prefLand, defaultToolbarHeightLand)
        _keyboardHeightLand = Value(prefLand, defaultKeyboardHeightLand)
    }

    override fun canHigher(): Boolean = if (isPort()) prefPort.canHigher() else prefLand.canHigher()

    override fun canLower(): Boolean = if (isPort()) prefPort.canLower() else prefLand.canLower()

    override fun isDefaultHeight() = (if (isPort()) prefPort else prefLand).isDefaultHeight()

    override fun makeHigher() = (if (isPort()) prefPort else prefLand).makeHigher()

    override fun makeLower() = (if (isPort()) prefPort else prefLand).makeLower()

    override fun lowerThanDefault() = (if (isPort()) prefPort else prefLand).lowerThanDefault()

    override fun currentLevel(): Int = currentLevel(isPort())

    fun currentLevel(port: Boolean): Int = (if (port) prefPort else prefLand).currentLevel()

    private class Pref(private val name: String, private val sp: SharedPreferences) : KbdResizeAction {
        companion object {
            const val MAX = 3
            const val MIN = -3
            const val BASE = 15.0f
        }

        private fun getSpValue() = sp.getInt(name, 0)

        private fun setSpValue(value: Int) = sp.edit().putInt(name, value).apply()

        fun getMaxValue(): Float = 1 + Pref.MAX / Pref.BASE

        fun getValue(): Float = 1 + getSpValue() / Pref.BASE

        /**
         * 是否可以继续变高
         */
        override fun canHigher() = getSpValue() < MAX

        /**
         * 是否可以继续变矮
         */
        override fun canLower() = getSpValue() > MIN

        override fun isDefaultHeight() = getSpValue() == 0

        override fun makeHigher() = setSpValue(adjust(getSpValue() + 1))

        override fun makeLower() = setSpValue(adjust(getSpValue() - 1))

        override fun lowerThanDefault() = getSpValue() < 0

        private fun adjust(value: Int): Int =
            when {
                (value > MAX) -> MAX
                (value < MIN) -> MIN
                else -> value
            }

        override fun currentLevel(): Int = getSpValue()
    }

    private class Value(private val pref: Pref, private val defaultHeight: Int) {

        fun getMaxValue(): Int = (defaultHeight * pref.getMaxValue()).toInt()

        fun getValue(): Int = (defaultHeight * pref.getValue()).toInt()
    }
}

private interface KbdResizeAction {
    fun makeHigher()
    fun makeLower()
    fun lowerThanDefault(): Boolean
    fun isDefaultHeight(): Boolean
    fun canHigher(): Boolean
    fun canLower(): Boolean
    fun currentLevel(): Int
}
