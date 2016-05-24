@file:Suppress("PackageDirectoryMismatch")
package norswap.violin.link
import norswap.violin.Stack
import norswap.violin.stream.*
import norswap.violin.utils.after

/**
 * Simple immutable linked list data structure. Use null as nil.
 */
class Link<out T: Any> (val item: T, val next: Link<T>?): Streamable<T>, Cloneable
{
    override fun stream() = object: PeekStream<T> {
        var link: Link<@UnsafeVariance T>? = this@Link
        override fun peek() = link?.item
        override fun next() = link?.item.after { link = link?.next }
    }

    /**
     * Returns a stream of the links composing this linked list.
     */
    fun linkStream() = object: PeekStream<Link<T>> {
        var link: Link<@UnsafeVariance T>? = this@Link
        override fun peek() = link
        override fun next() = link.after { link = link?.next }
    }

    override public fun clone(): Link<T> = this
    override fun toString() = stream().joinToString()
}

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

/**
 * A mutable linked list implemented as a pointer to an immutable linked list ([Link]) and a size.
 *
 * [size] must be the real size of [link].
 *
 * The [equals] method is shallow: two link lists are equal if their [size] are the same and
 * their [link] are reference-equals.
 */
class LinkList<T: Any> (
    var link: Link<T>? = null,
    override var size: Int = link.iterator().stream().count()
): Stack<T>, Cloneable
{
    override fun stream(): PeekStream<T> = link.stream()

    /**
     * Returns a stream of the links composing this linked list.
     */
    fun linkStream(): PeekStream<Link<T>> = link.linkStream()

    override val empty: Boolean
        get() = size == 0

    override fun push(item: T) { link = Link(item, link) }
    override fun peek(): T? = link ?. item
    override fun pop(): T? = link ?. item ?. after { link = link?.next }

    override public fun clone(): LinkList<T> = LinkList(link, size)
    override fun toString() = stream().joinToString()

    /**
     * Shallow comparison: two link lists are equal if their [size] are the same and
     * their [link] are reference-equals.
     */
    override fun equals(other: Any?) =
        (other is LinkList<*>)
        && other.size === this.size
        && other.link === this.link

    override fun hashCode() = (link?.hashCode() ?: 0) * 31 + size
}