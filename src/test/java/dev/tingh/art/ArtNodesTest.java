package dev.tingh.art;

import org.junit.Test;

import static dev.tingh.art.ArtNodes.*;
import static org.junit.Assert.*;

public class ArtNodesTest {

    private static final long key1 = 72057594037927936L;
    private static final long key2 = 144_115_188_075_855_872L;

    private static final ArtNode4<String> node1 = new ArtNode4<>(key1);
    private static final ArtNode4<String> node2 = new ArtNode4<>(key2);

    private static final ArtNode4<String> rootNode = new ArtNode4<>(key1, 7);

    @Test
    public void testInNode() {
        assertTrue(inNode(node1, key1));
    }

    @Test
    public void testInNode_False() {
        assertFalse(inNode(node1, key2));
    }

    @Test
    public void testRootNode() {
        assertTrue(inNode(rootNode, 0));
    }

    @Test
    public void testIsNodeGreaterThanOrEqualTo_GreaterThan() {
        assertTrue(isNodeGreaterThanOrEqualTo(node2, key1));
    }

    @Test
    public void testIsNodeGreaterThanOrEqualTo_EqualTo() {
        assertTrue(isNodeGreaterThanOrEqualTo(node1, key1));
    }

    @Test
    public void testIsNodeGreaterThanOrEqualTo_LessThan() {
        assertFalse(isNodeGreaterThanOrEqualTo(node1, key2));
    }

    @Test
    public void testIsNodeGreaterThan_GreaterThan() {
        assertTrue(isNodeGreaterThan(node2, key1));
    }

    @Test
    public void testIsNodeGreaterThan_EqualTo() {
        assertFalse(isNodeGreaterThan(node1, key1));
    }

    @Test
    public void testIsNodeGreaterThan_LessThan() {
        assertFalse(isNodeGreaterThan(node1, key2));
    }

    @Test
    public void testIsGreaterThan_Value() {
        assertTrue(isGreaterThan("", key2));
    }

    @Test
    public void testIsGreaterThan_GreaterThan() {
        assertTrue(isGreaterThan(node2, key1));
    }

    @Test
    public void testIsGreaterThan_EqualTo() {
        assertFalse(isGreaterThan(node1, key1));
    }

    @Test
    public void testIsGreaterThan_LessThan() {
        assertFalse(isGreaterThan(node1, key2));
    }

    @Test
    public void testIsNodeLessThanOrEqualTo_LessThan() {
        assertTrue(isNodeLessThanOrEqualTo(node1, key1));
    }

    @Test
    public void testIsNodeLessThanOrEqualTo_EqualTo() {
        assertTrue(isNodeLessThanOrEqualTo(node1, key2));
    }

    @Test
    public void testIsNodeLessThanOrEqualTo_GreaterThan() {
        assertFalse(isNodeLessThanOrEqualTo(node2, key1));
    }

    @Test
    public void testIsNodeLessThan_LessThan() {
        assertTrue(ArtNodes.isNodeLessThan(node1, key2));
    }

    @Test
    public void testIsNodeLessThan_EqualTo() {
        assertFalse(ArtNodes.isNodeLessThan(node1, key1));
    }

    @Test
    public void testIsNodeLessThan_GreaterThan() {
        assertFalse(ArtNodes.isNodeLessThan(node2, key1));
    }

    @Test
    public void testIsLessThan_Value() {
        assertTrue(ArtNodes.isLessThan("", key2));
    }

    @Test
    public void testIsLessThan_LessThan() {
        assertTrue(ArtNodes.isLessThan(node1, key2));
    }

    @Test
    public void testIsLessThan_EqualTo() {
        assertFalse(ArtNodes.isLessThan(node1, key1));
    }

    @Test
    public void testIsLessThan_GreaterThan() {
        assertFalse(ArtNodes.isLessThan(node2, key1));
    }
}

