// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.smoothstreaming;

import com.google.android.exoplayer2.source.chunk.BaseMediaChunkIterator;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.chunk.ChunkHolder;
import java.util.List;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.source.chunk.ContainerMediaChunk;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import android.net.Uri;
import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import java.io.IOException;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.upstream.DataSource;

public class DefaultSsChunkSource implements SsChunkSource
{
    private int currentManifestChunkOffset;
    private final DataSource dataSource;
    private final ChunkExtractorWrapper[] extractorWrappers;
    private IOException fatalError;
    private SsManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final int streamElementIndex;
    private final TrackSelection trackSelection;
    
    public DefaultSsChunkSource(final LoaderErrorThrower manifestLoaderErrorThrower, final SsManifest manifest, int i, final TrackSelection trackSelection, final DataSource dataSource) {
        this.manifestLoaderErrorThrower = manifestLoaderErrorThrower;
        this.manifest = manifest;
        this.streamElementIndex = i;
        this.trackSelection = trackSelection;
        this.dataSource = dataSource;
        final SsManifest.StreamElement streamElement = manifest.streamElements[i];
        this.extractorWrappers = new ChunkExtractorWrapper[trackSelection.length()];
        int indexInTrackGroup;
        Format format;
        TrackEncryptionBox[] trackEncryptionBoxes;
        int n;
        for (i = 0; i < this.extractorWrappers.length; ++i) {
            indexInTrackGroup = trackSelection.getIndexInTrackGroup(i);
            format = streamElement.formats[indexInTrackGroup];
            if (format.drmInitData != null) {
                trackEncryptionBoxes = manifest.protectionElement.trackEncryptionBoxes;
            }
            else {
                trackEncryptionBoxes = null;
            }
            if (streamElement.type == 2) {
                n = 4;
            }
            else {
                n = 0;
            }
            this.extractorWrappers[i] = new ChunkExtractorWrapper(new FragmentedMp4Extractor(3, null, new Track(indexInTrackGroup, streamElement.type, streamElement.timescale, -9223372036854775807L, manifest.durationUs, format, 0, trackEncryptionBoxes, n, null, null), null), streamElement.type, format);
        }
    }
    
    private static MediaChunk newMediaChunk(final Format format, final DataSource dataSource, final Uri uri, final String s, final int n, final long n2, final long n3, final long n4, final int n5, final Object o, final ChunkExtractorWrapper chunkExtractorWrapper) {
        return new ContainerMediaChunk(dataSource, new DataSpec(uri, 0L, -1L, s), format, n5, o, n2, n3, n4, -9223372036854775807L, n, 1, n2, chunkExtractorWrapper);
    }
    
    private long resolveTimeToLiveEdgeUs(final long n) {
        final SsManifest manifest = this.manifest;
        if (!manifest.isLive) {
            return -9223372036854775807L;
        }
        final SsManifest.StreamElement streamElement = manifest.streamElements[this.streamElementIndex];
        final int n2 = streamElement.chunkCount - 1;
        return streamElement.getStartTimeUs(n2) + streamElement.getChunkDurationUs(n2) - n;
    }
    
    @Override
    public long getAdjustedSeekPositionUs(final long n, final SeekParameters seekParameters) {
        final SsManifest.StreamElement streamElement = this.manifest.streamElements[this.streamElementIndex];
        final int chunkIndex = streamElement.getChunkIndex(n);
        final long startTimeUs = streamElement.getStartTimeUs(chunkIndex);
        long startTimeUs2;
        if (startTimeUs < n && chunkIndex < streamElement.chunkCount - 1) {
            startTimeUs2 = streamElement.getStartTimeUs(chunkIndex + 1);
        }
        else {
            startTimeUs2 = startTimeUs;
        }
        return Util.resolveSeekPositionUs(n, seekParameters, startTimeUs, startTimeUs2);
    }
    
