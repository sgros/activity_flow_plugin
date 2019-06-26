// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Assertions;

public final class SeekParameters
{
    public static final SeekParameters CLOSEST_SYNC;
    public static final SeekParameters DEFAULT;
    public static final SeekParameters EXACT;
    public static final SeekParameters NEXT_SYNC;
    public static final SeekParameters PREVIOUS_SYNC;
    public final long toleranceAfterUs;
    public final long toleranceBeforeUs;
    
    static {
        EXACT = new SeekParameters(0L, 0L);
        CLOSEST_SYNC = new SeekParameters(Long.MAX_VALUE, Long.MAX_VALUE);
        PREVIOUS_SYNC = new SeekParameters(Long.MAX_VALUE, 0L);
        NEXT_SYNC = new SeekParameters(0L, Long.MAX_VALUE);
        DEFAULT = SeekParameters.EXACT;
    }
    
    public SeekParameters(final long toleranceBeforeUs, final long toleranceAfterUs) {
        final boolean b = true;
        Assertions.checkArgument(toleranceBeforeUs >= 0L);
        Assertions.checkArgument(toleranceAfterUs >= 0L && b);
        this.toleranceBeforeUs = toleranceBeforeUs;
        this.toleranceAfterUs = toleranceAfterUs;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && SeekParameters.class == o.getClass()) {
            final SeekParameters seekParameters = (SeekParameters)o;
            if (this.toleranceBeforeUs != seekParameters.toleranceBeforeUs || this.toleranceAfterUs != seekParameters.toleranceAfterUs) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int)this.toleranceBeforeUs * 31 + (int)this.toleranceAfterUs;
    }
}
