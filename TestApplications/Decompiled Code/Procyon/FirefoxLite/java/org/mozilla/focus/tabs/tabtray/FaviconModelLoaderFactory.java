// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderFactory;

public class FaviconModelLoaderFactory implements ModelLoaderFactory<FaviconModel, FaviconModel>
{
    @Override
    public ModelLoader<FaviconModel, FaviconModel> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
        return new FaviconModelLoader();
    }
    
    public static class FaviconModelLoader implements ModelLoader<FaviconModel, FaviconModel>
    {
        public LoadData<FaviconModel> buildLoadData(final FaviconModel faviconModel, final int n, final int n2, final Options options) {
            return (LoadData<FaviconModel>)new LoadData(new ObjectKey(faviconModel.url), (DataFetcher<Object>)new Fetcher(faviconModel));
        }
        
        @Override
        public boolean handles(final FaviconModel faviconModel) {
            return true;
        }
    }
    
    public static class Fetcher implements DataFetcher<FaviconModel>
    {
        private FaviconModel model;
        
        Fetcher(final FaviconModel model) {
            this.model = model;
        }
        
        @Override
        public void cancel() {
        }
        
        @Override
        public void cleanup() {
        }
        
        @Override
        public Class<FaviconModel> getDataClass() {
            return FaviconModel.class;
        }
        
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
        
        @Override
        public void loadData(final Priority priority, final DataCallback<? super FaviconModel> dataCallback) {
            dataCallback.onDataReady(this.model);
        }
    }
}
