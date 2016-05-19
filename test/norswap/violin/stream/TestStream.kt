package norswap.violin.stream
import org.testng.annotations.Test
import org.testng.Assert.*

@Test fun empty() {
    assertNull(Stream.empty.next())
    assertNull(Stream.empty.next()) // empty can be reused
}

@Test(dependsOnGroups = arrayOf("Compat.Array_stream"))
fun iterator() {
    val iter = arrayOf(1, 2, 3).stream().iterator()
    for (i in 1..3) {
        assertTrue(iter.hasNext())
        assertEquals(iter.next(), i)
    }
}

@Test(dependsOnMethods = arrayOf("iterator"))
fun forLoop() {
    var i = 0
    for (j in arrayOf(1, 2, 3).stream()) assertEquals(++i, j)
}