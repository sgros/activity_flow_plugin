// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.bitmap_recycle;

public interface ArrayPool
{
    void clearMemory();
    
     <T> T get(final int p0, final Class<T> p1);
    
     <T> void put(final T p0, final Class<T> p1);
    
    void trimMemory(final int p0);
}
