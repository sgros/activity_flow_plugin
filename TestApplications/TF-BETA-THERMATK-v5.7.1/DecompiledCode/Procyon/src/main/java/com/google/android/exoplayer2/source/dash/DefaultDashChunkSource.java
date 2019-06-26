// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.source.chunk.BaseMediaChunkIterator;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import java.util.Collections;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import com.google.android.exoplayer2.extractor.rawcc.RawCcExtractor;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.source.chunk.ContainerMediaChunk;
import com.google.android.exoplayer2.source.chunk.SingleSampleMediaChunk;
import com.google.android.exoplayer2.source.chunk.InitializationChunk;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.chunk.ChunkHolder;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import java.util.List;
import java.util.Collection;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import android.os.SystemClock;
import java.util.ArrayList;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import java.io.IOException;
import com.google.android.exoplayer2.upstream.DataSource;

public class DefaultDashChunkSource implements DashChunkSource
{
    private final int[] adaptationSetIndices;
    private final DataSource dataSource;
    private final long elapsedRealtimeOffsetMs;
    private IOException fatalError;
    private long liveEdgeTimeUs;
    private DashManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final int maxSegmentsPerLoad;
    private boolean missingLastSegment;
    private int periodIndex;
    private final PlayerEmsgHandler.PlayerTrackEmsgHandler playerTrackEmsgHandler;
    protected final RepresentationHolder[] representationHolders;
    private final TrackSelection trackSelection;
    private final int trackType;
    
    public DefaultDashChunkSource(final LoaderErrorThrower manifestLoaderErrorThrower, final DashManifest manifest, int i, final int[] adaptationSetIndices, final TrackSelection trackSelection, final int trackType, final DataSource dataSource, long periodDurationUs, final int maxSegmentsPerLoad, final boolean b, final boolean b2, final PlayerEmsgHandler.PlayerTrackEmsgHandler playerTrackEmsgHandler) {
        this.manifestLoaderErrorThrower = manifestLoaderErrorThrower;
        this.manifest = manifest;
        this.adaptationSetIndices = adaptationSetIndices;
        this.trackSelection = trackSelection;
        this.trackType = trackType;
        this.dataSource = dataSource;
        this.periodIndex = i;
        this.elapsedRealtimeOffsetMs = periodDurationUs;
        this.maxSegmentsPerLoad = maxSegmentsPerLoad;
        this.playerTrackEmsgHandler = playerTrackEmsgHandler;
        periodDurationUs = manifest.getPeriodDurationUs(i);
        this.liveEdgeTimeUs = -9223372036854775807L;
        final ArrayList<Representation> representations = this.getRepresentations();
        this.representationHolders = new RepresentationHolder[trackSelection.length()];
        for (i = 0; i < this.representationHolders.length; ++i) {
            this.representationHolders[i] = new RepresentationHolder(periodDurationUs, trackType, (Representation)representations.get(trackSelection.getIndexInTrackGroup(i)), b, b2, playerTrackEmsgHandler);
        }
    }
    
    private long getNowUnixTimeUs() {
        long currentTimeMillis;
        if (this.elapsedRealtimeOffsetMs != 0L) {
            currentTimeMillis = SystemClock.elapsedRealtime() + this.elapsedRealtimeOffsetMs;
        }
        else {
            currentTimeMillis = System.currentTimeMillis();
        }
        return currentTimeMillis * 1000L;
    }
    
    private ArrayList<Representation> getRepresentations() {
        final List<AdaptationSet> adaptationSets = this.manifest.getPeriod(this.periodIndex).adaptationSets;
        final ArrayList<Representation> list = new ArrayList<Representation>();
        final int[] adaptationSetIndices = this.adaptationSetIndices;
        for (int length = adaptationSetIndices.length, i = 0; i < length; ++i) {
            list.addAll(adaptationSets.get(adaptationSetIndices[i]).representations);
        }
        return list;
    }
    
    private long getSegmentNum(final RepresentationHolder representationHolder, final MediaChunk mediaChunk, long n, final long n2, final long n3) {
        if (mediaChunk != null) {
            n = mediaChunk.getNextChunkIndex();
        }
        else {
            n = Util.constrainValue(representationHolder.getSegmentNum(n), n2, n3);
        }
        return n;
    }
    
