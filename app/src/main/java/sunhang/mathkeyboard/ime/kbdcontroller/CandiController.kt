package sunhang.mathkeyboard.ime.kbdcontroller

import androidx.recyclerview.widget.RecyclerView
import sunhang.mathkeyboard.R
import sunhang.mathkeyboard.ime.IMSContext
import sunhang.mathkeyboard.kbdviews.RootView

class CandiController(private val imsContext: IMSContext, private val rootView: RootView) : BaseController(){
    private val candiRv = rootView.findViewById<RecyclerView>(R.id.candi_rv)


}