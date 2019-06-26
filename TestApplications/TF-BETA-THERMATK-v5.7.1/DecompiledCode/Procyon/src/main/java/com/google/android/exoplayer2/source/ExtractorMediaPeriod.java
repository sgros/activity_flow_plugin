// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import java.io.EOFException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.StatsDataSource;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.FormatHolder;
import java.io.IOException;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.Extractor;
import android.net.Uri;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.util.ConditionVariable;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import android.os.Handler;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.extractor.ExtractorOutput;

final class ExtractorMediaPeriod implements MediaPeriod, ExtractorOutput, Loader.Callback<ExtractingLoadable>, ReleaseCallback, UpstreamFormatChangedListener
{
    private static final Format ICY_FORMAT;
    private final Allocator allocator;
    private MediaPeriod.Callback callback;
    private final long continueLoadingCheckIntervalBytes;
    private final String customCacheKey;
    private final DataSource dataSource;
    private int dataType;
    private long durationUs;
    private int enabledTrackCount;
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
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
    private final Loader loader;
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
    
    static {
        ICY_FORMAT = Format.createSampleFormat("icy", "application/x-icy", Long.MAX_VALUE);
    }
    
    public ExtractorMediaPeriod(final Uri uri, final DataSource dataSource, final Extractor[] array, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final MediaSourceEventListener.EventDispatcher eventDispatcher, final Listener listener, final Allocator allocator, final String customCacheKey, final int n) {
        this.uri = uri;
        this.dataSource = dataSource;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.eventDispatcher = eventDispatcher;
        this.listener = listener;
        this.allocator = allocator;
        this.customCacheKey = customCacheKey;
        this.continueLoadingCheckIntervalBytes = n;
        this.loader = new Loader("Loader:ExtractorMediaPeriod");
        this.extractorHolder = new ExtractorHolder(array);
        this.loadCondition = new ConditionVariable();
        this.maybeFinishPrepareRunnable = new _$$Lambda$ExtractorMediaPeriod$Ll7lI30pD07GZk92Lo8XgkQMAAY(this);
        this.onContinueLoadingRequestedRunnable = new _$$Lambda$ExtractorMediaPeriod$Hd_sBytb6cpkhM49l8dYCND3wmk(this);
        this.handler = new Handler();
        this.sampleQueueTrackIds = new TrackId[0];
        this.sampleQueues = new SampleQueue[0];
        this.pendingResetPositionUs = -9223372036854775807L;
        this.length = -1L;
        this.durationUs = -9223372036854775807L;
        this.dataType = 1;
        eventDispatcher.mediaPeriodCreated();
    }
    
    private boolean configureRetry(final ExtractingLoadable extractingLoadable, int i) {
        if (this.length == -1L) {
            final SeekMap seekMap = this.seekMap;
            if (seekMap == null || seekMap.getDurationUs() == -9223372036854775807L) {
                final boolean prepared = this.prepared;
                i = 0;
                if (prepared && !this.suppressRead()) {
                    this.pendingDeferredRetry = true;
                    return false;
                }
                this.notifyDiscontinuity = this.prepared;
                this.lastSeekPositionUs = 0L;
                this.extractedSamplesCountAtStartOfLoad = 0;
                for (SampleQueue[] sampleQueues = this.sampleQueues; i < sampleQueues.length; ++i) {
                    sampleQueues[i].reset();
                }
                extractingLoadable.setLoadPosition(0L, 0L);
                return true;
            }
        }
        this.extractedSamplesCountAtStartOfLoad = i;
        return true;
    }
    
    private void copyLengthFromLoader(final ExtractingLoadable extractingLoadable) {
        if (this.length == -1L) {
            this.length = extractingLoadable.length;
        }
    }
    
