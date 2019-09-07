package sunhang.mathkeyboard.kbdviews

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.kbdskin.CandiVisualAttr
import sunhang.mathkeyboard.tools.buildStateListDrawable
import sunhang.openlibrary.runOnMain

class CandiAdapter(private val requestLoadMore: () -> Unit,
                   private val requestChooseCandi: (Int, String) -> Unit) : RecyclerView.Adapter<CandiAdapter.CandiViewHolder>() {
    private var candis = mutableListOf<String>()
    var candiVisualAttr: CandiVisualAttr? = null

    private var loading = false
    private var hasMore = false

    fun reset() {
        candis.clear()
        notifyDataSetChanged()
    }

    fun setCandis(list: List<String>, hasMore: Boolean) {
        loading = false
        this.hasMore = hasMore

        with(candis) {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    fun appendCandis(list: List<String>, hasMore: Boolean) {
        loading = false
        this.hasMore = hasMore

        candis.addAll(list)
        notifyItemRangeInserted(candis.size - list.size, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandiViewHolder {
        val viewItem = View.inflate(parent.context, R.layout.candi_item, null).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.MATCH_PARENT
            )

            setOnClickListener {
                val index = it.tag as Int
                requestChooseCandi(index, candis[index])
            }
        }

        return CandiViewHolder(viewItem)
    }

    override fun getItemCount() = candis.size

    override fun onBindViewHolder(holder: CandiViewHolder, position: Int) {
        holder.textView.text = candis[position]

        // 显示下标
        if (position % 5 == 0) {
            holder.indexTv.text = position.toString()
            holder.indexTv.visibility = View.VISIBLE
        } else {
            holder.indexTv.text = ""
            holder.indexTv.visibility = View.INVISIBLE
        }

        // 应用皮肤
        val candiVisualAttr = this.candiVisualAttr
        if (!holder.skinIsApplied && candiVisualAttr != null) {
            holder.skinIsApplied = true

            holder.textView.setTextColor(candiVisualAttr.normalTextColor)
            holder.itemView.background = buildStateListDrawable(0, candiVisualAttr.itemPressedColor)
        }

        holder.itemView.tag = position

        if (position >= itemCount - 1 && hasMore && !loading) {
            loading = true

            runOnMain {
                requestLoadMore()
            }
        }
    }

    class CandiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var skinIsApplied = false
        val textView = itemView.findViewById<TextView>(R.id.text)!!
        val indexTv = itemView.findViewById<TextView>(R.id.index)!!
    }
}