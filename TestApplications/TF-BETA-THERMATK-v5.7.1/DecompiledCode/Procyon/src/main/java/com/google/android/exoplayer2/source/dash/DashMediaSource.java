// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash;

import java.util.regex.Matcher;
import java.text.ParseException;
import android.text.TextUtils;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.InputStream;
import java.util.regex.Pattern;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import java.util.List;
import java.util.Map;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.source.dash.manifest.UtcTimingElement;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.C;
import android.os.SystemClock;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import android.util.SparseArray;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import java.io.IOException;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.BaseMediaSource;

public final class DashMediaSource extends BaseMediaSource
{
    private final DashChunkSource.Factory chunkSourceFactory;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private DataSource dataSource;
    private long elapsedRealtimeOffsetMs;
    private long expiredManifestPublishTimeUs;
    private int firstPeriodId;
    private Handler handler;
    private Uri initialManifestUri;
    private final long livePresentationDelayMs;
    private final boolean livePresentationDelayOverridesManifest;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private Loader loader;
    private DashManifest manifest;
    private final ManifestCallback manifestCallback;
    private final DataSource.Factory manifestDataSourceFactory;
    private final MediaSourceEventListener.EventDispatcher manifestEventDispatcher;
    private IOException manifestFatalError;
    private long manifestLoadEndTimestampMs;
    private final LoaderErrorThrower manifestLoadErrorThrower;
    private boolean manifestLoadPending;
    private long manifestLoadStartTimestampMs;
    private final ParsingLoadable.Parser<? extends DashManifest> manifestParser;
    private Uri manifestUri;
    private final Object manifestUriLock;
    private TransferListener mediaTransferListener;
    private final SparseArray<DashMediaPeriod> periodsById;
    private final PlayerEmsgHandler.PlayerEmsgCallback playerEmsgCallback;
    private final Runnable refreshManifestRunnable;
    private final boolean sideloadedManifest;
    private final Runnable simulateManifestRefreshRunnable;
    private int staleManifestReloadAttempt;
    private final Object tag;
    
    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.dash");
    }
    
    @Deprecated
    public DashMediaSource(final Uri p0, final DataSource.Factory p1, final DashChunkSource.Factory p2, final int p3, final long p4, final Handler p5, final MediaSourceEventListener p6) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: aload_2        
        //     3: new             Lcom/google/android/exoplayer2/source/dash/manifest/DashManifestParser;
        //     6: dup            
        //     7: invokespecial   com/google/android/exoplayer2/source/dash/manifest/DashManifestParser.<init>:()V
        //    10: aload_3        
        //    11: iload           4
        //    13: lload           5
        //    15: aload           7
        //    17: aload           8
        //    19: invokespecial   com/google/android/exoplayer2/source/dash/DashMediaSource.<init>:(Landroid/net/Uri;Lcom/google/android/exoplayer2/upstream/DataSource$Factory;Lcom/google/android/exoplayer2/upstream/ParsingLoadable$Parser;Lcom/google/android/exoplayer2/source/dash/DashChunkSource$Factory;IJLandroid/os/Handler;Lcom/google/android/exoplayer2/source/MediaSourceEventListener;)V
        //    22: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Invalid BootstrapMethods attribute entry: 2 additional arguments required for method java/lang/invoke/StringConcatFactory.makeConcatWithConstants, but only 1 specified.
        //     at com.strobel.assembler.ir.Error.invalidBootstrapMethodEntry(Error.java:244)
        //     at com.strobel.assembler.ir.MetadataReader.readAttributeCore(MetadataReader.java:267)
        //     at com.strobel.assembler.metadata.ClassFileReader.readAttributeCore(ClassFileReader.java:261)
        //     at com.strobel.assembler.ir.MetadataReader.inflateAttributes(MetadataReader.java:426)
        //     at com.strobel.assembler.metadata.ClassFileReader.visitAttributes(ClassFileReader.java:1134)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:439)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.decompiler.NoRetryMetadataSystem.resolveType(DecompilerDriver.java:476)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.assembler.metadata.TypeReference.equals(TypeReference.java:118)
        //     at java.base/java.util.AbstractCollection.contains(AbstractCollection.java:108)
        //     at com.strobel.assembler.metadata.ClassFileReader.defineMethods(ClassFileReader.java:994)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:441)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.decompiler.NoRetryMetadataSystem.resolveType(DecompilerDriver.java:476)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2075)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:922)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Deprecated
    public DashMediaSource(final Uri uri, final DataSource.Factory factory, final DashChunkSource.Factory factory2, final Handler handler, final MediaSourceEventListener mediaSourceEventListener) {
        this(uri, factory, factory2, 3, -1L, handler, mediaSourceEventListener);
    }
    
    @Deprecated
    public DashMediaSource(final Uri uri, final DataSource.Factory factory, final ParsingLoadable.Parser<? extends DashManifest> parser, final DashChunkSource.Factory factory2, final int n, final long n2, final Handler handler, final MediaSourceEventListener mediaSourceEventListener) {
        final DefaultCompositeSequenceableLoaderFactory defaultCompositeSequenceableLoaderFactory = new DefaultCompositeSequenceableLoaderFactory();
        final DefaultLoadErrorHandlingPolicy defaultLoadErrorHandlingPolicy = new DefaultLoadErrorHandlingPolicy(n);
        long n3;
        if (n2 == -1L) {
            n3 = 30000L;
        }
        else {
            n3 = n2;
        }
        this(null, uri, factory, parser, factory2, defaultCompositeSequenceableLoaderFactory, defaultLoadErrorHandlingPolicy, n3, n2 != -1L, null);
        if (handler != null && mediaSourceEventListener != null) {
            this.addEventListener(handler, mediaSourceEventListener);
        }
    }
    
    private DashMediaSource(final DashManifest manifest, final Uri uri, final DataSource.Factory manifestDataSourceFactory, final ParsingLoadable.Parser<? extends DashManifest> manifestParser, final DashChunkSource.Factory chunkSourceFactory, final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final long livePresentationDelayMs, final boolean livePresentationDelayOverridesManifest, final Object tag) {
        this.initialManifestUri = uri;
        this.manifest = manifest;
        this.manifestUri = uri;
        this.manifestDataSourceFactory = manifestDataSourceFactory;
        this.manifestParser = manifestParser;
        this.chunkSourceFactory = chunkSourceFactory;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.livePresentationDelayMs = livePresentationDelayMs;
        this.livePresentationDelayOverridesManifest = livePresentationDelayOverridesManifest;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.tag = tag;
        this.sideloadedManifest = (manifest != null);
        this.manifestEventDispatcher = this.createEventDispatcher(null);
        this.manifestUriLock = new Object();
        this.periodsById = (SparseArray<DashMediaPeriod>)new SparseArray();
        this.playerEmsgCallback = new DefaultPlayerEmsgCallback();
        this.expiredManifestPublishTimeUs = -9223372036854775807L;
        if (this.sideloadedManifest) {
            Assertions.checkState(manifest.dynamic ^ true);
            this.manifestCallback = null;
            this.refreshManifestRunnable = null;
            this.simulateManifestRefreshRunnable = null;
            this.manifestLoadErrorThrower = new LoaderErrorThrower.Dummy();
        }
        else {
            this.manifestCallback = new ManifestCallback();
            this.manifestLoadErrorThrower = new ManifestLoadErrorThrower();
            this.refreshManifestRunnable = new _$$Lambda$DashMediaSource$QbzYvqCY1TT8f0KClkalovG_Oxc(this);
            this.simulateManifestRefreshRunnable = new _$$Lambda$DashMediaSource$e1nzB_O4m3YSG1BkxQDKPaNvDa8(this);
        }
    }
    
    private long getManifestLoadRetryDelayMillis() {
        return Math.min((this.staleManifestReloadAttempt - 1) * 1000, 5000);
    }
    
    private long getNowUnixTimeUs() {
        if (this.elapsedRealtimeOffsetMs != 0L) {
            return C.msToUs(SystemClock.elapsedRealtime() + this.elapsedRealtimeOffsetMs);
        }
        return C.msToUs(System.currentTimeMillis());
    }
    
    private void onUtcTimestampResolutionError(final IOException ex) {
        Log.e("DashMediaSource", "Failed to resolve UtcTiming element.", ex);
        this.processManifest(true);
    }
    
    private void onUtcTimestampResolved(final long elapsedRealtimeOffsetMs) {
        this.elapsedRealtimeOffsetMs = elapsedRealtimeOffsetMs;
        this.processManifest(true);
    }
    
    private void processManifest(final boolean b) {
        for (int i = 0; i < this.periodsById.size(); ++i) {
            final int key = this.periodsById.keyAt(i);
            if (key >= this.firstPeriodId) {
                ((DashMediaPeriod)this.periodsById.valueAt(i)).updateManifest(this.manifest, key - this.firstPeriodId);
            }
        }
        int n = this.manifest.getPeriodCount() - 1;
        final PeriodSeekInfo periodSeekInfo = PeriodSeekInfo.createPeriodSeekInfo(this.manifest.getPeriod(0), this.manifest.getPeriodDurationUs(0));
        final PeriodSeekInfo periodSeekInfo2 = PeriodSeekInfo.createPeriodSeekInfo(this.manifest.getPeriod(n), this.manifest.getPeriodDurationUs(n));
        long availableStartTimeUs = periodSeekInfo.availableStartTimeUs;
        long availableEndTimeUs = periodSeekInfo2.availableEndTimeUs;
        boolean b3;
        if (this.manifest.dynamic && !periodSeekInfo2.isIndexExplicit) {
            final long min = Math.min(this.getNowUnixTimeUs() - C.msToUs(this.manifest.availabilityStartTimeMs) - C.msToUs(this.manifest.getPeriod(n).startMs), availableEndTimeUs);
            final long timeShiftBufferDepthMs = this.manifest.timeShiftBufferDepthMs;
            long n2 = availableStartTimeUs;
            if (timeShiftBufferDepthMs != -9223372036854775807L) {
                long b2;
                DashManifest manifest;
                for (b2 = min - C.msToUs(timeShiftBufferDepthMs); b2 < 0L && n > 0; --n, b2 += manifest.getPeriodDurationUs(n)) {
                    manifest = this.manifest;
                }
                if (n == 0) {
                    n2 = Math.max(availableStartTimeUs, b2);
                }
                else {
                    n2 = this.manifest.getPeriodDurationUs(0);
                }
            }
            availableStartTimeUs = n2;
            b3 = true;
            availableEndTimeUs = min;
        }
        else {
            b3 = false;
        }
        long n3 = availableEndTimeUs - availableStartTimeUs;
        for (int j = 0; j < this.manifest.getPeriodCount() - 1; ++j) {
            n3 += this.manifest.getPeriodDurationUs(j);
        }
        final DashManifest manifest2 = this.manifest;
        long min2;
        if (manifest2.dynamic) {
            long livePresentationDelayMs = this.livePresentationDelayMs;
            if (!this.livePresentationDelayOverridesManifest) {
                final long suggestedPresentationDelayMs = manifest2.suggestedPresentationDelayMs;
                livePresentationDelayMs = livePresentationDelayMs;
                if (suggestedPresentationDelayMs != -9223372036854775807L) {
                    livePresentationDelayMs = suggestedPresentationDelayMs;
                }
            }
            if ((min2 = n3 - C.msToUs(livePresentationDelayMs)) < 5000000L) {
                min2 = Math.min(5000000L, n3 / 2L);
            }
        }
        else {
            min2 = 0L;
        }
        final DashManifest manifest3 = this.manifest;
        final long availabilityStartTimeMs = manifest3.availabilityStartTimeMs;
        final long startMs = manifest3.getPeriod(0).startMs;
        final long usToMs = C.usToMs(availableStartTimeUs);
        final DashManifest manifest4 = this.manifest;
        this.refreshSourceInfo(new DashTimeline(manifest4.availabilityStartTimeMs, availabilityStartTimeMs + startMs + usToMs, this.firstPeriodId, availableStartTimeUs, n3, min2, manifest4, this.tag), this.manifest);
        if (!this.sideloadedManifest) {
            this.handler.removeCallbacks(this.simulateManifestRefreshRunnable);
            if (b3) {
                this.handler.postDelayed(this.simulateManifestRefreshRunnable, 5000L);
            }
            if (this.manifestLoadPending) {
                this.startLoadingManifest();
            }
            else if (b) {
                final DashManifest manifest5 = this.manifest;
                if (manifest5.dynamic) {
                    final long minUpdatePeriodMs = manifest5.minUpdatePeriodMs;
                    if (minUpdatePeriodMs != -9223372036854775807L) {
                        long n4 = minUpdatePeriodMs;
                        if (minUpdatePeriodMs == 0L) {
                            n4 = 5000L;
                        }
                        this.scheduleManifestRefresh(Math.max(0L, this.manifestLoadStartTimestampMs + n4 - SystemClock.elapsedRealtime()));
                    }
                }
            }
        }
    }
    
    private void resolveUtcTimingElement(final UtcTimingElement utcTimingElement) {
        final String schemeIdUri = utcTimingElement.schemeIdUri;
        if (!Util.areEqual(schemeIdUri, "urn:mpeg:dash:utc:direct:2014") && !Util.areEqual(schemeIdUri, "urn:mpeg:dash:utc:direct:2012")) {
            if (!Util.areEqual(schemeIdUri, "urn:mpeg:dash:utc:http-iso:2014") && !Util.areEqual(schemeIdUri, "urn:mpeg:dash:utc:http-iso:2012")) {
                if (!Util.areEqual(schemeIdUri, "urn:mpeg:dash:utc:http-xsdate:2014") && !Util.areEqual(schemeIdUri, "urn:mpeg:dash:utc:http-xsdate:2012")) {
                    this.onUtcTimestampResolutionError(new IOException("Unsupported UTC timing scheme"));
                }
                else {
                    this.resolveUtcTimingElementHttp(utcTimingElement, new XsDateTimeParser());
                }
            }
            else {
                this.resolveUtcTimingElementHttp(utcTimingElement, new Iso8601Parser());
            }
        }
        else {
            this.resolveUtcTimingElementDirect(utcTimingElement);
        }
    }
    
    private void resolveUtcTimingElementDirect(final UtcTimingElement utcTimingElement) {
        try {
            this.onUtcTimestampResolved(Util.parseXsDateTime(utcTimingElement.value) - this.manifestLoadEndTimestampMs);
        }
        catch (ParserException ex) {
            this.onUtcTimestampResolutionError(ex);
        }
    }
    
    private void resolveUtcTimingElementHttp(final UtcTimingElement utcTimingElement, final ParsingLoadable.Parser<Long> parser) {
        this.startLoading(new ParsingLoadable<Object>(this.dataSource, Uri.parse(utcTimingElement.value), 5, (ParsingLoadable.Parser<?>)parser), (Loader.Callback<ParsingLoadable<Object>>)new UtcTimestampCallback(), 1);
    }
    
    private void scheduleManifestRefresh(final long n) {
        this.handler.postDelayed(this.refreshManifestRunnable, n);
    }
    
    private <T> void startLoading(final ParsingLoadable<T> parsingLoadable, final Loader.Callback<ParsingLoadable<T>> callback, final int n) {
        this.manifestEventDispatcher.loadStarted(parsingLoadable.dataSpec, parsingLoadable.type, this.loader.startLoading(parsingLoadable, callback, n));
    }
    
    private void startLoadingManifest() {
        this.handler.removeCallbacks(this.refreshManifestRunnable);
        if (this.loader.isLoading()) {
            this.manifestLoadPending = true;
            return;
        }
        synchronized (this.manifestUriLock) {
            final Uri manifestUri = this.manifestUri;
            // monitorexit(this.manifestUriLock)
            this.manifestLoadPending = false;
            this.startLoading(new ParsingLoadable<Object>(this.dataSource, manifestUri, 4, (ParsingLoadable.Parser<?>)this.manifestParser), (Loader.Callback<ParsingLoadable<Object>>)this.manifestCallback, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(4));
        }
    }
    
    @Override
    public MediaPeriod createPeriod(final MediaPeriodId mediaPeriodId, final Allocator allocator, final long n) {
        final int n2 = (int)mediaPeriodId.periodUid - this.firstPeriodId;
        final DashMediaPeriod dashMediaPeriod = new DashMediaPeriod(this.firstPeriodId + n2, this.manifest, n2, this.chunkSourceFactory, this.mediaTransferListener, this.loadErrorHandlingPolicy, this.createEventDispatcher(mediaPeriodId, this.manifest.getPeriod(n2).startMs), this.elapsedRealtimeOffsetMs, this.manifestLoadErrorThrower, allocator, this.compositeSequenceableLoaderFactory, this.playerEmsgCallback);
        this.periodsById.put(dashMediaPeriod.id, (Object)dashMediaPeriod);
        return dashMediaPeriod;
    }
    
    @Override
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.manifestLoadErrorThrower.maybeThrowError();
    }
    
    void onDashManifestPublishTimeExpired(final long expiredManifestPublishTimeUs) {
        final long expiredManifestPublishTimeUs2 = this.expiredManifestPublishTimeUs;
        if (expiredManifestPublishTimeUs2 == -9223372036854775807L || expiredManifestPublishTimeUs2 < expiredManifestPublishTimeUs) {
            this.expiredManifestPublishTimeUs = expiredManifestPublishTimeUs;
        }
    }
    
    void onDashManifestRefreshRequested() {
        this.handler.removeCallbacks(this.simulateManifestRefreshRunnable);
        this.startLoadingManifest();
    }
    
    void onLoadCanceled(final ParsingLoadable<?> parsingLoadable, final long n, final long n2) {
        this.manifestEventDispatcher.loadCanceled(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), parsingLoadable.type, n, n2, parsingLoadable.bytesLoaded());
    }
    
    void onManifestLoadCompleted(final ParsingLoadable<DashManifest> parsingLoadable, final long manifestLoadEndTimestampMs, final long n) {
        this.manifestEventDispatcher.loadCompleted(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), parsingLoadable.type, manifestLoadEndTimestampMs, n, parsingLoadable.bytesLoaded());
        final DashManifest manifest = parsingLoadable.getResult();
        final DashManifest manifest2 = this.manifest;
        final int n2 = 0;
        int periodCount;
        if (manifest2 == null) {
            periodCount = 0;
        }
        else {
            periodCount = manifest2.getPeriodCount();
        }
        long startMs;
        int n3;
        for (startMs = manifest.getPeriod(0).startMs, n3 = 0; n3 < periodCount && this.manifest.getPeriod(n3).startMs < startMs; ++n3) {}
        if (manifest.dynamic) {
            boolean b = false;
            Label_0242: {
                if (periodCount - n3 > manifest.getPeriodCount()) {
                    Log.w("DashMediaSource", "Loaded out of sync manifest");
                }
                else {
                    final long expiredManifestPublishTimeUs = this.expiredManifestPublishTimeUs;
                    if (expiredManifestPublishTimeUs == -9223372036854775807L || manifest.publishTimeMs * 1000L > expiredManifestPublishTimeUs) {
                        b = false;
                        break Label_0242;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Loaded stale dynamic manifest: ");
                    sb.append(manifest.publishTimeMs);
                    sb.append(", ");
                    sb.append(this.expiredManifestPublishTimeUs);
                    Log.w("DashMediaSource", sb.toString());
                }
                b = true;
            }
            if (b) {
                if (this.staleManifestReloadAttempt++ < this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(parsingLoadable.type)) {
                    this.scheduleManifestRefresh(this.getManifestLoadRetryDelayMillis());
                }
                else {
                    this.manifestFatalError = new DashManifestStaleException();
                }
                return;
            }
            this.staleManifestReloadAttempt = 0;
        }
        this.manifest = manifest;
        final boolean manifestLoadPending = this.manifestLoadPending;
        final DashManifest manifest3 = this.manifest;
        this.manifestLoadPending = (manifestLoadPending & manifest3.dynamic);
        this.manifestLoadStartTimestampMs = manifestLoadEndTimestampMs - n;
        this.manifestLoadEndTimestampMs = manifestLoadEndTimestampMs;
        if (manifest3.location != null) {
            final Object manifestUriLock = this.manifestUriLock;
            // monitorenter(manifestUriLock)
            int n4 = n2;
            try {
                if (parsingLoadable.dataSpec.uri == this.manifestUri) {
                    n4 = 1;
                }
                if (n4 != 0) {
                    this.manifestUri = this.manifest.location;
                }
            }
            finally {
            }
            // monitorexit(manifestUriLock)
        }
        if (periodCount == 0) {
            final DashManifest manifest4 = this.manifest;
            if (manifest4.dynamic) {
                final UtcTimingElement utcTiming = manifest4.utcTiming;
                if (utcTiming != null) {
                    this.resolveUtcTimingElement(utcTiming);
                    return;
                }
            }
            this.processManifest(true);
        }
        else {
            this.firstPeriodId += n3;
            this.processManifest(true);
        }
    }
    
    Loader.LoadErrorAction onManifestLoadError(final ParsingLoadable<DashManifest> parsingLoadable, final long n, final long n2, final IOException ex) {
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
    
    void onUtcTimestampLoadCompleted(final ParsingLoadable<Long> parsingLoadable, final long n, final long n2) {
        this.manifestEventDispatcher.loadCompleted(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), parsingLoadable.type, n, n2, parsingLoadable.bytesLoaded());
        this.onUtcTimestampResolved(parsingLoadable.getResult() - n);
    }
    
    Loader.LoadErrorAction onUtcTimestampLoadError(final ParsingLoadable<Long> parsingLoadable, final long n, final long n2, final IOException ex) {
        this.manifestEventDispatcher.loadError(parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), parsingLoadable.type, n, n2, parsingLoadable.bytesLoaded(), ex, true);
        this.onUtcTimestampResolutionError(ex);
        return Loader.DONT_RETRY;
    }
    
    public void prepareSourceInternal(final TransferListener mediaTransferListener) {
        this.mediaTransferListener = mediaTransferListener;
        if (this.sideloadedManifest) {
            this.processManifest(false);
        }
        else {
            this.dataSource = this.manifestDataSourceFactory.createDataSource();
            this.loader = new Loader("Loader:DashMediaSource");
            this.handler = new Handler();
            this.startLoadingManifest();
        }
    }
    
    @Override
    public void releasePeriod(final MediaPeriod mediaPeriod) {
        final DashMediaPeriod dashMediaPeriod = (DashMediaPeriod)mediaPeriod;
        dashMediaPeriod.release();
        this.periodsById.remove(dashMediaPeriod.id);
    }
    
    public void releaseSourceInternal() {
        this.manifestLoadPending = false;
        this.dataSource = null;
        final Loader loader = this.loader;
        if (loader != null) {
            loader.release();
            this.loader = null;
        }
        this.manifestLoadStartTimestampMs = 0L;
        this.manifestLoadEndTimestampMs = 0L;
        DashManifest manifest;
        if (this.sideloadedManifest) {
            manifest = this.manifest;
        }
        else {
            manifest = null;
        }
        this.manifest = manifest;
        this.manifestUri = this.initialManifestUri;
        this.manifestFatalError = null;
        final Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacksAndMessages((Object)null);
            this.handler = null;
        }
        this.elapsedRealtimeOffsetMs = 0L;
        this.staleManifestReloadAttempt = 0;
        this.expiredManifestPublishTimeUs = -9223372036854775807L;
        this.firstPeriodId = 0;
        this.periodsById.clear();
    }
    
    private static final class DashTimeline extends Timeline
    {
        private final int firstPeriodId;
        private final DashManifest manifest;
        private final long offsetInFirstPeriodUs;
        private final long presentationStartTimeMs;
        private final long windowDefaultStartPositionUs;
        private final long windowDurationUs;
        private final long windowStartTimeMs;
        private final Object windowTag;
        
        public DashTimeline(final long presentationStartTimeMs, final long windowStartTimeMs, final int firstPeriodId, final long offsetInFirstPeriodUs, final long windowDurationUs, final long windowDefaultStartPositionUs, final DashManifest manifest, final Object windowTag) {
            this.presentationStartTimeMs = presentationStartTimeMs;
            this.windowStartTimeMs = windowStartTimeMs;
            this.firstPeriodId = firstPeriodId;
            this.offsetInFirstPeriodUs = offsetInFirstPeriodUs;
            this.windowDurationUs = windowDurationUs;
            this.windowDefaultStartPositionUs = windowDefaultStartPositionUs;
            this.manifest = manifest;
            this.windowTag = windowTag;
        }
        
        private long getAdjustedWindowDefaultStartPositionUs(long n) {
            final long windowDefaultStartPositionUs = this.windowDefaultStartPositionUs;
            if (!this.manifest.dynamic) {
                return windowDefaultStartPositionUs;
            }
            long n2 = windowDefaultStartPositionUs;
            if (n > 0L) {
                n = (n2 = windowDefaultStartPositionUs + n);
                if (n > this.windowDurationUs) {
                    return -9223372036854775807L;
                }
            }
            final long offsetInFirstPeriodUs = this.offsetInFirstPeriodUs;
            long n3;
            int n4;
            for (n = this.manifest.getPeriodDurationUs(0), n3 = offsetInFirstPeriodUs + n2, n4 = 0; n4 < this.manifest.getPeriodCount() - 1 && n3 >= n; n3 -= n, ++n4, n = this.manifest.getPeriodDurationUs(n4)) {}
            final com.google.android.exoplayer2.source.dash.manifest.Period period = this.manifest.getPeriod(n4);
            final int adaptationSetIndex = period.getAdaptationSetIndex(2);
            if (adaptationSetIndex == -1) {
                return n2;
            }
            final DashSegmentIndex index = period.adaptationSets.get(adaptationSetIndex).representations.get(0).getIndex();
            long n5 = n2;
            if (index != null) {
                if (index.getSegmentCount(n) == 0) {
                    n5 = n2;
                }
                else {
                    n5 = n2 + index.getTimeUs(index.getSegmentNum(n3, n)) - n3;
                }
            }
            return n5;
        }
        
        @Override
        public int getIndexOfPeriod(final Object o) {
            if (!(o instanceof Integer)) {
                return -1;
            }
            final int n = (int)o - this.firstPeriodId;
            int n2;
            if (n < 0 || (n2 = n) >= this.getPeriodCount()) {
                n2 = -1;
            }
            return n2;
        }
        
        @Override
        public Period getPeriod(final int n, final Period period, final boolean b) {
            Assertions.checkIndex(n, 0, this.getPeriodCount());
            Object value = null;
            String id;
            if (b) {
                id = this.manifest.getPeriod(n).id;
            }
            else {
                id = null;
            }
            if (b) {
                value = this.firstPeriodId + n;
            }
            period.set(id, value, 0, this.manifest.getPeriodDurationUs(n), C.msToUs(this.manifest.getPeriod(n).startMs - this.manifest.getPeriod(0).startMs) - this.offsetInFirstPeriodUs);
            return period;
        }
        
        @Override
        public int getPeriodCount() {
            return this.manifest.getPeriodCount();
        }
        
        @Override
        public Object getUidOfPeriod(final int n) {
            Assertions.checkIndex(n, 0, this.getPeriodCount());
            return this.firstPeriodId + n;
        }
        
        @Override
        public Window getWindow(final int n, final Window window, final boolean b, long adjustedWindowDefaultStartPositionUs) {
            Assertions.checkIndex(n, 0, 1);
            adjustedWindowDefaultStartPositionUs = this.getAdjustedWindowDefaultStartPositionUs(adjustedWindowDefaultStartPositionUs);
            Object windowTag;
            if (b) {
                windowTag = this.windowTag;
            }
            else {
                windowTag = null;
            }
            final DashManifest manifest = this.manifest;
            window.set(windowTag, this.presentationStartTimeMs, this.windowStartTimeMs, true, manifest.dynamic && manifest.minUpdatePeriodMs != -9223372036854775807L && manifest.durationMs == -9223372036854775807L, adjustedWindowDefaultStartPositionUs, this.windowDurationUs, 0, this.getPeriodCount() - 1, this.offsetInFirstPeriodUs);
            return window;
        }
        
        @Override
        public int getWindowCount() {
            return 1;
        }
    }
    
    private final class DefaultPlayerEmsgCallback implements PlayerEmsgCallback
    {
        @Override
        public void onDashManifestPublishTimeExpired(final long n) {
            DashMediaSource.this.onDashManifestPublishTimeExpired(n);
        }
        
        @Override
        public void onDashManifestRefreshRequested() {
            DashMediaSource.this.onDashManifestRefreshRequested();
        }
    }
    
    static final class Iso8601Parser implements Parser<Long>
    {
        private static final Pattern TIMESTAMP_WITH_TIMEZONE_PATTERN;
        
        static {
            TIMESTAMP_WITH_TIMEZONE_PATTERN = Pattern.compile("(.+?)(Z|((\\+|-|\u2212)(\\d\\d)(:?(\\d\\d))?))");
        }
        
        public Long parse(final Uri uri, final InputStream in) throws IOException {
            final String line = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8"))).readLine();
            try {
                final Matcher matcher = Iso8601Parser.TIMESTAMP_WITH_TIMEZONE_PATTERN.matcher(line);
                if (matcher.matches()) {
                    final String group = matcher.group(1);
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    final long time = simpleDateFormat.parse(group).getTime();
                    long l;
                    if ("Z".equals(matcher.group(2))) {
                        l = time;
                    }
                    else {
                        long n;
                        if ("+".equals(matcher.group(4))) {
                            n = 1L;
                        }
                        else {
                            n = -1L;
                        }
                        final long long1 = Long.parseLong(matcher.group(5));
                        final String group2 = matcher.group(7);
                        long long2;
                        if (TextUtils.isEmpty((CharSequence)group2)) {
                            long2 = 0L;
                        }
                        else {
                            long2 = Long.parseLong(group2);
                        }
                        l = time - n * ((long1 * 60L + long2) * 60L * 1000L);
                    }
                    return l;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Couldn't parse timestamp: ");
                sb.append(line);
                throw new ParserException(sb.toString());
            }
            catch (ParseException ex) {
                throw new ParserException(ex);
            }
        }
    }
    
    private final class ManifestCallback implements Callback<ParsingLoadable<DashManifest>>
    {
        public void onLoadCanceled(final ParsingLoadable<DashManifest> parsingLoadable, final long n, final long n2, final boolean b) {
            DashMediaSource.this.onLoadCanceled(parsingLoadable, n, n2);
        }
        
        public void onLoadCompleted(final ParsingLoadable<DashManifest> parsingLoadable, final long n, final long n2) {
            DashMediaSource.this.onManifestLoadCompleted(parsingLoadable, n, n2);
        }
        
        public LoadErrorAction onLoadError(final ParsingLoadable<DashManifest> parsingLoadable, final long n, final long n2, final IOException ex, final int n3) {
            return DashMediaSource.this.onManifestLoadError(parsingLoadable, n, n2, ex);
        }
    }
    
    final class ManifestLoadErrorThrower implements LoaderErrorThrower
    {
        private void maybeThrowManifestError() throws IOException {
            if (DashMediaSource.this.manifestFatalError == null) {
                return;
            }
            throw DashMediaSource.this.manifestFatalError;
        }
        
        @Override
        public void maybeThrowError() throws IOException {
            DashMediaSource.this.loader.maybeThrowError();
            this.maybeThrowManifestError();
        }
    }
    
    private static final class PeriodSeekInfo
    {
        public final long availableEndTimeUs;
        public final long availableStartTimeUs;
        public final boolean isIndexExplicit;
        
        private PeriodSeekInfo(final boolean isIndexExplicit, final long availableStartTimeUs, final long availableEndTimeUs) {
            this.isIndexExplicit = isIndexExplicit;
            this.availableStartTimeUs = availableStartTimeUs;
            this.availableEndTimeUs = availableEndTimeUs;
        }
        
        public static PeriodSeekInfo createPeriodSeekInfo(final Period period, final long n) {
            while (true) {
                for (int size = period.adaptationSets.size(), i = 0; i < size; ++i) {
                    final int type = period.adaptationSets.get(i).type;
                    if (type == 1 || type == 2) {
                        final boolean b = true;
                        long min = Long.MAX_VALUE;
                        int j = 0;
                        boolean b2 = false;
                        boolean b3 = false;
                        long max = 0L;
                        while (j < size) {
                            final AdaptationSet set = period.adaptationSets.get(j);
                            if (!b || set.type != 3) {
                                final DashSegmentIndex index = set.representations.get(0).getIndex();
                                if (index == null) {
                                    return new PeriodSeekInfo(true, 0L, n);
                                }
                                b3 |= index.isExplicit();
                                final int segmentCount = index.getSegmentCount(n);
                                if (segmentCount == 0) {
                                    b2 = true;
                                    max = 0L;
                                    min = 0L;
                                }
                                else if (!b2) {
                                    final long firstSegmentNum = index.getFirstSegmentNum();
                                    max = Math.max(max, index.getTimeUs(firstSegmentNum));
                                    if (segmentCount != -1) {
                                        final long n2 = firstSegmentNum + segmentCount - 1L;
                                        min = Math.min(min, index.getTimeUs(n2) + index.getDurationUs(n2, n));
                                    }
                                }
                            }
                            ++j;
                        }
                        return new PeriodSeekInfo(b3, max, min);
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
    
    private final class UtcTimestampCallback implements Callback<ParsingLoadable<Long>>
    {
        public void onLoadCanceled(final ParsingLoadable<Long> parsingLoadable, final long n, final long n2, final boolean b) {
            DashMediaSource.this.onLoadCanceled(parsingLoadable, n, n2);
        }
        
        public void onLoadCompleted(final ParsingLoadable<Long> parsingLoadable, final long n, final long n2) {
            DashMediaSource.this.onUtcTimestampLoadCompleted(parsingLoadable, n, n2);
        }
        
        public LoadErrorAction onLoadError(final ParsingLoadable<Long> parsingLoadable, final long n, final long n2, final IOException ex, final int n3) {
            return DashMediaSource.this.onUtcTimestampLoadError(parsingLoadable, n, n2, ex);
        }
    }
    
    private static final class XsDateTimeParser implements Parser<Long>
    {
        public Long parse(final Uri uri, final InputStream in) throws IOException {
            return Util.parseXsDateTime(new BufferedReader(new InputStreamReader(in)).readLine());
        }
    }
}
