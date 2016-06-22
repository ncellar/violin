package norswap.violin.stream
import org.testng.annotations.Test
import org.testng.Assert.*

@Test fun map() {
    assertEquals(Stream(1, 2, 3).map { it + 1 }.list(), listOf(2, 3, 4))
}

@Test fun apply() {
    val list = mutableListOf<Int>()
    Stream(1, 2, 3).apply { list.add(this) }.each {}
    assertEquals(list, listOf(1, 2, 3))
}

@Test fun after() {
    val list = mutableListOf<Int>()
    Stream(1, 2, 3).after { list.add(it) }.each {}
    assertEquals(list, listOf(1, 2, 3))
}

@Test fun filter() {
    assertEquals(Stream(1, 2, 3, 4).filter { it % 2 == 0 }.list(), listOf(2, 4))
}

@Test fun indexed() {
    var i = 0
    Stream(1, 2, 3).indexed().each {
        assertEquals(it.index, i)
        assertEquals(it.value, ++i)
    }
}

@Test fun fmap() {
    assertEquals(Stream(1, 2, 3).fmap { Stream(it, it) }.list(), listOf(1, 1, 2, 2, 3, 3))
}

@Test fun upTo() {
    assertEquals(Stream(1, 2, 3, 4).upTo { it % 3 == 0 }.list(), listOf(1, 2))
}

@Test fun upThrough() {
    assertEquals(Stream(1, 2, 3, 4).upThrough { it % 3 == 0 }.list(), listOf(1, 2, 3))
}

@Test fun dropWhile() {
    assertEquals(Stream(1, 2, 3, 4).dropWhile { it < 3 }.list(), listOf(3, 4))
}

@Test fun drop() {
    assertEquals(Stream(1, 2, 3, 4).drop(2).list(), listOf(3, 4))
}

@Test fun takeWhile() {
    assertEquals(Stream(1, 2, 3, 4).takeWhile { it < 3 }.list(), listOf(1, 2))
}

@Test fun limit() {
    assertEquals(Stream(1, 2, 3, 4).limit(2).list(), listOf(1, 2))
}

@Test fun zip() {
    assertEquals(
        Stream(1, 2, 3).zip(Stream(2, 4, 6)).list(),
        listOf(Pair(1, 2), Pair(2, 4), Pair(3, 6)))
    assertEquals(Stream(1, 2, 3).zip(Stream(2)).list(), listOf(Pair(1, 2)))
}

@Test fun ziplong() {
    assertEquals(
        Stream(1, 2, 3).ziplong(Stream(2, 4)).list(),
        listOf(Pair(1, 2), Pair(2, 4), Pair(3, null)))
    assertEquals(
        Stream(1, 2).ziplong(Stream(2, 4, 6)).list(),
        listOf(Pair(1, 2), Pair(2, 4), Pair(null, 6)))

}

@Test fun concat() {
    var stream = Stream(1, 2, 3) then Stream(4, 5, 6)
    var i = 0
    stream.each { assertEquals(it, ++i) }
    assertEquals(i, 6)
    stream = Stream<Int>() then Stream(1, 2, 3)
    i = 0
    stream.each { assertEquals(it, ++i) }
    assertEquals(i, 3)
    stream = Stream(1, 2, 3) then Stream<Int>()
    i = 0
    stream.each { assertEquals(it, ++i) }
    assertEquals(i, 3)
    stream = Stream<Int>() then Stream<Int>()
    assertNull(stream.next())
}

@Test fun distinct() {
    val list = Stream(1, 1, 2, 3, 1, 2, 3, 3, 2).distinct().list()
    assertEquals(list, listOf(1, 2, 3))
}

@Test fun distinctBy() {
    val list = Stream(2, 3, 2, 5, 4, 6, 4, 2).distinctBy { it / 2 }.list()
    assertEquals(list, listOf(2, 5, 6))
}

@Test fun filterMap() {
    val list = Stream(1, 2, 3, 4).filterMap { if (it % 2 == 0) it / 2 else null }.list()
    assertEquals(list, listOf(1, 2))
}