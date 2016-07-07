# V I O L I N

Violin is a utility library for Kotlin.

# Streams

Violin's main attraction is its stream capabilities.

Streams are best described as iterators, but on which transformations (`map`,
`filter`, ...) can be applied lazily. Usually the same transformations are
applied eagerly on the stream's source (e.g. `kotlin.List`) or lazily on the
source (e.g. `kotlin.Sequence`). Violin streams are close to Java streams, but
(1) allow incremental (not all-at-once) consumption from the stream, and (2) do
not complect parallelization with stream processing.

See also this [longer comparison of Violin streams to some alternatives][vs].
[vs]: docs/streams_vs.md

Here is a completely bogus example (you're encouraged to submit a better one!):

    1.transitive { it + 1 }
        .dropWhile { it < 10 }
        .upTo { it > 20 }
        .filter { it % 2 == 0 }
        .map { it / 2 }
        .indexed()
        .joinToString(transform = { "${it.index}: ${it.value}" })
        .let { println(it) } // prints "0: 5, 1: 6, 2: 7, 3: 8, 4: 9, 5: 10"
        
The stream interface is dead simple:

    interface Stream <out T: Any> {
        fun stream(): T?
    }

# Linked Lists

Violin features [a flexible singly linked list implementation][lists].
[lists]: docs/lists.md

# Other

- [Maybe][maybe]

- [Conversions][funcs] between functions with and without receivers, over pairs
  or multiple parameters.

- A [stack][stack] interface.

- [Other stuff][misc]
  
[maybe]: docs/maybe.md
[funcs]: docs/funcs.md
[stack]: docs/stack.md
[misc]:  docs/misc.md

# License

Violin is released under the [BSD 3-Clauses License][license].
[license]: LICENSE.md
