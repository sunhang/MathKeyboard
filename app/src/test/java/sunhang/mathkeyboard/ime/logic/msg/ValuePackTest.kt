package sunhang.mathkeyboard.ime.logic.msg

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValuePackTest {
    @Test
    fun testDoubleValue() {
        val doubleValue = DoubleValue<Int, Boolean>(1, true)
        val (first, second) = doubleValue

        assertEquals(1, first)
        assertEquals(true, second)
    }
}