package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekMap.SeekPoints;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.SampleQueue.UpstreamFormatChangedListener;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.Loader.Callback;
import com.google.android.exoplayer2.upstream.Loader.LoadErrorAction;
import com.google.android.exoplayer2.upstream.Loader.Loadable;
import com.google.android.exoplayer2.upstream.Loader.ReleaseCallback;
import com.google.android.exoplayer2.upstream.StatsDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ConditionVariable;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.Arrays;

final class ExtractorMediaPeriod implements MediaPeriod, ExtractorOutput, Callback<ExtractingLoadable>, ReleaseCallback, UpstreamFormatChangedListener {
    private static final Format ICY_FORMAT = Format.createSampleFormat("icy", MimeTypes.APPLICATION_ICY, TimestampAdjuster.DO_NOT_OFFSET);
    private final Allocator allocator;
    private MediaPeriod.Callback callback;
    private final long continueLoadingCheckIntervalBytes;
    private final String customCacheKey;
    private final DataSource dataSource;
    private int dataType;
    private long durationUs;
    private int enabledTrackCount;
    private final EventDispatcher eventDispatcher;
    private int extractedSamplesCountAtStartOfLoad;
    private final ExtractorHolder extractorHolder;
    private final Handler handler;
    private boolean haveAudioVideoTracks;
    private IcyHeaders icyHeaders;
    private long lastSeekPositionUs;
    private long length;
    private final Listener listener;
    private final ConditionVariable loadCondition;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final Loader loader = new Loader("Loader:ExtractorMediaPeriod");
    private boolean loadingFinished;
    private final Runnable maybeFinishPrepareRunnable;
    private boolean notifiedReadingStarted;
    private boolean notifyDiscontinuity;
    private final Runnable onContinueLoadingRequestedRunnable;
    private boolean pendingDeferredRetry;
    private long pendingResetPositionUs;
    private boolean prepared;
    private PreparedState preparedState;
    private boolean released;
    private TrackId[] sampleQueueTrackIds;
    private SampleQueue[] sampleQueues;
    private boolean sampleQueuesBuilt;
    private SeekMap seekMap;
    private boolean seenFirstTrackSelection;
    private final Uri uri;

    private static final class ExtractorHolder {
        private Extractor extractor;
        private final Extractor[] extractors;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:22:0x0050 in {2, 10, 13, 14, 15, 19, 21} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        public com.google.android.exoplayer2.extractor.Extractor selectExtractor(com.google.android.exoplayer2.extractor.ExtractorInput r6, com.google.android.exoplayer2.extractor.ExtractorOutput r7, android.net.Uri r8) throws java.io.IOException, java.lang.InterruptedException {
            /*
            r5 = this;
            r0 = r5.extractor;
            if (r0 == 0) goto L_0x0005;
            return r0;
            r0 = r5.extractors;
            r1 = r0.length;
            r2 = 0;
            if (r2 >= r1) goto L_0x0024;
            r3 = r0[r2];
            r4 = r3.sniff(r6);	 Catch:{ EOFException -> 0x001e, all -> 0x0019 }
            if (r4 == 0) goto L_0x001e;	 Catch:{ EOFException -> 0x001e, all -> 0x0019 }
            r5.extractor = r3;	 Catch:{ EOFException -> 0x001e, all -> 0x0019 }
            r6.resetPeekPosition();
            goto L_0x0024;
            r7 = move-exception;
            r6.resetPeekPosition();
            throw r7;
            r6.resetPeekPosition();
            r2 = r2 + 1;
            goto L_0x0009;
            r6 = r5.extractor;
            if (r6 == 0) goto L_0x002e;
            r6.init(r7);
            r6 = r5.extractor;
            return r6;
            r6 = new com.google.android.exoplayer2.source.UnrecognizedInputFormatException;
            r7 = new java.lang.StringBuilder;
            r7.<init>();
            r0 = "None of the available extractors (";
            r7.append(r0);
            r0 = r5.extractors;
            r0 = com.google.android.exoplayer2.util.Util.getCommaDelimitedSimpleClassNames(r0);
            r7.append(r0);
            r0 = ") could read the stream.";
            r7.append(r0);
            r7 = r7.toString();
            r6.<init>(r7, r8);
            throw r6;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.ExtractorMediaPeriod$ExtractorHolder.selectExtractor(com.google.android.exoplayer2.extractor.ExtractorInput, com.google.android.exoplayer2.extractor.ExtractorOutput, android.net.Uri):com.google.android.exoplayer2.extractor.Extractor");
        }

        public ExtractorHolder(Extractor[] extractorArr) {
            this.extractors = extractorArr;
        }

        public void release() {
            Extractor extractor = this.extractor;
            if (extractor != null) {
                extractor.release();
                this.extractor = null;
            }
        }
    }

    interface Listener {
        void onSourceInfoRefreshed(long j, boolean z);
    }

    private static final class PreparedState {
        public final SeekMap seekMap;
        public final boolean[] trackEnabledStates;
        public final boolean[] trackIsAudioVideoFlags;
        public final boolean[] trackNotifiedDownstreamFormats;
        public final TrackGroupArray tracks;

        public PreparedState(SeekMap seekMap, TrackGroupArray trackGroupArray, boolean[] zArr) {
            this.seekMap = seekMap;
            this.tracks = trackGroupArray;
            this.trackIsAudioVideoFlags = zArr;
            int i = trackGroupArray.length;
            this.trackEnabledStates = new boolean[i];
            this.trackNotifiedDownstreamFormats = new boolean[i];
        }
    }

    private static final class TrackId {
        /* renamed from: id */
        public final int f21id;
        public final boolean isIcyTrack;

        public TrackId(int i, boolean z) {
            this.f21id = i;
            this.isIcyTrack = z;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || TrackId.class != obj.getClass()) {
                return false;
            }
            TrackId trackId = (TrackId) obj;
            if (!(this.f21id == trackId.f21id && this.isIcyTrack == trackId.isIcyTrack)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (this.f21id * 31) + this.isIcyTrack;
        }
    }

