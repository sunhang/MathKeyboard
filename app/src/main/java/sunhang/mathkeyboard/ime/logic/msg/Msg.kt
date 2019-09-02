package sunhang.mathkeyboard.ime.logic.msg

data class Msg(val type: Int, val valuePack: ValuePack) {
    class Logic {
        companion object {
            const val INIT = 0
            const val DISPOSE = 1
            const val CODE = 2
            const val PINYIN_DEOCODER = 3
            const val PLANE_TYPE = 4
        }
    }

    class Editor {
        companion object {
            const val COMMIT_CODE = 0
        }
    }
}
