// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned.rss;

public class DataCharacter
{
    private final int checksumPortion;
    private final int value;
    
    public DataCharacter(final int value, final int checksumPortion) {
        this.value = value;
        this.checksumPortion = checksumPortion;
    }
    
    @Override
    public final boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (!(o instanceof DataCharacter)) {
            b2 = b;
        }
        else {
            final DataCharacter dataCharacter = (DataCharacter)o;
            b2 = b;
            if (this.value == dataCharacter.value) {
                b2 = b;
                if (this.checksumPortion == dataCharacter.checksumPortion) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public final int getChecksumPortion() {
        return this.checksumPortion;
    }
    
    public final int getValue() {
        return this.value;
    }
    
    @Override
    public final int hashCode() {
        return this.value ^ this.checksumPortion;
    }
    
    @Override
    public final String toString() {
        return this.value + "(" + this.checksumPortion + ')';
    }
}
