package sunhang.mathkeyboard.tools

import android.util.Log
import sunhang.mathkeyboard.BuildConfig

private val TAG = "math_keyboard"

fun i(message: String) {
    if (BuildConfig.DEBUG) {
        Log.i(TAG, message)
    }
}