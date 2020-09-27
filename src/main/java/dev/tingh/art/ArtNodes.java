package dev.tingh.art;

public class ArtNodes {

    static <V> boolean inNode(IArtNode<V> node, long key) {
        int depth = node.getShift() + 8;
        if (depth >= 64) {
            return true;
        }
        return (node.getKey() >>> depth ^ key >>> depth) == 0;
    }

    static <V> boolean isNodeGreaterThanOrEqualTo(IArtNode<V> node, long key) {
        int depth = node.getShift() + 8;
        if (depth >= 64) {
            return true;
        }
        return (node.getKey() >>> depth) >= (key >>> depth);
    }

    static <V> boolean isNodeGreaterThan(IArtNode<V> node, long key) {
        int depth = node.getShift() + 8;
        if (depth >= 64) {
            return false;
        }
        return (node.getKey() >>> depth) > (key >>> depth);
    }

    static <V> boolean isGreaterThan(Object value, long key) {
        if (!(value instanceof IArtNode)) {
            return true;
        }
        return ((IArtNode<V>)value).getKey() > key;
    }

    static <V> boolean isNodeLessThanOrEqualTo(IArtNode<V> node, long key) {
        int depth = node.getShift() + 8;
        if (depth >= 64) {
            return true;
        }
        return (node.getKey() >>> depth) <= (key >>> depth);
    }

    static <V> boolean isNodeLessThan(IArtNode<V> node, long key) {
        int depth = node.getShift() + 8;
        if (depth >= 64) {
            return false;
        }
        return (node.getKey() >>> depth) < (key >>> depth);
    }

    static <V> boolean isLessThan(Object value, long key) {
        if (!(value instanceof IArtNode)) {
            return true;
        }
        return ((IArtNode<V>)value).getKey() < key;
    }
}
