package norswap.violin.stream
import org.testng.annotations.Test
import org.testng.Assert.*
import java.util.NoSuchElementException

@Test fun empty() {
    assertNull(Stream.empty.next())
    assertNull(Stream.empty.next()) // empty can be reused
}

@Test(dependsOnGroups = arrayOf("Compat.Array_stream"))
fun invokeArray() {
    testStream { Stream(*it) }
}

@Test(dependsOnMethods = arrayOf("invokeArray"))
fun iterator() {
    val iter = Stream(1, 2, 3).iterator()
    for (i in 1..3) {
        assertTrue(iter.hasNext())
        assertEquals(iter.next(), i)
    }
    assertFalse(iter.hasNext())
    assertThrows(NoSuchElementException::class.java) { iter.next() }
}

@Test(dependsOnMethods = arrayOf("iterator"))
fun forLoop() {
    var i = 0
    for (j in arrayOf(1, 2, 3).stream()) assertEquals(++i, j)
}

@Test(dependsOnMethods = arrayOf("invokeArray"))
fun toJavaStream() {
    val stream = Stream(1, 2, 3).toJavaStream()
    assertFalse(stream.isParallel)
    var i = 0
    stream.forEach { assertEquals(it, ++i) }
}