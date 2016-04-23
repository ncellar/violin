package norswap.violin
import norswap.violin.stream.Stream

/**
 * A walk is a finite [Stream].
 */
interface Walk <out T: Any>: Stream<T>
{
    companion object {
        /**
         * `Walk { ... }` is a terser way to write
         * `object: Walk<U> { override fun next() = ... }`
         */
        inline operator fun <U: Any> invoke(crossinline nextImpl: () -> U?)
            = object: Walk<U> { override fun next() = nextImpl() }

        /**
         * An empty walk, assignable to Walk<T> for any T.
         */
        val empty = Stream<Nothing> { null }
    }
}