    final class ExtractingLoadable implements Loadable, com.google.android.exoplayer2.source.IcyDataSource.Listener {
        private final StatsDataSource dataSource;
        private DataSpec dataSpec = buildDataSpec(0);
        private final ExtractorHolder extractorHolder;
        private final ExtractorOutput extractorOutput;
        private TrackOutput icyTrackOutput;
        private long length = -1;
        private volatile boolean loadCanceled;
        private final ConditionVariable loadCondition;
        private boolean pendingExtractorSeek = true;
        private final PositionHolder positionHolder = new PositionHolder();
        private long seekTimeUs;
        private boolean seenIcyMetadata;
        private final Uri uri;

        public ExtractingLoadable(Uri uri, DataSource dataSource, ExtractorHolder extractorHolder, ExtractorOutput extractorOutput, ConditionVariable conditionVariable) {
            this.uri = uri;
            this.dataSource = new StatsDataSource(dataSource);
            this.extractorHolder = extractorHolder;
            this.extractorOutput = extractorOutput;
            this.loadCondition = conditionVariable;
        }

        public void cancelLoad() {
            this.loadCanceled = true;
        }

        public void load() throws IOException, InterruptedException {
            Throwable th;
            int i = 0;
            while (i == 0 && !this.loadCanceled) {
                ExtractorInput extractorInput = null;
                try {
                    long j = this.positionHolder.position;
                    this.dataSpec = buildDataSpec(j);
                    this.length = this.dataSource.open(this.dataSpec);
                    if (this.length != -1) {
                        this.length += j;
                    }
                    Uri uri = this.dataSource.getUri();
                    Assertions.checkNotNull(uri);
                    uri = uri;
                    ExtractorMediaPeriod.this.icyHeaders = IcyHeaders.parse(this.dataSource.getResponseHeaders());
                    StatsDataSource statsDataSource = this.dataSource;
                    if (!(ExtractorMediaPeriod.this.icyHeaders == null || ExtractorMediaPeriod.this.icyHeaders.metadataInterval == -1)) {
                        statsDataSource = new IcyDataSource(this.dataSource, ExtractorMediaPeriod.this.icyHeaders.metadataInterval, this);
                        this.icyTrackOutput = ExtractorMediaPeriod.this.icyTrack();
                        this.icyTrackOutput.format(ExtractorMediaPeriod.ICY_FORMAT);
                    }
                    DefaultExtractorInput defaultExtractorInput = new DefaultExtractorInput(statsDataSource, j, this.length);
                    try {
                        Extractor selectExtractor = this.extractorHolder.selectExtractor(defaultExtractorInput, this.extractorOutput, uri);
                        if (this.pendingExtractorSeek) {
                            selectExtractor.seek(j, this.seekTimeUs);
                            this.pendingExtractorSeek = false;
                        }
                        while (i == 0 && !this.loadCanceled) {
                            this.loadCondition.block();
                            i = selectExtractor.read(defaultExtractorInput, this.positionHolder);
                            if (defaultExtractorInput.getPosition() > ExtractorMediaPeriod.this.continueLoadingCheckIntervalBytes + j) {
                                j = defaultExtractorInput.getPosition();
                                this.loadCondition.close();
                                ExtractorMediaPeriod.this.handler.post(ExtractorMediaPeriod.this.onContinueLoadingRequestedRunnable);
                            }
                        }
                        if (i == 1) {
                            i = 0;
                        } else {
                            this.positionHolder.position = defaultExtractorInput.getPosition();
                        }
                        Util.closeQuietly(this.dataSource);
                    } catch (Throwable th2) {
                        th = th2;
                        extractorInput = defaultExtractorInput;
                        if (!(i == 1 || extractorInput == null)) {
                            this.positionHolder.position = extractorInput.getPosition();
                        }
                        Util.closeQuietly(this.dataSource);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    this.positionHolder.position = extractorInput.getPosition();
                    Util.closeQuietly(this.dataSource);
                    throw th;
                }
            }
        }

        public void onIcyMetadata(ParsableByteArray parsableByteArray) {
            long max;
            if (this.seenIcyMetadata) {
                max = Math.max(ExtractorMediaPeriod.this.getLargestQueuedTimestampUs(), this.seekTimeUs);
            } else {
                max = this.seekTimeUs;
            }
            long j = max;
            int bytesLeft = parsableByteArray.bytesLeft();
            TrackOutput trackOutput = this.icyTrackOutput;
            Assertions.checkNotNull(trackOutput);
            TrackOutput trackOutput2 = trackOutput;
            trackOutput2.sampleData(parsableByteArray, bytesLeft);
            trackOutput2.sampleMetadata(j, 1, bytesLeft, 0, null);
            this.seenIcyMetadata = true;
        }

        private DataSpec buildDataSpec(long j) {
            return new DataSpec(this.uri, j, -1, ExtractorMediaPeriod.this.customCacheKey, 22);
        }

        private void setLoadPosition(long j, long j2) {
            this.positionHolder.position = j;
            this.seekTimeUs = j2;
            this.pendingExtractorSeek = true;
            this.seenIcyMetadata = false;
        }
    }

