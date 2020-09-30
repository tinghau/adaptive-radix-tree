package dev.tingh.art;

import java.util.Arrays;

import static dev.tingh.art.Art.toNodeLevelKeys;
import static dev.tingh.art.ArtNodes.*;

public class ArtNode16Linear<V> implements IArtNode<V> {

    protected final short[] keys = new short[16];
    protected final Object[] nodes = new Object[16];

    private final int depth;
    private final int shift;
    private final long key;

    protected short count = 0;

    public ArtNode16Linear(long key) {
        this(key, 0);
    }

    public ArtNode16Linear(long key, int depth) {
        this.key = key;
        this.depth = depth;
        this.shift = depth * 8;
    }

    public ArtNode16Linear(ArtNode4<V> node) {
        this(node.getKey(), node.getDepth());

        this.count = node.getCount();

        for (int i=0; i<node.getCount(); i++) {
            this.keys[i] = node.keys[i];
            this.nodes[i] = node.nodes[i];
        }
    }

    public ArtNode16Linear(ArtNode48<V> node) {
        this(node.getKey(), node.getDepth());
        int index = 0;

        for (short i=0; index < node.getCount(); i++) {
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
    public boolean put(long key, V value) {
        return putObject(key, value);
    }

    @Override
    public boolean put(long key, IArtNode<V> node) {
        return putObject(key, node);
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
    public Object get(long key) {
        if (!inNode(this, key)) {
            return null;
        }

        short index = (short)(key >>> shift & 255L);
        for (int i = 0; i < count; i++) {
            if (keys[i] == index) {
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
        return "ArtNode16Linear{" +
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

        ArtNode16Linear<?> that = (ArtNode16Linear<?>) o;

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
