package norswap.violin
import norswap.violin.stream.Stream
import java.util.stream.Stream as JStream

/**
 * Converts an iterator into a stream.
 *
 * Be cautious: this assumes the iterator is finite and so returns a [Stream].
 * If not so, assign the result to a [Stream] to avoid errors.
 */
fun <T: Any> Iterator<T>.tune(): Stream<T>
    = Stream { if(hasNext()) next() else null }

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