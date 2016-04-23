package norswap.violin
import norswap.violin.stream.Stream
import java.util.stream.Stream as JStream

/**
 * Converts an iterator into a walk.
 *
 * Be cautious: this assumes the iterator is finite and so returns a [Walk].
 * If not so, assign the result to a [Stream] to avoid errors.
 */
fun <T: Any> Iterator<T>.tune(): Walk<T>
    = Walk { if(hasNext()) next() else null }

/**
 * Converts an iterable into a walkable.
 *
 * Be cautious: this assumes the iterable yields finite iterators and so returns a [Walkable].
 * If not so, assign the result to a [Streamable] to avoid errors.
 */
fun <T: Any> Iterable<T>.tune(): Walkable<T>
    = object: Walkable<T> {
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
 * Converts a kotlin sequence into a walk.
 */
fun <T: Any> Sequence<T>.tune(): Walk<T>
    = iterator().tune()