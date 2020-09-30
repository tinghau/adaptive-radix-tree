package dev.tingh.art;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class LongARTreeTest {

    private final Random random = new Random(190880900);

    private static final long key1 = 779682134;            // [0, 0, 0, 0, 46, 121, 1, 86]
    private static final long key2 = 779682135;            // [0, 0, 0, 0, 46, 121, 1, 87]
    private static final long key3 = 779682136;            // [0, 0, 0, 0, 46, 121, 1, 88]
    private static final long key4 = 779682137;            // [0, 0, 0, 0, 46, 121, 1, 89]
    private static final long key5 = 779682138;            // [0, 0, 0, 0, 46, 121, 1, 90]
    private static final long key6 = 779682635;            // [0, 0, 0, 0, 46, 121, 3, 75]
    private static final long key7 = 779748171;            // [0, 0, 0, 0, 46, 122, 3, 75]
    private static final long key8 = 779748172;            // [0, 0, 0, 0, 46, 122, 3, 76]
    private static final long key9 = 100000000779682635L;  // [1, 99, 69, 120, 140, 3, 3, 75]

    private LongARTree<String> longARTree;

    @Before
    public void setup() {
        longARTree = new LongARTree<>();
    }

    @Test
    public void testPutRoot() {
        longARTree.put(key1, "hello");

        assertEquals("hello", longARTree.get(key1));
    }

    @Test
    public void testPut2Leaves() {
        longARTree.put(key1, "hello");
        longARTree.put(key2, "world");

        assertEquals("hello", longARTree.get(key1));
        assertEquals("world", longARTree.get(key2));
    }

    @Test
    public void testPutBranchingRootNode() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");

        assertEquals("hello", longARTree.get(key1));
        assertEquals("today", longARTree.get(key6));
    }

    @Test
    public void testPutBranchingNode() {
        longARTree.put(key5, "hello");
        longARTree.put(key7, "today");
        longARTree.put(key6, "world");

        assertEquals("hello", longARTree.get(key5));
        assertEquals("today", longARTree.get(key7));
        assertEquals("world", longARTree.get(key6));
    }

    @Test
    public void testPutRecursion() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");
        longARTree.put(key2, "world");

        assertEquals("hello", longARTree.get(key1));
        assertEquals("world", longARTree.get(key2));
        assertEquals("today", longARTree.get(key6));
    }

    @Test
    public void testPutGrow4() {
        longARTree.put(key1, "hello");
        longARTree.put(key2, "this");
        longARTree.put(key3, "big");
        longARTree.put(key4, "small");
        longARTree.put(key5, "world");

        assertEquals("world", longARTree.get(key5));
    }

    @Test
    public void testPutGrow16() {
        IntStream.range(0,17).forEach(i -> longARTree.put(779748096+i, String.valueOf(i)));

        assertEquals("16", longARTree.get(779748112));
    }

    @Test
    public void testPutGrow48() {
        IntStream.range(0,49).forEach(i -> longARTree.put(779748096+i, String.valueOf(i)));

        assertEquals("48", longARTree.get(779748144));
    }

    @Test
    public void testPutGrow256() {
        IntStream.range(0,256).forEach(i -> longARTree.put(779748096+i, String.valueOf(i)));
        longARTree.put(779748096+256, "256");

        assertEquals("256", longARTree.get(779748352));
    }

    @Test
    public void testPutKeysNoOverlap() {
        long[] keys = new long[]{-3796856330788593517L,   // [-53, 78, -37, 72, -92, 39, 32, -109]
                7397428503926138848L,   // [102, -88, -14, 7, -13, -35, -25, -32]
                5732725237514711066L,   // [79, -114, -69, 58, -16, -5, 48, 26]
                2286083675838776185L};  // [31, -71, -52, -3, -79, 48, -125, 121]

        Arrays.stream(keys).forEach(k -> longARTree.put(k, String.valueOf(k)));
        Arrays.stream(keys).forEach(k -> assertEquals(String.valueOf(k), longARTree.get(k)));
    }

    @Test
    public void testPutOnExistingRootBranch() {
        long[] keys = new long[8];
        for (int i=0; i<8; i++) {
            long value = random.nextLong();
            keys[i] = value;
            longARTree.put(value, String.valueOf(value));
        }
        longARTree.put(5870556280492264221L, String.valueOf(5870556280492264221L));

        assertEquals(String.valueOf(keys[7]), longARTree.get(keys[7]));
    }

    @Test
    public void testPutOnBranch() {
        long[] keys = new long[] {8503900492110308206L,
                8521293762703976736L,
                8503060225792475856L};

        Arrays.stream(keys).forEach(k -> longARTree.put(k, String.valueOf(k)));
        Arrays.stream(keys).forEach(k -> assertEquals(String.valueOf(k), longARTree.get(k)));
    }

    @Test
    public void testPutChildUncompress() {
        long[] keys = new long[] {-2687274112891598523L,
                -2687300028379518695L,
                -2682218877695518820L};

        Arrays.stream(keys).forEach(k -> longARTree.put(k, String.valueOf(k)));
        Arrays.stream(keys).forEach(k -> assertEquals(String.valueOf(k), longARTree.get(k)));
    }

    @Test
    public void testRemove() {
        longARTree.put(key1, "hello");
        longARTree.remove(key1);

        assertNull(longARTree.get(key1));
    }

    @Test
    public void testRemoveAfterInsert2Leaves() {
        longARTree.put(key1, "hello");
        longARTree.put(key2, "world");
        longARTree.remove(key1);

        assertNull(longARTree.get(key1));
    }

    @Test
    public void testRemoveAfterInsertBranchingRootNode() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");
        longARTree.remove(key1);

        assertNull(longARTree.get(key1));
    }

    @Test
    public void testRemoveAfterInsertBranchingNode() {
        longARTree.put(key5, "hello");
        longARTree.put(key7, "today");
        longARTree.put(key6, "world");
        longARTree.remove(key5);

        assertNull(longARTree.get(key5));
    }

    @Test
    public void testRemoveAfterInsertRecursion() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");
        longARTree.put(key2, "world");
        longARTree.remove(key1);

        assertNull(longARTree.get(key1));
    }

    @Test
    public void testRemoveShrink4() {
        longARTree.put(key1, "hello");
        longARTree.put(key2, "this");
        longARTree.put(key3, "big");
        longARTree.put(key4, "small");
        longARTree.put(key5, "world");
        longARTree.remove(key1);

        assertNull(longARTree.get(key1));
    }

    @Test
    public void testRemoveShrink16() {
        IntStream.range(0,17).forEach(i -> longARTree.put(779748096+i, String.valueOf(i)));
        longARTree.remove(779748096);

        assertNull(longARTree.get(779748096));
    }

    @Test
    public void testRemoveShrink48() {
        IntStream.range(0,49).forEach(i -> longARTree.put(779748096+i, String.valueOf(i)));
        longARTree.remove(779748096);

        assertNull(longARTree.get(779748096));
    }

    @Test
    public void testGetNull() {
        assertNull(longARTree.get(key1));
    }

    @Test
    public void testGetLeaf() {
        longARTree.put(key1, "hello");

        assertEquals("hello", longARTree.get(key1));
    }

    @Test
    public void testGetNullLeaf() {
        longARTree.put(key1, "hello");

        assertNull(longARTree.get(key2));
    }

    @Test
    public void testGetInDifferentBranch() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");

        assertNull(longARTree.get(key9));
    }

    @Test
    public void testGetRecurse() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");

        assertEquals("today", longARTree.get(key6));
    }

    @Test
    public void testGetRecurse1() {
        longARTree.put(key5, "hello");
        longARTree.put(key6, "there");
        longARTree.put(key7, "!");

        assertEquals("!", longARTree.get(key7));
    }

    @Test
    public void testGetParentNull() {
        assertNull(longARTree.getParent(key1, 6));
    }

    @Test
    public void testGetParentNode() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");

        IArtNode<String> parent = longARTree.getParent(key1, 1);
        assertEquals(2, parent.getCount());
        assertEquals(1, parent.getDepth());
    }

    @Test
    public void testGetParentInDifferentBranch() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");

        assertNull(longARTree.getParent(key9,3));
    }

    @Test
    public void testGetParentRecurse() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");

        IArtNode<String> parent = longARTree.getParent(key1, 1);
        assertEquals(2, parent.getCount());
        assertEquals(1, parent.getDepth());
    }

    @Test
    public void testContainsLeaf() {
        longARTree.put(key1, "hello");

        assertTrue(longARTree.contains(key1));
    }

    @Test
    public void testContainsNullLeaf() {
        longARTree.put(key1, "hello");

        assertFalse(longARTree.contains(key2));
    }

    @Test
    public void testContainsInDifferentBranch() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");

        assertFalse(longARTree.contains(key9));
    }

    @Test
    public void testContainsRecurse() {
        longARTree.put(key1, "hello");
        longARTree.put(key6, "today");

        assertTrue(longARTree.contains(key6));
    }

    @Test
    public void testGetCeilingMatch() {
        longARTree.put(key1, "hello");

        assertEquals("hello", longARTree.getCeiling(key1));
    }

    @Test
    public void testGetCeilingRecurse() {
        longARTree.put(key5, "hello");
        longARTree.put(key6, "there");
        longARTree.put(key7, "!");

        assertEquals("hello", longARTree.getCeiling(key4));
    }

    @Test
    public void testGetCeilingRecurseNull() {
        longARTree.put(key5, "hello");
        longARTree.put(key6, "there");
        longARTree.put(key7, "!");

        assertNull(longARTree.getCeiling(key8));
    }

    @Test
    public void testGetCeiling() {
        longARTree.put(key2, "hello");

        assertEquals("hello", longARTree.getCeiling(key1));
    }

    @Test
    public void testGetCeilingNull() {
        longARTree.put(key1, "hello");

        assertNull(longARTree.getCeiling(key2));
    }

    @Test
    public void testGetCeilingEmpty() {
        assertNull(longARTree.getCeiling(key1));
    }

    @Test
    public void testGetCeilingAtEndOfMap() {
        longARTree.put(-3146067877913151380L, "-3146067877913151380");
        longARTree.put(-18589142457476044L, "-18589142457476044");
        longARTree.put(-836236567651234L, "-836236567651234");

        assertNull(longARTree.getCeiling(9223371440841164470L));
    }

    @Test
    public void testGetCeilingOnDifferentBranch() {
        longARTree.put(-9017700876340088127L, String.valueOf(-9017700876340088127L));
        longARTree.put(2096929955676072019L, String.valueOf(2096929955676072019L));
        longARTree.put(2095516849049516633L, String.valueOf(2095516849049516633L));
        longARTree.put(2101757704529877292L, String.valueOf(2101757704529877292L));

        assertEquals("2096929955676072019", longARTree.getCeiling(2096892221972977866L));
    }

    @Test
    public void testGetCeilingOnDifferentBranch1() {
        longARTree.put(3595446722934323407L, String.valueOf(3595446722934323407L));
        longARTree.put(3593668425865759573L, String.valueOf(3593668425865759573L));

        assertEquals("3595446722934323407", longARTree.getCeiling(3594187510015345932L));
    }

    @Test
    public void testGetCeilingOnDifferentBranch2() {
        longARTree.put(-3146067877913151380L, "3146067877913151380");
        longARTree.put(9198235729560100803L, "9198235729560100803");
        longARTree.put(9223300301408329338L, "9223300301408329338");

        assertNull(longARTree.getCeiling(9223371440841164470L));
    }

    @Test
    public void testGetFloorMatch() {
        longARTree.put(key1, "hello");

        assertEquals("hello", longARTree.getFloor(key1));
    }

    @Test
    public void testGetFloor() {
        longARTree.put(key1, "hello");

        assertEquals("hello", longARTree.getFloor(key2));
    }

    @Test
    public void testGetFloorNull() {
        longARTree.put(key2, "hello");

        assertNull(longARTree.getFloor(key1));
    }

    @Test
    public void testGetFloorRecurse() {
        longARTree.put(key5, "hello");
        longARTree.put(key6, "there");
        longARTree.put(key7, "!");

        assertEquals("!", longARTree.getFloor(key8));
    }

    @Test
    public void testGetFloorRecurseNull() {
        longARTree.put(key5, "hello");
        longARTree.put(key6, "there");
        longARTree.put(key7, "!");

        assertNull(longARTree.getFloor(key4));
    }

    @Test
    public void testGetFloorEmpty() {
        assertNull(longARTree.getFloor(key1));
    }

    @Test
    public void testGetFloorAtEndOfMap() {
        longARTree.put(-3146067877913151380L, "-3146067877913151380");
        longARTree.put(-18589142457476044L, "-18589142457476044");
        longARTree.put(-836236567651234L, "-836236567651234");

        assertNull(longARTree.getFloor(3146067877913151381L));
    }

    @Test
    public void testGetFloorOnDifferentBranch() {
        longARTree.put(-9017700876340088127L, String.valueOf(-9017700876340088127L));
        longARTree.put(2096929955676072019L, String.valueOf(2096929955676072019L));
        longARTree.put(2095516849049516633L, String.valueOf(2095516849049516633L));
        longARTree.put(2101757704529877292L, String.valueOf(2101757704529877292L));

        assertEquals("2095516849049516633", longARTree.getFloor(2096892221972977866L));
    }

    @Test
    public void testGetFloorOnDifferentBranch1() {
        longARTree.put(3595446722934323407L, String.valueOf(3595446722934323407L));
        longARTree.put(3593668425865759573L, String.valueOf(3593668425865759573L));

        assertEquals("3593668425865759573", longARTree.getFloor(3594187510015345932L));
    }

    @Test
    public void testGetFloorOnDifferentBranch2() {
        longARTree.put(-3146067877913151380L, "-3146067877913151380");
        longARTree.put(-9198235729560100803L, "-9198235729560100803");
        longARTree.put(-9223300301408329338L, "-9223300301408329338");

        assertNull(longARTree.getFloor(-9223371440841164470L));
    }
}
