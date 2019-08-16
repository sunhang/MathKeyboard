package sunhang.mathkeyboard.kbdviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import sunhang.mathkeyboard.tools.dp2Px
import sunhang.mathkeyboard.tools.sp2Px
import kotlin.math.roundToInt

class NumColumnView(context: Context) : View(context) {
    private val codes = arrayListOf('.', '/', '+', '-', ':', '=', '@', '_')
    private val itemHeight = dp2Px(40.0f) as Int
    private var currentPressed = -1

    private val gestureDetector: GestureDetector
    private var onItemSelectListener: ((Int) -> Unit)? = null
    private val tempRect = Rect()
    private val tempBound = Rect()
    private val paint: Paint
    private val linePaint: Paint
    private val itemPressedPaint: Paint
    private val DEFAULT_SP_TEXTSIZE = 18f

    init {
        gestureDetector = GestureDetector(getContext(), SimpleOnGestureListener())

        paint = Paint()
        paint.textSize = sp2Px(DEFAULT_SP_TEXTSIZE)
        paint.textAlign = Paint.Align.LEFT
        paint.isAntiAlias = true
//            paint.color = ContextCompat.getColor(context, R.color.normal_key_text_color)

        linePaint = Paint()
        linePaint.color = -0x181819

        itemPressedPaint = Paint()
        itemPressedPaint.color = Color.LTGRAY
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var measuredHeight = itemHeight * codes.size
        if (measuredHeight < minimumHeight)
            measuredHeight = minimumHeight

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        tempRect.left = 0
        tempRect.right = width

        for (i in codes.indices) {
            tempRect.top = i * itemHeight
            tempRect.bottom = (i + 1) * itemHeight

//                val itemBackground = if (i == currentPressed) attr.keyBackgroundPressed else attr.keyBackground
//                paint.setColor(if (i == currentPressed) attr.keyLabelColorPressed else attr.keyLabelColor)

            // draw background
//                itemBackground.setBounds(tempRect)
//                itemBackground.draw(canvas)

            drawItem(canvas, codes[i].toString(), tempRect)

            if (i != codes.size - 1) {
                // draw line
                drawLine(canvas, tempRect)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        try {
            val field = GestureDetector::class.java.getDeclaredField("mAlwaysInTapRegion")
            field.isAccessible = true
            val b = field.get(gestureDetector) as Boolean
            if (!b) {
                currentPressed = -1
                invalidate()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

    private fun drawItem(canvas: Canvas, text: String, rect: Rect) {
        paint.getTextBounds(text, 0, text.length, tempBound)
        val x = (rect.centerX() - tempBound.centerX()).toFloat()
        val y = (rect.centerY() - (tempBound.top + tempBound.bottom) / 2).toFloat()
        canvas.drawText(text, x, y, paint)
    }

    private fun drawLine(canvas: Canvas, itemRect: Rect) {
        val startX = (itemRect.left + dp2Px(6.0f)) as Int
        val startY = tempRect.bottom
        val endX = (tempRect.right - dp2Px(6.0f)) as Int
        val endY = tempRect.bottom

        canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), linePaint)
    }

    private inner class SimpleOnGestureListener : GestureDetector.SimpleOnGestureListener() {

        private fun findRect(x: Int, y: Int): Int {
            var index = y / itemHeight

            if (index >= codes.size && index > 0)
                index = codes.size - 1

            return index
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val x = e.x.roundToInt()
            val y = e.y.roundToInt()

            val index = findRect(x, y)

            if (index != -1) {
                onItemSelectListener?.invoke(codes[index].toInt())
            }

            currentPressed = -1

            invalidate()

            return true
        }


        override fun onLongPress(e: MotionEvent) {
            currentPressed = -1
            invalidate()
        }

        override fun onShowPress(e: MotionEvent) {
            val x = e.x.roundToInt()
            val y = e.y.roundToInt()

            val index = findRect(x, y)

            if (index != -1) {
                currentPressed = index
            } else {
                currentPressed = -1
            }

            invalidate()
        }
    }
}
