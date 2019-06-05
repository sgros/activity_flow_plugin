// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.cache;

import java.io.File;
import com.bumptech.glide.load.Key;

public interface DiskCache
{
    File get(final Key p0);
    
    void put(final Key p0, final Writer p1);
    
    public interface Factory
    {
        DiskCache build();
    }
    
    public interface Writer
    {
        boolean write(final File p0);
    }
}
