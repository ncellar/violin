package norswap.violin.stream
import org.testng.annotations.Test
import org.testng.Assert.*

@Test(groups = arrayOf("Compat.Array_stream"))
fun Array_stream() {
    val stream = arrayOf(1, 2, 3).stream()
    for (i in 1..3) assertEquals(stream.next(), i)
    assertEquals(stream.next(), null)
}
