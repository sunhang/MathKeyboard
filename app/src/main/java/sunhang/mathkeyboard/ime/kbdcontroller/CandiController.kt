package sunhang.mathkeyboard.ime.kbdcontroller

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.ime.logic.msg.Msg
import sunhang.mathkeyboard.kbdskin.SkinModel
import sunhang.mathkeyboard.kbdviews.CandiAdapter
import sunhang.mathkeyboard.kbdviews.RootView

class CandiController(private val imsContext: IMSContext, private val rootView: RootView) : BaseController() {
    private val candiRoot = rootView.findViewById<ViewGroup>(R.id.candidate)
    private val candiRv = candiRoot.findViewById<RecyclerView>(R.id.candi_rv)
    private val candiAdapter = CandiAdapter(
        requestLoadMore = {
            imsContext.logicMsgPasser.passMessage(Msg.Logic.LOAD_MORE_CHOICES)
        }, requestChooseCandi = { position, candi ->
            imsContext.logicMsgPasser.passMessage(Msg.Logic.CHOOSE_CANDI, position, candi)
        }
    )

    init {
        candiRv.layoutManager = LinearLayoutManager(imsContext.context, LinearLayoutManager.HORIZONTAL, false)
        candiRv.adapter = candiAdapter
    }

    fun setCandis(list: List<String>, hasMore: Boolean) {
        candiAdapter.setCandis(list, hasMore)
    }

    fun appendCandis(list: List<String>, hasMore: Boolean) {
        candiAdapter.appendCandis(list, hasMore)
    }

    override fun useSkinAttr(skinModel: SkinModel) {
        super.useSkinAttr(skinModel)

        val candiAttr = skinModel.candiVisualAttr
        candiRv.setBackgroundColor(candiAttr.backgroundColor)
        candiAdapter.candiVisualAttr = candiAttr
        candiRoot.findViewById<View>(R.id.divider).background = ColorDrawable(candiAttr.normalTextColor and 0x80FFFFFF.toInt())
    }
}