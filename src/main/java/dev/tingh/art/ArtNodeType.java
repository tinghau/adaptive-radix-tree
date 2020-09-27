package dev.tingh.art;

public enum ArtNodeType {

    ArtNode4(4), ArtNode16(16), ArtNode48(48), ArtNode256(256);

    private final int size;

    ArtNodeType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
