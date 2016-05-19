package norswap.violin.stream
import norswap.violin.utils.after
import norswap.violin.utils.expr

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
inline fun <T: Any> Stream<T>.filter(crossinline keep: (T) -> Boolean): Stream<T> =
    Stream {
        var next: T?
        do { next = next() } while (next?.let{ !keep(it) }?:false)
        next
    }

/**
 * Returns a stream consisting of the items of this stream, paired with its index (starting
 * at 0 for the next item of this stream).
 */
fun <T: Any> Stream<T>.indexed(): Stream<IndexedValue<T>> =
    object: Stream<IndexedValue<T>> {
        private var i = 0
        override fun next() = this@indexed.next()?.let { IndexedValue(i++, it) }
    }

/**
 * Returns a stream consisting of the results of replacing each item of this stream with the
 * contents of a stream produced by applying [f] to the item.
 */
inline fun <T: Any, R: Any> Stream<T>.fmap(crossinline f: (T) -> Stream<R>): Stream<R> =
    object: Stream<R> {
        var nextStream: Stream<R>? = null
        override fun next(): R? {
            var next: R? = null
            while (next == null) {
                if (nextStream == null) nextStream = this@fmap.next()?.let(f)
                if (nextStream == null) return null
                next = nextStream?.next()
                if (next == null) nextStream = null
            }
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
    var dropping = true
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

/**
 * Returns a stream consisting of pairs made up by one item of this stream and one item of
 * [other]. The stream only runs as far as the shortest of the two streams.
 */
fun <T: Any, U: Any> Stream<T>.zip(other: Stream<U>): Stream<Pair<T, U>> =
    Stream {
        val a = next()
        val b = other.next()
        if (a != null && b != null) Pair(a, b) else null
    }

/**
 * Returns a stream consisting of pairs made up by one item of this stream and one item of
 * [other]. The stream runs as far as the longest of the two streams.
 */
fun <T: Any, U: Any> Stream<T>.ziplong(other: Stream<U>): Stream<Pair<T?, U?>> =
    Stream {
        val a = next()
        val b = other.next()
        if (a != null || b != null) Pair(a, b) else null
    }