// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.support.v4.util.Pools;

public class ModelLoaderRegistry
{
    private final ModelLoaderCache cache;
    private final MultiModelLoaderFactory multiModelLoaderFactory;
    
    public ModelLoaderRegistry(final Pools.Pool<List<Exception>> pool) {
        this(new MultiModelLoaderFactory(pool));
    }
    
    ModelLoaderRegistry(final MultiModelLoaderFactory multiModelLoaderFactory) {
        this.cache = new ModelLoaderCache();
        this.multiModelLoaderFactory = multiModelLoaderFactory;
    }
    
    private static <A> Class<A> getClass(final A a) {
        return (Class<A>)a.getClass();
    }
    
    private <A> List<ModelLoader<A, ?>> getModelLoadersForClass(final Class<A> clazz) {
        List<ModelLoader<A, ?>> list;
        if ((list = this.cache.get(clazz)) == null) {
            list = Collections.unmodifiableList((List<? extends ModelLoader<A, ?>>)this.multiModelLoaderFactory.build(clazz));
            this.cache.put(clazz, list);
        }
        return list;
    }
    
    public <Model, Data> void append(final Class<Model> clazz, final Class<Data> clazz2, final ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        synchronized (this) {
            this.multiModelLoaderFactory.append(clazz, clazz2, modelLoaderFactory);
            this.cache.clear();
        }
    }
    
    public List<Class<?>> getDataClasses(final Class<?> clazz) {
        synchronized (this) {
            return this.multiModelLoaderFactory.getDataClasses(clazz);
        }
    }
    
    public <A> List<ModelLoader<A, ?>> getModelLoaders(final A a) {
        synchronized (this) {
            final List<ModelLoader<A, ?>> modelLoadersForClass = this.getModelLoadersForClass((Class<A>)getClass((A)a));
            final int size = modelLoadersForClass.size();
            final ArrayList list = new ArrayList<ModelLoader<A, Data>>(size);
            for (int i = 0; i < size; ++i) {
                final ModelLoader<A, Data> modelLoader = modelLoadersForClass.get(i);
                if (modelLoader.handles(a)) {
                    list.add(modelLoader);
                }
            }
            return (List<ModelLoader<A, ?>>)list;
        }
    }
    
    public <Model, Data> void prepend(final Class<Model> clazz, final Class<Data> clazz2, final ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        synchronized (this) {
            this.multiModelLoaderFactory.prepend(clazz, clazz2, modelLoaderFactory);
            this.cache.clear();
        }
    }
    
    private static class ModelLoaderCache
    {
        private final Map<Class<?>, Entry<?>> cachedModelLoaders;
        
        ModelLoaderCache() {
            this.cachedModelLoaders = new HashMap<Class<?>, Entry<?>>();
        }
        
        public void clear() {
            this.cachedModelLoaders.clear();
        }
        
        public <Model> List<ModelLoader<Model, ?>> get(final Class<Model> clazz) {
            final Entry<?> entry = this.cachedModelLoaders.get(clazz);
            Object loaders;
            if (entry == null) {
                loaders = null;
            }
            else {
                loaders = entry.loaders;
            }
            return (List<ModelLoader<Model, ?>>)loaders;
        }
        
        public <Model> void put(final Class<Model> obj, final List<ModelLoader<Model, ?>> list) {
            if (this.cachedModelLoaders.put(obj, new Entry<Object>((List<ModelLoader<?, ?>>)list)) == null) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Already cached loaders for model: ");
            sb.append(obj);
            throw new IllegalStateException(sb.toString());
        }
        
        private static class Entry<Model>
        {
            final List<ModelLoader<Model, ?>> loaders;
            
            public Entry(final List<ModelLoader<Model, ?>> loaders) {
                this.loaders = loaders;
            }
        }
    }
}