    private final class SampleStreamImpl implements SampleStream {
        private final int track;

        public SampleStreamImpl(int i) {
            this.track = i;
        }

        public boolean isReady() {
            return ExtractorMediaPeriod.this.isReady(this.track);
        }

        public void maybeThrowError() throws IOException {
            ExtractorMediaPeriod.this.maybeThrowError();
        }

        public int readData(FormatHolder formatHolder, DecoderInputBuffer decoderInputBuffer, boolean z) {
            return ExtractorMediaPeriod.this.readData(this.track, formatHolder, decoderInputBuffer, z);
        }

        public int skipData(long j) {
            return ExtractorMediaPeriod.this.skipData(this.track, j);
        }
    }

    public void reevaluateBuffer(long j) {
    }

    public ExtractorMediaPeriod(Uri uri, DataSource dataSource, Extractor[] extractorArr, LoadErrorHandlingPolicy loadErrorHandlingPolicy, EventDispatcher eventDispatcher, Listener listener, Allocator allocator, String str, int i) {
        this.uri = uri;
        this.dataSource = dataSource;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.eventDispatcher = eventDispatcher;
        this.listener = listener;
        this.allocator = allocator;
        this.customCacheKey = str;
        this.continueLoadingCheckIntervalBytes = (long) i;
        this.extractorHolder = new ExtractorHolder(extractorArr);
        this.loadCondition = new ConditionVariable();
        this.maybeFinishPrepareRunnable = new C0193-$$Lambda$ExtractorMediaPeriod$Ll7lI30pD07GZk92Lo8XgkQMAAY(this);
        this.onContinueLoadingRequestedRunnable = new C0192-$$Lambda$ExtractorMediaPeriod$Hd-sBytb6cpkhM49l8dYCND3wmk(this);
        this.handler = new Handler();
        this.sampleQueueTrackIds = new TrackId[0];
        this.sampleQueues = new SampleQueue[0];
        this.pendingResetPositionUs = -9223372036854775807L;
        this.length = -1;
        this.durationUs = -9223372036854775807L;
        this.dataType = 1;
        eventDispatcher.mediaPeriodCreated();
    }

