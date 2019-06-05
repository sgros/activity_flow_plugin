// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

public abstract class UPCEANWriter extends OneDimensionalCodeWriter
{
    @Override
    public int getDefaultMargin() {
        return UPCEANReader.START_END_PATTERN.length;
    }
}
