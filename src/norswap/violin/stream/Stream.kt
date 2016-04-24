package norswap.violin.stream
import norswap.violin.utils.after
import norswap.violin.utils.expr
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

/**
 * Returns a stream consisting of the results of applying [f] to the items of this stream.
 */
inline fun <T: Any, R: Any> Stream<T>.map(crossinline f: (T) -> R): Stream<R>
    = Stream { next()?.let(f) }

/**
 * Returns a stream that yields the same items as this stream, but applying [f] to each
 * item before yielding it.
 */
inline fun <T: Any> Stream<T>.apply(crossinline f: T.() -> Unit): Stream<T>
    = Stream { next()?.apply(f) }

/**
 * Returns a stream that yields the same items as this stream, but applying [f] to each
 * item before yielding it.
 */
inline fun <T: Any> Stream<T>.after(crossinline f: (T) -> Unit): Stream<T>
    = Stream { next()?.after(f) }

/**
 * Returns a stream consisting of the items of this stream that match the [keep] predicate.
 */
inline fun <T: Any> Stream<T>.filter(crossinline keep: (T) -> Boolean): Stream<T>
    = Stream {
        var next: T?
        do { next = next() } while (next?.let{ !keep(it) }?:false)
        next
    }

/**
 * Returns a stream consisting of the items of this stream, paired with its index (starting
 * at 0 for the next item of this stream).
 */
fun <T: Any> Stream<T>.indexed(): Stream<Pair<Int, T>>
    = object: Stream<Pair<Int, T>> {
        private var i = 0
        override fun next() = this@indexed.next()?.let { Pair(i++, it) }
    }

/**
 * Returns a stream consisting of the results of replacing each item of this stream with the
 * contents of a stream produced by applying [f] to the item.
 */
inline fun <T: Any, R: Any> Stream<T>.fmap(crossinline f: (T) -> Stream<R>): Stream<R>
    = object: Stream<R> {
        var nextStream: Stream<R>? = null
        override fun next(): R? {
            var next: R?
            do {
                nextStream = this@fmap.next()?.let(f)
                next = nextStream?.next()
            } while (nextStream != null && next == null)
            return next
        }
    }

/**
 * Returns a stream consisting of the items of this stream, until an item matching [stop] is
 * encountered. The returned stream does not yield the matching item.
 */
inline fun <T: Any> Stream<T>.upTo(crossinline stop: (T) -> Boolean): Stream<T> {
    var ongoing = true
    return filter { ongoing && expr { ongoing = !stop(it) ; ongoing } }
}

/**
 * Returns a stream consisting of the items of this stream, until an item matching [stop] is
 * encountered. The matching item will be the last item of the returned stream.
 */
inline fun <T: Any> Stream<T>.upThrough(crossinline stop: (T) -> Boolean): Stream<T> {
    var ongoing = true
    return filter { ongoing && expr { ongoing = !stop(it) ; true } }
}

/**
 * Returns a stream consisting of the items of this stream, minus all the items at the beginning
 * of the stream that match [drop].
 */
inline fun <T: Any> Stream<T>.dropWhile(crossinline drop: (T) -> Boolean): Stream<T> {
    var dropping = false
    return filter { !dropping || expr { dropping = drop(it) ; !dropping } }
}

/**
 * Returns a stream consisting of the items of this stream, minus its [n] first items.
 */
fun <T: Any> Stream<T>.drop(n: Int = 1): Stream<T> {
    var i = 0
    return dropWhile { (i < n) after { if(it) ++i } }
}

/**
 * Returns a stream consisting of the items of this stream, until an item that doesn't match
 * [keep] is encountered (this item will not be yielded by the returned stream).
 */
inline fun <T: Any> Stream<T>.takeWhile(crossinline keep: (T) -> Boolean): Stream<T>
    = upTo { !keep(it) }

/**
 * Returns a stream consisting of the items of this stream, with a limit of [n].
 */
fun <T: Any> Stream<T>.limit(n: Int): Stream<T> {
    var i = 0
    return takeWhile { (i < n) after { if (it) ++i } }
}