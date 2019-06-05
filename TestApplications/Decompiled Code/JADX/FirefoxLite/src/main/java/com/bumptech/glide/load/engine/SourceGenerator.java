package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.util.LogTime;
import java.util.Collections;
import java.util.List;

class SourceGenerator implements DataCallback<Object>, DataFetcherGenerator, FetcherReadyCallback {
    /* renamed from: cb */
    private final FetcherReadyCallback f60cb;
    private Object dataToCache;
    private final DecodeHelper<?> helper;
    private volatile LoadData<?> loadData;
    private int loadDataListIndex;
    private DataCacheKey originalKey;
    private DataCacheGenerator sourceCacheGenerator;

    public SourceGenerator(DecodeHelper<?> decodeHelper, FetcherReadyCallback fetcherReadyCallback) {
        this.helper = decodeHelper;
        this.f60cb = fetcherReadyCallback;
    }

    public boolean startNext() {
        if (this.dataToCache != null) {
            Object obj = this.dataToCache;
            this.dataToCache = null;
            cacheData(obj);
        }
        if (this.sourceCacheGenerator != null && this.sourceCacheGenerator.startNext()) {
            return true;
        }
        this.sourceCacheGenerator = null;
        this.loadData = null;
        boolean z = false;
        while (!z && hasNextModelLoader()) {
            List loadData = this.helper.getLoadData();
            int i = this.loadDataListIndex;
            this.loadDataListIndex = i + 1;
            this.loadData = (LoadData) loadData.get(i);
            if (this.loadData != null && (this.helper.getDiskCacheStrategy().isDataCacheable(this.loadData.fetcher.getDataSource()) || this.helper.hasLoadPath(this.loadData.fetcher.getDataClass()))) {
                this.loadData.fetcher.loadData(this.helper.getPriority(), this);
                z = true;
            }
        }
        return z;
    }

    private boolean hasNextModelLoader() {
        return this.loadDataListIndex < this.helper.getLoadData().size();
    }

    private void cacheData(Object obj) {
        long logTime = LogTime.getLogTime();
        try {
            Encoder sourceEncoder = this.helper.getSourceEncoder(obj);
            DataCacheWriter dataCacheWriter = new DataCacheWriter(sourceEncoder, obj, this.helper.getOptions());
            this.originalKey = new DataCacheKey(this.loadData.sourceKey, this.helper.getSignature());
            this.helper.getDiskCache().put(this.originalKey, dataCacheWriter);
            if (Log.isLoggable("SourceGenerator", 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Finished encoding source to cache, key: ");
                stringBuilder.append(this.originalKey);
                stringBuilder.append(", data: ");
                stringBuilder.append(obj);
                stringBuilder.append(", encoder: ");
                stringBuilder.append(sourceEncoder);
                stringBuilder.append(", duration: ");
                stringBuilder.append(LogTime.getElapsedMillis(logTime));
                Log.v("SourceGenerator", stringBuilder.toString());
            }
            this.loadData.fetcher.cleanup();
            this.sourceCacheGenerator = new DataCacheGenerator(Collections.singletonList(this.loadData.sourceKey), this.helper, this);
        } catch (Throwable th) {
            this.loadData.fetcher.cleanup();
        }
    }

    public void cancel() {
        LoadData loadData = this.loadData;
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }

    public void onDataReady(Object obj) {
        DiskCacheStrategy diskCacheStrategy = this.helper.getDiskCacheStrategy();
        if (obj == null || !diskCacheStrategy.isDataCacheable(this.loadData.fetcher.getDataSource())) {
            this.f60cb.onDataFetcherReady(this.loadData.sourceKey, obj, this.loadData.fetcher, this.loadData.fetcher.getDataSource(), this.originalKey);
            return;
        }
        this.dataToCache = obj;
        this.f60cb.reschedule();
    }

    public void onLoadFailed(Exception exception) {
        this.f60cb.onDataFetcherFailed(this.originalKey, exception, this.loadData.fetcher, this.loadData.fetcher.getDataSource());
    }

    public void reschedule() {
        throw new UnsupportedOperationException();
    }

    public void onDataFetcherReady(Key key, Object obj, DataFetcher<?> dataFetcher, DataSource dataSource, Key key2) {
        this.f60cb.onDataFetcherReady(key, obj, dataFetcher, this.loadData.fetcher.getDataSource(), key);
    }

    public void onDataFetcherFailed(Key key, Exception exception, DataFetcher<?> dataFetcher, DataSource dataSource) {
        this.f60cb.onDataFetcherFailed(key, exception, dataFetcher, this.loadData.fetcher.getDataSource());
    }
}
