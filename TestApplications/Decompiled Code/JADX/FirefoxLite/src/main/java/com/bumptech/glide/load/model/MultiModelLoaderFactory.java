package com.bumptech.glide.load.model;

import android.support.p001v4.util.Pools.Pool;
import com.bumptech.glide.Registry.NoModelLoaderAvailableException;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiModelLoaderFactory {
    private static final Factory DEFAULT_FACTORY = new Factory();
    private static final ModelLoader<Object, Object> EMPTY_MODEL_LOADER = new EmptyModelLoader();
    private final Set<Entry<?, ?>> alreadyUsedEntries;
    private final List<Entry<?, ?>> entries;
    private final Pool<List<Exception>> exceptionListPool;
    private final Factory factory;

    private static class Entry<Model, Data> {
        final Class<Data> dataClass;
        final ModelLoaderFactory<Model, Data> factory;
        private final Class<Model> modelClass;

        public Entry(Class<Model> cls, Class<Data> cls2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
            this.modelClass = cls;
            this.dataClass = cls2;
            this.factory = modelLoaderFactory;
        }

        public boolean handles(Class<?> cls, Class<?> cls2) {
            return handles(cls) && this.dataClass.isAssignableFrom(cls2);
        }

        public boolean handles(Class<?> cls) {
            return this.modelClass.isAssignableFrom(cls);
        }
    }

    static class Factory {
        Factory() {
        }

        public <Model, Data> MultiModelLoader<Model, Data> build(List<ModelLoader<Model, Data>> list, Pool<List<Exception>> pool) {
            return new MultiModelLoader(list, pool);
        }
    }

    private static class EmptyModelLoader implements ModelLoader<Object, Object> {
        public LoadData<Object> buildLoadData(Object obj, int i, int i2, Options options) {
            return null;
        }

        public boolean handles(Object obj) {
            return false;
        }

        EmptyModelLoader() {
        }
    }

    public MultiModelLoaderFactory(Pool<List<Exception>> pool) {
        this(pool, DEFAULT_FACTORY);
    }

    MultiModelLoaderFactory(Pool<List<Exception>> pool, Factory factory) {
        this.entries = new ArrayList();
        this.alreadyUsedEntries = new HashSet();
        this.exceptionListPool = pool;
        this.factory = factory;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized <Model, Data> void append(Class<Model> cls, Class<Data> cls2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        add(cls, cls2, modelLoaderFactory, true);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized <Model, Data> void prepend(Class<Model> cls, Class<Data> cls2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        add(cls, cls2, modelLoaderFactory, false);
    }

    private <Model, Data> void add(Class<Model> cls, Class<Data> cls2, ModelLoaderFactory<Model, Data> modelLoaderFactory, boolean z) {
        this.entries.add(z ? this.entries.size() : 0, new Entry(cls, cls2, modelLoaderFactory));
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized <Model> List<ModelLoader<Model, ?>> build(Class<Model> cls) {
        ArrayList arrayList;
        try {
            arrayList = new ArrayList();
            for (Entry entry : this.entries) {
                if (!this.alreadyUsedEntries.contains(entry)) {
                    if (entry.handles(cls)) {
                        this.alreadyUsedEntries.add(entry);
                        arrayList.add(build(entry));
                        this.alreadyUsedEntries.remove(entry);
                    }
                }
            }
        } catch (Throwable th) {
            this.alreadyUsedEntries.clear();
        }
        return arrayList;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized List<Class<?>> getDataClasses(Class<?> cls) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (Entry entry : this.entries) {
            if (!arrayList.contains(entry.dataClass) && entry.handles(cls)) {
                arrayList.add(entry.dataClass);
            }
        }
        return arrayList;
    }

    public synchronized <Model, Data> ModelLoader<Model, Data> build(Class<Model> cls, Class<Data> cls2) {
        try {
            ArrayList arrayList = new ArrayList();
            Object obj = null;
            for (Entry entry : this.entries) {
                if (this.alreadyUsedEntries.contains(entry)) {
                    obj = 1;
                } else if (entry.handles(cls, cls2)) {
                    this.alreadyUsedEntries.add(entry);
                    arrayList.add(build(entry));
                    this.alreadyUsedEntries.remove(entry);
                }
            }
            if (arrayList.size() > 1) {
                return this.factory.build(arrayList, this.exceptionListPool);
            } else if (arrayList.size() == 1) {
                return (ModelLoader) arrayList.get(0);
            } else if (obj != null) {
                return emptyModelLoader();
            } else {
                throw new NoModelLoaderAvailableException(cls, cls2);
            }
        } catch (Throwable th) {
            this.alreadyUsedEntries.clear();
        }
    }

    private <Model, Data> ModelLoader<Model, Data> build(Entry<?, ?> entry) {
        return (ModelLoader) Preconditions.checkNotNull(entry.factory.build(this));
    }

    private static <Model, Data> ModelLoader<Model, Data> emptyModelLoader() {
        return EMPTY_MODEL_LOADER;
    }
}
