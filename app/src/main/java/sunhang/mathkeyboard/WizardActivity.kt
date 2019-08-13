package sunhang.mathkeyboard

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_wizard.*
import sunhang.mathkeyboard.tools.isImeDefault
import sunhang.mathkeyboard.tools.isImeEnabled
import sunhang.openlibrary.runOnMain
import java.lang.ref.WeakReference
import java.util.*

class WizardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wizard)
        checkAndLaunchMainActivity()
        step_one.setOnClickListener {
            launchIMESettings()

            // 开启一个timer，轮询检测ime是否enabled了，如果是则把WizardActivity调起到前台
            val checkIme = Timer()
            checkIme.schedule(CheckImeTimerTask(this@WizardActivity, checkIme), 0, 200)
        }
        step_two.setOnClickListener {
            showIMEPicker()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (!hasFocus) return

        refreshStepView()
        checkAndLaunchMainActivity()
    }

    private fun refreshStepView() {
        val imeEnabled = isImeEnabled(this)
        val imeDefault = isImeDefault(this)
        step_one.isEnabled = !imeEnabled
        step_two.isEnabled = imeEnabled && !imeDefault
    }

    private fun showIMEPicker() {
        val inputMethodMgr = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodMgr.showInputMethodPicker()
    }

    private fun checkAndLaunchMainActivity() {
        if (isImeDefault(this) && isImeEnabled(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun launchIMESettings() {
        try {
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                        or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                        or Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            try {
                val intent = Intent(Intent.ACTION_MAIN).setComponent(ComponentName("com.android.settings", "com.android.settings.LanguageSettings"))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, R.string.input_setting_error, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private class CheckImeTimerTask(activity: Activity, private val timer: Timer) : TimerTask() {
        private val target = WeakReference<Activity>(activity)

        override fun run() {
            val activity = target.get()
            if (activity == null || activity.isDestroyed) {
                timer.cancel()
                return
            }

            if (isImeEnabled(activity)) {
                runOnMain {
                    activity.startActivity(Intent(activity, CheckIMESetting::class.java))
                }
                timer.cancel()
            }
        }
    }

    class CheckIMESetting : Activity(){

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val intent = Intent(this, WizardActivity::class.java)
            intent.putExtra("ime_enabled", true)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }
}