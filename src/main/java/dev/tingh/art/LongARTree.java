package dev.tingh.art;

import static dev.tingh.art.ArtNodes.inNode;

public class LongARTree<V> implements ILongARTree<V> {

    private IArtNode<V> root;

    @Override
    public V get(long key) {
        return get(root, key);
    }

    private V get(IArtNode<V> node, long key) {
        if (node == null) {
            return null;
        } else if (isLeaf(node)) {
            return (V) node.get(key);
        } else if (!inNode(node, key)) {
            return null;
        }
        IArtNode<V> next = (IArtNode<V>) node.get(key);
        return get(next, key);
    }

    @Override
    public boolean contains(long key) {
        return contains(root, key);
    }

    private boolean contains(IArtNode<V> node, long key) {
        if (node == null) {
            return false;
        } else if (isLeaf(node) && node.get(key) != null) {
            return true;
        } else if (targetDepth(node, key) > node.getDepth()) {
            return false;
        }
        IArtNode<V> next = (IArtNode<V>) node.get(key);
        return contains(next, key);
    }

    // Least key greater than or equal to the given key.
    @Override
    public V getCeiling(long key) {
        return getCeiling(root, key);
    }

    private V getCeiling(IArtNode<V> node, long key) {
        if (node == null) {
            return null;
        } else if (isLeaf(node)) {
            return (V) node.getCeiling(key);
        }
        IArtNode<V> next = (IArtNode<V>) node.getCeiling(key);
        if (next == null) {
            return null;
        }
        return doGetCeiling(next, node, key);
    }

    private V doGetCeiling(IArtNode<V> node, IArtNode<V> parent, long key) {
        if (isLeaf(node)) {
            return getCeilingOrFirst(node, parent, key);
        }

        IArtNode<V> next = getCeilingNext(node, key);
        if (next == null) {
            return searchAnotherBranchForCeiling(node, parent);
        }
        return doGetCeiling(next, node, key);
    }

    private V searchAnotherBranchForCeiling(IArtNode<V> node, IArtNode<V> parent) {
        long newKey = plusOne(node, parent);
        if (newKey == Long.MIN_VALUE) {
            return null;
        }
        if (searchCeilingBeyondOneParent(newKey, parent)) {
            return getCeiling(root, newKey);
        }
        return getCeilingValue(parent, newKey);
    }

    private V getCeilingValue(IArtNode<V> node, long key) {
        IArtNode<V> next = (IArtNode<V>) node.getCeiling(key);
        if (next == null) {
            return null;
        }
        return getFirstValue(next);
    }

    private V getFirstValue(IArtNode<V> node) {
        if (isLeaf(node)) {
            return (V) node.getFirst();
        }
        IArtNode<V> next = (IArtNode<V>) node.getFirst();
        return getFirstValue(next);
    }

    private IArtNode<V> getLastNode(IArtNode<V> node) {
        if (isLeaf(node)) {
            return node;
        }
        return getLastNode((IArtNode<V>)node.getLast());
    }

    private boolean searchCeilingBeyondOneParent(long newKey, IArtNode<V> parent) {
        return newKey > getLastNode(parent).getKey();
    }

    private IArtNode<V> getCeilingNext(IArtNode<V> node, long key) {
        if (ArtNodes.inNode(node, key)) {
            return (IArtNode<V>) node.getCeiling(key);
        } else if (node.getKey() >= key) {
            return (IArtNode<V>) node.getFirst();
        }
        return (IArtNode<V>) node.getCeiling(key);
    }

    private V getCeilingOrFirst(IArtNode<V> node, IArtNode<V> parent, long key) {
        if (inNode(node, key)) {
            return (V) node.getCeiling(key);
        } else if (node.getKey() >= key) {
            return (V) node.getFirst();
        }
        long newKey = plusOne(node, parent);
        if (searchCeilingBeyondOneParent(newKey, parent)) {
            return getCeiling(root, newKey);
        }
        return getCeilingValue(parent, newKey);
    }

    private long plusOne(IArtNode<V> node, IArtNode<V> parent) {
        if (node.equals(parent.getLast())) {
            long index = (node.getKey() >>> parent.getShift());
            index++;

            return index << parent.getShift();
        }

        boolean flipped = false;
        long newKey = node.getKey() >>> parent.getShift();
        for (int i = parent.getDepth(); i > 0; i--) {
            if (!flipped && node.getKey() >>> (i*8) != 255) {
                newKey++;
                flipped = true;
            }
            newKey = newKey << 8;
        }
        return newKey;
    }

    @Override
    public V getFloor(long key) {
        return getFloor(root, key);
    }

