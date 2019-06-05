// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.Key;
import java.util.List;
import java.io.File;
import com.bumptech.glide.load.data.DataFetcher;

class DataCacheGenerator implements DataCallback<Object>, DataFetcherGenerator
{
    private File cacheFile;
    private List<Key> cacheKeys;
    private final FetcherReadyCallback cb;
    private final DecodeHelper<?> helper;
    private volatile ModelLoader.LoadData<?> loadData;
    private int modelLoaderIndex;
    private List<ModelLoader<File, ?>> modelLoaders;
    private int sourceIdIndex;
    private Key sourceKey;
    
    DataCacheGenerator(final DecodeHelper<?> decodeHelper, final FetcherReadyCallback fetcherReadyCallback) {
        this(decodeHelper.getCacheKeys(), decodeHelper, fetcherReadyCallback);
    }
    
    DataCacheGenerator(final List<Key> cacheKeys, final DecodeHelper<?> helper, final FetcherReadyCallback cb) {
        this.sourceIdIndex = -1;
        this.cacheKeys = cacheKeys;
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
        this.cb.onDataFetcherReady(this.sourceKey, o, this.loadData.fetcher, DataSource.DATA_DISK_CACHE, this.sourceKey);
    }
    
    @Override
    public void onLoadFailed(final Exception ex) {
        this.cb.onDataFetcherFailed(this.sourceKey, ex, this.loadData.fetcher, DataSource.DATA_DISK_CACHE);
    }
    
    @Override
    public boolean startNext() {
        while (true) {
            final List<ModelLoader<File, ?>> modelLoaders = this.modelLoaders;
            boolean b = false;
            if (modelLoaders != null && this.hasNextModelLoader()) {
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
            ++this.sourceIdIndex;
            if (this.sourceIdIndex >= this.cacheKeys.size()) {
                return false;
            }
            final Key sourceKey = this.cacheKeys.get(this.sourceIdIndex);
            this.cacheFile = this.helper.getDiskCache().get(new DataCacheKey(sourceKey, this.helper.getSignature()));
            if (this.cacheFile == null) {
                continue;
            }
            this.sourceKey = sourceKey;
            this.modelLoaders = this.helper.getModelLoaders(this.cacheFile);
            this.modelLoaderIndex = 0;
        }
    }
}
