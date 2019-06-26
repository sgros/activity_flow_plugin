package org.osmdroid.tileprovider.tilesource;

public class TileSourcePolicy {
    private final int mFlags;
    private final int mMaxConcurrent;

    public TileSourcePolicy() {
        this(0, 0);
    }

    public TileSourcePolicy(int i, int i2) {
        this.mMaxConcurrent = i;
        this.mFlags = i2;
    }

    public int getMaxConcurrent() {
        return this.mMaxConcurrent;
    }

    private boolean acceptsMeaninglessUserAgent() {
        return (this.mFlags & 4) == 0;
    }

    public boolean normalizesUserAgent() {
        return (this.mFlags & 8) != 0;
    }

    public boolean acceptsPreventive() {
        return (this.mFlags & 2) == 0;
    }

    public boolean acceptsUserAgent(String str) {
        boolean z = true;
        if (acceptsMeaninglessUserAgent()) {
            return true;
        }
        if (str == null || str.trim().length() <= 0 || str.equals("osmdroid")) {
            z = false;
        }
        return z;
    }
}
