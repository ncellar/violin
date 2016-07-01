package norswap.violin.stream

/**
 * A streamable is an object that can yield or be represented as a [Stream].
 */
interface Streamable <out T: Any>
{
    /**
     * Returns a stream tied to this object.
     *
     * Two successive calls to this method must return equivalent streams, assuming no modification
     * to this object occurred in between the two calls. Two streams are equivalent if they yield
     * the same sequence of items.
     */
    fun stream(): Stream<T>

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns an iterator backed by a stream.
     *
     * This method is required in order to be able to use [Streamable] instances in Kotlin for
     * loops. This is why it is not an extension method
     * (to make for loops available without the need to import extension methods).
     */
    operator fun iterator(): Iterator<T>
        = stream().iterator()
}