    public /* synthetic */ void lambda$new$0$ExtractorMediaPeriod() {
        if (!this.released) {
            MediaPeriod.Callback callback = this.callback;
            Assertions.checkNotNull(callback);
            callback.onContinueLoadingRequested(this);
        }
    }

    public void release() {
        if (this.prepared) {
            for (SampleQueue discardToEnd : this.sampleQueues) {
                discardToEnd.discardToEnd();
            }
        }
        this.loader.release(this);
        this.handler.removeCallbacksAndMessages(null);
        this.callback = null;
        this.released = true;
        this.eventDispatcher.mediaPeriodReleased();
    }

    public void onLoaderReleased() {
        for (SampleQueue reset : this.sampleQueues) {
            reset.reset();
        }
        this.extractorHolder.release();
    }

    public void prepare(MediaPeriod.Callback callback, long j) {
        this.callback = callback;
        this.loadCondition.open();
        startLoading();
    }

    public void maybeThrowPrepareError() throws IOException {
        maybeThrowError();
    }

    public TrackGroupArray getTrackGroups() {
        return getPreparedState().tracks;
    }

    public long selectTracks(TrackSelection[] trackSelectionArr, boolean[] zArr, SampleStream[] sampleStreamArr, boolean[] zArr2, long j) {
        PreparedState preparedState = getPreparedState();
        TrackGroupArray trackGroupArray = preparedState.tracks;
        boolean[] zArr3 = preparedState.trackEnabledStates;
        int i = this.enabledTrackCount;
        int i2 = 0;
        int i3 = 0;
        while (i3 < trackSelectionArr.length) {
            if (sampleStreamArr[i3] != null && (trackSelectionArr[i3] == null || !zArr[i3])) {
                int access$000 = ((SampleStreamImpl) sampleStreamArr[i3]).track;
                Assertions.checkState(zArr3[access$000]);
                this.enabledTrackCount--;
                zArr3[access$000] = false;
                sampleStreamArr[i3] = null;
            }
            i3++;
        }
        Object obj = (this.seenFirstTrackSelection ? i != 0 : j == 0) ? null : 1;
        Object obj2 = obj;
        int i4 = 0;
        while (i4 < trackSelectionArr.length) {
            if (sampleStreamArr[i4] == null && trackSelectionArr[i4] != null) {
                TrackSelection trackSelection = trackSelectionArr[i4];
                Assertions.checkState(trackSelection.length() == 1);
                Assertions.checkState(trackSelection.getIndexInTrackGroup(0) == 0);
                i3 = trackGroupArray.indexOf(trackSelection.getTrackGroup());
                Assertions.checkState(zArr3[i3] ^ 1);
                this.enabledTrackCount++;
                zArr3[i3] = true;
                sampleStreamArr[i4] = new SampleStreamImpl(i3);
                zArr2[i4] = true;
                if (obj2 == null) {
                    SampleQueue sampleQueue = this.sampleQueues[i3];
                    sampleQueue.rewind();
                    obj2 = (sampleQueue.advanceTo(j, true, true) != -1 || sampleQueue.getReadIndex() == 0) ? null : 1;
                }
            }
            i4++;
        }
        if (this.enabledTrackCount == 0) {
            this.pendingDeferredRetry = false;
            this.notifyDiscontinuity = false;
            SampleQueue[] sampleQueueArr;
            if (this.loader.isLoading()) {
                sampleQueueArr = this.sampleQueues;
                i4 = sampleQueueArr.length;
                while (i2 < i4) {
                    sampleQueueArr[i2].discardToEnd();
                    i2++;
                }
                this.loader.cancelLoading();
            } else {
                sampleQueueArr = this.sampleQueues;
                i4 = sampleQueueArr.length;
                while (i2 < i4) {
                    sampleQueueArr[i2].reset();
                    i2++;
                }
            }
        } else if (obj2 != null) {
            j = seekToUs(j);
            while (i2 < sampleStreamArr.length) {
                if (sampleStreamArr[i2] != null) {
                    zArr2[i2] = true;
                }
                i2++;
            }
        }
        this.seenFirstTrackSelection = true;
        return j;
    }

