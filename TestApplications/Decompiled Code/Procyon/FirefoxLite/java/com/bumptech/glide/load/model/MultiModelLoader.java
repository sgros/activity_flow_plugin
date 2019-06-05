// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import java.util.Collection;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.Priority;
import java.util.Arrays;
import java.util.Iterator;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import java.util.ArrayList;
import com.bumptech.glide.load.Options;
import java.util.List;
import android.support.v4.util.Pools;

class MultiModelLoader<Model, Data> implements ModelLoader<Model, Data>
{
    private final Pools.Pool<List<Exception>> exceptionListPool;
    private final List<ModelLoader<Model, Data>> modelLoaders;
    
    MultiModelLoader(final List<ModelLoader<Model, Data>> modelLoaders, final Pools.Pool<List<Exception>> exceptionListPool) {
        this.modelLoaders = modelLoaders;
        this.exceptionListPool = exceptionListPool;
    }
    
    @Override
    public LoadData<Data> buildLoadData(final Model model, final int n, final int n2, final Options options) {
        final int size = this.modelLoaders.size();
        final ArrayList list = new ArrayList<DataFetcher<Data>>(size);
        final LoadData<Data> loadData = null;
        int i = 0;
        Key key = null;
        while (i < size) {
            final ModelLoader<Model, Data> modelLoader = this.modelLoaders.get(i);
            Key sourceKey = key;
            if (modelLoader.handles(model)) {
                final LoadData<Data> buildLoadData = modelLoader.buildLoadData(model, n, n2, options);
                sourceKey = key;
                if (buildLoadData != null) {
                    sourceKey = buildLoadData.sourceKey;
                    list.add((DataFetcher<Data>)buildLoadData.fetcher);
                }
            }
            ++i;
            key = sourceKey;
        }
        Object o = loadData;
        if (!list.isEmpty()) {
            o = new LoadData(key, new MultiFetcher<Object>((List<DataFetcher<Object>>)list, this.exceptionListPool));
        }
        return (LoadData<Data>)o;
    }
    
    @Override
    public boolean handles(final Model model) {
        final Iterator<ModelLoader<Model, Data>> iterator = this.modelLoaders.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().handles(model)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MultiModelLoader{modelLoaders=");
        sb.append(Arrays.toString(this.modelLoaders.toArray(new ModelLoader[this.modelLoaders.size()])));
        sb.append('}');
        return sb.toString();
    }
    
    static class MultiFetcher<Data> implements DataFetcher<Data>, DataCallback<Data>
    {
        private DataCallback<? super Data> callback;
        private int currentIndex;
        private final Pools.Pool<List<Exception>> exceptionListPool;
        private List<Exception> exceptions;
        private final List<DataFetcher<Data>> fetchers;
        private Priority priority;
        
        MultiFetcher(final List<DataFetcher<Data>> fetchers, final Pools.Pool<List<Exception>> exceptionListPool) {
            this.exceptionListPool = exceptionListPool;
            Preconditions.checkNotEmpty(fetchers);
            this.fetchers = fetchers;
            this.currentIndex = 0;
        }
        
        private void startNextOrFail() {
            if (this.currentIndex < this.fetchers.size() - 1) {
                ++this.currentIndex;
                this.loadData(this.priority, this.callback);
            }
            else {
                this.callback.onLoadFailed(new GlideException("Fetch failed", new ArrayList<Exception>(this.exceptions)));
            }
        }
        
        @Override
        public void cancel() {
            final Iterator<DataFetcher<Data>> iterator = this.fetchers.iterator();
            while (iterator.hasNext()) {
                iterator.next().cancel();
            }
        }
        
        @Override
        public void cleanup() {
            if (this.exceptions != null) {
                this.exceptionListPool.release(this.exceptions);
            }
            this.exceptions = null;
            final Iterator<DataFetcher<Data>> iterator = this.fetchers.iterator();
            while (iterator.hasNext()) {
                iterator.next().cleanup();
            }
        }
        
        @Override
        public Class<Data> getDataClass() {
            return this.fetchers.get(0).getDataClass();
        }
        
        @Override
        public DataSource getDataSource() {
            return this.fetchers.get(0).getDataSource();
        }
        
        @Override
        public void loadData(final Priority priority, final DataCallback<? super Data> callback) {
            this.priority = priority;
            this.callback = callback;
            this.exceptions = this.exceptionListPool.acquire();
            this.fetchers.get(this.currentIndex).loadData(priority, (DataCallback<? super Data>)this);
        }
        
        @Override
        public void onDataReady(final Data data) {
            if (data != null) {
                this.callback.onDataReady((Object)data);
            }
            else {
                this.startNextOrFail();
            }
        }
        
        @Override
        public void onLoadFailed(final Exception ex) {
            this.exceptions.add(ex);
            this.startNextOrFail();
        }
    }
}
