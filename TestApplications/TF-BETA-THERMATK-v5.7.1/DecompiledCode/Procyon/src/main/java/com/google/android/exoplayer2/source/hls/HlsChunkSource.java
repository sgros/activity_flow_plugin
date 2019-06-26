// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import android.os.SystemClock;
import com.google.android.exoplayer2.trackselection.BaseTrackSelection;
import com.google.android.exoplayer2.source.chunk.BaseMediaChunkIterator;
import java.util.Arrays;
import com.google.android.exoplayer2.source.chunk.DataChunk;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import java.math.BigInteger;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.Format;
import java.util.List;
import java.io.IOException;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource;

class HlsChunkSource
{
    private final DataSource encryptionDataSource;
    private byte[] encryptionIv;
    private String encryptionIvString;
    private byte[] encryptionKey;
    private Uri encryptionKeyUri;
    private HlsMasterPlaylist.HlsUrl expectedPlaylistUrl;
    private final HlsExtractorFactory extractorFactory;
    private IOException fatalError;
    private boolean independentSegments;
    private boolean isTimestampMaster;
    private long liveEdgeInPeriodTimeUs;
    private final DataSource mediaDataSource;
    private final List<Format> muxedCaptionFormats;
    private final HlsPlaylistTracker playlistTracker;
    private byte[] scratchSpace;
    private boolean seenExpectedPlaylistError;
    private final TimestampAdjusterProvider timestampAdjusterProvider;
    private final TrackGroup trackGroup;
    private TrackSelection trackSelection;
    private final HlsMasterPlaylist.HlsUrl[] variants;
    
    public HlsChunkSource(final HlsExtractorFactory extractorFactory, final HlsPlaylistTracker playlistTracker, final HlsMasterPlaylist.HlsUrl[] variants, final HlsDataSourceFactory hlsDataSourceFactory, final TransferListener transferListener, final TimestampAdjusterProvider timestampAdjusterProvider, final List<Format> muxedCaptionFormats) {
        this.extractorFactory = extractorFactory;
        this.playlistTracker = playlistTracker;
        this.variants = variants;
        this.timestampAdjusterProvider = timestampAdjusterProvider;
        this.muxedCaptionFormats = muxedCaptionFormats;
        this.liveEdgeInPeriodTimeUs = -9223372036854775807L;
        final Format[] array = new Format[variants.length];
        final int[] array2 = new int[variants.length];
        for (int i = 0; i < variants.length; ++i) {
            array[i] = variants[i].format;
            array2[i] = i;
        }
        this.mediaDataSource = hlsDataSourceFactory.createDataSource(1);
        if (transferListener != null) {
            this.mediaDataSource.addTransferListener(transferListener);
        }
        this.encryptionDataSource = hlsDataSourceFactory.createDataSource(3);
        this.trackGroup = new TrackGroup(array);
        this.trackSelection = new InitializationTrackSelection(this.trackGroup, array2);
    }
    
    private void clearEncryptionData() {
        this.encryptionKeyUri = null;
        this.encryptionKey = null;
        this.encryptionIvString = null;
        this.encryptionIv = null;
    }
    
    private long getChunkMediaSequence(final HlsMediaChunk hlsMediaChunk, final boolean b, final HlsMediaPlaylist hlsMediaPlaylist, long mediaSequence, long mediaSequence2) {
        if (hlsMediaChunk != null && !b) {
            return hlsMediaChunk.getNextChunkIndex();
        }
        final long durationUs = hlsMediaPlaylist.durationUs;
        long startTimeUs = mediaSequence2;
        if (hlsMediaChunk != null) {
            if (this.independentSegments) {
                startTimeUs = mediaSequence2;
            }
            else {
                startTimeUs = hlsMediaChunk.startTimeUs;
            }
        }
        if (!hlsMediaPlaylist.hasEndTag && startTimeUs >= durationUs + mediaSequence) {
            mediaSequence = hlsMediaPlaylist.mediaSequence;
            mediaSequence2 = hlsMediaPlaylist.segments.size();
        }
        else {
            mediaSequence = Util.binarySearchFloor(hlsMediaPlaylist.segments, startTimeUs - mediaSequence, true, !this.playlistTracker.isLive() || hlsMediaChunk == null);
            mediaSequence2 = hlsMediaPlaylist.mediaSequence;
        }
        return mediaSequence + mediaSequence2;
    }
    
    private EncryptionKeyChunk newEncryptionKeyChunk(final Uri uri, final String s, final int n, final int n2, final Object o) {
        return new EncryptionKeyChunk(this.encryptionDataSource, new DataSpec(uri, 0L, -1L, null, 1), this.variants[n].format, n2, o, this.scratchSpace, s);
    }
    
