package norswap.violin

/**
 * This file contains inline methods to:
 *
 * (1) Convert between function with and without receivers (by shifting the first parameter
 *     into receiver position, or vice-versa): [shift], [unshift].
 *
 * (2) Convert between functions (of max three parameters, so
 *     that you can actually pass a method of two parameters) and functions over pairs.
 */

// -------------------------------------------------------------------------------------------------

/**
 * Shifts the parameter of [f] into receiver position.
 */
inline fun <A, R> shift (crossinline f: (A) -> R): A.() -> R
    = { f(this) }

// -------------------------------------------------------------------------------------------------

/**
 * Shifts the first parameter of [f] into receiver position.
 */
inline fun <A, B, R> shift (crossinline f: (A, B) -> R): A.(B) -> R
    = { b -> f(this, b) }

// -------------------------------------------------------------------------------------------------

/**
 * Unshifts the receiver of [f] into parameter position.
 */
inline fun <A, R> unshift (crossinline f: A.() -> R): (A) -> R
    = { it.f() }

// -------------------------------------------------------------------------------------------------

/**
 * Unshifts the receiver of [f] into parameter position.
 */
fun <A, B, R> unshift (f: A.(B) -> R): (A, B) -> R
    = { a, b -> a.f(b) }

// -------------------------------------------------------------------------------------------------

/**
 * Lifts a function of two parameter to a function over a pair.
 */
inline fun <P, Q, R> lift(crossinline f: (P, Q) -> R): (Pair<P, Q>) -> R =
    { f(it.first, it.second) }

// -------------------------------------------------------------------------------------------------

/**
 * Lift a function of three parameters to a function whose second parameter is a pair.
 */
inline fun <S, P, Q, R> lift(crossinline f: (S, P, Q) -> R): (S, Pair<P, Q>) -> R =
    {  s, p -> f(s, p.first, p.second) }

// -------------------------------------------------------------------------------------------------

/**
 * Lowers a function over a pair to a function of two parameters.
 */
inline fun <P, Q, R> lower(crossinline f: (Pair<P, Q>) -> R): (P, Q) -> R =
    { p, q -> f(p to q) }

// -------------------------------------------------------------------------------------------------

/**
 * Lowers a function whose second parameter is a pair to a function of three parameters.
 */
inline fun <S, P, Q, R> lower(crossinline f: (S, Pair<P, Q>) -> R): (S, P, Q) -> R =
    { s, p, q -> f(s, p to q) }

// -------------------------------------------------------------------------------------------------
