# V I O L I N

Violin is a utility library for Kotlin.

It tries to be **human-sized**, i.e., small enough that its code can be quickly
understood without significant time investment. Hence, it focuses on a few
orthogonal features and good documentation.

- [Presentation](#Presentation)
- [Installation](#Installation)
- [Documentation](#Documentation)
- [Roadmap](#Roadmap)
- [License (BSD-3)](LICENSE.md)

# Presentation

## Streams

Violin's main attraction is its stream capabilities.

Streams are best described as iterators, but on which transformations (`map`,
`filter`, ...) can be applied lazily. Usually the same transformations are
applied eagerly on the stream's source (e.g. `kotlin.List`) or lazily on the
source (e.g. `kotlin.Sequence`). Violin streams are close to Java streams, but
(1) allow incremental (not all-at-once) consumption from the stream, and (2) do
not complect parallelization with stream processing.

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

## Other

- [Maybe][maybe]

- A flexible [singly linked list][lists] implementation.

- [Conversions][funcs] between functions with and without receivers, over pairs
  or multiple parameters.

- A [stack][stack] interface.

- [Other stuff][misc]

[lists]: http://norswap.com/violin/kotlin/norswap.violin.link/index.html
[maybe]: http://norswap.com/violin/kotlin/norswap.violin/-maybe/index.html
[funcs]: http://norswap.com/violin/kotlin/norswap.violin/index.html
[stack]: http://norswap.com/violin/kotlin/norswap.violin/-stack/index.html
[misc]:  http://norswap.com/violin/kotlin/norswap.violin.utils/index.html

# Installation

### Manual

Download [the latest release](releases) and be include it in your classpath.

    curl -T https://github.com/norswap/violin/releases/download/0.0.1/violin-0.0.1.jar

### Maven

    <dependency>
      <groupId>com.norswap</groupId>
      <artifactId>violin</artifactId>
      <version>0.0.1</version>
    </dependency>

### Gradle

    compile 'com.norswap:violin:0.0.1'

### Build from Source

You need `make`, `curl` and a JDK (8+).

You also need the CLI Kotlin compiler (1.0.3 - superior *should be* compatible).
If you don't have it, run `make kotlin` to install the 1.0.3 compiler locally
before continuing.

    make deps
    make build
    make buildTests
    make test
    make jar
    
More targets (`docs`, `clean`, ...) available in the [makefile](makefile).
    
# Documentation

- [KDoc](http://norswap.com/violin/kotlin) (to use violin from Kotlin)
- [Javadoc](http://norswap.com/violin/java) (to use violin from Java)

# Roadmap

- Implement fast hashtable using [Robin Hood Hashing][robin].
- Implement a fast *immutable* map using [optimized hash-array map tries][hamt].

[robin]: http://sebastiansylvan.com/post/robin-hood-hashing-should-be-your-default-hash-table-implementation/
[hamt]: http://michael.steindorfer.name/publications/oopsla15.pdf
