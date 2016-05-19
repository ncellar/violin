package norswap.violin.stream
import org.testng.Assert.*

fun testStream(builder: (Array<Int>) -> Stream<Int>) {
    val stream = builder(arrayOf(1, 2, 3))
    for (i in 1..3) assertEquals(stream.next(), i)
    assertNull(stream.next())
    assertNull(builder(arrayOf<Int>()).next())
}