package sunhang.mathkeyboard.base.common

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.NinePatchDrawable

class CloneableNinePatchDrawable(private val res: Resources, private val bitmap: Bitmap, private val chunk: ByteArray, private val padding: Rect, private val path: String)
    : NinePatchDrawable(res, bitmap, chunk, padding, path) {

    fun makeClone(): CloneableNinePatchDrawable {
        return CloneableNinePatchDrawable(res, bitmap, chunk, padding, path)
    }
}
