package sunhang.mathkeyboard.files

import sunhang.mathkeyboard.GlobalVariable
import java.io.File

class FilePath {
    companion object {
        private fun keyboardBasePath(): File {
            val dir = File(GlobalVariable.context.filesDir.absolutePath, "keyboard")
            checkThenMakeDir(dir)
            return dir
        }

        fun keyboardZhProtoFile(width: Int, height: Int): File {
            return File(keyboardBasePath(), "zh_${width}_$height")
        }

        fun keyboardEnProtoFile(width: Int, height: Int): File {
            return File(keyboardBasePath(), "en_${width}_$height")
        }

        fun keyboardNumProtoFile(width: Int, height: Int): File {
            return File(keyboardBasePath(), "num_${width}_$height")
        }

        private fun checkThenMakeDir(dir: File) {
            if (dir.isFile) {
                dir.delete()
            }
            if (!dir.exists()) {
                dir.mkdirs()
            }
        }
    }
}

