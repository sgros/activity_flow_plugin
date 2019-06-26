package com.google.android.exoplayer2.source.smoothstreaming;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.chunk.BaseMediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.source.chunk.ChunkHolder;
import com.google.android.exoplayer2.source.chunk.ContainerMediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest.StreamElement;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.List;

public class DefaultSsChunkSource implements SsChunkSource {
    private int currentManifestChunkOffset;
    private final DataSource dataSource;
    private final ChunkExtractorWrapper[] extractorWrappers;
    private IOException fatalError;
    private SsManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final int streamElementIndex;
    private final TrackSelection trackSelection;

    public static final class Factory implements com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory {
        private final com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory;

        public Factory(com.google.android.exoplayer2.upstream.DataSource.Factory factory) {
            this.dataSourceFactory = factory;
        }

        public SsChunkSource createChunkSource(LoaderErrorThrower loaderErrorThrower, SsManifest ssManifest, int i, TrackSelection trackSelection, TransferListener transferListener) {
            DataSource createDataSource = this.dataSourceFactory.createDataSource();
            if (transferListener != null) {
                createDataSource.addTransferListener(transferListener);
            }
            return new DefaultSsChunkSource(loaderErrorThrower, ssManifest, i, trackSelection, createDataSource);
        }
    }

    private static final class StreamElementIterator extends BaseMediaChunkIterator {
        private final StreamElement streamElement;
        private final int trackIndex;

        public StreamElementIterator(StreamElement streamElement, int i, int i2) {
            super((long) i2, (long) (streamElement.chunkCount - 1));
            this.streamElement = streamElement;
            this.trackIndex = i;
        }
    }

    public void onChunkLoadCompleted(Chunk chunk) {
    }

    public DefaultSsChunkSource(LoaderErrorThrower loaderErrorThrower, SsManifest ssManifest, int i, TrackSelection trackSelection, DataSource dataSource) {
        SsManifest ssManifest2 = ssManifest;
        int i2 = i;
        TrackSelection trackSelection2 = trackSelection;
        this.manifestLoaderErrorThrower = loaderErrorThrower;
        this.manifest = ssManifest2;
        this.streamElementIndex = i2;
        this.trackSelection = trackSelection2;
        this.dataSource = dataSource;
        StreamElement streamElement = ssManifest2.streamElements[i2];
        this.extractorWrappers = new ChunkExtractorWrapper[trackSelection.length()];
        int i3 = 0;
        while (i3 < this.extractorWrappers.length) {
            int indexInTrackGroup = trackSelection2.getIndexInTrackGroup(i3);
            Format format = streamElement.formats[indexInTrackGroup];
            i = i3;
            Track track = r7;
            Track track2 = new Track(indexInTrackGroup, streamElement.type, streamElement.timescale, -9223372036854775807L, ssManifest2.durationUs, format, 0, format.drmInitData != null ? ssManifest2.protectionElement.trackEncryptionBoxes : null, streamElement.type == 2 ? 4 : 0, null, null);
            this.extractorWrappers[i] = new ChunkExtractorWrapper(new FragmentedMp4Extractor(3, null, track, null), streamElement.type, format);
            i3 = i + 1;
        }
    }

    public long getAdjustedSeekPositionUs(long j, SeekParameters seekParameters) {
        StreamElement streamElement = this.manifest.streamElements[this.streamElementIndex];
        int chunkIndex = streamElement.getChunkIndex(j);
        long startTimeUs = streamElement.getStartTimeUs(chunkIndex);
        long startTimeUs2 = (startTimeUs >= j || chunkIndex >= streamElement.chunkCount - 1) ? startTimeUs : streamElement.getStartTimeUs(chunkIndex + 1);
        return Util.resolveSeekPositionUs(j, seekParameters, startTimeUs, startTimeUs2);
    }

    public void updateManifest(SsManifest ssManifest) {
        StreamElement[] streamElementArr = this.manifest.streamElements;
        int i = this.streamElementIndex;
        StreamElement streamElement = streamElementArr[i];
        int i2 = streamElement.chunkCount;
        StreamElement streamElement2 = ssManifest.streamElements[i];
        if (i2 == 0 || streamElement2.chunkCount == 0) {
            this.currentManifestChunkOffset += i2;
        } else {
            int i3 = i2 - 1;
            long startTimeUs = streamElement.getStartTimeUs(i3) + streamElement.getChunkDurationUs(i3);
            long startTimeUs2 = streamElement2.getStartTimeUs(0);
            if (startTimeUs <= startTimeUs2) {
                this.currentManifestChunkOffset += i2;
            } else {
                this.currentManifestChunkOffset += streamElement.getChunkIndex(startTimeUs2);
            }
        }
        this.manifest = ssManifest;
    }

