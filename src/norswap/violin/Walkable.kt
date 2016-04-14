package norswap.violin

/**
 * A walkable is an object that can yield or be represented as a [Walk].
 */
interface Walkable <out T: Any>: Streamable<T>
{
    override fun stream(): Walk<T>

    /**
     * Converts this to an iterable.
     */
    fun toIterable()
        = object: Iterable<T> { override fun iterator() = stream().toIterator() }
}