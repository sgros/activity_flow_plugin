// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

public final class SeekPoint
{
    public static final SeekPoint START;
    public final long position;
    public final long timeUs;
    
    static {
        START = new SeekPoint(0L, 0L);
    }
    
    public SeekPoint(final long timeUs, final long position) {
        this.timeUs = timeUs;
        this.position = position;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && SeekPoint.class == o.getClass()) {
            final SeekPoint seekPoint = (SeekPoint)o;
            if (this.timeUs != seekPoint.timeUs || this.position != seekPoint.position) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int)this.timeUs * 31 + (int)this.position;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[timeUs=");
        sb.append(this.timeUs);
        sb.append(", position=");
        sb.append(this.position);
        sb.append("]");
        return sb.toString();
    }
}
