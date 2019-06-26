// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import java.util.List;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.SinglePeriodTimeline;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import java.io.IOException;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistTracker;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylist;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import android.os.Handler;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.upstream.TransferListener;
import android.net.Uri;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.source.BaseMediaSource;

public final class HlsMediaSource extends BaseMediaSource implements PrimaryPlaylistListener
{
    private final boolean allowChunklessPreparation;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private final HlsExtractorFactory extractorFactory;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final Uri manifestUri;
    private TransferListener mediaTransferListener;
    private final HlsPlaylistTracker playlistTracker;
    private final Object tag;
    
    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.hls");
    }
    
    @Deprecated
    public HlsMediaSource(final Uri uri, final HlsDataSourceFactory hlsDataSourceFactory, final HlsExtractorFactory hlsExtractorFactory, final int n, final Handler handler, final MediaSourceEventListener mediaSourceEventListener, final ParsingLoadable.Parser<HlsPlaylist> parser) {
        this(uri, hlsDataSourceFactory, hlsExtractorFactory, new DefaultCompositeSequenceableLoaderFactory(), new DefaultLoadErrorHandlingPolicy(n), new DefaultHlsPlaylistTracker(hlsDataSourceFactory, new DefaultLoadErrorHandlingPolicy(n), parser), false, null);
        if (handler != null && mediaSourceEventListener != null) {
            this.addEventListener(handler, mediaSourceEventListener);
        }
    }
    
    private HlsMediaSource(final Uri manifestUri, final HlsDataSourceFactory dataSourceFactory, final HlsExtractorFactory extractorFactory, final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, final LoadErrorHandlingPolicy loadErrorHandlingPolicy, final HlsPlaylistTracker playlistTracker, final boolean allowChunklessPreparation, final Object tag) {
        this.manifestUri = manifestUri;
        this.dataSourceFactory = dataSourceFactory;
        this.extractorFactory = extractorFactory;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.playlistTracker = playlistTracker;
        this.allowChunklessPreparation = allowChunklessPreparation;
        this.tag = tag;
    }
    
    @Deprecated
    public HlsMediaSource(final Uri uri, final DataSource.Factory factory, final int n, final Handler handler, final MediaSourceEventListener mediaSourceEventListener) {
        this(uri, new DefaultHlsDataSourceFactory(factory), HlsExtractorFactory.DEFAULT, n, handler, mediaSourceEventListener, new HlsPlaylistParser());
    }
    
    @Deprecated
    public HlsMediaSource(final Uri uri, final DataSource.Factory factory, final Handler handler, final MediaSourceEventListener mediaSourceEventListener) {
        this(uri, factory, 3, handler, mediaSourceEventListener);
    }
    
    @Override
    public MediaPeriod createPeriod(final MediaPeriodId mediaPeriodId, final Allocator allocator, final long n) {
        return new HlsMediaPeriod(this.extractorFactory, this.playlistTracker, this.dataSourceFactory, this.mediaTransferListener, this.loadErrorHandlingPolicy, this.createEventDispatcher(mediaPeriodId), allocator, this.compositeSequenceableLoaderFactory, this.allowChunklessPreparation);
    }
    
    @Override
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.playlistTracker.maybeThrowPrimaryPlaylistRefreshError();
    }
    
    @Override
    public void onPrimaryPlaylistRefreshed(final HlsMediaPlaylist hlsMediaPlaylist) {
        long usToMs;
        if (hlsMediaPlaylist.hasProgramDateTime) {
            usToMs = C.usToMs(hlsMediaPlaylist.startTimeUs);
        }
        else {
            usToMs = -9223372036854775807L;
        }
        final int playlistType = hlsMediaPlaylist.playlistType;
        long n;
        if (playlistType != 2 && playlistType != 1) {
            n = -9223372036854775807L;
        }
        else {
            n = usToMs;
        }
        long n2 = hlsMediaPlaylist.startOffsetUs;
        SinglePeriodTimeline singlePeriodTimeline;
        if (this.playlistTracker.isLive()) {
            final long n3 = hlsMediaPlaylist.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
            long n4;
            if (hlsMediaPlaylist.hasEndTag) {
                n4 = n3 + hlsMediaPlaylist.durationUs;
            }
            else {
                n4 = -9223372036854775807L;
            }
            final List<HlsMediaPlaylist.Segment> segments = hlsMediaPlaylist.segments;
            if (n2 == -9223372036854775807L) {
                if (segments.isEmpty()) {
                    n2 = 0L;
                }
                else {
                    n2 = ((HlsMediaPlaylist.Segment)segments.get(Math.max(0, segments.size() - 3))).relativeStartTimeUs;
                }
            }
            singlePeriodTimeline = new SinglePeriodTimeline(n, usToMs, n4, hlsMediaPlaylist.durationUs, n3, n2, true, hlsMediaPlaylist.hasEndTag ^ true, this.tag);
        }
        else {
            if (n2 == -9223372036854775807L) {
                n2 = 0L;
            }
            final long durationUs = hlsMediaPlaylist.durationUs;
            singlePeriodTimeline = new SinglePeriodTimeline(n, usToMs, durationUs, durationUs, 0L, n2, true, false, this.tag);
        }
        this.refreshSourceInfo(singlePeriodTimeline, new HlsManifest(this.playlistTracker.getMasterPlaylist(), hlsMediaPlaylist));
    }
    
    public void prepareSourceInternal(final TransferListener mediaTransferListener) {
        this.mediaTransferListener = mediaTransferListener;
        this.playlistTracker.start(this.manifestUri, this.createEventDispatcher(null), (HlsPlaylistTracker.PrimaryPlaylistListener)this);
    }
    
    @Override
    public void releasePeriod(final MediaPeriod mediaPeriod) {
        ((HlsMediaPeriod)mediaPeriod).release();
    }
    
    public void releaseSourceInternal() {
        this.playlistTracker.stop();
    }
}
