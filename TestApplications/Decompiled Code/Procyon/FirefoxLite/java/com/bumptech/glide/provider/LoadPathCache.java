// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.provider;

import java.util.concurrent.atomic.AtomicReference;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.util.MultiClassKey;
import android.support.v4.util.ArrayMap;

public class LoadPathCache
{
    private final ArrayMap<MultiClassKey, LoadPath<?, ?, ?>> cache;
    private final AtomicReference<MultiClassKey> keyRef;
    
    public LoadPathCache() {
        this.cache = new ArrayMap<MultiClassKey, LoadPath<?, ?, ?>>();
        this.keyRef = new AtomicReference<MultiClassKey>();
    }
    
    private MultiClassKey getKey(final Class<?> clazz, final Class<?> clazz2, final Class<?> clazz3) {
        MultiClassKey multiClassKey;
        if ((multiClassKey = this.keyRef.getAndSet(null)) == null) {
            multiClassKey = new MultiClassKey();
        }
        multiClassKey.set(clazz, clazz2, clazz3);
        return multiClassKey;
    }
    
    public boolean contains(final Class<?> clazz, final Class<?> clazz2, final Class<?> clazz3) {
        final MultiClassKey key = this.getKey(clazz, clazz2, clazz3);
        synchronized (this.cache) {
            final boolean containsKey = this.cache.containsKey(key);
            // monitorexit(this.cache)
            this.keyRef.set(key);
            return containsKey;
        }
    }
    
    public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> get(final Class<Data> clazz, final Class<TResource> clazz2, final Class<Transcode> clazz3) {
        final MultiClassKey key = this.getKey(clazz, clazz2, clazz3);
        synchronized (this.cache) {
            final LoadPath<?, ?, ?> loadPath = this.cache.get(key);
            // monitorexit(this.cache)
            this.keyRef.set(key);
            return (LoadPath<Data, TResource, Transcode>)loadPath;
        }
    }
    
    public void put(final Class<?> clazz, final Class<?> clazz2, final Class<?> clazz3, final LoadPath<?, ?, ?> loadPath) {
        synchronized (this.cache) {
            this.cache.put(new MultiClassKey(clazz, clazz2, clazz3), loadPath);
        }
    }
}
