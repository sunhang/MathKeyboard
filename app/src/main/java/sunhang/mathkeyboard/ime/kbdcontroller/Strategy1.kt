package sunhang.mathkeyboard.ime.kbdcontroller

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.Log
import android.view.TextureView
import android.view.View
import java.util.concurrent.atomic.AtomicBoolean

class Strategy1(private val context: Context, private val view: TextureView) {
    private lateinit var render: LessonOneRenderer1
    private lateinit var thread: GLProducerThread

    init {
//        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        view.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.i("sunhang", "onSurfaceTextureSizeChanged " + surface)
                render.resize(width, height)
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                Log.i("sunhang", "onSurfaceTextureUpdated " + surface)
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.i("sunhang", "onSurfaceTextureDestroyed " + surface)
                thread.interrupt()

                return true
            }

            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.i("sunhang", "onSurfaceTextureAvailable")
                render = LessonOneRenderer1()
                render.setViewport(width, height)
                thread = GLProducerThread(
                    surface,
                    render
                )
                thread.start()
            }

        }
    }
}