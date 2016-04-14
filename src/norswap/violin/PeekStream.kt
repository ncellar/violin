package norswap.violin

/**
 * A peek stream is a stream where one can peek at the next value without advancing the stream.
 */
interface PeekStream <out T: Any>: Stream<T>
{
    /**
     * Peek at the next value (the value that [next] would return) without advancing the stream.
     *
     * The next call to [peek] or [next] must return the same value.
     */
    fun peek(): T?
}