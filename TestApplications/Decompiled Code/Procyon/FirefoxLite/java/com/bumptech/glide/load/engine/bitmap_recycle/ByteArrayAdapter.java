// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.bitmap_recycle;

public final class ByteArrayAdapter implements ArrayAdapterInterface<byte[]>
{
    @Override
    public int getArrayLength(final byte[] array) {
        return array.length;
    }
    
    @Override
    public int getElementSizeInBytes() {
        return 1;
    }
    
    @Override
    public String getTag() {
        return "ByteArrayPool";
    }
    
    @Override
    public byte[] newArray(final int n) {
        return new byte[n];
    }
}
