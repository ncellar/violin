package norswap.violin

/**
 * An interface analogous to the Kotlin version of `Cloneable`, except that [copy] is public
 * and that [copycast] is available for effortless casting.
 */
interface Copyable: Cloneable
{
    /**
     * Returns a copy of the object.
     *
     * The copy is not guaranteed to be deep, meaning the copy may hold references to mutable
     * data structures shared with the original.
     */
    fun copy(): Any = clone()

    // ---------------------------------------------------------------------------------------------

    /**
     * Same as [copy], but casts the copy to [T] (which should be inferred, use an explicit cast
     * otherwise).
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: Any> copycast(): T = clone() as T
}