    private V getFloor(IArtNode<V> node, long key) {
        if (node == null) {
            return null;
        } else if (isLeaf(node)) {
            return (V)node.getFloor(key);
        }
        IArtNode<V> next = (IArtNode<V>) node.getFloor(key);
        if (next == null) {
            return null;
        }
        return doGetFloor(next, node, key);
    }

    // At leaf, return floor or sibling last.
    // Otherwise, iterate to the leaf node.
    private V doGetFloor(IArtNode<V> node, IArtNode<V> parent, long key) {
        if (isLeaf(node)) {
            return getFloorOrLast(node, parent, key);
        }
        IArtNode<V> next = getFloorNext(node, key);
        // Look to sibling
        if (next == null) {
            return searchAnotherBranchForFloor(node, parent);
        }
        return doGetFloor(next, node, key);
    }

    private V searchAnotherBranchForFloor(IArtNode<V> node, IArtNode<V> parent) {
        long newKey = minusOne(node, parent);
        if (newKey == Long.MAX_VALUE) {
            return null;
        }
        if (searchFloorBeyondOneParent( newKey, parent)) {
            return getFloor(root, newKey);
        }
        return getFloorValue(parent,newKey);
    }

    private boolean searchFloorBeyondOneParent(long key, IArtNode<V> parent) {
        return key < getFirstNode(parent).getKey();
    }

    private IArtNode<V> getFirstNode(IArtNode<V> node) {
        if (isLeaf(node)) {
            return node;
        }
        return getFirstNode((IArtNode<V>)node.getFirst());
    }

    private IArtNode<V> getFloorNext(IArtNode<V> node, long key) {
        if (inNode(node, key)) {
            return (IArtNode<V>) node.getFloor(key);
        } else if (node.getKey() <= key) {
            return (IArtNode<V>) node.getLast();
        }
        return (IArtNode<V>) node.getFloor(key);
    }

    // Gets value or looks at sibling.
    private V getFloorOrLast(IArtNode<V> node, IArtNode<V> parent, long key) {
        if (inNode(node, key)) {
            return (V)node.getFloor(key);
        } else if (node.getKey() <= key) {
            return (V)node.getLast();
        }

        long newKey = minusOne(node, parent);
        if (searchFloorBeyondOneParent(newKey, parent)) {
            return getFloor(root, newKey);
        }
        return getFloorValue(parent, newKey);
    }

    private V getFloorValue(IArtNode<V> node, long key) {
        IArtNode<V> next = (IArtNode<V>) node.getFloor(key);
        if (next == null) {
            return null;
        }
        return getLastValue(next);
    }

    private V getLastValue(IArtNode<V> node) {
        if (isLeaf(node)) {
            return (V) node.getLast();
        }
        IArtNode<V> next = (IArtNode<V>) node.getLast();
        return getLastValue(next);
    }

    private long minusOne(IArtNode<V> node, IArtNode<V> parent) {
        long minusOne = node.getKey() >>> ((parent.getShift()));
        minusOne = minusOne << parent.getShift();
        minusOne--;

        return minusOne;
    }

    @Override
    public void put(long key, V leaf) {
        put(root, key, leaf);
    }

    private void put(IArtNode<V> node, long key, V leaf) {
        if (node == null) {
            setRoot(key, leaf);
        } else if (isLeaf(node)) {
            doPut(node, key, leaf);
        } else {
            doPut(node, key, leaf, node.get(key));
        }
    }

    private void doPut(IArtNode<V> node, long key, V leaf) {
        if (inNode(node, key)) {
            if (node.isFull()) {
                IArtNode<V> newNode = grow(node);
                node = replace(node, newNode);
            }
            node.put(key, leaf);
        } else {
            doPutLeafNode(key, node, leaf);
        }
    }

    private void doPutLeafNode(long key, IArtNode<V> node, V leaf) {
        int targetDepth = targetDepth(node, key);
        IArtNode<V> leafNode = new ArtNode4<>(key);
        leafNode.put(key, leaf);

        if (targetDepth != 7) {
            doPut(key, targetDepth, node, leafNode);
        } else {
            doPutRoot(key, targetDepth, node, leafNode);
        }
    }

    private void doPut(long key, int targetDepth, IArtNode<V> node, IArtNode<V> leafNode) {
        if (node.getDepth() == targetDepth) {
            node.put(key, leafNode);
        } else {
            branch(key, targetDepth, node, leafNode);
        }
    }

