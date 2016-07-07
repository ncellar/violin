# Package norswap.violin.stream

### Stream Operations

The extension functions for [Stream] can be split in two categories: transformers and consumers.
Transformers take a stream as input and return another stream. Consumers consume items from
the stream in order to produce a value.

#### Transformers

1. Mapping: [map], [after], [fmap]
2. Filtering: [filter], [filterMap], [upTo], [upThrough], [dropWhile], [drop], [takeWhile], [limit]
3. Other: [indexed], [distinct], [distinctBy], [zip], [zipLong], [then]
 
#### Consumers

1. Other: [each], [last], [any], [all], [count]
2. Create Data Structures: [mutableList], [list], [mutableSet], [set], [linkList], [stack], [array], [anyArray]
3. Reducing: [foldl], [foldr], [reduce], [reduceRight]
4. Extrema: [maxWith], [minWith], [max], [min]
5. Partition: [associateMutable], [associate], [groupBy], [partition]
6. To String: [joinTo], [jointToString]