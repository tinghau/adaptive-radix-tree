package dev.tingh.art;

import static dev.tingh.art.Art.toNodeLevelKeys;
import static dev.tingh.art.ArtNodes.*;

public class ArtNode48<V> implements IArtNode<V> {

    protected final short[] index = new short[256];
    protected final Object[] nodes = new Object[48];

    private final int depth;
    private final int shift;
    private final long key;

    private short count = 0;

    public ArtNode48(long key) {
        this(key, 0);
    }

    public ArtNode48(long key, int depth) {
        this.key = key;
        this.depth = depth;
        this.shift = depth * 8;

        for (int i = 0; i < 256; i++) {
            index[i] = -1;
        }
    }

    public ArtNode48(ArtNode16Linear<V> node) {
        this(node.getKey(), node.getDepth());

        this.count = node.getCount();

        for (short i=0; i<node.getCount(); i++) {
            this.index[node.keys[i]] = i;
            this.nodes[i] = node.nodes[i];
        }
    }

    public ArtNode48(ArtNode256<V> node) {
        this(node.getKey(), node.getDepth());

        for (short i=0; count < node.getCount(); i++) {
            if (node.nodes[i] != null) {
                this.index[i] = count;
                this.nodes[count] = node.nodes[i];
                count++;
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
        return count == 48;
    }

    @Override
    public ArtNodeType getNodeType() {
        return ArtNodeType.ArtNode48;
    }

    @Override
    public boolean canShrink() {
        return count <= 16;
    }

    @Override
    public Object get(long key) {
        if (!inNode(this, key)) {
            return null;
        }
        short nodeIndex = (short) (key >>> shift & 255L);
        if (index[nodeIndex] == -1) {
            return null;
        }
        return nodes[index[nodeIndex]];
    }

    @Override
    public Object getCeiling(long key) {
        if (!isNodeGreaterThanOrEqualTo(this, key)) {
            return null;
        }
        int nodeIndex = isNodeGreaterThan(this, key) ? 0 : (short) (key >>> shift & 255L);
        if (index[nodeIndex] != -1) {
            return nodes[index[nodeIndex]];
        } else {
            return scanForCeiling(nodeIndex);
        }
    }

    private Object scanForCeiling(int nodeIndex) {
        while (nodeIndex < 255) {
            nodeIndex++;
            if (index[nodeIndex] != -1) {
                return nodes[index[nodeIndex]];
            }
        }
        return null;
    }

    @Override
    public Object getFirst() {
        for (int i = 0; i < 256; i++) {
            if (index[i] != -1) {
                return nodes[index[i]];
            }
        }
        return null;
    }

    @Override
    public Object getFloor(long key) {
        if (!isNodeLessThanOrEqualTo(this, key)) {
            return null;
        }
        short nodeIndex =  (short) (key >>> shift & 255L);
        if (index[nodeIndex] != -1) {
            return nodes[index[nodeIndex]];
        } else {
            return scanForFloor(nodeIndex, key);
        }
    }

    private Object scanForFloor(short nodeIndex, long key) {
        while (nodeIndex > 0) {
            nodeIndex--;
            if (index[nodeIndex] != -1 && isLessThan(nodes[index[nodeIndex]], key)) {
                return  nodes[index[nodeIndex]];
            }
        }
        return null;
    }

    @Override
    public Object getLast() {
        for (int i = 255; i >= 0; i--) {
            if (index[i] != -1) {
                return nodes[index[i]];
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
        short keyIndex = (short) (key >>> shift & 255L);
        if (contains(keyIndex)) {
            nodes[index[keyIndex]] = value;
            return true;
        } else if (!isFull()) {
            putObject(keyIndex, value);
            return true;
        }
        return false;
    }

    private void putObject(short keyIndex, Object value) {
        short nodeIndex = getFreeIndex();
        index[keyIndex] = nodeIndex;
        nodes[nodeIndex] = value;
        count++;
    }

    private short getFreeIndex() {
        if (nodes[count] == null) {
            return count;
        } else {
            for (short i=0; i<48; i++) {
                if (nodes[i] == null) {
                    return i;
                }
            }
        }
        throw new IllegalStateException("Node is full");
    }

    private boolean contains(short key) {
        return index[key] >= 0;
    }

    @Override
    public V remove(long key) {
        V result = null;
        short nodeIndex = (short) (key >>> shift & 255L);

        if (contains(nodeIndex)) {
            int pos = index[nodeIndex];
            result = (V) nodes[pos];
            nodes[pos] = null;
            index[nodeIndex] = -1;
            count--;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ArtNode48{" +
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

        ArtNode48<?> artNode48 = (ArtNode48<?>) o;

        if (depth != artNode48.depth) {
            return false;
        }
        return key == artNode48.key;
    }

    @Override
    public int hashCode() {
        int result = depth;
        result = 31 * result + (int) (key ^ (key >>> 32));
        return result;
    }
}
