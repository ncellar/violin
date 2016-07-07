package norswap.violin
import norswap.violin.stream.PeekStream
import norswap.violin.stream.Streamable
import norswap.violin.stream.last
import norswap.violin.stream.limit

/**
 * A fairly classical stack interface, conspicuously missing from both Java and Kotlin stdlibs.
 */
interface Stack<T: Any>: Streamable<T>
{
    // ---------------------------------------------------------------------------------------------

    /**
     * The number of item in this stack.
     */
    val size: Int

    // ---------------------------------------------------------------------------------------------

    /**
     * Is the stack empty?
     */
    val empty: Boolean
        get() = size == 0

    // ---------------------------------------------------------------------------------------------

    /**
     * Adds [item] on the top of the stack.
     */

    fun push(item: T)

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns the item at the top of the stack, or null if the stack is empty.
     */
    fun peek(): T?

    // ---------------------------------------------------------------------------------------------

    /**
     * Removes and returns the item at the top of the stack, or null if the stack is empty.
     */
    fun pop(): T?

    // ---------------------------------------------------------------------------------------------

    /**
     * Return the item at the given depth. The item at depth 0 is the top of the stack.
     */
    fun at(depth: Int): T? =
        if (size <= depth) null
        else stream().limit(depth + 1).last()

    // ---------------------------------------------------------------------------------------------

    /**
     * Pop items from the stack until its size is [target].
     * If `size > target`, does nothing.
     */
    fun truncate(target: Int) {
        while (size > target) pop()
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns a stream consisting of the items of the stack.
     * Consuming items from the stream (with [next]) pops them from the stack.
     */
    fun poppingStream() = object: PeekStream<T> {
        override fun peek() = this@Stack.peek()
        override fun next() = this@Stack.pop()
    }

    // ---------------------------------------------------------------------------------------------

    override fun stream(): PeekStream<T>
}
