// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.DataSource;

public abstract class DiskCacheStrategy
{
    public static final DiskCacheStrategy ALL;
    public static final DiskCacheStrategy AUTOMATIC;
    public static final DiskCacheStrategy DATA;
    public static final DiskCacheStrategy NONE;
    public static final DiskCacheStrategy RESOURCE;
    
    static {
        ALL = new DiskCacheStrategy() {
            @Override
            public boolean decodeCachedData() {
                return true;
            }
            
            @Override
            public boolean decodeCachedResource() {
                return true;
            }
            
            @Override
            public boolean isDataCacheable(final DataSource dataSource) {
                return dataSource == DataSource.REMOTE;
            }
            
            @Override
            public boolean isResourceCacheable(final boolean b, final DataSource dataSource, final EncodeStrategy encodeStrategy) {
                return dataSource != DataSource.RESOURCE_DISK_CACHE && dataSource != DataSource.MEMORY_CACHE;
            }
        };
        NONE = new DiskCacheStrategy() {
            @Override
            public boolean decodeCachedData() {
                return false;
            }
            
            @Override
            public boolean decodeCachedResource() {
                return false;
            }
            
            @Override
            public boolean isDataCacheable(final DataSource dataSource) {
                return false;
            }
            
            @Override
            public boolean isResourceCacheable(final boolean b, final DataSource dataSource, final EncodeStrategy encodeStrategy) {
                return false;
            }
        };
        DATA = new DiskCacheStrategy() {
            @Override
            public boolean decodeCachedData() {
                return true;
            }
            
            @Override
            public boolean decodeCachedResource() {
                return false;
            }
            
            @Override
            public boolean isDataCacheable(final DataSource dataSource) {
                return dataSource != DataSource.DATA_DISK_CACHE && dataSource != DataSource.MEMORY_CACHE;
            }
            
            @Override
            public boolean isResourceCacheable(final boolean b, final DataSource dataSource, final EncodeStrategy encodeStrategy) {
                return false;
            }
        };
        RESOURCE = new DiskCacheStrategy() {
            @Override
            public boolean decodeCachedData() {
                return false;
            }
            
            @Override
            public boolean decodeCachedResource() {
                return true;
            }
            
            @Override
            public boolean isDataCacheable(final DataSource dataSource) {
                return false;
            }
            
            @Override
            public boolean isResourceCacheable(final boolean b, final DataSource dataSource, final EncodeStrategy encodeStrategy) {
                return dataSource != DataSource.RESOURCE_DISK_CACHE && dataSource != DataSource.MEMORY_CACHE;
            }
        };
        AUTOMATIC = new DiskCacheStrategy() {
            @Override
            public boolean decodeCachedData() {
                return true;
            }
            
            @Override
            public boolean decodeCachedResource() {
                return true;
            }
            
            @Override
            public boolean isDataCacheable(final DataSource dataSource) {
                return dataSource == DataSource.REMOTE;
            }
            
            @Override
            public boolean isResourceCacheable(final boolean b, final DataSource dataSource, final EncodeStrategy encodeStrategy) {
                return ((b && dataSource == DataSource.DATA_DISK_CACHE) || dataSource == DataSource.LOCAL) && encodeStrategy == EncodeStrategy.TRANSFORMED;
            }
        };
    }
    
    public abstract boolean decodeCachedData();
    
    public abstract boolean decodeCachedResource();
    
    public abstract boolean isDataCacheable(final DataSource p0);
    
    public abstract boolean isResourceCacheable(final boolean p0, final DataSource p1, final EncodeStrategy p2);
}
