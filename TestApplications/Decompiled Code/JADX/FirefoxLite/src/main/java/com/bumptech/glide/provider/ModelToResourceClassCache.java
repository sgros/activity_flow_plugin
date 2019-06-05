package com.bumptech.glide.provider;

import android.support.p001v4.util.ArrayMap;
import com.bumptech.glide.util.MultiClassKey;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ModelToResourceClassCache {
    private final ArrayMap<MultiClassKey, List<Class<?>>> registeredResourceClassCache = new ArrayMap();
    private final AtomicReference<MultiClassKey> resourceClassKeyRef = new AtomicReference();

    public List<Class<?>> get(Class<?> cls, Class<?> cls2) {
        List list;
        Object obj = (MultiClassKey) this.resourceClassKeyRef.getAndSet(null);
        if (obj == null) {
            obj = new MultiClassKey(cls, cls2);
        } else {
            obj.set(cls, cls2);
        }
        synchronized (this.registeredResourceClassCache) {
            list = (List) this.registeredResourceClassCache.get(obj);
        }
        this.resourceClassKeyRef.set(obj);
        return list;
    }

    public void put(Class<?> cls, Class<?> cls2, List<Class<?>> list) {
        synchronized (this.registeredResourceClassCache) {
            this.registeredResourceClassCache.put(new MultiClassKey(cls, cls2), list);
        }
    }
}
