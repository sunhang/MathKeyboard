package sunhang.mathkeyboard.ime.kbdcontroller

import android.opengl.GLSurfaceView

class Strategy0(private val glSurfaceView: GLSurfaceView) {
    init {
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(LessonOneRenderer())
    }
}