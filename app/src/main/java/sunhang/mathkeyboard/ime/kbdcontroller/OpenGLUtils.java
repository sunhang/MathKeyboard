package sunhang.mathkeyboard.ime.kbdcontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;


import static android.opengl.GLES20.*;
import static android.opengl.GLES20.glGenerateMipmap;

public class OpenGLUtils {
    // context用户解析纹理资源时使用，resourceId为纹理资源的ID
    public static int loadTexture(Context context, int resourceId) {
        //textureObjectIds用于存储OpenGL生成纹理对象的ID，我们只需要一个纹理
        final int[] textureObjectIds = new int[1];
        //1代表生成一个纹理
        glGenTextures(1, textureObjectIds, 0);
        //判断是否生成成功
        if(textureObjectIds[0] == 0) {
            Log.w("sunhang", "generate a texture object failed!");
            return 0;
        }
        //加载纹理资源，解码成bitmap形式
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if (bitmap == null) {
            Log.w("sunhang", "Resource ID: " + resourceId + " decoded failed");
            //删除指定的纹理对象
            glDeleteTextures(1,textureObjectIds, 0);
            return 0;
        }
        //第一个参数代表这是一个2D纹理，第二个参数就是OpenGL要绑定的纹理对象ID，也就是让OpenGL后面的纹理调用都使用此纹理对象
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        //设置纹理过滤参数，GL_TEXTURE_MIN_FILTER代表纹理缩写的情况，GL_LINEAR_MIPMAP_LINEAR代表缩小时使用三线性过滤的方式，至于过滤方式以后再详解
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        //GL_TEXTURE_MAG_FILTER代表纹理放大，GL_LINEAR代表双线性过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        //加载实际纹理图像数据到OpenGL ES的纹理对象中，这个函数是Android封装好的，可以直接加载bitmap格式，
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        //bitmap已经被加载到OpenGL了，所以bitmap可释放掉了，防止内存泄露
        bitmap.recycle();
        //我们为纹理生成MIP贴图，提高渲染性能，但是可占用较多的内存
        glGenerateMipmap(GL_TEXTURE_2D);
        //现在OpenGL已经完成了纹理的加载，不需要再绑定此纹理了，后面使用此纹理时通过纹理对象的ID即可
        glBindTexture(GL_TEXTURE_2D, 0);
        //返回OpenGL生成的纹理对象ID
        return textureObjectIds[0];
    }

    public static int loadShader(Context context, int shaderType, String shaderCode) {
        int shaderId = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shaderId, shaderCode);
        GLES20.glCompileShader(shaderId);

        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {//若编译失败则显示错误日志并删除此shader
            Log.e("sunhang", "Could not compile shader " + shaderType + ":");
            Log.e("sunhang", GLES20.glGetShaderInfoLog(shaderId));
            GLES20.glDeleteShader(shaderId);
            shaderId = 0;
        } else {
            Log.i("sunhang", "compile vertexShader success!");
        }

        return shaderId;
    }
}
