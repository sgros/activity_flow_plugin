// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.EncodeStrategy;
import android.content.Context;
import com.bumptech.glide.util.Preconditions;
import android.support.v4.os.TraceCompat;
import com.bumptech.glide.load.Transformation;
import java.util.Map;
import com.bumptech.glide.load.data.DataRewinder;
import java.util.Collection;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import android.os.Build$VERSION;
import android.util.Log;
import com.bumptech.glide.util.LogTime;
import java.util.ArrayList;
import com.bumptech.glide.util.pool.StateVerifier;
import com.bumptech.glide.Priority;
import android.support.v4.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.GlideContext;
import java.util.List;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.pool.FactoryPools;

class DecodeJob<R> implements FetcherReadyCallback, Poolable, Comparable<DecodeJob<?>>, Runnable
{
    private Callback<R> callback;
    private Key currentAttemptingKey;
    private Object currentData;
    private DataSource currentDataSource;
    private DataFetcher<?> currentFetcher;
    private volatile DataFetcherGenerator currentGenerator;
    Key currentSourceKey;
    private Thread currentThread;
    final DecodeHelper<R> decodeHelper;
    final DeferredEncodeManager<?> deferredEncodeManager;
    private final DiskCacheProvider diskCacheProvider;
    DiskCacheStrategy diskCacheStrategy;
    private final List<Exception> exceptions;
    private GlideContext glideContext;
    int height;
    private volatile boolean isCallbackNotified;
    private volatile boolean isCancelled;
    private EngineKey loadKey;
    private boolean onlyRetrieveFromCache;
    Options options;
    private int order;
    private final Pools.Pool<DecodeJob<?>> pool;
    private Priority priority;
    private final ReleaseManager releaseManager;
    private RunReason runReason;
    Key signature;
    private Stage stage;
    private long startFetchTime;
    private final StateVerifier stateVerifier;
    int width;
    
    DecodeJob(final DiskCacheProvider diskCacheProvider, final Pools.Pool<DecodeJob<?>> pool) {
        this.decodeHelper = new DecodeHelper<R>();
        this.exceptions = new ArrayList<Exception>();
        this.stateVerifier = StateVerifier.newInstance();
        this.deferredEncodeManager = new DeferredEncodeManager<Object>();
        this.releaseManager = new ReleaseManager();
        this.diskCacheProvider = diskCacheProvider;
        this.pool = pool;
    }
    
    private <Data> Resource<R> decodeFromData(final DataFetcher<?> dataFetcher, final Data data, final DataSource dataSource) throws GlideException {
        if (data == null) {
            dataFetcher.cleanup();
            return null;
        }
        try {
            final long logTime = LogTime.getLogTime();
            final Resource<R> decodeFromFetcher = this.decodeFromFetcher(data, dataSource);
            if (Log.isLoggable("DecodeJob", 2)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Decoded result ");
                sb.append(decodeFromFetcher);
                this.logWithTimeAndKey(sb.toString(), logTime);
            }
            return decodeFromFetcher;
        }
        finally {
            dataFetcher.cleanup();
        }
    }
    
    private <Data> Resource<R> decodeFromFetcher(final Data data, final DataSource dataSource) throws GlideException {
        return this.runLoadPath(data, dataSource, (LoadPath<Data, ?, R>)this.decodeHelper.getLoadPath(data.getClass()));
    }
    
    private void decodeFromRetrievedData() {
        if (Log.isLoggable("DecodeJob", 2)) {
            final long startFetchTime = this.startFetchTime;
            final StringBuilder sb = new StringBuilder();
            sb.append("data: ");
            sb.append(this.currentData);
            sb.append(", cache key: ");
            sb.append(this.currentSourceKey);
            sb.append(", fetcher: ");
            sb.append(this.currentFetcher);
            this.logWithTimeAndKey("Retrieved data", startFetchTime, sb.toString());
        }
        Resource<R> decodeFromData = null;
        try {
            decodeFromData = this.decodeFromData(this.currentFetcher, this.currentData, this.currentDataSource);
        }
        catch (GlideException ex) {
            ex.setLoggingDetails(this.currentAttemptingKey, this.currentDataSource);
            this.exceptions.add(ex);
        }
        if (decodeFromData != null) {
            this.notifyEncodeAndRelease(decodeFromData, this.currentDataSource);
        }
        else {
            this.runGenerators();
        }
    }
    
