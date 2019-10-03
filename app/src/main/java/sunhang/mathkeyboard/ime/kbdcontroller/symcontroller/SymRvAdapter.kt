package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sunhang.mathkeyboard.ime.kbdcontroller.UniPanelSkinAttrUser
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser
import sunhang.mathkeyboard.kbdskin.UniversalPanelAttr
import sunhang.mathkeyboard.tools.buildStateListDrawable
import sunhang.mathkeyboard.tools.dp2Px
import sunhang.mathkeyboard.tools.i
import sunhang.mathkeyboard.tools.setLayoutParamSize
import sunhang.openlibrary.methodStack
import kotlin.math.roundToInt

open class SymRvAdapter(
    private val logicMsgPasser: MsgPasser,
    private val list: List<String>
) : RecyclerView.Adapter<SymRvAdapter.SymViewHolder>(),
    UniPanelSkinAttrUser {

    override var universalPanelAttr: UniversalPanelAttr? = null
        set(value) {
            field = value
            i(methodStack())
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymViewHolder {
        val itemView = TextView(parent.context).apply {
            gravity = Gravity.CENTER
            setLayoutParamSize(RecyclerView.LayoutParams::class.java, this,
                RecyclerView.LayoutParams.MATCH_PARENT,
                dp2Px(48f).roundToInt())
        }

        itemView.setOnClickListener {
            logicMsgPasser.passMessage(Msg.Logic.TEXT, it.tag as String)
        }

        return SymViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SymViewHolder, position: Int) {
        val sym = list[position]
        with(holder) {
            with(textView) {
                text = sym
                textSize = 18.0f
            }
            itemView.tag = sym

            universalPanelAttr = this@SymRvAdapter.universalPanelAttr
        }
    }

    class SymViewHolder(item: View) : RecyclerView.ViewHolder(item),
        UniPanelSkinAttrUser {

        override var universalPanelAttr: UniversalPanelAttr? = null
            set(value) {
                if (field != value) {
                    value?.let {
                        textView.background = buildStateListDrawable(
                            it.itemBackground,
                            it.itemPressedBackground
                        )
                    }
                }

                field = value
            }

        val textView = item as TextView
    }
}