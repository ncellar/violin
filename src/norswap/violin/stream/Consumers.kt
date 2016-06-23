package norswap.violin.stream
import norswap.violin.Stack
import norswap.violin.link.LinkList
import java.util.Comparator

/**
 * Applies [f] to each item in the stream.
 */
inline fun <T: Any> Stream<T>.each(f: (T) -> Unit) {
    var tmp = next()
    while (tmp != null) { f(tmp) ; tmp = next() }
}

/**
 * Pulls all the items of the stream into a mutable list and returns it.
 */
fun <T: Any> Stream<T>.mutableList(): MutableList<T> {
    val list = arrayListOf<T>()
    each { list.add(it) }
    return list
}

/**
 * Pulls all the items of the stream into a list and returns it.
 */
fun <T: Any> Stream<T>.list(): List<T>
    = mutableList()

/**
 * Pulls all the items of the stream into a mutable set and returns it.
 */
fun <T: Any> Stream<T>.mutableSet(): MutableSet<T> {
    val set = mutableSetOf<T>()
    each { set.add(it) }
    return set
}

/**
 * Pulls all the items of the stream into a set and returns it.
 */
fun <T: Any> Stream<T>.set(): Set<T>
    = mutableSet()

/**
 * Pulls all the items of the stream into a link list (the first item of the stream will
 * be the last item of the list) and returns it.
 */
fun <T: Any> Stream<T>.linkList(): LinkList<T> {
    val list = LinkList<T>()
    each { list.push(it) }
    return list
}

/**
 * Pulls all the items of the stream into a stack (the first item of the stream will
 * be the last item of the list) and returns it.
 */
fun <T: Any> Stream<T>.stack(): Stack<T>
    = linkList()

/**
 * Pulls all the items of the stream into an array and returns it.
 */
inline fun <reified T: Any> Stream<T>.array(): Array<T>
    = mutableList().toTypedArray()

/**
 * Pulls all the items of the stream into an array (with type parameter Any) and returns it.
 */
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
fun Stream<Any>.anyArray(): Array<Any>
    = (mutableList() as java.util.List<*>).toArray()

/**
 * Folds [reduce] over the items of the stream, from left to right, using [first] as initial
 * item on the left.
 *
 * e.g. `Stream(1, 2, 3).foldl(0) { r, t -> r + t }` == `(((0 + 1) + 2) + 3)`
 */
inline fun <T: Any, R> Stream<T>.foldl(first: R, reduce: (R, T) -> R): R {
    var tmp = first
    each { tmp = reduce(tmp, it) }
    return tmp
}

/**
 * Folds [reduce] over the items of the stream, from right to left, using [last] as initial
 * item on the right.
 *
 * e.g. `Stream(1, 2, 3).foldr(0) { r, t -> r + t }` == `(((0 + 3) + 2) + 1)`
 */
inline fun <T: Any, R> Stream<T>.foldr(last: R, reduce: (R, T) -> R): R
    = list().reverseStream().foldl(last, reduce)

/**
 * Folds [f] over the items of the stream, from left to right.
 *
 * e.g. `Stream(1, 2, 3).reduce { r, t -> r + t }` == ((1 + 2) + 3)`
 */
inline fun <T: Any> Stream<T>.reduce(f: (T, T) -> T): T?
    = next()?.let { foldl(it, f) }

/**
 * Folds [f] over the items of the stream, from right to left.
 *
 * e.g. `Stream(1, 2, 3).reduce { r, t -> r + t }` == `((3 + 2) + 1)`
 */
inline fun <reified T: Any> Stream<T>.reduceRight(f: (T, T) -> T): T?
    = list().reverseStream().reduce { a, b -> f(a, b) }

/**
 * Returns the last item of this stream.
 */
fun <T: Any> Stream<T>.last(): T? {
    var next = next()
    var last: T? = null
    while (next != null) { last = next ; next = next() }
    return last
}

/**
 * Returns the first item of this stream that satisfies the given predicate, if any.
 */
inline fun <T: Any> Stream<T>.first(crossinline p: (T) -> Boolean): T?
    = filter(p).next()

/**
 * Returns the last item of this stream that satisfies the given predicate, if any.
 */
inline fun <T: Any> Stream<T>.last(crossinline p: (T) -> Boolean): T?
    = filter(p).last()

/**
 * Indicates whether any item of this stream matches the given predicate.
 * This consumes items in the stream up to and including the one matching the predicate.
 */
inline fun <T: Any> Stream<T>.any(crossinline p: (T) -> Boolean): Boolean
    = first(p) != null

/**
 * Indicates whether all items of this stream match the given predicate
 * (true if the stream is empty).
 */