    private DataFetcherGenerator getNextGenerator() {
        switch (DecodeJob$1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[this.stage.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unrecognized stage: ");
                sb.append(this.stage);
                throw new IllegalStateException(sb.toString());
            }
            case 4: {
                return null;
            }
            case 3: {
                return new SourceGenerator(this.decodeHelper, this);
            }
            case 2: {
                return new DataCacheGenerator(this.decodeHelper, this);
            }
            case 1: {
                return new ResourceCacheGenerator(this.decodeHelper, this);
            }
        }
    }
    
    private Stage getNextStage(Stage obj) {
        switch (DecodeJob$1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[obj.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unrecognized stage: ");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString());
            }
            case 5: {
                if (this.diskCacheStrategy.decodeCachedResource()) {
                    obj = Stage.RESOURCE_CACHE;
                }
                else {
                    obj = this.getNextStage(Stage.RESOURCE_CACHE);
                }
                return obj;
            }
            case 3:
            case 4: {
                return Stage.FINISHED;
            }
            case 2: {
                if (this.onlyRetrieveFromCache) {
                    obj = Stage.FINISHED;
                }
                else {
                    obj = Stage.SOURCE;
                }
                return obj;
            }
            case 1: {
                if (this.diskCacheStrategy.decodeCachedData()) {
                    obj = Stage.DATA_CACHE;
                }
                else {
                    obj = this.getNextStage(Stage.DATA_CACHE);
                }
                return obj;
            }
        }
    }
    
    private Options getOptionsWithHardwareConfig(final DataSource dataSource) {
        final Options options = this.options;
        if (Build$VERSION.SDK_INT < 26) {
            return options;
        }
        if (options.get(Downsampler.ALLOW_HARDWARE_CONFIG) != null) {
            return options;
        }
        if (dataSource != DataSource.RESOURCE_DISK_CACHE) {
            final Options options2 = options;
            if (!this.decodeHelper.isScaleOnlyOrNoTransform()) {
                return options2;
            }
        }
        final Options options2 = new Options();
        options2.putAll(this.options);
        options2.set(Downsampler.ALLOW_HARDWARE_CONFIG, true);
        return options2;
    }
    
    private int getPriority() {
        return this.priority.ordinal();
    }
    
    private void logWithTimeAndKey(final String s, final long n) {
        this.logWithTimeAndKey(s, n, null);
    }
    
