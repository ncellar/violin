package norswap.violin.link
import norswap.violin.stream.each
import org.testng.annotations.Test
import org.testng.Assert.*

@Test fun linkEmpty() {
    val link = Link<Int>()
    assertNull(link)
    assertNull(link.stream().next())
    assertNull(link.linkStream().next())
    assertFalse(link.iterator().hasNext())
}

@Test fun link() {
    val link = Link(1, 2, 3)
    var j = 0
    for (i in link)
        assertEquals(i, ++j)
    j = 0
    link.stream().each { assertEquals(it, ++j) }

}

@Test fun linkStream() {
    val link = Link(1, 2, 3)
    var i = 0
    link.linkStream().each { assertEquals(it.item, ++i) }
}
