package dev.tingh.art;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public abstract class ArtNodeTest {

    protected final long key1 = 779682034L;
    protected final long key2 = 779682035L;
    protected final long key3 = 779682036L;
    protected final long key4 = 779682037L;
    protected final long key5 = -5227030564933015453L;
    protected final long key6 = -5227154688779242538L;

    protected IArtNode<String> node;

    @Before
    public void setup() {
        node = getNode(779682034);
    }

    public abstract IArtNode<String> getNode(long key);

    public abstract IArtNode<String> getNode(long key, int depth);

    public abstract ArtNodeType getExpectedNodeType();

    @Test
    public void testIsFullFalse() {
        assertFalse(node.isFull());
    }

    @Test
    public void testIsFullTrue() {
        IntStream.range(0, node.getNodeType().getSize())
                .forEach(i -> node.put(779682048L+i, String.valueOf(i)));

        assertTrue(node.isFull());
    }

    @Test
    public void testGetNodeType() {
        assertEquals(getExpectedNodeType(), node.getNodeType());
    }

    @Test
    public void testPutValue() {
        node.put(key1, "hello");
        node.put(key2, "world");

        assertEquals("hello", node.get(key1));
        assertEquals("world", node.get(key2));
    }

    @Test
    public void testPutOverrideValue() {
        node.put(key1, "hello");
        node.put(key1, "hi");
        node.put(key2, "world");

        assertEquals("hi", node.get(key1));
        assertEquals("world", node.get(key2));
    }

    @Test
    public void testPutNode() {
        IArtNode<String> child = new ArtNode4<>(key1);
        node = getNode(key1);
        node.put(key1, child);

        assertEquals(child, node.get(key1));
    }

    @Test
    public void testPutOverrideNode() {
        IArtNode<String> child1 = getNode(key1);
        IArtNode<String> child2 = getNode(key1, 3);

        node = getNode(key1);
        node.put(key1, child1);
        node.put(key1, child2);

        assertEquals(child2, node.get(key1));
    }

    @Test
    public void testPutNode2() {
        IArtNode<String> child1 = getNode(key1);
        IArtNode<String> child2 = getNode(key2);
        node = getNode(key1);
        node.put(key1, child1);
        node.put(key2, child2);

        assertEquals(child1, node.get(key1));
        assertEquals(child2, node.get(key2));
    }

    @Test
    public void testFullNodeRemoveThenPut() {
        int size = node.getNodeType().getSize();

        IArtNode<String> node = getNode(779682134L);
        IntStream.range(0, size)
                    .forEach(i -> node.put(779682134L+i, String.valueOf(i)));
        node.remove(779682135L);
        node.put(779682134L + size, String.valueOf(size));

        assertEquals("0", node.get(779682134L));
        assertEquals(String.valueOf(size), node.get(779682134L + size));
    }

    @Test
    public void testOverridePutInFullNode() {
        IArtNode<String> node = getNode(779682048L);

        IntStream.range(0, node.getNodeType().getSize())
                .forEach(i -> node.put(779682048L+i, String.valueOf(i)));
        node.put(779682048L, "0b");

        assertEquals("0b", node.get(779682048L));
    }

    @Test
    public void testPutInFullNode() {
        IArtNode<String> node = getNode(779682048L);

        int size = node.getNodeType().getSize();

        IntStream.range(0, size)
                .forEach(i -> node.put(779682048L+i, String.valueOf(i)));

        assertFalse(node.put(779682048L + size, String.valueOf(size)));
    }

    @Test
    public void testGet() {
        node.put(key1, "hello");

        assertEquals("hello", node.get(key1));
    }

    @Test
    public void testGetNull() {
        assertNull(node.get(key5));
    }

    @Test
    public void testGetCeilingMatch() {
        node.put(key1, "hello");

        assertEquals("hello", node.getCeiling(key1));
    }

    @Test
    public void testGetCeiling() {
        node.put(key2, "hello");

        assertEquals("hello", node.getCeiling(key1));
    }

    @Test
    public void testGetCeilingNull() {
        node.put(key1, "hello");

        assertNull(node.getCeiling(key2));
    }

    @Test
    public void testGetCeiling1() {
        IArtNode<String> node = getNode(key5, 5);
        IArtNode<String> child1 = getNode(key5, "5227030564933015453");
        IArtNode<String> child2 = getNode(key6, "-5227154688779242538");

        node.put(key5, child1);
        node.put(key6, child2);

        assertEquals(child1, node.getCeiling(-5227030564933015514L));
    }

    @Test
    public void testGetCeiling2() {
        long key1 = 146703797262928205L;
        long key2 = 146752219992246589L;

        IArtNode<String> node = getNode(key1, 5);
        IArtNode<String> child = getNode(key1, "146703797262928205");

        node.put(key1, child);
        node.put(key2, getNode(key2, "146752219992246589"));

        assertEquals(child, node.getCeiling(145329408610086364L));
    }

    private IArtNode<String> getNode(long key, String value) {
        IArtNode<String> node = getNode(key);
        node.put(key, value);
        return node;
    }

    @Test
    public void testGetCeilingDifferentBranch() {
        IArtNode<String> node = getNode(key1, "hello");
        node.put(key1, "hello");

        assertNull(node.getCeiling(key5));
    }

    @Test
    public void testGetFirst() {
        node.put(key1, "hello");

        assertEquals("hello", node.getFirst());
    }

    @Test
    public void testGetFirstNull() {
        assertNull(node.getFirst());
    }

    @Test
    public void testGetFirst1() {
        IArtNode<String> node = getNode(key5, 5);
        IArtNode<String> child1 = getNode(key5, "5227030564933015453");
        IArtNode<String> child2 = getNode(key6, "-5227154688779242538");

        node.put(key5, child1);
        node.put(key6, child2);

        assertEquals(child2, node.getFirst());
    }

    @Test
    public void testGetFirstEdge() {
        node.put(779682303L, "hello");

        assertEquals("hello", node.getFirst());
    }

    @Test
    public void testGetFloorMatch() {
        node.put(key1, "hello");

        assertEquals("hello", node.getFloor(key1));
    }

    @Test
    public void testGetFloor() {
        node.put(key1, "hello");

        assertEquals("hello", node.getFloor(key2));
    }

    @Test
    public void testGetFloorNull() {
        node.put(key2, "hello");

        assertNull(node.getFloor(key1));
    }

    @Test
    public void testGetFloorDifferentBranch() {
        IArtNode<String> node = getNode(key5, "hello");
        node.put(key5, "hello");

        assertNull(node.getFloor(key1));
    }

    @Test
    public void testGetFloorDifferentBranch1() {
        long key1 = 4360118905724710640L;
        long key2 = 4360119749394743773L;

        IArtNode<String> node = getNode(key1, 4);
        node.put(key1, getNode(key1, "hello"));
        node.put(key2, getNode(key2, "world"));

        assertNull(node.getFloor(4360109573934252674L));
    }

    @Test
    public void testGetFloor1() {
        IArtNode<String> node = getNode(key5, 5);
        IArtNode<String> child1 = getNode(key5, "-5227030564933015453");
        IArtNode<String> child2 = getNode(key6, "-5227154688779242538");

        node.put(key5, child1);
        node.put(key6, child2);

        assertEquals(child2, node.getFloor(-5227154688779242539L));
    }

    @Test
    public void testGetFloor2() {
        long key1 = 2096929955676072019L;
        long key2 = 2095516849049516633L;
        long key3 = 2101757704529877292L;

        IArtNode<String> node = getNode(key1, 6);

        IArtNode<String> child = getNode(key2, "2095516849049516633");

        node.put(key1, getNode(key1, "2096929955676072019"));
        node.put(key2, child);
        node.put(key3, getNode(key3, "2101757704529877292"));

        assertEquals(child, node.getFloor(2095516849049516634L));
    }

    @Test
    public void testGetFloor3() {
        long key1 = 4620902174120611888L;
        long key2 = 4620693839979927856L;

        IArtNode<String> node = getNode(key1, 5);
        IArtNode<String> child = getNode(key2, "4620693839979927856");

        node.put(key1, getNode(key1, "4620902174120611888"));
        node.put(key2, child);

        assertEquals(child, node.getFloor(4620693839979927857L));
    }

    @Test
    public void testGetLast() {
        node.put(key1, "hello");

        assertEquals("hello", node.getLast());
    }

    @Test
    public void testGetLast1() {
        IArtNode<String> node = getNode(key5, 5);
        IArtNode<String> child1 = getNode(key5, "5227030564933015453");
        IArtNode<String> child2 = getNode(key6, "-5227154688779242538");

        node.put(key5, child1);
        node.put(key6, child2);

        assertEquals(child1, node.getLast());
    }

    @Test
    public void testGetLastNull() {
        assertNull(node.getLast());
    }

    @Test
    public void testGetLastEdge() {
        node.put(779682048L, "hello");

        assertEquals("hello", node.getLast());
    }

    @Test
    public void testRemove() {
        node.put(key1, "hello");
        node.put(key2, "world");

        assertEquals("hello", node.remove(key1));
        assertNull(node.get(key1));
        assertEquals("world", node.get(key2));
    }

    @Test
    public void testRemove1() {
        node.put(key1, "hello");
        node.put(key2, "world");

        assertEquals("hello", node.remove(key1));
        assertNull(node.get(key1));
    }

    @Test
    public void testRemoveFirstKeyAndRetrieveLatest() {
        node.put(key1, "hello");
        node.put(key2, "world");
        node.put(key3, "hi");
        node.put(key4, "there");

        node.remove(key2);
        assertEquals("hello", node.get(key1));
        assertEquals("hi", node.get(key3));
        assertEquals("there", node.get(key4));
    }

    @Test
    public void removeInvalidKey() {
        assertNull(node.remove(key1));
    }

    @Test
    public void testRemoveInvalidKeyFromFull() {
        IntStream.range(0, node.getNodeType().getSize())
                .forEach(i -> node.put(key1+i, String.valueOf(i)) );
        assertNull(node.remove(key5));
    }

    @Test
    public void testRemoveFromFull() {
        IntStream.range(0, node.getNodeType().getSize())
                .forEach(i -> node.put(779682048L+i, String.valueOf(i)));

        assertEquals("0", node.remove(779682048L));
        assertNull(node.get(779682048L));
    }

    @Test
    public void testCanShrink() {
        assertTrue(node.canShrink());
    }
}
