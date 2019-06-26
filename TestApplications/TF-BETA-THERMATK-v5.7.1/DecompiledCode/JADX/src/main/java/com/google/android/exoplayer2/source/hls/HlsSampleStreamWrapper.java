package com.google.android.exoplayer2.source.hls;

import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.SampleQueue;
import com.google.android.exoplayer2.source.SampleQueue.UpstreamFormatChangedListener;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.hls.HlsChunkSource.HlsChunkHolder;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.Loader.LoadErrorAction;
import com.google.android.exoplayer2.upstream.Loader.ReleaseCallback;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

final class HlsSampleStreamWrapper implements com.google.android.exoplayer2.upstream.Loader.Callback<Chunk>, ReleaseCallback, SequenceableLoader, ExtractorOutput, UpstreamFormatChangedListener {
    private final Allocator allocator;
    private int audioSampleQueueIndex = -1;
    private boolean audioSampleQueueMappingDone;
    private final Callback callback;
    private final HlsChunkSource chunkSource;
    private int chunkUid;
    private Format downstreamTrackFormat;
    private int enabledTrackGroupCount;
    private final EventDispatcher eventDispatcher;
    private final Handler handler = new Handler();
    private boolean haveAudioVideoSampleQueues;
    private final ArrayList<HlsSampleStream> hlsSampleStreams = new ArrayList();
    private long lastSeekPositionUs;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final Loader loader = new Loader("Loader:HlsSampleStreamWrapper");
    private boolean loadingFinished;
    private final Runnable maybeFinishPrepareRunnable = new C0210-$$Lambda$HlsSampleStreamWrapper$8JyeEr0irIOShv9LlAxAmgzl5vY(this);
    private final ArrayList<HlsMediaChunk> mediaChunks = new ArrayList();
    private final Format muxedAudioFormat;
    private final HlsChunkHolder nextChunkHolder = new HlsChunkHolder();
    private final Runnable onTracksEndedRunnable = new C0211-$$Lambda$HlsSampleStreamWrapper$afhkI3tagC_-MAOTh7FzBWzQsno(this);
    private TrackGroupArray optionalTrackGroups;
    private long pendingResetPositionUs;
    private boolean pendingResetUpstreamFormats;
    private boolean prepared;
    private int primarySampleQueueIndex;
    private int primarySampleQueueType;
    private int primaryTrackGroupIndex;
    private final List<HlsMediaChunk> readOnlyMediaChunks = Collections.unmodifiableList(this.mediaChunks);
    private boolean released;
    private long sampleOffsetUs;
    private boolean[] sampleQueueIsAudioVideoFlags = new boolean[0];
    private int[] sampleQueueTrackIds = new int[0];
    private SampleQueue[] sampleQueues = new SampleQueue[0];
    private boolean sampleQueuesBuilt;
    private boolean[] sampleQueuesEnabledStates = new boolean[0];
    private boolean seenFirstTrackSelection;
    private int[] trackGroupToSampleQueueIndex;
    private TrackGroupArray trackGroups;
    private final int trackType;
    private boolean tracksEnded;
    private Format upstreamTrackFormat;
    private int videoSampleQueueIndex = -1;
    private boolean videoSampleQueueMappingDone;

    public interface Callback extends com.google.android.exoplayer2.source.SequenceableLoader.Callback<HlsSampleStreamWrapper> {
        void onPlaylistRefreshRequired(HlsUrl hlsUrl);

        void onPrepared();
    }

    private static final class PrivTimestampStrippingSampleQueue extends SampleQueue {
        public PrivTimestampStrippingSampleQueue(Allocator allocator) {
            super(allocator);
        }

        public void format(Format format) {
            super.format(format.copyWithMetadata(getAdjustedMetadata(format.metadata)));
        }

        private Metadata getAdjustedMetadata(Metadata metadata) {
            if (metadata == null) {
                return null;
            }
            int length = metadata.length();
            int i = 0;
            int i2 = 0;
            while (i2 < length) {
                Entry entry = metadata.get(i2);
                if (entry instanceof PrivFrame) {
                    if ("com.apple.streaming.transportStreamTimestamp".equals(((PrivFrame) entry).owner)) {
                        break;
                    }
                }
                i2++;
            }
            i2 = -1;
            if (i2 == -1) {
                return metadata;
            }
            if (length == 1) {
                return null;
            }
            Entry[] entryArr = new Entry[(length - 1)];
            while (i < length) {
                if (i != i2) {
                    entryArr[i < i2 ? i : i - 1] = metadata.get(i);
                }
                i++;
            }
            return new Metadata(entryArr);
        }
    }

