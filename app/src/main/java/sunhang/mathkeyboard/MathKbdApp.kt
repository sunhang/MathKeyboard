package sunhang.mathkeyboard

import android.app.Application

class MathKbdApp : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalVariable.context = this
    }
}