    private int getExtractedSamplesCount() {
        final SampleQueue[] sampleQueues = this.sampleQueues;
        final int length = sampleQueues.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            n += sampleQueues[i].getWriteIndex();
            ++i;
        }
        return n;
    }
    
    private long getLargestQueuedTimestampUs() {
        final SampleQueue[] sampleQueues = this.sampleQueues;
        final int length = sampleQueues.length;
        long max = Long.MIN_VALUE;
        for (int i = 0; i < length; ++i) {
            max = Math.max(max, sampleQueues[i].getLargestQueuedTimestampUs());
        }
        return max;
    }
    
    private PreparedState getPreparedState() {
        final PreparedState preparedState = this.preparedState;
        Assertions.checkNotNull(preparedState);
        return preparedState;
    }
    
    private boolean isPendingReset() {
        return this.pendingResetPositionUs != -9223372036854775807L;
    }
    
    private void maybeFinishPrepare() {
        final SeekMap seekMap = this.seekMap;
        if (!this.released && !this.prepared && this.sampleQueuesBuilt) {
            if (seekMap != null) {
                final SampleQueue[] sampleQueues = this.sampleQueues;
                for (int length = sampleQueues.length, i = 0; i < length; ++i) {
                    if (sampleQueues[i].getUpstreamFormat() == null) {
                        return;
                    }
                }
                this.loadCondition.close();
                final int length2 = this.sampleQueues.length;
                final TrackGroup[] array = new TrackGroup[length2];
                final boolean[] array2 = new boolean[length2];
                this.durationUs = seekMap.getDurationUs();
                for (int j = 0; j < length2; ++j) {
                    final Format upstreamFormat = this.sampleQueues[j].getUpstreamFormat();
                    final String sampleMimeType = upstreamFormat.sampleMimeType;
                    final boolean audio = MimeTypes.isAudio(sampleMimeType);
                    final boolean b = audio || MimeTypes.isVideo(sampleMimeType);
                    array2[j] = b;
                    this.haveAudioVideoTracks |= b;
                    final IcyHeaders icyHeaders = this.icyHeaders;
                    Format copyWithBitrate = upstreamFormat;
                    if (icyHeaders != null) {
                        Format copyWithMetadata = null;
                        Label_0265: {
                            if (!audio) {
                                copyWithMetadata = upstreamFormat;
                                if (!this.sampleQueueTrackIds[j].isIcyTrack) {
                                    break Label_0265;
                                }
                            }
                            final Metadata metadata = upstreamFormat.metadata;
                            Metadata copyWithAppendedEntries;
                            if (metadata == null) {
                                copyWithAppendedEntries = new Metadata(new Metadata.Entry[] { icyHeaders });
                            }
                            else {
                                copyWithAppendedEntries = metadata.copyWithAppendedEntries(icyHeaders);
                            }
                            copyWithMetadata = upstreamFormat.copyWithMetadata(copyWithAppendedEntries);
                        }
                        copyWithBitrate = copyWithMetadata;
                        if (audio) {
                            copyWithBitrate = copyWithMetadata;
                            if (copyWithMetadata.bitrate == -1) {
                                final int bitrate = icyHeaders.bitrate;
                                copyWithBitrate = copyWithMetadata;
                                if (bitrate != -1) {
                                    copyWithBitrate = copyWithMetadata.copyWithBitrate(bitrate);
                                }
                            }
                        }
                    }
                    array[j] = new TrackGroup(new Format[] { copyWithBitrate });
                }
                int dataType;
                if (this.length == -1L && seekMap.getDurationUs() == -9223372036854775807L) {
                    dataType = 7;
                }
                else {
                    dataType = 1;
                }
                this.dataType = dataType;
                this.preparedState = new PreparedState(seekMap, new TrackGroupArray(array), array2);
                this.prepared = true;
                this.listener.onSourceInfoRefreshed(this.durationUs, seekMap.isSeekable());
                final MediaPeriod.Callback callback = this.callback;
                Assertions.checkNotNull(callback);
                callback.onPrepared(this);
            }
        }
    }
    
    private void maybeNotifyDownstreamFormat(final int n) {
        final PreparedState preparedState = this.getPreparedState();
        final boolean[] trackNotifiedDownstreamFormats = preparedState.trackNotifiedDownstreamFormats;
        if (!trackNotifiedDownstreamFormats[n]) {
            final Format format = preparedState.tracks.get(n).getFormat(0);
            this.eventDispatcher.downstreamFormatChanged(MimeTypes.getTrackType(format.sampleMimeType), format, 0, null, this.lastSeekPositionUs);
            trackNotifiedDownstreamFormats[n] = true;
        }
    }
    
    private void maybeStartDeferredRetry(int i) {
        final boolean[] trackIsAudioVideoFlags = this.getPreparedState().trackIsAudioVideoFlags;
        if (this.pendingDeferredRetry && trackIsAudioVideoFlags[i]) {
            if (!this.sampleQueues[i].hasNextSample()) {
                this.pendingResetPositionUs = 0L;
                i = 0;
                this.pendingDeferredRetry = false;
                this.notifyDiscontinuity = true;
                this.lastSeekPositionUs = 0L;
                this.extractedSamplesCountAtStartOfLoad = 0;
                for (SampleQueue[] sampleQueues = this.sampleQueues; i < sampleQueues.length; ++i) {
                    sampleQueues[i].reset();
                }
                final MediaPeriod.Callback callback = this.callback;
                Assertions.checkNotNull(callback);
                ((SequenceableLoader.Callback<ExtractorMediaPeriod>)callback).onContinueLoadingRequested(this);
            }
        }
    }
    
    private TrackOutput prepareTrackOutput(final TrackId trackId) {
        final int length = this.sampleQueues.length;
        for (int i = 0; i < length; ++i) {
            if (trackId.equals(this.sampleQueueTrackIds[i])) {
                return this.sampleQueues[i];
            }
        }
        final SampleQueue sampleQueue = new SampleQueue(this.allocator);
        sampleQueue.setUpstreamFormatChangeListener((SampleQueue.UpstreamFormatChangedListener)this);
        final TrackId[] sampleQueueTrackIds = this.sampleQueueTrackIds;
        final int n = length + 1;
        final TrackId[] array = Arrays.copyOf(sampleQueueTrackIds, n);
        array[length] = trackId;
        Util.castNonNullTypeArray(array);
        this.sampleQueueTrackIds = array;
        final SampleQueue[] array2 = Arrays.copyOf(this.sampleQueues, n);
        array2[length] = sampleQueue;
        Util.castNonNullTypeArray(array2);
        this.sampleQueues = array2;
        return sampleQueue;
    }
    
    private boolean seekInsideBufferUs(final boolean[] array, final long n) {
        final int length = this.sampleQueues.length;
        int n2 = 0;
        while (true) {
            boolean b = true;
            if (n2 >= length) {
                return true;
            }
            final SampleQueue sampleQueue = this.sampleQueues[n2];
            sampleQueue.rewind();
            if (sampleQueue.advanceTo(n, true, false) == -1) {
                b = false;
            }
            if (!b && (array[n2] || !this.haveAudioVideoTracks)) {
                return false;
            }
            ++n2;
        }
    }
    
    private void startLoading() {
        final ExtractingLoadable extractingLoadable = new ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this, this.loadCondition);
        if (this.prepared) {
            final SeekMap seekMap = this.getPreparedState().seekMap;
            Assertions.checkState(this.isPendingReset());
            final long durationUs = this.durationUs;
            if (durationUs != -9223372036854775807L && this.pendingResetPositionUs >= durationUs) {
                this.loadingFinished = true;
                this.pendingResetPositionUs = -9223372036854775807L;
                return;
            }
            extractingLoadable.setLoadPosition(seekMap.getSeekPoints(this.pendingResetPositionUs).first.position, this.pendingResetPositionUs);
            this.pendingResetPositionUs = -9223372036854775807L;
        }
        this.extractedSamplesCountAtStartOfLoad = this.getExtractedSamplesCount();
        this.eventDispatcher.loadStarted(extractingLoadable.dataSpec, 1, -1, null, 0, null, extractingLoadable.seekTimeUs, this.durationUs, this.loader.startLoading(extractingLoadable, (Loader.Callback<ExtractingLoadable>)this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(this.dataType)));
    }
    
    private boolean suppressRead() {
        return this.notifyDiscontinuity || this.isPendingReset();
    }
    
    @Override
    public boolean continueLoading(final long n) {
        if (!this.loadingFinished && !this.pendingDeferredRetry && (!this.prepared || this.enabledTrackCount != 0)) {
            boolean open = this.loadCondition.open();
            if (!this.loader.isLoading()) {
                this.startLoading();
                open = true;
            }
            return open;
        }
        return false;
    }
    
    @Override
    public void discardBuffer(final long n, final boolean b) {
        if (this.isPendingReset()) {
            return;
        }
        final boolean[] trackEnabledStates = this.getPreparedState().trackEnabledStates;
        for (int length = this.sampleQueues.length, i = 0; i < length; ++i) {
            this.sampleQueues[i].discardTo(n, b, trackEnabledStates[i]);
        }
    }
    
    @Override
    public void endTracks() {
        this.sampleQueuesBuilt = true;
        this.handler.post(this.maybeFinishPrepareRunnable);
    }
    
    @Override
    public long getAdjustedSeekPositionUs(final long n, final SeekParameters seekParameters) {
        final SeekMap seekMap = this.getPreparedState().seekMap;
        if (!seekMap.isSeekable()) {
            return 0L;
        }
        final SeekMap.SeekPoints seekPoints = seekMap.getSeekPoints(n);
        return Util.resolveSeekPositionUs(n, seekParameters, seekPoints.first.timeUs, seekPoints.second.timeUs);
    }
    
    @Override
    public long getBufferedPositionUs() {
        final boolean[] trackIsAudioVideoFlags = this.getPreparedState().trackIsAudioVideoFlags;
        if (this.loadingFinished) {
            return Long.MIN_VALUE;
        }
        if (this.isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long n2;
        if (this.haveAudioVideoTracks) {
            final int length = this.sampleQueues.length;
            int n = 0;
            long a = Long.MAX_VALUE;
            while (true) {
                n2 = a;
                if (n >= length) {
                    break;
                }
                long min = a;
                if (trackIsAudioVideoFlags[n]) {
                    min = a;
                    if (!this.sampleQueues[n].isLastSampleQueued()) {
                        min = Math.min(a, this.sampleQueues[n].getLargestQueuedTimestampUs());
                    }
                }
                ++n;
                a = min;
            }
        }
        else {
            n2 = Long.MAX_VALUE;
        }
        long largestQueuedTimestampUs = n2;
        if (n2 == Long.MAX_VALUE) {
            largestQueuedTimestampUs = this.getLargestQueuedTimestampUs();
        }
        long lastSeekPositionUs = largestQueuedTimestampUs;
        if (largestQueuedTimestampUs == Long.MIN_VALUE) {
            lastSeekPositionUs = this.lastSeekPositionUs;
        }
        return lastSeekPositionUs;
    }
    
    @Override
    public long getNextLoadPositionUs() {
        long bufferedPositionUs;
        if (this.enabledTrackCount == 0) {
            bufferedPositionUs = Long.MIN_VALUE;
        }
        else {
            bufferedPositionUs = this.getBufferedPositionUs();
        }
        return bufferedPositionUs;
    }
    
    @Override
    public TrackGroupArray getTrackGroups() {
        return this.getPreparedState().tracks;
    }
    
    TrackOutput icyTrack() {
        return this.prepareTrackOutput(new TrackId(0, true));
    }
    
    boolean isReady(final int n) {
        return !this.suppressRead() && (this.loadingFinished || this.sampleQueues[n].hasNextSample());
    }
    
    void maybeThrowError() throws IOException {
        this.loader.maybeThrowError(this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(this.dataType));
    }
    
    @Override
    public void maybeThrowPrepareError() throws IOException {
        this.maybeThrowError();
    }
    
    public void onLoadCanceled(final ExtractingLoadable extractingLoadable, final long n, final long n2, final boolean b) {
        this.eventDispatcher.loadCanceled(extractingLoadable.dataSpec, extractingLoadable.dataSource.getLastOpenedUri(), extractingLoadable.dataSource.getLastResponseHeaders(), 1, -1, null, 0, null, extractingLoadable.seekTimeUs, this.durationUs, n, n2, extractingLoadable.dataSource.getBytesRead());
        if (!b) {
            this.copyLengthFromLoader(extractingLoadable);
            final SampleQueue[] sampleQueues = this.sampleQueues;
            for (int length = sampleQueues.length, i = 0; i < length; ++i) {
                sampleQueues[i].reset();
            }
            if (this.enabledTrackCount > 0) {
                final MediaPeriod.Callback callback = this.callback;
                Assertions.checkNotNull(callback);
                ((SequenceableLoader.Callback<ExtractorMediaPeriod>)callback).onContinueLoadingRequested(this);
            }
        }
    }
    
    public void onLoadCompleted(final ExtractingLoadable extractingLoadable, final long n, final long n2) {
        if (this.durationUs == -9223372036854775807L) {
            final SeekMap seekMap = this.seekMap;
            Assertions.checkNotNull(seekMap);
            final SeekMap seekMap2 = seekMap;
            final long largestQueuedTimestampUs = this.getLargestQueuedTimestampUs();
            long durationUs;
            if (largestQueuedTimestampUs == Long.MIN_VALUE) {
                durationUs = 0L;
            }
            else {
                durationUs = largestQueuedTimestampUs + 10000L;
            }
            this.durationUs = durationUs;
            this.listener.onSourceInfoRefreshed(this.durationUs, seekMap2.isSeekable());
        }
        this.eventDispatcher.loadCompleted(extractingLoadable.dataSpec, extractingLoadable.dataSource.getLastOpenedUri(), extractingLoadable.dataSource.getLastResponseHeaders(), 1, -1, null, 0, null, extractingLoadable.seekTimeUs, this.durationUs, n, n2, extractingLoadable.dataSource.getBytesRead());
        this.copyLengthFromLoader(extractingLoadable);
        this.loadingFinished = true;
        final MediaPeriod.Callback callback = this.callback;
        Assertions.checkNotNull(callback);
        ((SequenceableLoader.Callback<ExtractorMediaPeriod>)callback).onContinueLoadingRequested(this);
    }
    
    public LoadErrorAction onLoadError(final ExtractingLoadable extractingLoadable, final long n, final long n2, final IOException ex, int extractedSamplesCount) {
        this.copyLengthFromLoader(extractingLoadable);
        final long retryDelayMs = this.loadErrorHandlingPolicy.getRetryDelayMsFor(this.dataType, this.durationUs, ex, extractedSamplesCount);
        Loader.LoadErrorAction loadErrorAction;
        if (retryDelayMs == -9223372036854775807L) {
            loadErrorAction = Loader.DONT_RETRY_FATAL;
        }
        else {
            extractedSamplesCount = this.getExtractedSamplesCount();
            final boolean b = extractedSamplesCount > this.extractedSamplesCountAtStartOfLoad;
            if (this.configureRetry(extractingLoadable, extractedSamplesCount)) {
                loadErrorAction = Loader.createRetryAction(b, retryDelayMs);
            }
            else {
                loadErrorAction = Loader.DONT_RETRY;
            }
        }
        this.eventDispatcher.loadError(extractingLoadable.dataSpec, extractingLoadable.dataSource.getLastOpenedUri(), extractingLoadable.dataSource.getLastResponseHeaders(), 1, -1, null, 0, null, extractingLoadable.seekTimeUs, this.durationUs, n, n2, extractingLoadable.dataSource.getBytesRead(), ex, loadErrorAction.isRetry() ^ true);
        return loadErrorAction;
    }
    
    @Override
    public void onLoaderReleased() {
        final SampleQueue[] sampleQueues = this.sampleQueues;
        for (int length = sampleQueues.length, i = 0; i < length; ++i) {
            sampleQueues[i].reset();
        }
        this.extractorHolder.release();
    }
    
    @Override
    public void onUpstreamFormatChanged(final Format format) {
        this.handler.post(this.maybeFinishPrepareRunnable);
    }
    
    @Override
    public void prepare(final MediaPeriod.Callback callback, final long n) {
        this.callback = callback;
        this.loadCondition.open();
        this.startLoading();
    }
    
    int readData(final int n, final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b) {
        if (this.suppressRead()) {
            return -3;
        }
        this.maybeNotifyDownstreamFormat(n);
        final int read = this.sampleQueues[n].read(formatHolder, decoderInputBuffer, b, this.loadingFinished, this.lastSeekPositionUs);
        if (read == -3) {
            this.maybeStartDeferredRetry(n);
        }
        return read;
    }
    
    @Override
    public long readDiscontinuity() {
        if (!this.notifiedReadingStarted) {
            this.eventDispatcher.readingStarted();
            this.notifiedReadingStarted = true;
        }
        if (this.notifyDiscontinuity && (this.loadingFinished || this.getExtractedSamplesCount() > this.extractedSamplesCountAtStartOfLoad)) {
            this.notifyDiscontinuity = false;
            return this.lastSeekPositionUs;
        }
        return -9223372036854775807L;
    }
    
    @Override
    public void reevaluateBuffer(final long n) {
    }
    
    public void release() {
        if (this.prepared) {
            final SampleQueue[] sampleQueues = this.sampleQueues;
            for (int length = sampleQueues.length, i = 0; i < length; ++i) {
                sampleQueues[i].discardToEnd();
            }
        }
        this.loader.release((Loader.ReleaseCallback)this);
        this.handler.removeCallbacksAndMessages((Object)null);
        this.callback = null;
        this.released = true;
        this.eventDispatcher.mediaPeriodReleased();
    }
    
    @Override
    public void seekMap(final SeekMap seekMap) {
        this.seekMap = seekMap;
        this.handler.post(this.maybeFinishPrepareRunnable);
    }
    
    @Override
    public long seekToUs(long pendingResetPositionUs) {
        final PreparedState preparedState = this.getPreparedState();
        final SeekMap seekMap = preparedState.seekMap;
        final boolean[] trackIsAudioVideoFlags = preparedState.trackIsAudioVideoFlags;
        if (!seekMap.isSeekable()) {
            pendingResetPositionUs = 0L;
        }
        int i = 0;
        this.notifyDiscontinuity = false;
        this.lastSeekPositionUs = pendingResetPositionUs;
        if (this.isPendingReset()) {
            return this.pendingResetPositionUs = pendingResetPositionUs;
        }
        if (this.dataType != 7 && this.seekInsideBufferUs(trackIsAudioVideoFlags, pendingResetPositionUs)) {
            return pendingResetPositionUs;
        }
        this.pendingDeferredRetry = false;
        this.pendingResetPositionUs = pendingResetPositionUs;
        this.loadingFinished = false;
        if (this.loader.isLoading()) {
            this.loader.cancelLoading();
        }
        else {
            for (SampleQueue[] sampleQueues = this.sampleQueues; i < sampleQueues.length; ++i) {
                sampleQueues[i].reset();
            }
        }
        return pendingResetPositionUs;
    }
    
    @Override
    public long selectTracks(final TrackSelection[] array, final boolean[] array2, final SampleStream[] array3, final boolean[] array4, long seekToUs) {
        final PreparedState preparedState = this.getPreparedState();
        final TrackGroupArray tracks = preparedState.tracks;
        final boolean[] trackEnabledStates = preparedState.trackEnabledStates;
        final int enabledTrackCount = this.enabledTrackCount;
        final int n = 0;
        final int n2 = 0;
        final int n3 = 0;
        for (int i = 0; i < array.length; ++i) {
            if (array3[i] != null && (array[i] == null || !array2[i])) {
                final int access$000 = ((SampleStreamImpl)array3[i]).track;
                Assertions.checkState(trackEnabledStates[access$000]);
                --this.enabledTrackCount;
                trackEnabledStates[access$000] = false;
                array3[i] = null;
            }
        }
        final boolean b = this.seenFirstTrackSelection ? (enabledTrackCount == 0) : (seekToUs != 0L);
        int j = 0;
        int n4 = b ? 1 : 0;
        while (j < array.length) {
            int n5 = n4;
            if (array3[j] == null) {
                n5 = n4;
                if (array[j] != null) {
                    final TrackSelection trackSelection = array[j];
                    Assertions.checkState(trackSelection.length() == 1);
                    Assertions.checkState(trackSelection.getIndexInTrackGroup(0) == 0);
                    final int index = tracks.indexOf(trackSelection.getTrackGroup());
                    Assertions.checkState(trackEnabledStates[index] ^ true);
                    ++this.enabledTrackCount;
                    trackEnabledStates[index] = true;
                    array3[j] = new SampleStreamImpl(index);
                    array4[j] = true;
                    if ((n5 = n4) == 0) {
                        final SampleQueue sampleQueue = this.sampleQueues[index];
                        sampleQueue.rewind();
                        if (sampleQueue.advanceTo(seekToUs, true, true) == -1 && sampleQueue.getReadIndex() != 0) {
                            n5 = 1;
                        }
                        else {
                            n5 = 0;
                        }
                    }
                }
            }
            ++j;
            n4 = n5;
        }
        long n6;
        if (this.enabledTrackCount == 0) {
            this.pendingDeferredRetry = false;
            this.notifyDiscontinuity = false;
            if (this.loader.isLoading()) {
                final SampleQueue[] sampleQueues = this.sampleQueues;
                for (int length = sampleQueues.length, k = n3; k < length; ++k) {
                    sampleQueues[k].discardToEnd();
                }
                this.loader.cancelLoading();
                n6 = seekToUs;
            }
            else {
                final SampleQueue[] sampleQueues2 = this.sampleQueues;
                final int length2 = sampleQueues2.length;
                int n7 = n;
                while (true) {
                    n6 = seekToUs;
                    if (n7 >= length2) {
                        break;
                    }
                    sampleQueues2[n7].reset();
                    ++n7;
                }
            }
        }
        else {
            n6 = seekToUs;
            if (n4 != 0) {
                seekToUs = this.seekToUs(seekToUs);
                int n8 = n2;
                while (true) {
                    n6 = seekToUs;
                    if (n8 >= array3.length) {
                        break;
                    }
                    if (array3[n8] != null) {
                        array4[n8] = true;
                    }
                    ++n8;
                }
            }
        }
        this.seenFirstTrackSelection = true;
        return n6;
    }
    
    int skipData(final int n, final long n2) {
        final boolean suppressRead = this.suppressRead();
        int advanceToEnd = 0;
        if (suppressRead) {
            return 0;
        }
        this.maybeNotifyDownstreamFormat(n);
        final SampleQueue sampleQueue = this.sampleQueues[n];
        if (this.loadingFinished && n2 > sampleQueue.getLargestQueuedTimestampUs()) {
            advanceToEnd = sampleQueue.advanceToEnd();
        }
        else {
            final int advanceTo = sampleQueue.advanceTo(n2, true, true);
            if (advanceTo != -1) {
                advanceToEnd = advanceTo;
            }
        }
        if (advanceToEnd == 0) {
            this.maybeStartDeferredRetry(n);
        }
        return advanceToEnd;
    }
    
    @Override
    public TrackOutput track(final int n, final int n2) {
        return this.prepareTrackOutput(new TrackId(n, false));
    }
    
    final class ExtractingLoadable implements Loadable, IcyDataSource.Listener
    {
        private final StatsDataSource dataSource;
        private DataSpec dataSpec;
        private final ExtractorHolder extractorHolder;
        private final ExtractorOutput extractorOutput;
        private TrackOutput icyTrackOutput;
        private long length;
        private volatile boolean loadCanceled;
        private final ConditionVariable loadCondition;
        private boolean pendingExtractorSeek;
        private final PositionHolder positionHolder;
        private long seekTimeUs;
        private boolean seenIcyMetadata;
        private final Uri uri;
        
        public ExtractingLoadable(final Uri uri, final DataSource dataSource, final ExtractorHolder extractorHolder, final ExtractorOutput extractorOutput, final ConditionVariable loadCondition) {
            this.uri = uri;
            this.dataSource = new StatsDataSource(dataSource);
            this.extractorHolder = extractorHolder;
            this.extractorOutput = extractorOutput;
            this.loadCondition = loadCondition;
            this.positionHolder = new PositionHolder();
            this.pendingExtractorSeek = true;
            this.length = -1L;
            this.dataSpec = this.buildDataSpec(0L);
        }
        
        private DataSpec buildDataSpec(final long n) {
            return new DataSpec(this.uri, n, -1L, ExtractorMediaPeriod.this.customCacheKey, 22);
        }
        
        private void setLoadPosition(final long position, final long seekTimeUs) {
            this.positionHolder.position = position;
            this.seekTimeUs = seekTimeUs;
            this.pendingExtractorSeek = true;
            this.seenIcyMetadata = false;
        }
        
        @Override
        public void cancelLoad() {
            this.loadCanceled = true;
        }
        
        @Override
        public void load() throws IOException, InterruptedException {
            int n = 0;
            while (n == 0 && !this.loadCanceled) {
                final ExtractorInput extractorInput = null;
                ExtractorInput extractorInput2 = null;
                try {
                    final long position = this.positionHolder.position;
                    this.dataSpec = this.buildDataSpec(position);
                    this.length = this.dataSource.open(this.dataSpec);
                    if (this.length != -1L) {
                        this.length += position;
                    }
                    final Uri uri = this.dataSource.getUri();
                    Assertions.checkNotNull(uri);
                    final Uri uri2 = uri;
                    ExtractorMediaPeriod.this.icyHeaders = IcyHeaders.parse(this.dataSource.getResponseHeaders());
                    DataSource dataSource;
                    final StatsDataSource statsDataSource = (StatsDataSource)(dataSource = this.dataSource);
                    if (ExtractorMediaPeriod.this.icyHeaders != null) {
                        dataSource = statsDataSource;
                        if (ExtractorMediaPeriod.this.icyHeaders.metadataInterval != -1) {
                            dataSource = new IcyDataSource(this.dataSource, ExtractorMediaPeriod.this.icyHeaders.metadataInterval, (IcyDataSource.Listener)this);
                            (this.icyTrackOutput = ExtractorMediaPeriod.this.icyTrack()).format(ExtractorMediaPeriod.ICY_FORMAT);
                        }
                    }
                    final DefaultExtractorInput defaultExtractorInput = new DefaultExtractorInput(dataSource, position, this.length);
                    int n2 = n;
                    try {
                        final Extractor selectExtractor = this.extractorHolder.selectExtractor(defaultExtractorInput, this.extractorOutput, uri2);
                        int i = n;
                        long position2 = position;
                        n2 = n;
                        if (this.pendingExtractorSeek) {
                            n2 = n;
                            selectExtractor.seek(position, this.seekTimeUs);
                            n2 = n;
                            this.pendingExtractorSeek = false;
                            position2 = position;
                            i = n;
                        }
                        while (i == 0) {
                            n2 = i;
                            if (this.loadCanceled) {
                                break;
                            }
                            n2 = i;
                            this.loadCondition.block();
                            n2 = i;
                            n = (n2 = (i = selectExtractor.read(defaultExtractorInput, this.positionHolder)));
                            if (defaultExtractorInput.getPosition() <= ExtractorMediaPeriod.this.continueLoadingCheckIntervalBytes + position2) {
                                continue;
                            }
                            n2 = n;
                            position2 = defaultExtractorInput.getPosition();
                            n2 = n;
                            this.loadCondition.close();
                            n2 = n;
                            ExtractorMediaPeriod.this.handler.post(ExtractorMediaPeriod.this.onContinueLoadingRequestedRunnable);
                            i = n;
                        }
                        if (i == 1) {
                            n = 0;
                        }
                        else {
                            this.positionHolder.position = defaultExtractorInput.getPosition();
                            n = i;
                        }
                        Util.closeQuietly(this.dataSource);
                    }
                    finally {
                        n = n2;
                    }
                }
                finally {
                    extractorInput2 = extractorInput;
                }
                if (n != 1 && extractorInput2 != null) {
                    this.positionHolder.position = extractorInput2.getPosition();
                }
                Util.closeQuietly(this.dataSource);
            }
        }
        
        @Override
        public void onIcyMetadata(final ParsableByteArray parsableByteArray) {
            long n;
            if (!this.seenIcyMetadata) {
                n = this.seekTimeUs;
            }
            else {
                n = Math.max(ExtractorMediaPeriod.this.getLargestQueuedTimestampUs(), this.seekTimeUs);
            }
            final int bytesLeft = parsableByteArray.bytesLeft();
            final TrackOutput icyTrackOutput = this.icyTrackOutput;
            Assertions.checkNotNull(icyTrackOutput);
            final TrackOutput trackOutput = icyTrackOutput;
            trackOutput.sampleData(parsableByteArray, bytesLeft);
            trackOutput.sampleMetadata(n, 1, bytesLeft, 0, null);
            this.seenIcyMetadata = true;
        }
    }
    
    private static final class ExtractorHolder
    {
        private Extractor extractor;
        private final Extractor[] extractors;
        
        public ExtractorHolder(final Extractor[] extractors) {
            this.extractors = extractors;
        }
        
        public void release() {
            final Extractor extractor = this.extractor;
            if (extractor != null) {
                extractor.release();
                this.extractor = null;
            }
        }
        
        public Extractor selectExtractor(final ExtractorInput extractorInput, final ExtractorOutput extractorOutput, final Uri uri) throws IOException, InterruptedException {
            final Extractor extractor = this.extractor;
            if (extractor != null) {
                return extractor;
            }
            final Extractor[] extractors = this.extractors;
            final int length = extractors.length;
            final int n = 0;
            if (n >= length) {
                goto Label_0089;
            }
            final Extractor extractor2 = extractors[n];
            try {
                if (extractor2.sniff(extractorInput)) {
                    this.extractor = extractor2;
                    goto Label_0089;
                }
                goto Label_0077;
            }
            catch (EOFException ex) {
                goto Label_0077;
            }
            finally {
                extractorInput.resetPeekPosition();
            }
        }
    }
    
    interface Listener
    {
        void onSourceInfoRefreshed(final long p0, final boolean p1);
    }
    
    private static final class PreparedState
    {
        public final SeekMap seekMap;
        public final boolean[] trackEnabledStates;
        public final boolean[] trackIsAudioVideoFlags;
        public final boolean[] trackNotifiedDownstreamFormats;
        public final TrackGroupArray tracks;
        
        public PreparedState(final SeekMap seekMap, final TrackGroupArray tracks, final boolean[] trackIsAudioVideoFlags) {
            this.seekMap = seekMap;
            this.tracks = tracks;
            this.trackIsAudioVideoFlags = trackIsAudioVideoFlags;
            final int length = tracks.length;
            this.trackEnabledStates = new boolean[length];
            this.trackNotifiedDownstreamFormats = new boolean[length];
        }
    }
    
    private final class SampleStreamImpl implements SampleStream
    {
        private final int track;
        
        public SampleStreamImpl(final int track) {
            this.track = track;
        }
        
        @Override
        public boolean isReady() {
            return ExtractorMediaPeriod.this.isReady(this.track);
        }
        
        @Override
        public void maybeThrowError() throws IOException {
            ExtractorMediaPeriod.this.maybeThrowError();
        }
        
        @Override
        public int readData(final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b) {
            return ExtractorMediaPeriod.this.readData(this.track, formatHolder, decoderInputBuffer, b);
        }
        
        @Override
        public int skipData(final long n) {
            return ExtractorMediaPeriod.this.skipData(this.track, n);
        }
    }
    
    private static final class TrackId
    {
        public final int id;
        public final boolean isIcyTrack;
        
        public TrackId(final int id, final boolean isIcyTrack) {
            this.id = id;
            this.isIcyTrack = isIcyTrack;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this == o) {
                return true;
            }
            if (o != null && TrackId.class == o.getClass()) {
                final TrackId trackId = (TrackId)o;
                if (this.id != trackId.id || this.isIcyTrack != trackId.isIcyTrack) {
                    b = false;
                }
                return b;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.id * 31 + (this.isIcyTrack ? 1 : 0);
        }
    }
}
