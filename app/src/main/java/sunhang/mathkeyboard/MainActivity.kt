package sunhang.mathkeyboard

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val view = GLSurfaceView(this)
//        with(view) {
//            setEGLContextClientVersion(2);
//            setRenderer(MyRender1(this@MainActivity))
//        }
//        setContentView(view)


        /*
        Handler().postDelayed({
            startActivity(Intent(this@MainActivity, PreviewActivity::class.java))
        }, 200)*/
    }
}
