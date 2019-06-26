package com.google.android.exoplayer2.source.dash;

import android.os.SystemClock;
import com.google.android.exoplayer2.C0131C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.rawcc.RawCcExtractor;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.chunk.BaseMediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.source.chunk.ChunkHolder;
import com.google.android.exoplayer2.source.chunk.ContainerMediaChunk;
import com.google.android.exoplayer2.source.chunk.InitializationChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.SingleSampleMediaChunk;
import com.google.android.exoplayer2.source.dash.PlayerEmsgHandler.PlayerTrackEmsgHandler;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultDashChunkSource implements DashChunkSource {
    private final int[] adaptationSetIndices;
    private final DataSource dataSource;
    private final long elapsedRealtimeOffsetMs;
    private IOException fatalError;
    private long liveEdgeTimeUs = -9223372036854775807L;
    private DashManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final int maxSegmentsPerLoad;
    private boolean missingLastSegment;
    private int periodIndex;
    private final PlayerTrackEmsgHandler playerTrackEmsgHandler;
    protected final RepresentationHolder[] representationHolders;
    private final TrackSelection trackSelection;
    private final int trackType;

    protected static final class RepresentationHolder {
        final ChunkExtractorWrapper extractorWrapper;
        private final long periodDurationUs;
        public final Representation representation;
        public final DashSegmentIndex segmentIndex;
        private final long segmentNumShift;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:23:0x008f in {3, 7, 11, 14, 18, 20, 22} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        com.google.android.exoplayer2.source.dash.DefaultDashChunkSource.RepresentationHolder copyWithNewRepresentation(long r19, com.google.android.exoplayer2.source.dash.manifest.Representation r21) throws com.google.android.exoplayer2.source.BehindLiveWindowException {
            /*
            r18 = this;
            r0 = r18;
            r2 = r19;
            r1 = r0.representation;
            r8 = r1.getIndex();
            r9 = r21.getIndex();
            if (r8 != 0) goto L_0x001f;
            r9 = new com.google.android.exoplayer2.source.dash.DefaultDashChunkSource$RepresentationHolder;
            r5 = r0.extractorWrapper;
            r6 = r0.segmentNumShift;
            r1 = r9;
            r2 = r19;
            r4 = r21;
            r1.<init>(r2, r4, r5, r6, r8);
            return r9;
            r1 = r8.isExplicit();
            if (r1 != 0) goto L_0x0035;
            r10 = new com.google.android.exoplayer2.source.dash.DefaultDashChunkSource$RepresentationHolder;
            r5 = r0.extractorWrapper;
            r6 = r0.segmentNumShift;
            r1 = r10;
            r2 = r19;
            r4 = r21;
            r8 = r9;
            r1.<init>(r2, r4, r5, r6, r8);
            return r10;
            r1 = r8.getSegmentCount(r2);
            if (r1 != 0) goto L_0x004b;
            r10 = new com.google.android.exoplayer2.source.dash.DefaultDashChunkSource$RepresentationHolder;
            r5 = r0.extractorWrapper;
            r6 = r0.segmentNumShift;
            r1 = r10;
            r2 = r19;
            r4 = r21;
            r8 = r9;
            r1.<init>(r2, r4, r5, r6, r8);
            return r10;
            r4 = r8.getFirstSegmentNum();
            r6 = (long) r1;
            r4 = r4 + r6;
            r6 = 1;
            r4 = r4 - r6;
            r10 = r8.getTimeUs(r4);
            r12 = r8.getDurationUs(r4, r2);
            r10 = r10 + r12;
            r12 = r9.getFirstSegmentNum();
            r14 = r9.getTimeUs(r12);
            r6 = r0.segmentNumShift;
            r1 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1));
            if (r1 != 0) goto L_0x0072;
            r16 = 1;
            r4 = r4 + r16;
            r4 = r4 - r12;
            r6 = r6 + r4;
            goto L_0x007b;
            r1 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1));
            if (r1 < 0) goto L_0x0089;
            r4 = r8.getSegmentNum(r14, r2);
            goto L_0x006f;
            r10 = new com.google.android.exoplayer2.source.dash.DefaultDashChunkSource$RepresentationHolder;
            r5 = r0.extractorWrapper;
            r1 = r10;
            r2 = r19;
            r4 = r21;
            r8 = r9;
            r1.<init>(r2, r4, r5, r6, r8);
            return r10;
            r1 = new com.google.android.exoplayer2.source.BehindLiveWindowException;
            r1.<init>();
            throw r1;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.DefaultDashChunkSource$RepresentationHolder.copyWithNewRepresentation(long, com.google.android.exoplayer2.source.dash.manifest.Representation):com.google.android.exoplayer2.source.dash.DefaultDashChunkSource$RepresentationHolder");
        }

        RepresentationHolder(long j, int i, Representation representation, boolean z, boolean z2, TrackOutput trackOutput) {
            this(j, representation, createExtractorWrapper(i, representation, z, z2, trackOutput), 0, representation.getIndex());
        }

        private RepresentationHolder(long j, Representation representation, ChunkExtractorWrapper chunkExtractorWrapper, long j2, DashSegmentIndex dashSegmentIndex) {
            this.periodDurationUs = j;
            this.representation = representation;
            this.segmentNumShift = j2;
            this.extractorWrapper = chunkExtractorWrapper;
            this.segmentIndex = dashSegmentIndex;
        }

        /* Access modifiers changed, original: 0000 */
        public RepresentationHolder copyWithNewSegmentIndex(DashSegmentIndex dashSegmentIndex) {
            return new RepresentationHolder(this.periodDurationUs, this.representation, this.extractorWrapper, this.segmentNumShift, dashSegmentIndex);
        }

        public long getFirstSegmentNum() {
            return this.segmentIndex.getFirstSegmentNum() + this.segmentNumShift;
        }

        public int getSegmentCount() {
            return this.segmentIndex.getSegmentCount(this.periodDurationUs);
        }

        public long getSegmentStartTimeUs(long j) {
            return this.segmentIndex.getTimeUs(j - this.segmentNumShift);
        }

        public long getSegmentEndTimeUs(long j) {
            return getSegmentStartTimeUs(j) + this.segmentIndex.getDurationUs(j - this.segmentNumShift, this.periodDurationUs);
        }

        public long getSegmentNum(long j) {
            return this.segmentIndex.getSegmentNum(j, this.periodDurationUs) + this.segmentNumShift;
        }

        public RangedUri getSegmentUrl(long j) {
            return this.segmentIndex.getSegmentUrl(j - this.segmentNumShift);
        }

        public long getFirstAvailableSegmentNum(DashManifest dashManifest, int i, long j) {
            if (getSegmentCount() != -1 || dashManifest.timeShiftBufferDepthMs == -9223372036854775807L) {
                return getFirstSegmentNum();
            }
            return Math.max(getFirstSegmentNum(), getSegmentNum(((j - C0131C.msToUs(dashManifest.availabilityStartTimeMs)) - C0131C.msToUs(dashManifest.getPeriod(i).startMs)) - C0131C.msToUs(dashManifest.timeShiftBufferDepthMs)));
        }

        public long getLastAvailableSegmentNum(DashManifest dashManifest, int i, long j) {
            long segmentNum;
            int segmentCount = getSegmentCount();
            if (segmentCount == -1) {
                segmentNum = getSegmentNum((j - C0131C.msToUs(dashManifest.availabilityStartTimeMs)) - C0131C.msToUs(dashManifest.getPeriod(i).startMs));
            } else {
                segmentNum = getFirstSegmentNum() + ((long) segmentCount);
            }
            return segmentNum - 1;
        }

        private static boolean mimeTypeIsWebm(String str) {
            return str.startsWith(MimeTypes.VIDEO_WEBM) || str.startsWith(MimeTypes.AUDIO_WEBM) || str.startsWith(MimeTypes.APPLICATION_WEBM);
        }

        private static boolean mimeTypeIsRawText(String str) {
            return MimeTypes.isText(str) || MimeTypes.APPLICATION_TTML.equals(str);
        }

        private static ChunkExtractorWrapper createExtractorWrapper(int i, Representation representation, boolean z, boolean z2, TrackOutput trackOutput) {
            String str = representation.format.containerMimeType;
            if (mimeTypeIsRawText(str)) {
                return null;
            }
            Extractor rawCcExtractor;
            if (MimeTypes.APPLICATION_RAWCC.equals(str)) {
                rawCcExtractor = new RawCcExtractor(representation.format);
            } else if (mimeTypeIsWebm(str)) {
                rawCcExtractor = new MatroskaExtractor(1);
            } else {
                List singletonList;
                int i2 = z ? 4 : 0;
                if (z2) {
                    singletonList = Collections.singletonList(Format.createTextSampleFormat(null, MimeTypes.APPLICATION_CEA608, 0, null));
                } else {
                    singletonList = Collections.emptyList();
                }
                Extractor fragmentedMp4Extractor = new FragmentedMp4Extractor(i2, null, null, null, singletonList, trackOutput);
            }
            return new ChunkExtractorWrapper(rawCcExtractor, i, representation.format);
        }
    }

    public static final class Factory implements com.google.android.exoplayer2.source.dash.DashChunkSource.Factory {
        private final com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory;
        private final int maxSegmentsPerLoad;

        public Factory(com.google.android.exoplayer2.upstream.DataSource.Factory factory) {
            this(factory, 1);
        }

        public Factory(com.google.android.exoplayer2.upstream.DataSource.Factory factory, int i) {
            this.dataSourceFactory = factory;
            this.maxSegmentsPerLoad = i;
        }

        public DashChunkSource createDashChunkSource(LoaderErrorThrower loaderErrorThrower, DashManifest dashManifest, int i, int[] iArr, TrackSelection trackSelection, int i2, long j, boolean z, boolean z2, PlayerTrackEmsgHandler playerTrackEmsgHandler, TransferListener transferListener) {
            TransferListener transferListener2 = transferListener;
            DataSource createDataSource = this.dataSourceFactory.createDataSource();
            if (transferListener2 != null) {
                createDataSource.addTransferListener(transferListener2);
            }
            return new DefaultDashChunkSource(loaderErrorThrower, dashManifest, i, iArr, trackSelection, i2, createDataSource, j, this.maxSegmentsPerLoad, z, z2, playerTrackEmsgHandler);
        }
    }

    protected static final class RepresentationSegmentIterator extends BaseMediaChunkIterator {
        private final RepresentationHolder representationHolder;

        public RepresentationSegmentIterator(RepresentationHolder representationHolder, long j, long j2) {
            super(j, j2);
            this.representationHolder = representationHolder;
        }
    }

    public DefaultDashChunkSource(LoaderErrorThrower loaderErrorThrower, DashManifest dashManifest, int i, int[] iArr, TrackSelection trackSelection, int i2, DataSource dataSource, long j, int i3, boolean z, boolean z2, PlayerTrackEmsgHandler playerTrackEmsgHandler) {
        TrackSelection trackSelection2 = trackSelection;
        this.manifestLoaderErrorThrower = loaderErrorThrower;
        this.manifest = dashManifest;
        this.adaptationSetIndices = iArr;
        this.trackSelection = trackSelection2;
        this.trackType = i2;
        this.dataSource = dataSource;
        this.periodIndex = i;
        this.elapsedRealtimeOffsetMs = j;
        this.maxSegmentsPerLoad = i3;
        this.playerTrackEmsgHandler = playerTrackEmsgHandler;
        long periodDurationUs = dashManifest.getPeriodDurationUs(i);
        ArrayList representations = getRepresentations();
        this.representationHolders = new RepresentationHolder[trackSelection.length()];
        for (int i4 = 0; i4 < this.representationHolders.length; i4++) {
            this.representationHolders[i4] = new RepresentationHolder(periodDurationUs, i2, (Representation) representations.get(trackSelection2.getIndexInTrackGroup(i4)), z, z2, playerTrackEmsgHandler);
        }
    }

    public long getAdjustedSeekPositionUs(long j, SeekParameters seekParameters) {
        for (RepresentationHolder representationHolder : this.representationHolders) {
            if (representationHolder.segmentIndex != null) {
                long segmentNum = representationHolder.getSegmentNum(j);
                long segmentStartTimeUs = representationHolder.getSegmentStartTimeUs(segmentNum);
                long segmentStartTimeUs2 = (segmentStartTimeUs >= j || segmentNum >= ((long) (representationHolder.getSegmentCount() - 1))) ? segmentStartTimeUs : representationHolder.getSegmentStartTimeUs(segmentNum + 1);
                return Util.resolveSeekPositionUs(j, seekParameters, segmentStartTimeUs, segmentStartTimeUs2);
            }
        }
        return j;
    }

    public void updateManifest(DashManifest dashManifest, int i) {
        try {
            this.manifest = dashManifest;
            this.periodIndex = i;
            long periodDurationUs = this.manifest.getPeriodDurationUs(this.periodIndex);
            ArrayList representations = getRepresentations();
            for (int i2 = 0; i2 < this.representationHolders.length; i2++) {
                this.representationHolders[i2] = this.representationHolders[i2].copyWithNewRepresentation(periodDurationUs, (Representation) representations.get(this.trackSelection.getIndexInTrackGroup(i2)));
            }
        } catch (BehindLiveWindowException e) {
            this.fatalError = e;
        }
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

    public void getNextChunk(long j, long j2, List<? extends MediaChunk> list, ChunkHolder chunkHolder) {
        ChunkHolder chunkHolder2 = chunkHolder;
        if (this.fatalError == null) {
            long j3 = j2 - j;
            long resolveTimeToLiveEdgeUs = resolveTimeToLiveEdgeUs(j);
            long msToUs = (C0131C.msToUs(this.manifest.availabilityStartTimeMs) + C0131C.msToUs(this.manifest.getPeriod(this.periodIndex).startMs)) + j2;
            PlayerTrackEmsgHandler playerTrackEmsgHandler = this.playerTrackEmsgHandler;
            if (playerTrackEmsgHandler == null || !playerTrackEmsgHandler.maybeRefreshManifestBeforeLoadingNextChunk(msToUs)) {
                List<? extends MediaChunk> list2;
                MediaChunk mediaChunk;
                long j4;
                long firstAvailableSegmentNum;
                long nowUnixTimeUs = getNowUnixTimeUs();
                if (list.isEmpty()) {
                    list2 = list;
                    mediaChunk = null;
                } else {
                    mediaChunk = (MediaChunk) list.get(list.size() - 1);
                }
                MediaChunkIterator[] mediaChunkIteratorArr = new MediaChunkIterator[this.trackSelection.length()];
                int i = 0;
                while (i < mediaChunkIteratorArr.length) {
                    MediaChunkIterator[] mediaChunkIteratorArr2;
                    int i2;
                    RepresentationHolder representationHolder = this.representationHolders[i];
                    if (representationHolder.segmentIndex == null) {
                        mediaChunkIteratorArr[i] = MediaChunkIterator.EMPTY;
                        mediaChunkIteratorArr2 = mediaChunkIteratorArr;
                        i2 = i;
                        j4 = nowUnixTimeUs;
                    } else {
                        firstAvailableSegmentNum = representationHolder.getFirstAvailableSegmentNum(this.manifest, this.periodIndex, nowUnixTimeUs);
                        long lastAvailableSegmentNum = representationHolder.getLastAvailableSegmentNum(this.manifest, this.periodIndex, nowUnixTimeUs);
                        RepresentationHolder representationHolder2 = representationHolder;
                        mediaChunkIteratorArr2 = mediaChunkIteratorArr;
                        i2 = i;
                        j4 = nowUnixTimeUs;
                        msToUs = getSegmentNum(representationHolder, mediaChunk, j2, firstAvailableSegmentNum, lastAvailableSegmentNum);
                        if (msToUs < firstAvailableSegmentNum) {
                            mediaChunkIteratorArr2[i2] = MediaChunkIterator.EMPTY;
                        } else {
                            mediaChunkIteratorArr2[i2] = new RepresentationSegmentIterator(representationHolder2, msToUs, lastAvailableSegmentNum);
                        }
                    }
                    i = i2 + 1;
                    list2 = list;
                    mediaChunkIteratorArr = mediaChunkIteratorArr2;
                    nowUnixTimeUs = j4;
                }
                j4 = nowUnixTimeUs;
                this.trackSelection.updateSelectedTrack(j, j3, resolveTimeToLiveEdgeUs, list, mediaChunkIteratorArr);
                RepresentationHolder representationHolder3 = this.representationHolders[this.trackSelection.getSelectedIndex()];
                ChunkExtractorWrapper chunkExtractorWrapper = representationHolder3.extractorWrapper;
                if (chunkExtractorWrapper != null) {
                    Representation representation = representationHolder3.representation;
                    RangedUri initializationUri = chunkExtractorWrapper.getSampleFormats() == null ? representation.getInitializationUri() : null;
                    RangedUri indexUri = representationHolder3.segmentIndex == null ? representation.getIndexUri() : null;
                    if (!(initializationUri == null && indexUri == null)) {
                        chunkHolder2.chunk = newInitializationChunk(representationHolder3, this.dataSource, this.trackSelection.getSelectedFormat(), this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), initializationUri, indexUri);
                        return;
                    }
                }
                resolveTimeToLiveEdgeUs = representationHolder3.periodDurationUs;
                firstAvailableSegmentNum = -9223372036854775807L;
                boolean z = resolveTimeToLiveEdgeUs != -9223372036854775807L;
                if (representationHolder3.getSegmentCount() == 0) {
                    chunkHolder2.endOfStream = z;
                    return;
                }
                long j5 = j4;
                long firstAvailableSegmentNum2 = representationHolder3.getFirstAvailableSegmentNum(this.manifest, this.periodIndex, j5);
                nowUnixTimeUs = representationHolder3.getLastAvailableSegmentNum(this.manifest, this.periodIndex, j5);
                updateLiveEdgeTimeUs(representationHolder3, nowUnixTimeUs);
                long j6 = nowUnixTimeUs;
                nowUnixTimeUs = getSegmentNum(representationHolder3, mediaChunk, j2, firstAvailableSegmentNum2, nowUnixTimeUs);
                if (nowUnixTimeUs < firstAvailableSegmentNum2) {
                    this.fatalError = new BehindLiveWindowException();
                } else if (nowUnixTimeUs > j6 || (this.missingLastSegment && nowUnixTimeUs >= j6)) {
                    chunkHolder2.endOfStream = z;
                } else if (!z || representationHolder3.getSegmentStartTimeUs(nowUnixTimeUs) < resolveTimeToLiveEdgeUs) {
                    int min = (int) Math.min((long) this.maxSegmentsPerLoad, (j6 - nowUnixTimeUs) + 1);
                    if (resolveTimeToLiveEdgeUs != -9223372036854775807L) {
                        while (min > 1 && representationHolder3.getSegmentStartTimeUs((((long) min) + nowUnixTimeUs) - 1) >= resolveTimeToLiveEdgeUs) {
                            min--;
                        }
                    }
                    int i3 = min;
                    if (list.isEmpty()) {
                        firstAvailableSegmentNum = j2;
                    }
                    chunkHolder2.chunk = newMediaChunk(representationHolder3, this.dataSource, this.trackType, this.trackSelection.getSelectedFormat(), this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), nowUnixTimeUs, i3, firstAvailableSegmentNum);
                } else {
                    chunkHolder2.endOfStream = true;
                }
            }
        }
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof InitializationChunk) {
            int indexOf = this.trackSelection.indexOf(((InitializationChunk) chunk).trackFormat);
            RepresentationHolder representationHolder = this.representationHolders[indexOf];
            if (representationHolder.segmentIndex == null) {
                SeekMap seekMap = representationHolder.extractorWrapper.getSeekMap();
                if (seekMap != null) {
                    this.representationHolders[indexOf] = representationHolder.copyWithNewSegmentIndex(new DashWrappingSegmentIndex((ChunkIndex) seekMap, representationHolder.representation.presentationTimeOffsetUs));
                }
            }
        }
        PlayerTrackEmsgHandler playerTrackEmsgHandler = this.playerTrackEmsgHandler;
        if (playerTrackEmsgHandler != null) {
            playerTrackEmsgHandler.onChunkLoadCompleted(chunk);
        }
    }

    public boolean onChunkLoadError(Chunk chunk, boolean z, Exception exception, long j) {
        boolean z2 = false;
        if (!z) {
            return false;
        }
        PlayerTrackEmsgHandler playerTrackEmsgHandler = this.playerTrackEmsgHandler;
        if (playerTrackEmsgHandler != null && playerTrackEmsgHandler.maybeRefreshManifestOnLoadingError(chunk)) {
            return true;
        }
        if (!this.manifest.dynamic && (chunk instanceof MediaChunk) && (exception instanceof InvalidResponseCodeException) && ((InvalidResponseCodeException) exception).responseCode == 404) {
            RepresentationHolder representationHolder = this.representationHolders[this.trackSelection.indexOf(chunk.trackFormat)];
            int segmentCount = representationHolder.getSegmentCount();
            if (!(segmentCount == -1 || segmentCount == 0)) {
                if (((MediaChunk) chunk).getNextChunkIndex() > (representationHolder.getFirstSegmentNum() + ((long) segmentCount)) - 1) {
                    this.missingLastSegment = true;
                    return true;
                }
            }
        }
        if (j != -9223372036854775807L) {
            TrackSelection trackSelection = this.trackSelection;
            if (trackSelection.blacklist(trackSelection.indexOf(chunk.trackFormat), j)) {
                z2 = true;
            }
        }
        return z2;
    }

    private long getSegmentNum(RepresentationHolder representationHolder, MediaChunk mediaChunk, long j, long j2, long j3) {
        if (mediaChunk != null) {
            return mediaChunk.getNextChunkIndex();
        }
        return Util.constrainValue(representationHolder.getSegmentNum(j), j2, j3);
    }

    private ArrayList<Representation> getRepresentations() {
        List list = this.manifest.getPeriod(this.periodIndex).adaptationSets;
        ArrayList arrayList = new ArrayList();
        for (int i : this.adaptationSetIndices) {
            arrayList.addAll(((AdaptationSet) list.get(i)).representations);
        }
        return arrayList;
    }

    private void updateLiveEdgeTimeUs(RepresentationHolder representationHolder, long j) {
        this.liveEdgeTimeUs = this.manifest.dynamic ? representationHolder.getSegmentEndTimeUs(j) : -9223372036854775807L;
    }

    private long getNowUnixTimeUs() {
        long elapsedRealtime;
        if (this.elapsedRealtimeOffsetMs != 0) {
            elapsedRealtime = SystemClock.elapsedRealtime() + this.elapsedRealtimeOffsetMs;
        } else {
            elapsedRealtime = System.currentTimeMillis();
        }
        return elapsedRealtime * 1000;
    }

    private long resolveTimeToLiveEdgeUs(long j) {
        Object obj = (!this.manifest.dynamic || this.liveEdgeTimeUs == -9223372036854775807L) ? null : 1;
        return obj != null ? this.liveEdgeTimeUs - j : -9223372036854775807L;
    }

    /* Access modifiers changed, original: protected */
    public Chunk newInitializationChunk(RepresentationHolder representationHolder, DataSource dataSource, Format format, int i, Object obj, RangedUri rangedUri, RangedUri rangedUri2) {
        String str = representationHolder.representation.baseUrl;
        if (rangedUri != null) {
            rangedUri2 = rangedUri.attemptMerge(rangedUri2, str);
            if (rangedUri2 == null) {
                rangedUri2 = rangedUri;
            }
        }
        return new InitializationChunk(dataSource, new DataSpec(rangedUri2.resolveUri(str), rangedUri2.start, rangedUri2.length, representationHolder.representation.getCacheKey()), format, i, obj, representationHolder.extractorWrapper);
    }

    /* Access modifiers changed, original: protected */
    public Chunk newMediaChunk(RepresentationHolder representationHolder, DataSource dataSource, int i, Format format, int i2, Object obj, long j, int i3, long j2) {
        RepresentationHolder representationHolder2 = representationHolder;
        long j3 = j;
        Representation representation = representationHolder2.representation;
        long segmentStartTimeUs = representationHolder2.getSegmentStartTimeUs(j3);
        RangedUri segmentUrl = representationHolder2.getSegmentUrl(j3);
        String str = representation.baseUrl;
        if (representationHolder2.extractorWrapper == null) {
            return new SingleSampleMediaChunk(dataSource, new DataSpec(segmentUrl.resolveUri(str), segmentUrl.start, segmentUrl.length, representation.getCacheKey()), format, i2, obj, segmentStartTimeUs, representationHolder2.getSegmentEndTimeUs(j3), j, i, format);
        }
        int i4 = 1;
        RangedUri rangedUri = segmentUrl;
        int i5 = 1;
        int i6 = i3;
        while (i4 < i6) {
            RangedUri attemptMerge = rangedUri.attemptMerge(representationHolder2.getSegmentUrl(((long) i4) + j3), str);
            if (attemptMerge == null) {
                break;
            }
            i5++;
            i4++;
            rangedUri = attemptMerge;
        }
        long segmentEndTimeUs = representationHolder2.getSegmentEndTimeUs((((long) i5) + j3) - 1);
        long access$000 = representationHolder.periodDurationUs;
        long j4 = (access$000 == -9223372036854775807L || access$000 > segmentEndTimeUs) ? -9223372036854775807L : access$000;
        DataSpec dataSpec = r18;
        DataSpec dataSpec2 = new DataSpec(rangedUri.resolveUri(str), rangedUri.start, rangedUri.length, representation.getCacheKey());
        return new ContainerMediaChunk(dataSource, dataSpec, format, i2, obj, segmentStartTimeUs, segmentEndTimeUs, j2, j4, j, i5, -representation.presentationTimeOffsetUs, representationHolder2.extractorWrapper);
    }
}
