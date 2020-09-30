package dev.tingh.art;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class ArtNode256Test extends ArtNodeTest {

    @Override
    public IArtNode<String> getNode(long key) {
        return new ArtNode256<>(key);
    }

    @Override
    public IArtNode<String> getNode(long key, int depth) {
        return new ArtNode256<>(key, depth);
    }

    @Override
    public ArtNodeType getExpectedNodeType() {
        return ArtNodeType.ArtNode256;
    }

    @Test
    public void testArtNode256FromArtNode48() {
        ArtNode48<String> node48 = new ArtNode48<>(779682134);
        IntStream.range(0, 48).forEach(i -> node48.put(779682134+i, String.valueOf(i)));

        ArtNode256<String> node256 = new ArtNode256<>(node48);
        assertEquals("0", node256.get(779682134));
        assertEquals("47", node256.get(779682181));
    }

    @Override
    @Test
    public void testFullNodeRemoveThenPut() {
        // Not a valid test because once a 256 node is full, entries should populate the next node.
    }

    @Override
    @Test
    public void testPutInFullNode() {
        // Not a valid test because once a 256 node is full, entries should populate the next node.
    }

    @Override
    @Test
    public void testRemoveInvalidKeyFromFull() {
        // Not a valid test because once a 256 node is full, all keys become valid.
    }

    @Override
    @Test
    public void testIsFullTrue() {
        // Not a valid test because 256 is "never" full, it always accepts addtions.
    }

}
