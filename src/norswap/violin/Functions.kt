@file:Suppress("NOTHING_TO_INLINE")
package norswap.violin
/**
 * This file contains inline methods to convert between functions (of max two parameters)
 * and methods as well as functions over pairs.
 */

/**
 * Converts from type `(T) -> R` to `T.() -> R`.
 */
inline fun <T, R> ((T) -> R).methodic(): T.() -> R =
    { this@methodic(this) }

/**
 * Converts from type `T.() -> R` to `(T) -> R`.
 */
inline fun <T, R> (T.() -> R).functional(): (T) -> R =
    { it.this@functional() }

/**
 * Converts from type `(T, P) -> R` to `T.(P) -> R`.
 */
inline fun <T, P, R> ((T, P) -> R).methodic(): T.(P) -> R =
    { this@methodic(this, it) }

/**
 * Converts from type `T.(P) -> R` to `(T, P) -> R`.
 */
inline fun <T, P, R> (T.(P) -> R).functional(): (T, P) -> R =
    { recv, param -> recv.this@functional(param) }

/**
 * Lifts a function of two parameter to a function over a pair.
 */
inline fun <P, Q, R> lift(crossinline f: (P, Q) -> R): (Pair<P, Q>) -> R =
    { f(it.first, it.second) }

/**
 * Lifts a method with a parameter to a function over a pair.
 */
inline fun <A, B, R> liftm(crossinline f: A.(B) -> R): (Pair<A, B>) -> R =
    { f(it.first, it.second) } // same as `lift(f.functional())`

/**
 * Lift a method of two parameters to a method with a pair parameter.
 */
inline fun <T, A, B, R> lift(crossinline f: T.(A, B) -> R): T.(Pair<A, B>) -> R =
    {  f(it.first, it.second) }

/**
 * Lowers a function over a pair to a function of two parameters.
 */
inline fun <A, B, R> lower(crossinline f: (Pair<A, B>) -> R): (A, B) -> R =
    { a, b -> f(a to b) }

/**
 * Lowers a function over a pair to a method with a parameter.
 */
inline fun <T, P, R> lowerm(crossinline f: (Pair<T, P>) -> R): T.(P) -> R =
    { f(this to it) } // same as `lower(f).methodic()`

/**
 * Lowers a method with a pair parameter to over a pair to a method of two parameters.
 */
inline fun <T, A, B, R> lower(crossinline f: T.(Pair<A, B>) -> R): T.(A, B) -> R =
    { a, b -> f(a to b) }