// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.engine.cache.DiskCacheAdapter;
import com.bumptech.glide.util.pool.FactoryPools;
import android.support.v4.util.Pools;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.Priority;
import com.bumptech.glide.GlideContext;
import android.util.Log;
import com.bumptech.glide.util.LogTime;
import android.os.MessageQueue$IdleHandler;
import android.os.Looper;
import java.util.HashMap;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.engine.cache.DiskCache;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import com.bumptech.glide.load.Key;
import java.util.Map;
import com.bumptech.glide.load.engine.cache.MemoryCache;

public class Engine implements EngineJobListener, ResourceListener, ResourceRemovedListener
{
    private final Map<Key, WeakReference<EngineResource<?>>> activeResources;
    private final MemoryCache cache;
    private final DecodeJobFactory decodeJobFactory;
    private final LazyDiskCacheProvider diskCacheProvider;
    private final EngineJobFactory engineJobFactory;
    private final Map<Key, EngineJob<?>> jobs;
    private final EngineKeyFactory keyFactory;
    private final ResourceRecycler resourceRecycler;
    private ReferenceQueue<EngineResource<?>> resourceReferenceQueue;
    
    public Engine(final MemoryCache memoryCache, final DiskCache.Factory factory, final GlideExecutor glideExecutor, final GlideExecutor glideExecutor2, final GlideExecutor glideExecutor3) {
        this(memoryCache, factory, glideExecutor, glideExecutor2, glideExecutor3, null, null, null, null, null, null);
    }
    
    Engine(final MemoryCache cache, final DiskCache.Factory factory, final GlideExecutor glideExecutor, final GlideExecutor glideExecutor2, final GlideExecutor glideExecutor3, final Map<Key, EngineJob<?>> map, final EngineKeyFactory engineKeyFactory, final Map<Key, WeakReference<EngineResource<?>>> map2, final EngineJobFactory engineJobFactory, final DecodeJobFactory decodeJobFactory, final ResourceRecycler resourceRecycler) {
        this.cache = cache;
        this.diskCacheProvider = new LazyDiskCacheProvider(factory);
        Map<Key, WeakReference<EngineResource<?>>> activeResources = map2;
        if (map2 == null) {
            activeResources = new HashMap<Key, WeakReference<EngineResource<?>>>();
        }
        this.activeResources = activeResources;
        EngineKeyFactory keyFactory;
        if ((keyFactory = engineKeyFactory) == null) {
            keyFactory = new EngineKeyFactory();
        }
        this.keyFactory = keyFactory;
        Map<Key, EngineJob<?>> jobs;
        if ((jobs = map) == null) {
            jobs = new HashMap<Key, EngineJob<?>>();
        }
        this.jobs = jobs;
        EngineJobFactory engineJobFactory2;
        if ((engineJobFactory2 = engineJobFactory) == null) {
            engineJobFactory2 = new EngineJobFactory(glideExecutor, glideExecutor2, glideExecutor3, this);
        }
        this.engineJobFactory = engineJobFactory2;
        DecodeJobFactory decodeJobFactory2;
        if ((decodeJobFactory2 = decodeJobFactory) == null) {
            decodeJobFactory2 = new DecodeJobFactory(this.diskCacheProvider);
        }
        this.decodeJobFactory = decodeJobFactory2;
        ResourceRecycler resourceRecycler2;
        if ((resourceRecycler2 = resourceRecycler) == null) {
            resourceRecycler2 = new ResourceRecycler();
        }
        this.resourceRecycler = resourceRecycler2;
        cache.setResourceRemovedListener((MemoryCache.ResourceRemovedListener)this);
    }
    
    private EngineResource<?> getEngineResourceFromCache(final Key key) {
        final Resource<?> remove = this.cache.remove(key);
        EngineResource<?> engineResource;
        if (remove == null) {
            engineResource = null;
        }
        else if (remove instanceof EngineResource) {
            engineResource = (EngineResource<?>)remove;
        }
        else {
            engineResource = new EngineResource<Object>(remove, true);
        }
        return engineResource;
    }
    
    private ReferenceQueue<EngineResource<?>> getReferenceQueue() {
        if (this.resourceReferenceQueue == null) {
            this.resourceReferenceQueue = new ReferenceQueue<EngineResource<?>>();
            Looper.myQueue().addIdleHandler((MessageQueue$IdleHandler)new RefQueueIdleHandler(this.activeResources, this.resourceReferenceQueue));
        }
        return this.resourceReferenceQueue;
    }
    
    private EngineResource<?> loadFromActiveResources(final Key key, final boolean b) {
        EngineResource<?> engineResource = null;
        if (!b) {
            return null;
        }
        final WeakReference<EngineResource<?>> weakReference = this.activeResources.get(key);
        if (weakReference != null) {
            engineResource = weakReference.get();
            if (engineResource != null) {
                engineResource.acquire();
            }
            else {
                this.activeResources.remove(key);
            }
        }
        return engineResource;
    }
    
