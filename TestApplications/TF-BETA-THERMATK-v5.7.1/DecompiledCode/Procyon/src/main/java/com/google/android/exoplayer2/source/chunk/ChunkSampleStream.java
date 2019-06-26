// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.util.Util;
import java.util.Collections;
import com.google.android.exoplayer2.upstream.Allocator;
import java.util.List;
import java.util.ArrayList;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.SampleQueue;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.SampleStream;

public class ChunkSampleStream<T extends ChunkSource> implements SampleStream, SequenceableLoader, Loader.Callback<Chunk>, Loader.ReleaseCallback
{
    private final SequenceableLoader.Callback<ChunkSampleStream<T>> callback;
    private final T chunkSource;
    long decodeOnlyUntilPositionUs;
    private final SampleQueue[] embeddedSampleQueues;
    private final Format[] embeddedTrackFormats;
    private final int[] embeddedTrackTypes;
    private final boolean[] embeddedTracksSelected;
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
    private long lastSeekPositionUs;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final Loader loader;
    boolean loadingFinished;
    private final BaseMediaChunkOutput mediaChunkOutput;
    private final ArrayList<BaseMediaChunk> mediaChunks;
    private final ChunkHolder nextChunkHolder;
    private int nextNotifyPrimaryFormatMediaChunkIndex;
    private long pendingResetPositionUs;
    private Format primaryDownstreamTrackFormat;
    private final SampleQueue primarySampleQueue;
    public final int primaryTrackType;
    private final List<BaseMediaChunk> readOnlyMediaChunks;
    private ReleaseCallback<T> releaseCallback;
    
    public ChunkSampleStream(int i, final int[] embeddedTrackTypes, final Format[] embeddedTrackFormats, final T chunkSource, final SequenceableLoader.Callback<ChunkSampleStream<T>> callback, final Allocator allocator, final long n, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final MediaSourceEventListener.EventDispatcher eventDispatcher) {
        this.primaryTrackType = i;
        this.embeddedTrackTypes = embeddedTrackTypes;
        this.embeddedTrackFormats = embeddedTrackFormats;
        this.chunkSource = chunkSource;
        this.callback = callback;
        this.eventDispatcher = eventDispatcher;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.loader = new Loader("Loader:ChunkSampleStream");
        this.nextChunkHolder = new ChunkHolder();
        this.mediaChunks = new ArrayList<BaseMediaChunk>();
        this.readOnlyMediaChunks = Collections.unmodifiableList((List<? extends BaseMediaChunk>)this.mediaChunks);
        final int n2 = 0;
        int length;
        if (embeddedTrackTypes == null) {
            length = 0;
        }
        else {
            length = embeddedTrackTypes.length;
        }
        this.embeddedSampleQueues = new SampleQueue[length];
        this.embeddedTracksSelected = new boolean[length];
        final int n3 = length + 1;
        final int[] array = new int[n3];
        final SampleQueue[] array2 = new SampleQueue[n3];
        this.primarySampleQueue = new SampleQueue(allocator);
        array[0] = i;
        array2[0] = this.primarySampleQueue;
        SampleQueue sampleQueue;
        int n4;
        for (i = n2; i < length; i = n4) {
            sampleQueue = new SampleQueue(allocator);
            this.embeddedSampleQueues[i] = sampleQueue;
            n4 = i + 1;
            array2[n4] = sampleQueue;
            array[n4] = embeddedTrackTypes[i];
        }
        this.mediaChunkOutput = new BaseMediaChunkOutput(array, array2);
        this.pendingResetPositionUs = n;
        this.lastSeekPositionUs = n;
    }
    
    private void discardDownstreamMediaChunks(int min) {
        min = Math.min(this.primarySampleIndexToMediaChunkIndex(min, 0), this.nextNotifyPrimaryFormatMediaChunkIndex);
        if (min > 0) {
            Util.removeRange(this.mediaChunks, 0, min);
            this.nextNotifyPrimaryFormatMediaChunkIndex -= min;
        }
    }
    
