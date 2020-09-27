package dev.tingh.art;

public interface IArtNode<V> {

    long getKey();

    int getDepth();

    int getShift();

    short getCount();

    boolean isFull();

    ArtNodeType getNodeType();

    boolean canShrink();

    Object get(long key);

    Object getCeiling(long key);

    Object getFirst();

    Object getFloor(long key);

    Object getLast();

    boolean put(long key, V value);

    boolean put(long key, IArtNode<V> node);

    V remove(long key);
}