inline fun <T: Any> Stream<T>.all(crossinline p: (T) -> Boolean): Boolean
    = first { !p(it) } == null

/**
 * Returns the number of items left in the stream, consuming all items in the process.
 */
fun <T: Any> Stream<T>.count(): Int {
    var count = 0
    each { ++count }
    return count
}

/**
 * Returns the number of items equal to [e] in the stream.
 */
fun <T: Any> Stream<T>.count(e: Any?): Int
    = filter { it == e }.count()

/**
 * Return the maximum item of the stream.
 * If two items compare identical, the earliest will be preferred.
 */
fun <T: Comparable<T>> Stream<T>.max(): T?
    = foldl(null as T?) { r, t -> if (r == null) t else if (r >= t) r else t }

/**
 * Return the maximum item of the stream, determined by delegation to [f].
 * If two items compare identical, the earliest will be preferred.
 */
inline fun <T: Any, U: Comparable<U>> Stream<T>.maxBy(f: (T) -> U): T?
    = foldl(null as T?) { r, t -> if (r == null) t else if (f(r) >= f(t)) r else t }

/**
 * Return the maximum item of the stream, determined by delegation to [cmp].
 * If two items compare identical, the earliest will be preferred.
 */
fun <T: Any> Stream<T>.maxWith(cmp: Comparator<T>): T?
    = foldl(null as T?) { r, t -> if (r == null) t else if (cmp.compare(r, t) >= 0) r else t }

/**
 * Return the minimum item of the stream.
 * If two items compare identical, the earliest will be preferred.
 */
fun <T: Comparable<T>> Stream<T>.min(): T?
    = foldl(null as T?) { r, t -> if (r == null) t else if (r <= t) r else t }

/**
 * Return the minimum item of the stream, determined by delegation to [f].
 * If two items compare identical, the earliest will be preferred.
 */
inline fun <T: Any, U: Comparable<U>> Stream<T>.minBy(f: (T) -> U): T?
    = foldl(null as T?) { r, t -> if (r == null) t else if (f(r) <= f(t)) r else t }

/**
 * Return the minimum item of the stream, determined by delegation to [cmp].
 * If two items compare identical, the earliest will be preferred.
 */
fun <T: Any> Stream<T>.minWith(cmp: Comparator<T>): T?
    = foldl(null as T?) { r, t -> if (r == null) t else if (cmp.compare(r, t) <= 0) r else t }

/**
 * Returns a mutable map that contains the entry returned by [f] for each item in the stream.
 * The items are inserted in stream order, so the latest item which claims a key wins.
 */
fun <T: Any, K, V> Stream<T>.associateMutable(f: (T) -> Pair<K, V>): MutableMap<K, V> {
    val out = mutableMapOf<K, V>()
    map(f).each { out.put(it.first, it.second) }
    return out
}

/**
 * Returns a mutable map that contains the entry returned by [f] for each item in the stream.
 * The items are inserted in stream order, so the latest item which claims a key wins.
 */
fun <T: Any, K, V> Stream<T>.associate(f: (T) -> Pair<K, V>): Map<K, V>
    = associateMutable(f)

/**
 * Groups items in the stream by the selector returned by [selector].
 */
fun <T: Any, K> Stream<T>.groupBy(selector: (T) -> K): Map<K, List<T>> {
    val map = mutableMapOf<K, MutableList<T>>()
    each { map.getOrPut(selector(it)) { mutableListOf() }.add(it) }
    return map
}

/**
 * Groups items in two lists depending on whether [predicate] returns true (first list) or
 * false (second list) for each item.
 */
fun <T: Any> Stream<T>.partition(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val truthy = mutableListOf<T>()
    val falsy  = mutableListOf<T>()
    each { (if (predicate(it)) truthy else falsy).add(it) }
    return Pair(truthy, falsy)
}

/**
 * Creates a string from all the items separated using [separator] and using the given [prefix] and
 * [postfix] if supplied. If you specify a non-negative value of [limit], only the first [limit]
 * items will be appended, followed by the [truncated] string (which defaults to "...").
 * If non-null, [transform] is applied to all items prior to appending.
 */
fun <T: Any, A : Appendable> Stream<T>.joinTo(
    buffer: A,
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = null
): A
    = list().joinTo(buffer, separator, prefix, postfix, limit, truncated, transform)

/**
 * Creates a string from all the items separated using [separator] and using the given [prefix] and
 * [postfix] if supplied. If you specify a non-negative value of [limit], only the first [limit]
 * items will be appended, followed by the [truncated] string (which defaults to "...").
 * If non-null, [transform] is applied to all items prior to appending.
 */
fun <T: Any> Stream<T>.joinToString(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = null
): String
    = list().joinToString(separator, prefix, postfix, limit, truncated, transform)