    private void logWithTimeAndKey(String string, final long n, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(" in ");
        sb.append(LogTime.getElapsedMillis(n));
        sb.append(", load key: ");
        sb.append(this.loadKey);
        if (str != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(", ");
            sb2.append(str);
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        sb.append(", thread: ");
        sb.append(Thread.currentThread().getName());
        Log.v("DecodeJob", sb.toString());
    }
    
    private void notifyComplete(final Resource<R> resource, final DataSource dataSource) {
        this.setNotifiedOrThrow();
        this.callback.onResourceReady(resource, dataSource);
    }
    
    private void notifyEncodeAndRelease(final Resource<R> resource, final DataSource dataSource) {
        if (resource instanceof Initializable) {
            ((Initializable)resource).initialize();
        }
        LockedResource<R> obtain = null;
        Resource<R> resource2 = resource;
        if (this.deferredEncodeManager.hasResourceToEncode()) {
            resource2 = (obtain = LockedResource.obtain(resource));
        }
        this.notifyComplete(resource2, dataSource);
        this.stage = Stage.ENCODE;
        try {
            if (this.deferredEncodeManager.hasResourceToEncode()) {
                this.deferredEncodeManager.encode(this.diskCacheProvider, this.options);
            }
        }
        finally {
            if (obtain != null) {
                obtain.unlock();
            }
            this.onEncodeComplete();
        }
    }
    
    private void notifyFailed() {
        this.setNotifiedOrThrow();
        this.callback.onLoadFailed(new GlideException("Failed to load resource", new ArrayList<Exception>(this.exceptions)));
        this.onLoadFailed();
    }
    
    private void onEncodeComplete() {
        if (this.releaseManager.onEncodeComplete()) {
            this.releaseInternal();
        }
    }
    
    private void onLoadFailed() {
        if (this.releaseManager.onFailed()) {
            this.releaseInternal();
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
        this.startFetchTime = 0L;
        this.isCancelled = false;
        this.exceptions.clear();
        this.pool.release(this);
    }
    
    private void runGenerators() {
        this.currentThread = Thread.currentThread();
        this.startFetchTime = LogTime.getLogTime();
        boolean startNext = false;
        do {
            boolean b = startNext;
            if (!this.isCancelled) {
                b = startNext;
                if (this.currentGenerator != null) {
                    startNext = this.currentGenerator.startNext();
                    if (!(b = startNext)) {
                        this.stage = this.getNextStage(this.stage);
                        this.currentGenerator = this.getNextGenerator();
                        continue;
                    }
                }
            }
            if ((this.stage == Stage.FINISHED || this.isCancelled) && !b) {
                this.notifyFailed();
            }
            return;
        } while (this.stage != Stage.SOURCE);
        this.reschedule();
    }
    
    private <Data, ResourceType> Resource<R> runLoadPath(Data rewinder, final DataSource dataSource, final LoadPath<Data, ResourceType, R> loadPath) throws GlideException {
        final Options optionsWithHardwareConfig = this.getOptionsWithHardwareConfig(dataSource);
        rewinder = (Data)this.glideContext.getRegistry().getRewinder(rewinder);
        try {
            return loadPath.load((DataRewinder<Data>)rewinder, optionsWithHardwareConfig, this.width, this.height, new DecodeCallback<ResourceType>(dataSource));
        }
        finally {
            ((DataRewinder)rewinder).cleanup();
        }
    }
    
    private void runWrapped() {
        switch (DecodeJob$1.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[this.runReason.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unrecognized run reason: ");
                sb.append(this.runReason);
                throw new IllegalStateException(sb.toString());
            }
            case 3: {
                this.decodeFromRetrievedData();
                break;
            }
            case 2: {
                this.runGenerators();
                break;
            }
            case 1: {
                this.stage = this.getNextStage(Stage.INITIALIZE);
                this.currentGenerator = this.getNextGenerator();
                this.runGenerators();
                break;
            }
        }
    }
    
    private void setNotifiedOrThrow() {
        this.stateVerifier.throwIfRecycled();
        if (!this.isCallbackNotified) {
            this.isCallbackNotified = true;
            return;
        }
        throw new IllegalStateException("Already notified");
    }
    
    public void cancel() {
        this.isCancelled = true;
        final DataFetcherGenerator currentGenerator = this.currentGenerator;
        if (currentGenerator != null) {
            currentGenerator.cancel();
        }
    }
    
    @Override
    public int compareTo(final DecodeJob<?> decodeJob) {
        int n;
        if ((n = this.getPriority() - decodeJob.getPriority()) == 0) {
            n = this.order - decodeJob.order;
        }
        return n;
    }
    
    @Override
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }
    
    DecodeJob<R> init(final GlideContext glideContext, final Object o, final EngineKey loadKey, final Key signature, final int width, final int height, final Class<?> clazz, final Class<R> clazz2, final Priority priority, final DiskCacheStrategy diskCacheStrategy, final Map<Class<?>, Transformation<?>> map, final boolean b, final boolean b2, final boolean onlyRetrieveFromCache, final Options options, final Callback<R> callback, final int order) {
        this.decodeHelper.init(glideContext, o, signature, width, height, diskCacheStrategy, clazz, clazz2, priority, options, map, b, b2, this.diskCacheProvider);
        this.glideContext = glideContext;
        this.signature = signature;
        this.priority = priority;
        this.loadKey = loadKey;
        this.width = width;
        this.height = height;
        this.diskCacheStrategy = diskCacheStrategy;
        this.onlyRetrieveFromCache = onlyRetrieveFromCache;
        this.options = options;
        this.callback = callback;
        this.order = order;
        this.runReason = RunReason.INITIALIZE;
        return this;
    }
    
    @Override
    public void onDataFetcherFailed(final Key key, final Exception ex, final DataFetcher<?> dataFetcher, final DataSource dataSource) {
        dataFetcher.cleanup();
        final GlideException ex2 = new GlideException("Fetching data failed", ex);
        ex2.setLoggingDetails(key, dataSource, dataFetcher.getDataClass());
        this.exceptions.add(ex2);
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
            this.callback.reschedule(this);
        }
        else {
            this.runGenerators();
        }
    }
    
