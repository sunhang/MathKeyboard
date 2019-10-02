package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.kbd_sym.view.*
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSAction
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.BaseController
import sunhang.mathkeyboard.kbdskin.SkinAttrUser
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.uiLazy


class SymController(private val imsContext: IMSContext, rootView: RootView) : BaseController(),
    IMSAction, SkinAttrUser {
    private val containter = rootView.findViewById<ViewGroup>(R.id.ime_layer)
    private val context = imsContext.context
    private val layout by uiLazy {
        View.inflate(context, R.layout.kbd_sym, null).apply {
            with(viewpager) {
                addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))
                adapter = Adapter()
            }

            with(tablayout) {
                context.resources.getStringArray(R.array.symbol_titles).forEach { title ->
                    addTab(newTab().apply {
                        text = title
                    })
                }
            }

            tablayout.addOnTabSelectedListener(object :
                TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position ?: 0
                    viewpager.currentItem = position
                }

            })

        }
    }


    fun show() {
        if (layout.parent == null) {
            containter.addView(
                layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                imsContext.imeLayoutConfig.toolbarHeight
            )
        }
    }

    fun hide() {
        if (layout.parent != null) {
            containter.removeView(layout)
        }
    }

    class Adapter : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return true
        }

        override fun getCount(): Int {
            return 0
        }

    }
}