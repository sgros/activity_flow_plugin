// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.util.Assertions;
import java.util.Arrays;

final class NalUnitTargetBuffer
{
    private boolean isCompleted;
    private boolean isFilling;
    public byte[] nalData;
    public int nalLength;
    private final int targetType;
    
    public NalUnitTargetBuffer(final int targetType, final int n) {
        this.targetType = targetType;
        (this.nalData = new byte[n + 3])[2] = 1;
    }
    
    public void appendToNalUnit(final byte[] array, final int n, int n2) {
        if (!this.isFilling) {
            return;
        }
        n2 -= n;
        final byte[] nalData = this.nalData;
        final int length = nalData.length;
        final int nalLength = this.nalLength;
        if (length < nalLength + n2) {
            this.nalData = Arrays.copyOf(nalData, (nalLength + n2) * 2);
        }
        System.arraycopy(array, n, this.nalData, this.nalLength, n2);
        this.nalLength += n2;
    }
    
    public boolean endNalUnit(final int n) {
        if (!this.isFilling) {
            return false;
        }
        this.nalLength -= n;
        this.isFilling = false;
        return this.isCompleted = true;
    }
    
    public boolean isCompleted() {
        return this.isCompleted;
    }
    
    public void reset() {
        this.isFilling = false;
        this.isCompleted = false;
    }
    
    public void startNalUnit(final int n) {
        final boolean isFilling = this.isFilling;
        boolean isFilling2 = true;
        Assertions.checkState(isFilling ^ true);
        if (n != this.targetType) {
            isFilling2 = false;
        }
        this.isFilling = isFilling2;
        if (this.isFilling) {
            this.nalLength = 3;
            this.isCompleted = false;
        }
    }
}
