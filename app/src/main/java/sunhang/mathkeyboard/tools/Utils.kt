package sunhang.mathkeyboard.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.ViewGroup
import sunhang.mathkeyboard.GlobalVariable
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
