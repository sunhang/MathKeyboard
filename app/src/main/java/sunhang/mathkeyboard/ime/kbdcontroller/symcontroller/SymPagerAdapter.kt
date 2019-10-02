package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser

class SymPagerAdapter(private val logicMsgPasser: MsgPasser, private val symTypes: Array<SymType>) :
    PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun getCount(): Int {
        return symTypes.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return RecyclerView(container.context).apply {
            layoutManager = GridLayoutManager(container.context, 8)
            adapter = if (symTypes[position] == SymType.RECENT_USED) {
                SymRecentRvAdapter(logicMsgPasser)
            } else {
                SymRvAdapter(logicMsgPasser, SymData.map[symTypes[position]]!!)
            }
        }.also {
            container.addView(it)
        }
    }
}