    private static int getTrackTypeScore(int i) {
        return i != 1 ? i != 2 ? i != 3 ? 0 : 1 : 3 : 2;
    }

    public void reevaluateBuffer(long j) {
    }

    public void seekMap(SeekMap seekMap) {
    }

    public HlsSampleStreamWrapper(int i, Callback callback, HlsChunkSource hlsChunkSource, Allocator allocator, long j, Format format, LoadErrorHandlingPolicy loadErrorHandlingPolicy, EventDispatcher eventDispatcher) {
        this.trackType = i;
        this.callback = callback;
        this.chunkSource = hlsChunkSource;
        this.allocator = allocator;
        this.muxedAudioFormat = format;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.eventDispatcher = eventDispatcher;
        this.lastSeekPositionUs = j;
        this.pendingResetPositionUs = j;
    }

    public void continuePreparing() {
        if (!this.prepared) {
            continueLoading(this.lastSeekPositionUs);
        }
    }

    public void prepareWithMasterPlaylistInfo(TrackGroupArray trackGroupArray, int i, TrackGroupArray trackGroupArray2) {
        this.prepared = true;
        this.trackGroups = trackGroupArray;
        this.optionalTrackGroups = trackGroupArray2;
        this.primaryTrackGroupIndex = i;
        this.callback.onPrepared();
    }

