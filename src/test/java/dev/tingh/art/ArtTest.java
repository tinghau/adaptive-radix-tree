package dev.tingh.art;

import org.junit.Test;

import static dev.tingh.art.Art.toNodeLevelKeys;
import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;
import static org.junit.Assert.assertEquals;

public class ArtTest {

    @Test
    public void toNodeLevelKeys_MinValue() {
        assertEquals("128,0,0,0,0,0,0,0", toNodeLevelKeys(MIN_VALUE));
    }

    @Test
    public void toNodeLevelKeys_MaxValue() {
        assertEquals("127,255,255,255,255,255,255,255", toNodeLevelKeys(MAX_VALUE));
    }
}
