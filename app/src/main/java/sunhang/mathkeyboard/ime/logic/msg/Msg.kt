package sunhang.mathkeyboard.ime.logic.msg

data class Msg(val type: Type, val valuePack: ValuePack) {
    interface Type

    enum class Logic : Type {
//      const val INIT = 0
//      const val DISPOSE = 1
        CODE,
        PINYIN_DEOCODER,
        PLANE_TYPE,
        LOAD_MORE_CHOICES,
        CHOOSE_CANDI,
        PREDICT,
        START_INPUT_VIEW,
        FINISH_INPUT_VIEW,
        TEXT
    }

    enum class Editor : Type {
        COMMIT_CODE,
        COMMIT_CANDI,
        COMMIT_TEXT,
        COMPOSE,
    }

    enum class KbdUI: Type {
        CANDI_RESET,
        CANDI_SHOW,
        CANDI_APPEND,
        COMPOSE // 写作串
    }
}