    @Override
    public void onDataFetcherReady(final Key currentSourceKey, final Object currentData, final DataFetcher<?> currentFetcher, final DataSource currentDataSource, final Key currentAttemptingKey) {
        this.currentSourceKey = currentSourceKey;
        this.currentData = currentData;
        this.currentFetcher = currentFetcher;
        this.currentDataSource = currentDataSource;
        this.currentAttemptingKey = currentAttemptingKey;
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = RunReason.DECODE_DATA;
            this.callback.reschedule(this);
            return;
        }
        TraceCompat.beginSection("DecodeJob.decodeFromRetrievedData");
        try {
            this.decodeFromRetrievedData();
        }
        finally {
            TraceCompat.endSection();
        }
    }
    
    void release(final boolean b) {
        if (this.releaseManager.release(b)) {
            this.releaseInternal();
        }
    }
    
    @Override
    public void reschedule() {
        this.runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;
        this.callback.reschedule(this);
    }
    
    @Override
    public void run() {
        TraceCompat.beginSection("DecodeJob#run");
        final DataFetcher<?> currentFetcher = this.currentFetcher;
        final boolean b = false;
        boolean b2 = false;
        final boolean b3 = false;
        final boolean b4 = false;
        try {
            StringBuilder sb;
            StringBuilder sb2;
            boolean b5 = false;
            StringBuilder sb3;
            final RuntimeException ex;
            StringBuilder sb4;
            StringBuilder sb5;
            Block_13_Outer:Block_17_Outer:
            while (true) {
                try {
                    if (this.isCancelled) {
                        this.notifyFailed();
                        Label_0059: {
                            if (currentFetcher != null && this.currentFetcher != null) {
                                b2 = b4;
                                if (!currentFetcher.equals(this.currentFetcher)) {
                                    break Label_0059;
                                }
                            }
                            b2 = true;
                        }
                        sb = new StringBuilder();
                        sb.append("Fetchers don't match!, old: ");
                        sb.append(currentFetcher);
                        sb.append(" new: ");
                        sb.append(this.currentFetcher);
                        Preconditions.checkArgument(b2, sb.toString());
                        if (currentFetcher != null) {
                            currentFetcher.cleanup();
                        }
                        TraceCompat.endSection();
                        return;
                    }
                    this.runWrapped();
                    Label_0156: {
                        if (currentFetcher != null && this.currentFetcher != null) {
                            b2 = b;
                            if (!currentFetcher.equals(this.currentFetcher)) {
                                break Label_0156;
                            }
                        }
                        b2 = true;
                    }
                    sb2 = new StringBuilder();
                    sb2.append("Fetchers don't match!, old: ");
                    sb2.append(currentFetcher);
                    sb2.append(" new: ");
                    sb2.append(this.currentFetcher);
                    Preconditions.checkArgument(b2, sb2.toString());
                    if (currentFetcher != null) {
                        currentFetcher.cleanup();
                    }
                    Label_0219: {
                        TraceCompat.endSection();
                    }
                }
                finally {
                    Label_0442: {
                        if (currentFetcher != null && this.currentFetcher != null) {
                            b5 = b3;
                            if (!currentFetcher.equals(this.currentFetcher)) {
                                break Label_0442;
                            }
                        }
                        b5 = true;
                    }
                    sb3 = new StringBuilder();
                    sb3.append("Fetchers don't match!, old: ");
                    sb3.append(currentFetcher);
                    sb3.append(" new: ");
                    sb3.append(this.currentFetcher);
                    Preconditions.checkArgument(b5, sb3.toString());
                    if (currentFetcher != null) {
                        currentFetcher.cleanup();
                    }
                    TraceCompat.endSection();
                    // iftrue(Label_0316:, this.stage == Stage.ENCODE)
                    // iftrue(Label_0351:, currentFetcher != null && this.currentFetcher != null && !currentFetcher.equals((Object)this.currentFetcher))
                    // iftrue(Label_0219:, currentFetcher == null)
                    Label_0316: {
                        while (true) {
                            this.notifyFailed();
                            break Label_0316;
                            Label_0412: {
                                throw ex;
                            }
                            sb4 = new StringBuilder();
                            sb4.append("DecodeJob threw unexpectedly, isCancelled: ");
                            sb4.append(this.isCancelled);
                            sb4.append(", stage: ");
                            sb4.append(this.stage);
                            Log.d("DecodeJob", sb4.toString(), (Throwable)ex);
                            continue Block_17_Outer;
                        }
                        b2 = true;
                        while (true) {
                            Label_0351: {
                                break Label_0351;
                                continue Block_13_Outer;
                            }
                            sb5 = new StringBuilder();
                            sb5.append("Fetchers don't match!, old: ");
                            sb5.append(currentFetcher);
                            sb5.append(" new: ");
                            sb5.append(this.currentFetcher);
                            Preconditions.checkArgument(b2, sb5.toString());
                            continue;
                        }
                    }
                }
                // iftrue(Label_0412:, !this.isCancelled)
                break;
            }
        }
        catch (RuntimeException ex2) {}
    }
    
    boolean willDecodeFromCache() {
        final Stage nextStage = this.getNextStage(Stage.INITIALIZE);
        return nextStage == Stage.RESOURCE_CACHE || nextStage == Stage.DATA_CACHE;
    }
    
    interface Callback<R>
    {
        void onLoadFailed(final GlideException p0);
        
        void onResourceReady(final Resource<R> p0, final DataSource p1);
        
        void reschedule(final DecodeJob<?> p0);
    }
    
    private final class DecodeCallback<Z> implements DecodePath.DecodeCallback<Z>
    {
        private final DataSource dataSource;
        
        DecodeCallback(final DataSource dataSource) {
            this.dataSource = dataSource;
        }
        
        private Class<Z> getResourceClass(final Resource<Z> resource) {
            return (Class<Z>)resource.get().getClass();
        }
        
        @Override
        public Resource<Z> onResourceDecoded(final Resource<Z> resource) {
            final Class<Z> resourceClass = this.getResourceClass((Resource<Z>)resource);
            final DataSource dataSource = this.dataSource;
            final DataSource resource_DISK_CACHE = DataSource.RESOURCE_DISK_CACHE;
            final ResourceEncoder<Object> resourceEncoder = null;
            Transformation<?> transformation;
            Resource<?> transform;
            if (dataSource != resource_DISK_CACHE) {
                transformation = DecodeJob.this.decodeHelper.getTransformation((Class<Object>)resourceClass);
                transform = transformation.transform((Context)DecodeJob.this.glideContext, resource, DecodeJob.this.width, DecodeJob.this.height);
            }
            else {
                transform = resource;
                transformation = null;
            }
            if (!resource.equals(transform)) {
                resource.recycle();
            }
            Object resultEncoder;
            EncodeStrategy encodeStrategy;
            if (DecodeJob.this.decodeHelper.isResourceEncoderAvailable(transform)) {
                resultEncoder = DecodeJob.this.decodeHelper.getResultEncoder(transform);
                encodeStrategy = ((ResourceEncoder)resultEncoder).getEncodeStrategy(DecodeJob.this.options);
            }
            else {
                final EncodeStrategy none = EncodeStrategy.NONE;
                resultEncoder = resourceEncoder;
                encodeStrategy = none;
            }
            final boolean sourceKey = DecodeJob.this.decodeHelper.isSourceKey(DecodeJob.this.currentSourceKey);
            Resource<Z> obtain = (Resource<Z>)transform;
            if (DecodeJob.this.diskCacheStrategy.isResourceCacheable(sourceKey ^ true, this.dataSource, encodeStrategy)) {
                if (resultEncoder == null) {
                    throw new Registry.NoResultEncoderAvailableException(transform.get().getClass());
                }
                Key key;
                if (encodeStrategy == EncodeStrategy.SOURCE) {
                    key = new DataCacheKey(DecodeJob.this.currentSourceKey, DecodeJob.this.signature);
                }
                else {
                    if (encodeStrategy != EncodeStrategy.TRANSFORMED) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown strategy: ");
                        sb.append(encodeStrategy);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    key = new ResourceCacheKey(DecodeJob.this.currentSourceKey, DecodeJob.this.signature, DecodeJob.this.width, DecodeJob.this.height, transformation, resourceClass, DecodeJob.this.options);
                }
                obtain = LockedResource.obtain(transform);
                DecodeJob.this.deferredEncodeManager.init(key, (ResourceEncoder<Object>)resultEncoder, (LockedResource<Object>)obtain);
            }
            return obtain;
        }
    }
    
    private static class DeferredEncodeManager<Z>
    {
        private ResourceEncoder<Z> encoder;
        private Key key;
        private LockedResource<Z> toEncode;
        
        DeferredEncodeManager() {
        }
        
        void clear() {
            this.key = null;
            this.encoder = null;
            this.toEncode = null;
        }
        
        void encode(final DiskCacheProvider diskCacheProvider, final Options options) {
            TraceCompat.beginSection("DecodeJob.encode");
            try {
                diskCacheProvider.getDiskCache().put(this.key, (DiskCache.Writer)new DataCacheWriter((Encoder<Object>)this.encoder, this.toEncode, options));
            }
            finally {
                this.toEncode.unlock();
                TraceCompat.endSection();
            }
        }
        
        boolean hasResourceToEncode() {
            return this.toEncode != null;
        }
        
         <X> void init(final Key key, final ResourceEncoder<X> encoder, final LockedResource<X> toEncode) {
            this.key = key;
            this.encoder = (ResourceEncoder<Z>)encoder;
            this.toEncode = (LockedResource<Z>)toEncode;
        }
    }
    
    interface DiskCacheProvider
    {
        DiskCache getDiskCache();
    }
    
    private static class ReleaseManager
    {
        private boolean isEncodeComplete;
        private boolean isFailed;
        private boolean isReleased;
        
        ReleaseManager() {
        }
        
        private boolean isComplete(final boolean b) {
            return (this.isFailed || b || this.isEncodeComplete) && this.isReleased;
        }
        
        boolean onEncodeComplete() {
            synchronized (this) {
                this.isEncodeComplete = true;
                return this.isComplete(false);
            }
        }
        
        boolean onFailed() {
            synchronized (this) {
                this.isFailed = true;
                return this.isComplete(false);
            }
        }
        
        boolean release(final boolean b) {
            synchronized (this) {
                this.isReleased = true;
                return this.isComplete(b);
            }
        }
        
        void reset() {
            synchronized (this) {
                this.isEncodeComplete = false;
                this.isReleased = false;
                this.isFailed = false;
            }
        }
    }
    
    private enum RunReason
    {
        DECODE_DATA, 
        INITIALIZE, 
        SWITCH_TO_SOURCE_SERVICE;
    }
    
    private enum Stage
    {
        DATA_CACHE, 
        ENCODE, 
        FINISHED, 
        INITIALIZE, 
        RESOURCE_CACHE, 
        SOURCE;
    }
}