    public void maybeThrowError() throws IOException {
        IOException iOException = this.fatalError;
        if (iOException == null) {
            this.manifestLoaderErrorThrower.maybeThrowError();
            return;
        }
        throw iOException;
    }

    public int getPreferredQueueSize(long j, List<? extends MediaChunk> list) {
        if (this.fatalError != null || this.trackSelection.length() < 2) {
            return list.size();
        }
        return this.trackSelection.evaluateQueueSize(j, list);
    }

    public final void getNextChunk(long j, long j2, List<? extends MediaChunk> list, ChunkHolder chunkHolder) {
        long j3 = j2;
        ChunkHolder chunkHolder2 = chunkHolder;
        if (this.fatalError == null) {
            SsManifest ssManifest = this.manifest;
            StreamElement streamElement = ssManifest.streamElements[this.streamElementIndex];
            if (streamElement.chunkCount == 0) {
                chunkHolder2.endOfStream = ssManifest.isLive ^ 1;
                return;
            }
            int chunkIndex;
            if (list.isEmpty()) {
                chunkIndex = streamElement.getChunkIndex(j3);
                List<? extends MediaChunk> list2 = list;
            } else {
                chunkIndex = (int) (((MediaChunk) list.get(list.size() - 1)).getNextChunkIndex() - ((long) this.currentManifestChunkOffset));
                if (chunkIndex < 0) {
                    this.fatalError = new BehindLiveWindowException();
                    return;
                }
            }
            if (chunkIndex >= streamElement.chunkCount) {
                chunkHolder2.endOfStream = this.manifest.isLive ^ 1;
                return;
            }
            long j4 = j3 - j;
            long resolveTimeToLiveEdgeUs = resolveTimeToLiveEdgeUs(j);
            MediaChunkIterator[] mediaChunkIteratorArr = new MediaChunkIterator[this.trackSelection.length()];
            for (int i = 0; i < mediaChunkIteratorArr.length; i++) {
                mediaChunkIteratorArr[i] = new StreamElementIterator(streamElement, this.trackSelection.getIndexInTrackGroup(i), chunkIndex);
            }
            this.trackSelection.updateSelectedTrack(j, j4, resolveTimeToLiveEdgeUs, list, mediaChunkIteratorArr);
            long startTimeUs = streamElement.getStartTimeUs(chunkIndex);
            long chunkDurationUs = startTimeUs + streamElement.getChunkDurationUs(chunkIndex);
            if (!list.isEmpty()) {
                j3 = -9223372036854775807L;
            }
            long j5 = j3;
            int i2 = chunkIndex + this.currentManifestChunkOffset;
            int selectedIndex = this.trackSelection.getSelectedIndex();
            ChunkExtractorWrapper chunkExtractorWrapper = this.extractorWrappers[selectedIndex];
            Uri buildRequestUri = streamElement.buildRequestUri(this.trackSelection.getIndexInTrackGroup(selectedIndex), chunkIndex);
            chunkHolder2.chunk = newMediaChunk(this.trackSelection.getSelectedFormat(), this.dataSource, buildRequestUri, null, i2, startTimeUs, chunkDurationUs, j5, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), chunkExtractorWrapper);
        }
    }

    public boolean onChunkLoadError(Chunk chunk, boolean z, Exception exception, long j) {
        if (z && j != -9223372036854775807L) {
            TrackSelection trackSelection = this.trackSelection;
            if (trackSelection.blacklist(trackSelection.indexOf(chunk.trackFormat), j)) {
                return true;
            }
        }
        return false;
    }

    private static MediaChunk newMediaChunk(Format format, DataSource dataSource, Uri uri, String str, int i, long j, long j2, long j3, int i2, Object obj, ChunkExtractorWrapper chunkExtractorWrapper) {
        Format format2 = format;
        DataSource dataSource2 = dataSource;
        long j4 = j;
        long j5 = j;
        long j6 = j2;
        long j7 = j3;
        int i3 = i2;
        Object obj2 = obj;
        ChunkExtractorWrapper chunkExtractorWrapper2 = chunkExtractorWrapper;
        DataSpec dataSpec = r26;
        DataSpec dataSpec2 = new DataSpec(uri, 0, -1, str);
        return new ContainerMediaChunk(dataSource2, dataSpec, format2, i3, obj2, j4, j6, j7, -9223372036854775807L, (long) i, 1, j5, chunkExtractorWrapper2);
    }

    private long resolveTimeToLiveEdgeUs(long j) {
        SsManifest ssManifest = this.manifest;
        if (!ssManifest.isLive) {
            return -9223372036854775807L;
        }
        StreamElement streamElement = ssManifest.streamElements[this.streamElementIndex];
        int i = streamElement.chunkCount - 1;
        return (streamElement.getStartTimeUs(i) + streamElement.getChunkDurationUs(i)) - j;
    }
}
