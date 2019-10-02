package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.kbd_sym.view.*
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSAction
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.kbdcontroller.BaseController
import sunhang.mathkeyboard.kbdskin.SkinAttrUser
import sunhang.mathkeyboard.kbdskin.SkinModel
import sunhang.mathkeyboard.kbdviews.RootView
import sunhang.openlibrary.uiLazy


class SymController(private val imsContext: IMSContext, rootView: RootView) : BaseController(),
    IMSAction, SkinAttrUser {
    private val containter = rootView.findViewById<ViewGroup>(R.id.ime_layer)
    private val context = imsContext.context
    private var skinModel: SkinModel? = null
    private var shown = false
    private val layout by lazyBuildLayout {
        builderContext = context

        skinInitializer = {
            skinModel?.let {
                applySkin(it)
            }
        }

        viewInitializer = {
            with(viewpager) {
                addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))
                adapter = SymPagerAdapter(imsContext.logicMsgPasser, symTypes)
            }

            with(tablayout) {
                symTypes.map { it.textStr(context) }.forEach { title ->
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

    /**
     * 是不是编写得过了? [lazyBuildLayout]和[Layout.LayoutBuilder]有点像DSL？晦涩?
     */
    private fun lazyBuildLayout(statement: Layout.LayoutBuilder.() -> Unit): Lazy<Layout> {
        return uiLazy<Layout> {
            with(Layout.LayoutBuilder()) {
                statement()
                build()
            }
        }
    }

    class Layout(context: Context) {
        val real = View.inflate(context, R.layout.kbd_sym, null)!!

        fun applySkin(skinModel: SkinModel) {
            with(real) {
                background = skinModel.keyboardVisualAttributes.wallpaper
                tablayout.background = skinModel.keyboardVisualAttributes.topBackground
            }
        }

        val shown = real.parent != null

        /**
         * 是不是编写得过了？
         */
        fun view(statement: View.() -> Unit = {}) = real.apply {
            statement()
        }


        class LayoutBuilder {
            lateinit var builderContext: Context
            lateinit var viewInitializer: View.() -> Unit
            lateinit var skinInitializer: Layout.() -> Unit

            fun build(): Layout {
                return Layout(builderContext).apply {
                    real.viewInitializer()
                    skinInitializer()
                }
            }
        }
    }

    override fun useSkinAttr(skinModel: SkinModel) {
        super.useSkinAttr(skinModel)
        this.skinModel = skinModel

        /**
         * 这里用[show],不用[Layout.shown], 不然layout实例会被过早创建
         */
        if (shown) {
            layout.applySkin(skinModel)
        }
    }

    fun show() {
        if (!layout.shown) {
            containter.addView(
                layout.view {
                    tablayout.layoutParams.height = imsContext.imeLayoutConfig.toolbarHeight
                },
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        shown = true
    }

    fun hide() {
        if (shown && layout.shown) {
            containter.removeView(layout.view())
        }

        shown = false
    }
}