    private long resolveTimeToLiveEdgeUs(long n) {
        if (this.manifest.dynamic && this.liveEdgeTimeUs != -9223372036854775807L) {
            n = this.liveEdgeTimeUs - n;
        }
        else {
            n = -9223372036854775807L;
        }
        return n;
    }
    
    private void updateLiveEdgeTimeUs(final RepresentationHolder representationHolder, long segmentEndTimeUs) {
        if (this.manifest.dynamic) {
            segmentEndTimeUs = representationHolder.getSegmentEndTimeUs(segmentEndTimeUs);
        }
        else {
            segmentEndTimeUs = -9223372036854775807L;
        }
        this.liveEdgeTimeUs = segmentEndTimeUs;
    }
    
    @Override
    public long getAdjustedSeekPositionUs(final long n, final SeekParameters seekParameters) {
        for (final RepresentationHolder representationHolder : this.representationHolders) {
            if (representationHolder.segmentIndex != null) {
                final long segmentNum = representationHolder.getSegmentNum(n);
                final long segmentStartTimeUs = representationHolder.getSegmentStartTimeUs(segmentNum);
                long segmentStartTimeUs2;
                if (segmentStartTimeUs < n && segmentNum < representationHolder.getSegmentCount() - 1) {
                    segmentStartTimeUs2 = representationHolder.getSegmentStartTimeUs(segmentNum + 1L);
                }
                else {
                    segmentStartTimeUs2 = segmentStartTimeUs;
                }
                return Util.resolveSeekPositionUs(n, seekParameters, segmentStartTimeUs, segmentStartTimeUs2);
            }
        }
        return n;
    }
    
