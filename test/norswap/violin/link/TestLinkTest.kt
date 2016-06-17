package norswap.violin.link
import norswap.violin.stream.each
import org.testng.annotations.Test
import org.testng.Assert.*

@Test fun linkListEmpty() {
    val list = LinkList<Int>()
    assertEquals(list.size, 0)
    assertNull(list.stream().next())
    assertNull(list.linkStream().next())
    assertFalse(list.iterator().hasNext())
}

@Test fun linkList() {
    val list = LinkList(1, 2, 3)
    var j = 0
    for (i in list)
        assertEquals(i, ++j)
    j = 0
    list.stream().each { assertEquals(it, ++j) }

}

@Test fun linkListSize() {
    val list = LinkList(1, 2, 3)
    assertEquals(list.size, 3)
    list.pop()
    assertEquals(list.size, 2)
}

@Test fun linkListStack() {
    val list = LinkList(1, 2, 3)
    assertEquals(list.pop(), 1)
    assertEquals(list.peek(), 2)
    list.push(0)
    assertEquals(list.peek(), 0)
    list.pop() ; list.pop() ; list.pop()
    assertTrue(list.empty)
    assertNull(list.peek())
    assertNull(list.pop())
}

@Test fun linkListEquals() {
    val first = LinkList(1, 2, 3)
    val second = LinkList(first.link, 3)
    assertEquals(first, second)
    assertEquals(LinkList<Int>(), LinkList<Int>())
}