    public void discardBuffer(long j, boolean z) {
        if (!isPendingReset()) {
            boolean[] zArr = getPreparedState().trackEnabledStates;
            int length = this.sampleQueues.length;
            for (int i = 0; i < length; i++) {
                this.sampleQueues[i].discardTo(j, z, zArr[i]);
            }
        }
    }

    public boolean continueLoading(long j) {
        if (this.loadingFinished || this.pendingDeferredRetry || (this.prepared && this.enabledTrackCount == 0)) {
            return false;
        }
        boolean open = this.loadCondition.open();
        if (!this.loader.isLoading()) {
            startLoading();
            open = true;
        }
        return open;
    }

    public long getNextLoadPositionUs() {
        return this.enabledTrackCount == 0 ? Long.MIN_VALUE : getBufferedPositionUs();
    }

    public long readDiscontinuity() {
        if (!this.notifiedReadingStarted) {
            this.eventDispatcher.readingStarted();
            this.notifiedReadingStarted = true;
        }
        if (!this.notifyDiscontinuity || (!this.loadingFinished && getExtractedSamplesCount() <= this.extractedSamplesCountAtStartOfLoad)) {
            return -9223372036854775807L;
        }
        this.notifyDiscontinuity = false;
        return this.lastSeekPositionUs;
    }

    public long getBufferedPositionUs() {
        boolean[] zArr = getPreparedState().trackIsAudioVideoFlags;
        if (this.loadingFinished) {
            return Long.MIN_VALUE;
        }
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long j;
        if (this.haveAudioVideoTracks) {
            int length = this.sampleQueues.length;
            int i = 0;
            j = TimestampAdjuster.DO_NOT_OFFSET;
            while (i < length) {
                if (zArr[i] && !this.sampleQueues[i].isLastSampleQueued()) {
                    j = Math.min(j, this.sampleQueues[i].getLargestQueuedTimestampUs());
                }
                i++;
            }
        } else {
            j = TimestampAdjuster.DO_NOT_OFFSET;
        }
        if (j == TimestampAdjuster.DO_NOT_OFFSET) {
            j = getLargestQueuedTimestampUs();
        }
        if (j == Long.MIN_VALUE) {
            j = this.lastSeekPositionUs;
        }
        return j;
    }

    public long seekToUs(long j) {
        PreparedState preparedState = getPreparedState();
        SeekMap seekMap = preparedState.seekMap;
        boolean[] zArr = preparedState.trackIsAudioVideoFlags;
        if (!seekMap.isSeekable()) {
            j = 0;
        }
        int i = 0;
        this.notifyDiscontinuity = false;
        this.lastSeekPositionUs = j;
        if (isPendingReset()) {
            this.pendingResetPositionUs = j;
            return j;
        } else if (this.dataType != 7 && seekInsideBufferUs(zArr, j)) {
            return j;
        } else {
            this.pendingDeferredRetry = false;
            this.pendingResetPositionUs = j;
            this.loadingFinished = false;
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
            } else {
                SampleQueue[] sampleQueueArr = this.sampleQueues;
                int length = sampleQueueArr.length;
                while (i < length) {
                    sampleQueueArr[i].reset();
                    i++;
                }
            }
            return j;
        }
    }

