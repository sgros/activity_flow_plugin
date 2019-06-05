// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.load.Options;

public class UnitModelLoader<Model> implements ModelLoader<Model, Model>
{
    @Override
    public LoadData<Model> buildLoadData(final Model model, final int n, final int n2, final Options options) {
        return (LoadData<Model>)new LoadData(new ObjectKey(model), new UnitFetcher<Object>(model));
    }
    
    @Override
    public boolean handles(final Model model) {
        return true;
    }
    
    public static class Factory<Model> implements ModelLoaderFactory<Model, Model>
    {
        @Override
        public ModelLoader<Model, Model> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new UnitModelLoader<Model>();
        }
    }
    
    private static class UnitFetcher<Model> implements DataFetcher<Model>
    {
        private final Model resource;
        
        public UnitFetcher(final Model resource) {
            this.resource = resource;
        }
        
        @Override
        public void cancel() {
        }
        
        @Override
        public void cleanup() {
        }
        
        @Override
        public Class<Model> getDataClass() {
            return (Class<Model>)this.resource.getClass();
        }
        
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
        
        @Override
        public void loadData(final Priority priority, final DataCallback<? super Model> dataCallback) {
            dataCallback.onDataReady(this.resource);
        }
    }
}