    public void maybeThrowPrepareError() throws IOException {
        maybeThrowError();
    }

    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }

    public int bindSampleQueueToSampleStream(int i) {
        int i2 = this.trackGroupToSampleQueueIndex[i];
        int i3 = -2;
        if (i2 == -1) {
            if (this.optionalTrackGroups.indexOf(this.trackGroups.get(i)) != -1) {
                i3 = -3;
            }
            return i3;
        }
        boolean[] zArr = this.sampleQueuesEnabledStates;
        if (zArr[i2]) {
            return -2;
        }
        zArr[i2] = true;
        return i2;
    }

    public void unbindSampleQueue(int i) {
        i = this.trackGroupToSampleQueueIndex[i];
        Assertions.checkState(this.sampleQueuesEnabledStates[i]);
        this.sampleQueuesEnabledStates[i] = false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:67:0x0132  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x013c  */
    public boolean selectTracks(com.google.android.exoplayer2.trackselection.TrackSelection[] r20, boolean[] r21, com.google.android.exoplayer2.source.SampleStream[] r22, boolean[] r23, long r24, boolean r26) {
        /*
        r19 = this;
        r0 = r19;
        r1 = r20;
        r2 = r22;
        r12 = r24;
        r3 = r0.prepared;
        com.google.android.exoplayer2.util.Assertions.checkState(r3);
        r3 = r0.enabledTrackGroupCount;
        r14 = 0;
        r4 = 0;
    L_0x0011:
        r5 = r1.length;
        r6 = 0;
        r15 = 1;
        if (r4 >= r5) goto L_0x0033;
    L_0x0016:
        r5 = r2[r4];
        if (r5 == 0) goto L_0x0030;
    L_0x001a:
        r5 = r1[r4];
        if (r5 == 0) goto L_0x0022;
    L_0x001e:
        r5 = r21[r4];
        if (r5 != 0) goto L_0x0030;
    L_0x0022:
        r5 = r0.enabledTrackGroupCount;
        r5 = r5 - r15;
        r0.enabledTrackGroupCount = r5;
        r5 = r2[r4];
        r5 = (com.google.android.exoplayer2.source.hls.HlsSampleStream) r5;
        r5.unbindSampleQueue();
        r2[r4] = r6;
    L_0x0030:
        r4 = r4 + 1;
        goto L_0x0011;
    L_0x0033:
        if (r26 != 0) goto L_0x0045;
    L_0x0035:
        r4 = r0.seenFirstTrackSelection;
        if (r4 == 0) goto L_0x003c;
    L_0x0039:
        if (r3 != 0) goto L_0x0043;
    L_0x003b:
        goto L_0x0045;
    L_0x003c:
        r3 = r0.lastSeekPositionUs;
        r5 = (r12 > r3 ? 1 : (r12 == r3 ? 0 : -1));
        if (r5 == 0) goto L_0x0043;
    L_0x0042:
        goto L_0x0045;
    L_0x0043:
        r3 = 0;
        goto L_0x0046;
    L_0x0045:
        r3 = 1;
    L_0x0046:
        r4 = r0.chunkSource;
        r4 = r4.getTrackSelection();
        r16 = r3;
        r11 = r4;
        r3 = 0;
    L_0x0050:
        r5 = r1.length;
        if (r3 >= r5) goto L_0x00b0;
    L_0x0053:
        r5 = r2[r3];
        if (r5 != 0) goto L_0x00ad;
    L_0x0057:
        r5 = r1[r3];
        if (r5 == 0) goto L_0x00ad;
    L_0x005b:
        r5 = r0.enabledTrackGroupCount;
        r5 = r5 + r15;
        r0.enabledTrackGroupCount = r5;
        r5 = r1[r3];
        r7 = r0.trackGroups;
        r8 = r5.getTrackGroup();
        r7 = r7.indexOf(r8);
        r8 = r0.primaryTrackGroupIndex;
        if (r7 != r8) goto L_0x0076;
    L_0x0070:
        r8 = r0.chunkSource;
        r8.selectTracks(r5);
        r11 = r5;
    L_0x0076:
        r5 = new com.google.android.exoplayer2.source.hls.HlsSampleStream;
        r5.<init>(r0, r7);
        r2[r3] = r5;
        r23[r3] = r15;
        r5 = r0.trackGroupToSampleQueueIndex;
        if (r5 == 0) goto L_0x008a;
    L_0x0083:
        r5 = r2[r3];
        r5 = (com.google.android.exoplayer2.source.hls.HlsSampleStream) r5;
        r5.bindSampleQueue();
    L_0x008a:
        r5 = r0.sampleQueuesBuilt;
        if (r5 == 0) goto L_0x00ad;
    L_0x008e:
        if (r16 != 0) goto L_0x00ad;
    L_0x0090:
        r5 = r0.sampleQueues;
        r8 = r0.trackGroupToSampleQueueIndex;
        r7 = r8[r7];
        r5 = r5[r7];
        r5.rewind();
        r7 = r5.advanceTo(r12, r15, r15);
        r8 = -1;
        if (r7 != r8) goto L_0x00ab;
    L_0x00a2:
        r5 = r5.getReadIndex();
        if (r5 == 0) goto L_0x00ab;
    L_0x00a8:
        r16 = 1;
        goto L_0x00ad;
    L_0x00ab:
        r16 = 0;
    L_0x00ad:
        r3 = r3 + 1;
        goto L_0x0050;
    L_0x00b0:
        r1 = r0.enabledTrackGroupCount;
        if (r1 != 0) goto L_0x00e5;
    L_0x00b4:
        r1 = r0.chunkSource;
        r1.reset();
        r0.downstreamTrackFormat = r6;
        r1 = r0.mediaChunks;
        r1.clear();
        r1 = r0.loader;
        r1 = r1.isLoading();
        if (r1 == 0) goto L_0x00e0;
    L_0x00c8:
        r1 = r0.sampleQueuesBuilt;
        if (r1 == 0) goto L_0x00d9;
    L_0x00cc:
        r1 = r0.sampleQueues;
        r3 = r1.length;
    L_0x00cf:
        if (r14 >= r3) goto L_0x00d9;
    L_0x00d1:
        r4 = r1[r14];
        r4.discardToEnd();
        r14 = r14 + 1;
        goto L_0x00cf;
    L_0x00d9:
        r1 = r0.loader;
        r1.cancelLoading();
        goto L_0x014b;
    L_0x00e0:
        r19.resetSampleQueues();
        goto L_0x014b;
    L_0x00e5:
        r1 = r0.mediaChunks;
        r1 = r1.isEmpty();
        if (r1 != 0) goto L_0x0138;
    L_0x00ed:
        r1 = com.google.android.exoplayer2.util.Util.areEqual(r11, r4);
        if (r1 != 0) goto L_0x0138;
    L_0x00f3:
        r1 = r0.seenFirstTrackSelection;
        if (r1 != 0) goto L_0x012f;
    L_0x00f7:
        r3 = 0;
        r1 = (r12 > r3 ? 1 : (r12 == r3 ? 0 : -1));
        if (r1 >= 0) goto L_0x00fe;
    L_0x00fd:
        r3 = -r12;
    L_0x00fe:
        r6 = r3;
        r1 = r19.getLastMediaChunk();
        r3 = r0.chunkSource;
        r17 = r3.createMediaChunkIterators(r1, r12);
        r8 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r10 = r0.readOnlyMediaChunks;
        r3 = r11;
        r4 = r24;
        r18 = r11;
        r11 = r17;
        r3.updateSelectedTrack(r4, r6, r8, r10, r11);
        r3 = r0.chunkSource;
        r3 = r3.getTrackGroup();
        r1 = r1.trackFormat;
        r1 = r3.indexOf(r1);
        r3 = r18.getSelectedIndexInTrackGroup();
        if (r3 == r1) goto L_0x012d;
    L_0x012c:
        goto L_0x012f;
    L_0x012d:
        r1 = 0;
        goto L_0x0130;
    L_0x012f:
        r1 = 1;
    L_0x0130:
        if (r1 == 0) goto L_0x0138;
    L_0x0132:
        r0.pendingResetUpstreamFormats = r15;
        r1 = 1;
        r16 = 1;
        goto L_0x013a;
    L_0x0138:
        r1 = r26;
    L_0x013a:
        if (r16 == 0) goto L_0x014b;
    L_0x013c:
        r0.seekToUs(r12, r1);
    L_0x013f:
        r1 = r2.length;
        if (r14 >= r1) goto L_0x014b;
    L_0x0142:
        r1 = r2[r14];
        if (r1 == 0) goto L_0x0148;
    L_0x0146:
        r23[r14] = r15;
    L_0x0148:
        r14 = r14 + 1;
        goto L_0x013f;
    L_0x014b:
        r0.updateSampleStreams(r2);
        r0.seenFirstTrackSelection = r15;
        return r16;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper.selectTracks(com.google.android.exoplayer2.trackselection.TrackSelection[], boolean[], com.google.android.exoplayer2.source.SampleStream[], boolean[], long, boolean):boolean");
    }

    public void discardBuffer(long j, boolean z) {
        if (this.sampleQueuesBuilt && !isPendingReset()) {
            int length = this.sampleQueues.length;
            for (int i = 0; i < length; i++) {
                this.sampleQueues[i].discardTo(j, z, this.sampleQueuesEnabledStates[i]);
            }
        }
    }

    public boolean seekToUs(long j, boolean z) {
        this.lastSeekPositionUs = j;
        if (isPendingReset()) {
            this.pendingResetPositionUs = j;
            return true;
        } else if (this.sampleQueuesBuilt && !z && seekInsideBufferUs(j)) {
            return false;
        } else {
            this.pendingResetPositionUs = j;
            this.loadingFinished = false;
            this.mediaChunks.clear();
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
            } else {
                resetSampleQueues();
            }
            return true;
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
        this.released = true;
        this.hlsSampleStreams.clear();
    }

    public void onLoaderReleased() {
        resetSampleQueues();
    }

    public void setIsTimestampMaster(boolean z) {
        this.chunkSource.setIsTimestampMaster(z);
    }

    public boolean onPlaylistError(HlsUrl hlsUrl, long j) {
        return this.chunkSource.onPlaylistError(hlsUrl, j);
    }

    public boolean isReady(int i) {
        return this.loadingFinished || (!isPendingReset() && this.sampleQueues[i].hasNextSample());
    }

    public void maybeThrowError() throws IOException {
        this.loader.maybeThrowError();
        this.chunkSource.maybeThrowError();
    }

    public int readData(int i, FormatHolder formatHolder, DecoderInputBuffer decoderInputBuffer, boolean z) {
        if (isPendingReset()) {
            return -3;
        }
        int i2 = 0;
        if (!this.mediaChunks.isEmpty()) {
            int i3 = 0;
            while (i3 < this.mediaChunks.size() - 1 && finishedReadingChunk((HlsMediaChunk) this.mediaChunks.get(i3))) {
                i3++;
            }
            Util.removeRange(this.mediaChunks, 0, i3);
            HlsMediaChunk hlsMediaChunk = (HlsMediaChunk) this.mediaChunks.get(0);
            Format format = hlsMediaChunk.trackFormat;
            if (!format.equals(this.downstreamTrackFormat)) {
                this.eventDispatcher.downstreamFormatChanged(this.trackType, format, hlsMediaChunk.trackSelectionReason, hlsMediaChunk.trackSelectionData, hlsMediaChunk.startTimeUs);
            }
            this.downstreamTrackFormat = format;
        }
        int read = this.sampleQueues[i].read(formatHolder, decoderInputBuffer, z, this.loadingFinished, this.lastSeekPositionUs);
        if (read == -5 && i == this.primarySampleQueueIndex) {
            i = this.sampleQueues[i].peekSourceId();
            while (i2 < this.mediaChunks.size() && ((HlsMediaChunk) this.mediaChunks.get(i2)).uid != i) {
                i2++;
            }
            formatHolder.format = formatHolder.format.copyWithManifestFormatInfo(i2 < this.mediaChunks.size() ? ((HlsMediaChunk) this.mediaChunks.get(i2)).trackFormat : this.upstreamTrackFormat);
        }
        return read;
    }

    public int skipData(int i, long j) {
        if (isPendingReset()) {
            return 0;
        }
        SampleQueue sampleQueue = this.sampleQueues[i];
        if (this.loadingFinished && j > sampleQueue.getLargestQueuedTimestampUs()) {
            return sampleQueue.advanceToEnd();
        }
        i = sampleQueue.advanceTo(j, true, true);
        if (i == -1) {
            i = 0;
        }
        return i;
    }

    public long getBufferedPositionUs() {
        if (this.loadingFinished) {
            return Long.MIN_VALUE;
        }
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long j = this.lastSeekPositionUs;
        Chunk lastMediaChunk = getLastMediaChunk();
        if (!lastMediaChunk.isLoadCompleted()) {
            if (this.mediaChunks.size() > 1) {
                ArrayList arrayList = this.mediaChunks;
                lastMediaChunk = (HlsMediaChunk) arrayList.get(arrayList.size() - 2);
            } else {
                lastMediaChunk = null;
            }
        }
        if (lastMediaChunk != null) {
            j = Math.max(j, lastMediaChunk.endTimeUs);
        }
        if (this.sampleQueuesBuilt) {
            for (SampleQueue largestQueuedTimestampUs : this.sampleQueues) {
                j = Math.max(j, largestQueuedTimestampUs.getLargestQueuedTimestampUs());
            }
        }
        return j;
    }

    public long getNextLoadPositionUs() {
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        return this.loadingFinished ? Long.MIN_VALUE : getLastMediaChunk().endTimeUs;
    }

    public boolean continueLoading(long j) {
        if (this.loadingFinished || this.loader.isLoading()) {
            return false;
        }
        List emptyList;
        long j2;
        if (isPendingReset()) {
            emptyList = Collections.emptyList();
            j2 = this.pendingResetPositionUs;
        } else {
            emptyList = this.readOnlyMediaChunks;
            HlsMediaChunk lastMediaChunk = getLastMediaChunk();
            if (lastMediaChunk.isLoadCompleted()) {
                j2 = lastMediaChunk.endTimeUs;
            } else {
                j2 = Math.max(this.lastSeekPositionUs, lastMediaChunk.startTimeUs);
            }
        }
        this.chunkSource.getNextChunk(j, j2, emptyList, this.nextChunkHolder);
        HlsChunkHolder hlsChunkHolder = this.nextChunkHolder;
        boolean z = hlsChunkHolder.endOfStream;
        Chunk chunk = hlsChunkHolder.chunk;
        HlsUrl hlsUrl = hlsChunkHolder.playlist;
        hlsChunkHolder.clear();
        if (z) {
            this.pendingResetPositionUs = -9223372036854775807L;
            this.loadingFinished = true;
            return true;
        } else if (chunk == null) {
            if (hlsUrl != null) {
                this.callback.onPlaylistRefreshRequired(hlsUrl);
            }
            return false;
        } else {
            if (isMediaChunk(chunk)) {
                this.pendingResetPositionUs = -9223372036854775807L;
                HlsMediaChunk hlsMediaChunk = (HlsMediaChunk) chunk;
                hlsMediaChunk.init(this);
                this.mediaChunks.add(hlsMediaChunk);
                this.upstreamTrackFormat = hlsMediaChunk.trackFormat;
            }
            this.eventDispatcher.loadStarted(chunk.dataSpec, chunk.type, this.trackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, this.loader.startLoading(chunk, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(chunk.type)));
            return true;
        }
    }

    public void onLoadCompleted(Chunk chunk, long j, long j2) {
        Chunk chunk2 = chunk;
        long j3 = j;
        long j4 = j2;
        this.chunkSource.onChunkLoadCompleted(chunk2);
        this.eventDispatcher.loadCompleted(chunk2.dataSpec, chunk.getUri(), chunk.getResponseHeaders(), chunk2.type, this.trackType, chunk2.trackFormat, chunk2.trackSelectionReason, chunk2.trackSelectionData, chunk2.startTimeUs, chunk2.endTimeUs, j3, j4, chunk.bytesLoaded());
        if (this.prepared) {
            this.callback.onContinueLoadingRequested(this);
        } else {
            continueLoading(this.lastSeekPositionUs);
        }
    }

    public void onLoadCanceled(Chunk chunk, long j, long j2, boolean z) {
        Chunk chunk2 = chunk;
        this.eventDispatcher.loadCanceled(chunk2.dataSpec, chunk.getUri(), chunk.getResponseHeaders(), chunk2.type, this.trackType, chunk2.trackFormat, chunk2.trackSelectionReason, chunk2.trackSelectionData, chunk2.startTimeUs, chunk2.endTimeUs, j, j2, chunk.bytesLoaded());
        if (!z) {
            resetSampleQueues();
            if (this.enabledTrackGroupCount > 0) {
                this.callback.onContinueLoadingRequested(this);
            }
        }
    }

    public LoadErrorAction onLoadError(Chunk chunk, long j, long j2, IOException iOException, int i) {
        LoadErrorAction loadErrorAction;
        Chunk chunk2 = chunk;
        long bytesLoaded = chunk.bytesLoaded();
        boolean isMediaChunk = isMediaChunk(chunk);
        long blacklistDurationMsFor = this.loadErrorHandlingPolicy.getBlacklistDurationMsFor(chunk2.type, j2, iOException, i);
        boolean z = false;
        boolean maybeBlacklistTrack = blacklistDurationMsFor != -9223372036854775807L ? this.chunkSource.maybeBlacklistTrack(chunk2, blacklistDurationMsFor) : false;
        if (maybeBlacklistTrack) {
            if (isMediaChunk && bytesLoaded == 0) {
                ArrayList arrayList = this.mediaChunks;
                if (((HlsMediaChunk) arrayList.remove(arrayList.size() - 1)) == chunk2) {
                    z = true;
                }
                Assertions.checkState(z);
                if (this.mediaChunks.isEmpty()) {
                    this.pendingResetPositionUs = this.lastSeekPositionUs;
                }
            }
            loadErrorAction = Loader.DONT_RETRY;
        } else {
            long retryDelayMsFor = this.loadErrorHandlingPolicy.getRetryDelayMsFor(chunk2.type, j2, iOException, i);
            loadErrorAction = retryDelayMsFor != -9223372036854775807L ? Loader.createRetryAction(false, retryDelayMsFor) : Loader.DONT_RETRY_FATAL;
        }
        LoadErrorAction loadErrorAction2 = loadErrorAction;
        this.eventDispatcher.loadError(chunk2.dataSpec, chunk.getUri(), chunk.getResponseHeaders(), chunk2.type, this.trackType, chunk2.trackFormat, chunk2.trackSelectionReason, chunk2.trackSelectionData, chunk2.startTimeUs, chunk2.endTimeUs, j, j2, bytesLoaded, iOException, loadErrorAction2.isRetry() ^ 1);
        if (maybeBlacklistTrack) {
            if (this.prepared) {
                this.callback.onContinueLoadingRequested(this);
            } else {
                continueLoading(this.lastSeekPositionUs);
            }
        }
        return loadErrorAction2;
    }

    public void init(int i, boolean z, boolean z2) {
        int i2 = 0;
        if (!z2) {
            this.audioSampleQueueMappingDone = false;
            this.videoSampleQueueMappingDone = false;
        }
        this.chunkUid = i;
        for (SampleQueue sourceId : this.sampleQueues) {
            sourceId.sourceId(i);
        }
        if (z) {
            SampleQueue[] sampleQueueArr = this.sampleQueues;
            int length = sampleQueueArr.length;
            while (i2 < length) {
                sampleQueueArr[i2].splice();
                i2++;
            }
        }
    }

    public TrackOutput track(int i, int i2) {
        int i3;
        SampleQueue[] sampleQueueArr = this.sampleQueues;
        int length = sampleQueueArr.length;
        boolean z = false;
        TrackOutput trackOutput;
        if (i2 == 1) {
            i3 = this.audioSampleQueueIndex;
            if (i3 != -1) {
                if (this.audioSampleQueueMappingDone) {
                    if (this.sampleQueueTrackIds[i3] == i) {
                        trackOutput = sampleQueueArr[i3];
                    } else {
                        trackOutput = createDummyTrackOutput(i, i2);
                    }
                    return trackOutput;
                }
                this.audioSampleQueueMappingDone = true;
                this.sampleQueueTrackIds[i3] = i;
                return sampleQueueArr[i3];
            } else if (this.tracksEnded) {
                return createDummyTrackOutput(i, i2);
            }
        } else if (i2 == 2) {
            i3 = this.videoSampleQueueIndex;
            if (i3 != -1) {
                if (this.videoSampleQueueMappingDone) {
                    if (this.sampleQueueTrackIds[i3] == i) {
                        trackOutput = sampleQueueArr[i3];
                    } else {
                        trackOutput = createDummyTrackOutput(i, i2);
                    }
                    return trackOutput;
                }
                this.videoSampleQueueMappingDone = true;
                this.sampleQueueTrackIds[i3] = i;
                return sampleQueueArr[i3];
            } else if (this.tracksEnded) {
                return createDummyTrackOutput(i, i2);
            }
        } else {
            for (int i4 = 0; i4 < length; i4++) {
                if (this.sampleQueueTrackIds[i4] == i) {
                    return this.sampleQueues[i4];
                }
            }
            if (this.tracksEnded) {
                return createDummyTrackOutput(i, i2);
            }
        }
        PrivTimestampStrippingSampleQueue privTimestampStrippingSampleQueue = new PrivTimestampStrippingSampleQueue(this.allocator);
        privTimestampStrippingSampleQueue.setSampleOffsetUs(this.sampleOffsetUs);
        privTimestampStrippingSampleQueue.sourceId(this.chunkUid);
        privTimestampStrippingSampleQueue.setUpstreamFormatChangeListener(this);
        i3 = length + 1;
        this.sampleQueueTrackIds = Arrays.copyOf(this.sampleQueueTrackIds, i3);
        this.sampleQueueTrackIds[length] = i;
        this.sampleQueues = (SampleQueue[]) Arrays.copyOf(this.sampleQueues, i3);
        this.sampleQueues[length] = privTimestampStrippingSampleQueue;
        this.sampleQueueIsAudioVideoFlags = Arrays.copyOf(this.sampleQueueIsAudioVideoFlags, i3);
        boolean[] zArr = this.sampleQueueIsAudioVideoFlags;
        if (i2 == 1 || i2 == 2) {
            z = true;
        }
        zArr[length] = z;
        this.haveAudioVideoSampleQueues |= this.sampleQueueIsAudioVideoFlags[length];
        if (i2 == 1) {
            this.audioSampleQueueMappingDone = true;
            this.audioSampleQueueIndex = length;
        } else if (i2 == 2) {
            this.videoSampleQueueMappingDone = true;
            this.videoSampleQueueIndex = length;
        }
        if (getTrackTypeScore(i2) > getTrackTypeScore(this.primarySampleQueueType)) {
            this.primarySampleQueueIndex = length;
            this.primarySampleQueueType = i2;
        }
        this.sampleQueuesEnabledStates = Arrays.copyOf(this.sampleQueuesEnabledStates, i3);
        return privTimestampStrippingSampleQueue;
    }

    public void endTracks() {
        this.tracksEnded = true;
        this.handler.post(this.onTracksEndedRunnable);
    }

    public void onUpstreamFormatChanged(Format format) {
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    public void setSampleOffsetUs(long j) {
        this.sampleOffsetUs = j;
        for (SampleQueue sampleOffsetUs : this.sampleQueues) {
            sampleOffsetUs.setSampleOffsetUs(j);
        }
    }

    private void updateSampleStreams(SampleStream[] sampleStreamArr) {
        this.hlsSampleStreams.clear();
        for (SampleStream sampleStream : sampleStreamArr) {
            if (sampleStream != null) {
                this.hlsSampleStreams.add((HlsSampleStream) sampleStream);
            }
        }
    }

    private boolean finishedReadingChunk(HlsMediaChunk hlsMediaChunk) {
        int i = hlsMediaChunk.uid;
        int length = this.sampleQueues.length;
        int i2 = 0;
        while (i2 < length) {
            if (this.sampleQueuesEnabledStates[i2] && this.sampleQueues[i2].peekSourceId() == i) {
                return false;
            }
            i2++;
        }
        return true;
    }

    private void resetSampleQueues() {
        for (SampleQueue reset : this.sampleQueues) {
            reset.reset(this.pendingResetUpstreamFormats);
        }
        this.pendingResetUpstreamFormats = false;
    }

    private void onTracksEnded() {
        this.sampleQueuesBuilt = true;
        maybeFinishPrepare();
    }

    private void maybeFinishPrepare() {
        if (!this.released && this.trackGroupToSampleQueueIndex == null && this.sampleQueuesBuilt) {
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
            if (this.trackGroups != null) {
                mapSampleQueuesToMatchTrackGroups();
            } else {
                buildTracksFromSampleStreams();
                this.prepared = true;
                this.callback.onPrepared();
            }
        }
    }

    private void mapSampleQueuesToMatchTrackGroups() {
        int i = this.trackGroups.length;
        this.trackGroupToSampleQueueIndex = new int[i];
        Arrays.fill(this.trackGroupToSampleQueueIndex, -1);
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = 0;
            while (true) {
                SampleQueue[] sampleQueueArr = this.sampleQueues;
                if (i3 >= sampleQueueArr.length) {
                    break;
                } else if (formatsMatch(sampleQueueArr[i3].getUpstreamFormat(), this.trackGroups.get(i2).getFormat(0))) {
                    this.trackGroupToSampleQueueIndex[i2] = i3;
                    break;
                } else {
                    i3++;
                }
            }
        }
        Iterator it = this.hlsSampleStreams.iterator();
        while (it.hasNext()) {
            ((HlsSampleStream) it.next()).bindSampleQueue();
        }
    }

    private void buildTracksFromSampleStreams() {
        int length = this.sampleQueues.length;
        boolean z = false;
        int i = 0;
        int i2 = 6;
        int i3 = -1;
        while (true) {
            int i4 = 2;
            if (i >= length) {
                break;
            }
            String str = this.sampleQueues[i].getUpstreamFormat().sampleMimeType;
            if (!MimeTypes.isVideo(str)) {
                i4 = MimeTypes.isAudio(str) ? 1 : MimeTypes.isText(str) ? 3 : 6;
            }
            if (getTrackTypeScore(i4) > getTrackTypeScore(i2)) {
                i3 = i;
                i2 = i4;
            } else if (i4 == i2 && i3 != -1) {
                i3 = -1;
            }
            i++;
        }
        TrackGroup trackGroup = this.chunkSource.getTrackGroup();
        i = trackGroup.length;
        this.primaryTrackGroupIndex = -1;
        this.trackGroupToSampleQueueIndex = new int[length];
        for (int i5 = 0; i5 < length; i5++) {
            this.trackGroupToSampleQueueIndex[i5] = i5;
        }
        TrackGroup[] trackGroupArr = new TrackGroup[length];
        for (int i6 = 0; i6 < length; i6++) {
            Format upstreamFormat = this.sampleQueues[i6].getUpstreamFormat();
            if (i6 == i3) {
                Format[] formatArr = new Format[i];
                if (i == 1) {
                    formatArr[0] = upstreamFormat.copyWithManifestFormatInfo(trackGroup.getFormat(0));
                } else {
                    for (int i7 = 0; i7 < i; i7++) {
                        formatArr[i7] = deriveFormat(trackGroup.getFormat(i7), upstreamFormat, true);
                    }
                }
                trackGroupArr[i6] = new TrackGroup(formatArr);
                this.primaryTrackGroupIndex = i6;
            } else {
                Format format = (i2 == 2 && MimeTypes.isAudio(upstreamFormat.sampleMimeType)) ? this.muxedAudioFormat : null;
                trackGroupArr[i6] = new TrackGroup(deriveFormat(format, upstreamFormat, false));
            }
        }
        this.trackGroups = new TrackGroupArray(trackGroupArr);
        if (this.optionalTrackGroups == null) {
            z = true;
        }
        Assertions.checkState(z);
        this.optionalTrackGroups = TrackGroupArray.EMPTY;
    }

    private HlsMediaChunk getLastMediaChunk() {
        ArrayList arrayList = this.mediaChunks;
        return (HlsMediaChunk) arrayList.get(arrayList.size() - 1);
    }

    private boolean isPendingReset() {
        return this.pendingResetPositionUs != -9223372036854775807L;
    }

    private boolean seekInsideBufferUs(long j) {
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
            if (z || (!this.sampleQueueIsAudioVideoFlags[i] && this.haveAudioVideoSampleQueues)) {
                i++;
            }
        }
        return false;
    }

    private static Format deriveFormat(Format format, Format format2, boolean z) {
        if (format == null) {
            return format2;
        }
        int i = z ? format.bitrate : -1;
        String codecsOfType = Util.getCodecsOfType(format.codecs, MimeTypes.getTrackType(format2.sampleMimeType));
        String mediaMimeType = MimeTypes.getMediaMimeType(codecsOfType);
        if (mediaMimeType == null) {
            mediaMimeType = format2.sampleMimeType;
        }
        return format2.copyWithContainerInfo(format.f14id, format.label, mediaMimeType, codecsOfType, i, format.width, format.height, format.selectionFlags, format.language);
    }

    private static boolean isMediaChunk(Chunk chunk) {
        return chunk instanceof HlsMediaChunk;
    }

    private static boolean formatsMatch(Format format, Format format2) {
        String str = format.sampleMimeType;
        String str2 = format2.sampleMimeType;
        int trackType = MimeTypes.getTrackType(str);
        boolean z = true;
        if (trackType != 3) {
            if (trackType != MimeTypes.getTrackType(str2)) {
                z = false;
            }
            return z;
        } else if (!Util.areEqual(str, str2)) {
            return false;
        } else {
            if (!MimeTypes.APPLICATION_CEA608.equals(str) && !MimeTypes.APPLICATION_CEA708.equals(str)) {
                return true;
            }
            if (format.accessibilityChannel != format2.accessibilityChannel) {
                z = false;
            }
            return z;
        }
    }

    private static DummyTrackOutput createDummyTrackOutput(int i, int i2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unmapped track with id ");
        stringBuilder.append(i);
        stringBuilder.append(" of type ");
        stringBuilder.append(i2);
        Log.m18w("HlsSampleStreamWrapper", stringBuilder.toString());
        return new DummyTrackOutput();
    }
}
