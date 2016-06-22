package norswap.violin.stream
import org.testng.annotations.Test
import org.testng.Assert.*
import java.util.NoSuchElementException

@Test fun empty() {
    assertNull(Stream.empty.next())
    assertNull(Stream.empty.next()) // empty can be reused
}

@Test fun invokeArray() {
    testStream { Stream(*it) }
}

@Test fun iterator() {
    val iter = Stream(1, 2, 3).iterator()
    for (i in 1..3) {
        assertTrue(iter.hasNext())
        assertEquals(iter.next(), i)
    }
    assertFalse(iter.hasNext())
    assertThrows(NoSuchElementException::class.java) { iter.next() }
}

@Test fun forLoop() {
    var i = 0
    for (j in arrayOf(1, 2, 3).stream()) assertEquals(++i, j)
}

@Test fun toJavaStream() {
    val stream = Stream(1, 2, 3).toJavaStream()
    assertFalse(stream.isParallel)
    var i = 0
    stream.forEach { assertEquals(it, ++i) }
}

@Test fun transitive() {
    val list = 1.transitive { it + 1 }.limit(4).list()
    assertEquals(list, listOf(1, 2, 3, 4))
}