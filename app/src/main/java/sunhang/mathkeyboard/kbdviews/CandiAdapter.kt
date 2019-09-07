package sunhang.mathkeyboard.kbdviews

import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sunhang.mathkeyboard.kbdskin.CandiVisualAttr
import sunhang.mathkeyboard.tools.buildStateListDrawable
import sunhang.mathkeyboard.tools.dp2Px

class CandiAdapter : RecyclerView.Adapter<CandiAdapter.CandiViewHolder>() {
    private var candis = mutableListOf<String>()
    var candiVisualAttr: CandiVisualAttr? = null

    fun reset() {
        candis.clear()
        notifyDataSetChanged()
    }

    fun setCandis(list: List<String>) {
        with(candis) {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    fun appendCandis(list: List<String>) {
        candis.addAll(list)
        notifyItemRangeInserted(candis.size - list.size, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandiViewHolder {
        val textView = TextView(parent.context).apply {
            textSize = 20.0f
            gravity = Gravity.CENTER
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT)
            minWidth = dp2Px(50.0f).toInt()
            setPadding(dp2Px(5.0f).toInt(), paddingTop, dp2Px(5.0f).toInt(), paddingBottom)

            setOnClickListener {  }
        }

        return CandiViewHolder(textView)
    }

    override fun getItemCount() = candis.size

    override fun onBindViewHolder(holder: CandiViewHolder, position: Int) {
        holder.textView.text = candis[position]

        val candiVisualAttr = this.candiVisualAttr
        if (!holder.skinIsApplied && candiVisualAttr != null) {
            holder.textView.setTextColor(candiVisualAttr.normalTextColor)
            holder.textView.background = buildStateListDrawable(0, candiVisualAttr.itemPressedColor)
        }
    }

    class CandiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var skinIsApplied = false
        val textView = itemView as TextView
    }
}