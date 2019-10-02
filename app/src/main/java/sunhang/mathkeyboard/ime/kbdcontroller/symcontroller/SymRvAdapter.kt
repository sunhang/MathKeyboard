package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser

open class SymRvAdapter(private val logicMsgPasser: MsgPasser, private val list: List<String>) :
    RecyclerView.Adapter<SymRvAdapter.SymViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymViewHolder {
        return SymViewHolder(TextView(parent.context)).apply {
            itemView.setOnClickListener {
                logicMsgPasser.passMessage(Msg.Logic.TEXT, it.tag as String)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SymViewHolder, position: Int) {
        val sym = list[position]
        with(holder) {
            textView.text = sym
            itemView.tag = sym
        }
    }

    class SymViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val textView = item as TextView
    }
}