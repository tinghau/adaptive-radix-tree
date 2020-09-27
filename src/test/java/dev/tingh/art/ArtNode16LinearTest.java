package dev.tingh.art;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class ArtNode16LinearTest extends ArtNodeTest {

    @Override
    public IArtNode<String> getNode(long key) {
        return new ArtNode16Linear<>(key);
    }

    @Override
    public IArtNode<String> getNode(long key, int depth) {
        return new ArtNode16Linear<>(key, depth);
    }

    @Override
    public ArtNodeType getExpectedNodeType() {
        return ArtNodeType.ArtNode16;
    }

    @Test
    public void testArtNode16FromArtNode4() {
        ArtNode4<String> node4 = new ArtNode4<>(779682134L);
        IntStream.range(0, 4).forEach(i -> node4.put(779682134L+i, String.valueOf(i)));

        IArtNode<String> node16 = new ArtNode16Linear<>(node4);
        assertEquals("0", node16.get(779682134L));
        assertEquals("3", node16.get(779682137L));
        assertEquals(4, node16.getCount());
    }

    @Test
    public void testArtNode16FromArtNode48() {
        ArtNode48<String> node48 = new ArtNode48<>(779682134L);
        IntStream.range(0, 16).forEach(i -> node48.put(779682134L + i, String.valueOf(i)));

        IArtNode<String> node16 = new ArtNode16Linear<>(node48);
        assertEquals("0", node16.get(779682134L));
        assertEquals("15", node16.get(779682149L));
        assertEquals(16, node16.getCount());
    }
}
