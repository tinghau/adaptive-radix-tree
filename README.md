# adaptive-radix-tree

### Description
An implementation of the adaptive radix tree, for more information take a look at the paper: 
“The Adaptive Radix Tree: ARTful Indexing for Main-Memory Databases” by Viktor Leis.

I built this project purely out of interest and is not used elsewhere.

However, the implementation is well-tested, currently with the following test coverage: 
100% class (10/10), 94% method (179/189), 93% line (784/835) coverage.
This adaptive radix tree implementation performs favourably compared to TreeMap (OpenJDK-14) and Long2ObjectRBMap (fastutil-8.4.1).

### Installation
This module builds using Gradle.
While the implementation depends only on Core Java classes, the tests have a dependency on JUnit and log4j.

### Usage
There is `ILongARTree<V>` interface, this is it:

````java
public interface ILongARTree<V> {
    V get(long key);

    V getCeiling(long key);

    V getFloor(long key);

    boolean contains(long key);

    void put(long key, V leaf);

    V remove(long key);
}
````

The implementation is `LongARTree<V>`.
Note that the keys are of type long. 
If converting another type to a long value, 
make sure the resulting value is unique and (ideally) inherits the ordering characteristic of the original type.

### To Do
* Better separation of duties between the adaptive radix tree and its nodes.
* Better ceiling / floor navigation, if the ceiling or floor entry is not a sibling, then the search currently starts again from the root with a modified key.
* Extend key type support beyond long types.
* Review the Art node header memory usage.
