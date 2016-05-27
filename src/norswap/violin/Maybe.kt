@file:Suppress("PackageDirectoryMismatch")
package norswap.violin.maybe

/**
 * Represent a possible value ([Some]) or an absence of value ([None]).
 *
 * In general, you should use nullable types (`T?`) instead of Maybe.
 * In some cases, you can't use nullable types because null already fills another role
 * (for instance, it signals to end of a stream). In those case, you can use Maybe, although it
 * is advisable to be wary of it.
 *
 * Work with maybe by converting them nullable values with [invoke].
 */
sealed class Maybe<out T: Any>
{
    class Some<out T: Any>(val value: T): Maybe<T>()
    object None : Maybe<Nothing>()

    override fun toString(): String = when (this) {
        is Some<T> -> "Some($value)"
        is None -> "None"
    }

    operator fun invoke(): T? = when (this) {
        is Some<T> -> value
        is None -> null
    }

    override fun equals(other: Any?)
        =  this === other
        || this is Some
        && other is Some<*>
        && value == other.value

    override fun hashCode() =
        if (this is Some) (value ?. hashCode() ?: 0) * 31
        else super.hashCode()
}

/**
 * An alias for [Maybe.None] that can be imported through `import norswap.violin.maybe.*`.
 */
val None = Maybe.None

/**
 * An alias for the [Maybe.Some] constructor that can be imported through
 * `import norswap.violin.maybe.*`.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T: Any> Some(value: T) = Maybe.Some(value)