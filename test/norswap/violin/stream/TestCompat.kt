package norswap.violin.stream
import org.testng.Assert
import org.testng.annotations.Test

@Test fun Iterator_stream() {
    testStream { it.iterator().stream() }
}

@Test fun Stream_stream() {
    testStream { java.util.stream.Stream.of(*it).stream() }
}

@Test fun Sequence_stream() {
    testStream { sequenceOf(*it).stream() }
}

@Test fun Array_stream() {
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

@Test fun Array_pureReverseStream() {
    val stream = arrayOf(null, null, 3, null, null, 2, null, null, 1, null, null).pureReverseStream()
    for (i in 1..3) Assert.assertEquals(stream.next(), i)
    Assert.assertNull(stream.next())
    Assert.assertNull(arrayOf<Int>().pureReverseStream().next())
    Assert.assertNull(arrayOf<Int?>(null, null).pureReverseStream().next())
}