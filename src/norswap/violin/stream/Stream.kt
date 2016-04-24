package norswap.violin.stream
import java.util.NoSuchElementException
import java.util.Spliterator
import java.util.function.Consumer
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
     */
    fun toIterator()
        = object: Iterator<T> {
        private var peek: T? = null
        override fun hasNext(): Boolean {
            if (peek == null) peek = this@Stream.next()
            return peek != null
        }
        override fun next() = if (hasNext()) peek!! else throw NoSuchElementException()
    }

    /**
     * Converts this to a kotlin sequence.
     *
     * The stream and the sequence are linked:
     * items consumed within one will not show up in the other.
     */
    fun toSequence() = Sequence { toIterator() }

    /**
     * Converts this to a java stream.
     *
     * Both streams are linked:
     * items consumed within one will not show up in the other.
     */
    fun toJavaStream(): java.util.stream.Stream<@UnsafeVariance T>
    {
        val spliterator = object: Spliterator<T> {
            override fun estimateSize() = Long.MAX_VALUE
            override fun characteristics() = Spliterator.NONNULL
            override fun trySplit() = null
            override fun tryAdvance(action: Consumer<in T>?): Boolean {
                val next = next()
                next?.let { action!!.accept(it) }
                return next != null
            }
        }
        return StreamSupport.stream(spliterator, false)
    }
}