package dev.tingh.art;

import java.util.Arrays;

import static dev.tingh.art.Art.toNodeLevelKeys;
import static dev.tingh.art.ArtNodes.*;
import static dev.tingh.art.ArtNodes.isNodeLessThan;
import static java.util.Arrays.binarySearch;

public class ArtNode16BinarySearch<V> implements IArtNode<V> {

    protected final short[] keys = new short[16];
    protected final Object[] nodes = new Object[16];

    private final int depth;
    private final int shift;
    private final long key;

    protected short count = 0;

    public ArtNode16BinarySearch(long key) {
        this(key,0);
    }

    public ArtNode16BinarySearch(long key, int depth) {
        this.key = key;
        this.depth = depth;
        this.shift = depth * 8;
    }

    public ArtNode16BinarySearch(ArtNode4<V> node) {
        this(node.getKey(), node.getDepth());

        for (int i = 0; i < node.getCount(); i++) {
            insert(node.keys[i], node.nodes[i]);
        }
        this.count = node.getCount();
    }

    public ArtNode16BinarySearch(ArtNode48<V> node) {
        this(node.getKey(), node.getDepth());

        int index = 0;

        for (short i=0; index<node.getCount(); i++) {
            if (node.index[i] != -1) {
                this.keys[index] = i;
                this.nodes[index] = node.nodes[node.index[i]];
                index++;
            }
        }
        this.count = node.getCount();
    }

    @Override
    public long getKey() {
        return key;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public int getShift() {
        return shift;
    }

    @Override
    public short getCount() {
        return count;
    }

    @Override
    public boolean isFull() {
        return count == 16;
    }

    @Override
    public ArtNodeType getNodeType() {
        return ArtNodeType.ArtNode16;
    }

    @Override
    public boolean canShrink() {
        return count <= 4;
    }

    @Override
    public Object get(long key) {
        if (!inNode(this, key)) {
            return null;
        }
        int index = binarySearch(keys, 0, count, (short)(key >>> shift & 255L));
        if (index < 0) {
            return null;
        }
        return nodes[index];
    }

    @Override
    public Object getCeiling(long key) {
        if (!isNodeGreaterThanOrEqualTo(this, key)) {
            return null;
        }

        short nodeIndex = ArtNodes.isNodeGreaterThan(this, key) ? 0 : (short)(key >>> shift & 255L);

        int index = binarySearch(keys, 0, count, nodeIndex);
        if (index >= 0) {
            return nodes[index];
        }
        index = -index - 1;
        if (index < count && ArtNodes.isGreaterThan(nodes[index], key))  {
            return nodes[index];
        }
        return null;
    }

    @Override
    public Object getFirst() {
        return nodes[0];
    }

    @Override
    public Object getFloor(long key) {
        if (!isNodeLessThanOrEqualTo(this, key)) {
            return null;
        }

        short nodeIndex = isNodeLessThan(this, key) ? 255 : (short)(key >>> shift & 255L);
        int index = binarySearch(keys, 0, count, nodeIndex);
        if (index >= 0 ) {
            return nodes[index];
        }
        index = -index - 2;
        if (index < count && index >= 0 && keys[index] <= (key >>> shift & 255L)
                && ArtNodes.isLessThan(nodes[index],key)) {
            return nodes[index];
        }
        return null;
    }

    @Override
    public Object getLast() {
        if (count == 0) {
            return null;
        }
        return nodes[count-1];
    }

    @Override
    public boolean put(long key, V value) {
        return putObject(key, value);
    }

    @Override
    public boolean put(long key, IArtNode<V> node) {
        return putObject(key, node);
    }

    private boolean putObject(long key, Object value) {
        short nodeIndex = (short)(key >>> shift & 255L);
        int containsIndex = binarySearch(keys, 0, count, nodeIndex);
        if (containsIndex >= 0) {
            this.nodes[containsIndex] = value;
            return true;
        } else if (!isFull()) {
            insert(nodeIndex, value);
            return true;
        }
        return false;
    }

    private void insert(short key, Object value) {
        int index = Math.abs(binarySearch(keys, 0, count, key) + 1);
        count++;
        int dest = index + 1;
        int length = count - dest;
        System.arraycopy(keys, index, keys, dest, length);
        System.arraycopy(nodes, index, nodes, dest, length);
        keys[index] = key;
        nodes[index] = value;
    }

    @Override
    public V remove(long key) {
        int index = binarySearch(keys,0,count, (short)(key >>> shift & 255L));
        if (index < 0) {
            return null;
        }
        V result = (V) nodes[index];
        int source = index + 1;
        int length = count - source;
        System.arraycopy(keys, source, keys, index, length);
        System.arraycopy(nodes, source, nodes, index, length);
        count--;

        return result;
    }

    @Override
    public String toString() {
        return "ArtNode16BinarySearch{" +
                "key=" + key +
                ", nodeLevelKey=" + toNodeLevelKeys(key) +
                ", depth=" + depth +
                ", count=" + count +
                ", keys=" + Arrays.toString(keys) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtNode16BinarySearch<?> that = (ArtNode16BinarySearch<?>) o;

        if (depth != that.depth) {
            return false;
        }
        return key == that.key;
    }

    @Override
    public int hashCode() {
        int result = depth;
        result = 31 * result + (int) (key ^ (key >>> 32));
        return result;
    }
}
