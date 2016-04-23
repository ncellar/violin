package norswap.violin
import norswap.violin.stream.Stream

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
}