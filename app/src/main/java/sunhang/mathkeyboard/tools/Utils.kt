package sunhang.mathkeyboard.tools

import android.content.ComponentName
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Debug
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import sunhang.mathkeyboard.GlobalVariable
import sunhang.mathkeyboard.MathIMS
import java.lang.reflect.InvocationTargetException

fun sp2Px(sp: Float): Float {
    return GlobalVariable.context.getResources().getDisplayMetrics().density * sp
}

fun dp2Px(dp: Float): Float {
    return GlobalVariable.context.getResources().getDisplayMetrics().density * dp
}

fun setLayoutParamSize(cls: Class<*>, view: View, width: Int, height: Int) {
    var lp: ViewGroup.LayoutParams? = view.layoutParams
    if (lp != null) {
        lp.width = width
        lp.height = height
    } else {
        try {
            lp = cls.getConstructor(Int::class.javaPrimitiveType, Int::class.javaPrimitiveType).newInstance(
                width,
                height
            ) as ViewGroup.LayoutParams
            view.layoutParams = lp
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }

    }
}

fun <T> newInstance(cls: Class<T>): T? {
    try {
        return cls.newInstance()
    } catch (e: InstantiationException) {
        e.printStackTrace()
        return null
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
        return null
    }
}

fun isNetAvailable(context: Context): Boolean {
    val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    } else {
        val networkInfo = connManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isAvailable
    }
}


fun isImeEnabled(context: Context): Boolean {
    val myInputMethod = ComponentName(context, MathIMS::class.java)
    val inputMethodMgr = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return null != inputMethodMgr.enabledInputMethodList.find {
        it.component.toShortString() == myInputMethod.toShortString()
    }
}

fun isImeDefault(context: Context): Boolean {
    return try {
        ComponentName.unflattenFromString(
            Settings.Secure.getString(context.contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        )?.toShortString() == ComponentName(context, MathIMS::class.java).toShortString()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun isPortrait(context: Context): Boolean {
    return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
}

fun debugTime(): Float {
    return Debug.threadCpuTimeNanos() / 1000_000.0f
}

fun elapsedTime(title: String, begin: Float) {
    i("$title elapsed time : ${debugTime() - begin}")
}

