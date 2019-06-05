package com.bumptech.glide.load.model;

import android.support.p001v4.util.Pools.Pool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelLoaderRegistry {
    private final ModelLoaderCache cache;
    private final MultiModelLoaderFactory multiModelLoaderFactory;

    private static class ModelLoaderCache {
        private final Map<Class<?>, Entry<?>> cachedModelLoaders = new HashMap();

        private static class Entry<Model> {
            final List<ModelLoader<Model, ?>> loaders;

            public Entry(List<ModelLoader<Model, ?>> list) {
                this.loaders = list;
            }
        }

        ModelLoaderCache() {
        }

        public void clear() {
            this.cachedModelLoaders.clear();
        }

        public <Model> void put(Class<Model> cls, List<ModelLoader<Model, ?>> list) {
            if (((Entry) this.cachedModelLoaders.put(cls, new Entry(list))) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Already cached loaders for model: ");
                stringBuilder.append(cls);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }

        public <Model> List<ModelLoader<Model, ?>> get(Class<Model> cls) {
            Entry entry = (Entry) this.cachedModelLoaders.get(cls);
            if (entry == null) {
                return null;
            }
            return entry.loaders;
        }
    }

    public ModelLoaderRegistry(Pool<List<Exception>> pool) {
        this(new MultiModelLoaderFactory(pool));
    }

    ModelLoaderRegistry(MultiModelLoaderFactory multiModelLoaderFactory) {
        this.cache = new ModelLoaderCache();
        this.multiModelLoaderFactory = multiModelLoaderFactory;
    }

    public synchronized <Model, Data> void append(Class<Model> cls, Class<Data> cls2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        this.multiModelLoaderFactory.append(cls, cls2, modelLoaderFactory);
        this.cache.clear();
    }

    public synchronized <Model, Data> void prepend(Class<Model> cls, Class<Data> cls2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        this.multiModelLoaderFactory.prepend(cls, cls2, modelLoaderFactory);
        this.cache.clear();
    }

    public synchronized <A> List<ModelLoader<A, ?>> getModelLoaders(A a) {
        ArrayList arrayList;
        List modelLoadersForClass = getModelLoadersForClass(getClass(a));
        int size = modelLoadersForClass.size();
        arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            ModelLoader modelLoader = (ModelLoader) modelLoadersForClass.get(i);
            if (modelLoader.handles(a)) {
                arrayList.add(modelLoader);
            }
        }
        return arrayList;
    }

    public synchronized List<Class<?>> getDataClasses(Class<?> cls) {
        return this.multiModelLoaderFactory.getDataClasses(cls);
    }

    private <A> List<ModelLoader<A, ?>> getModelLoadersForClass(Class<A> cls) {
        List<ModelLoader<A, ?>> list = this.cache.get(cls);
        if (list != null) {
            return list;
        }
        List unmodifiableList = Collections.unmodifiableList(this.multiModelLoaderFactory.build((Class) cls));
        this.cache.put(cls, unmodifiableList);
        return unmodifiableList;
    }

    private static <A> Class<A> getClass(A a) {
        return a.getClass();
    }
}
