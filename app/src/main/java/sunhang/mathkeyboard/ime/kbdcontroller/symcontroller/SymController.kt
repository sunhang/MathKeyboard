package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
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
            val fbBack = fb_back
            val tabLayout = tab_layout
            val viewPager = view_pager

            with(viewPager) {
                addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
                adapter = SymPagerAdapter(imsContext, symTypes).apply {
                    universalPanelAttr = skinModel?.universalPanelAttr

                    hideFab = {
                        if (fbBack.isShown) {
                            fbBack.hide()
                        }
                    }

                    showFab = {
                        if (!fbBack.isShown) {
                            fbBack.show()
                        }
                    }
                }
            }

            with(tabLayout) {
                symTypes.map { it.textStr(context) }.forEach { title ->
                    addTab(newTab().apply {
                        text = title
                    })
                }

                addOnTabSelectedListener(object :
                    TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
                    override fun onTabReselected(tab: TabLayout.Tab?) {}

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}

                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        val position = tab?.position ?: 0
                        viewPager.currentItem = position
                    }

                })
            }

            fbBack.setOnClickListener {
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