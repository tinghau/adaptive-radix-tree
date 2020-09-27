package dev.tingh.art;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class ArtNode48Test extends ArtNodeTest {

    @Override
    public IArtNode<String> getNode(long key) {
        return new ArtNode48<>(key);
    }

    @Override
    public IArtNode<String> getNode(long key, int depth) {
        return new ArtNode48<>(key, depth);
    }

    @Override
    public ArtNodeType getExpectedNodeType() {
        return ArtNodeType.ArtNode48;
    }

    @Test
    public void testArtNode48FromArtNode16() {
        ArtNode16Linear<String> node16 = new ArtNode16Linear<>(779682134);
        IntStream.range(0, 16).forEach(i -> node16.put(779682134L + i, String.valueOf(i)));

        ArtNode48<String> node48 = new ArtNode48<>(node16);
        assertEquals("0", node48.get(779682134L));
        assertEquals("15", node48.get(779682149L));
    }

    @Test
    public void testArtNode48FromArtNode256() {
        ArtNode256<String> node256 = new ArtNode256<>(779682134L);
        IntStream.range(0, 48).forEach(i -> node256.put(779682134L + i, String.valueOf(i)));

        ArtNode48<String> node48 = new ArtNode48<>(node256);
        assertEquals("0", node48.get(779682134L));
        assertEquals("47", node48.get(779682181L));
    }
}
