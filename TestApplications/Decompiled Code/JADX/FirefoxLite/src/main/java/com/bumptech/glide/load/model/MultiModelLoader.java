package com.bumptech.glide.load.model;

import android.support.p001v4.util.Pools.Pool;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class MultiModelLoader<Model, Data> implements ModelLoader<Model, Data> {
    private final Pool<List<Exception>> exceptionListPool;
    private final List<ModelLoader<Model, Data>> modelLoaders;

    static class MultiFetcher<Data> implements DataFetcher<Data>, DataCallback<Data> {
        private DataCallback<? super Data> callback;
        private int currentIndex = 0;
        private final Pool<List<Exception>> exceptionListPool;
        private List<Exception> exceptions;
        private final List<DataFetcher<Data>> fetchers;
        private Priority priority;

        MultiFetcher(List<DataFetcher<Data>> list, Pool<List<Exception>> pool) {
            this.exceptionListPool = pool;
            Preconditions.checkNotEmpty((Collection) list);
            this.fetchers = list;
        }

        public void loadData(Priority priority, DataCallback<? super Data> dataCallback) {
            this.priority = priority;
            this.callback = dataCallback;
            this.exceptions = (List) this.exceptionListPool.acquire();
            ((DataFetcher) this.fetchers.get(this.currentIndex)).loadData(priority, this);
        }

        public void cleanup() {
            if (this.exceptions != null) {
                this.exceptionListPool.release(this.exceptions);
            }
            this.exceptions = null;
            for (DataFetcher cleanup : this.fetchers) {
                cleanup.cleanup();
            }
        }

        public void cancel() {
            for (DataFetcher cancel : this.fetchers) {
                cancel.cancel();
            }
        }

        public Class<Data> getDataClass() {
            return ((DataFetcher) this.fetchers.get(0)).getDataClass();
        }

        public DataSource getDataSource() {
            return ((DataFetcher) this.fetchers.get(0)).getDataSource();
        }

        public void onDataReady(Data data) {
            if (data != null) {
                this.callback.onDataReady(data);
            } else {
                startNextOrFail();
            }
        }

        public void onLoadFailed(Exception exception) {
            this.exceptions.add(exception);
            startNextOrFail();
        }

        private void startNextOrFail() {
            if (this.currentIndex < this.fetchers.size() - 1) {
                this.currentIndex++;
                loadData(this.priority, this.callback);
                return;
            }
            this.callback.onLoadFailed(new GlideException("Fetch failed", new ArrayList(this.exceptions)));
        }
    }

    MultiModelLoader(List<ModelLoader<Model, Data>> list, Pool<List<Exception>> pool) {
        this.modelLoaders = list;
        this.exceptionListPool = pool;
    }

    public LoadData<Data> buildLoadData(Model model, int i, int i2, Options options) {
        int size = this.modelLoaders.size();
        ArrayList arrayList = new ArrayList(size);
        Key key = null;
        for (int i3 = 0; i3 < size; i3++) {
            ModelLoader modelLoader = (ModelLoader) this.modelLoaders.get(i3);
            if (modelLoader.handles(model)) {
                LoadData buildLoadData = modelLoader.buildLoadData(model, i, i2, options);
                if (buildLoadData != null) {
                    key = buildLoadData.sourceKey;
                    arrayList.add(buildLoadData.fetcher);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new LoadData(key, new MultiFetcher(arrayList, this.exceptionListPool));
    }

    public boolean handles(Model model) {
        for (ModelLoader handles : this.modelLoaders) {
            if (handles.handles(model)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MultiModelLoader{modelLoaders=");
        stringBuilder.append(Arrays.toString(this.modelLoaders.toArray(new ModelLoader[this.modelLoaders.size()])));
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
