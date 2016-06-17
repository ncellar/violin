package norswap.violin.link
import norswap.violin.stream.*
import norswap.violin.utils.after

/**
 * Immutable singly linked list data structure. Use null as nil.
 */
class Link<out T: Any> (val item: T, val next: Link<T>?): Streamable<T>, Cloneable
{
    override fun stream() = object: PeekStream<T> {
        var link: Link<@UnsafeVariance T>? = this@Link
        override fun peek() = link?.item
        override fun next() = link?.item after { link = link?.next }
    }

    /**
     * Returns a stream of the links composing this linked list.
     */
    fun linkStream() = object: PeekStream<Link<T>> {
        var link: Link<@UnsafeVariance T>? = this@Link
        override fun peek() = link
        override fun next() = link after { link = link?.next }
    }

    override public fun clone(): Link<T> = this
    override fun toString() = stream().joinToString()
}

/**
 * Builds an immutable singly linked list from [items].
 *
 * The items are inserted right-to-left (so the last item is in the innermost link, and
 * iteration order is order in which the items appear).
 */
fun <T: Any> Link(vararg items: T): Link<T>? =
    items.foldRight<T, Link<T>?>(null) { it, r -> Link(it, r) }

/**
 * Returns the stream of a potentially empty immutable linked list.
 */
fun <T: Any> Link<T>?.stream() = this?.stream() ?: PeekStream.empty

/**
 * Returns the [linkStream] of a potentially empty immutable linked list.
 */
fun <T: Any> Link<T>?.linkStream() = this?.linkStream() ?: PeekStream.empty

/**
 * Returns the iterator of a potentially empty immutable linked list.
 */
operator fun <T: Any> Link<T>?.iterator() = this?.iterator() ?: emptyList<T>().iterator()

fun <T: Any> Link<T>?.toString() = stream().joinToString()
