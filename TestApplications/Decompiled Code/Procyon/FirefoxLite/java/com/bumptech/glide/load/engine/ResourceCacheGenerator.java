// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import java.util.List;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import com.bumptech.glide.load.data.DataFetcher;

class ResourceCacheGenerator implements DataCallback<Object>, DataFetcherGenerator
{
    private File cacheFile;
    private final FetcherReadyCallback cb;
    private ResourceCacheKey currentKey;
    private final DecodeHelper<?> helper;
    private volatile ModelLoader.LoadData<?> loadData;
    private int modelLoaderIndex;
    private List<ModelLoader<File, ?>> modelLoaders;
    private int resourceClassIndex;
    private int sourceIdIndex;
    private Key sourceKey;
    
    public ResourceCacheGenerator(final DecodeHelper<?> helper, final FetcherReadyCallback cb) {
        this.sourceIdIndex = 0;
        this.resourceClassIndex = -1;
        this.helper = helper;
        this.cb = cb;
    }
    
    private boolean hasNextModelLoader() {
        return this.modelLoaderIndex < this.modelLoaders.size();
    }
    
    @Override
    public void cancel() {
        final ModelLoader.LoadData<?> loadData = this.loadData;
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }
    
    @Override
    public void onDataReady(final Object o) {
        this.cb.onDataFetcherReady(this.sourceKey, o, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE, this.currentKey);
    }
    
    @Override
    public void onLoadFailed(final Exception ex) {
        this.cb.onDataFetcherFailed(this.currentKey, ex, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE);
    }
    
    @Override
    public boolean startNext() {
        final List<Key> cacheKeys = this.helper.getCacheKeys();
        final boolean empty = cacheKeys.isEmpty();
        boolean b = false;
        if (empty) {
            return false;
        }
        final List<Class<?>> registeredResourceClasses = this.helper.getRegisteredResourceClasses();
        while (this.modelLoaders == null || !this.hasNextModelLoader()) {
            ++this.resourceClassIndex;
            if (this.resourceClassIndex >= registeredResourceClasses.size()) {
                ++this.sourceIdIndex;
                if (this.sourceIdIndex >= cacheKeys.size()) {
                    return false;
                }
                this.resourceClassIndex = 0;
            }
            final Key sourceKey = cacheKeys.get(this.sourceIdIndex);
            final Class<Object> clazz = registeredResourceClasses.get(this.resourceClassIndex);
            this.currentKey = new ResourceCacheKey(sourceKey, this.helper.getSignature(), this.helper.getWidth(), this.helper.getHeight(), this.helper.getTransformation(clazz), clazz, this.helper.getOptions());
            this.cacheFile = this.helper.getDiskCache().get(this.currentKey);
            if (this.cacheFile != null) {
                this.sourceKey = sourceKey;
                this.modelLoaders = this.helper.getModelLoaders(this.cacheFile);
                this.modelLoaderIndex = 0;
            }
        }
        this.loadData = null;
        while (!b && this.hasNextModelLoader()) {
            this.loadData = this.modelLoaders.get(this.modelLoaderIndex++).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
            if (this.loadData != null && this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
                this.loadData.fetcher.loadData(this.helper.getPriority(), (DataFetcher.DataCallback<?>)this);
                b = true;
            }
        }
        return b;
    }
}
