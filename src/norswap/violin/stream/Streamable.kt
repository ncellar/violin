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

    /**
     * Returns an iterable backed by the streamable.
     */
    fun iterable() = object: Iterable<T> {
        override fun iterator() = this@Streamable.stream().iterator()
    }

    /**
     * Returns an iterator over [stream]. This method is required in order to be able
     * to use [Streamable] instances in Kotlin for loops.
     */
    operator fun iterator(): Iterator<T> = stream().iterator()

    /**
     * Returns a sequence backed by the streamable.
     */
    fun sequence() = object: Sequence<T> {
        override fun iterator() = this@Streamable.stream().iterator()
    }
}