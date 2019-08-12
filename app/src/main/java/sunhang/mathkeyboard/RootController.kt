package sunhang.mathkeyboard

class RootController : BaseController(){
    private val keyboardController = KeyboardController()
    init {
        imsActions.add(keyboardController)
    }
}