package sunhang.mathkeyboard.ime.logic.work

import sunhang.mathkeyboard.ime.logic.Editor
import sunhang.openlibrary.runOnMain
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class LogicContext(private val inputToEditor: EditorImpl) {
    var state: State? = null

    val editor = Proxy.newProxyInstance(
        Editor::class.java.classLoader,
        arrayOf(Editor::class.java)
    ) { _, method: Method?, args: Array<out Any>? ->
        runOnMain {
            val array = args ?: arrayOf()
            method?.invoke(inputToEditor, *array)
        }
    } as Editor
}