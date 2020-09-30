package dev.tingh.art;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LongARTreeManyTest {

    private static final Logger logger = LoggerFactory.getLogger(LongARTreeManyTest.class);

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(LongARTreeManyTest.class);
        for (Failure failure : result.getFailures()) {
            logger.info(failure.toString());
        }
    }

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

    @Test
    public void testRemoveMany() {
        Set<Long> keys = new HashSet<>();
        for (int i=0; i<512000; i++) {
            long value = random.nextLong();
            keys.add(value);
            longARTree.put(value, String.valueOf(value));
        }

        for (long key : keys) {
            assertEquals(String.valueOf(key), longARTree.remove(key));
        }
    }
}
