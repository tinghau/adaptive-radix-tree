package dev.tingh.art;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ArtNode4Test extends ArtNodeTest {

    @Override
    public IArtNode<String> getNode(long key) {
        return new ArtNode4<>(key);
    }

    @Override
    public IArtNode<String> getNode(long key, int depth) {
        return new ArtNode4<>(key, depth);
    }

    @Override
    public ArtNodeType getExpectedNodeType() {
        return ArtNodeType.ArtNode4;
    }

    @Test
    public void testArtNode4FromArtNode16Linear() {
        ArtNode16Linear<String> node16 = new ArtNode16Linear<>(779682134L);
        IntStream.range(0, 4).forEach(i -> node16.put(779682134L+i, String.valueOf(i)));

        ArtNode4<String> node4 = new ArtNode4<>(node16);
        assertEquals("0", node4.get(779682134L));
        assertEquals("3", node4.get(779682137L));
        assertEquals(4, node4.getCount());
    }

    @Test
    public void testArtNode4FromArtNode16BinarySearch() {
        ArtNode16BinarySearch<String> node16 = new ArtNode16BinarySearch<>(779682134L);
        IntStream.range(0, 4).forEach(i -> node16.put(779682134L+i, String.valueOf(i)));

        ArtNode4<String> node4 = new ArtNode4<>(node16);
        assertEquals("0", node4.get(779682134L));
        assertEquals("3", node4.get(779682137L));
        assertEquals(4, node4.getCount());
    }

    @Override
    @Test
    public void testCanShrink() {
        assertFalse(node.canShrink());
    }
}
