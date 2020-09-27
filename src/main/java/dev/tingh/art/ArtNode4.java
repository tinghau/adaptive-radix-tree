package dev.tingh.art;


import java.util.Arrays;

import static dev.tingh.art.Art.toNodeLevelKeys;
import static dev.tingh.art.ArtNodes.*;

public class ArtNode4<V> implements IArtNode<V> {

    protected final short[] keys = new short[4];
    protected final Object[] nodes = new Object[4];

    private final int depth;
    private final int shift;

    private final long key;

    private short count = 0;

    public ArtNode4(long key) {
        this(key, 0);
    }

    public ArtNode4(long key, int depth) {
        this.key = key;
        this.depth = depth;
        this.shift = depth * 8;
    }

    public ArtNode4(ArtNode16Linear<V> node) {
        this(node.getKey(), node.getDepth());

        for (int i=0; i < node.getCount(); i++) {
            this.keys[i] = node.keys[i];
            this.nodes[i] = node.nodes[i];
        }
        count = node.getCount();
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
    public ArtNodeType getNodeType() {
        return ArtNodeType.ArtNode4;
    }

    @Override
    public boolean canShrink() {
        return false;
    }

    @Override
    public boolean put(long key, V value) {
        return putObject(key, value);
    }

    private boolean putObject(long key, Object value) {
        short nodeIndex = (short) (key >>> shift & 255L);
        int index = contains(nodeIndex);

        if (index != -1) {
            this.nodes[index] = value;
            return true;
        } else if (!isFull()) {
            putObject(nodeIndex, value);
            return true;
        }
        return false;
    }

    private void putObject(short nodeIndex, Object value) {
        this.keys[count] = nodeIndex;
        this.nodes[count] = value;
        count++;
    }

    private int contains(short key) {
        for (int i = 0; i < count; i++) {
            if (keys[i] == key) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isFull() {
        return count == ArtNodeType.ArtNode4.getSize();
    }

    @Override
    public boolean put(long key, IArtNode<V> node) {
        return putObject(key, node);
    }

    @Override
    public Object get(long key) {
        if (!inNode(this, key)) {
            return null;
        }

        for (int i = 0; i < count; i++) {
            if (keys[i] == (key >>> shift & 255L)) {
                return nodes[i];
            }
        }
        return null;
    }

    @Override
    public Object getCeiling(long key) {
        if (!isNodeGreaterThanOrEqualTo(this, key)) {
            return null;
        }

        int index = -1;
        short best = 255;
        short nodeIndex = isNodeGreaterThan(this, key) ? 0 : (short) (key >>> shift & 255L);

        for (int i = 0; i < count; i++) {
            if (nodeIndex == keys[i]) {
                return nodes[i];
            } else if (nodeIndex < keys[i] && keys[i] <= best && isGreaterThan(nodes[i], key)) {
                index = i;
                best = keys[i];
            }
        }
        return index != -1 ? nodes[index] : null;
    }

    @Override
    public Object getFirst() {
        int index = -1;
        short best = 255;
        for (int i=0; i < count; i++) {
            if (keys[i] <= best) {
                index = i;
                best = keys[i];
            }
        }
        if (index == -1) {
            return null;
        }
        return nodes[index];
    }

    @Override
    public Object getFloor(long key) {
        if (!isNodeLessThanOrEqualTo(this, key)) {
            return null;
        }
        int index = -1;
        short best = 0;
        short nodeIndex = isNodeLessThan(this, key) ? 255 : (short) (key >>> shift & 255L);

        for (int i=0; i < count; i++) {
            if (nodeIndex == keys[i]) {
                return nodes[i];
            } else if (keys[i] < nodeIndex && keys[i] >= best && isLessThan(nodes[i], key )) {
                index = i;
                best = keys[i];
            }
        }
        return index != -1 ? nodes[index] : null;
    }

    @Override
    public Object getLast() {
        int index = -1;
        short best = 0;

        for (int i=0; i<count; i++) {
            if (keys[i] >= best) {
                index = i;
                best = keys[i];
            }
         }
        if (index == -1) {
            return null;
        }
        return nodes[index];
    }

    @Override
    public V remove(long key) {
        short index = (short) (key >>> shift & 255L);

        V result = null;

        for (int i=0; i < count; i++) {
            if (keys[i] == index) {
                result = doRemove(i);
                break;
            }
        }
        return result;
    }

    private V doRemove(int i) {
        V result = (V)nodes[i];
        int limit = count - 1;
        for (int j = i; j < limit; j++) {
            keys[j] = keys[j+1];
            nodes[j] = nodes[j+1];
        }
        count--;
        return result;
    }

    @Override
    public String toString() {
        return "ArtNode4{" +
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

        ArtNode4<?> artNode4 = (ArtNode4<?>) o;

        if (depth != artNode4.depth) {
            return false;
        }
        return key == artNode4.key;
    }

    @Override
    public int hashCode() {
        int result = depth;
        result = 31 * result + (int) (key ^ (key >>> 32));
        return result;
    }
}
