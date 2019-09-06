package sunhang.mathkeyboard.ime.logic.msg

data class Msg(val type: Type, val valuePack: ValuePack) {
    interface Type

    enum class Logic : Type {
//      const val INIT = 0
//      const val DISPOSE = 1
        CODE,
        PINYIN_DEOCODER,
        PLANE_TYPE
    }

    enum class Editor : Type {
        COMMIT_CODE
    }

    enum class KbdUI: Type {
        CANDI_RESET,
        CANDI_SHOW,
        CANDI_APPEND
    }
}