    private BaseMediaChunk discardUpstreamMediaChunksFromIndex(int index) {
        final BaseMediaChunk baseMediaChunk = this.mediaChunks.get(index);
        final ArrayList<BaseMediaChunk> mediaChunks = this.mediaChunks;
        Util.removeRange((List<Object>)mediaChunks, index, mediaChunks.size());
        this.nextNotifyPrimaryFormatMediaChunkIndex = Math.max(this.nextNotifyPrimaryFormatMediaChunkIndex, this.mediaChunks.size());
        final SampleQueue primarySampleQueue = this.primarySampleQueue;
        index = 0;
        primarySampleQueue.discardUpstreamSamples(baseMediaChunk.getFirstSampleIndex(0));
        while (true) {
            final SampleQueue[] embeddedSampleQueues = this.embeddedSampleQueues;
            if (index >= embeddedSampleQueues.length) {
                break;
            }
            final SampleQueue sampleQueue = embeddedSampleQueues[index];
            ++index;
            sampleQueue.discardUpstreamSamples(baseMediaChunk.getFirstSampleIndex(index));
        }
        return baseMediaChunk;
    }
    
    private BaseMediaChunk getLastMediaChunk() {
        final ArrayList<BaseMediaChunk> mediaChunks = this.mediaChunks;
        return mediaChunks.get(mediaChunks.size() - 1);
    }
    
    private boolean haveReadFromMediaChunk(int index) {
        final BaseMediaChunk baseMediaChunk = this.mediaChunks.get(index);
        if (this.primarySampleQueue.getReadIndex() > baseMediaChunk.getFirstSampleIndex(0)) {
            return true;
        }
        index = 0;
        SampleQueue[] embeddedSampleQueues;
        do {
            embeddedSampleQueues = this.embeddedSampleQueues;
            if (index < embeddedSampleQueues.length) {
                continue;
            }
            return false;
        } while (embeddedSampleQueues[index].getReadIndex() <= baseMediaChunk.getFirstSampleIndex(++index));
        return true;
    }
    
    private boolean isMediaChunk(final Chunk chunk) {
        return chunk instanceof BaseMediaChunk;
    }
    
    private void maybeNotifyPrimaryTrackFormatChanged() {
        final int primarySampleIndexToMediaChunkIndex = this.primarySampleIndexToMediaChunkIndex(this.primarySampleQueue.getReadIndex(), this.nextNotifyPrimaryFormatMediaChunkIndex - 1);
        while (true) {
            final int nextNotifyPrimaryFormatMediaChunkIndex = this.nextNotifyPrimaryFormatMediaChunkIndex;
            if (nextNotifyPrimaryFormatMediaChunkIndex > primarySampleIndexToMediaChunkIndex) {
                break;
            }
            this.nextNotifyPrimaryFormatMediaChunkIndex = nextNotifyPrimaryFormatMediaChunkIndex + 1;
            this.maybeNotifyPrimaryTrackFormatChanged(nextNotifyPrimaryFormatMediaChunkIndex);
        }
    }
    
    private void maybeNotifyPrimaryTrackFormatChanged(final int index) {
        final BaseMediaChunk baseMediaChunk = this.mediaChunks.get(index);
        final Format trackFormat = baseMediaChunk.trackFormat;
        if (!trackFormat.equals(this.primaryDownstreamTrackFormat)) {
            this.eventDispatcher.downstreamFormatChanged(this.primaryTrackType, trackFormat, baseMediaChunk.trackSelectionReason, baseMediaChunk.trackSelectionData, baseMediaChunk.startTimeUs);
        }
        this.primaryDownstreamTrackFormat = trackFormat;
    }
    
    private int primarySampleIndexToMediaChunkIndex(final int n, int n2) {
        int index;
        do {
            index = n2 + 1;
            if (index >= this.mediaChunks.size()) {
                return this.mediaChunks.size() - 1;
            }
            n2 = index;
        } while (this.mediaChunks.get(index).getFirstSampleIndex(0) <= n);
        return index - 1;
    }
    
