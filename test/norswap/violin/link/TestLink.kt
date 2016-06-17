package norswap.violin.link
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
    for (i in link.stream())
        assertEquals(i, ++j)
}

@Test fun linkStream() {
    val link = Link(1, 2, 3)
    var j = 0
    for (l in link.linkStream())
        assertEquals(l.item, ++j)
}
