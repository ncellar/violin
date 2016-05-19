package norswap.violin.stream
import org.testng.Assert
import org.testng.annotations.Test

@Test fun Iterator_tune() {
    testStream { it.iterator().tune() }
}

@Test fun Stream_tune() {
    testStream { java.util.stream.Stream.of(*it).tune() }
}

@Test fun Sequence_tune() {
    testStream { sequenceOf(*it).tune() }
}

@Test(groups = arrayOf("Compat.Array_stream"))
fun Array_stream() {
    testStream { it.stream() }
}

@Test fun Array_reverseStream() {
    val stream = arrayOf(3, 2, 1).reverseStream()
    for (i in 1..3) Assert.assertEquals(stream.next(), i)
    Assert.assertNull(stream.next())
    Assert.assertNull(arrayOf<Int>().reverseStream().next())
}

@Test fun Array_pureStream() {
    val stream = arrayOf(null, null, 1, null, null, 2, null, null, 3, null, null).pureStream()
    for (i in 1..3) Assert.assertEquals(stream.next(), i)
    Assert.assertNull(stream.next())
    Assert.assertNull(arrayOf<Int>().pureStream().next())
    Assert.assertNull(arrayOf<Int?>(null, null).pureStream().next())
}

@Test(dependsOnMethods = arrayOf("Array_reverseStream", "Array_pureStream"))
fun Array_pureReverseStream() {
    val stream = arrayOf(null, null, 3, null, null, 2, null, null, 1, null, null).pureReverseStream()
    for (i in 1..3) Assert.assertEquals(stream.next(), i)
    Assert.assertNull(stream.next())
    Assert.assertNull(arrayOf<Int>().pureReverseStream().next())
    Assert.assertNull(arrayOf<Int?>(null, null).pureReverseStream().next())
}