package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import java.io.File;
import java.util.List;

class DataCacheGenerator implements DataCallback<Object>, DataFetcherGenerator {
    private File cacheFile;
    private List<Key> cacheKeys;
    /* renamed from: cb */
    private final FetcherReadyCallback f58cb;
    private final DecodeHelper<?> helper;
    private volatile LoadData<?> loadData;
    private int modelLoaderIndex;
    private List<ModelLoader<File, ?>> modelLoaders;
    private int sourceIdIndex;
    private Key sourceKey;

    DataCacheGenerator(DecodeHelper<?> decodeHelper, FetcherReadyCallback fetcherReadyCallback) {
        this(decodeHelper.getCacheKeys(), decodeHelper, fetcherReadyCallback);
    }

    DataCacheGenerator(List<Key> list, DecodeHelper<?> decodeHelper, FetcherReadyCallback fetcherReadyCallback) {
        this.sourceIdIndex = -1;
        this.cacheKeys = list;
        this.helper = decodeHelper;
        this.f58cb = fetcherReadyCallback;
    }

    public boolean startNext() {
        while (true) {
            boolean z = false;
            if (this.modelLoaders == null || !hasNextModelLoader()) {
                this.sourceIdIndex++;
                if (this.sourceIdIndex >= this.cacheKeys.size()) {
                    return false;
                }
                Key key = (Key) this.cacheKeys.get(this.sourceIdIndex);
                this.cacheFile = this.helper.getDiskCache().get(new DataCacheKey(key, this.helper.getSignature()));
                if (this.cacheFile != null) {
                    this.sourceKey = key;
                    this.modelLoaders = this.helper.getModelLoaders(this.cacheFile);
                    this.modelLoaderIndex = 0;
                }
            } else {
                this.loadData = null;
                while (!z && hasNextModelLoader()) {
                    List list = this.modelLoaders;
                    int i = this.modelLoaderIndex;
                    this.modelLoaderIndex = i + 1;
                    this.loadData = ((ModelLoader) list.get(i)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
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
        this.f58cb.onDataFetcherReady(this.sourceKey, obj, this.loadData.fetcher, DataSource.DATA_DISK_CACHE, this.sourceKey);
    }

    public void onLoadFailed(Exception exception) {
        this.f58cb.onDataFetcherFailed(this.sourceKey, exception, this.loadData.fetcher, DataSource.DATA_DISK_CACHE);
    }
}
