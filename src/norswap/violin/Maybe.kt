package norswap.violin

/**
 * Represent a possible value ([Some]) or an absence of value ([None]).
 *
 * In general, you should use nullable types (`T?`) instead of Maybe.
 * In some cases, you can't use nullable types because null already fills another role
 * (for instance, it signals to end of a stream). In those case, you can use Maybe, although it
 * is advisable to be wary of it.
 *
 * You can convert a nullable into a Maybe using the [Maybe] function.
 * You can convert a Maybe into a nullable by using [invoke].
 *
 * This is not a sealed class because a sealed would requires an annoying additional
 * import to use [Some] and [None] directly (e.g. `import norswap.violin.Maybe.*`).
 */
open class Maybe<out T: Any> internal constructor()
{
    override fun toString(): String = when (this) {
        is Some<T> -> "Some($value)"
        else -> "None"
    }

    // ---------------------------------------------------------------------------------------------

    operator fun invoke(): T? = when (this) {
        is Some<T> -> value
        else -> null
    }

    // ---------------------------------------------------------------------------------------------

    override fun equals(other: Any?)
        =  this === other
        || this is Some
        && other is Some<*>
        && value == other.value

    // ---------------------------------------------------------------------------------------------

    override fun hashCode() =
        if (this is Some) value.hashCode() * 31
        else super.hashCode()
}

////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * See [Maybe].
 */
class Some<out T: Any>(val value: T): Maybe<T>()

////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * See [Maybe].
 */
object None : Maybe<Nothing>()

////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Creates a [Maybe] instance from a nullable object (null -> [None]).
 */
fun <T: Any> Maybe(item: T?): Maybe<T> =
    if (item != null) Some(item) else None

////////////////////////////////////////////////////////////////////////////////////////////////////
