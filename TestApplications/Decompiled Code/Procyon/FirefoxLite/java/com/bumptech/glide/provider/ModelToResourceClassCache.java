// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.provider;

import java.util.concurrent.atomic.AtomicReference;
import java.util.List;
import com.bumptech.glide.util.MultiClassKey;
import android.support.v4.util.ArrayMap;

public class ModelToResourceClassCache
{
    private final ArrayMap<MultiClassKey, List<Class<?>>> registeredResourceClassCache;
    private final AtomicReference<MultiClassKey> resourceClassKeyRef;
    
    public ModelToResourceClassCache() {
        this.resourceClassKeyRef = new AtomicReference<MultiClassKey>();
        this.registeredResourceClassCache = new ArrayMap<MultiClassKey, List<Class<?>>>();
    }
    
    public List<Class<?>> get(final Class<?> clazz, final Class<?> clazz2) {
        final MultiClassKey multiClassKey = this.resourceClassKeyRef.getAndSet(null);
        MultiClassKey newValue;
        if (multiClassKey == null) {
            newValue = new MultiClassKey(clazz, clazz2);
        }
        else {
            multiClassKey.set(clazz, clazz2);
            newValue = multiClassKey;
        }
        synchronized (this.registeredResourceClassCache) {
            final List<Class<?>> list = this.registeredResourceClassCache.get(newValue);
            // monitorexit(this.registeredResourceClassCache)
            this.resourceClassKeyRef.set(newValue);
            return list;
        }
    }
    
    public void put(final Class<?> clazz, final Class<?> clazz2, final List<Class<?>> list) {
        synchronized (this.registeredResourceClassCache) {
            this.registeredResourceClassCache.put(new MultiClassKey(clazz, clazz2), list);
        }
    }
}
