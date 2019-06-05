package com.bumptech.glide.load.engine;

import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.support.p001v4.util.Pools.Pool;
import android.util.Log;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCacheAdapter;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.cache.MemoryCache.ResourceRemovedListener;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.FactoryPools.Factory;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class Engine implements EngineJobListener, ResourceListener, ResourceRemovedListener {
    private final Map<Key, WeakReference<EngineResource<?>>> activeResources;
    private final MemoryCache cache;
    private final DecodeJobFactory decodeJobFactory;
    private final LazyDiskCacheProvider diskCacheProvider;
    private final EngineJobFactory engineJobFactory;
    private final Map<Key, EngineJob<?>> jobs;
    private final EngineKeyFactory keyFactory;
    private final ResourceRecycler resourceRecycler;
    private ReferenceQueue<EngineResource<?>> resourceReferenceQueue;

    static class DecodeJobFactory {
        private int creationOrder;
        final DiskCacheProvider diskCacheProvider;
        final Pool<DecodeJob<?>> pool = FactoryPools.simple(150, new C03971());

        /* renamed from: com.bumptech.glide.load.engine.Engine$DecodeJobFactory$1 */
        class C03971 implements Factory<DecodeJob<?>> {
            C03971() {
            }

            public DecodeJob<?> create() {
                return new DecodeJob(DecodeJobFactory.this.diskCacheProvider, DecodeJobFactory.this.pool);
            }
        }

        DecodeJobFactory(DiskCacheProvider diskCacheProvider) {
            this.diskCacheProvider = diskCacheProvider;
        }

        /* Access modifiers changed, original: 0000 */
        public <R> DecodeJob<R> build(GlideContext glideContext, Object obj, EngineKey engineKey, Key key, int i, int i2, Class<?> cls, Class<R> cls2, Priority priority, DiskCacheStrategy diskCacheStrategy, Map<Class<?>, Transformation<?>> map, boolean z, boolean z2, boolean z3, Options options, Callback<R> callback) {
            GlideContext glideContext2 = glideContext;
            Object obj2 = obj;
            EngineKey engineKey2 = engineKey;
            Key key2 = key;
            int i3 = i;
            int i4 = i2;
            Class<?> cls3 = cls;
            Class<R> cls4 = cls2;
            Priority priority2 = priority;
            DiskCacheStrategy diskCacheStrategy2 = diskCacheStrategy;
            Map<Class<?>, Transformation<?>> map2 = map;
            boolean z4 = z;
            boolean z5 = z2;
            boolean z6 = z3;
            Options options2 = options;
            Callback<R> callback2 = callback;
            DecodeJob decodeJob = (DecodeJob) this.pool.acquire();
            int i5 = this.creationOrder;
            int i6 = i5;
            this.creationOrder = i5 + 1;
            return decodeJob.init(glideContext2, obj2, engineKey2, key2, i3, i4, cls3, cls4, priority2, diskCacheStrategy2, map2, z4, z5, z6, options2, callback2, i6);
        }
    }

    static class EngineJobFactory {
        final GlideExecutor diskCacheExecutor;
        final EngineJobListener listener;
        final Pool<EngineJob<?>> pool = FactoryPools.simple(150, new C03981());
        final GlideExecutor sourceExecutor;
        final GlideExecutor sourceUnlimitedExecutor;

        /* renamed from: com.bumptech.glide.load.engine.Engine$EngineJobFactory$1 */
        class C03981 implements Factory<EngineJob<?>> {
            C03981() {
            }

            public EngineJob<?> create() {
                return new EngineJob(EngineJobFactory.this.diskCacheExecutor, EngineJobFactory.this.sourceExecutor, EngineJobFactory.this.sourceUnlimitedExecutor, EngineJobFactory.this.listener, EngineJobFactory.this.pool);
            }
        }

        EngineJobFactory(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, EngineJobListener engineJobListener) {
            this.diskCacheExecutor = glideExecutor;
            this.sourceExecutor = glideExecutor2;
            this.sourceUnlimitedExecutor = glideExecutor3;
            this.listener = engineJobListener;
        }

        /* Access modifiers changed, original: 0000 */
        public <R> EngineJob<R> build(Key key, boolean z, boolean z2) {
            return ((EngineJob) this.pool.acquire()).init(key, z, z2);
        }
    }

    public static class LoadStatus {
        /* renamed from: cb */
        private final ResourceCallback f37cb;
        private final EngineJob<?> engineJob;

        public LoadStatus(ResourceCallback resourceCallback, EngineJob<?> engineJob) {
            this.f37cb = resourceCallback;
            this.engineJob = engineJob;
        }

        public void cancel() {
            this.engineJob.removeCallback(this.f37cb);
        }
    }

    private static class RefQueueIdleHandler implements IdleHandler {
        private final Map<Key, WeakReference<EngineResource<?>>> activeResources;
        private final ReferenceQueue<EngineResource<?>> queue;

        public RefQueueIdleHandler(Map<Key, WeakReference<EngineResource<?>>> map, ReferenceQueue<EngineResource<?>> referenceQueue) {
            this.activeResources = map;
            this.queue = referenceQueue;
        }

        public boolean queueIdle() {
            ResourceWeakReference resourceWeakReference = (ResourceWeakReference) this.queue.poll();
            if (resourceWeakReference != null) {
                this.activeResources.remove(resourceWeakReference.key);
            }
            return true;
        }
    }

    private static class ResourceWeakReference extends WeakReference<EngineResource<?>> {
        final Key key;

        public ResourceWeakReference(Key key, EngineResource<?> engineResource, ReferenceQueue<? super EngineResource<?>> referenceQueue) {
            super(engineResource, referenceQueue);
            this.key = key;
        }
    }

    private static class LazyDiskCacheProvider implements DiskCacheProvider {
        private volatile DiskCache diskCache;
        private final DiskCache.Factory factory;

        public LazyDiskCacheProvider(DiskCache.Factory factory) {
            this.factory = factory;
        }

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

    public Engine(MemoryCache memoryCache, DiskCache.Factory factory, GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3) {
        this(memoryCache, factory, glideExecutor, glideExecutor2, glideExecutor3, null, null, null, null, null, null);
    }

    Engine(MemoryCache memoryCache, DiskCache.Factory factory, GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, Map<Key, EngineJob<?>> map, EngineKeyFactory engineKeyFactory, Map<Key, WeakReference<EngineResource<?>>> map2, EngineJobFactory engineJobFactory, DecodeJobFactory decodeJobFactory, ResourceRecycler resourceRecycler) {
        Map map22;
        Map map3;
        this.cache = memoryCache;
        this.diskCacheProvider = new LazyDiskCacheProvider(factory);
        if (map22 == null) {
            map22 = new HashMap();
        }
        this.activeResources = map22;
        if (engineKeyFactory == null) {
            engineKeyFactory = new EngineKeyFactory();
        }
        this.keyFactory = engineKeyFactory;
        if (map3 == null) {
            map3 = new HashMap();
        }
        this.jobs = map3;
        if (engineJobFactory == null) {
            engineJobFactory = new EngineJobFactory(glideExecutor, glideExecutor2, glideExecutor3, this);
        }
        this.engineJobFactory = engineJobFactory;
        if (decodeJobFactory == null) {
            decodeJobFactory = new DecodeJobFactory(this.diskCacheProvider);
        }
        this.decodeJobFactory = decodeJobFactory;
        if (resourceRecycler == null) {
            resourceRecycler = new ResourceRecycler();
        }
        this.resourceRecycler = resourceRecycler;
        memoryCache.setResourceRemovedListener(this);
    }

    public <R> LoadStatus load(GlideContext glideContext, Object obj, Key key, int i, int i2, Class<?> cls, Class<R> cls2, Priority priority, DiskCacheStrategy diskCacheStrategy, Map<Class<?>, Transformation<?>> map, boolean z, boolean z2, Options options, boolean z3, boolean z4, boolean z5, ResourceCallback resourceCallback) {
        boolean z6 = z3;
        ResourceCallback resourceCallback2 = resourceCallback;
        Util.assertMainThread();
        long logTime = LogTime.getLogTime();
        EngineKey buildKey = this.keyFactory.buildKey(obj, key, i, i2, map, cls, cls2, options);
        EngineResource loadFromCache = loadFromCache(buildKey, z6);
        if (loadFromCache != null) {
            resourceCallback2.onResourceReady(loadFromCache, DataSource.MEMORY_CACHE);
            if (Log.isLoggable("Engine", 2)) {
                logWithTimeAndKey("Loaded resource from cache", logTime, buildKey);
            }
            return null;
        }
        loadFromCache = loadFromActiveResources(buildKey, z6);
        if (loadFromCache != null) {
            resourceCallback2.onResourceReady(loadFromCache, DataSource.MEMORY_CACHE);
            if (Log.isLoggable("Engine", 2)) {
                logWithTimeAndKey("Loaded resource from active resources", logTime, buildKey);
            }
            return null;
        }
        EngineJob engineJob = (EngineJob) this.jobs.get(buildKey);
        if (engineJob != null) {
            engineJob.addCallback(resourceCallback2);
            if (Log.isLoggable("Engine", 2)) {
                logWithTimeAndKey("Added to existing load", logTime, buildKey);
            }
            return new LoadStatus(resourceCallback2, engineJob);
        }
        EngineJob build = this.engineJobFactory.build(buildKey, z6, z4);
        long j = logTime;
        DecodeJob build2 = this.decodeJobFactory.build(glideContext, obj, buildKey, key, i, i2, cls, cls2, priority, diskCacheStrategy, map, z, z2, z5, options, build);
        this.jobs.put(buildKey, build);
        build.addCallback(resourceCallback2);
        build.start(build2);
        if (Log.isLoggable("Engine", 2)) {
            logWithTimeAndKey("Started new load", j, buildKey);
        }
        return new LoadStatus(resourceCallback2, build);
    }

    private static void logWithTimeAndKey(String str, long j, Key key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(" in ");
        stringBuilder.append(LogTime.getElapsedMillis(j));
        stringBuilder.append("ms, key: ");
        stringBuilder.append(key);
        Log.v("Engine", stringBuilder.toString());
    }

    private EngineResource<?> loadFromActiveResources(Key key, boolean z) {
        EngineResource<?> engineResource = null;
        if (!z) {
            return null;
        }
        WeakReference weakReference = (WeakReference) this.activeResources.get(key);
        if (weakReference != null) {
            engineResource = (EngineResource) weakReference.get();
            if (engineResource != null) {
                engineResource.acquire();
            } else {
                this.activeResources.remove(key);
            }
        }
        return engineResource;
    }

    private EngineResource<?> loadFromCache(Key key, boolean z) {
        if (!z) {
            return null;
        }
        EngineResource engineResourceFromCache = getEngineResourceFromCache(key);
        if (engineResourceFromCache != null) {
            engineResourceFromCache.acquire();
            this.activeResources.put(key, new ResourceWeakReference(key, engineResourceFromCache, getReferenceQueue()));
        }
        return engineResourceFromCache;
    }

    private EngineResource<?> getEngineResourceFromCache(Key key) {
        Resource remove = this.cache.remove(key);
        if (remove == null) {
            return null;
        }
        if (remove instanceof EngineResource) {
            return (EngineResource) remove;
        }
        return new EngineResource(remove, true);
    }

    public void release(Resource<?> resource) {
        Util.assertMainThread();
        if (resource instanceof EngineResource) {
            ((EngineResource) resource).release();
            return;
        }
        throw new IllegalArgumentException("Cannot release anything but an EngineResource");
    }

    public void onEngineJobComplete(Key key, EngineResource<?> engineResource) {
        Util.assertMainThread();
        if (engineResource != null) {
            engineResource.setResourceListener(key, this);
            if (engineResource.isCacheable()) {
                this.activeResources.put(key, new ResourceWeakReference(key, engineResource, getReferenceQueue()));
            }
        }
        this.jobs.remove(key);
    }

    public void onEngineJobCancelled(EngineJob engineJob, Key key) {
        Util.assertMainThread();
        if (engineJob.equals((EngineJob) this.jobs.get(key))) {
            this.jobs.remove(key);
        }
    }

    public void onResourceRemoved(Resource<?> resource) {
        Util.assertMainThread();
        this.resourceRecycler.recycle(resource);
    }

    public void onResourceReleased(Key key, EngineResource engineResource) {
        Util.assertMainThread();
        this.activeResources.remove(key);
        if (engineResource.isCacheable()) {
            this.cache.put(key, engineResource);
        } else {
            this.resourceRecycler.recycle(engineResource);
        }
    }

    private ReferenceQueue<EngineResource<?>> getReferenceQueue() {
        if (this.resourceReferenceQueue == null) {
            this.resourceReferenceQueue = new ReferenceQueue();
            Looper.myQueue().addIdleHandler(new RefQueueIdleHandler(this.activeResources, this.resourceReferenceQueue));
        }
        return this.resourceReferenceQueue;
    }
}
