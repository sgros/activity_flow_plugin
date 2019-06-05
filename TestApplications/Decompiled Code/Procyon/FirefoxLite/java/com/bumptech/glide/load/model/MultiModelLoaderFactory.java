// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import com.bumptech.glide.load.Options;
import java.util.Iterator;
import com.bumptech.glide.Registry;
import com.bumptech.glide.util.Preconditions;
import java.util.HashSet;
import java.util.ArrayList;
import android.support.v4.util.Pools;
import java.util.List;
import java.util.Set;

public class MultiModelLoaderFactory
{
    private static final Factory DEFAULT_FACTORY;
    private static final ModelLoader<Object, Object> EMPTY_MODEL_LOADER;
    private final Set<Entry<?, ?>> alreadyUsedEntries;
    private final List<Entry<?, ?>> entries;
    private final Pools.Pool<List<Exception>> exceptionListPool;
    private final Factory factory;
    
    static {
        DEFAULT_FACTORY = new Factory();
        EMPTY_MODEL_LOADER = new EmptyModelLoader();
    }
    
    public MultiModelLoaderFactory(final Pools.Pool<List<Exception>> pool) {
        this(pool, MultiModelLoaderFactory.DEFAULT_FACTORY);
    }
    
    MultiModelLoaderFactory(final Pools.Pool<List<Exception>> exceptionListPool, final Factory factory) {
        this.entries = new ArrayList<Entry<?, ?>>();
        this.alreadyUsedEntries = new HashSet<Entry<?, ?>>();
        this.exceptionListPool = exceptionListPool;
        this.factory = factory;
    }
    
    private <Model, Data> void add(final Class<Model> clazz, final Class<Data> clazz2, final ModelLoaderFactory<Model, Data> modelLoaderFactory, final boolean b) {
        final Entry<Model, Data> entry = new Entry<Model, Data>(clazz, clazz2, modelLoaderFactory);
        final List<Entry<?, ?>> entries = this.entries;
        int size;
        if (b) {
            size = this.entries.size();
        }
        else {
            size = 0;
        }
        entries.add(size, entry);
    }
    
    private <Model, Data> ModelLoader<Model, Data> build(final Entry<?, ?> entry) {
        return Preconditions.checkNotNull(entry.factory.build(this));
    }
    
    private static <Model, Data> ModelLoader<Model, Data> emptyModelLoader() {
        return (ModelLoader<Model, Data>)MultiModelLoaderFactory.EMPTY_MODEL_LOADER;
    }
    
     <Model, Data> void append(final Class<Model> clazz, final Class<Data> clazz2, final ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        synchronized (this) {
            this.add(clazz, clazz2, modelLoaderFactory, true);
        }
    }
    
    public <Model, Data> ModelLoader<Model, Data> build(final Class<Model> clazz, final Class<Data> clazz2) {
        // monitorenter(this)
        try {
            final ArrayList<ModelLoader<Model, Data>> list = new ArrayList<ModelLoader<Model, Data>>();
            final Iterator<Entry<?, ?>> iterator = this.entries.iterator();
            boolean b = false;
            while (iterator.hasNext()) {
                final Entry<?, ?> entry = iterator.next();
                if (this.alreadyUsedEntries.contains(entry)) {
                    b = true;
                }
                else {
                    if (!entry.handles(clazz, clazz2)) {
                        continue;
                    }
                    this.alreadyUsedEntries.add(entry);
                    list.add(this.build(entry));
                    this.alreadyUsedEntries.remove(entry);
                }
            }
            if (list.size() > 1) {
                // monitorexit(this)
                return this.factory.build(list, this.exceptionListPool);
            }
            if (list.size() == 1) {
                // monitorexit(this)
                return list.get(0);
            }
            if (b) {
                // monitorexit(this)
                return emptyModelLoader();
            }
            throw new Registry.NoModelLoaderAvailableException(clazz, clazz2);
        }
        catch (Throwable t) {
            this.alreadyUsedEntries.clear();
            throw t;
        }
    }
    // monitorexit(this)
    
     <Model> List<ModelLoader<Model, ?>> build(final Class<Model> clazz) {
        // monitorenter(this)
        try {
            try {
                final ArrayList<ModelLoader<Object, Object>> list = new ArrayList<ModelLoader<Object, Object>>();
                for (final Entry<?, ?> entry : this.entries) {
                    if (this.alreadyUsedEntries.contains(entry)) {
                        continue;
                    }
                    if (!entry.handles(clazz)) {
                        continue;
                    }
                    this.alreadyUsedEntries.add(entry);
                    list.add(this.build(entry));
                    this.alreadyUsedEntries.remove(entry);
                }
                // monitorexit(this)
                return (List<ModelLoader<Model, ?>>)list;
            }
            finally {}
        }
        catch (Throwable t) {
            this.alreadyUsedEntries.clear();
            throw t;
        }
    }
    // monitorexit(this)
    
    List<Class<?>> getDataClasses(final Class<?> clazz) {
        synchronized (this) {
            final ArrayList<Class<Data>> list = new ArrayList<Class<Data>>();
            for (final Entry<?, ?> entry : this.entries) {
                if (!list.contains(entry.dataClass) && entry.handles(clazz)) {
                    list.add((Class<Data>)entry.dataClass);
                }
            }
            return (List<Class<?>>)list;
        }
    }
    
     <Model, Data> void prepend(final Class<Model> clazz, final Class<Data> clazz2, final ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        synchronized (this) {
            this.add(clazz, clazz2, modelLoaderFactory, false);
        }
    }
    
    private static class EmptyModelLoader implements ModelLoader<Object, Object>
    {
        EmptyModelLoader() {
        }
        
        @Override
        public LoadData<Object> buildLoadData(final Object o, final int n, final int n2, final Options options) {
            return null;
        }
        
        @Override
        public boolean handles(final Object o) {
            return false;
        }
    }
    
    private static class Entry<Model, Data>
    {
        final Class<Data> dataClass;
        final ModelLoaderFactory<Model, Data> factory;
        private final Class<Model> modelClass;
        
        public Entry(final Class<Model> modelClass, final Class<Data> dataClass, final ModelLoaderFactory<Model, Data> factory) {
            this.modelClass = modelClass;
            this.dataClass = dataClass;
            this.factory = factory;
        }
        
        public boolean handles(final Class<?> clazz) {
            return this.modelClass.isAssignableFrom(clazz);
        }
        
        public boolean handles(final Class<?> clazz, final Class<?> clazz2) {
            return this.handles(clazz) && this.dataClass.isAssignableFrom(clazz2);
        }
    }
    
    static class Factory
    {
        public <Model, Data> MultiModelLoader<Model, Data> build(final List<ModelLoader<Model, Data>> list, final Pools.Pool<List<Exception>> pool) {
            return new MultiModelLoader<Model, Data>(list, pool);
        }
    }
}
