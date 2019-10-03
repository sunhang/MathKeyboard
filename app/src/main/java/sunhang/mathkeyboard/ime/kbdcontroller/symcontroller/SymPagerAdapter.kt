package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import sunhang.mathkeyboard.base.common.RvItemDecoration
import sunhang.mathkeyboard.ime.kbdcontroller.UniPanelSkinAttrUser
import sunhang.mathkeyboard.ime.logic.msg.MsgPasser
import sunhang.mathkeyboard.kbdskin.UniversalPanelAttr
import java.lang.ref.WeakReference

class SymPagerAdapter(
    private val logicMsgPasser: MsgPasser,
    private val symTypes: Array<SymType>
) : PagerAdapter(), UniPanelSkinAttrUser {
    private val weakViewpagers = HashSet<WeakReference<RecyclerView>>()

    override var universalPanelAttr: UniversalPanelAttr? = null
        set(value) {
            field = value
//            applySkin(value)

            notifyDataSetChanged()
        }

    /*
    private fun applySkin(args: UniversalPanelAttr?) {
        weakViewpagers.forEach {
            it.get()?.run {
                (adapter as SymRvAdapter).run {
                    universalPanelAttr = args
                }

                (getItemDecorationAt(0) as RvItemDecoration).run {
                    dividerColor = args?.itemDividerColor ?: 0
                }
            }
        }

        // 去掉已回收对象的weakReference
        weakViewpagers.iterator().let {
            while (it.hasNext()) {
                if (it.next().get() == null) {
                    it.remove()
                }
            }
        }
    }*/

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
            }.also {
                it.universalPanelAttr = universalPanelAttr
            }

            addItemDecoration(RvItemDecoration().apply {
                dividerColor = universalPanelAttr?.itemDividerColor ?: 0
            })
        }.also {
            weakViewpagers.add(WeakReference<RecyclerView>(it))
            container.addView(it)
        }
    }
}
