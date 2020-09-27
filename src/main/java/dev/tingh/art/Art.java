package dev.tingh.art;

public class Art {

    public static String toNodeLevelKeys(long key) {
        return (key >>> 56 & 255L) + "," +
                (key >>> 48 & 255L) + "," +
                (key >>> 40 & 255L) + "," +
                (key >>> 32 & 255L) + "," +
                (key >>> 24 & 255L) + "," +
                (key >>> 16 & 255L) + "," +
                (key >>> 8 & 255L) + "," +
                (key & 255L);
    }
}
