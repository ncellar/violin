package norswap.violin.stream

/**
 * Applies [f] to each item in the stream.
 */
inline fun <T: Any> Stream<T>.each(f: (T) -> Unit) {
    var tmp = next()
    while (tmp != null) { f(tmp) ; tmp = next() }
}

/**
 * Pulls all the items of the stream into an array and returns it.
 */
inline fun <reified T: Any> Stream<T>.array(): Array<T> {
    val list = arrayListOf<T>()
    each { list.add(it) }
    return list.toTypedArray()
}

/**
 * Folds [reduce] over the items of the stream, from left to right, using [first] as initial
 * item on the left.
 *
 * e.g. `Stream(1, 2, 3).foldl(0) { r, t -> r + t }` == `(((0 + 1) + 2) + 3)`
 */
inline fun <T: Any, R: Any> Stream<T>.foldl(first: R, reduce: (R, T) -> R): R {
    var tmp = first
    each { tmp = reduce(tmp, it) }
    return tmp
}

/**
 * Folds [reduce] over the items of the stream, from right to left, using [last] as initial
 * item on the right.
 *
 * e.g. `Stream(1, 2, 3).foldr(0) { r, t -> r + t }` == `(0 + (1 + (2 + 3)))`
 */
inline fun <reified T: Any, R: Any> Stream<T>.foldr(last: R, reduce: (R, T) -> R): R
    = array().reverseStream().foldl(last, reduce)

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
 * e.g. `Stream(1, 2, 3).reduce { r, t -> r + t }` == `(1 + (2 + 3))`
 */
inline fun <reified T: Any> Stream<T>.reduceRight(f: (T, T) -> T): T?
    = next()?.let { foldr(it, f) }