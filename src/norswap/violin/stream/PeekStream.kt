package norswap.violin.stream
import norswap.violin.utils.after

/**
 * A peek stream is a stream where one can peek at the next value without advancing the stream.
 */
interface PeekStream <out T: Any>: Stream<T>
{
    companion object {
        /**
         * Creates a [PeekStream] from a [Stream.next] implementation
         * by caching the next item.
         */
        inline operator fun <U: Any> invoke(crossinline nextImpl: () -> U?)
            = object: PeekStream<U> {
                var next: U? = null
                override fun peek()
                    = if (next == null) { next = nextImpl() ; next }
                      else next
                override fun next()
                    = if (next == null) nextImpl()
                      else next.after { next = null }
            }

        /**
         * Creates a [PeekStream] from an existing stream by caching the next item.
         */
        operator fun <U: Any> invoke(stream: Stream<U>)
            = invoke { stream.next() }

        /**
         * Creates a [PeekStream] from the given items.
         */
        operator fun <U: Any> invoke(vararg items: U)
            = invoke(Stream(*items))

        /**
         * An empty peek stream, assignable to PeekStream<T> for any T.
         */
        val empty = PeekStream<Nothing> { null }
    }

    /**
     * Peek at the next value (the value that [next] would return) without advancing the stream.
     *
     * The next call to [peek] or [next] must return the same value.
     */
    fun peek(): T?
}