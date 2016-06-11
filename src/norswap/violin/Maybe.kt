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
open class Maybe<out T: Any> internal constructor()
{
    override fun toString(): String = when (this) {
        is Some<T> -> "Some($value)"
        else -> "None"
    }

    operator fun invoke(): T? = when (this) {
        is Some<T> -> value
        else -> null
    }

    override fun equals(other: Any?)
        =  this === other
        || this is Some
        && other is Some<*>
        && value == other.value

    override fun hashCode() =
        if (this is Some) value.hashCode() * 31
        else super.hashCode()
}

/**
 * See [Maybe].
 */
class Some<out T: Any>(val value: T): Maybe<T>()

/**
 * See [Maybe].
 */
object None : Maybe<Nothing>()

/**
 * Creates a [Maybe] instance from a nullable object.
 */
fun <T: Any> Maybe(item: T?): Maybe<T> =
    if (item != null) Some(item) else None
