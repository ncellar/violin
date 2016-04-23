@file:Suppress("PackageDirectoryMismatch")
package norswap.violin.utils

/**
 * Returns the receiver after evaluating [f] on it.
 */
inline infix fun <T> T.after(f: (T) -> Unit): T {
    f(this)
    return this
}