    @Override
    public boolean continueLoading(long decodeOnlyUntilPositionUs) {
        final boolean loadingFinished = this.loadingFinished;
        boolean b = false;
        if (loadingFinished || this.loader.isLoading()) {
            return false;
        }
        final boolean pendingReset = this.isPendingReset();
        List<? extends MediaChunk> list;
        long n;
        if (pendingReset) {
            list = Collections.emptyList();
            n = this.pendingResetPositionUs;
        }
        else {
            list = this.readOnlyMediaChunks;
            n = this.getLastMediaChunk().endTimeUs;
        }
        this.chunkSource.getNextChunk(decodeOnlyUntilPositionUs, n, list, this.nextChunkHolder);
        final ChunkHolder nextChunkHolder = this.nextChunkHolder;
        final boolean endOfStream = nextChunkHolder.endOfStream;
        final Chunk chunk = nextChunkHolder.chunk;
        nextChunkHolder.clear();
        if (endOfStream) {
            this.pendingResetPositionUs = -9223372036854775807L;
            return this.loadingFinished = true;
        }
        if (chunk == null) {
            return false;
        }
        if (this.isMediaChunk(chunk)) {
            final BaseMediaChunk e = (BaseMediaChunk)chunk;
            if (pendingReset) {
                if (e.startTimeUs == this.pendingResetPositionUs) {
                    b = true;
                }
                if (b) {
                    decodeOnlyUntilPositionUs = 0L;
                }
                else {
                    decodeOnlyUntilPositionUs = this.pendingResetPositionUs;
                }
                this.decodeOnlyUntilPositionUs = decodeOnlyUntilPositionUs;
                this.pendingResetPositionUs = -9223372036854775807L;
            }
            e.init(this.mediaChunkOutput);
            this.mediaChunks.add(e);
        }
        decodeOnlyUntilPositionUs = this.loader.startLoading(chunk, (Loader.Callback<?>)this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(chunk.type));
        this.eventDispatcher.loadStarted(chunk.dataSpec, chunk.type, this.primaryTrackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, decodeOnlyUntilPositionUs);
        return true;
    }
    
    public void discardBuffer(long firstTimestampUs, final boolean b) {
        if (this.isPendingReset()) {
            return;
        }
        final int firstIndex = this.primarySampleQueue.getFirstIndex();
        this.primarySampleQueue.discardTo(firstTimestampUs, b, true);
        final int firstIndex2 = this.primarySampleQueue.getFirstIndex();
        if (firstIndex2 > firstIndex) {
            firstTimestampUs = this.primarySampleQueue.getFirstTimestampUs();
            int n = 0;
            while (true) {
                final SampleQueue[] embeddedSampleQueues = this.embeddedSampleQueues;
                if (n >= embeddedSampleQueues.length) {
                    break;
                }
                embeddedSampleQueues[n].discardTo(firstTimestampUs, b, this.embeddedTracksSelected[n]);
                ++n;
            }
        }
        this.discardDownstreamMediaChunks(firstIndex2);
    }
    
    public long getAdjustedSeekPositionUs(final long n, final SeekParameters seekParameters) {
        return this.chunkSource.getAdjustedSeekPositionUs(n, seekParameters);
    }
    
