package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.content.Context
import sunhang.mathkeyboard.R

enum class SymType(private val textRes: Int) {
    RECENT_USED(R.string.symbol_titles_common),
    MATHEMATICS(R.string.symbol_titles_math),
    EN_SYMBOL(R.string.symbol_titles_en),
    ZH_SYMBOL(R.string.symbol_titles_zh);

    fun textStr(context: Context) = context.getString(textRes) ?: ""
}

val symTypes = SymType.values()
