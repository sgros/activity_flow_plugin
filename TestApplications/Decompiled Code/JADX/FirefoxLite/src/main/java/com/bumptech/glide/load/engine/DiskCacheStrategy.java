package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;

public abstract class DiskCacheStrategy {
    public static final DiskCacheStrategy ALL = new C03921();
    public static final DiskCacheStrategy AUTOMATIC = new C03965();
    public static final DiskCacheStrategy DATA = new C03943();
    public static final DiskCacheStrategy NONE = new C03932();
    public static final DiskCacheStrategy RESOURCE = new C03954();

    /* renamed from: com.bumptech.glide.load.engine.DiskCacheStrategy$1 */
    static class C03921 extends DiskCacheStrategy {
        public boolean decodeCachedData() {
            return true;
        }

        public boolean decodeCachedResource() {
            return true;
        }

        C03921() {
        }

        public boolean isDataCacheable(DataSource dataSource) {
            return dataSource == DataSource.REMOTE;
        }

        public boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return (dataSource == DataSource.RESOURCE_DISK_CACHE || dataSource == DataSource.MEMORY_CACHE) ? false : true;
        }
    }

    /* renamed from: com.bumptech.glide.load.engine.DiskCacheStrategy$2 */
    static class C03932 extends DiskCacheStrategy {
        public boolean decodeCachedData() {
            return false;
        }

        public boolean decodeCachedResource() {
            return false;
        }

        public boolean isDataCacheable(DataSource dataSource) {
            return false;
        }

        public boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return false;
        }

        C03932() {
        }
    }

    /* renamed from: com.bumptech.glide.load.engine.DiskCacheStrategy$3 */
    static class C03943 extends DiskCacheStrategy {
        public boolean decodeCachedData() {
            return true;
        }

        public boolean decodeCachedResource() {
            return false;
        }

        public boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return false;
        }

        C03943() {
        }

        public boolean isDataCacheable(DataSource dataSource) {
            return (dataSource == DataSource.DATA_DISK_CACHE || dataSource == DataSource.MEMORY_CACHE) ? false : true;
        }
    }

    /* renamed from: com.bumptech.glide.load.engine.DiskCacheStrategy$4 */
    static class C03954 extends DiskCacheStrategy {
        public boolean decodeCachedData() {
            return false;
        }

        public boolean decodeCachedResource() {
            return true;
        }

        public boolean isDataCacheable(DataSource dataSource) {
            return false;
        }

        C03954() {
        }

        public boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return (dataSource == DataSource.RESOURCE_DISK_CACHE || dataSource == DataSource.MEMORY_CACHE) ? false : true;
        }
    }

    /* renamed from: com.bumptech.glide.load.engine.DiskCacheStrategy$5 */
    static class C03965 extends DiskCacheStrategy {
        public boolean decodeCachedData() {
            return true;
        }

        public boolean decodeCachedResource() {
            return true;
        }

        C03965() {
        }

        public boolean isDataCacheable(DataSource dataSource) {
            return dataSource == DataSource.REMOTE;
        }

        public boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy) {
            return ((z && dataSource == DataSource.DATA_DISK_CACHE) || dataSource == DataSource.LOCAL) && encodeStrategy == EncodeStrategy.TRANSFORMED;
        }
    }

    public abstract boolean decodeCachedData();

    public abstract boolean decodeCachedResource();

    public abstract boolean isDataCacheable(DataSource dataSource);

    public abstract boolean isResourceCacheable(boolean z, DataSource dataSource, EncodeStrategy encodeStrategy);
}
