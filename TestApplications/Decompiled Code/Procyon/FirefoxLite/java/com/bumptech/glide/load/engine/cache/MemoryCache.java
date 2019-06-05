// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.Key;

public interface MemoryCache
{
    void clearMemory();
    
    Resource<?> put(final Key p0, final Resource<?> p1);
    
    Resource<?> remove(final Key p0);
    
    void setResourceRemovedListener(final ResourceRemovedListener p0);
    
    void trimMemory(final int p0);
    
    public interface ResourceRemovedListener
    {
        void onResourceRemoved(final Resource<?> p0);
    }
}
