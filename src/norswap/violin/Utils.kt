@file:Suppress("PackageDirectoryMismatch")
package norswap.violin.utils

inline infix fun <T> T.after(f: (T) -> Unit): T {
    f(this)
    return this
}
