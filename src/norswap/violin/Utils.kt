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
