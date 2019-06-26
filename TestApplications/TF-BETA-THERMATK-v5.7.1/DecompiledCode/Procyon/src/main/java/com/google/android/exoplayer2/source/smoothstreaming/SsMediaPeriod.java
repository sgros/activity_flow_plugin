// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.smoothstreaming;

import java.util.ArrayList;
import com.google.android.exoplayer2.source.SampleStream;
import java.io.IOException;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.ChunkSource;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.source.chunk.ChunkSampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.MediaPeriod;

final class SsMediaPeriod implements MediaPeriod, SequenceableLoader.Callback<ChunkSampleStream<SsChunkSource>>
{
    private final Allocator allocator;
    private MediaPeriod.Callback callback;
    private final SsChunkSource.Factory chunkSourceFactory;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private SsManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private boolean notifiedReadingStarted;
    private ChunkSampleStream<SsChunkSource>[] sampleStreams;
    private final TrackGroupArray trackGroups;
    private final TransferListener transferListener;
    
    public SsMediaPeriod(final SsManifest manifest, final SsChunkSource.Factory chunkSourceFactory, final TransferListener transferListener, final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final MediaSourceEventListener.EventDispatcher eventDispatcher, final LoaderErrorThrower manifestLoaderErrorThrower, final Allocator allocator) {
        this.manifest = manifest;
        this.chunkSourceFactory = chunkSourceFactory;
        this.transferListener = transferListener;
        this.manifestLoaderErrorThrower = manifestLoaderErrorThrower;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.eventDispatcher = eventDispatcher;
        this.allocator = allocator;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.trackGroups = buildTrackGroups(manifest);
        this.sampleStreams = newSampleStreamArray(0);
        this.compositeSequenceableLoader = compositeSequenceableLoaderFactory.createCompositeSequenceableLoader((SequenceableLoader[])this.sampleStreams);
        eventDispatcher.mediaPeriodCreated();
    }
    
    private ChunkSampleStream<SsChunkSource> buildSampleStream(final TrackSelection trackSelection, final long n) {
        final int index = this.trackGroups.indexOf(trackSelection.getTrackGroup());
        return new ChunkSampleStream<SsChunkSource>(this.manifest.streamElements[index].type, null, null, this.chunkSourceFactory.createChunkSource(this.manifestLoaderErrorThrower, this.manifest, index, trackSelection, this.transferListener), (SequenceableLoader.Callback<ChunkSampleStream<ChunkSource>>)this, this.allocator, n, this.loadErrorHandlingPolicy, this.eventDispatcher);
    }
    
    private static TrackGroupArray buildTrackGroups(final SsManifest ssManifest) {
        final TrackGroup[] array = new TrackGroup[ssManifest.streamElements.length];
        int n = 0;
        while (true) {
            final SsManifest.StreamElement[] streamElements = ssManifest.streamElements;
            if (n >= streamElements.length) {
                break;
            }
            array[n] = new TrackGroup(streamElements[n].formats);
            ++n;
        }
        return new TrackGroupArray(array);
    }
    
    private static ChunkSampleStream<SsChunkSource>[] newSampleStreamArray(final int n) {
        return (ChunkSampleStream<SsChunkSource>[])new ChunkSampleStream[n];
    }
    
    @Override
    public boolean continueLoading(final long n) {
        return this.compositeSequenceableLoader.continueLoading(n);
    }
    
    @Override
    public void discardBuffer(final long n, final boolean b) {
        final ChunkSampleStream<SsChunkSource>[] sampleStreams = this.sampleStreams;
        for (int length = sampleStreams.length, i = 0; i < length; ++i) {
            sampleStreams[i].discardBuffer(n, b);
        }
    }
    
    @Override
    public long getAdjustedSeekPositionUs(final long n, final SeekParameters seekParameters) {
        for (final ChunkSampleStream<SsChunkSource> chunkSampleStream : this.sampleStreams) {
            if (chunkSampleStream.primaryTrackType == 2) {
                return chunkSampleStream.getAdjustedSeekPositionUs(n, seekParameters);
            }
        }
        return n;
    }
    
    @Override
    public long getBufferedPositionUs() {
        return this.compositeSequenceableLoader.getBufferedPositionUs();
    }
    
    @Override
    public long getNextLoadPositionUs() {
        return this.compositeSequenceableLoader.getNextLoadPositionUs();
    }
    
    @Override
    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }
    
    @Override
    public void maybeThrowPrepareError() throws IOException {
        this.manifestLoaderErrorThrower.maybeThrowError();
    }
    
    public void onContinueLoadingRequested(final ChunkSampleStream<SsChunkSource> chunkSampleStream) {
        ((SequenceableLoader.Callback<SsMediaPeriod>)this.callback).onContinueLoadingRequested(this);
    }
    
    @Override
    public void prepare(final MediaPeriod.Callback callback, final long n) {
        (this.callback = callback).onPrepared(this);
    }
    
    @Override
    public long readDiscontinuity() {
        if (!this.notifiedReadingStarted) {
            this.eventDispatcher.readingStarted();
            this.notifiedReadingStarted = true;
        }
        return -9223372036854775807L;
    }
    
    @Override
    public void reevaluateBuffer(final long n) {
        this.compositeSequenceableLoader.reevaluateBuffer(n);
    }
    
    public void release() {
        final ChunkSampleStream<SsChunkSource>[] sampleStreams = this.sampleStreams;
        for (int length = sampleStreams.length, i = 0; i < length; ++i) {
            sampleStreams[i].release();
        }
        this.callback = null;
        this.eventDispatcher.mediaPeriodReleased();
    }
    
    @Override
    public long seekToUs(final long n) {
        final ChunkSampleStream<SsChunkSource>[] sampleStreams = this.sampleStreams;
        for (int length = sampleStreams.length, i = 0; i < length; ++i) {
            sampleStreams[i].seekToUs(n);
        }
        return n;
    }
    
    @Override
    public long selectTracks(final TrackSelection[] array, final boolean[] array2, final SampleStream[] array3, final boolean[] array4, final long n) {
        final ArrayList<ChunkSampleStream<SsChunkSource>> list = new ArrayList<ChunkSampleStream<SsChunkSource>>();
        for (int i = 0; i < array.length; ++i) {
            if (array3[i] != null) {
                final ChunkSampleStream e = (ChunkSampleStream)array3[i];
                if (array[i] != null && array2[i]) {
                    list.add(e);
                }
                else {
                    e.release();
                    array3[i] = null;
                }
            }
            if (array3[i] == null && array[i] != null) {
                final ChunkSampleStream<SsChunkSource> buildSampleStream = this.buildSampleStream(array[i], n);
                list.add(buildSampleStream);
                array3[i] = buildSampleStream;
                array4[i] = true;
            }
        }
        list.toArray(this.sampleStreams = newSampleStreamArray(list.size()));
        this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.createCompositeSequenceableLoader((SequenceableLoader[])this.sampleStreams);
        return n;
    }
    
    public void updateManifest(final SsManifest manifest) {
        this.manifest = manifest;
        final ChunkSampleStream<SsChunkSource>[] sampleStreams = this.sampleStreams;
        for (int length = sampleStreams.length, i = 0; i < length; ++i) {
            sampleStreams[i].getChunkSource().updateManifest(manifest);
        }
        ((SequenceableLoader.Callback<SsMediaPeriod>)this.callback).onContinueLoadingRequested(this);
    }
}