    private long resolveTimeToLiveEdgeUs(final long n) {
        final long liveEdgeInPeriodTimeUs = this.liveEdgeInPeriodTimeUs;
        long n2 = -9223372036854775807L;
        if (liveEdgeInPeriodTimeUs != -9223372036854775807L) {
            n2 = this.liveEdgeInPeriodTimeUs - n;
        }
        return n2;
    }
    
    private void setEncryptionData(final Uri encryptionKeyUri, final String encryptionIvString, final byte[] encryptionKey) {
        String substring;
        if (Util.toLowerInvariant(encryptionIvString).startsWith("0x")) {
            substring = encryptionIvString.substring(2);
        }
        else {
            substring = encryptionIvString;
        }
        final byte[] byteArray = new BigInteger(substring, 16).toByteArray();
        final byte[] encryptionIv = new byte[16];
        int n;
        if (byteArray.length > 16) {
            n = byteArray.length - 16;
        }
        else {
            n = 0;
        }
        System.arraycopy(byteArray, n, encryptionIv, encryptionIv.length - byteArray.length + n, byteArray.length - n);
        this.encryptionKeyUri = encryptionKeyUri;
        this.encryptionKey = encryptionKey;
        this.encryptionIvString = encryptionIvString;
        this.encryptionIv = encryptionIv;
    }
    
    private void updateLiveEdgeTimeUs(final HlsMediaPlaylist hlsMediaPlaylist) {
        long liveEdgeInPeriodTimeUs;
        if (hlsMediaPlaylist.hasEndTag) {
            liveEdgeInPeriodTimeUs = -9223372036854775807L;
        }
        else {
            liveEdgeInPeriodTimeUs = hlsMediaPlaylist.getEndTimeUs() - this.playlistTracker.getInitialStartTimeUs();
        }
        this.liveEdgeInPeriodTimeUs = liveEdgeInPeriodTimeUs;
    }
    
    public MediaChunkIterator[] createMediaChunkIterators(final HlsMediaChunk hlsMediaChunk, final long n) {
        int index;
        if (hlsMediaChunk == null) {
            index = -1;
        }
        else {
            index = this.trackGroup.indexOf(hlsMediaChunk.trackFormat);
        }
        final MediaChunkIterator[] array = new MediaChunkIterator[this.trackSelection.length()];
        for (int i = 0; i < array.length; ++i) {
            final int indexInTrackGroup = this.trackSelection.getIndexInTrackGroup(i);
            final HlsMasterPlaylist.HlsUrl hlsUrl = this.variants[indexInTrackGroup];
            if (!this.playlistTracker.isSnapshotValid(hlsUrl)) {
                array[i] = MediaChunkIterator.EMPTY;
            }
            else {
                final HlsMediaPlaylist playlistSnapshot = this.playlistTracker.getPlaylistSnapshot(hlsUrl, false);
                final long n2 = playlistSnapshot.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
                final long chunkMediaSequence = this.getChunkMediaSequence(hlsMediaChunk, indexInTrackGroup != index, playlistSnapshot, n2, n);
                final long mediaSequence = playlistSnapshot.mediaSequence;
                if (chunkMediaSequence < mediaSequence) {
                    array[i] = MediaChunkIterator.EMPTY;
                }
                else {
                    array[i] = new HlsMediaPlaylistSegmentIterator(playlistSnapshot, n2, (int)(chunkMediaSequence - mediaSequence));
                }
            }
        }
        return array;
    }
    
