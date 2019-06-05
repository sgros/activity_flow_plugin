package org.mozilla.focus.tabs.tabtray;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataFetcher.DataCallback;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

public class FaviconModelLoaderFactory implements ModelLoaderFactory<FaviconModel, FaviconModel> {

    public static class FaviconModelLoader implements ModelLoader<FaviconModel, FaviconModel> {
        public boolean handles(FaviconModel faviconModel) {
            return true;
        }

        public LoadData<FaviconModel> buildLoadData(FaviconModel faviconModel, int i, int i2, Options options) {
            return new LoadData(new ObjectKey(faviconModel.url), new Fetcher(faviconModel));
        }
    }

    public static class Fetcher implements DataFetcher<FaviconModel> {
        private FaviconModel model;

        public void cancel() {
        }

        public void cleanup() {
        }

        Fetcher(FaviconModel faviconModel) {
            this.model = faviconModel;
        }

        public void loadData(Priority priority, DataCallback<? super FaviconModel> dataCallback) {
            dataCallback.onDataReady(this.model);
        }

        public Class<FaviconModel> getDataClass() {
            return FaviconModel.class;
        }

        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }

    public ModelLoader<FaviconModel, FaviconModel> build(MultiModelLoaderFactory multiModelLoaderFactory) {
        return new FaviconModelLoader();
    }
}
