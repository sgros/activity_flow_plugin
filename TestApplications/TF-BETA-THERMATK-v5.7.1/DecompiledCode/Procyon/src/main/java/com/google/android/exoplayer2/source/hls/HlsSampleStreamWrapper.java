// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.FormatHolder;
import java.io.IOException;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.SampleStream;
import java.util.Iterator;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.util.MimeTypes;
import java.util.Collections;
import java.util.List;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import java.util.ArrayList;
import android.os.Handler;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.source.SampleQueue;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.upstream.Loader;

final class HlsSampleStreamWrapper implements Loader.Callback<Chunk>, ReleaseCallback, SequenceableLoader, ExtractorOutput, UpstreamFormatChangedListener
{
    private final Allocator allocator;
    private int audioSampleQueueIndex;
    private boolean audioSampleQueueMappingDone;
    private final Callback callback;
    private final HlsChunkSource chunkSource;
    private int chunkUid;
    private Format downstreamTrackFormat;
    private int enabledTrackGroupCount;
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
    private final Handler handler;
    private boolean haveAudioVideoSampleQueues;
    private final ArrayList<HlsSampleStream> hlsSampleStreams;
    private long lastSeekPositionUs;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final Loader loader;
    private boolean loadingFinished;
    private final Runnable maybeFinishPrepareRunnable;
    private final ArrayList<HlsMediaChunk> mediaChunks;
    private final Format muxedAudioFormat;
    private final HlsChunkSource.HlsChunkHolder nextChunkHolder;
    private final Runnable onTracksEndedRunnable;
    private TrackGroupArray optionalTrackGroups;
    private long pendingResetPositionUs;
    private boolean pendingResetUpstreamFormats;
    private boolean prepared;
    private int primarySampleQueueIndex;
    private int primarySampleQueueType;
    private int primaryTrackGroupIndex;
    private final List<HlsMediaChunk> readOnlyMediaChunks;
    private boolean released;
    private long sampleOffsetUs;
    private boolean[] sampleQueueIsAudioVideoFlags;
    private int[] sampleQueueTrackIds;
    private SampleQueue[] sampleQueues;
    private boolean sampleQueuesBuilt;
    private boolean[] sampleQueuesEnabledStates;
    private boolean seenFirstTrackSelection;
    private int[] trackGroupToSampleQueueIndex;
    private TrackGroupArray trackGroups;
    private final int trackType;
    private boolean tracksEnded;
    private Format upstreamTrackFormat;
    private int videoSampleQueueIndex;
    private boolean videoSampleQueueMappingDone;
    
    public HlsSampleStreamWrapper(final int trackType, final Callback callback, final HlsChunkSource chunkSource, final Allocator allocator, final long n, final Format muxedAudioFormat, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final MediaSourceEventListener.EventDispatcher eventDispatcher) {
        this.trackType = trackType;
        this.callback = callback;
        this.chunkSource = chunkSource;
        this.allocator = allocator;
        this.muxedAudioFormat = muxedAudioFormat;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.eventDispatcher = eventDispatcher;
        this.loader = new Loader("Loader:HlsSampleStreamWrapper");
        this.nextChunkHolder = new HlsChunkSource.HlsChunkHolder();
        this.sampleQueueTrackIds = new int[0];
        this.audioSampleQueueIndex = -1;
        this.videoSampleQueueIndex = -1;
        this.sampleQueues = new SampleQueue[0];
        this.sampleQueueIsAudioVideoFlags = new boolean[0];
        this.sampleQueuesEnabledStates = new boolean[0];
        this.mediaChunks = new ArrayList<HlsMediaChunk>();
        this.readOnlyMediaChunks = Collections.unmodifiableList((List<? extends HlsMediaChunk>)this.mediaChunks);
        this.hlsSampleStreams = new ArrayList<HlsSampleStream>();
        this.maybeFinishPrepareRunnable = new _$$Lambda$HlsSampleStreamWrapper$8JyeEr0irIOShv9LlAxAmgzl5vY(this);
        this.onTracksEndedRunnable = new _$$Lambda$HlsSampleStreamWrapper$afhkI3tagC__MAOTh7FzBWzQsno(this);
        this.handler = new Handler();
        this.lastSeekPositionUs = n;
        this.pendingResetPositionUs = n;
    }
    
