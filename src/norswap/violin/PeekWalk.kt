package norswap.violin

/**
 * A peek walk is a walk where one can peek at the next value without advancing the walk.
 */
interface PeekWalk <out T: Any>: PeekStream<T>, Walk<T>