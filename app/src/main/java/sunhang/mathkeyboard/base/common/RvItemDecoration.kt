package sunhang.mathkeyboard.base.common

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RvItemDecoration : RecyclerView.ItemDecoration() {
    private val dividerWidth = 1
    private val dividerDrawable = ColorDrawable(Color.TRANSPARENT)

    var dividerColor: Int = 0
        set(value) {
            field = value
            dividerDrawable.color = value
        }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount
        (0 until childCount).forEach {
            val child = parent.getChildAt(it)
            val params = child.layoutParams as RecyclerView.LayoutParams

            // draw horizontal line
            val top = child.bottom + params.bottomMargin
            val bottom = top + dividerWidth
            dividerDrawable.setBounds(child.left, top, child.right, bottom)
            dividerDrawable.draw(c)

            // draw vertical line
            val left = child.right + params.rightMargin
            val right = left + dividerWidth
            dividerDrawable.setBounds(left, child.top, right, child.bottom)
            dividerDrawable.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

//        (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        outRect.right = 1
        outRect.bottom = 1
    }
}