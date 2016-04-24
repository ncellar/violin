package norswap.violin.stream
import java.util.stream.Stream as JStream

/// Conversion to Stream ---------------------------------------------------------------------------

/**
 * Converts an iterator into a stream.
 *
 * Be cautious: this assumes the iterator is finite and so returns a [Stream].
 * If not so, assign the result to a [Stream] to avoid errors.
 */
fun <T: Any> Iterator<T>.tune(): Stream<T>
    = Stream { if (hasNext()) next() else null }

/**
 * Converts a java stream into a violin stream.
 */
fun <T: Any> java.util.stream.Stream<T>.tune(): Stream<T> {
    val iterator = iterator()
    return Stream { if (iterator.hasNext()) iterator.next() else null }
}

/**
 * Converts a kotlin sequence into a stream.
 */
fun <T: Any> Sequence<T>.tune(): Stream<T>
    = iterator().tune()

/// Conversion to Streamable -----------------------------------------------------------------------

/**
 * Converts an iterable into a streamable.
 *
 * Be cautious: this assumes the iterable yields finite iterators and so returns a [Streamable].
 * If not so, assign the result to a [Streamable] to avoid errors.
 */
fun <T: Any> Iterable<T>.tune(): Streamable<T>
    = object: Streamable<T> {
        override fun stream() = iterator().tune()
    }

/// Stream from standard types ---------------------------------------------------------------------

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
 * Returns a stream consisting of the items of the iterable.
 */
fun <T: Any> Iterable<T>.stream(): Stream<T>
    = iterator().tune()

// -------------------------------------------------------------------------------------------------