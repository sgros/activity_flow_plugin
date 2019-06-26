// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.decoder;

public abstract class Buffer
{
    private int flags;
    
    public final void addFlag(final int n) {
        this.flags |= n;
    }
    
    public void clear() {
        this.flags = 0;
    }
    
    public final void clearFlag(final int n) {
        this.flags &= ~n;
    }
    
    protected final boolean getFlag(final int n) {
        return (this.flags & n) == n;
    }
    
    public final boolean isDecodeOnly() {
        return this.getFlag(Integer.MIN_VALUE);
    }
    
    public final boolean isEndOfStream() {
        return this.getFlag(4);
    }
    
    public final boolean isKeyFrame() {
        return this.getFlag(1);
    }
    
    public final void setFlags(final int flags) {
        this.flags = flags;
    }
}
