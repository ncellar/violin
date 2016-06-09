package norswap.violin.link
import norswap.violin.Stack
import norswap.violin.stream.*
import norswap.violin.utils.after

/**
 * A mutable singly linked list implemented as a pointer to an immutable linked list ([Link])
 * and a size.
 *
 * [size] must be the real size of [link].
 *
 * The [equals] method is shallow: two link lists are equal if their [size] are the same and
 * their [link] are reference-equals.
 */
class LinkList<T: Any> (
    var link: Link<T>? = null,
    override var size: Int = link.iterator().stream().count())
: Stack<T>, Cloneable
{
    /**
     * Builds a mutable linked list from [items].
     */
    constructor (vararg items: T) : this(null, 0) {
        items.reverseStream().each { push(it) }
    }

    override fun stream(): PeekStream<T> = link.stream()

    /**
     * Returns a stream of the links composing this linked list.
     */
    fun linkStream(): PeekStream<Link<T>> = link.linkStream()

    override val empty: Boolean
        get() = size == 0

    override fun push(item: T) { link = Link(item, link) ; ++ size }
    override fun peek(): T? = link ?. item
    override fun pop(): T? = link ?. item ?. after { link = link?.next ; -- size }

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