    @Override
    public final void getNextChunk(long startTimeUs, long n, final List<? extends MediaChunk> list, final ChunkHolder chunkHolder) {
        if (this.fatalError != null) {
            return;
        }
        final SsManifest manifest = this.manifest;
        final SsManifest.StreamElement streamElement = manifest.streamElements[this.streamElementIndex];
        if (streamElement.chunkCount == 0) {
            chunkHolder.endOfStream = (manifest.isLive ^ true);
            return;
        }
        int chunkIndex;
        if (list.isEmpty()) {
            chunkIndex = streamElement.getChunkIndex(n);
        }
        else if ((chunkIndex = (int)(((MediaChunk)list.get(list.size() - 1)).getNextChunkIndex() - this.currentManifestChunkOffset)) < 0) {
            this.fatalError = new BehindLiveWindowException();
            return;
        }
        if (chunkIndex >= streamElement.chunkCount) {
            chunkHolder.endOfStream = (this.manifest.isLive ^ true);
            return;
        }
        final long resolveTimeToLiveEdgeUs = this.resolveTimeToLiveEdgeUs(startTimeUs);
        final MediaChunkIterator[] array = new MediaChunkIterator[this.trackSelection.length()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new StreamElementIterator(streamElement, this.trackSelection.getIndexInTrackGroup(i), chunkIndex);
        }
        this.trackSelection.updateSelectedTrack(startTimeUs, n - startTimeUs, resolveTimeToLiveEdgeUs, list, array);
        startTimeUs = streamElement.getStartTimeUs(chunkIndex);
        final long chunkDurationUs = streamElement.getChunkDurationUs(chunkIndex);
        if (!list.isEmpty()) {
            n = -9223372036854775807L;
        }
        final int currentManifestChunkOffset = this.currentManifestChunkOffset;
        final int selectedIndex = this.trackSelection.getSelectedIndex();
        chunkHolder.chunk = newMediaChunk(this.trackSelection.getSelectedFormat(), this.dataSource, streamElement.buildRequestUri(this.trackSelection.getIndexInTrackGroup(selectedIndex), chunkIndex), null, chunkIndex + currentManifestChunkOffset, startTimeUs, startTimeUs + chunkDurationUs, n, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), this.extractorWrappers[selectedIndex]);
    }
    
    @Override
    public int getPreferredQueueSize(final long n, final List<? extends MediaChunk> list) {
        if (this.fatalError == null && this.trackSelection.length() >= 2) {
            return this.trackSelection.evaluateQueueSize(n, list);
        }
        return list.size();
    }
    
    @Override
    public void maybeThrowError() throws IOException {
        final IOException fatalError = this.fatalError;
        if (fatalError == null) {
            this.manifestLoaderErrorThrower.maybeThrowError();
            return;
        }
        throw fatalError;
    }
    
    @Override
    public void onChunkLoadCompleted(final Chunk chunk) {
    }
    
    @Override
    public boolean onChunkLoadError(final Chunk chunk, final boolean b, final Exception ex, final long n) {
        if (b && n != -9223372036854775807L) {
            final TrackSelection trackSelection = this.trackSelection;
            if (trackSelection.blacklist(trackSelection.indexOf(chunk.trackFormat), n)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void updateManifest(final SsManifest manifest) {
        final SsManifest.StreamElement[] streamElements = this.manifest.streamElements;
        final int streamElementIndex = this.streamElementIndex;
        final SsManifest.StreamElement streamElement = streamElements[streamElementIndex];
        final int chunkCount = streamElement.chunkCount;
        final SsManifest.StreamElement streamElement2 = manifest.streamElements[streamElementIndex];
        if (chunkCount != 0 && streamElement2.chunkCount != 0) {
            final int n = chunkCount - 1;
            final long startTimeUs = streamElement.getStartTimeUs(n);
            final long chunkDurationUs = streamElement.getChunkDurationUs(n);
            final long startTimeUs2 = streamElement2.getStartTimeUs(0);
            if (startTimeUs + chunkDurationUs <= startTimeUs2) {
                this.currentManifestChunkOffset += chunkCount;
            }
            else {
                this.currentManifestChunkOffset += streamElement.getChunkIndex(startTimeUs2);
            }
        }
        else {
            this.currentManifestChunkOffset += chunkCount;
        }
        this.manifest = manifest;
    }
    
    public static final class Factory implements SsChunkSource.Factory
    {
        private final DataSource.Factory dataSourceFactory;
        
        public Factory(final DataSource.Factory dataSourceFactory) {
            this.dataSourceFactory = dataSourceFactory;
        }
        
        @Override
        public SsChunkSource createChunkSource(final LoaderErrorThrower loaderErrorThrower, final SsManifest ssManifest, final int n, final TrackSelection trackSelection, final TransferListener transferListener) {
            final DataSource dataSource = this.dataSourceFactory.createDataSource();
            if (transferListener != null) {
                dataSource.addTransferListener(transferListener);
            }
            return new DefaultSsChunkSource(loaderErrorThrower, ssManifest, n, trackSelection, dataSource);
        }
    }
    
    private static final class StreamElementIterator extends BaseMediaChunkIterator
    {
        private final SsManifest.StreamElement streamElement;
        private final int trackIndex;
        
        public StreamElementIterator(final SsManifest.StreamElement streamElement, final int trackIndex, final int n) {
            super(n, streamElement.chunkCount - 1);
            this.streamElement = streamElement;
            this.trackIndex = trackIndex;
        }
    }
}
