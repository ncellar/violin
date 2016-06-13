@file:Suppress("PackageDirectoryMismatch")
package norswap.violin.utils

/**
 * Returns the result of [f].
 * The point of to allow statements in an expression context.
 */
inline fun <T> expr(f: () -> T): T = f()

/**
 * Returns the receiver after evaluating [f] on it.
 */
inline infix fun <T> T.after(f: (T) -> Unit): T {
    f(this)
    return this
}

/**
 * Analogous to [arrayOf], but doesn't require reification.
 */
@Suppress("UNCHECKED_CAST")
fun <T: Any> array(vararg items: T) = items as Array<T>

/**
 * Shorthand for [StringBuilder.append].
 */
operator fun StringBuilder.plusAssign(s: String) { append(s) }

/**
 * Shorthand for [StringBuilder.append].
 */
operator fun StringBuilder.plusAssign(o: Any?) { append(o) }

/**
 * Casts the receiver to [T].
 *
 * This is more useful than regular casts because it enables casts to non-denotable types
 * through type inference.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any?.cast() = this as T

/**
 * Like [substring] but allows [start] and [end] to be negative numbers, which count down from the
 * end of the string. i.e., a negative [start] is equivalent to `this.length - start`.
 *
 * The [end] bound is exclusive if positive, inclusive if negative.
 * Hence `str[x, str.length] == str[x, -1]` always holds.
 * Another way to put it is that a negative [end] is equivalent to `this.length - end + 1`.
 */
operator fun CharSequence.get(start: Int, end: Int = length) =
    substring(
        if (start >= 0) start else length - start,
        if (end >= 0) end else length - end + 1)