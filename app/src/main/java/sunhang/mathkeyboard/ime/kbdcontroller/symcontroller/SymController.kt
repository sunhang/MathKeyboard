package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
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
import sunhang.mathkeyboard.tools.i
import sunhang.openlibrary.screenWidth
import sunhang.openlibrary.uiLazy


class SymController(private val imsContext: IMSContext, rootView: RootView) : BaseController(),
    IMSAction, SkinAttrUser {
    private val containter = rootView.findViewById<ViewGroup>(R.id.ime_layer)
    private val context = imsContext.context
    private var skinModel: SkinModel? = null
    private var shown = false
    private var animating = false
    private var positionX = 0
    private var positionY = 0

    private val layout by lazyBuildLayout {
        builderContext = context

        skinInitializer = {
            skinModel?.let {
                applySkin(it)
            }
        }

        viewInitializer = {
            with(view_pager) {
                addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
                adapter = SymPagerAdapter(imsContext.logicMsgPasser, symTypes)
            }

            with(tab_layout) {
                symTypes.map { it.textStr(context) }.forEach { title ->
                    addTab(newTab().apply {
                        text = title
                    })
                }
            }

            tab_layout.addOnTabSelectedListener(object :
                TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position ?: 0
                    view_pager.currentItem = position
                }

            })

            fb_back.setOnClickListener {
                i("fb_back clicked")
                hide()
            }
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
                tab_layout.background = skinModel.keyboardVisualAttributes.topBackground
                with(view_pager.adapter as SymPagerAdapter) {
                    universalPanelAttr = skinModel.universalPanelAttr
                }
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

    fun show(centerX: Int, centerY: Int) {
        if (animating || shown) {
            return
        }

        positionX = centerX
        positionY = centerY

        val view = layout.view {
            tab_layout.layoutParams.height = imsContext.imeLayoutConfig.toolbarHeight
        }

        containter.addView(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewAnimationUtils.createCircularReveal(
                view,
                centerX,
                centerY,
                0f,
                context.screenWidth.toFloat()
            ).run {
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        animating = false
                    }

                })

                duration = 309
                start()
                animating = true
            }
        }

        shown = true
    }

    fun hide() {
        if (animating || !shown) {
            return
        }


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            containter.removeView(layout.view())
        } else {
            ViewAnimationUtils.createCircularReveal(
                layout.view(),
                positionX,
                positionY,
                context.screenWidth.toFloat(),
                0f
            ).run {
                duration = 309
                start()
                animating = true

                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        containter.removeView(layout.view())
                        animating = false
                    }
                })
            }
        }

        shown = false
    }
}