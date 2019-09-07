package sunhang.mathkeyboard.ime.kbdcontroller

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdskin.SkinModel
import sunhang.mathkeyboard.kbdviews.CandiAdapter
import sunhang.mathkeyboard.kbdviews.RootView

class CandiController(private val imsContext: IMSContext, private val rootView: RootView) : BaseController(){
    private val candiRoot = rootView.findViewById<ViewGroup>(R.id.candidate)
    private val candiRv = candiRoot as RecyclerView
    private val candiAdapter = CandiAdapter()

    init {
        candiRv.layoutManager = LinearLayoutManager(imsContext.context, LinearLayoutManager.HORIZONTAL, false)
        candiRv.adapter = candiAdapter
    }

    fun setCandis(list: List<String>) {
        candiAdapter.setCandis(list)
    }

    fun appendCandis(list: List<String>) {
        candiAdapter.appendCandis(list)
    }

    override fun useSkinAttr(skinModel: SkinModel) {
        super.useSkinAttr(skinModel)

        val candiAttr = skinModel.candiVisualAttr
        candiRv.setBackgroundColor(candiAttr.backgroundColor)
        candiAdapter.candiVisualAttr = candiAttr
    }
}