    private void buildTracksFromSampleStreams() {
        final int length = this.sampleQueues.length;
        boolean b = false;
        int n = 0;
        int n2 = 6;
        int n3 = -1;
        while (true) {
            int n4 = 2;
            if (n >= length) {
                break;
            }
            final String sampleMimeType = this.sampleQueues[n].getUpstreamFormat().sampleMimeType;
            if (!MimeTypes.isVideo(sampleMimeType)) {
                if (MimeTypes.isAudio(sampleMimeType)) {
                    n4 = 1;
                }
                else if (MimeTypes.isText(sampleMimeType)) {
                    n4 = 3;
                }
                else {
                    n4 = 6;
                }
            }
            int n5;
            int n6;
            if (getTrackTypeScore(n4) > getTrackTypeScore(n2)) {
                n5 = n;
                n6 = n4;
            }
            else {
                n6 = n2;
                n5 = n3;
                if (n4 == n2) {
                    n6 = n2;
                    if ((n5 = n3) != -1) {
                        n5 = -1;
                        n6 = n2;
                    }
                }
            }
            ++n;
            n2 = n6;
            n3 = n5;
        }
        final TrackGroup trackGroup = this.chunkSource.getTrackGroup();
        final int length2 = trackGroup.length;
        this.primaryTrackGroupIndex = -1;
        this.trackGroupToSampleQueueIndex = new int[length];
        for (int i = 0; i < length; ++i) {
            this.trackGroupToSampleQueueIndex[i] = i;
        }
        final TrackGroup[] array = new TrackGroup[length];
        for (int j = 0; j < length; ++j) {
            final Format upstreamFormat = this.sampleQueues[j].getUpstreamFormat();
            if (j == n3) {
                final Format[] array2 = new Format[length2];
                if (length2 == 1) {
                    array2[0] = upstreamFormat.copyWithManifestFormatInfo(trackGroup.getFormat(0));
                }
                else {
                    for (int k = 0; k < length2; ++k) {
                        array2[k] = deriveFormat(trackGroup.getFormat(k), upstreamFormat, true);
                    }
                }
                array[j] = new TrackGroup(array2);
                this.primaryTrackGroupIndex = j;
            }
            else {
                Format muxedAudioFormat;
                if (n2 == 2 && MimeTypes.isAudio(upstreamFormat.sampleMimeType)) {
                    muxedAudioFormat = this.muxedAudioFormat;
                }
                else {
                    muxedAudioFormat = null;
                }
                array[j] = new TrackGroup(new Format[] { deriveFormat(muxedAudioFormat, upstreamFormat, false) });
            }
        }
        this.trackGroups = new TrackGroupArray(array);
        if (this.optionalTrackGroups == null) {
            b = true;
        }
        Assertions.checkState(b);
        this.optionalTrackGroups = TrackGroupArray.EMPTY;
    }
    