    private EngineResource<?> loadFromCache(final Key key, final boolean b) {
        if (!b) {
            return null;
        }
        final EngineResource<?> engineResourceFromCache = this.getEngineResourceFromCache(key);
        if (engineResourceFromCache != null) {
            engineResourceFromCache.acquire();
            this.activeResources.put(key, new ResourceWeakReference(key, engineResourceFromCache, this.getReferenceQueue()));
        }
        return engineResourceFromCache;
    }
    
    private static void logWithTimeAndKey(final String str, final long n, final Key obj) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" in ");
        sb.append(LogTime.getElapsedMillis(n));
        sb.append("ms, key: ");
        sb.append(obj);
        Log.v("Engine", sb.toString());
    }
    
    public <R> LoadStatus load(final GlideContext glideContext, final Object o, final Key key, final int n, final int n2, final Class<?> clazz, final Class<R> clazz2, final Priority priority, final DiskCacheStrategy diskCacheStrategy, final Map<Class<?>, Transformation<?>> map, final boolean b, final boolean b2, final Options options, final boolean b3, final boolean b4, final boolean b5, final ResourceCallback resourceCallback) {
        Util.assertMainThread();
        final long logTime = LogTime.getLogTime();
        final EngineKey buildKey = this.keyFactory.buildKey(o, key, n, n2, map, clazz, clazz2, options);
        final EngineResource<?> loadFromCache = this.loadFromCache(buildKey, b3);
        if (loadFromCache != null) {
            resourceCallback.onResourceReady(loadFromCache, DataSource.MEMORY_CACHE);
            if (Log.isLoggable("Engine", 2)) {
                logWithTimeAndKey("Loaded resource from cache", logTime, buildKey);
            }
            return null;
        }
        final EngineResource<?> loadFromActiveResources = this.loadFromActiveResources(buildKey, b3);
        if (loadFromActiveResources != null) {
            resourceCallback.onResourceReady(loadFromActiveResources, DataSource.MEMORY_CACHE);
            if (Log.isLoggable("Engine", 2)) {
                logWithTimeAndKey("Loaded resource from active resources", logTime, buildKey);
            }
            return null;
        }
        final EngineJob<?> engineJob = this.jobs.get(buildKey);
        if (engineJob != null) {
            engineJob.addCallback(resourceCallback);
            if (Log.isLoggable("Engine", 2)) {
                logWithTimeAndKey("Added to existing load", logTime, buildKey);
            }
            return new LoadStatus(resourceCallback, engineJob);
        }
        final EngineJob<Object> build = this.engineJobFactory.build(buildKey, b3, b4);
        final DecodeJob<R> build2 = this.decodeJobFactory.build(glideContext, o, buildKey, key, n, n2, clazz, clazz2, priority, diskCacheStrategy, map, b, b2, b5, options, (DecodeJob.Callback<R>)build);
        this.jobs.put(buildKey, build);
        build.addCallback(resourceCallback);
        build.start((DecodeJob<Object>)build2);
        if (Log.isLoggable("Engine", 2)) {
            logWithTimeAndKey("Started new load", logTime, buildKey);
        }
        return new LoadStatus(resourceCallback, build);
    }
    
    @Override
    public void onEngineJobCancelled(final EngineJob engineJob, final Key key) {
        Util.assertMainThread();
        if (engineJob.equals(this.jobs.get(key))) {
            this.jobs.remove(key);
        }
    }
    
    @Override
    public void onEngineJobComplete(final Key key, final EngineResource<?> engineResource) {
        Util.assertMainThread();
        if (engineResource != null) {
            engineResource.setResourceListener(key, (EngineResource.ResourceListener)this);
            if (engineResource.isCacheable()) {
                this.activeResources.put(key, new ResourceWeakReference(key, engineResource, this.getReferenceQueue()));
            }
        }
        this.jobs.remove(key);
    }
    
    @Override
    public void onResourceReleased(final Key key, final EngineResource engineResource) {
        Util.assertMainThread();
        this.activeResources.remove(key);
        if (engineResource.isCacheable()) {
            this.cache.put(key, engineResource);
        }
        else {
            this.resourceRecycler.recycle(engineResource);
        }
    }
    
    @Override
    public void onResourceRemoved(final Resource<?> resource) {
        Util.assertMainThread();
        this.resourceRecycler.recycle(resource);
    }
    
    public void release(final Resource<?> resource) {
        Util.assertMainThread();
        if (resource instanceof EngineResource) {
            ((EngineResource)resource).release();
            return;
        }
        throw new IllegalArgumentException("Cannot release anything but an EngineResource");
    }
    
    static class DecodeJobFactory
    {
        private int creationOrder;
        final DecodeJob.DiskCacheProvider diskCacheProvider;
        final Pools.Pool<DecodeJob<?>> pool;
        
        DecodeJobFactory(final DecodeJob.DiskCacheProvider diskCacheProvider) {
            this.pool = FactoryPools.simple(150, (FactoryPools.Factory<DecodeJob<?>>)new FactoryPools.Factory<DecodeJob<?>>() {
                public DecodeJob<?> create() {
                    return new DecodeJob<Object>(DecodeJobFactory.this.diskCacheProvider, DecodeJobFactory.this.pool);
                }
            });
            this.diskCacheProvider = diskCacheProvider;
        }
        
         <R> DecodeJob<R> build(final GlideContext glideContext, final Object o, final EngineKey engineKey, final Key key, final int n, final int n2, final Class<?> clazz, final Class<R> clazz2, final Priority priority, final DiskCacheStrategy diskCacheStrategy, final Map<Class<?>, Transformation<?>> map, final boolean b, final boolean b2, final boolean b3, final Options options, final DecodeJob.Callback<R> callback) {
            return (DecodeJob<R>)this.pool.acquire().init(glideContext, o, engineKey, key, n, n2, clazz, clazz2, priority, diskCacheStrategy, map, b, b2, b3, options, (DecodeJob.Callback<?>)callback, this.creationOrder++);
        }
    }
    
    static class EngineJobFactory
    {
        final GlideExecutor diskCacheExecutor;
        final EngineJobListener listener;
        final Pools.Pool<EngineJob<?>> pool;
        final GlideExecutor sourceExecutor;
        final GlideExecutor sourceUnlimitedExecutor;
        
        EngineJobFactory(final GlideExecutor diskCacheExecutor, final GlideExecutor sourceExecutor, final GlideExecutor sourceUnlimitedExecutor, final EngineJobListener listener) {
            this.pool = FactoryPools.simple(150, (FactoryPools.Factory<EngineJob<?>>)new FactoryPools.Factory<EngineJob<?>>() {
                public EngineJob<?> create() {
                    return new EngineJob<Object>(EngineJobFactory.this.diskCacheExecutor, EngineJobFactory.this.sourceExecutor, EngineJobFactory.this.sourceUnlimitedExecutor, EngineJobFactory.this.listener, EngineJobFactory.this.pool);
                }
            });
            this.diskCacheExecutor = diskCacheExecutor;
            this.sourceExecutor = sourceExecutor;
            this.sourceUnlimitedExecutor = sourceUnlimitedExecutor;
            this.listener = listener;
        }
        
         <R> EngineJob<R> build(final Key key, final boolean b, final boolean b2) {
            return (EngineJob<R>)this.pool.acquire().init(key, b, b2);
        }
    }
    
    private static class LazyDiskCacheProvider implements DiskCacheProvider
    {
        private volatile DiskCache diskCache;
        private final DiskCache.Factory factory;
        
        public LazyDiskCacheProvider(final DiskCache.Factory factory) {
            this.factory = factory;
        }
        
        @Override
        public DiskCache getDiskCache() {
            if (this.diskCache == null) {
                synchronized (this) {
                    if (this.diskCache == null) {
                        this.diskCache = this.factory.build();
                    }
                    if (this.diskCache == null) {
                        this.diskCache = new DiskCacheAdapter();
                    }
                }
            }
            return this.diskCache;
        }
    }
    
    public static class LoadStatus
    {
        private final ResourceCallback cb;
        private final EngineJob<?> engineJob;
        
        public LoadStatus(final ResourceCallback cb, final EngineJob<?> engineJob) {
            this.cb = cb;
            this.engineJob = engineJob;
        }
        
        public void cancel() {
            this.engineJob.removeCallback(this.cb);
        }
    }
    
    private static class RefQueueIdleHandler implements MessageQueue$IdleHandler
    {
        private final Map<Key, WeakReference<EngineResource<?>>> activeResources;
        private final ReferenceQueue<EngineResource<?>> queue;
        
        public RefQueueIdleHandler(final Map<Key, WeakReference<EngineResource<?>>> activeResources, final ReferenceQueue<EngineResource<?>> queue) {
            this.activeResources = activeResources;
            this.queue = queue;
        }
        
        public boolean queueIdle() {
            final ResourceWeakReference resourceWeakReference = (ResourceWeakReference)this.queue.poll();
            if (resourceWeakReference != null) {
                this.activeResources.remove(resourceWeakReference.key);
            }
            return true;
        }
    }
    
    private static class ResourceWeakReference extends WeakReference<EngineResource<?>>
    {
        final Key key;
        
        public ResourceWeakReference(final Key key, final EngineResource<?> referent, final ReferenceQueue<? super EngineResource<?>> q) {
            super(referent, q);
            this.key = key;
        }
    }
}
