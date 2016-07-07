@file:Suppress("PackageDirectoryMismatch")
package norswap.violin.utils
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Comparator

/**
 * This file contains a smattering of functions that do not find their place anywhere else.
 */

// -------------------------------------------------------------------------------------------------

/**
 * Returns the result of [f].
 * The point is to allow statements in an expression context.
 */
inline fun <T> expr(f: () -> T): T = f()

// -------------------------------------------------------------------------------------------------

/**
 * Returns the receiver after evaluating [f] on it.
 */
inline infix fun <T> T.after(f: (T) -> Unit): T {
    f(this)
    return this
}

// -------------------------------------------------------------------------------------------------

/**
 * Syntactic sugar for `if (this) then f() else null`.
 */
infix inline fun <T> Boolean.then (f: () -> T): T?
    = if (this) f() else null

// -------------------------------------------------------------------------------------------------

/**
 * Analogous to `Kotlin.arrayOf`, but doesn't require reification.
 */
@Suppress("UNCHECKED_CAST")
fun <T: Any> array(vararg items: T) = items as Array<T>

// -------------------------------------------------------------------------------------------------

/**
 * Shorthand for [StringBuilder.append].
 */
operator fun StringBuilder.plusAssign(s: String) { append(s) }

// -------------------------------------------------------------------------------------------------

/**
 * Shorthand for [StringBuilder.append].
 */
operator fun StringBuilder.plusAssign(o: Any?) { append(o) }

// -------------------------------------------------------------------------------------------------

/**
 * Casts the receiver to [T].
 *
 * This is more useful than regular casts because it enables casts to non-denotable types
 * through type inference.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any?.cast() = this as T

// -------------------------------------------------------------------------------------------------

/**
 * Like `String.substring` but allows [start] and [end] to be negative numbers.
 *
 * The first item in the sequence has index `0` (same as `-length`).
 * The last item in the sequence has index `length-1` (same as `-1`).
 *
 * The [start] bound is always inclusive.
 * The [end] bound is exclusive if positive, else exclusive.
 *
 * Throws an [IndexOutOfBoundsException] if the [start] bounds refers to an index below `0` or
 * if [end] refers to an index equal to or past `CharSequence.length`.
 *
 * It is fine to call this with [start] referring to `a` and [end] referring to `b` such that
 * `a > b`, as long the previous condition is respected. The result is then the empty string.
 */
operator fun CharSequence.get(start: Int, end: Int = length): String {
    val a = if (start >= 0) start else length + start
    val b = if (end >= 0) end else length + end + 1
    if (a < 0) throw IndexOutOfBoundsException("Start index < 0")
    if (b > length) throw IndexOutOfBoundsException("End index > length")
    if (a > b) return ""
    return substring(a, b)
}

// -------------------------------------------------------------------------------------------------

/**
 * Reads a complete file and returns its contents as a string.
 * @throws IOException see [Files.readAllBytes]
 * @throws InvalidPathException see [Paths.get]
 */
fun readFile(file: String)
    = String(Files.readAllBytes(Paths.get(file)))

// -------------------------------------------------------------------------------------------------

/**
 * Returns a comparator for type T that delegates to a `Comparable` type U.
 */
fun <T, U: Comparable<U>> comparator(f: (T) -> U)
    = Comparator<T> { o1, o2 -> f(o1).compareTo(f(o2)) }

// -------------------------------------------------------------------------------------------------

/**
 * Returns a comparator for type [T] that delegates the receiver, a comparator for type [U].
 */
fun <T, U> Comparator<U>.derive(f: (T) -> U)
    = Comparator<T> { o1, o2 -> compare(f(o1), f(o2)) }

// -------------------------------------------------------------------------------------------------