package dev.tingh.art;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class LongARTreeManyTest {

    private final Random random = new Random(190880900);

    private LongARTree<String> longARTree;

    @Before
    public void setup() {
        longARTree = new LongARTree<>();
    }

    @Test
    public void testPutGetMany() {
        long[] keys = new long[256000];
        for (int i=0; i<keys.length; i++) {
            long value = random.nextLong();
            keys[i] = value;
            longARTree.put(value, String.valueOf(value));
        }

        for (long key : keys) {
            assertEquals(String.valueOf(key), longARTree.get(key));
        }
    }
}
