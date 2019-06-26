// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.smoothstreaming;

import com.google.android.exoplayer2.ParserException;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.upstream.Allocator;
import android.os.SystemClock;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.SinglePeriodTimeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.util.ArrayList;
import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.source.BaseMediaSource;

public final class SsMediaSource extends BaseMediaSource implements Callback<ParsingLoadable<SsManifest>>
{
    private final SsChunkSource.Factory chunkSourceFactory;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final long livePresentationDelayMs;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private SsManifest manifest;
    private DataSource manifestDataSource;
    private final DataSource.Factory manifestDataSourceFactory;
    private final MediaSourceEventListener.EventDispatcher manifestEventDispatcher;
    private long manifestLoadStartTimestamp;
    private Loader manifestLoader;
    private LoaderErrorThrower manifestLoaderErrorThrower;
    private final ParsingLoadable.Parser<? extends SsManifest> manifestParser;
    private Handler manifestRefreshHandler;
    private final Uri manifestUri;
    private final ArrayList<SsMediaPeriod> mediaPeriods;
    private TransferListener mediaTransferListener;
    private final boolean sideloadedManifest;
    private final Object tag;
    
    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.smoothstreaming");
    }
    
    @Deprecated
    public SsMediaSource(final Uri uri, final DataSource.Factory factory, final SsChunkSource.Factory factory2, final int n, final long n2, final Handler handler, final MediaSourceEventListener mediaSourceEventListener) {
        this(uri, factory, new SsManifestParser(), factory2, n, n2, handler, mediaSourceEventListener);
    }
    
    @Deprecated
    public SsMediaSource(final Uri uri, final DataSource.Factory factory, final SsChunkSource.Factory factory2, final Handler handler, final MediaSourceEventListener mediaSourceEventListener) {
        this(uri, factory, factory2, 3, 30000L, handler, mediaSourceEventListener);
    }
    
    @Deprecated
    public SsMediaSource(final Uri uri, final DataSource.Factory factory, final ParsingLoadable.Parser<? extends SsManifest> parser, final SsChunkSource.Factory factory2, final int n, final long n2, final Handler handler, final MediaSourceEventListener mediaSourceEventListener) {
        this(null, uri, factory, parser, factory2, new DefaultCompositeSequenceableLoaderFactory(), new DefaultLoadErrorHandlingPolicy(n), n2, null);
        if (handler != null && mediaSourceEventListener != null) {
            this.addEventListener(handler, mediaSourceEventListener);
        }
    }
    
    private SsMediaSource(final SsManifest manifest, Uri fixManifestUri, final DataSource.Factory manifestDataSourceFactory, final ParsingLoadable.Parser<? extends SsManifest> manifestParser, final SsChunkSource.Factory chunkSourceFactory, final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final long livePresentationDelayMs, final Object tag) {
        final boolean b = false;
        Assertions.checkState(manifest == null || !manifest.isLive);
        this.manifest = manifest;
        if (fixManifestUri == null) {
            fixManifestUri = null;
        }
        else {
            fixManifestUri = SsUtil.fixManifestUri(fixManifestUri);
        }
        this.manifestUri = fixManifestUri;
        this.manifestDataSourceFactory = manifestDataSourceFactory;
        this.manifestParser = manifestParser;
        this.chunkSourceFactory = chunkSourceFactory;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.livePresentationDelayMs = livePresentationDelayMs;
        this.manifestEventDispatcher = this.createEventDispatcher(null);
        this.tag = tag;
        boolean sideloadedManifest = b;
        if (manifest != null) {
            sideloadedManifest = true;
        }
        this.sideloadedManifest = sideloadedManifest;
        this.mediaPeriods = new ArrayList<SsMediaPeriod>();
    }
    
    private void processManifest() {
        for (int i = 0; i < this.mediaPeriods.size(); ++i) {
            this.mediaPeriods.get(i).updateManifest(this.manifest);
        }
        final SsManifest.StreamElement[] streamElements = this.manifest.streamElements;
        final int length = streamElements.length;
        long a = Long.MIN_VALUE;
        long n = Long.MAX_VALUE;
        long max;
        long min;
        for (int j = 0; j < length; ++j, a = max, n = min) {
            final SsManifest.StreamElement streamElement = streamElements[j];
            max = a;
            min = n;
            if (streamElement.chunkCount > 0) {
                min = Math.min(n, streamElement.getStartTimeUs(0));
                max = Math.max(a, streamElement.getStartTimeUs(streamElement.chunkCount - 1) + streamElement.getChunkDurationUs(streamElement.chunkCount - 1));
            }
        }
        SinglePeriodTimeline singlePeriodTimeline;
        if (n == Long.MAX_VALUE) {
            long n2;
            if (this.manifest.isLive) {
                n2 = -9223372036854775807L;
            }
            else {
                n2 = 0L;
            }
            singlePeriodTimeline = new SinglePeriodTimeline(n2, 0L, 0L, 0L, true, this.manifest.isLive, this.tag);
        }
        else {
            final SsManifest manifest = this.manifest;
            if (manifest.isLive) {
                final long dvrWindowLengthUs = manifest.dvrWindowLengthUs;
                long max2 = n;
                if (dvrWindowLengthUs != -9223372036854775807L) {
                    max2 = n;
                    if (dvrWindowLengthUs > 0L) {
                        max2 = Math.max(n, a - dvrWindowLengthUs);
                    }
                }
                final long n3 = a - max2;
                long min2;
                if ((min2 = n3 - C.msToUs(this.livePresentationDelayMs)) < 5000000L) {
                    min2 = Math.min(5000000L, n3 / 2L);
                }
                singlePeriodTimeline = new SinglePeriodTimeline(-9223372036854775807L, n3, max2, min2, true, true, this.tag);
            }
            else {
                final long durationUs = manifest.durationUs;
                long n4;
                if (durationUs != -9223372036854775807L) {
                    n4 = durationUs;
                }
                else {
                    n4 = a - n;
                }
                singlePeriodTimeline = new SinglePeriodTimeline(n + n4, n4, n, 0L, true, false, this.tag);
            }
        }
        this.refreshSourceInfo(singlePeriodTimeline, this.manifest);
    }
    
    private void scheduleManifestRefresh() {
        if (!this.manifest.isLive) {
            return;
        }
        this.manifestRefreshHandler.postDelayed((Runnable)new _$$Lambda$SsMediaSource$tFjHmMdOxDkhvkY7QhPdfdPmbtI(this), Math.max(0L, this.manifestLoadStartTimestamp + 5000L - SystemClock.elapsedRealtime()));
    }
    
    private void startLoadingManifest() {
        final ParsingLoadable parsingLoadable = new ParsingLoadable(this.manifestDataSource, this.manifestUri, 4, (ParsingLoadable.Parser<? extends T>)this.manifestParser);
        this.manifestEventDispatcher.loadStarted(parsingLoadable.dataSpec, parsingLoadable.type, this.manifestLoader.startLoading(parsingLoadable, (Loader.Callback<ParsingLoadable>)this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(parsingLoadable.type)));
    }
    
    @Override
    public MediaPeriod createPeriod(final MediaPeriodId mediaPeriodId, final Allocator allocator, final long n) {
        final SsMediaPeriod e = new SsMediaPeriod(this.manifest, this.chunkSourceFactory, this.mediaTransferListener, this.compositeSequenceableLoaderFactory, this.loadErrorHandlingPolicy, this.createEventDispatcher(mediaPeriodId), this.manifestLoaderErrorThrower, allocator);
        this.mediaPeriods.add(e);
        return e;
    }
    
    @Override
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.manifestLoaderErrorThrower.maybeThrowError();
    }
    
    public void onLoadCanceled(final ParsingLoadable<SsManifest> parsingLoadable, final long n, final long n2, final boolean b) {
        this.manifestEventDispatcher.loadCanceled(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), parsingLoadable.type, n, n2, parsingLoadable.bytesLoaded());
    }
    
    public void onLoadCompleted(final ParsingLoadable<SsManifest> parsingLoadable, final long n, final long n2) {
        this.manifestEventDispatcher.loadCompleted(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), parsingLoadable.type, n, n2, parsingLoadable.bytesLoaded());
        this.manifest = parsingLoadable.getResult();
        this.manifestLoadStartTimestamp = n - n2;
        this.processManifest();
        this.scheduleManifestRefresh();
    }
    
    public LoadErrorAction onLoadError(final ParsingLoadable<SsManifest> parsingLoadable, final long n, final long n2, final IOException ex, final int n3) {
        final boolean b = ex instanceof ParserException;
        this.manifestEventDispatcher.loadError(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), parsingLoadable.type, n, n2, parsingLoadable.bytesLoaded(), ex, b);
        Loader.LoadErrorAction loadErrorAction;
        if (b) {
            loadErrorAction = Loader.DONT_RETRY_FATAL;
        }
        else {
            loadErrorAction = Loader.RETRY;
        }
        return loadErrorAction;
    }
    
    public void prepareSourceInternal(final TransferListener mediaTransferListener) {
        this.mediaTransferListener = mediaTransferListener;
        if (this.sideloadedManifest) {
            this.manifestLoaderErrorThrower = new LoaderErrorThrower.Dummy();
            this.processManifest();
        }
        else {
            this.manifestDataSource = this.manifestDataSourceFactory.createDataSource();
            this.manifestLoader = new Loader("Loader:Manifest");
            this.manifestLoaderErrorThrower = this.manifestLoader;
            this.manifestRefreshHandler = new Handler();
            this.startLoadingManifest();
        }
    }
    
    @Override
    public void releasePeriod(final MediaPeriod o) {
        ((SsMediaPeriod)o).release();
        this.mediaPeriods.remove(o);
    }
    
    public void releaseSourceInternal() {
        SsManifest manifest;
        if (this.sideloadedManifest) {
            manifest = this.manifest;
        }
        else {
            manifest = null;
        }
        this.manifest = manifest;
        this.manifestDataSource = null;
        this.manifestLoadStartTimestamp = 0L;
        final Loader manifestLoader = this.manifestLoader;
        if (manifestLoader != null) {
            manifestLoader.release();
            this.manifestLoader = null;
        }
        final Handler manifestRefreshHandler = this.manifestRefreshHandler;
        if (manifestRefreshHandler != null) {
            manifestRefreshHandler.removeCallbacksAndMessages((Object)null);
            this.manifestRefreshHandler = null;
        }
    }
}
