package norswap.violin.stream
import org.testng.annotations.Test
import org.testng.Assert.*

@Test fun invoke() {
    testStream { PeekStream(*it) }
    val stream = PeekStream(1, 2, 3)
    for (i in 1..3) {
        assertEquals(stream.peek(), i)
        assertEquals(stream.next(), i)
    }
    assertNull(stream.next())
    assertNull(PeekStream<Int>().next())
}