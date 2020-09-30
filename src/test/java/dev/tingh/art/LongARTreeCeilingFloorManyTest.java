package dev.tingh.art;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static dev.tingh.art.Art.toNodeLevelKeys;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LongARTreeCeilingFloorManyTest {

    private static final Logger logger = LoggerFactory.getLogger(LongARTreeCeilingFloorManyTest.class);

    private final LongARTree<String> longARTree = new LongARTree<>();
    private final TreeMap<Long, String> treeMap = new TreeMap<>();

    private final long[] keys = new long[256000];

    public LongARTreeCeilingFloorManyTest() {
        Random random = new Random(190880900);

        boolean seen = false;

        for (int i = 0; i < 20000; i++) {
            long value =  random.nextLong();

            longARTree.put(value, String.valueOf(value));
            treeMap.put(value, String.valueOf(value));
        }

        for (int i = 0; i < keys.length; i++) {
            keys[i] = random.nextLong();
        }
    }

    @Test
    public void testGetFloorMany() {
        for (long key : keys) {
            Map.Entry<Long, String> entry = treeMap.floorEntry(key);
            if (entry != null) {
                assertEquals(treeMap.floorEntry(key).getValue(), longARTree.getFloor(key));
            } else {
                assertNull(longARTree.getFloor(key));
            }
        }
    }

    @Test
    public void testGetCeilingMany() {
        for (long key : keys) {
            Map.Entry<Long, String> entry = treeMap.ceilingEntry(key);
            if (entry != null) {
                assertEquals(treeMap.ceilingEntry(key).getValue(), longARTree.getCeiling(key));
            } else {
                assertNull(longARTree.getCeiling(key));
            }
        }
    }

    @Test
    public void testGetCeilingOnDifferentBranch1() {
        logger.info("Test: testGetCeilingOnDifferentBranch1()");
        logger.info("Key: " + toNodeLevelKeys(145329408610086364L));
        logger.info("Expected: " + toNodeLevelKeys(146703797262928205L));

        assertEquals("146703797262928205", longARTree.getCeiling(145329408610086364L));
    }

    @Test
    public void testGetCeilingOnDifferentBranch2() {
        logger.info("Test: testGetCeilingOnDifferentBranch2()");
        logger.info("Key: " + toNodeLevelKeys(3594187510015345932L));
        logger.info("Expected: " + toNodeLevelKeys(3595446722934323407L));

        assertEquals("3595446722934323407", longARTree.getCeiling(3594187510015345932L));
    }

    @Test
    public void testGetCeilingOnDifferentBranch3() {
        logger.info("Test: testGetCeilingOnDifferentBranch3()");
        logger.info("Key: " + toNodeLevelKeys(720506699570727447L));
        logger.info("Expected: " + toNodeLevelKeys(721036075353989980L));

        assertEquals("721036075353989980", longARTree.getCeiling(720506699570727447L));
    }

    @Test
    public void testGetCeilingOnDifferentBranch4() {
        logger.info("Test: testGetCeilingOnDifferentBranch4()");
        logger.info("Key: " + toNodeLevelKeys(6124547560889179860L));
        logger.info("Expected: " + toNodeLevelKeys(6124569034291338189L));

        assertEquals("6124569034291338189", longARTree.getCeiling(6124547560889179860L));
    }

    @Test
    public void testGetCeilingOnDifferentBranch5() {
        logger.info("Test: testGetCeilingOnDifferentBranch5()");
        logger.info("Key: " + toNodeLevelKeys(-7367773660225373991L));
        logger.info("Expected: " + toNodeLevelKeys(-7364328736081872457L));

        assertEquals("-7364328736081872457", longARTree.getCeiling(-7367773660225373991L));
    }


    @Test
    public void testGetCeilingOnDifferentBranch6() {
        logger.info("Test: testGetCeilingOnDifferentBranch6()");
        logger.info("Key: " + toNodeLevelKeys(5230545182099773384L));
        logger.info("Expected: " + toNodeLevelKeys(5230751531218668063L));

        assertEquals("5230751531218668063", longARTree.getCeiling(5230545182099773384L));
    }

    @Test
    public void testGetCeilingOnDifferentBranch7() {
        logger.info("Test: testGetCeilingOnDifferentBranch7()");
        logger.info("Key: " + toNodeLevelKeys(5230545182099773384L));
        logger.info("Expected: " + toNodeLevelKeys(5230751531218668063L));

        assertEquals("5230751531218668063", longARTree.getCeiling(5230545182099773384L));
    }

    @Test
    public void testGetCeilingOnDifferentBranch8() {
        logger.info("Test: testGetCeilingOnDifferentBranch8()");
        logger.info("Key: " + toNodeLevelKeys(9223371440841164470L));

        assertNull(longARTree.getCeiling(9223371440841164470L));
    }

    @Test
    public void testGetCeilingOnDifferentBranch9() {
        logger.info("Test: testGetCeilingOnDifferentBranch9()");
        logger.info("Key: " + toNodeLevelKeys(-2145650666781104482L));
        logger.info("Expected: " + toNodeLevelKeys(-2143058402134791360L));

        assertEquals("-2143058402134791360", longARTree.getCeiling(-2145650666781104482L));
    }

    @Test
    public void testGetFloorOnDifferentBranch1() {
        logger.info("Test: testGetFloorOnDifferentBranch1()");
        logger.info("Key: " + toNodeLevelKeys(145329408610086364L));
        logger.info("Expected: " + toNodeLevelKeys(5230751531218668063L));

        assertEquals("144035381730917841", longARTree.getFloor(145329408610086364L));
    }

    @Test
    public void testGetFloorOnDifferentBranch2() {
        logger.info("Test: testGetFloorOnDifferentBranch2()");
        logger.info("Key: " + toNodeLevelKeys(-4978606753338289410L));
        logger.info("Expected: " + toNodeLevelKeys(-4979866180961112492L));

        assertEquals("-4979866180961112492", longARTree.getFloor(-4978606753338289410L));
    }

    @Test
    public void testGetFloorOnDifferentBranch3() {
        logger.info("Test: testGetFloorOnDifferentBranch3()");
        logger.info("Key: " + toNodeLevelKeys(9007202972680176047L));
        logger.info("Expected: " + toNodeLevelKeys(9005259179523620862L));

        assertEquals("9005259179523620862", longARTree.getFloor(9007202972680176047L));
    }

    @Test
    public void testGetFloorOnDifferentBranch4() {
        logger.info("Test: testGetFloorOnDifferentBranch4()");
        logger.info("Key: " + toNodeLevelKeys(5981637823141629186L));
        logger.info("Expected: " + toNodeLevelKeys(5979987638416015645L));

        assertEquals("5979987638416015645", longARTree.getFloor(5981637823141629186L));
    }

    @Test
    public void testGetFloorOnDifferentBranch5() {
        logger.info("Test: testGetFloorOnDifferentBranch5()");
        logger.info("Key: " + toNodeLevelKeys(4620838164985894110L));
        logger.info("Expected: " + toNodeLevelKeys(4620693839979927856L));

        assertEquals("4620693839979927856", longARTree.getFloor(4620838164985894110L));
    }

    @Test
    public void testGetFloorOnDifferentBranch6() {
        logger.info("Test: testGetFloorOnDifferentBranch6()");
        logger.info("Key: " + toNodeLevelKeys(-1729062466449968949L));
        logger.info("Expected: " + toNodeLevelKeys(-1729489945422221396L));

        assertEquals("-1729489945422221396", longARTree.getFloor(-1729062466449968949L));
    }

    @Test
    public void testGetFloorOnDifferentBranch7() {
        logger.info("Test: testGetFloorOnDifferentBranch7()");
        logger.info("Key: " + toNodeLevelKeys(-9223079162961865399L));

        assertNull(longARTree.getFloor(-9223079162961865399L));
    }

    @Test
    public void testGetFloorOnDifferentBranch8() {
        logger.info("Test: testGetFloorOnDifferentBranch8()");
        logger.info("Key: " + toNodeLevelKeys(4360109573934252674L));
        logger.info("Expected: " + toNodeLevelKeys(4357919072591074581L));

        assertEquals("4357919072591074581", longARTree.getFloor(4360109573934252674L));
    }

    @Test
    public void testGetFloorOnDifferentBranch9() {
        logger.info("Test: testGetFloorOnDifferentBranch9()");
        logger.info("Key: " + toNodeLevelKeys(447345935810439L));
        logger.info("Expected: " + toNodeLevelKeys(-865564111507549L));

        assertEquals("-865564111507549", longARTree.getFloor(447345935810439L));
    }

    @Test
    public void testGetFloorOnDifferentBranch10() {
        logger.info("Test: testGetFloorOnDifferentBranch10()");
        logger.info("Key: " + toNodeLevelKeys(8437564901346902137L));
        logger.info("Expected: " + toNodeLevelKeys(8435317477894472833L));

        assertEquals("8435317477894472833", longARTree.getFloor(8437564901346902137L));
    }

    @Test
    public void testGetFloorOnDifferentBranch11() {
        logger.info("Test: testGetFloorOnDifferentBranch11()");
        logger.info("Key: " + toNodeLevelKeys(2903924008896374692L));
        logger.info("Expected: " + toNodeLevelKeys(2903509749744339588L));

        assertEquals("2903509749744339588", longARTree.getFloor(2903924008896374692L));
    }
}
