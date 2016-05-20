package norswap.violin
import norswap.violin.stream.PeekStream
import norswap.violin.stream.Streamable
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

    override public fun clone(): Link<T> = this
}

/**
 * Returns the stream of a potentially empty immutable linked list.
 */
fun <T: Any> Link<T>?.stream() = this?.stream() ?: PeekStream.empty

/**
 * Returns the iterator of a potentially empty immutable linked list.
 */
operator fun <T: Any> Link<T>?.iterator() = this?.iterator() ?: emptyList<T>().iterator()

/**
 * A mutable linked list implemented as a pointer to an immutable linked list ([Link]) and a size.
 */
class LinkList<T: Any> (
    var link: Link<T>? = null,
    var size: Int = link.iterator().asSequence().count()
): Streamable<T>, Cloneable
{
    override fun stream(): PeekStream<T> = link.stream()

    val empty: Boolean
        get() = size == 0

    fun push(item: T) { link = Link(item, link) }
    fun peek(): T? = link ?. item
    fun pop(): T? = link ?. item ?. after { link = link?.next }

    override public fun clone(): LinkList<T> = LinkList(link, size)
}