package norswap.violin

/**
 * A stream is a (potentially infinite) sequence of items.
 *
 * For streams that are always finite, use [Walk].
 */
interface Stream <out T: Any>
{
    /**
     * Returns the next item in the stream, or ´null´ if the stream is the stream is over.
     *
     * Once this returns `null`, subsequent calls must return ´null´ as well.
     */
    fun next(): T?
}