package sunhang.mathkeyboard.ime.logic

interface ValuePack

object NoneValue: ValuePack

data class SingleValue<T>(val value: T): ValuePack

data class DoubleValue<T, K>(val first: T, val second: K): ValuePack
