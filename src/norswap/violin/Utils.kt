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