package sunhang.mathkeyboard

import android.view.View
import android.view.inputmethod.EditorInfo
import io.reactivex.disposables.CompositeDisposable
import sunhang.mathkeyboard.kbdskin.KeyboardVisualAttributes
import sunhang.mathkeyboard.kbdskin.SkinAttrUser
import sunhang.mathkeyboard.kbdviews.RootView

open class BaseController : IMSAction, SkinAttrUser {
    protected val imsActions = mutableListOf<IMSAction>()
    protected val skinAttrUsers = mutableListOf<SkinAttrUser>()
    protected val compositeDisposable = CompositeDisposable()

    protected fun attach(baseController: BaseController) {
        imsActions.add(baseController)
        skinAttrUsers.add(baseController)
    }

    override fun onCreate(imsContext: IMSContext, rootView: RootView) {
        imsActions.forEach { it.onCreate(imsContext, rootView) }
    }

    override fun onCreateInputViewInvoked() {
        imsActions.forEach { it.onCreateInputViewInvoked() }
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        imsActions.forEach { it.onStartInputView(info, restarting) }
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        imsActions.forEach { it.onFinishInputView(finishingInput) }
    }

    override fun onDestroy() {
        imsActions.forEach { it.onDestroy() }
        compositeDisposable.clear()
    }

    override fun useSkinAttr(skinAttri: KeyboardVisualAttributes) {
        skinAttrUsers.forEach { it.useSkinAttr(skinAttri) }
    }

}