    public void getNextChunk(long n, long n2, final List<HlsMediaChunk> list, final HlsChunkHolder hlsChunkHolder) {
        HlsMediaChunk hlsMediaChunk;
        if (list.isEmpty()) {
            hlsMediaChunk = null;
        }
        else {
            hlsMediaChunk = list.get(list.size() - 1);
        }
        int index;
        if (hlsMediaChunk == null) {
            index = -1;
        }
        else {
            index = this.trackGroup.indexOf(hlsMediaChunk.trackFormat);
        }
        final long n3 = n2 - n;
        final long resolveTimeToLiveEdgeUs = this.resolveTimeToLiveEdgeUs(n);
        long max = n3;
        long n4 = 0L;
        Label_0151: {
            if (hlsMediaChunk != null) {
                max = n3;
                if (!this.independentSegments) {
                    final long durationUs = hlsMediaChunk.getDurationUs();
                    max = Math.max(0L, n3 - durationUs);
                    if (resolveTimeToLiveEdgeUs != -9223372036854775807L) {
                        final long max2 = Math.max(0L, resolveTimeToLiveEdgeUs - durationUs);
                        max = max;
                        n4 = max2;
                        break Label_0151;
                    }
                }
            }
            n4 = resolveTimeToLiveEdgeUs;
        }
        this.trackSelection.updateSelectedTrack(n, max, n4, list, this.createMediaChunkIterators(hlsMediaChunk, n2));
        final int selectedIndexInTrackGroup = this.trackSelection.getSelectedIndexInTrackGroup();
        final boolean b = false;
        final boolean b2 = false;
        final boolean b3 = index != selectedIndexInTrackGroup;
        HlsMasterPlaylist.HlsUrl hlsUrl = this.variants[selectedIndexInTrackGroup];
        if (!this.playlistTracker.isSnapshotValid(hlsUrl)) {
            hlsChunkHolder.playlist = hlsUrl;
            final boolean seenExpectedPlaylistError = this.seenExpectedPlaylistError;
            boolean b4 = b2;
            if (this.expectedPlaylistUrl == hlsUrl) {
                b4 = true;
            }
            this.seenExpectedPlaylistError = (seenExpectedPlaylistError & b4);
            this.expectedPlaylistUrl = hlsUrl;
            return;
        }
        HlsMediaPlaylist hlsMediaPlaylist = this.playlistTracker.getPlaylistSnapshot(hlsUrl, true);
        this.independentSegments = hlsMediaPlaylist.hasIndependentSegments;
        this.updateLiveEdgeTimeUs(hlsMediaPlaylist);
        final long n5 = hlsMediaPlaylist.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
        n = this.getChunkMediaSequence(hlsMediaChunk, b3, hlsMediaPlaylist, n5, n2);
        if (n < hlsMediaPlaylist.mediaSequence) {
            if (hlsMediaChunk == null || !b3) {
                this.fatalError = new BehindLiveWindowException();
                return;
            }
            hlsUrl = this.variants[index];
            hlsMediaPlaylist = this.playlistTracker.getPlaylistSnapshot(hlsUrl, true);
            n2 = hlsMediaPlaylist.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
            n = hlsMediaChunk.getNextChunkIndex();
        }
        else {
            index = selectedIndexInTrackGroup;
            n2 = n5;
        }
        final int n6 = (int)(n - hlsMediaPlaylist.mediaSequence);
        if (n6 >= hlsMediaPlaylist.segments.size()) {
            if (hlsMediaPlaylist.hasEndTag) {
                hlsChunkHolder.endOfStream = true;
            }
            else {
                hlsChunkHolder.playlist = hlsUrl;
                final boolean seenExpectedPlaylistError2 = this.seenExpectedPlaylistError;
                boolean b5 = b;
                if (this.expectedPlaylistUrl == hlsUrl) {
                    b5 = true;
                }
                this.seenExpectedPlaylistError = (seenExpectedPlaylistError2 & b5);
                this.expectedPlaylistUrl = hlsUrl;
            }
            return;
        }
        this.seenExpectedPlaylistError = false;
        DataSpec dataSpec = null;
        this.expectedPlaylistUrl = null;
        final HlsMediaPlaylist.Segment segment = hlsMediaPlaylist.segments.get(n6);
        final String fullSegmentEncryptionKeyUri = segment.fullSegmentEncryptionKeyUri;
        if (fullSegmentEncryptionKeyUri != null) {
            final Uri resolveToUri = UriUtil.resolveToUri(hlsMediaPlaylist.baseUri, fullSegmentEncryptionKeyUri);
            if (!resolveToUri.equals((Object)this.encryptionKeyUri)) {
                hlsChunkHolder.chunk = this.newEncryptionKeyChunk(resolveToUri, segment.encryptionIV, index, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData());
                return;
            }
            if (!Util.areEqual(segment.encryptionIV, this.encryptionIvString)) {
                this.setEncryptionData(resolveToUri, segment.encryptionIV, this.encryptionKey);
            }
        }
        else {
            this.clearEncryptionData();
        }
        final HlsMediaPlaylist.Segment initializationSegment = segment.initializationSegment;
        if (initializationSegment != null) {
            dataSpec = new DataSpec(UriUtil.resolveToUri(hlsMediaPlaylist.baseUri, initializationSegment.url), initializationSegment.byterangeOffset, initializationSegment.byterangeLength, null);
        }
        n2 += segment.relativeStartTimeUs;
        final int n7 = hlsMediaPlaylist.discontinuitySequence + segment.relativeDiscontinuitySequence;
        hlsChunkHolder.chunk = new HlsMediaChunk(this.extractorFactory, this.mediaDataSource, new DataSpec(UriUtil.resolveToUri(hlsMediaPlaylist.baseUri, segment.url), segment.byterangeOffset, segment.byterangeLength, null), dataSpec, hlsUrl, this.muxedCaptionFormats, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), n2, n2 + segment.durationUs, n, n7, segment.hasGapTag, this.isTimestampMaster, this.timestampAdjusterProvider.getAdjuster(n7), hlsMediaChunk, segment.drmInitData, this.encryptionKey, this.encryptionIv);
    }
    
    public TrackGroup getTrackGroup() {
        return this.trackGroup;
    }
    
    public TrackSelection getTrackSelection() {
        return this.trackSelection;
    }
    
    public boolean maybeBlacklistTrack(final Chunk chunk, final long n) {
        final TrackSelection trackSelection = this.trackSelection;
        return trackSelection.blacklist(trackSelection.indexOf(this.trackGroup.indexOf(chunk.trackFormat)), n);
    }
    
    public void maybeThrowError() throws IOException {
        final IOException fatalError = this.fatalError;
        if (fatalError == null) {
            final HlsMasterPlaylist.HlsUrl expectedPlaylistUrl = this.expectedPlaylistUrl;
            if (expectedPlaylistUrl != null && this.seenExpectedPlaylistError) {
                this.playlistTracker.maybeThrowPlaylistRefreshError(expectedPlaylistUrl);
            }
            return;
        }
        throw fatalError;
    }
    
    public void onChunkLoadCompleted(final Chunk chunk) {
        if (chunk instanceof EncryptionKeyChunk) {
            final EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk)chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            this.setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.iv, encryptionKeyChunk.getResult());
        }
    }
    
    public boolean onPlaylistError(final HlsMasterPlaylist.HlsUrl hlsUrl, final long n) {
        final int index = this.trackGroup.indexOf(hlsUrl.format);
        final boolean b = true;
        if (index == -1) {
            return true;
        }
        final int index2 = this.trackSelection.indexOf(index);
        if (index2 == -1) {
            return true;
        }
        this.seenExpectedPlaylistError |= (this.expectedPlaylistUrl == hlsUrl);
        boolean b2 = b;
        if (n != -9223372036854775807L) {
            b2 = (this.trackSelection.blacklist(index2, n) && b);
        }
        return b2;
    }
    
    public void reset() {
        this.fatalError = null;
    }
    
    public void selectTracks(final TrackSelection trackSelection) {
        this.trackSelection = trackSelection;
    }
    
    public void setIsTimestampMaster(final boolean isTimestampMaster) {
        this.isTimestampMaster = isTimestampMaster;
    }
    
    private static final class EncryptionKeyChunk extends DataChunk
    {
        public final String iv;
        private byte[] result;
        
        public EncryptionKeyChunk(final DataSource dataSource, final DataSpec dataSpec, final Format format, final int n, final Object o, final byte[] array, final String iv) {
            super(dataSource, dataSpec, 3, format, n, o, array);
            this.iv = iv;
        }
        
        @Override
        protected void consume(final byte[] original, final int newLength) throws IOException {
            this.result = Arrays.copyOf(original, newLength);
        }
        
        public byte[] getResult() {
            return this.result;
        }
    }
    
    public static final class HlsChunkHolder
    {
        public Chunk chunk;
        public boolean endOfStream;
        public HlsMasterPlaylist.HlsUrl playlist;
        
        public HlsChunkHolder() {
            this.clear();
        }
        
        public void clear() {
            this.chunk = null;
            this.endOfStream = false;
            this.playlist = null;
        }
    }
    
    private static final class HlsMediaPlaylistSegmentIterator extends BaseMediaChunkIterator
    {
        private final HlsMediaPlaylist playlist;
        private final long startOfPlaylistInPeriodUs;
        
        public HlsMediaPlaylistSegmentIterator(final HlsMediaPlaylist playlist, final long startOfPlaylistInPeriodUs, final int n) {
            super(n, playlist.segments.size() - 1);
            this.playlist = playlist;
            this.startOfPlaylistInPeriodUs = startOfPlaylistInPeriodUs;
        }
    }
    
    private static final class InitializationTrackSelection extends BaseTrackSelection
    {
        private int selectedIndex;
        
        public InitializationTrackSelection(final TrackGroup trackGroup, final int[] array) {
            super(trackGroup, array);
            this.selectedIndex = this.indexOf(trackGroup.getFormat(0));
        }
        
        @Override
        public int getSelectedIndex() {
            return this.selectedIndex;
        }
        
        @Override
        public Object getSelectionData() {
            return null;
        }
        
        @Override
        public int getSelectionReason() {
            return 0;
        }
        
        @Override
        public void updateSelectedTrack(long elapsedRealtime, final long n, final long n2, final List<? extends MediaChunk> list, final MediaChunkIterator[] array) {
            elapsedRealtime = SystemClock.elapsedRealtime();
            if (!this.isBlacklisted(this.selectedIndex, elapsedRealtime)) {
                return;
            }
            for (int i = super.length - 1; i >= 0; --i) {
                if (!this.isBlacklisted(i, elapsedRealtime)) {
                    this.selectedIndex = i;
                    return;
                }
            }
            throw new IllegalStateException();
        }
    }
}
