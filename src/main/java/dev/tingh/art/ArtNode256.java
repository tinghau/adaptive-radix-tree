package dev.tingh.art;

import static dev.tingh.art.Art.toNodeLevelKeys;
import static dev.tingh.art.ArtNodes.*;

public class ArtNode256<V> implements IArtNode<V> {

    protected final Object[] nodes = new Object[256];

    private final int depth;
    private final int shift;
    private final long key;

    private short count = 0;

    public ArtNode256(long key) {
        this(key, 0);
    }

    public ArtNode256(long key, int depth) {
        this.key = key;
        this.depth = depth;
        this.shift = depth * 8;
    }

    public ArtNode256(ArtNode48<V> node) {
        this(node.getKey(), node.getDepth());

        this.count = node.getCount();

        for (int i=0; i<256; i++) {
            if (node.index[i] != -1) {
                nodes[i] = node.nodes[node.index[i]];
            }
        }
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
        return false;
    }

    @Override
    public ArtNodeType getNodeType() {
        return ArtNodeType.ArtNode256;
    }

    @Override
    public boolean canShrink() {
        return count <= 48;
    }

    @Override
    public Object get(long key) {
        if (!inNode(this, key)) {
            return null;
        }
        return nodes[(short)(key >>> shift & 255L)];
    }

    @Override
    public Object getCeiling(long key) {
        if (!isNodeGreaterThanOrEqualTo(this, key)) {
            return null;
        }
        short nodeIndex = isNodeGreaterThan(this, key) ? 0 : (short)(key >>> shift & 255L);
        if (nodes[nodeIndex] != null) {
            return nodes[nodeIndex];
        } else {
            return scanForCeiling(key, nodeIndex);
        }
    }

    private Object scanForCeiling(long key, short nodeIndex) {
        while (nodeIndex < 255) {
            nodeIndex++;
            if (nodes[nodeIndex] != null && isGreaterThan(nodes[nodeIndex], key)) {
                return nodes[nodeIndex];
            }
        }
        return null;
    }

    @Override
    public Object getFirst() {
        for (int i=0; i < 256; i++) {
            if (nodes[i] != null) {
                return nodes[i];
            }
        }
        return null;
    }

    @Override
    public Object getFloor(long key) {
        if (!ArtNodes.isNodeLessThanOrEqualTo(this, key)) {
            return null;
        }
        short nodeIndex = (short)(key >>> shift & 255L);
        if (nodes[nodeIndex] != null) {
            return nodes[nodeIndex];
        } else {
            return scanForFloor(key, nodeIndex);
        }
    }

    private Object scanForFloor(long key, short nodeIndex) {
        while (nodeIndex > 0) {
            nodeIndex--;
            if (nodes[nodeIndex] != null && ArtNodes.isLessThan(nodes[nodeIndex], key)) {
                return nodes[nodeIndex];
            }
        }
        return null;
    }

    @Override
    public Object getLast() {
        for (int i=255; i >= 0; i--) {
            if (nodes[i] != null) {
                return nodes[i];
            }
        }
        return null;
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

        if (!contains(nodeIndex)) {
            count++;
        }
        nodes[nodeIndex] = value;
        return true;
    }

    private boolean contains(short index) {
        return nodes[index] != null;
    }

    @Override
    public V remove(long key) {
        short nodeIndex = (short)(key >>> shift & 255L);

        V result = null;
        if (contains(nodeIndex)) {
            result = (V)nodes[nodeIndex];
            nodes[nodeIndex] = null;
            count--;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ArtNode256{" +
                "key=" + key +
                ", nodeLevelKey=" + toNodeLevelKeys(key) +
                ", depth=" + depth +
                ", count=" + count +
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

        ArtNode256<?> that = (ArtNode256<?>) o;

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
