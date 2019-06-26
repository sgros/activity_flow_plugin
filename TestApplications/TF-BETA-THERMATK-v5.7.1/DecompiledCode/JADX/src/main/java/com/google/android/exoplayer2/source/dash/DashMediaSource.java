package com.google.android.exoplayer2.source.dash;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.SparseArray;
import com.google.android.exoplayer2.C0131C;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.dash.DashChunkSource.Factory;
import com.google.android.exoplayer2.source.dash.PlayerEmsgHandler.PlayerEmsgCallback;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.source.dash.manifest.UtcTimingElement;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.Loader.Callback;
import com.google.android.exoplayer2.upstream.Loader.LoadErrorAction;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower.Dummy;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DashMediaSource extends BaseMediaSource {
    private final Factory chunkSourceFactory;
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
    private final EventDispatcher manifestEventDispatcher;
    private IOException manifestFatalError;
    private long manifestLoadEndTimestampMs;
    private final LoaderErrorThrower manifestLoadErrorThrower;
    private boolean manifestLoadPending;
    private long manifestLoadStartTimestampMs;
    private final Parser<? extends DashManifest> manifestParser;
    private Uri manifestUri;
    private final Object manifestUriLock;
    private TransferListener mediaTransferListener;
    private final SparseArray<DashMediaPeriod> periodsById;
    private final PlayerEmsgCallback playerEmsgCallback;
    private final Runnable refreshManifestRunnable;
    private final boolean sideloadedManifest;
    private final Runnable simulateManifestRefreshRunnable;
    private int staleManifestReloadAttempt;
    private final Object tag;

    private static final class PeriodSeekInfo {
        public final long availableEndTimeUs;
        public final long availableStartTimeUs;
        public final boolean isIndexExplicit;

        public static PeriodSeekInfo createPeriodSeekInfo(Period period, long j) {
            int i;
            Object obj;
            Period period2 = period;
            long j2 = j;
            int size = period2.adaptationSets.size();
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                i = ((AdaptationSet) period2.adaptationSets.get(i3)).type;
                if (i == 1 || i == 2) {
                    obj = 1;
                    break;
                }
            }
            obj = null;
            long j3 = TimestampAdjuster.DO_NOT_OFFSET;
            int i4 = 0;
            Object obj2 = null;
            int i5 = 0;
            long j4 = 0;
            while (i4 < size) {
                int i6;
                Object obj3;
                AdaptationSet adaptationSet = (AdaptationSet) period2.adaptationSets.get(i4);
                if (obj == null || adaptationSet.type != 3) {
                    DashSegmentIndex index = ((Representation) adaptationSet.representations.get(i2)).getIndex();
                    if (index == null) {
                        return new PeriodSeekInfo(true, 0, j);
                    }
                    i = index.isExplicit() | i5;
                    int segmentCount = index.getSegmentCount(j2);
                    if (segmentCount == 0) {
                        i6 = size;
                        obj3 = obj;
                        i5 = i;
                        obj2 = 1;
                        j4 = 0;
                        j3 = 0;
                    } else {
                        if (obj2 == null) {
                            obj3 = obj;
                            long firstSegmentNum = index.getFirstSegmentNum();
                            i6 = size;
                            long max = Math.max(j4, index.getTimeUs(firstSegmentNum));
                            if (segmentCount != -1) {
                                firstSegmentNum = (firstSegmentNum + ((long) segmentCount)) - 1;
                                j4 = max;
                                j3 = Math.min(j3, index.getTimeUs(firstSegmentNum) + index.getDurationUs(firstSegmentNum, j2));
                            } else {
                                j4 = max;
                            }
                        } else {
                            i6 = size;
                            obj3 = obj;
                        }
                        i5 = i;
                    }
                } else {
                    i6 = size;
                    obj3 = obj;
                }
                i4++;
                i2 = 0;
                period2 = period;
                obj = obj3;
                size = i6;
            }
            return new PeriodSeekInfo(i5, j4, j3);
        }

        private PeriodSeekInfo(boolean z, long j, long j2) {
            this.isIndexExplicit = z;
            this.availableStartTimeUs = j;
            this.availableEndTimeUs = j2;
        }
    }

    private static final class DashTimeline extends Timeline {
        private final int firstPeriodId;
        private final DashManifest manifest;
        private final long offsetInFirstPeriodUs;
        private final long presentationStartTimeMs;
        private final long windowDefaultStartPositionUs;
        private final long windowDurationUs;
        private final long windowStartTimeMs;
        private final Object windowTag;

        public int getWindowCount() {
            return 1;
        }

        public DashTimeline(long j, long j2, int i, long j3, long j4, long j5, DashManifest dashManifest, Object obj) {
            this.presentationStartTimeMs = j;
            this.windowStartTimeMs = j2;
            this.firstPeriodId = i;
            this.offsetInFirstPeriodUs = j3;
            this.windowDurationUs = j4;
            this.windowDefaultStartPositionUs = j5;
            this.manifest = dashManifest;
            this.windowTag = obj;
        }

        public int getPeriodCount() {
            return this.manifest.getPeriodCount();
        }

        public Timeline.Period getPeriod(int i, Timeline.Period period, boolean z) {
            Assertions.checkIndex(i, 0, getPeriodCount());
            Integer num = null;
            Object obj = z ? this.manifest.getPeriod(i).f24id : null;
            if (z) {
                num = Integer.valueOf(this.firstPeriodId + i);
            }
            period.set(obj, num, 0, this.manifest.getPeriodDurationUs(i), C0131C.msToUs(this.manifest.getPeriod(i).startMs - this.manifest.getPeriod(0).startMs) - this.offsetInFirstPeriodUs);
            return period;
        }

        public Window getWindow(int i, Window window, boolean z, long j) {
            Assertions.checkIndex(i, 0, 1);
            long adjustedWindowDefaultStartPositionUs = getAdjustedWindowDefaultStartPositionUs(j);
            Object obj = z ? this.windowTag : null;
            DashManifest dashManifest = this.manifest;
            boolean z2 = dashManifest.dynamic && dashManifest.minUpdatePeriodMs != -9223372036854775807L && dashManifest.durationMs == -9223372036854775807L;
            window.set(obj, this.presentationStartTimeMs, this.windowStartTimeMs, true, z2, adjustedWindowDefaultStartPositionUs, this.windowDurationUs, 0, getPeriodCount() - 1, this.offsetInFirstPeriodUs);
            return window;
        }

        public int getIndexOfPeriod(Object obj) {
            if (!(obj instanceof Integer)) {
                return -1;
            }
            int intValue = ((Integer) obj).intValue() - this.firstPeriodId;
            if (intValue < 0 || intValue >= getPeriodCount()) {
                intValue = -1;
            }
            return intValue;
        }

        private long getAdjustedWindowDefaultStartPositionUs(long j) {
            long j2 = this.windowDefaultStartPositionUs;
            if (!this.manifest.dynamic) {
                return j2;
            }
            if (j > 0) {
                j2 += j;
                if (j2 > this.windowDurationUs) {
                    return -9223372036854775807L;
                }
            }
            j = this.offsetInFirstPeriodUs + j2;
            long periodDurationUs = this.manifest.getPeriodDurationUs(0);
            long j3 = j;
            int i = 0;
            while (i < this.manifest.getPeriodCount() - 1 && j3 >= periodDurationUs) {
                j3 -= periodDurationUs;
                i++;
                periodDurationUs = this.manifest.getPeriodDurationUs(i);
            }
            Period period = this.manifest.getPeriod(i);
            int adaptationSetIndex = period.getAdaptationSetIndex(2);
            if (adaptationSetIndex == -1) {
                return j2;
            }
            DashSegmentIndex index = ((Representation) ((AdaptationSet) period.adaptationSets.get(adaptationSetIndex)).representations.get(0)).getIndex();
            if (!(index == null || index.getSegmentCount(periodDurationUs) == 0)) {
                j2 = (j2 + index.getTimeUs(index.getSegmentNum(j3, periodDurationUs))) - j3;
            }
            return j2;
        }

        public Object getUidOfPeriod(int i) {
            Assertions.checkIndex(i, 0, getPeriodCount());
            return Integer.valueOf(this.firstPeriodId + i);
        }
    }

    private final class DefaultPlayerEmsgCallback implements PlayerEmsgCallback {
        private DefaultPlayerEmsgCallback() {
        }

        public void onDashManifestRefreshRequested() {
            DashMediaSource.this.onDashManifestRefreshRequested();
        }

        public void onDashManifestPublishTimeExpired(long j) {
            DashMediaSource.this.onDashManifestPublishTimeExpired(j);
        }
    }

    static final class Iso8601Parser implements Parser<Long> {
        private static final Pattern TIMESTAMP_WITH_TIMEZONE_PATTERN = Pattern.compile("(.+?)(Z|((\\+|-|−)(\\d\\d)(:?(\\d\\d))?))");

        Iso8601Parser() {
        }

        public Long parse(Uri uri, InputStream inputStream) throws IOException {
            String readLine = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8"))).readLine();
            try {
                Matcher matcher = TIMESTAMP_WITH_TIMEZONE_PATTERN.matcher(readLine);
                if (matcher.matches()) {
                    readLine = matcher.group(1);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    long time = simpleDateFormat.parse(readLine).getTime();
                    if (!"Z".equals(matcher.group(2))) {
                        long j = "+".equals(matcher.group(4)) ? 1 : -1;
                        long parseLong = Long.parseLong(matcher.group(5));
                        readLine = matcher.group(7);
                        time -= j * ((((parseLong * 60) + (TextUtils.isEmpty(readLine) ? 0 : Long.parseLong(readLine))) * 60) * 1000);
                    }
                    return Long.valueOf(time);
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Couldn't parse timestamp: ");
                stringBuilder.append(readLine);
                throw new ParserException(stringBuilder.toString());
            } catch (ParseException e) {
                throw new ParserException(e);
            }
        }
    }

    private final class ManifestCallback implements Callback<ParsingLoadable<DashManifest>> {
        private ManifestCallback() {
        }

        public void onLoadCompleted(ParsingLoadable<DashManifest> parsingLoadable, long j, long j2) {
            DashMediaSource.this.onManifestLoadCompleted(parsingLoadable, j, j2);
        }

        public void onLoadCanceled(ParsingLoadable<DashManifest> parsingLoadable, long j, long j2, boolean z) {
            DashMediaSource.this.onLoadCanceled(parsingLoadable, j, j2);
        }

        public LoadErrorAction onLoadError(ParsingLoadable<DashManifest> parsingLoadable, long j, long j2, IOException iOException, int i) {
            return DashMediaSource.this.onManifestLoadError(parsingLoadable, j, j2, iOException);
        }
    }

    final class ManifestLoadErrorThrower implements LoaderErrorThrower {
        ManifestLoadErrorThrower() {
        }

        public void maybeThrowError() throws IOException {
            DashMediaSource.this.loader.maybeThrowError();
            maybeThrowManifestError();
        }

        private void maybeThrowManifestError() throws IOException {
            if (DashMediaSource.this.manifestFatalError != null) {
                throw DashMediaSource.this.manifestFatalError;
            }
        }
    }

    private final class UtcTimestampCallback implements Callback<ParsingLoadable<Long>> {
        private UtcTimestampCallback() {
        }

        public void onLoadCompleted(ParsingLoadable<Long> parsingLoadable, long j, long j2) {
            DashMediaSource.this.onUtcTimestampLoadCompleted(parsingLoadable, j, j2);
        }

        public void onLoadCanceled(ParsingLoadable<Long> parsingLoadable, long j, long j2, boolean z) {
            DashMediaSource.this.onLoadCanceled(parsingLoadable, j, j2);
        }

        public LoadErrorAction onLoadError(ParsingLoadable<Long> parsingLoadable, long j, long j2, IOException iOException, int i) {
            return DashMediaSource.this.onUtcTimestampLoadError(parsingLoadable, j, j2, iOException);
        }
    }

    private static final class XsDateTimeParser implements Parser<Long> {
        private XsDateTimeParser() {
        }

        public Long parse(Uri uri, InputStream inputStream) throws IOException {
            return Long.valueOf(Util.parseXsDateTime(new BufferedReader(new InputStreamReader(inputStream)).readLine()));
        }
    }

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.dash");
    }

    @Deprecated
    public DashMediaSource(Uri uri, DataSource.Factory factory, Factory factory2, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        this(uri, factory, factory2, 3, -1, handler, mediaSourceEventListener);
    }

    @Deprecated
    public DashMediaSource(Uri uri, DataSource.Factory factory, Factory factory2, int i, long j, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        this(uri, factory, new DashManifestParser(), factory2, i, j, handler, mediaSourceEventListener);
    }

    @Deprecated
    public DashMediaSource(Uri uri, DataSource.Factory factory, Parser<? extends DashManifest> parser, Factory factory2, int i, long j, Handler handler, MediaSourceEventListener mediaSourceEventListener) {
        Handler handler2 = handler;
        MediaSourceEventListener mediaSourceEventListener2 = mediaSourceEventListener;
        this(null, uri, factory, parser, factory2, new DefaultCompositeSequenceableLoaderFactory(), new DefaultLoadErrorHandlingPolicy(i), j == -1 ? 30000 : j, j != -1, null);
        if (handler2 == null || mediaSourceEventListener2 == null) {
            return;
        }
        addEventListener(handler2, mediaSourceEventListener2);
    }

    private DashMediaSource(DashManifest dashManifest, Uri uri, DataSource.Factory factory, Parser<? extends DashManifest> parser, Factory factory2, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, LoadErrorHandlingPolicy loadErrorHandlingPolicy, long j, boolean z, Object obj) {
        this.initialManifestUri = uri;
        this.manifest = dashManifest;
        this.manifestUri = uri;
        this.manifestDataSourceFactory = factory;
        this.manifestParser = parser;
        this.chunkSourceFactory = factory2;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.livePresentationDelayMs = j;
        this.livePresentationDelayOverridesManifest = z;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.tag = obj;
        this.sideloadedManifest = dashManifest != null;
        this.manifestEventDispatcher = createEventDispatcher(null);
        this.manifestUriLock = new Object();
        this.periodsById = new SparseArray();
        this.playerEmsgCallback = new DefaultPlayerEmsgCallback();
        this.expiredManifestPublishTimeUs = -9223372036854775807L;
        if (this.sideloadedManifest) {
            Assertions.checkState(dashManifest.dynamic ^ 1);
            this.manifestCallback = null;
            this.refreshManifestRunnable = null;
            this.simulateManifestRefreshRunnable = null;
            this.manifestLoadErrorThrower = new Dummy();
            return;
        }
        this.manifestCallback = new ManifestCallback();
        this.manifestLoadErrorThrower = new ManifestLoadErrorThrower();
        this.refreshManifestRunnable = new C0206-$$Lambda$DashMediaSource$QbzYvqCY1TT8f0KClkalovG-Oxc(this);
        this.simulateManifestRefreshRunnable = new C0207-$$Lambda$DashMediaSource$e1nzB-O4m3YSG1BkxQDKPaNvDa8(this);
    }

    public /* synthetic */ void lambda$new$0$DashMediaSource() {
        processManifest(false);
    }

    public void prepareSourceInternal(TransferListener transferListener) {
        this.mediaTransferListener = transferListener;
        if (this.sideloadedManifest) {
            processManifest(false);
            return;
        }
        this.dataSource = this.manifestDataSourceFactory.createDataSource();
        this.loader = new Loader("Loader:DashMediaSource");
        this.handler = new Handler();
        startLoadingManifest();
    }

    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.manifestLoadErrorThrower.maybeThrowError();
    }

    public MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator, long j) {
        MediaPeriodId mediaPeriodId2 = mediaPeriodId;
        int intValue = ((Integer) mediaPeriodId2.periodUid).intValue() - this.firstPeriodId;
        EventDispatcher createEventDispatcher = createEventDispatcher(mediaPeriodId2, this.manifest.getPeriod(intValue).startMs);
        DashMediaPeriod dashMediaPeriod = new DashMediaPeriod(this.firstPeriodId + intValue, this.manifest, intValue, this.chunkSourceFactory, this.mediaTransferListener, this.loadErrorHandlingPolicy, createEventDispatcher, this.elapsedRealtimeOffsetMs, this.manifestLoadErrorThrower, allocator, this.compositeSequenceableLoaderFactory, this.playerEmsgCallback);
        this.periodsById.put(dashMediaPeriod.f637id, dashMediaPeriod);
        return dashMediaPeriod;
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        DashMediaPeriod dashMediaPeriod = (DashMediaPeriod) mediaPeriod;
        dashMediaPeriod.release();
        this.periodsById.remove(dashMediaPeriod.f637id);
    }

    public void releaseSourceInternal() {
        this.manifestLoadPending = false;
        this.dataSource = null;
        Loader loader = this.loader;
        if (loader != null) {
            loader.release();
            this.loader = null;
        }
        this.manifestLoadStartTimestampMs = 0;
        this.manifestLoadEndTimestampMs = 0;
        this.manifest = this.sideloadedManifest ? this.manifest : null;
        this.manifestUri = this.initialManifestUri;
        this.manifestFatalError = null;
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.handler = null;
        }
        this.elapsedRealtimeOffsetMs = 0;
        this.staleManifestReloadAttempt = 0;
        this.expiredManifestPublishTimeUs = -9223372036854775807L;
        this.firstPeriodId = 0;
        this.periodsById.clear();
    }

    /* Access modifiers changed, original: 0000 */
    public void onDashManifestRefreshRequested() {
        this.handler.removeCallbacks(this.simulateManifestRefreshRunnable);
        startLoadingManifest();
    }

    /* Access modifiers changed, original: 0000 */
    public void onDashManifestPublishTimeExpired(long j) {
        long j2 = this.expiredManifestPublishTimeUs;
        if (j2 == -9223372036854775807L || j2 < j) {
            this.expiredManifestPublishTimeUs = j;
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00b9  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0099  */
    public void onManifestLoadCompleted(com.google.android.exoplayer2.upstream.ParsingLoadable<com.google.android.exoplayer2.source.dash.manifest.DashManifest> r18, long r19, long r21) {
        /*
        r17 = this;
        r1 = r17;
        r0 = r18;
        r13 = r19;
        r2 = r1.manifestEventDispatcher;
        r3 = r0.dataSpec;
        r4 = r18.getUri();
        r5 = r18.getResponseHeaders();
        r6 = r0.type;
        r11 = r18.bytesLoaded();
        r7 = r19;
        r9 = r21;
        r2.loadCompleted(r3, r4, r5, r6, r7, r9, r11);
        r2 = r18.getResult();
        r2 = (com.google.android.exoplayer2.source.dash.manifest.DashManifest) r2;
        r3 = r1.manifest;
        r4 = 0;
        if (r3 != 0) goto L_0x002c;
    L_0x002a:
        r3 = 0;
        goto L_0x0030;
    L_0x002c:
        r3 = r3.getPeriodCount();
    L_0x0030:
        r5 = r2.getPeriod(r4);
        r5 = r5.startMs;
        r7 = 0;
    L_0x0037:
        if (r7 >= r3) goto L_0x0048;
    L_0x0039:
        r8 = r1.manifest;
        r8 = r8.getPeriod(r7);
        r8 = r8.startMs;
        r10 = (r8 > r5 ? 1 : (r8 == r5 ? 0 : -1));
        if (r10 >= 0) goto L_0x0048;
    L_0x0045:
        r7 = r7 + 1;
        goto L_0x0037;
    L_0x0048:
        r5 = r2.dynamic;
        r6 = 1;
        if (r5 == 0) goto L_0x00bb;
    L_0x004d:
        r5 = r3 - r7;
        r8 = r2.getPeriodCount();
        if (r5 <= r8) goto L_0x005e;
    L_0x0055:
        r5 = "DashMediaSource";
        r8 = "Loaded out of sync manifest";
        com.google.android.exoplayer2.util.Log.m18w(r5, r8);
    L_0x005c:
        r5 = 1;
        goto L_0x0097;
    L_0x005e:
        r8 = r1.expiredManifestPublishTimeUs;
        r10 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r5 == 0) goto L_0x0096;
    L_0x0069:
        r10 = r2.publishTimeMs;
        r15 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r10 = r10 * r15;
        r5 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1));
        if (r5 > 0) goto L_0x0096;
    L_0x0073:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r8 = "Loaded stale dynamic manifest: ";
        r5.append(r8);
        r8 = r2.publishTimeMs;
        r5.append(r8);
        r8 = ", ";
        r5.append(r8);
        r8 = r1.expiredManifestPublishTimeUs;
        r5.append(r8);
        r5 = r5.toString();
        r8 = "DashMediaSource";
        com.google.android.exoplayer2.util.Log.m18w(r8, r5);
        goto L_0x005c;
    L_0x0096:
        r5 = 0;
    L_0x0097:
        if (r5 == 0) goto L_0x00b9;
    L_0x0099:
        r2 = r1.staleManifestReloadAttempt;
        r3 = r2 + 1;
        r1.staleManifestReloadAttempt = r3;
        r3 = r1.loadErrorHandlingPolicy;
        r0 = r0.type;
        r0 = r3.getMinimumLoadableRetryCount(r0);
        if (r2 >= r0) goto L_0x00b1;
    L_0x00a9:
        r2 = r17.getManifestLoadRetryDelayMillis();
        r1.scheduleManifestRefresh(r2);
        goto L_0x00b8;
    L_0x00b1:
        r0 = new com.google.android.exoplayer2.source.dash.DashManifestStaleException;
        r0.<init>();
        r1.manifestFatalError = r0;
    L_0x00b8:
        return;
    L_0x00b9:
        r1.staleManifestReloadAttempt = r4;
    L_0x00bb:
        r1.manifest = r2;
        r2 = r1.manifestLoadPending;
        r5 = r1.manifest;
        r8 = r5.dynamic;
        r2 = r2 & r8;
        r1.manifestLoadPending = r2;
        r8 = r13 - r21;
        r1.manifestLoadStartTimestampMs = r8;
        r1.manifestLoadEndTimestampMs = r13;
        r2 = r5.location;
        if (r2 == 0) goto L_0x00e9;
    L_0x00d0:
        r2 = r1.manifestUriLock;
        monitor-enter(r2);
        r0 = r0.dataSpec;	 Catch:{ all -> 0x00e6 }
        r0 = r0.uri;	 Catch:{ all -> 0x00e6 }
        r5 = r1.manifestUri;	 Catch:{ all -> 0x00e6 }
        if (r0 != r5) goto L_0x00dc;
    L_0x00db:
        r4 = 1;
    L_0x00dc:
        if (r4 == 0) goto L_0x00e4;
    L_0x00de:
        r0 = r1.manifest;	 Catch:{ all -> 0x00e6 }
        r0 = r0.location;	 Catch:{ all -> 0x00e6 }
        r1.manifestUri = r0;	 Catch:{ all -> 0x00e6 }
    L_0x00e4:
        monitor-exit(r2);	 Catch:{ all -> 0x00e6 }
        goto L_0x00e9;
    L_0x00e6:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x00e6 }
        throw r0;
    L_0x00e9:
        if (r3 != 0) goto L_0x00fd;
    L_0x00eb:
        r0 = r1.manifest;
        r2 = r0.dynamic;
        if (r2 == 0) goto L_0x00f9;
    L_0x00f1:
        r0 = r0.utcTiming;
        if (r0 == 0) goto L_0x00f9;
    L_0x00f5:
        r1.resolveUtcTimingElement(r0);
        goto L_0x0105;
    L_0x00f9:
        r1.processManifest(r6);
        goto L_0x0105;
    L_0x00fd:
        r0 = r1.firstPeriodId;
        r0 = r0 + r7;
        r1.firstPeriodId = r0;
        r1.processManifest(r6);
    L_0x0105:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.DashMediaSource.onManifestLoadCompleted(com.google.android.exoplayer2.upstream.ParsingLoadable, long, long):void");
    }

    /* Access modifiers changed, original: 0000 */
    public LoadErrorAction onManifestLoadError(ParsingLoadable<DashManifest> parsingLoadable, long j, long j2, IOException iOException) {
        ParsingLoadable<DashManifest> parsingLoadable2 = parsingLoadable;
        IOException iOException2 = iOException;
        boolean z = iOException2 instanceof ParserException;
        this.manifestEventDispatcher.loadError(parsingLoadable2.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), parsingLoadable2.type, j, j2, parsingLoadable.bytesLoaded(), iOException2, z);
        return z ? Loader.DONT_RETRY_FATAL : Loader.RETRY;
    }

    /* Access modifiers changed, original: 0000 */
    public void onUtcTimestampLoadCompleted(ParsingLoadable<Long> parsingLoadable, long j, long j2) {
        ParsingLoadable<Long> parsingLoadable2 = parsingLoadable;
        this.manifestEventDispatcher.loadCompleted(parsingLoadable2.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), parsingLoadable2.type, j, j2, parsingLoadable.bytesLoaded());
        onUtcTimestampResolved(((Long) parsingLoadable.getResult()).longValue() - j);
    }

    /* Access modifiers changed, original: 0000 */
    public LoadErrorAction onUtcTimestampLoadError(ParsingLoadable<Long> parsingLoadable, long j, long j2, IOException iOException) {
        ParsingLoadable<Long> parsingLoadable2 = parsingLoadable;
        EventDispatcher eventDispatcher = this.manifestEventDispatcher;
        DataSpec dataSpec = parsingLoadable2.dataSpec;
        Uri uri = parsingLoadable.getUri();
        Map responseHeaders = parsingLoadable.getResponseHeaders();
        int i = parsingLoadable2.type;
        eventDispatcher.loadError(dataSpec, uri, responseHeaders, i, j, j2, parsingLoadable.bytesLoaded(), iOException, true);
        onUtcTimestampResolutionError(iOException);
        return Loader.DONT_RETRY;
    }

    /* Access modifiers changed, original: 0000 */
    public void onLoadCanceled(ParsingLoadable<?> parsingLoadable, long j, long j2) {
        ParsingLoadable<?> parsingLoadable2 = parsingLoadable;
        this.manifestEventDispatcher.loadCanceled(parsingLoadable2.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), parsingLoadable2.type, j, j2, parsingLoadable.bytesLoaded());
    }

    private void resolveUtcTimingElement(UtcTimingElement utcTimingElement) {
        String str = utcTimingElement.schemeIdUri;
        if (Util.areEqual(str, "urn:mpeg:dash:utc:direct:2014") || Util.areEqual(str, "urn:mpeg:dash:utc:direct:2012")) {
            resolveUtcTimingElementDirect(utcTimingElement);
        } else if (Util.areEqual(str, "urn:mpeg:dash:utc:http-iso:2014") || Util.areEqual(str, "urn:mpeg:dash:utc:http-iso:2012")) {
            resolveUtcTimingElementHttp(utcTimingElement, new Iso8601Parser());
        } else if (Util.areEqual(str, "urn:mpeg:dash:utc:http-xsdate:2014") || Util.areEqual(str, "urn:mpeg:dash:utc:http-xsdate:2012")) {
            resolveUtcTimingElementHttp(utcTimingElement, new XsDateTimeParser());
        } else {
            onUtcTimestampResolutionError(new IOException("Unsupported UTC timing scheme"));
        }
    }

    private void resolveUtcTimingElementDirect(UtcTimingElement utcTimingElement) {
        try {
            onUtcTimestampResolved(Util.parseXsDateTime(utcTimingElement.value) - this.manifestLoadEndTimestampMs);
        } catch (ParserException e) {
            onUtcTimestampResolutionError(e);
        }
    }

    private void resolveUtcTimingElementHttp(UtcTimingElement utcTimingElement, Parser<Long> parser) {
        startLoading(new ParsingLoadable(this.dataSource, Uri.parse(utcTimingElement.value), 5, (Parser) parser), new UtcTimestampCallback(), 1);
    }

    private void onUtcTimestampResolved(long j) {
        this.elapsedRealtimeOffsetMs = j;
        processManifest(true);
    }

    private void onUtcTimestampResolutionError(IOException iOException) {
        Log.m15e("DashMediaSource", "Failed to resolve UtcTiming element.", iOException);
        processManifest(true);
    }

    private void processManifest(boolean z) {
        int i;
        long j;
        Object obj;
        long j2;
        long j3;
        for (i = 0; i < this.periodsById.size(); i++) {
            int keyAt = this.periodsById.keyAt(i);
            if (keyAt >= this.firstPeriodId) {
                ((DashMediaPeriod) this.periodsById.valueAt(i)).updateManifest(this.manifest, keyAt - this.firstPeriodId);
            }
        }
        i = this.manifest.getPeriodCount() - 1;
        PeriodSeekInfo createPeriodSeekInfo = PeriodSeekInfo.createPeriodSeekInfo(this.manifest.getPeriod(0), this.manifest.getPeriodDurationUs(0));
        PeriodSeekInfo createPeriodSeekInfo2 = PeriodSeekInfo.createPeriodSeekInfo(this.manifest.getPeriod(i), this.manifest.getPeriodDurationUs(i));
        long j4 = createPeriodSeekInfo.availableStartTimeUs;
        long j5 = createPeriodSeekInfo2.availableEndTimeUs;
        if (!this.manifest.dynamic || createPeriodSeekInfo2.isIndexExplicit) {
            j = j4;
            obj = null;
        } else {
            j5 = Math.min((getNowUnixTimeUs() - C0131C.msToUs(this.manifest.availabilityStartTimeMs)) - C0131C.msToUs(this.manifest.getPeriod(i).startMs), j5);
            j2 = this.manifest.timeShiftBufferDepthMs;
            if (j2 != -9223372036854775807L) {
                j2 = j5 - C0131C.msToUs(j2);
                while (j2 < 0 && i > 0) {
                    i--;
                    j2 += this.manifest.getPeriodDurationUs(i);
                }
                if (i == 0) {
                    j2 = Math.max(j4, j2);
                } else {
                    j2 = this.manifest.getPeriodDurationUs(0);
                }
                j4 = j2;
            }
            j = j4;
            obj = 1;
        }
        long j6 = j5 - j;
        for (int i2 = 0; i2 < this.manifest.getPeriodCount() - 1; i2++) {
            j6 += this.manifest.getPeriodDurationUs(i2);
        }
        DashManifest dashManifest = this.manifest;
        if (dashManifest.dynamic) {
            j2 = this.livePresentationDelayMs;
            if (!this.livePresentationDelayOverridesManifest) {
                j4 = dashManifest.suggestedPresentationDelayMs;
                if (j4 != -9223372036854775807L) {
                    j2 = j4;
                }
            }
            long msToUs = j6 - C0131C.msToUs(j2);
            if (msToUs < 5000000) {
                msToUs = Math.min(5000000, j6 / 2);
            }
            j3 = msToUs;
        } else {
            j3 = 0;
        }
        dashManifest = this.manifest;
        long usToMs = (dashManifest.availabilityStartTimeMs + dashManifest.getPeriod(0).startMs) + C0131C.usToMs(j);
        dashManifest = this.manifest;
        refreshSourceInfo(new DashTimeline(dashManifest.availabilityStartTimeMs, usToMs, this.firstPeriodId, j, j6, j3, dashManifest, this.tag), this.manifest);
        if (!this.sideloadedManifest) {
            this.handler.removeCallbacks(this.simulateManifestRefreshRunnable);
            if (obj != null) {
                this.handler.postDelayed(this.simulateManifestRefreshRunnable, 5000);
            }
            if (this.manifestLoadPending) {
                startLoadingManifest();
            } else if (z) {
                DashManifest dashManifest2 = this.manifest;
                if (dashManifest2.dynamic) {
                    long j7 = dashManifest2.minUpdatePeriodMs;
                    if (j7 != -9223372036854775807L) {
                        if (j7 == 0) {
                            j7 = 5000;
                        }
                        scheduleManifestRefresh(Math.max(0, (this.manifestLoadStartTimestampMs + j7) - SystemClock.elapsedRealtime()));
                    }
                }
            }
        }
    }

    private void scheduleManifestRefresh(long j) {
        this.handler.postDelayed(this.refreshManifestRunnable, j);
    }

    private void startLoadingManifest() {
        this.handler.removeCallbacks(this.refreshManifestRunnable);
        if (this.loader.isLoading()) {
            this.manifestLoadPending = true;
            return;
        }
        Uri uri;
        synchronized (this.manifestUriLock) {
            uri = this.manifestUri;
        }
        this.manifestLoadPending = false;
        startLoading(new ParsingLoadable(this.dataSource, uri, 4, this.manifestParser), this.manifestCallback, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(4));
    }

    private long getManifestLoadRetryDelayMillis() {
        return (long) Math.min((this.staleManifestReloadAttempt - 1) * 1000, 5000);
    }

    private <T> void startLoading(ParsingLoadable<T> parsingLoadable, Callback<ParsingLoadable<T>> callback, int i) {
        this.manifestEventDispatcher.loadStarted(parsingLoadable.dataSpec, parsingLoadable.type, this.loader.startLoading(parsingLoadable, callback, i));
    }

    private long getNowUnixTimeUs() {
        if (this.elapsedRealtimeOffsetMs != 0) {
            return C0131C.msToUs(SystemClock.elapsedRealtime() + this.elapsedRealtimeOffsetMs);
        }
        return C0131C.msToUs(System.currentTimeMillis());
    }
}