    @Override
    public void getNextChunk(long n, final long n2, final List<? extends MediaChunk> list, final ChunkHolder chunkHolder) {
        if (this.fatalError != null) {
            return;
        }
        final long resolveTimeToLiveEdgeUs = this.resolveTimeToLiveEdgeUs(n);
        final long msToUs = C.msToUs(this.manifest.availabilityStartTimeMs);
        final long msToUs2 = C.msToUs(this.manifest.getPeriod(this.periodIndex).startMs);
        final PlayerEmsgHandler.PlayerTrackEmsgHandler playerTrackEmsgHandler = this.playerTrackEmsgHandler;
        if (playerTrackEmsgHandler != null && playerTrackEmsgHandler.maybeRefreshManifestBeforeLoadingNextChunk(msToUs + msToUs2 + n2)) {
            return;
        }
        final long nowUnixTimeUs = this.getNowUnixTimeUs();
        MediaChunk mediaChunk;
        if (list.isEmpty()) {
            mediaChunk = null;
        }
        else {
            mediaChunk = (MediaChunk)list.get(list.size() - 1);
        }
        final MediaChunkIterator[] array = new MediaChunkIterator[this.trackSelection.length()];
        for (int i = 0; i < array.length; ++i) {
            final RepresentationHolder representationHolder = this.representationHolders[i];
            if (representationHolder.segmentIndex == null) {
                array[i] = MediaChunkIterator.EMPTY;
            }
            else {
                final long firstAvailableSegmentNum = representationHolder.getFirstAvailableSegmentNum(this.manifest, this.periodIndex, nowUnixTimeUs);
                final long lastAvailableSegmentNum = representationHolder.getLastAvailableSegmentNum(this.manifest, this.periodIndex, nowUnixTimeUs);
                final MediaChunkIterator[] array2 = array;
                final int n3 = i;
                final long segmentNum = this.getSegmentNum(representationHolder, mediaChunk, n2, firstAvailableSegmentNum, lastAvailableSegmentNum);
                if (segmentNum < firstAvailableSegmentNum) {
                    array2[n3] = MediaChunkIterator.EMPTY;
                }
                else {
                    array2[n3] = new RepresentationSegmentIterator(representationHolder, segmentNum, lastAvailableSegmentNum);
                }
            }
        }
        this.trackSelection.updateSelectedTrack(n, n2 - n, resolveTimeToLiveEdgeUs, list, array);
        final RepresentationHolder representationHolder2 = this.representationHolders[this.trackSelection.getSelectedIndex()];
        final ChunkExtractorWrapper extractorWrapper = representationHolder2.extractorWrapper;
        if (extractorWrapper != null) {
            final Representation representation = representationHolder2.representation;
            RangedUri initializationUri;
            if (extractorWrapper.getSampleFormats() == null) {
                initializationUri = representation.getInitializationUri();
            }
            else {
                initializationUri = null;
            }
            RangedUri indexUri;
            if (representationHolder2.segmentIndex == null) {
                indexUri = representation.getIndexUri();
            }
            else {
                indexUri = null;
            }
            if (initializationUri != null || indexUri != null) {
                chunkHolder.chunk = this.newInitializationChunk(representationHolder2, this.dataSource, this.trackSelection.getSelectedFormat(), this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), initializationUri, indexUri);
                return;
            }
        }
        final long access$000 = representationHolder2.periodDurationUs;
        n = -9223372036854775807L;
        final boolean b = access$000 != -9223372036854775807L;
        if (representationHolder2.getSegmentCount() == 0) {
            chunkHolder.endOfStream = b;
            return;
        }
        final long firstAvailableSegmentNum2 = representationHolder2.getFirstAvailableSegmentNum(this.manifest, this.periodIndex, nowUnixTimeUs);
        final long lastAvailableSegmentNum2 = representationHolder2.getLastAvailableSegmentNum(this.manifest, this.periodIndex, nowUnixTimeUs);
        this.updateLiveEdgeTimeUs(representationHolder2, lastAvailableSegmentNum2);
        final long segmentNum2 = this.getSegmentNum(representationHolder2, mediaChunk, n2, firstAvailableSegmentNum2, lastAvailableSegmentNum2);
        if (segmentNum2 < firstAvailableSegmentNum2) {
            this.fatalError = new BehindLiveWindowException();
            return;
        }
        if (segmentNum2 > lastAvailableSegmentNum2 || (this.missingLastSegment && segmentNum2 >= lastAvailableSegmentNum2)) {
            chunkHolder.endOfStream = b;
            return;
        }
        if (b && representationHolder2.getSegmentStartTimeUs(segmentNum2) >= access$000) {
            chunkHolder.endOfStream = true;
            return;
        }
        int n5;
        int n4 = n5 = (int)Math.min(this.maxSegmentsPerLoad, lastAvailableSegmentNum2 - segmentNum2 + 1L);
        if (access$000 != -9223372036854775807L) {
            while ((n5 = n4) > 1) {
                n5 = n4;
                if (representationHolder2.getSegmentStartTimeUs(n4 + segmentNum2 - 1L) < access$000) {
                    break;
                }
                --n4;
            }
        }
        if (list.isEmpty()) {
            n = n2;
        }
        chunkHolder.chunk = this.newMediaChunk(representationHolder2, this.dataSource, this.trackType, this.trackSelection.getSelectedFormat(), this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), segmentNum2, n5, n);
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
    
    protected Chunk newInitializationChunk(final RepresentationHolder representationHolder, final DataSource dataSource, final Format format, final int n, final Object o, final RangedUri rangedUri, RangedUri attemptMerge) {
        final String baseUrl = representationHolder.representation.baseUrl;
        RangedUri rangedUri2 = attemptMerge;
        if (rangedUri != null) {
            attemptMerge = rangedUri.attemptMerge(attemptMerge, baseUrl);
            if ((rangedUri2 = attemptMerge) == null) {
                rangedUri2 = rangedUri;
            }
        }
        return new InitializationChunk(dataSource, new DataSpec(rangedUri2.resolveUri(baseUrl), rangedUri2.start, rangedUri2.length, representationHolder.representation.getCacheKey()), format, n, o, representationHolder.extractorWrapper);
    }
    
    protected Chunk newMediaChunk(final RepresentationHolder representationHolder, final DataSource dataSource, int n, final Format format, final int n2, final Object o, final long n3, final int n4, long segmentEndTimeUs) {
        final Representation representation = representationHolder.representation;
        final long segmentStartTimeUs = representationHolder.getSegmentStartTimeUs(n3);
        RangedUri segmentUrl = representationHolder.getSegmentUrl(n3);
        final String baseUrl = representation.baseUrl;
        if (representationHolder.extractorWrapper == null) {
            segmentEndTimeUs = representationHolder.getSegmentEndTimeUs(n3);
            return new SingleSampleMediaChunk(dataSource, new DataSpec(segmentUrl.resolveUri(baseUrl), segmentUrl.start, segmentUrl.length, representation.getCacheKey()), format, n2, o, segmentStartTimeUs, segmentEndTimeUs, n3, n, format);
        }
        int i = 1;
        n = 1;
        while (i < n4) {
            final RangedUri attemptMerge = segmentUrl.attemptMerge(representationHolder.getSegmentUrl(i + n3), baseUrl);
            if (attemptMerge == null) {
                break;
            }
            ++n;
            ++i;
            segmentUrl = attemptMerge;
        }
        final long segmentEndTimeUs2 = representationHolder.getSegmentEndTimeUs(n + n3 - 1L);
        long access$000 = representationHolder.periodDurationUs;
        if (access$000 == -9223372036854775807L || access$000 > segmentEndTimeUs2) {
            access$000 = -9223372036854775807L;
        }
        return new ContainerMediaChunk(dataSource, new DataSpec(segmentUrl.resolveUri(baseUrl), segmentUrl.start, segmentUrl.length, representation.getCacheKey()), format, n2, o, segmentStartTimeUs, segmentEndTimeUs2, segmentEndTimeUs, access$000, n3, n, -representation.presentationTimeOffsetUs, representationHolder.extractorWrapper);
    }
    
    @Override
    public void onChunkLoadCompleted(final Chunk chunk) {
        if (chunk instanceof InitializationChunk) {
            final int index = this.trackSelection.indexOf(((InitializationChunk)chunk).trackFormat);
            final RepresentationHolder representationHolder = this.representationHolders[index];
            if (representationHolder.segmentIndex == null) {
                final SeekMap seekMap = representationHolder.extractorWrapper.getSeekMap();
                if (seekMap != null) {
                    this.representationHolders[index] = representationHolder.copyWithNewSegmentIndex(new DashWrappingSegmentIndex((ChunkIndex)seekMap, representationHolder.representation.presentationTimeOffsetUs));
                }
            }
        }
        final PlayerEmsgHandler.PlayerTrackEmsgHandler playerTrackEmsgHandler = this.playerTrackEmsgHandler;
        if (playerTrackEmsgHandler != null) {
            playerTrackEmsgHandler.onChunkLoadCompleted(chunk);
        }
    }
    
    @Override
    public boolean onChunkLoadError(final Chunk chunk, final boolean b, final Exception ex, final long n) {
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final PlayerEmsgHandler.PlayerTrackEmsgHandler playerTrackEmsgHandler = this.playerTrackEmsgHandler;
        if (playerTrackEmsgHandler != null && playerTrackEmsgHandler.maybeRefreshManifestOnLoadingError(chunk)) {
            return true;
        }
        if (!this.manifest.dynamic && chunk instanceof MediaChunk && ex instanceof HttpDataSource.InvalidResponseCodeException && ((HttpDataSource.InvalidResponseCodeException)ex).responseCode == 404) {
            final RepresentationHolder representationHolder = this.representationHolders[this.trackSelection.indexOf(chunk.trackFormat)];
            final int segmentCount = representationHolder.getSegmentCount();
            if (segmentCount != -1 && segmentCount != 0 && ((MediaChunk)chunk).getNextChunkIndex() > representationHolder.getFirstSegmentNum() + segmentCount - 1L) {
                return this.missingLastSegment = true;
            }
        }
        boolean b3 = b2;
        if (n != -9223372036854775807L) {
            final TrackSelection trackSelection = this.trackSelection;
            b3 = b2;
            if (trackSelection.blacklist(trackSelection.indexOf(chunk.trackFormat), n)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    public void updateManifest(final DashManifest manifest, int i) {
        try {
            this.manifest = manifest;
            this.periodIndex = i;
            final long periodDurationUs = this.manifest.getPeriodDurationUs(this.periodIndex);
            final ArrayList<Representation> representations = this.getRepresentations();
            for (i = 0; i < this.representationHolders.length; ++i) {
                this.representationHolders[i] = this.representationHolders[i].copyWithNewRepresentation(periodDurationUs, (Representation)representations.get(this.trackSelection.getIndexInTrackGroup(i)));
            }
        }
        catch (BehindLiveWindowException fatalError) {
            this.fatalError = fatalError;
        }
    }
    
    public static final class Factory implements DashChunkSource.Factory
    {
        private final DataSource.Factory dataSourceFactory;
        private final int maxSegmentsPerLoad;
        
        public Factory(final DataSource.Factory factory) {
            this(factory, 1);
        }
        
        public Factory(final DataSource.Factory dataSourceFactory, final int maxSegmentsPerLoad) {
            this.dataSourceFactory = dataSourceFactory;
            this.maxSegmentsPerLoad = maxSegmentsPerLoad;
        }
        
        @Override
        public DashChunkSource createDashChunkSource(final LoaderErrorThrower loaderErrorThrower, final DashManifest dashManifest, final int n, final int[] array, final TrackSelection trackSelection, final int n2, final long n3, final boolean b, final boolean b2, final PlayerEmsgHandler.PlayerTrackEmsgHandler playerTrackEmsgHandler, final TransferListener transferListener) {
            final DataSource dataSource = this.dataSourceFactory.createDataSource();
            if (transferListener != null) {
                dataSource.addTransferListener(transferListener);
            }
            return new DefaultDashChunkSource(loaderErrorThrower, dashManifest, n, array, trackSelection, n2, dataSource, n3, this.maxSegmentsPerLoad, b, b2, playerTrackEmsgHandler);
        }
    }
    
    protected static final class RepresentationHolder
    {
        final ChunkExtractorWrapper extractorWrapper;
        private final long periodDurationUs;
        public final Representation representation;
        public final DashSegmentIndex segmentIndex;
        private final long segmentNumShift;
        
        RepresentationHolder(final long n, final int n2, final Representation representation, final boolean b, final boolean b2, final TrackOutput trackOutput) {
            this(n, representation, createExtractorWrapper(n2, representation, b, b2, trackOutput), 0L, representation.getIndex());
        }
        
        private RepresentationHolder(final long periodDurationUs, final Representation representation, final ChunkExtractorWrapper extractorWrapper, final long segmentNumShift, final DashSegmentIndex segmentIndex) {
            this.periodDurationUs = periodDurationUs;
            this.representation = representation;
            this.segmentNumShift = segmentNumShift;
            this.extractorWrapper = extractorWrapper;
            this.segmentIndex = segmentIndex;
        }
        
        private static ChunkExtractorWrapper createExtractorWrapper(final int n, final Representation representation, final boolean b, final boolean b2, final TrackOutput trackOutput) {
            final String containerMimeType = representation.format.containerMimeType;
            if (mimeTypeIsRawText(containerMimeType)) {
                return null;
            }
            Extractor extractor;
            if ("application/x-rawcc".equals(containerMimeType)) {
                extractor = new RawCcExtractor(representation.format);
            }
            else if (mimeTypeIsWebm(containerMimeType)) {
                extractor = new MatroskaExtractor(1);
            }
            else {
                int n2;
                if (b) {
                    n2 = 4;
                }
                else {
                    n2 = 0;
                }
                List<Format> list;
                if (b2) {
                    list = Collections.singletonList(Format.createTextSampleFormat(null, "application/cea-608", 0, null));
                }
                else {
                    list = Collections.emptyList();
                }
                extractor = new FragmentedMp4Extractor(n2, null, null, null, list, trackOutput);
            }
            return new ChunkExtractorWrapper(extractor, n, representation.format);
        }
        
        private static boolean mimeTypeIsRawText(final String anObject) {
            return MimeTypes.isText(anObject) || "application/ttml+xml".equals(anObject);
        }
        
        private static boolean mimeTypeIsWebm(final String s) {
            return s.startsWith("video/webm") || s.startsWith("audio/webm") || s.startsWith("application/webm");
        }
        
        RepresentationHolder copyWithNewRepresentation(final long n, final Representation representation) throws BehindLiveWindowException {
            final DashSegmentIndex index = this.representation.getIndex();
            final DashSegmentIndex index2 = representation.getIndex();
            if (index == null) {
                return new RepresentationHolder(n, representation, this.extractorWrapper, this.segmentNumShift, index);
            }
            if (!index.isExplicit()) {
                return new RepresentationHolder(n, representation, this.extractorWrapper, this.segmentNumShift, index2);
            }
            final int segmentCount = index.getSegmentCount(n);
            if (segmentCount == 0) {
                return new RepresentationHolder(n, representation, this.extractorWrapper, this.segmentNumShift, index2);
            }
            final long n2 = index.getFirstSegmentNum() + segmentCount - 1L;
            final long n3 = index.getTimeUs(n2) + index.getDurationUs(n2, n);
            final long firstSegmentNum = index2.getFirstSegmentNum();
            final long timeUs = index2.getTimeUs(firstSegmentNum);
            final long segmentNumShift = this.segmentNumShift;
            long segmentNum;
            if (n3 == timeUs) {
                segmentNum = n2 + 1L;
            }
            else {
                if (n3 < timeUs) {
                    throw new BehindLiveWindowException();
                }
                segmentNum = index.getSegmentNum(timeUs, n);
            }
            return new RepresentationHolder(n, representation, this.extractorWrapper, segmentNumShift + (segmentNum - firstSegmentNum), index2);
        }
        
        RepresentationHolder copyWithNewSegmentIndex(final DashSegmentIndex dashSegmentIndex) {
            return new RepresentationHolder(this.periodDurationUs, this.representation, this.extractorWrapper, this.segmentNumShift, dashSegmentIndex);
        }
        
        public long getFirstAvailableSegmentNum(final DashManifest dashManifest, final int n, final long n2) {
            if (this.getSegmentCount() == -1 && dashManifest.timeShiftBufferDepthMs != -9223372036854775807L) {
                return Math.max(this.getFirstSegmentNum(), this.getSegmentNum(n2 - C.msToUs(dashManifest.availabilityStartTimeMs) - C.msToUs(dashManifest.getPeriod(n).startMs) - C.msToUs(dashManifest.timeShiftBufferDepthMs)));
            }
            return this.getFirstSegmentNum();
        }
        
        public long getFirstSegmentNum() {
            return this.segmentIndex.getFirstSegmentNum() + this.segmentNumShift;
        }
        
        public long getLastAvailableSegmentNum(final DashManifest dashManifest, final int n, long segmentNum) {
            final int segmentCount = this.getSegmentCount();
            if (segmentCount == -1) {
                segmentNum = this.getSegmentNum(segmentNum - C.msToUs(dashManifest.availabilityStartTimeMs) - C.msToUs(dashManifest.getPeriod(n).startMs));
            }
            else {
                segmentNum = this.getFirstSegmentNum() + segmentCount;
            }
            return segmentNum - 1L;
        }
        
        public int getSegmentCount() {
            return this.segmentIndex.getSegmentCount(this.periodDurationUs);
        }
        
        public long getSegmentEndTimeUs(final long n) {
            return this.getSegmentStartTimeUs(n) + this.segmentIndex.getDurationUs(n - this.segmentNumShift, this.periodDurationUs);
        }
        
        public long getSegmentNum(final long n) {
            return this.segmentIndex.getSegmentNum(n, this.periodDurationUs) + this.segmentNumShift;
        }
        
        public long getSegmentStartTimeUs(final long n) {
            return this.segmentIndex.getTimeUs(n - this.segmentNumShift);
        }
        
        public RangedUri getSegmentUrl(final long n) {
            return this.segmentIndex.getSegmentUrl(n - this.segmentNumShift);
        }
    }
    
    protected static final class RepresentationSegmentIterator extends BaseMediaChunkIterator
    {
        private final RepresentationHolder representationHolder;
        
        public RepresentationSegmentIterator(final RepresentationHolder representationHolder, final long n, final long n2) {
            super(n, n2);
            this.representationHolder = representationHolder;
        }
    }
}
