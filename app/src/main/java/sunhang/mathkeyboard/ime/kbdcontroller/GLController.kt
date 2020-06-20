package sunhang.mathkeyboard.ime.kbdcontroller

import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.TextureView
import android.view.View.LAYER_TYPE_HARDWARE
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdskin.SkinAttrUser
import sunhang.mathkeyboard.kbdviews.RootView
import java.util.concurrent.atomic.AtomicBoolean

class GLController(val imsContext: IMSContext, val rootView: RootView) : BaseController(), SkinAttrUser {
    private lateinit var strategy0: Strategy0
    private lateinit var strategy1: Strategy1

    override fun onCreate() {
        super.onCreate()
        strategy0 = Strategy0(rootView.findViewById<GLSurfaceView>(R.id.gl_sv))
//        strategy1 = Strategy1(imsContext.context, rootView.findViewById<TextureView>(R.id.tv))
    }
}
