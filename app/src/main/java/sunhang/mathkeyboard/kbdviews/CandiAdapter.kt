package sunhang.mathkeyboard.kbdviews

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CandiAdapter : RecyclerView.Adapter<CandiAdapter.CandiViewHolder>() {
    private var candis = mutableListOf<String>()

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
        val textView = TextView(parent.context)
        return CandiViewHolder(textView)
    }

    override fun getItemCount() = candis.size

    override fun onBindViewHolder(holder: CandiViewHolder, position: Int) {
        holder.textView.text = candis[position]
    }

    class CandiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView as TextView
    }
}