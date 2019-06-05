// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.bitmap_recycle;

public final class IntegerArrayAdapter implements ArrayAdapterInterface<int[]>
{
    @Override
    public int getArrayLength(final int[] array) {
        return array.length;
    }
    
    @Override
    public int getElementSizeInBytes() {
        return 4;
    }
    
    @Override
    public String getTag() {
        return "IntegerArrayPool";
    }
    
    @Override
    public int[] newArray(final int n) {
        return new int[n];
    }
}
