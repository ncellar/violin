package norswap.violin.stream
import org.testng.annotations.Test
import org.testng.Assert.*
import java.util.Comparator

@Test fun each() {
    var i = 0
    Stream(1, 2, 3).each { assertEquals(it, ++i) }
}

@Test fun mutableList() {
    val list = Stream(1, 2, 3).mutableList()
    list.add(4)
    assertEquals(list, listOf(1, 2, 3, 4))
}

@Test fun list() {
    assertEquals(Stream(1, 2, 3).list(), listOf(1, 2, 3))
}

@Test fun array() {
    assertEquals(Stream(1, 2, 3).array(), arrayOf(1, 2, 3))
}

@Test fun foldl() {
    assertEquals(Stream(1, 2, 3).foldl(0) { a, b -> a * 10 + b }, 123)
}

@Test fun foldr() {
    assertEquals(Stream(1, 2, 3).foldr(0) { a, b -> a * 10 + b }, 321)
}

@Test fun reduce() {
    assertEquals(Stream(1, 2, 3).reduce { a, b -> a * 10 + b }, 123)
    assertNull(Stream<Int>().reduce { a, b -> a + b })
}

@Test fun reduceRight() {
    assertEquals(Stream(1, 2, 3).reduceRight { a, b -> a * 10 + b }, 321)
    assertNull(Stream<Int>().reduceRight { a, b -> a + b })
}

@Test fun last() {
    assertEquals(Stream(1, 2, 3).last(), 3)
    assertNull(Stream<Int>().last())
}

@Test fun first() {
    assertEquals(Stream(1, 2, 3, 4).first { it % 2 == 0 }, 2)
    assertNull(Stream(1, 2, 3, 4).first { it % 5 == 0 })
    assertNull(Stream<Int>().first { true })
}
@Test fun lastPredicate() {
    assertEquals(Stream(1, 2, 3, 4).last { it % 2 == 0 }, 4)
    assertNull(Stream(1, 2, 3, 4).last { it % 5 == 0 })
    assertNull(Stream<Int>().last { true })
}

@Test fun any() {
    assertTrue(Stream(1, 2, 3).any { it % 2 == 0 })
    assertFalse(Stream(1, 3, 5).any { it % 2 == 0 })
    assertFalse(Stream<Int>().any { true })
}

@Test fun all() {
    assertTrue(Stream(1, 2, 3).all { it < 4 })
    assertFalse(Stream(1, 2, 3, 4).all { it < 4 })
    assertTrue(Stream<Int>().all { true })
}

@Test fun count() {
    assertEquals(Stream(1, 2, 3).count(), 3)
    assertEquals(Stream<Int>().count(), 0)
}

@Test fun max() {
    assertEquals(Stream(1, 3, 2).max(), 3)
    assertEquals(Stream(1, 2, 3).max(), 3)
    assertEquals(Stream(3, 2, 1).max(), 3)
    assertNull(Stream<Int>().max())
}

@Test fun maxBy() {
    assertEquals(Stream(1, 2, 3).maxBy { it }, 3)
}

val intComparator = object: Comparator<Int> {
    override fun compare(a: Int, b: Int) = a - b
}

@Test fun maxWith() {
    assertEquals(Stream(1, 2, 3).maxWith(intComparator), 3)
}

@Test fun min() {
    assertEquals(Stream(3, 1, 2).min(), 1)
    assertEquals(Stream(1, 2, 3).min(), 1)
    assertEquals(Stream(3, 2, 1).min(), 1)
    assertNull(Stream<Int>().min())
}

@Test fun minBy() {
    assertEquals(Stream(1, 2, 3).minBy { it }, 1)
}

@Test fun minWith() {
    assertEquals(Stream(1, 2, 3).minWith(intComparator), 1)
}

@Test fun associate() {
    assertEquals(Stream(1, 2, 3).associate { it to it }, mapOf(1 to 1, 2 to 2, 3 to 3))
}

@Test fun linkList() {
    var i = 3
    Stream(1, 2, 3).linkList().stream().each { assertEquals(it, i--) }
}

@Test fun set() {
    val set = Stream(1, 2, 3).set()
    assertEquals(set, setOf(1, 2, 3))
}

@Test fun joinToString() {
    val str = Stream(1, 2, 3)
        .joinToString(prefix="[", postfix= "]", limit=2) { (it * 2).toString() }
    assertEquals(str, "[2, 4, ...]")
}

@Test fun groupBy() {
    val map = Stream(2, 3, 4, 5, 6, 7).groupBy { it / 2 }
    val expect = mapOf(1 to listOf(2, 3), 2 to listOf(4, 5), 3 to listOf(6, 7))
    assertEquals(map, expect)
}

@Test fun partition() {
    val pair = Stream(1, 2, 3, 4).partition { it % 2 == 0 }
    val expect = Pair(listOf(2, 4), listOf(1, 3))
    assertEquals(pair, expect)
}