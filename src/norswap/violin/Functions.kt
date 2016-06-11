@file:Suppress("NOTHING_TO_INLINE")
package norswap.violin
/**
 * This file contains inline methods to convert between functions (of max two parameters)
 * and methods as well as functions over pairs.
 */

/**
 * Lifts a function of two parameter to a function over a pair.
 */
inline fun <P, Q, R> lift(crossinline f: (P, Q) -> R): (Pair<P, Q>) -> R =
    { f(it.first, it.second) }

/**
 * Lift a function of three parameters to a function whose second parameter is a pair.
 */
inline fun <S, P, Q, R> lift(crossinline f: (S, P, Q) -> R): (S, Pair<P, Q>) -> R =
    {  s, p -> f(s, p.first, p.second) }

/**
 * Lowers a function over a pair to a function of two parameters.
 */
inline fun <P, Q, R> lower(crossinline f: (Pair<P, Q>) -> R): (P, Q) -> R =
    { p, q -> f(p to q) }

/**
 * Lowers a function whose second parameter is a pair to a function of three parameters.
 */
inline fun <S, P, Q, R> lower(crossinline f: (S, Pair<P, Q>) -> R): (S, P, Q) -> R =
    { s, p, q -> f(s, p to q) }