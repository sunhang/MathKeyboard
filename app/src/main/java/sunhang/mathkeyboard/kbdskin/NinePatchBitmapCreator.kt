package sunhang.mathkeyboard.kbdskin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.NinePatchDrawable
import sunhang.mathkeyboard.base.common.CloneableNinePatchDrawable
import java.util.*

private val COLOR_9 = intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1)
private val COLOR_16 = intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
private val DEFAULT_PADDING = Rect(0, 0, 0, 0)

fun createNinePatchBitmap(context: Context, bitmap: Bitmap): NinePatchDrawable {
    val chunk = getNinePatchChunk(bitmap.width, bitmap.height)
    return CloneableNinePatchDrawable(context.resources, bitmap, chunk, DEFAULT_PADDING, "")
}

private fun getNinePatchChunk(bmWidth: Int, bmHeight: Int): ByteArray {
    val xDivs = IntArray(2)
    val yDivs = IntArray(2)
    xDivs[0] = bmWidth / 2
    xDivs[1] = bmWidth / 2 + 1
    yDivs[0] = bmHeight / 2
    yDivs[1] = bmHeight / 2 + 1
    return makeNinePatchData(xDivs, yDivs, getColors(xDivs.size, yDivs.size))
}

private fun getColors(xDivCnt: Int, yDivCnt: Int): IntArray {
    val needCnt = (xDivCnt + 1) * (yDivCnt + 1)
    if (needCnt == 9)
        return COLOR_9
    if (needCnt == 16)
        return COLOR_16

    // Create a new color if required.
    val colors = IntArray(needCnt)
    Arrays.fill(colors, 1)
    return colors
}

private fun makeNinePatchData(
    xDivs: IntArray,
    yDivs: IntArray,
    colors: IntArray
): ByteArray {
    val numXDivs = xDivs.size.toByte()
    val numYDivs = yDivs.size.toByte()
    val numColors = colors.size.toByte()

    /** chunk data format:
     * sizeof(Res_png_9patch) = 32
     * XDivs - X Divs array
     * YDivs - Y Divs array
     * colors - hint colors array
     */
    val sizeOfData = 32 + (numXDivs.toInt() + numYDivs.toInt() + numColors.toInt()) * 4
    val ninePatchData = ByteArray(sizeOfData)
    // low efficiency
    //Arrays.fill(ninePatchData, (byte)0);

    ninePatchData[0] = 1   // serialized
    ninePatchData[1] = numXDivs
    ninePatchData[2] = numYDivs
    ninePatchData[3] = numColors
    // numXDivs
    for (i in 0 until numXDivs) {
        if (xDivs[i] >= 0 && xDivs[i] < 255) {
            ninePatchData[32 + i * 4] = xDivs[i].toByte()
        } else {
            ninePatchData[32 + i * 4] = (xDivs[i] and 0xFF).toByte()
            ninePatchData[32 + i * 4 + 1] = (xDivs[i] shr 0x8).toByte()
        }

    }
    // numYDivs
    for (i in 0 until numYDivs) {
        if (yDivs[i] >= 0 && yDivs[i] < 255) {
            ninePatchData[32 + (numXDivs + i) * 4] = yDivs[i].toByte()
        } else {
            ninePatchData[32 + (numXDivs + i) * 4] = (yDivs[i] and 0xFF).toByte()
            ninePatchData[32 + (numXDivs + i) * 4 + 1] = (yDivs[i] shr 0x8).toByte()
        }
    }
    // numColors
    for (i in 0 until numColors) {
        ninePatchData[32 + (numXDivs.toInt() + numYDivs.toInt() + i) * 4] = colors[i].toByte()
    }
    return ninePatchData
}
