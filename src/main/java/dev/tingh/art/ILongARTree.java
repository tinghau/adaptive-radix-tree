package dev.tingh.art;

public interface ILongARTree<V> {
    V get(long key);

    boolean contains(long key);

    V getCeiling(long key);

    V getFloor(long key);

    void put(long key, V leaf);

    IArtNode<V> getParent(long key, int parentDepth);

    V remove(long key);
}