    private static DummyTrackOutput createDummyTrackOutput(final int i, final int j) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Unmapped track with id ");
        sb.append(i);
        sb.append(" of type ");
        sb.append(j);
        Log.w("HlsSampleStreamWrapper", sb.toString());
        return new DummyTrackOutput();
    }
    
    private static Format deriveFormat(final Format format, final Format format2, final boolean b) {
        if (format == null) {
            return format2;
        }
        int bitrate;
        if (b) {
            bitrate = format.bitrate;
        }
        else {
            bitrate = -1;
        }
        final String codecsOfType = Util.getCodecsOfType(format.codecs, MimeTypes.getTrackType(format2.sampleMimeType));
        String s;
        if ((s = MimeTypes.getMediaMimeType(codecsOfType)) == null) {
            s = format2.sampleMimeType;
        }
        return format2.copyWithContainerInfo(format.id, format.label, s, codecsOfType, bitrate, format.width, format.height, format.selectionFlags, format.language);
    }
    
    private boolean finishedReadingChunk(final HlsMediaChunk hlsMediaChunk) {
        final int uid = hlsMediaChunk.uid;
        for (int length = this.sampleQueues.length, i = 0; i < length; ++i) {
            if (this.sampleQueuesEnabledStates[i] && this.sampleQueues[i].peekSourceId() == uid) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean formatsMatch(final Format format, final Format format2) {
        final String sampleMimeType = format.sampleMimeType;
        final String sampleMimeType2 = format2.sampleMimeType;
        final int trackType = MimeTypes.getTrackType(sampleMimeType);
        final boolean b = true;
        boolean b2 = true;
        if (trackType != 3) {
            if (trackType != MimeTypes.getTrackType(sampleMimeType2)) {
                b2 = false;
            }
            return b2;
        }
        return Util.areEqual(sampleMimeType, sampleMimeType2) && ((!"application/cea-608".equals(sampleMimeType) && !"application/cea-708".equals(sampleMimeType)) || (format.accessibilityChannel == format2.accessibilityChannel && b));
    }
    
    private HlsMediaChunk getLastMediaChunk() {
        final ArrayList<HlsMediaChunk> mediaChunks = this.mediaChunks;
        return mediaChunks.get(mediaChunks.size() - 1);
    }
    
    private static int getTrackTypeScore(final int n) {
        if (n == 1) {
            return 2;
        }
        if (n == 2) {
            return 3;
        }
        if (n != 3) {
            return 0;
        }
        return 1;
    }
    
    private static boolean isMediaChunk(final Chunk chunk) {
        return chunk instanceof HlsMediaChunk;
    }
    
    private boolean isPendingReset() {
        return this.pendingResetPositionUs != -9223372036854775807L;
    }
    
    private void mapSampleQueuesToMatchTrackGroups() {
        final int length = this.trackGroups.length;
        Arrays.fill(this.trackGroupToSampleQueueIndex = new int[length], -1);
        for (int i = 0; i < length; ++i) {
            int n = 0;
            while (true) {
                final SampleQueue[] sampleQueues = this.sampleQueues;
                if (n >= sampleQueues.length) {
                    break;
                }
                if (formatsMatch(sampleQueues[n].getUpstreamFormat(), this.trackGroups.get(i).getFormat(0))) {
                    this.trackGroupToSampleQueueIndex[i] = n;
                    break;
                }
                ++n;
            }
        }
        final Iterator<HlsSampleStream> iterator = this.hlsSampleStreams.iterator();
        while (iterator.hasNext()) {
            iterator.next().bindSampleQueue();
        }
    }
    
    private void maybeFinishPrepare() {
        if (!this.released && this.trackGroupToSampleQueueIndex == null) {
            if (this.sampleQueuesBuilt) {
                final SampleQueue[] sampleQueues = this.sampleQueues;
                for (int length = sampleQueues.length, i = 0; i < length; ++i) {
                    if (sampleQueues[i].getUpstreamFormat() == null) {
                        return;
                    }
                }
                if (this.trackGroups != null) {
                    this.mapSampleQueuesToMatchTrackGroups();
                }
                else {
                    this.buildTracksFromSampleStreams();
                    this.prepared = true;
                    this.callback.onPrepared();
                }
            }
        }
    }
    
    private void onTracksEnded() {
        this.sampleQueuesBuilt = true;
        this.maybeFinishPrepare();
    }
    
    private void resetSampleQueues() {
        final SampleQueue[] sampleQueues = this.sampleQueues;
        for (int length = sampleQueues.length, i = 0; i < length; ++i) {
            sampleQueues[i].reset(this.pendingResetUpstreamFormats);
        }
        this.pendingResetUpstreamFormats = false;
    }
    
    private boolean seekInsideBufferUs(final long n) {
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
            if (!b && (this.sampleQueueIsAudioVideoFlags[n2] || !this.haveAudioVideoSampleQueues)) {
                return false;
            }
            ++n2;
        }
    }
    
    private void updateSampleStreams(final SampleStream[] array) {
        this.hlsSampleStreams.clear();
        for (final SampleStream sampleStream : array) {
            if (sampleStream != null) {
                this.hlsSampleStreams.add((HlsSampleStream)sampleStream);
            }
        }
    }
    
    public int bindSampleQueueToSampleStream(int n) {
        final int n2 = this.trackGroupToSampleQueueIndex[n];
        final int n3 = -2;
        if (n2 == -1) {
            if (this.optionalTrackGroups.indexOf(this.trackGroups.get(n)) == -1) {
                n = n3;
            }
            else {
                n = -3;
            }
            return n;
        }
        final boolean[] sampleQueuesEnabledStates = this.sampleQueuesEnabledStates;
        if (sampleQueuesEnabledStates[n2]) {
            return -2;
        }
        sampleQueuesEnabledStates[n2] = true;
        return n2;
    }
    
    @Override
    public boolean continueLoading(long startLoading) {
        if (this.loadingFinished || this.loader.isLoading()) {
            return false;
        }
        List<HlsMediaChunk> list;
        long n;
        if (this.isPendingReset()) {
            list = Collections.emptyList();
            n = this.pendingResetPositionUs;
        }
        else {
            list = this.readOnlyMediaChunks;
            final HlsMediaChunk lastMediaChunk = this.getLastMediaChunk();
            if (lastMediaChunk.isLoadCompleted()) {
                n = lastMediaChunk.endTimeUs;
            }
            else {
                n = Math.max(this.lastSeekPositionUs, lastMediaChunk.startTimeUs);
            }
        }
        this.chunkSource.getNextChunk(startLoading, n, list, this.nextChunkHolder);
        final HlsChunkSource.HlsChunkHolder nextChunkHolder = this.nextChunkHolder;
        final boolean endOfStream = nextChunkHolder.endOfStream;
        final Chunk chunk = nextChunkHolder.chunk;
        final HlsMasterPlaylist.HlsUrl playlist = nextChunkHolder.playlist;
        nextChunkHolder.clear();
        if (endOfStream) {
            this.pendingResetPositionUs = -9223372036854775807L;
            return this.loadingFinished = true;
        }
        if (chunk == null) {
            if (playlist != null) {
                this.callback.onPlaylistRefreshRequired(playlist);
            }
            return false;
        }
        if (isMediaChunk(chunk)) {
            this.pendingResetPositionUs = -9223372036854775807L;
            final HlsMediaChunk e = (HlsMediaChunk)chunk;
            e.init(this);
            this.mediaChunks.add(e);
            this.upstreamTrackFormat = e.trackFormat;
        }
        startLoading = this.loader.startLoading((HlsMediaChunk)chunk, (Loader.Callback<HlsMediaChunk>)this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(chunk.type));
        this.eventDispatcher.loadStarted(chunk.dataSpec, chunk.type, this.trackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, startLoading);
        return true;
    }
    
    public void continuePreparing() {
        if (!this.prepared) {
            this.continueLoading(this.lastSeekPositionUs);
        }
    }
    
    public void discardBuffer(final long n, final boolean b) {
        if (this.sampleQueuesBuilt) {
            if (!this.isPendingReset()) {
                for (int length = this.sampleQueues.length, i = 0; i < length; ++i) {
                    this.sampleQueues[i].discardTo(n, b, this.sampleQueuesEnabledStates[i]);
                }
            }
        }
    }
    
    @Override
    public void endTracks() {
        this.tracksEnded = true;
        this.handler.post(this.onTracksEndedRunnable);
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
        HlsMediaChunk lastMediaChunk = this.getLastMediaChunk();
        if (!lastMediaChunk.isLoadCompleted()) {
            if (this.mediaChunks.size() > 1) {
                final ArrayList<HlsMediaChunk> mediaChunks = this.mediaChunks;
                lastMediaChunk = mediaChunks.get(mediaChunks.size() - 2);
            }
            else {
                lastMediaChunk = null;
            }
        }
        long a = lastSeekPositionUs;
        if (lastMediaChunk != null) {
            a = Math.max(lastSeekPositionUs, lastMediaChunk.endTimeUs);
        }
        long n = a;
        if (this.sampleQueuesBuilt) {
            final SampleQueue[] sampleQueues = this.sampleQueues;
            final int length = sampleQueues.length;
            int n2 = 0;
            while (true) {
                n = a;
                if (n2 >= length) {
                    break;
                }
                a = Math.max(a, sampleQueues[n2].getLargestQueuedTimestampUs());
                ++n2;
            }
        }
        return n;
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
    
    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }
    
    public void init(int i, final boolean b, final boolean b2) {
        final int n = 0;
        if (!b2) {
            this.audioSampleQueueMappingDone = false;
            this.videoSampleQueueMappingDone = false;
        }
        this.chunkUid = i;
        final SampleQueue[] sampleQueues = this.sampleQueues;
        for (int length = sampleQueues.length, j = 0; j < length; ++j) {
            sampleQueues[j].sourceId(i);
        }
        if (b) {
            final SampleQueue[] sampleQueues2 = this.sampleQueues;
            int length2;
            for (length2 = sampleQueues2.length, i = n; i < length2; ++i) {
                sampleQueues2[i].splice();
            }
        }
    }
    
    public boolean isReady(final int n) {
        return this.loadingFinished || (!this.isPendingReset() && this.sampleQueues[n].hasNextSample());
    }
    
    public void maybeThrowError() throws IOException {
        this.loader.maybeThrowError();
        this.chunkSource.maybeThrowError();
    }
    
    public void maybeThrowPrepareError() throws IOException {
        this.maybeThrowError();
    }
    
    public void onLoadCanceled(final Chunk chunk, final long n, final long n2, final boolean b) {
        this.eventDispatcher.loadCanceled(chunk.dataSpec, chunk.getUri(), chunk.getResponseHeaders(), chunk.type, this.trackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, n, n2, chunk.bytesLoaded());
        if (!b) {
            this.resetSampleQueues();
            if (this.enabledTrackGroupCount > 0) {
                ((SequenceableLoader.Callback<HlsSampleStreamWrapper>)this.callback).onContinueLoadingRequested(this);
            }
        }
    }
    
    public void onLoadCompleted(final Chunk chunk, final long n, final long n2) {
        this.chunkSource.onChunkLoadCompleted(chunk);
        this.eventDispatcher.loadCompleted(chunk.dataSpec, chunk.getUri(), chunk.getResponseHeaders(), chunk.type, this.trackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, n, n2, chunk.bytesLoaded());
        if (!this.prepared) {
            this.continueLoading(this.lastSeekPositionUs);
        }
        else {
            ((SequenceableLoader.Callback<HlsSampleStreamWrapper>)this.callback).onContinueLoadingRequested(this);
        }
    }
    
    public LoadErrorAction onLoadError(final Chunk chunk, final long n, final long n2, final IOException ex, final int n3) {
        final long bytesLoaded = chunk.bytesLoaded();
        final boolean mediaChunk = isMediaChunk(chunk);
        final long blacklistDurationMs = this.loadErrorHandlingPolicy.getBlacklistDurationMsFor(chunk.type, n2, ex, n3);
        boolean b = false;
        final boolean b2 = blacklistDurationMs != -9223372036854775807L && this.chunkSource.maybeBlacklistTrack(chunk, blacklistDurationMs);
        Loader.LoadErrorAction loadErrorAction;
        if (b2) {
            if (mediaChunk && bytesLoaded == 0L) {
                final ArrayList<HlsMediaChunk> mediaChunks = this.mediaChunks;
                if (mediaChunks.remove(mediaChunks.size() - 1) == chunk) {
                    b = true;
                }
                Assertions.checkState(b);
                if (this.mediaChunks.isEmpty()) {
                    this.pendingResetPositionUs = this.lastSeekPositionUs;
                }
            }
            loadErrorAction = Loader.DONT_RETRY;
        }
        else {
            final long retryDelayMs = this.loadErrorHandlingPolicy.getRetryDelayMsFor(chunk.type, n2, ex, n3);
            if (retryDelayMs != -9223372036854775807L) {
                loadErrorAction = Loader.createRetryAction(false, retryDelayMs);
            }
            else {
                loadErrorAction = Loader.DONT_RETRY_FATAL;
            }
        }
        this.eventDispatcher.loadError(chunk.dataSpec, chunk.getUri(), chunk.getResponseHeaders(), chunk.type, this.trackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, n, n2, bytesLoaded, ex, loadErrorAction.isRetry() ^ true);
        if (b2) {
            if (!this.prepared) {
                this.continueLoading(this.lastSeekPositionUs);
            }
            else {
                ((SequenceableLoader.Callback<HlsSampleStreamWrapper>)this.callback).onContinueLoadingRequested(this);
            }
        }
        return loadErrorAction;
    }
    
    @Override
    public void onLoaderReleased() {
        this.resetSampleQueues();
    }
    
    public boolean onPlaylistError(final HlsMasterPlaylist.HlsUrl hlsUrl, final long n) {
        return this.chunkSource.onPlaylistError(hlsUrl, n);
    }
    
    @Override
    public void onUpstreamFormatChanged(final Format format) {
        this.handler.post(this.maybeFinishPrepareRunnable);
    }
    
    public void prepareWithMasterPlaylistInfo(final TrackGroupArray trackGroups, final int primaryTrackGroupIndex, final TrackGroupArray optionalTrackGroups) {
        this.prepared = true;
        this.trackGroups = trackGroups;
        this.optionalTrackGroups = optionalTrackGroups;
        this.primaryTrackGroupIndex = primaryTrackGroupIndex;
        this.callback.onPrepared();
    }
    
    public int readData(int n, final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b) {
        if (this.isPendingReset()) {
            return -3;
        }
        final boolean empty = this.mediaChunks.isEmpty();
        final int n2 = 0;
        if (!empty) {
            int index;
            for (index = 0; index < this.mediaChunks.size() - 1 && this.finishedReadingChunk(this.mediaChunks.get(index)); ++index) {}
            Util.removeRange(this.mediaChunks, 0, index);
            final HlsMediaChunk hlsMediaChunk = this.mediaChunks.get(0);
            final Format trackFormat = hlsMediaChunk.trackFormat;
            if (!trackFormat.equals(this.downstreamTrackFormat)) {
                this.eventDispatcher.downstreamFormatChanged(this.trackType, trackFormat, hlsMediaChunk.trackSelectionReason, hlsMediaChunk.trackSelectionData, hlsMediaChunk.startTimeUs);
            }
            this.downstreamTrackFormat = trackFormat;
        }
        final int read = this.sampleQueues[n].read(formatHolder, decoderInputBuffer, b, this.loadingFinished, this.lastSeekPositionUs);
        if (read == -5 && n == this.primarySampleQueueIndex) {
            int peekSourceId;
            for (peekSourceId = this.sampleQueues[n].peekSourceId(), n = n2; n < this.mediaChunks.size() && this.mediaChunks.get(n).uid != peekSourceId; ++n) {}
            Format format;
            if (n < this.mediaChunks.size()) {
                format = this.mediaChunks.get(n).trackFormat;
            }
            else {
                format = this.upstreamTrackFormat;
            }
            formatHolder.format = formatHolder.format.copyWithManifestFormatInfo(format);
        }
        return read;
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
        this.released = true;
        this.hlsSampleStreams.clear();
    }
    
    @Override
    public void seekMap(final SeekMap seekMap) {
    }
    
    public boolean seekToUs(final long pendingResetPositionUs, final boolean b) {
        this.lastSeekPositionUs = pendingResetPositionUs;
        if (this.isPendingReset()) {
            this.pendingResetPositionUs = pendingResetPositionUs;
            return true;
        }
        if (this.sampleQueuesBuilt && !b && this.seekInsideBufferUs(pendingResetPositionUs)) {
            return false;
        }
        this.pendingResetPositionUs = pendingResetPositionUs;
        this.loadingFinished = false;
        this.mediaChunks.clear();
        if (this.loader.isLoading()) {
            this.loader.cancelLoading();
        }
        else {
            this.resetSampleQueues();
        }
        return true;
    }
    
    public boolean selectTracks(final TrackSelection[] array, final boolean[] array2, final SampleStream[] array3, final boolean[] array4, final long n, boolean b) {
        Assertions.checkState(this.prepared);
        final int enabledTrackGroupCount = this.enabledTrackGroupCount;
        final int n2 = 0;
        final int n3 = 0;
        for (int i = 0; i < array.length; ++i) {
            if (array3[i] != null && (array[i] == null || !array2[i])) {
                --this.enabledTrackGroupCount;
                ((HlsSampleStream)array3[i]).unbindSampleQueue();
                array3[i] = null;
            }
        }
        boolean b2 = b || (this.seenFirstTrackSelection ? (enabledTrackGroupCount == 0) : (n != this.lastSeekPositionUs));
        TrackSelection trackSelection2;
        final TrackSelection trackSelection = trackSelection2 = this.chunkSource.getTrackSelection();
        TrackSelection trackSelection3;
        boolean b3;
        for (int j = 0; j < array.length; ++j, trackSelection2 = trackSelection3, b2 = b3) {
            trackSelection3 = trackSelection2;
            b3 = b2;
            if (array3[j] == null) {
                trackSelection3 = trackSelection2;
                b3 = b2;
                if (array[j] != null) {
                    ++this.enabledTrackGroupCount;
                    final TrackSelection trackSelection4 = array[j];
                    final int index = this.trackGroups.indexOf(trackSelection4.getTrackGroup());
                    if (index == this.primaryTrackGroupIndex) {
                        this.chunkSource.selectTracks(trackSelection4);
                        trackSelection2 = trackSelection4;
                    }
                    array3[j] = new HlsSampleStream(this, index);
                    array4[j] = true;
                    if (this.trackGroupToSampleQueueIndex != null) {
                        ((HlsSampleStream)array3[j]).bindSampleQueue();
                    }
                    trackSelection3 = trackSelection2;
                    b3 = b2;
                    if (this.sampleQueuesBuilt) {
                        trackSelection3 = trackSelection2;
                        if (!(b3 = b2)) {
                            final SampleQueue sampleQueue = this.sampleQueues[this.trackGroupToSampleQueueIndex[index]];
                            sampleQueue.rewind();
                            if (sampleQueue.advanceTo(n, true, true) == -1 && sampleQueue.getReadIndex() != 0) {
                                b3 = true;
                                trackSelection3 = trackSelection2;
                            }
                            else {
                                b3 = false;
                                trackSelection3 = trackSelection2;
                            }
                        }
                    }
                }
            }
        }
        boolean b4;
        if (this.enabledTrackGroupCount == 0) {
            this.chunkSource.reset();
            this.downstreamTrackFormat = null;
            this.mediaChunks.clear();
            if (this.loader.isLoading()) {
                if (this.sampleQueuesBuilt) {
                    final SampleQueue[] sampleQueues = this.sampleQueues;
                    for (int length = sampleQueues.length, k = n3; k < length; ++k) {
                        sampleQueues[k].discardToEnd();
                    }
                }
                this.loader.cancelLoading();
                b4 = b2;
            }
            else {
                this.resetSampleQueues();
                b4 = b2;
            }
        }
        else {
            if (!this.mediaChunks.isEmpty() && !Util.areEqual(trackSelection2, trackSelection)) {
                boolean b5 = false;
                Label_0575: {
                    if (!this.seenFirstTrackSelection) {
                        long n4 = 0L;
                        if (n < 0L) {
                            n4 = -n;
                        }
                        final HlsMediaChunk lastMediaChunk = this.getLastMediaChunk();
                        trackSelection2.updateSelectedTrack(n, n4, -9223372036854775807L, this.readOnlyMediaChunks, this.chunkSource.createMediaChunkIterators(lastMediaChunk, n));
                        if (trackSelection2.getSelectedIndexInTrackGroup() == this.chunkSource.getTrackGroup().indexOf(lastMediaChunk.trackFormat)) {
                            b5 = false;
                            break Label_0575;
                        }
                    }
                    b5 = true;
                }
                if (b5) {
                    this.pendingResetUpstreamFormats = true;
                    b = true;
                    b2 = true;
                }
            }
            if (b4 = b2) {
                this.seekToUs(n, b);
                int n5 = n2;
                while (true) {
                    b4 = b2;
                    if (n5 >= array3.length) {
                        break;
                    }
                    if (array3[n5] != null) {
                        array4[n5] = true;
                    }
                    ++n5;
                }
            }
        }
        this.updateSampleStreams(array3);
        this.seenFirstTrackSelection = true;
        return b4;
    }
    
    public void setIsTimestampMaster(final boolean isTimestampMaster) {
        this.chunkSource.setIsTimestampMaster(isTimestampMaster);
    }
    
    public void setSampleOffsetUs(final long n) {
        this.sampleOffsetUs = n;
        final SampleQueue[] sampleQueues = this.sampleQueues;
        for (int length = sampleQueues.length, i = 0; i < length; ++i) {
            sampleQueues[i].setSampleOffsetUs(n);
        }
    }
    
    public int skipData(int advanceTo, final long n) {
        if (this.isPendingReset()) {
            return 0;
        }
        final SampleQueue sampleQueue = this.sampleQueues[advanceTo];
        if (this.loadingFinished && n > sampleQueue.getLargestQueuedTimestampUs()) {
            return sampleQueue.advanceToEnd();
        }
        if ((advanceTo = sampleQueue.advanceTo(n, true, true)) == -1) {
            advanceTo = 0;
        }
        return advanceTo;
    }
    
    @Override
    public TrackOutput track(final int n, final int primarySampleQueueType) {
        final SampleQueue[] sampleQueues = this.sampleQueues;
        final int length = sampleQueues.length;
        boolean b = false;
        if (primarySampleQueueType == 1) {
            final int audioSampleQueueIndex = this.audioSampleQueueIndex;
            if (audioSampleQueueIndex != -1) {
                if (this.audioSampleQueueMappingDone) {
                    TrackOutput dummyTrackOutput;
                    if (this.sampleQueueTrackIds[audioSampleQueueIndex] == n) {
                        dummyTrackOutput = sampleQueues[audioSampleQueueIndex];
                    }
                    else {
                        dummyTrackOutput = createDummyTrackOutput(n, primarySampleQueueType);
                    }
                    return dummyTrackOutput;
                }
                this.audioSampleQueueMappingDone = true;
                this.sampleQueueTrackIds[audioSampleQueueIndex] = n;
                return sampleQueues[audioSampleQueueIndex];
            }
            else if (this.tracksEnded) {
                return createDummyTrackOutput(n, primarySampleQueueType);
            }
        }
        else if (primarySampleQueueType == 2) {
            final int videoSampleQueueIndex = this.videoSampleQueueIndex;
            if (videoSampleQueueIndex != -1) {
                if (this.videoSampleQueueMappingDone) {
                    TrackOutput dummyTrackOutput2;
                    if (this.sampleQueueTrackIds[videoSampleQueueIndex] == n) {
                        dummyTrackOutput2 = sampleQueues[videoSampleQueueIndex];
                    }
                    else {
                        dummyTrackOutput2 = createDummyTrackOutput(n, primarySampleQueueType);
                    }
                    return dummyTrackOutput2;
                }
                this.videoSampleQueueMappingDone = true;
                this.sampleQueueTrackIds[videoSampleQueueIndex] = n;
                return sampleQueues[videoSampleQueueIndex];
            }
            else if (this.tracksEnded) {
                return createDummyTrackOutput(n, primarySampleQueueType);
            }
        }
        else {
            for (int i = 0; i < length; ++i) {
                if (this.sampleQueueTrackIds[i] == n) {
                    return this.sampleQueues[i];
                }
            }
            if (this.tracksEnded) {
                return createDummyTrackOutput(n, primarySampleQueueType);
            }
        }
        final PrivTimestampStrippingSampleQueue privTimestampStrippingSampleQueue = new PrivTimestampStrippingSampleQueue(this.allocator);
        privTimestampStrippingSampleQueue.setSampleOffsetUs(this.sampleOffsetUs);
        privTimestampStrippingSampleQueue.sourceId(this.chunkUid);
        privTimestampStrippingSampleQueue.setUpstreamFormatChangeListener((SampleQueue.UpstreamFormatChangedListener)this);
        final int[] sampleQueueTrackIds = this.sampleQueueTrackIds;
        final int n2 = length + 1;
        (this.sampleQueueTrackIds = Arrays.copyOf(sampleQueueTrackIds, n2))[length] = n;
        (this.sampleQueues = Arrays.copyOf(this.sampleQueues, n2))[length] = privTimestampStrippingSampleQueue;
        this.sampleQueueIsAudioVideoFlags = Arrays.copyOf(this.sampleQueueIsAudioVideoFlags, n2);
        final boolean[] sampleQueueIsAudioVideoFlags = this.sampleQueueIsAudioVideoFlags;
        if (primarySampleQueueType == 1 || primarySampleQueueType == 2) {
            b = true;
        }
        sampleQueueIsAudioVideoFlags[length] = b;
        this.haveAudioVideoSampleQueues |= this.sampleQueueIsAudioVideoFlags[length];
        if (primarySampleQueueType == 1) {
            this.audioSampleQueueMappingDone = true;
            this.audioSampleQueueIndex = length;
        }
        else if (primarySampleQueueType == 2) {
            this.videoSampleQueueMappingDone = true;
            this.videoSampleQueueIndex = length;
        }
        if (getTrackTypeScore(primarySampleQueueType) > getTrackTypeScore(this.primarySampleQueueType)) {
            this.primarySampleQueueIndex = length;
            this.primarySampleQueueType = primarySampleQueueType;
        }
        this.sampleQueuesEnabledStates = Arrays.copyOf(this.sampleQueuesEnabledStates, n2);
        return privTimestampStrippingSampleQueue;
    }
    
    public void unbindSampleQueue(int n) {
        n = this.trackGroupToSampleQueueIndex[n];
        Assertions.checkState(this.sampleQueuesEnabledStates[n]);
        this.sampleQueuesEnabledStates[n] = false;
    }
    
    public interface Callback extends SequenceableLoader.Callback<HlsSampleStreamWrapper>
    {
        void onPlaylistRefreshRequired(final HlsMasterPlaylist.HlsUrl p0);
        
        void onPrepared();
    }
    
    private static final class PrivTimestampStrippingSampleQueue extends SampleQueue
    {
        public PrivTimestampStrippingSampleQueue(final Allocator allocator) {
            super(allocator);
        }
        
        private Metadata getAdjustedMetadata(final Metadata metadata) {
            if (metadata == null) {
                return null;
            }
            final int length = metadata.length();
            final int n = 0;
            int i = 0;
            while (true) {
                while (i < length) {
                    final Metadata.Entry value = metadata.get(i);
                    if (value instanceof PrivFrame && "com.apple.streaming.transportStreamTimestamp".equals(((PrivFrame)value).owner)) {
                        final int n2 = i;
                        if (n2 == -1) {
                            return metadata;
                        }
                        if (length == 1) {
                            return null;
                        }
                        final Metadata.Entry[] array = new Metadata.Entry[length - 1];
                        for (int j = n; j < length; ++j) {
                            if (j != n2) {
                                int n3;
                                if (j < n2) {
                                    n3 = j;
                                }
                                else {
                                    n3 = j - 1;
                                }
                                array[n3] = metadata.get(j);
                            }
                        }
                        return new Metadata(array);
                    }
                    else {
                        ++i;
                    }
                }
                final int n2 = -1;
                continue;
            }
        }
        
        @Override
        public void format(final Format format) {
            super.format(format.copyWithMetadata(this.getAdjustedMetadata(format.metadata)));
        }
    }
}
