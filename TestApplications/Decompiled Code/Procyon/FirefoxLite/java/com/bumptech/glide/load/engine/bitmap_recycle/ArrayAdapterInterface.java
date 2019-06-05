// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.bitmap_recycle;

public interface ArrayAdapterInterface<T>
{
    int getArrayLength(final T p0);
    
    int getElementSizeInBytes();
    
    String getTag();
    
    T newArray(final int p0);
}
