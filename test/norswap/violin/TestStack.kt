package norswap.violin
import norswap.violin.link.LinkList
import norswap.violin.stream.testStream
import org.testng.annotations.Test
import org.testng.Assert.*

@Test fun poppingStream() {
    testStream { LinkList(*it).poppingStream() }
    val stack = LinkList(1, 2, 3)
    val stream = stack.poppingStream()
    assertEquals(stream.next(), 1)
    assertEquals(stack.peek(), 2)
    assertEquals(stream.peek(), 2)
    assertEquals(stack.peek(), 2)
    assertEquals(stream.next(), 2)
    assertEquals(stack.pop(), 3)
    assertNull(stream.next(), null)
}
