package norswap.violin

/**
 * An interface analogous to the Kotlin version of [Cloneable], except that [copy] is public
 * and that [copycast] is available for effortless casting.
 */
interface Copyable: Cloneable {
    fun copy(): Any = clone()
    @Suppress("UNCHECKED_CAST")
    fun <T: Any> copycast(): T = clone() as T
}
