package com.bumptech.glide.load.engine;

import android.os.Build.VERSION;
import android.support.p001v4.p003os.TraceCompat;
import android.support.p001v4.util.Pools.Pool;
import android.util.Log;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry.NoResultEncoderAvailableException;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.pool.FactoryPools.Poolable;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DecodeJob<R> implements FetcherReadyCallback, Poolable, Comparable<DecodeJob<?>>, Runnable {
    private Callback<R> callback;
    private Key currentAttemptingKey;
    private Object currentData;
    private DataSource currentDataSource;
    private DataFetcher<?> currentFetcher;
    private volatile DataFetcherGenerator currentGenerator;
    Key currentSourceKey;
    private Thread currentThread;
    final DecodeHelper<R> decodeHelper = new DecodeHelper();
    final DeferredEncodeManager<?> deferredEncodeManager = new DeferredEncodeManager();
    private final DiskCacheProvider diskCacheProvider;
    DiskCacheStrategy diskCacheStrategy;
    private final List<Exception> exceptions = new ArrayList();
    private GlideContext glideContext;
    int height;
    private volatile boolean isCallbackNotified;
    private volatile boolean isCancelled;
    private EngineKey loadKey;
    private boolean onlyRetrieveFromCache;
    Options options;
    private int order;
    private final Pool<DecodeJob<?>> pool;
    private Priority priority;
    private final ReleaseManager releaseManager = new ReleaseManager();
    private RunReason runReason;
    Key signature;
    private Stage stage;
    private long startFetchTime;
    private final StateVerifier stateVerifier = StateVerifier.newInstance();
    int width;

    interface Callback<R> {
        void onLoadFailed(GlideException glideException);

        void onResourceReady(Resource<R> resource, DataSource dataSource);

        void reschedule(DecodeJob<?> decodeJob);
    }

    private static class DeferredEncodeManager<Z> {
        private ResourceEncoder<Z> encoder;
        private Key key;
        private LockedResource<Z> toEncode;

        DeferredEncodeManager() {
        }

        /* Access modifiers changed, original: 0000 */
        public <X> void init(Key key, ResourceEncoder<X> resourceEncoder, LockedResource<X> lockedResource) {
            this.key = key;
            this.encoder = resourceEncoder;
            this.toEncode = lockedResource;
        }

        /* Access modifiers changed, original: 0000 */
        public void encode(DiskCacheProvider diskCacheProvider, Options options) {
            TraceCompat.beginSection("DecodeJob.encode");
            try {
                diskCacheProvider.getDiskCache().put(this.key, new DataCacheWriter(this.encoder, this.toEncode, options));
            } finally {
                this.toEncode.unlock();
                TraceCompat.endSection();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean hasResourceToEncode() {
            return this.toEncode != null;
        }

        /* Access modifiers changed, original: 0000 */
        public void clear() {
            this.key = null;
            this.encoder = null;
            this.toEncode = null;
        }
    }

    interface DiskCacheProvider {
        DiskCache getDiskCache();
    }

    private static class ReleaseManager {
        private boolean isEncodeComplete;
        private boolean isFailed;
        private boolean isReleased;

        ReleaseManager() {
        }

        /* Access modifiers changed, original: declared_synchronized */
        public synchronized boolean release(boolean z) {
            this.isReleased = true;
            return isComplete(z);
        }

        /* Access modifiers changed, original: declared_synchronized */
        public synchronized boolean onEncodeComplete() {
            this.isEncodeComplete = true;
            return isComplete(false);
        }

        /* Access modifiers changed, original: declared_synchronized */
        public synchronized boolean onFailed() {
            this.isFailed = true;
            return isComplete(false);
        }

        /* Access modifiers changed, original: declared_synchronized */
        public synchronized void reset() {
            this.isEncodeComplete = false;
            this.isReleased = false;
            this.isFailed = false;
        }

        private boolean isComplete(boolean z) {
            return (this.isFailed || z || this.isEncodeComplete) && this.isReleased;
        }
    }

    private enum RunReason {
        INITIALIZE,
        SWITCH_TO_SOURCE_SERVICE,
        DECODE_DATA
    }

    private enum Stage {
        INITIALIZE,
        RESOURCE_CACHE,
        DATA_CACHE,
        SOURCE,
        ENCODE,
        FINISHED
    }

    private final class DecodeCallback<Z> implements DecodeCallback<Z> {
        private final DataSource dataSource;

        DecodeCallback(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        public Resource<Z> onResourceDecoded(Resource<Z> resource) {
            Transformation transformation;
            Object transform;
            EncodeStrategy encodeStrategy;
            Class resourceClass = getResourceClass(resource);
            ResourceEncoder resourceEncoder = null;
            if (this.dataSource != DataSource.RESOURCE_DISK_CACHE) {
                Transformation transformation2 = DecodeJob.this.decodeHelper.getTransformation(resourceClass);
                transformation = transformation2;
                transform = transformation2.transform(DecodeJob.this.glideContext, resource, DecodeJob.this.width, DecodeJob.this.height);
            } else {
                transform = resource;
                transformation = null;
            }
            if (!resource.equals(transform)) {
                resource.recycle();
            }
            if (DecodeJob.this.decodeHelper.isResourceEncoderAvailable(transform)) {
                resourceEncoder = DecodeJob.this.decodeHelper.getResultEncoder(transform);
                encodeStrategy = resourceEncoder.getEncodeStrategy(DecodeJob.this.options);
            } else {
                encodeStrategy = EncodeStrategy.NONE;
            }
            ResourceEncoder resourceEncoder2 = resourceEncoder;
            if (!DecodeJob.this.diskCacheStrategy.isResourceCacheable(DecodeJob.this.decodeHelper.isSourceKey(DecodeJob.this.currentSourceKey) ^ 1, this.dataSource, encodeStrategy)) {
                return transform;
            }
            if (resourceEncoder2 != null) {
                Key dataCacheKey;
                if (encodeStrategy == EncodeStrategy.SOURCE) {
                    dataCacheKey = new DataCacheKey(DecodeJob.this.currentSourceKey, DecodeJob.this.signature);
                } else if (encodeStrategy == EncodeStrategy.TRANSFORMED) {
                    Key resourceCacheKey = new ResourceCacheKey(DecodeJob.this.currentSourceKey, DecodeJob.this.signature, DecodeJob.this.width, DecodeJob.this.height, transformation, resourceClass, DecodeJob.this.options);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown strategy: ");
                    stringBuilder.append(encodeStrategy);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                Resource<Z> obtain = LockedResource.obtain(transform);
                DecodeJob.this.deferredEncodeManager.init(dataCacheKey, resourceEncoder2, obtain);
                return obtain;
            }
            throw new NoResultEncoderAvailableException(transform.get().getClass());
        }

        private Class<Z> getResourceClass(Resource<Z> resource) {
            return resource.get().getClass();
        }
    }

    DecodeJob(DiskCacheProvider diskCacheProvider, Pool<DecodeJob<?>> pool) {
        this.diskCacheProvider = diskCacheProvider;
        this.pool = pool;
    }

    /* Access modifiers changed, original: 0000 */
    public DecodeJob<R> init(GlideContext glideContext, Object obj, EngineKey engineKey, Key key, int i, int i2, Class<?> cls, Class<R> cls2, Priority priority, DiskCacheStrategy diskCacheStrategy, Map<Class<?>, Transformation<?>> map, boolean z, boolean z2, boolean z3, Options options, Callback<R> callback, int i3) {
        this.decodeHelper.init(glideContext, obj, key, i, i2, diskCacheStrategy, cls, cls2, priority, options, map, z, z2, this.diskCacheProvider);
        this.glideContext = glideContext;
        this.signature = key;
        this.priority = priority;
        this.loadKey = engineKey;
        this.width = i;
        this.height = i2;
        this.diskCacheStrategy = diskCacheStrategy;
        this.onlyRetrieveFromCache = z3;
        this.options = options;
        this.callback = callback;
        this.order = i3;
        this.runReason = RunReason.INITIALIZE;
        return this;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean willDecodeFromCache() {
        Stage nextStage = getNextStage(Stage.INITIALIZE);
        return nextStage == Stage.RESOURCE_CACHE || nextStage == Stage.DATA_CACHE;
    }

    /* Access modifiers changed, original: 0000 */
    public void release(boolean z) {
        if (this.releaseManager.release(z)) {
            releaseInternal();
        }
    }

    private void onEncodeComplete() {
        if (this.releaseManager.onEncodeComplete()) {
            releaseInternal();
        }
    }

    private void onLoadFailed() {
        if (this.releaseManager.onFailed()) {
            releaseInternal();
        }
    }

    private void releaseInternal() {
        this.releaseManager.reset();
        this.deferredEncodeManager.clear();
        this.decodeHelper.clear();
        this.isCallbackNotified = false;
        this.glideContext = null;
        this.signature = null;
        this.options = null;
        this.priority = null;
        this.loadKey = null;
        this.callback = null;
        this.stage = null;
        this.currentGenerator = null;
        this.currentThread = null;
        this.currentSourceKey = null;
        this.currentData = null;
        this.currentDataSource = null;
        this.currentFetcher = null;
        this.startFetchTime = 0;
        this.isCancelled = false;
        this.exceptions.clear();
        this.pool.release(this);
    }

    public int compareTo(DecodeJob<?> decodeJob) {
        int priority = getPriority() - decodeJob.getPriority();
        return priority == 0 ? this.order - decodeJob.order : priority;
    }

    private int getPriority() {
        return this.priority.ordinal();
    }

    public void cancel() {
        this.isCancelled = true;
        DataFetcherGenerator dataFetcherGenerator = this.currentGenerator;
        if (dataFetcherGenerator != null) {
            dataFetcherGenerator.cancel();
        }
    }

    /* JADX WARNING: Missing block: B:25:0x0076, code skipped:
            if (r0 != null) goto L_0x0078;
     */
    /* JADX WARNING: Missing block: B:26:0x0078, code skipped:
            r0.cleanup();
     */
    /* JADX WARNING: Missing block: B:27:0x007b, code skipped:
            android.support.p001v4.p003os.TraceCompat.endSection();
     */
    /* JADX WARNING: Missing block: B:46:0x00e7, code skipped:
            if (r0 != null) goto L_0x0078;
     */
    /* JADX WARNING: Missing block: B:47:0x00ea, code skipped:
            return;
     */
    public void run() {
        /*
        r7 = this;
        r0 = "DecodeJob#run";
        android.support.p001v4.p003os.TraceCompat.beginSection(r0);
        r0 = r7.currentFetcher;
        r1 = 0;
        r2 = 1;
        r3 = r7.isCancelled;	 Catch:{ RuntimeException -> 0x0081 }
        if (r3 == 0) goto L_0x0046;
    L_0x000d:
        r7.notifyFailed();	 Catch:{ RuntimeException -> 0x0081 }
        if (r0 == 0) goto L_0x001e;
    L_0x0012:
        r3 = r7.currentFetcher;
        if (r3 == 0) goto L_0x001e;
    L_0x0016:
        r3 = r7.currentFetcher;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x001f;
    L_0x001e:
        r1 = 1;
    L_0x001f:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Fetchers don't match!, old: ";
        r2.append(r3);
        r2.append(r0);
        r3 = " new: ";
        r2.append(r3);
        r3 = r7.currentFetcher;
        r2.append(r3);
        r2 = r2.toString();
        com.bumptech.glide.util.Preconditions.checkArgument(r1, r2);
        if (r0 == 0) goto L_0x0042;
    L_0x003f:
        r0.cleanup();
    L_0x0042:
        android.support.p001v4.p003os.TraceCompat.endSection();
        return;
    L_0x0046:
        r7.runWrapped();	 Catch:{ RuntimeException -> 0x0081 }
        if (r0 == 0) goto L_0x0057;
    L_0x004b:
        r3 = r7.currentFetcher;
        if (r3 == 0) goto L_0x0057;
    L_0x004f:
        r3 = r7.currentFetcher;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0058;
    L_0x0057:
        r1 = 1;
    L_0x0058:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Fetchers don't match!, old: ";
        r2.append(r3);
        r2.append(r0);
        r3 = " new: ";
        r2.append(r3);
        r3 = r7.currentFetcher;
        r2.append(r3);
        r2 = r2.toString();
        com.bumptech.glide.util.Preconditions.checkArgument(r1, r2);
        if (r0 == 0) goto L_0x007b;
    L_0x0078:
        r0.cleanup();
    L_0x007b:
        android.support.p001v4.p003os.TraceCompat.endSection();
        goto L_0x00ea;
    L_0x007f:
        r3 = move-exception;
        goto L_0x00ec;
    L_0x0081:
        r3 = move-exception;
        r4 = "DecodeJob";
        r5 = 3;
        r4 = android.util.Log.isLoggable(r4, r5);	 Catch:{ all -> 0x007f }
        if (r4 == 0) goto L_0x00ad;
    L_0x008b:
        r4 = "DecodeJob";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x007f }
        r5.<init>();	 Catch:{ all -> 0x007f }
        r6 = "DecodeJob threw unexpectedly, isCancelled: ";
        r5.append(r6);	 Catch:{ all -> 0x007f }
        r6 = r7.isCancelled;	 Catch:{ all -> 0x007f }
        r5.append(r6);	 Catch:{ all -> 0x007f }
        r6 = ", stage: ";
        r5.append(r6);	 Catch:{ all -> 0x007f }
        r6 = r7.stage;	 Catch:{ all -> 0x007f }
        r5.append(r6);	 Catch:{ all -> 0x007f }
        r5 = r5.toString();	 Catch:{ all -> 0x007f }
        android.util.Log.d(r4, r5, r3);	 Catch:{ all -> 0x007f }
    L_0x00ad:
        r4 = r7.stage;	 Catch:{ all -> 0x007f }
        r5 = com.bumptech.glide.load.engine.DecodeJob.Stage.ENCODE;	 Catch:{ all -> 0x007f }
        if (r4 == r5) goto L_0x00b6;
    L_0x00b3:
        r7.notifyFailed();	 Catch:{ all -> 0x007f }
    L_0x00b6:
        r4 = r7.isCancelled;	 Catch:{ all -> 0x007f }
        if (r4 == 0) goto L_0x00eb;
    L_0x00ba:
        if (r0 == 0) goto L_0x00c8;
    L_0x00bc:
        r3 = r7.currentFetcher;
        if (r3 == 0) goto L_0x00c8;
    L_0x00c0:
        r3 = r7.currentFetcher;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x00c9;
    L_0x00c8:
        r1 = 1;
    L_0x00c9:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Fetchers don't match!, old: ";
        r2.append(r3);
        r2.append(r0);
        r3 = " new: ";
        r2.append(r3);
        r3 = r7.currentFetcher;
        r2.append(r3);
        r2 = r2.toString();
        com.bumptech.glide.util.Preconditions.checkArgument(r1, r2);
        if (r0 == 0) goto L_0x007b;
    L_0x00e9:
        goto L_0x0078;
    L_0x00ea:
        return;
    L_0x00eb:
        throw r3;	 Catch:{ all -> 0x007f }
    L_0x00ec:
        if (r0 == 0) goto L_0x00fa;
    L_0x00ee:
        r4 = r7.currentFetcher;
        if (r4 == 0) goto L_0x00fa;
    L_0x00f2:
        r4 = r7.currentFetcher;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x00fb;
    L_0x00fa:
        r1 = 1;
    L_0x00fb:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = "Fetchers don't match!, old: ";
        r2.append(r4);
        r2.append(r0);
        r4 = " new: ";
        r2.append(r4);
        r4 = r7.currentFetcher;
        r2.append(r4);
        r2 = r2.toString();
        com.bumptech.glide.util.Preconditions.checkArgument(r1, r2);
        if (r0 == 0) goto L_0x011e;
    L_0x011b:
        r0.cleanup();
    L_0x011e:
        android.support.p001v4.p003os.TraceCompat.endSection();
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.DecodeJob.run():void");
    }

    private void runWrapped() {
        switch (this.runReason) {
            case INITIALIZE:
                this.stage = getNextStage(Stage.INITIALIZE);
                this.currentGenerator = getNextGenerator();
                runGenerators();
                return;
            case SWITCH_TO_SOURCE_SERVICE:
                runGenerators();
                return;
            case DECODE_DATA:
                decodeFromRetrievedData();
                return;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unrecognized run reason: ");
                stringBuilder.append(this.runReason);
                throw new IllegalStateException(stringBuilder.toString());
        }
    }

    private DataFetcherGenerator getNextGenerator() {
        switch (this.stage) {
            case RESOURCE_CACHE:
                return new ResourceCacheGenerator(this.decodeHelper, this);
            case DATA_CACHE:
                return new DataCacheGenerator(this.decodeHelper, this);
            case SOURCE:
                return new SourceGenerator(this.decodeHelper, this);
            case FINISHED:
                return null;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unrecognized stage: ");
                stringBuilder.append(this.stage);
                throw new IllegalStateException(stringBuilder.toString());
        }
    }

    private void runGenerators() {
        this.currentThread = Thread.currentThread();
        this.startFetchTime = LogTime.getLogTime();
        boolean z = false;
        while (!this.isCancelled && this.currentGenerator != null) {
            z = this.currentGenerator.startNext();
            if (z) {
                break;
            }
            this.stage = getNextStage(this.stage);
            this.currentGenerator = getNextGenerator();
            if (this.stage == Stage.SOURCE) {
                reschedule();
                return;
            }
        }
        if ((this.stage == Stage.FINISHED || this.isCancelled) && !r0) {
            notifyFailed();
        }
    }

    private void notifyFailed() {
        setNotifiedOrThrow();
        this.callback.onLoadFailed(new GlideException("Failed to load resource", new ArrayList(this.exceptions)));
        onLoadFailed();
    }

    private void notifyComplete(Resource<R> resource, DataSource dataSource) {
        setNotifiedOrThrow();
        this.callback.onResourceReady(resource, dataSource);
    }

    private void setNotifiedOrThrow() {
        this.stateVerifier.throwIfRecycled();
        if (this.isCallbackNotified) {
            throw new IllegalStateException("Already notified");
        }
        this.isCallbackNotified = true;
    }

    private Stage getNextStage(Stage stage) {
        switch (stage) {
            case RESOURCE_CACHE:
                if (this.diskCacheStrategy.decodeCachedData()) {
                    stage = Stage.DATA_CACHE;
                } else {
                    stage = getNextStage(Stage.DATA_CACHE);
                }
                return stage;
            case DATA_CACHE:
                return this.onlyRetrieveFromCache ? Stage.FINISHED : Stage.SOURCE;
            case SOURCE:
            case FINISHED:
                return Stage.FINISHED;
            case INITIALIZE:
                if (this.diskCacheStrategy.decodeCachedResource()) {
                    stage = Stage.RESOURCE_CACHE;
                } else {
                    stage = getNextStage(Stage.RESOURCE_CACHE);
                }
                return stage;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unrecognized stage: ");
                stringBuilder.append(stage);
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public void reschedule() {
        this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
        this.callback.reschedule(this);
    }

    public void onDataFetcherReady(Key key, Object obj, DataFetcher<?> dataFetcher, DataSource dataSource, Key key2) {
        this.currentSourceKey = key;
        this.currentData = obj;
        this.currentFetcher = dataFetcher;
        this.currentDataSource = dataSource;
        this.currentAttemptingKey = key2;
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = RunReason.DECODE_DATA;
            this.callback.reschedule(this);
            return;
        }
        TraceCompat.beginSection("DecodeJob.decodeFromRetrievedData");
        try {
            decodeFromRetrievedData();
        } finally {
            TraceCompat.endSection();
        }
    }

    public void onDataFetcherFailed(Key key, Exception exception, DataFetcher<?> dataFetcher, DataSource dataSource) {
        dataFetcher.cleanup();
        GlideException glideException = new GlideException("Fetching data failed", exception);
        glideException.setLoggingDetails(key, dataSource, dataFetcher.getDataClass());
        this.exceptions.add(glideException);
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
            this.callback.reschedule(this);
            return;
        }
        runGenerators();
    }

    private void decodeFromRetrievedData() {
        if (Log.isLoggable("DecodeJob", 2)) {
            long j = this.startFetchTime;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("data: ");
            stringBuilder.append(this.currentData);
            stringBuilder.append(", cache key: ");
            stringBuilder.append(this.currentSourceKey);
            stringBuilder.append(", fetcher: ");
            stringBuilder.append(this.currentFetcher);
            logWithTimeAndKey("Retrieved data", j, stringBuilder.toString());
        }
        Resource resource = null;
        try {
            resource = decodeFromData(this.currentFetcher, this.currentData, this.currentDataSource);
        } catch (GlideException e) {
            e.setLoggingDetails(this.currentAttemptingKey, this.currentDataSource);
            this.exceptions.add(e);
        }
        if (resource != null) {
            notifyEncodeAndRelease(resource, this.currentDataSource);
        } else {
            runGenerators();
        }
    }

    private void notifyEncodeAndRelease(Resource<R> resource, DataSource dataSource) {
        Resource resource2;
        if (resource2 instanceof Initializable) {
            ((Initializable) resource2).initialize();
        }
        LockedResource lockedResource = null;
        if (this.deferredEncodeManager.hasResourceToEncode()) {
            resource2 = LockedResource.obtain(resource2);
            lockedResource = resource2;
        }
        notifyComplete(resource2, dataSource);
        this.stage = Stage.ENCODE;
        try {
            if (this.deferredEncodeManager.hasResourceToEncode()) {
                this.deferredEncodeManager.encode(this.diskCacheProvider, this.options);
            }
            if (lockedResource != null) {
                lockedResource.unlock();
            }
            onEncodeComplete();
        } catch (Throwable th) {
            if (lockedResource != null) {
                lockedResource.unlock();
            }
            onEncodeComplete();
        }
    }

    private <Data> Resource<R> decodeFromData(DataFetcher<?> dataFetcher, Data data, DataSource dataSource) throws GlideException {
        if (data == null) {
            dataFetcher.cleanup();
            return null;
        }
        try {
            long logTime = LogTime.getLogTime();
            Resource decodeFromFetcher = decodeFromFetcher(data, dataSource);
            if (Log.isLoggable("DecodeJob", 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Decoded result ");
                stringBuilder.append(decodeFromFetcher);
                logWithTimeAndKey(stringBuilder.toString(), logTime);
            }
            dataFetcher.cleanup();
            return decodeFromFetcher;
        } catch (Throwable th) {
            dataFetcher.cleanup();
        }
    }

    private <Data> Resource<R> decodeFromFetcher(Data data, DataSource dataSource) throws GlideException {
        return runLoadPath(data, dataSource, this.decodeHelper.getLoadPath(data.getClass()));
    }

    private Options getOptionsWithHardwareConfig(DataSource dataSource) {
        Options options = this.options;
        if (VERSION.SDK_INT < 26 || options.get(Downsampler.ALLOW_HARDWARE_CONFIG) != null) {
            return options;
        }
        if (dataSource == DataSource.RESOURCE_DISK_CACHE || this.decodeHelper.isScaleOnlyOrNoTransform()) {
            options = new Options();
            options.putAll(this.options);
            options.set(Downsampler.ALLOW_HARDWARE_CONFIG, Boolean.valueOf(true));
        }
        return options;
    }

    private <Data, ResourceType> Resource<R> runLoadPath(Data data, DataSource dataSource, LoadPath<Data, ResourceType, R> loadPath) throws GlideException {
        Options optionsWithHardwareConfig = getOptionsWithHardwareConfig(dataSource);
        DataRewinder rewinder = this.glideContext.getRegistry().getRewinder(data);
        try {
            Resource<R> load = loadPath.load(rewinder, optionsWithHardwareConfig, this.width, this.height, new DecodeCallback(dataSource));
            return load;
        } finally {
            rewinder.cleanup();
        }
    }

    private void logWithTimeAndKey(String str, long j) {
        logWithTimeAndKey(str, j, null);
    }

    private void logWithTimeAndKey(String str, long j, String str2) {
        String str3 = "DecodeJob";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(" in ");
        stringBuilder.append(LogTime.getElapsedMillis(j));
        stringBuilder.append(", load key: ");
        stringBuilder.append(this.loadKey);
        if (str2 != null) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", ");
            stringBuilder2.append(str2);
            str = stringBuilder2.toString();
        } else {
            str = "";
        }
        stringBuilder.append(str);
        stringBuilder.append(", thread: ");
        stringBuilder.append(Thread.currentThread().getName());
        Log.v(str3, stringBuilder.toString());
    }

    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }
}
