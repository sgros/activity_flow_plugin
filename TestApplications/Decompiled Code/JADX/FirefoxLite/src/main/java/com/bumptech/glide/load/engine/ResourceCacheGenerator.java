package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import java.io.File;
import java.util.List;

class ResourceCacheGenerator implements DataCallback<Object>, DataFetcherGenerator {
    private File cacheFile;
    /* renamed from: cb */
    private final FetcherReadyCallback f59cb;
    private ResourceCacheKey currentKey;
    private final DecodeHelper<?> helper;
    private volatile LoadData<?> loadData;
    private int modelLoaderIndex;
    private List<ModelLoader<File, ?>> modelLoaders;
    private int resourceClassIndex = -1;
    private int sourceIdIndex = 0;
    private Key sourceKey;

    public ResourceCacheGenerator(DecodeHelper<?> decodeHelper, FetcherReadyCallback fetcherReadyCallback) {
        this.helper = decodeHelper;
        this.f59cb = fetcherReadyCallback;
    }

    public boolean startNext() {
        List cacheKeys = this.helper.getCacheKeys();
        boolean z = false;
        if (cacheKeys.isEmpty()) {
            return false;
        }
        List registeredResourceClasses = this.helper.getRegisteredResourceClasses();
        while (true) {
            if (this.modelLoaders == null || !hasNextModelLoader()) {
                this.resourceClassIndex++;
                if (this.resourceClassIndex >= registeredResourceClasses.size()) {
                    this.sourceIdIndex++;
                    if (this.sourceIdIndex >= cacheKeys.size()) {
                        return false;
                    }
                    this.resourceClassIndex = 0;
                }
                Key key = (Key) cacheKeys.get(this.sourceIdIndex);
                Class cls = (Class) registeredResourceClasses.get(this.resourceClassIndex);
                Key key2 = key;
                this.currentKey = new ResourceCacheKey(key2, this.helper.getSignature(), this.helper.getWidth(), this.helper.getHeight(), this.helper.getTransformation(cls), cls, this.helper.getOptions());
                this.cacheFile = this.helper.getDiskCache().get(this.currentKey);
                if (this.cacheFile != null) {
                    this.sourceKey = key;
                    this.modelLoaders = this.helper.getModelLoaders(this.cacheFile);
                    this.modelLoaderIndex = 0;
                }
            } else {
                this.loadData = null;
                while (!z && hasNextModelLoader()) {
                    cacheKeys = this.modelLoaders;
                    int i = this.modelLoaderIndex;
                    this.modelLoaderIndex = i + 1;
                    this.loadData = ((ModelLoader) cacheKeys.get(i)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
                    if (this.loadData != null && this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
                        this.loadData.fetcher.loadData(this.helper.getPriority(), this);
                        z = true;
                    }
                }
                return z;
            }
        }
    }

    private boolean hasNextModelLoader() {
        return this.modelLoaderIndex < this.modelLoaders.size();
    }

    public void cancel() {
        LoadData loadData = this.loadData;
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }

    public void onDataReady(Object obj) {
        this.f59cb.onDataFetcherReady(this.sourceKey, obj, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE, this.currentKey);
    }

    public void onLoadFailed(Exception exception) {
        this.f59cb.onDataFetcherFailed(this.currentKey, exception, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE);
    }
}
