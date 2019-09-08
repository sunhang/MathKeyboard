package sunhang.mathkeyboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        Handler().postDelayed({
            startActivity(Intent(this@MainActivity, PreviewActivity::class.java))
        }, 200)*/
    }
}
