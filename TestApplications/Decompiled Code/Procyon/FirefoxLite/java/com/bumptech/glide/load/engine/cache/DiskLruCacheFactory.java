// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.cache;

import java.io.File;

public class DiskLruCacheFactory implements Factory
{
    private final CacheDirectoryGetter cacheDirectoryGetter;
    private final int diskCacheSize;
    
    public DiskLruCacheFactory(final CacheDirectoryGetter cacheDirectoryGetter, final int diskCacheSize) {
        this.diskCacheSize = diskCacheSize;
        this.cacheDirectoryGetter = cacheDirectoryGetter;
    }
    
    @Override
    public DiskCache build() {
        final File cacheDirectory = this.cacheDirectoryGetter.getCacheDirectory();
        if (cacheDirectory == null) {
            return null;
        }
        if (!cacheDirectory.mkdirs() && (!cacheDirectory.exists() || !cacheDirectory.isDirectory())) {
            return null;
        }
        return DiskLruCacheWrapper.get(cacheDirectory, this.diskCacheSize);
    }
    
    public interface CacheDirectoryGetter
    {
        File getCacheDirectory();
    }
}
