package norswap.violin.stream
import java.util.stream.Stream as JStream

/// Conversion to Stream ---------------------------------------------------------------------------

/**
 * Converts an iterator into a stream.
 *
 * Be cautious: this assumes the iterator is finite and so returns a [Stream].
 * If not so, assign the result to a [Stream] to avoid errors.
 */
fun <T: Any> Iterator<T>.stream(): Stream<T>
    = Stream { if (hasNext()) next() else null }

/**
 * Converts a java stream into a violin stream.
 */
fun <T: Any> java.util.stream.Stream<T>.stream(): Stream<T> {
    val iterator = iterator()
    return Stream { if (iterator.hasNext()) iterator.next() else null }
}

/**
 * Returns a stream consisting of the items of the iterable.
 */
fun <T: Any> Iterable<T>.stream(): Stream<T>
    = iterator().stream()

/**
 * Returns a stream consisting of the items of the sequence.
 */
fun <T: Any> Sequence<T>.stream(): Stream<T>
    = iterator().stream()

/// Conversion to Streamable -----------------------------------------------------------------------

/**
 * Converts an iterable into a streamable.
 */
fun <T: Any> Iterable<T>.streamable() = object: Streamable<T> {
    override fun stream() = this@streamable.iterator().stream()
}

/**
 * Converts a kotlin sequence into a streamable.
 */
fun <T: Any> Sequence<T>.streamable() = object: Streamable<T> {
    override fun stream() = this@streamable.iterator().stream()
}

/// Stream from Collections ------------------------------------------------------------------------

/**
 * Returns a stream consisting of the items of the array.
 */
fun <T: Any> Array<out T>.stream(): Stream<T>{
    var i = 0
    return Stream { if (i < size) get(i++) else null }
}

/**
 * Returns a stream consisting of the items of the array, in reverse order.
 */
fun <T: Any> Array<out T>.reverseStream(): Stream<T> {
    var i = size
    return Stream { if (i > 0) get(--i) else null }
}

/**
 * Returns a stream consisting of the non-null items of the array.
 */
fun <T: Any> Array<out T?>.pureStream(): Stream<T> {
    var i = 0
    return Stream {
        var item: T? = null
        while (i < size && item == null) item = get(i++)
        item
    }
}

/**
 * Returns a stream consisting of the non-null items of the array, in reverse order.
 */
fun <T: Any> Array<out T?>.pureReverseStream(): Stream<T> {
    var i = size
    return Stream {
        var item: T? = null
        while (i > 0 && item == null) item = get(--i)
        item
    }
}

/**
 * Returns a stream consisting of the items of the list, in reverse order.
 */
fun <T: Any> List<T>.reverseStream(): Stream<T>
    = reversed().stream()

/// ------------------------------------------------------------------------------------------------
