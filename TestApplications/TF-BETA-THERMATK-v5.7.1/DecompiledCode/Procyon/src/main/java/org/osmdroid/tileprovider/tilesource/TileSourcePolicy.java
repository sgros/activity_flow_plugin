// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.tilesource;

public class TileSourcePolicy
{
    private final int mFlags;
    private final int mMaxConcurrent;
    
    public TileSourcePolicy() {
        this(0, 0);
    }
    
    public TileSourcePolicy(final int mMaxConcurrent, final int mFlags) {
        this.mMaxConcurrent = mMaxConcurrent;
        this.mFlags = mFlags;
    }
    
    private boolean acceptsMeaninglessUserAgent() {
        return (this.mFlags & 0x4) == 0x0;
    }
    
    public boolean acceptsPreventive() {
        return (this.mFlags & 0x2) == 0x0;
    }
    
    public boolean acceptsUserAgent(final String s) {
        final boolean acceptsMeaninglessUserAgent = this.acceptsMeaninglessUserAgent();
        boolean b = true;
        if (acceptsMeaninglessUserAgent) {
            return true;
        }
        if (s == null || s.trim().length() <= 0 || s.equals("osmdroid")) {
            b = false;
        }
        return b;
    }
    
    public int getMaxConcurrent() {
        return this.mMaxConcurrent;
    }
    
    public boolean normalizesUserAgent() {
        return (this.mFlags & 0x8) != 0x0;
    }
}