    public long getAdjustedSeekPositionUs(long j, SeekParameters seekParameters) {
        SeekMap seekMap = getPreparedState().seekMap;
        if (!seekMap.isSeekable()) {
            return 0;
        }
        SeekPoints seekPoints = seekMap.getSeekPoints(j);
        return Util.resolveSeekPositionUs(j, seekParameters, seekPoints.first.timeUs, seekPoints.second.timeUs);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isReady(int i) {
        return !suppressRead() && (this.loadingFinished || this.sampleQueues[i].hasNextSample());
    }

    /* Access modifiers changed, original: 0000 */
    public void maybeThrowError() throws IOException {
        this.loader.maybeThrowError(this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(this.dataType));
    }

    /* Access modifiers changed, original: 0000 */
    public int readData(int i, FormatHolder formatHolder, DecoderInputBuffer decoderInputBuffer, boolean z) {
        if (suppressRead()) {
            return -3;
        }
        maybeNotifyDownstreamFormat(i);
        int read = this.sampleQueues[i].read(formatHolder, decoderInputBuffer, z, this.loadingFinished, this.lastSeekPositionUs);
        if (read == -3) {
            maybeStartDeferredRetry(i);
        }
        return read;
    }

    /* Access modifiers changed, original: 0000 */
    public int skipData(int i, long j) {
        int i2 = 0;
        if (suppressRead()) {
            return 0;
        }
        maybeNotifyDownstreamFormat(i);
        SampleQueue sampleQueue = this.sampleQueues[i];
        if (!this.loadingFinished || j <= sampleQueue.getLargestQueuedTimestampUs()) {
            int advanceTo = sampleQueue.advanceTo(j, true, true);
            if (advanceTo != -1) {
                i2 = advanceTo;
            }
        } else {
            i2 = sampleQueue.advanceToEnd();
        }
        if (i2 == 0) {
            maybeStartDeferredRetry(i);
        }
        return i2;
    }

    private void maybeNotifyDownstreamFormat(int i) {
        PreparedState preparedState = getPreparedState();
        boolean[] zArr = preparedState.trackNotifiedDownstreamFormats;
        if (!zArr[i]) {
            Format format = preparedState.tracks.get(i).getFormat(0);
            this.eventDispatcher.downstreamFormatChanged(MimeTypes.getTrackType(format.sampleMimeType), format, 0, null, this.lastSeekPositionUs);
            zArr[i] = true;
        }
    }

    private void maybeStartDeferredRetry(int i) {
        boolean[] zArr = getPreparedState().trackIsAudioVideoFlags;
        if (this.pendingDeferredRetry && zArr[i] && !this.sampleQueues[i].hasNextSample()) {
            this.pendingResetPositionUs = 0;
            i = 0;
            this.pendingDeferredRetry = false;
            this.notifyDiscontinuity = true;
            this.lastSeekPositionUs = 0;
            this.extractedSamplesCountAtStartOfLoad = 0;
            SampleQueue[] sampleQueueArr = this.sampleQueues;
            int length = sampleQueueArr.length;
            while (i < length) {
                sampleQueueArr[i].reset();
                i++;
            }
            MediaPeriod.Callback callback = this.callback;
            Assertions.checkNotNull(callback);
            callback.onContinueLoadingRequested(this);
        }
    }

    private boolean suppressRead() {
        return this.notifyDiscontinuity || isPendingReset();
    }

    public void onLoadCompleted(ExtractingLoadable extractingLoadable, long j, long j2) {
        if (this.durationUs == -9223372036854775807L) {
            SeekMap seekMap = this.seekMap;
            Assertions.checkNotNull(seekMap);
            seekMap = seekMap;
            long largestQueuedTimestampUs = getLargestQueuedTimestampUs();
            this.durationUs = largestQueuedTimestampUs == Long.MIN_VALUE ? 0 : largestQueuedTimestampUs + 10000;
            this.listener.onSourceInfoRefreshed(this.durationUs, seekMap.isSeekable());
        }
        this.eventDispatcher.loadCompleted(extractingLoadable.dataSpec, extractingLoadable.dataSource.getLastOpenedUri(), extractingLoadable.dataSource.getLastResponseHeaders(), 1, -1, null, 0, null, extractingLoadable.seekTimeUs, this.durationUs, j, j2, extractingLoadable.dataSource.getBytesRead());
        copyLengthFromLoader(extractingLoadable);
        this.loadingFinished = true;
        MediaPeriod.Callback callback = this.callback;
        Assertions.checkNotNull(callback);
        callback.onContinueLoadingRequested(this);
    }

    public void onLoadCanceled(ExtractingLoadable extractingLoadable, long j, long j2, boolean z) {
        this.eventDispatcher.loadCanceled(extractingLoadable.dataSpec, extractingLoadable.dataSource.getLastOpenedUri(), extractingLoadable.dataSource.getLastResponseHeaders(), 1, -1, null, 0, null, extractingLoadable.seekTimeUs, this.durationUs, j, j2, extractingLoadable.dataSource.getBytesRead());
        if (!z) {
            copyLengthFromLoader(extractingLoadable);
            for (SampleQueue reset : this.sampleQueues) {
                reset.reset();
            }
            if (this.enabledTrackCount > 0) {
                MediaPeriod.Callback callback = this.callback;
                Assertions.checkNotNull(callback);
                callback.onContinueLoadingRequested(this);
            }
        }
    }

    public LoadErrorAction onLoadError(ExtractingLoadable extractingLoadable, long j, long j2, IOException iOException, int i) {
        LoadErrorAction loadErrorAction;
        copyLengthFromLoader(extractingLoadable);
        long retryDelayMsFor = this.loadErrorHandlingPolicy.getRetryDelayMsFor(this.dataType, this.durationUs, iOException, i);
        ExtractingLoadable extractingLoadable2;
        if (retryDelayMsFor == -9223372036854775807L) {
            loadErrorAction = Loader.DONT_RETRY_FATAL;
            extractingLoadable2 = extractingLoadable;
        } else {
            boolean z;
            int extractedSamplesCount = getExtractedSamplesCount();
            if (extractedSamplesCount > this.extractedSamplesCountAtStartOfLoad) {
                extractingLoadable2 = extractingLoadable;
                z = true;
            } else {
                extractingLoadable2 = extractingLoadable;
                z = false;
            }
            loadErrorAction = configureRetry(extractingLoadable2, extractedSamplesCount) ? Loader.createRetryAction(z, retryDelayMsFor) : Loader.DONT_RETRY;
        }
        this.eventDispatcher.loadError(extractingLoadable.dataSpec, extractingLoadable.dataSource.getLastOpenedUri(), extractingLoadable.dataSource.getLastResponseHeaders(), 1, -1, null, 0, null, extractingLoadable.seekTimeUs, this.durationUs, j, j2, extractingLoadable.dataSource.getBytesRead(), iOException, loadErrorAction.isRetry() ^ 1);
        return loadErrorAction;
    }

    public TrackOutput track(int i, int i2) {
        return prepareTrackOutput(new TrackId(i, false));
    }

    public void endTracks() {
        this.sampleQueuesBuilt = true;
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    public void seekMap(SeekMap seekMap) {
        this.seekMap = seekMap;
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    /* Access modifiers changed, original: 0000 */
    public TrackOutput icyTrack() {
        return prepareTrackOutput(new TrackId(0, true));
    }

    public void onUpstreamFormatChanged(Format format) {
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    private TrackOutput prepareTrackOutput(TrackId trackId) {
        int length = this.sampleQueues.length;
        for (int i = 0; i < length; i++) {
            if (trackId.equals(this.sampleQueueTrackIds[i])) {
                return this.sampleQueues[i];
            }
        }
        SampleQueue sampleQueue = new SampleQueue(this.allocator);
        sampleQueue.setUpstreamFormatChangeListener(this);
        int i2 = length + 1;
        TrackId[] trackIdArr = (TrackId[]) Arrays.copyOf(this.sampleQueueTrackIds, i2);
        trackIdArr[length] = trackId;
        Util.castNonNullTypeArray(trackIdArr);
        this.sampleQueueTrackIds = trackIdArr;
        SampleQueue[] sampleQueueArr = (SampleQueue[]) Arrays.copyOf(this.sampleQueues, i2);
        sampleQueueArr[length] = sampleQueue;
        Util.castNonNullTypeArray(sampleQueueArr);
        this.sampleQueues = sampleQueueArr;
        return sampleQueue;
    }

    private void maybeFinishPrepare() {
        SeekMap seekMap = this.seekMap;
        if (!(this.released || this.prepared || !this.sampleQueuesBuilt || seekMap == null)) {
            SampleQueue[] sampleQueueArr = this.sampleQueues;
            int length = sampleQueueArr.length;
            int i = 0;
            while (i < length) {
                if (sampleQueueArr[i].getUpstreamFormat() != null) {
                    i++;
                } else {
                    return;
                }
            }
            this.loadCondition.close();
            int length2 = this.sampleQueues.length;
            TrackGroup[] trackGroupArr = new TrackGroup[length2];
            boolean[] zArr = new boolean[length2];
            this.durationUs = seekMap.getDurationUs();
            int i2 = 0;
            while (i2 < length2) {
                Format upstreamFormat = this.sampleQueues[i2].getUpstreamFormat();
                String str = upstreamFormat.sampleMimeType;
                boolean isAudio = MimeTypes.isAudio(str);
                boolean z = isAudio || MimeTypes.isVideo(str);
                zArr[i2] = z;
                this.haveAudioVideoTracks = z | this.haveAudioVideoTracks;
                IcyHeaders icyHeaders = this.icyHeaders;
                if (icyHeaders != null) {
                    if (isAudio || this.sampleQueueTrackIds[i2].isIcyTrack) {
                        Metadata metadata = upstreamFormat.metadata;
                        if (metadata == null) {
                            metadata = new Metadata(icyHeaders);
                        } else {
                            metadata = metadata.copyWithAppendedEntries(icyHeaders);
                        }
                        upstreamFormat = upstreamFormat.copyWithMetadata(metadata);
                    }
                    if (isAudio && upstreamFormat.bitrate == -1) {
                        int i3 = icyHeaders.bitrate;
                        if (i3 != -1) {
                            upstreamFormat = upstreamFormat.copyWithBitrate(i3);
                        }
                    }
                }
                trackGroupArr[i2] = new TrackGroup(upstreamFormat);
                i2++;
            }
            length2 = (this.length == -1 && seekMap.getDurationUs() == -9223372036854775807L) ? 7 : 1;
            this.dataType = length2;
            this.preparedState = new PreparedState(seekMap, new TrackGroupArray(trackGroupArr), zArr);
            this.prepared = true;
            this.listener.onSourceInfoRefreshed(this.durationUs, seekMap.isSeekable());
            MediaPeriod.Callback callback = this.callback;
            Assertions.checkNotNull(callback);
            callback.onPrepared(this);
        }
    }

    private PreparedState getPreparedState() {
        PreparedState preparedState = this.preparedState;
        Assertions.checkNotNull(preparedState);
        return preparedState;
    }

    private void copyLengthFromLoader(ExtractingLoadable extractingLoadable) {
        if (this.length == -1) {
            this.length = extractingLoadable.length;
        }
    }

    private void startLoading() {
        ExtractingLoadable extractingLoadable = new ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this, this.loadCondition);
        if (this.prepared) {
            SeekMap seekMap = getPreparedState().seekMap;
            Assertions.checkState(isPendingReset());
            long j = this.durationUs;
            if (j == -9223372036854775807L || this.pendingResetPositionUs < j) {
                extractingLoadable.setLoadPosition(seekMap.getSeekPoints(this.pendingResetPositionUs).first.position, this.pendingResetPositionUs);
                this.pendingResetPositionUs = -9223372036854775807L;
            } else {
                this.loadingFinished = true;
                this.pendingResetPositionUs = -9223372036854775807L;
                return;
            }
        }
        this.extractedSamplesCountAtStartOfLoad = getExtractedSamplesCount();
        this.eventDispatcher.loadStarted(extractingLoadable.dataSpec, 1, -1, null, 0, null, extractingLoadable.seekTimeUs, this.durationUs, this.loader.startLoading(extractingLoadable, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(this.dataType)));
    }

    private boolean configureRetry(ExtractingLoadable extractingLoadable, int i) {
        if (this.length == -1) {
            SeekMap seekMap = this.seekMap;
            if (seekMap == null || seekMap.getDurationUs() == -9223372036854775807L) {
                int i2 = 0;
                if (!this.prepared || suppressRead()) {
                    this.notifyDiscontinuity = this.prepared;
                    this.lastSeekPositionUs = 0;
                    this.extractedSamplesCountAtStartOfLoad = 0;
                    SampleQueue[] sampleQueueArr = this.sampleQueues;
                    int length = sampleQueueArr.length;
                    while (i2 < length) {
                        sampleQueueArr[i2].reset();
                        i2++;
                    }
                    extractingLoadable.setLoadPosition(0, 0);
                    return true;
                }
                this.pendingDeferredRetry = true;
                return false;
            }
        }
        this.extractedSamplesCountAtStartOfLoad = i;
        return true;
    }

    private boolean seekInsideBufferUs(boolean[] zArr, long j) {
        int length = this.sampleQueues.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= length) {
                return true;
            }
            SampleQueue sampleQueue = this.sampleQueues[i];
            sampleQueue.rewind();
            if (sampleQueue.advanceTo(j, true, false) == -1) {
                z = false;
            }
            if (z || (!zArr[i] && this.haveAudioVideoTracks)) {
                i++;
            }
        }
        return false;
    }

    private int getExtractedSamplesCount() {
        int i = 0;
        for (SampleQueue writeIndex : this.sampleQueues) {
            i += writeIndex.getWriteIndex();
        }
        return i;
    }

    private long getLargestQueuedTimestampUs() {
        long j = Long.MIN_VALUE;
        for (SampleQueue largestQueuedTimestampUs : this.sampleQueues) {
            j = Math.max(j, largestQueuedTimestampUs.getLargestQueuedTimestampUs());
        }
        return j;
    }

    private boolean isPendingReset() {
        return this.pendingResetPositionUs != -9223372036854775807L;
    }
}