    private void doPut(IArtNode<V> node, long key, V leaf, Object next) {
        if (next instanceof IArtNode) {
            put((IArtNode<V>) next, key, leaf);
        } else {
            if (node.isFull()) {
                IArtNode<V> newNode = grow(node);
                node = replace(node, newNode);
            }
            doPutLeafNode(key, node, leaf);
        }
    }

    private void doPutRoot(long key, int targetDepth, IArtNode<V> node, IArtNode<V> leafNode) {
        if (root.getDepth() == 7) {
            root.put(key, leafNode);
        } else if (!node.isFull()) {
            IArtNode<V> parent = new ArtNode4<>(key, targetDepth);
            parent.put(key, leafNode);
            parent.put(node.getKey(), node);
            root = parent;
        }
    }

    private void branch(long key, int targetDepth, IArtNode<V> node, IArtNode<V> leafNode) {
        IArtNode<V> newNode = new ArtNode4<>(key, targetDepth);
        newNode.put(key, leafNode);
        newNode.put(node.getKey(), node);
        replace(node, newNode);
    }

    // Return the depth of the match
    private int targetDepth(IArtNode<V> node, long key) {
        long compare = node.getKey() ^ key;
        int depth = 7;

        for (int i =7; i>=0; i--) {
            if ((compare & -72057594037927936L) == 0) {
                depth--;
            } else {
                return depth;
            }
            compare = compare << 8;
        }
        return 0;
    }

    private IArtNode<V> replace(IArtNode<V> node, IArtNode<V> newNode) {
        if (node == root) {
            root = newNode;
        } else {
            IArtNode<V> parent = getParent(node.getKey(), node.getDepth() + 1);
            parent.remove(node.getKey());
            parent.put(newNode.getKey(), newNode);
        }
        return newNode;
    }

    @Override
    public IArtNode<V> getParent(long key, int parentDepth) {
        return getParent(root, key, parentDepth);
    }

    private IArtNode<V> getParent(IArtNode<V> node, long key, int parentDepth) {
        if (node == null) {
            return null;
        } else if (node.getDepth() == parentDepth) {
            return node;
        } else if (node.getDepth() < parentDepth) {
            return null;
        }
        IArtNode<V> next = (IArtNode<V>) node.get(key);
        if (next.getDepth() < parentDepth) {
            return node;
        }
        return getParent(next, key, parentDepth);
    }

    private IArtNode<V> grow(IArtNode<V> node) {
        switch (node.getNodeType()) {
            case ArtNode4:
                return new ArtNode16Linear<>((ArtNode4<V>) node);
            case ArtNode16:
                return new ArtNode48<>((ArtNode16Linear<V>) node);
            case ArtNode48:
                return new ArtNode256<>((ArtNode48<V>) node);
        }
        throw new IllegalStateException("Unhandled nodeType=" + node.getNodeType());
    }

    private boolean isLeaf(IArtNode<V> node) {
        return node.getDepth() == 0;
    }

    private void setRoot(long key, V value) {
        IArtNode<V> node = new ArtNode4<>(key);
        node.put(key, value);

        root = node;
    }

    @Override
    public V remove(long key) {
        return remove(root, key);
    }

    private V remove(IArtNode<V> node, long key) {
        V result = null;

        if (node != null) {
            if (isLeaf(node) && inNode(node, key)) {
                result = node.remove(key);
                shrinkAndCompress(node);
            } else {
                Object next = node.get(key);
                if (next instanceof IArtNode) {
                    result = remove((IArtNode<V>) next, key);
                }
            }
        }
        return result;
    }

    private void shrinkAndCompress(IArtNode<V> node) {
        if (node.canShrink()) {
            IArtNode<V> newNode = shrink(node);
            replace(node, newNode);
        }
        compress(node);
    }

    private void compress(IArtNode<V> node) {
        if (node.getCount() == 0) {
            if (node == root) {
                root = null;
            } else {
                IArtNode<V> parent = getParent(node.getKey(), node.getDepth() + 1);
                parent.remove(node.getKey());

                if (parent.getCount() == 1) {
                    ArtNode4<V> node4 = (ArtNode4<V>) parent;
                    replace(parent, (IArtNode<V>) node4.nodes[0]);
                }
                shrinkAndCompress(parent);
            }
        }
    }

    private IArtNode<V> shrink(IArtNode<V> node) {
        switch (node.getNodeType()) {
            case ArtNode16:
                return new ArtNode4<>((ArtNode16Linear<V>) node);
            case ArtNode48:
                return new ArtNode16Linear<>((ArtNode48<V>) node);
            case ArtNode256:
                return new ArtNode48<>((ArtNode256<V>) node);
        }
        throw new IllegalStateException("Unhandled nodeType=" + node.getNodeType());
    }
}
