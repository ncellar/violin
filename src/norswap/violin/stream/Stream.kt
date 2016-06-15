package norswap.violin.stream
import norswap.violin.utils.after
import java.util.NoSuchElementException
import java.util.stream.StreamSupport

/**
 * A stream is a (potentially infinite) sequence of items.
 * As such, a stream usually describes a lazy computation.
 */
interface Stream <out T: Any>
{
    companion object {
        /**
         * `Stream { ... }` is a terser way to write
         * `object: Stream<U> { override fun next() = ... }`
         */
        inline operator fun <U: Any> invoke(crossinline nextImpl: () -> U?)
            = object: Stream<U> { override fun next() = nextImpl() }

        /**
         * Enables the syntax `Stream(1, 2, 3)` instead of `arrayOf(1, 2, 3).stream()`.
         */
        operator fun <U: Any> invoke(vararg items: U)
            = items.stream()

        /**
         * An empty stream, assignable to Stream<T> for any T.
         */
        val empty = Stream<Nothing> { null }
    }

    /**
     * Returns the next item in the stream, or ´null´ if the stream is the stream is over.
     *
     * Once this returns `null`, subsequent calls must return ´null´ as well.
     */
    fun next(): T?

    /**
     * Converts this to an iterator.
     *
     * The stream and the iterator are linked:
     * items consumed within one will not show up in the other.
     *
     * This method ensures that streams can be used in Kotlin for-loops.
     * This is why it isn't called `toIterator`.
     */
    operator fun iterator() = object: Iterator<T> {
        private var peek: T? = this@Stream.next()
        override fun hasNext() = peek != null
        override fun next(): T
            = (peek ?: throw NoSuchElementException()) after { peek = this@Stream.next() }
    }

    /**
     * Converts this to a kotlin sequence.
     *
     * The stream and the sequence are linked:
     * items consumed within one will not show up in the other.
     */
    fun toSequence() = Sequence { iterator() }

    /**
     * Converts this to a java stream.
     *
     * Both streams are linked:
     * items consumed within one will not show up in the other.
     */
    fun toJavaStream() = StreamSupport.stream(
        java.lang.Iterable<@UnsafeVariance T> { this@Stream.iterator() }.spliterator(),
        false)
}

/**
 * Returns the stream consisting of the receiver and the transitive closure of function [f] over
 * this receiver. Each item in the sequence (excepted the first) is the result of applying [f] on
 * the previous item.
 */
fun <T: Any> T.transitive(f: (T) -> T?): Stream<T> {
    var first = true
    var last: T? = this
    return Stream {
        if (first) last after { first = false }
        else if (last == null) null
        else f(last as T) after { last = it }
    }
}