    @Override
    public long getBufferedPositionUs() {
        if (this.loadingFinished) {
            return Long.MIN_VALUE;
        }
        if (this.isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        final long lastSeekPositionUs = this.lastSeekPositionUs;
        BaseMediaChunk lastMediaChunk = this.getLastMediaChunk();
        if (!lastMediaChunk.isLoadCompleted()) {
            if (this.mediaChunks.size() > 1) {
                final ArrayList<BaseMediaChunk> mediaChunks = this.mediaChunks;
                lastMediaChunk = mediaChunks.get(mediaChunks.size() - 2);
            }
            else {
                lastMediaChunk = null;
            }
        }
        long max = lastSeekPositionUs;
        if (lastMediaChunk != null) {
            max = Math.max(lastSeekPositionUs, lastMediaChunk.endTimeUs);
        }
        return Math.max(max, this.primarySampleQueue.getLargestQueuedTimestampUs());
    }
    
    public T getChunkSource() {
        return this.chunkSource;
    }
    
    @Override
    public long getNextLoadPositionUs() {
        if (this.isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long endTimeUs;
        if (this.loadingFinished) {
            endTimeUs = Long.MIN_VALUE;
        }
        else {
            endTimeUs = this.getLastMediaChunk().endTimeUs;
        }
        return endTimeUs;
    }
    
    boolean isPendingReset() {
        return this.pendingResetPositionUs != -9223372036854775807L;
    }
    
    @Override
    public boolean isReady() {
        return this.loadingFinished || (!this.isPendingReset() && this.primarySampleQueue.hasNextSample());
    }
    
    @Override
    public void maybeThrowError() throws IOException {
        this.loader.maybeThrowError();
        if (!this.loader.isLoading()) {
            this.chunkSource.maybeThrowError();
        }
    }
    
    public void onLoadCanceled(final Chunk chunk, final long n, final long n2, final boolean b) {
        this.eventDispatcher.loadCanceled(chunk.dataSpec, chunk.getUri(), chunk.getResponseHeaders(), chunk.type, this.primaryTrackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, n, n2, chunk.bytesLoaded());
        if (!b) {
            this.primarySampleQueue.reset();
            final SampleQueue[] embeddedSampleQueues = this.embeddedSampleQueues;
            for (int length = embeddedSampleQueues.length, i = 0; i < length; ++i) {
                embeddedSampleQueues[i].reset();
            }
            this.callback.onContinueLoadingRequested(this);
        }
    }
    
    public void onLoadCompleted(final Chunk chunk, final long n, final long n2) {
        this.chunkSource.onChunkLoadCompleted(chunk);
        this.eventDispatcher.loadCompleted(chunk.dataSpec, chunk.getUri(), chunk.getResponseHeaders(), chunk.type, this.primaryTrackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, n, n2, chunk.bytesLoaded());
        this.callback.onContinueLoadingRequested(this);
    }
    
    public LoadErrorAction onLoadError(final Chunk chunk, final long n, final long n2, final IOException ex, final int n3) {
        final long bytesLoaded = chunk.bytesLoaded();
        final boolean mediaChunk = this.isMediaChunk(chunk);
        final int n4 = this.mediaChunks.size() - 1;
        final boolean b = bytesLoaded == 0L || !mediaChunk || !this.haveReadFromMediaChunk(n4);
        long blacklistDurationMs;
        if (b) {
            blacklistDurationMs = this.loadErrorHandlingPolicy.getBlacklistDurationMsFor(chunk.type, n2, ex, n3);
        }
        else {
            blacklistDurationMs = -9223372036854775807L;
        }
        Object dont_RETRY = null;
        if (this.chunkSource.onChunkLoadError(chunk, b, ex, blacklistDurationMs)) {
            if (b) {
                final Loader.LoadErrorAction loadErrorAction = (Loader.LoadErrorAction)(dont_RETRY = Loader.DONT_RETRY);
                if (mediaChunk) {
                    Assertions.checkState(this.discardUpstreamMediaChunksFromIndex(n4) == chunk);
                    dont_RETRY = loadErrorAction;
                    if (this.mediaChunks.isEmpty()) {
                        this.pendingResetPositionUs = this.lastSeekPositionUs;
                        dont_RETRY = loadErrorAction;
                    }
                }
            }
            else {
                Log.w("ChunkSampleStream", "Ignoring attempt to cancel non-cancelable load.");
                dont_RETRY = dont_RETRY;
            }
        }
        LoadErrorAction loadErrorAction2;
        if ((loadErrorAction2 = (LoadErrorAction)dont_RETRY) == null) {
            final long retryDelayMs = this.loadErrorHandlingPolicy.getRetryDelayMsFor(chunk.type, n2, ex, n3);
            Loader.LoadErrorAction loadErrorAction3;
            if (retryDelayMs != -9223372036854775807L) {
                loadErrorAction3 = Loader.createRetryAction(false, retryDelayMs);
            }
            else {
                loadErrorAction3 = Loader.DONT_RETRY_FATAL;
            }
            loadErrorAction2 = loadErrorAction3;
        }
        final boolean b2 = loadErrorAction2.isRetry() ^ true;
        this.eventDispatcher.loadError(chunk.dataSpec, chunk.getUri(), chunk.getResponseHeaders(), chunk.type, this.primaryTrackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, n, n2, bytesLoaded, ex, b2);
        if (b2) {
            this.callback.onContinueLoadingRequested(this);
        }
        return loadErrorAction2;
    }
    
    @Override
    public void onLoaderReleased() {
        this.primarySampleQueue.reset();
        final SampleQueue[] embeddedSampleQueues = this.embeddedSampleQueues;
        for (int length = embeddedSampleQueues.length, i = 0; i < length; ++i) {
            embeddedSampleQueues[i].reset();
        }
        final ReleaseCallback<T> releaseCallback = this.releaseCallback;
        if (releaseCallback != null) {
            releaseCallback.onSampleStreamReleased(this);
        }
    }
    
    @Override
    public int readData(final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b) {
        if (this.isPendingReset()) {
            return -3;
        }
        this.maybeNotifyPrimaryTrackFormatChanged();
        return this.primarySampleQueue.read(formatHolder, decoderInputBuffer, b, this.loadingFinished, this.decodeOnlyUntilPositionUs);
    }
    
    @Override
    public void reevaluateBuffer(long endTimeUs) {
        if (!this.loader.isLoading()) {
            if (!this.isPendingReset()) {
                final int size = this.mediaChunks.size();
                int i;
                if (size <= (i = this.chunkSource.getPreferredQueueSize(endTimeUs, this.readOnlyMediaChunks))) {
                    return;
                }
                while (true) {
                    while (i < size) {
                        if (!this.haveReadFromMediaChunk(i)) {
                            if (i == size) {
                                return;
                            }
                            endTimeUs = this.getLastMediaChunk().endTimeUs;
                            final BaseMediaChunk discardUpstreamMediaChunksFromIndex = this.discardUpstreamMediaChunksFromIndex(i);
                            if (this.mediaChunks.isEmpty()) {
                                this.pendingResetPositionUs = this.lastSeekPositionUs;
                            }
                            this.loadingFinished = false;
                            this.eventDispatcher.upstreamDiscarded(this.primaryTrackType, discardUpstreamMediaChunksFromIndex.startTimeUs, endTimeUs);
                            return;
                        }
                        else {
                            ++i;
                        }
                    }
                    i = size;
                    continue;
                }
            }
        }
    }
    
    public void release() {
        this.release(null);
    }
    
    public void release(final ReleaseCallback<T> releaseCallback) {
        this.releaseCallback = releaseCallback;
        this.primarySampleQueue.discardToEnd();
        final SampleQueue[] embeddedSampleQueues = this.embeddedSampleQueues;
        for (int length = embeddedSampleQueues.length, i = 0; i < length; ++i) {
            embeddedSampleQueues[i].discardToEnd();
        }
        this.loader.release((Loader.ReleaseCallback)this);
    }
    
    public void seekToUs(final long pendingResetPositionUs) {
        this.lastSeekPositionUs = pendingResetPositionUs;
        if (this.isPendingReset()) {
            this.pendingResetPositionUs = pendingResetPositionUs;
            return;
        }
        final BaseMediaChunk baseMediaChunk = null;
        final int n = 0;
        int index = 0;
        BaseMediaChunk baseMediaChunk2;
        while (true) {
            baseMediaChunk2 = baseMediaChunk;
            if (index >= this.mediaChunks.size()) {
                break;
            }
            baseMediaChunk2 = this.mediaChunks.get(index);
            final long startTimeUs = baseMediaChunk2.startTimeUs;
            if (startTimeUs == pendingResetPositionUs && baseMediaChunk2.clippedStartTimeUs == -9223372036854775807L) {
                break;
            }
            if (startTimeUs > pendingResetPositionUs) {
                baseMediaChunk2 = baseMediaChunk;
                break;
            }
            ++index;
        }
        this.primarySampleQueue.rewind();
        boolean setReadPosition;
        if (baseMediaChunk2 != null) {
            setReadPosition = this.primarySampleQueue.setReadPosition(baseMediaChunk2.getFirstSampleIndex(0));
            this.decodeOnlyUntilPositionUs = 0L;
        }
        else {
            setReadPosition = (this.primarySampleQueue.advanceTo(pendingResetPositionUs, true, pendingResetPositionUs < this.getNextLoadPositionUs()) != -1);
            this.decodeOnlyUntilPositionUs = this.lastSeekPositionUs;
        }
        if (setReadPosition) {
            this.nextNotifyPrimaryFormatMediaChunkIndex = this.primarySampleIndexToMediaChunkIndex(this.primarySampleQueue.getReadIndex(), 0);
            for (final SampleQueue sampleQueue : this.embeddedSampleQueues) {
                sampleQueue.rewind();
                sampleQueue.advanceTo(pendingResetPositionUs, true, false);
            }
        }
        else {
            this.pendingResetPositionUs = pendingResetPositionUs;
            this.loadingFinished = false;
            this.mediaChunks.clear();
            this.nextNotifyPrimaryFormatMediaChunkIndex = 0;
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
            }
            else {
                this.primarySampleQueue.reset();
                final SampleQueue[] embeddedSampleQueues2 = this.embeddedSampleQueues;
                for (int length2 = embeddedSampleQueues2.length, j = n; j < length2; ++j) {
                    embeddedSampleQueues2[j].reset();
                }
            }
        }
    }
    
    public EmbeddedSampleStream selectEmbeddedTrack(final long n, final int n2) {
        for (int i = 0; i < this.embeddedSampleQueues.length; ++i) {
            if (this.embeddedTrackTypes[i] == n2) {
                Assertions.checkState(this.embeddedTracksSelected[i] ^ true);
                this.embeddedTracksSelected[i] = true;
                this.embeddedSampleQueues[i].rewind();
                this.embeddedSampleQueues[i].advanceTo(n, true, true);
                return new EmbeddedSampleStream(this, this.embeddedSampleQueues[i], i);
            }
        }
        throw new IllegalStateException();
    }
    
    @Override
    public int skipData(final long n) {
        final boolean pendingReset = this.isPendingReset();
        final int n2 = 0;
        if (pendingReset) {
            return 0;
        }
        int n3;
        if (this.loadingFinished && n > this.primarySampleQueue.getLargestQueuedTimestampUs()) {
            n3 = this.primarySampleQueue.advanceToEnd();
        }
        else {
            n3 = this.primarySampleQueue.advanceTo(n, true, true);
            if (n3 == -1) {
                n3 = n2;
            }
        }
        this.maybeNotifyPrimaryTrackFormatChanged();
        return n3;
    }
    
    public final class EmbeddedSampleStream implements SampleStream
    {
        private final int index;
        private boolean notifiedDownstreamFormat;
        public final ChunkSampleStream<T> parent;
        private final SampleQueue sampleQueue;
        
        public EmbeddedSampleStream(final ChunkSampleStream<T> parent, final SampleQueue sampleQueue, final int index) {
            this.parent = parent;
            this.sampleQueue = sampleQueue;
            this.index = index;
        }
        
        private void maybeNotifyDownstreamFormat() {
            if (!this.notifiedDownstreamFormat) {
                ChunkSampleStream.this.eventDispatcher.downstreamFormatChanged(ChunkSampleStream.this.embeddedTrackTypes[this.index], ChunkSampleStream.this.embeddedTrackFormats[this.index], 0, null, ChunkSampleStream.this.lastSeekPositionUs);
                this.notifiedDownstreamFormat = true;
            }
        }
        
        @Override
        public boolean isReady() {
            final ChunkSampleStream this$0 = ChunkSampleStream.this;
            return this$0.loadingFinished || (!this$0.isPendingReset() && this.sampleQueue.hasNextSample());
        }
        
        @Override
        public void maybeThrowError() throws IOException {
        }
        
        @Override
        public int readData(final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b) {
            if (ChunkSampleStream.this.isPendingReset()) {
                return -3;
            }
            this.maybeNotifyDownstreamFormat();
            final SampleQueue sampleQueue = this.sampleQueue;
            final ChunkSampleStream this$0 = ChunkSampleStream.this;
            return sampleQueue.read(formatHolder, decoderInputBuffer, b, this$0.loadingFinished, this$0.decodeOnlyUntilPositionUs);
        }
        
        public void release() {
            Assertions.checkState(ChunkSampleStream.this.embeddedTracksSelected[this.index]);
            ChunkSampleStream.this.embeddedTracksSelected[this.index] = false;
        }
        
        @Override
        public int skipData(final long n) {
            final boolean pendingReset = ChunkSampleStream.this.isPendingReset();
            int advanceToEnd = 0;
            if (pendingReset) {
                return 0;
            }
            this.maybeNotifyDownstreamFormat();
            if (ChunkSampleStream.this.loadingFinished && n > this.sampleQueue.getLargestQueuedTimestampUs()) {
                advanceToEnd = this.sampleQueue.advanceToEnd();
            }
            else {
                final int advanceTo = this.sampleQueue.advanceTo(n, true, true);
                if (advanceTo != -1) {
                    advanceToEnd = advanceTo;
                }
            }
            return advanceToEnd;
        }
    }
    
    public interface ReleaseCallback<T extends ChunkSource>
    {
        void onSampleStreamReleased(final ChunkSampleStream<T> p0);
    }
}
