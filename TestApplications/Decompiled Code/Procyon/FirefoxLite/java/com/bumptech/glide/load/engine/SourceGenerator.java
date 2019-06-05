// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Encoder;
import java.util.Collections;
import android.util.Log;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.data.DataFetcher;

class SourceGenerator implements DataCallback<Object>, DataFetcherGenerator, FetcherReadyCallback
{
    private final FetcherReadyCallback cb;
    private Object dataToCache;
    private final DecodeHelper<?> helper;
    private volatile ModelLoader.LoadData<?> loadData;
    private int loadDataListIndex;
    private DataCacheKey originalKey;
    private DataCacheGenerator sourceCacheGenerator;
    
    public SourceGenerator(final DecodeHelper<?> helper, final FetcherReadyCallback cb) {
        this.helper = helper;
        this.cb = cb;
    }
    
    private void cacheData(final Object obj) {
        final long logTime = LogTime.getLogTime();
        try {
            final Encoder<Object> sourceEncoder = this.helper.getSourceEncoder(obj);
            final DataCacheWriter dataCacheWriter = new DataCacheWriter(sourceEncoder, obj, this.helper.getOptions());
            this.originalKey = new DataCacheKey(this.loadData.sourceKey, this.helper.getSignature());
            this.helper.getDiskCache().put(this.originalKey, (DiskCache.Writer)dataCacheWriter);
            if (Log.isLoggable("SourceGenerator", 2)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Finished encoding source to cache, key: ");
                sb.append(this.originalKey);
                sb.append(", data: ");
                sb.append(obj);
                sb.append(", encoder: ");
                sb.append(sourceEncoder);
                sb.append(", duration: ");
                sb.append(LogTime.getElapsedMillis(logTime));
                Log.v("SourceGenerator", sb.toString());
            }
            this.loadData.fetcher.cleanup();
            this.sourceCacheGenerator = new DataCacheGenerator(Collections.singletonList(this.loadData.sourceKey), this.helper, this);
        }
        finally {
            this.loadData.fetcher.cleanup();
        }
    }
    
    private boolean hasNextModelLoader() {
        return this.loadDataListIndex < this.helper.getLoadData().size();
    }
    
    @Override
    public void cancel() {
        final ModelLoader.LoadData<?> loadData = this.loadData;
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }
    
    @Override
    public void onDataFetcherFailed(final Key key, final Exception ex, final DataFetcher<?> dataFetcher, final DataSource dataSource) {
        this.cb.onDataFetcherFailed(key, ex, dataFetcher, this.loadData.fetcher.getDataSource());
    }
    
    @Override
    public void onDataFetcherReady(final Key key, final Object o, final DataFetcher<?> dataFetcher, final DataSource dataSource, final Key key2) {
        this.cb.onDataFetcherReady(key, o, dataFetcher, this.loadData.fetcher.getDataSource(), key);
    }
    
    @Override
    public void onDataReady(final Object dataToCache) {
        final DiskCacheStrategy diskCacheStrategy = this.helper.getDiskCacheStrategy();
        if (dataToCache != null && diskCacheStrategy.isDataCacheable(this.loadData.fetcher.getDataSource())) {
            this.dataToCache = dataToCache;
            this.cb.reschedule();
        }
        else {
            this.cb.onDataFetcherReady(this.loadData.sourceKey, dataToCache, this.loadData.fetcher, this.loadData.fetcher.getDataSource(), this.originalKey);
        }
    }
    
    @Override
    public void onLoadFailed(final Exception ex) {
        this.cb.onDataFetcherFailed(this.originalKey, ex, this.loadData.fetcher, this.loadData.fetcher.getDataSource());
    }
    
    @Override
    public void reschedule() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean startNext() {
        if (this.dataToCache != null) {
            final Object dataToCache = this.dataToCache;
            this.dataToCache = null;
            this.cacheData(dataToCache);
        }
        if (this.sourceCacheGenerator != null && this.sourceCacheGenerator.startNext()) {
            return true;
        }
        this.sourceCacheGenerator = null;
        this.loadData = null;
        boolean b;
        for (b = false; !b && this.hasNextModelLoader(); b = true) {
            this.loadData = (ModelLoader.LoadData<?>)(ModelLoader.LoadData)this.helper.getLoadData().get(this.loadDataListIndex++);
            if (this.loadData != null && (this.helper.getDiskCacheStrategy().isDataCacheable(this.loadData.fetcher.getDataSource()) || this.helper.hasLoadPath(this.loadData.fetcher.getDataClass()))) {
                this.loadData.fetcher.loadData(this.helper.getPriority(), (DataFetcher.DataCallback<?>)this);
            }
        }
        return b;
    }
}
