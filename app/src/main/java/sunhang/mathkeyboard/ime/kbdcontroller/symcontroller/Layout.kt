package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.kbd_sym.view.*
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.kbdskin.SkinModel
import sunhang.mathkeyboard.tools.NORMAL

class Layout(context: Context) {
    val real = View.inflate(context, R.layout.kbd_sym, null)!!

    fun applySkin(skinModel: SkinModel) {
        with(real) {
            background = skinModel.keyboardVisualAttributes.wallpaper
            tab_layout.background = skinModel.keyboardVisualAttributes.topBackground
            // todo tab的文字的颜色还未设置

            with(view_pager.adapter as SymPagerAdapter) {
                universalPanelAttr = skinModel.universalPanelAttr
            }

            with(fb_back) {
                backgroundTintList = ColorStateList(
                    arrayOf<IntArray>(NORMAL),
                    intArrayOf(skinModel.universalPanelAttr.funcBackgroundColor)
                )

                val icon = ContextCompat.getDrawable(context, R.drawable.key_back)!!
                icon.setColorFilter(skinModel.universalPanelAttr.funTextColor, PorterDuff.Mode.SRC_IN)
                setImageDrawable(icon)
            }

            tab_layout.setSelectedTabIndicatorColor(skinModel.universalPanelAttr.specialColor)
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
