package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.BaseMediaChunkIterator;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.DataChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.trackselection.BaseTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

class HlsChunkSource {
    private final DataSource encryptionDataSource;
    private byte[] encryptionIv;
    private String encryptionIvString;
    private byte[] encryptionKey;
    private Uri encryptionKeyUri;
    private HlsUrl expectedPlaylistUrl;
    private final HlsExtractorFactory extractorFactory;
    private IOException fatalError;
    private boolean independentSegments;
    private boolean isTimestampMaster;
    private long liveEdgeInPeriodTimeUs = -9223372036854775807L;
    private final DataSource mediaDataSource;
    private final List<Format> muxedCaptionFormats;
    private final HlsPlaylistTracker playlistTracker;
    private byte[] scratchSpace;
    private boolean seenExpectedPlaylistError;
    private final TimestampAdjusterProvider timestampAdjusterProvider;
    private final TrackGroup trackGroup;
    private TrackSelection trackSelection;
    private final HlsUrl[] variants;

    public static final class HlsChunkHolder {
        public Chunk chunk;
        public boolean endOfStream;
        public HlsUrl playlist;

        public HlsChunkHolder() {
            clear();
        }

        public void clear() {
            this.chunk = null;
            this.endOfStream = false;
            this.playlist = null;
        }
    }

    private static final class HlsMediaPlaylistSegmentIterator extends BaseMediaChunkIterator {
        private final HlsMediaPlaylist playlist;
        private final long startOfPlaylistInPeriodUs;

        public HlsMediaPlaylistSegmentIterator(HlsMediaPlaylist hlsMediaPlaylist, long j, int i) {
            super((long) i, (long) (hlsMediaPlaylist.segments.size() - 1));
            this.playlist = hlsMediaPlaylist;
            this.startOfPlaylistInPeriodUs = j;
        }
    }

    private static final class InitializationTrackSelection extends BaseTrackSelection {
        private int selectedIndex;

        public Object getSelectionData() {
            return null;
        }

        public int getSelectionReason() {
            return 0;
        }

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:12:0x0025 in {2, 8, 9, 11} preds:[]
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
        public void updateSelectedTrack(long r1, long r3, long r5, java.util.List<? extends com.google.android.exoplayer2.source.chunk.MediaChunk> r7, com.google.android.exoplayer2.source.chunk.MediaChunkIterator[] r8) {
            /*
            r0 = this;
            r1 = android.os.SystemClock.elapsedRealtime();
            r3 = r0.selectedIndex;
            r3 = r0.isBlacklisted(r3, r1);
            if (r3 != 0) goto L_0x000d;
            return;
            r3 = r0.length;
            r3 = r3 + -1;
            if (r3 < 0) goto L_0x001f;
            r4 = r0.isBlacklisted(r3, r1);
            if (r4 != 0) goto L_0x001c;
            r0.selectedIndex = r3;
            return;
            r3 = r3 + -1;
            goto L_0x0011;
            r1 = new java.lang.IllegalStateException;
            r1.<init>();
            throw r1;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.hls.HlsChunkSource$InitializationTrackSelection.updateSelectedTrack(long, long, long, java.util.List, com.google.android.exoplayer2.source.chunk.MediaChunkIterator[]):void");
        }

        public InitializationTrackSelection(TrackGroup trackGroup, int[] iArr) {
            super(trackGroup, iArr);
            this.selectedIndex = indexOf(trackGroup.getFormat(0));
        }

        public int getSelectedIndex() {
            return this.selectedIndex;
        }
    }

    private static final class EncryptionKeyChunk extends DataChunk {
        /* renamed from: iv */
        public final String f25iv;
        private byte[] result;

        public EncryptionKeyChunk(DataSource dataSource, DataSpec dataSpec, Format format, int i, Object obj, byte[] bArr, String str) {
            super(dataSource, dataSpec, 3, format, i, obj, bArr);
            this.f25iv = str;
        }

        /* Access modifiers changed, original: protected */
        public void consume(byte[] bArr, int i) throws IOException {
            this.result = Arrays.copyOf(bArr, i);
        }

        public byte[] getResult() {
            return this.result;
        }
    }

    public HlsChunkSource(HlsExtractorFactory hlsExtractorFactory, HlsPlaylistTracker hlsPlaylistTracker, HlsUrl[] hlsUrlArr, HlsDataSourceFactory hlsDataSourceFactory, TransferListener transferListener, TimestampAdjusterProvider timestampAdjusterProvider, List<Format> list) {
        this.extractorFactory = hlsExtractorFactory;
        this.playlistTracker = hlsPlaylistTracker;
        this.variants = hlsUrlArr;
        this.timestampAdjusterProvider = timestampAdjusterProvider;
        this.muxedCaptionFormats = list;
        Format[] formatArr = new Format[hlsUrlArr.length];
        int[] iArr = new int[hlsUrlArr.length];
        for (int i = 0; i < hlsUrlArr.length; i++) {
            formatArr[i] = hlsUrlArr[i].format;
            iArr[i] = i;
        }
        this.mediaDataSource = hlsDataSourceFactory.createDataSource(1);
        if (transferListener != null) {
            this.mediaDataSource.addTransferListener(transferListener);
        }
        this.encryptionDataSource = hlsDataSourceFactory.createDataSource(3);
        this.trackGroup = new TrackGroup(formatArr);
        this.trackSelection = new InitializationTrackSelection(this.trackGroup, iArr);
    }

    public void maybeThrowError() throws IOException {
        IOException iOException = this.fatalError;
        if (iOException == null) {
            HlsUrl hlsUrl = this.expectedPlaylistUrl;
            if (hlsUrl != null && this.seenExpectedPlaylistError) {
                this.playlistTracker.maybeThrowPlaylistRefreshError(hlsUrl);
                return;
            }
            return;
        }
        throw iOException;
    }

    public TrackGroup getTrackGroup() {
        return this.trackGroup;
    }

    public void selectTracks(TrackSelection trackSelection) {
        this.trackSelection = trackSelection;
    }

    public TrackSelection getTrackSelection() {
        return this.trackSelection;
    }

    public void reset() {
        this.fatalError = null;
    }

    public void setIsTimestampMaster(boolean z) {
        this.isTimestampMaster = z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x006f  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x007e  */
    public void getNextChunk(long r40, long r42, java.util.List<com.google.android.exoplayer2.source.hls.HlsMediaChunk> r44, com.google.android.exoplayer2.source.hls.HlsChunkSource.HlsChunkHolder r45) {
        /*
        r39 = this;
        r8 = r39;
        r6 = r42;
        r9 = r45;
        r0 = r44.isEmpty();
        r11 = 1;
        if (r0 == 0) goto L_0x0011;
    L_0x000d:
        r1 = r44;
        r4 = 0;
        goto L_0x001f;
    L_0x0011:
        r0 = r44.size();
        r0 = r0 - r11;
        r1 = r44;
        r0 = r1.get(r0);
        r0 = (com.google.android.exoplayer2.source.hls.HlsMediaChunk) r0;
        r4 = r0;
    L_0x001f:
        if (r4 != 0) goto L_0x0024;
    L_0x0021:
        r0 = -1;
        r5 = -1;
        goto L_0x002d;
    L_0x0024:
        r0 = r8.trackGroup;
        r2 = r4.trackFormat;
        r0 = r0.indexOf(r2);
        r5 = r0;
    L_0x002d:
        r2 = r6 - r40;
        r12 = r39.resolveTimeToLiveEdgeUs(r40);
        if (r4 == 0) goto L_0x0056;
    L_0x0035:
        r0 = r8.independentSegments;
        if (r0 != 0) goto L_0x0056;
    L_0x0039:
        r14 = r4.getDurationUs();
        r2 = r2 - r14;
        r10 = 0;
        r2 = java.lang.Math.max(r10, r2);
        r16 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r0 = (r12 > r16 ? 1 : (r12 == r16 ? 0 : -1));
        if (r0 == 0) goto L_0x0056;
    L_0x004d:
        r12 = r12 - r14;
        r10 = java.lang.Math.max(r10, r12);
        r15 = r2;
        r17 = r10;
        goto L_0x0059;
    L_0x0056:
        r15 = r2;
        r17 = r12;
    L_0x0059:
        r20 = r8.createMediaChunkIterators(r4, r6);
        r12 = r8.trackSelection;
        r13 = r40;
        r19 = r44;
        r12.updateSelectedTrack(r13, r15, r17, r19, r20);
        r0 = r8.trackSelection;
        r10 = r0.getSelectedIndexInTrackGroup();
        r11 = 0;
        if (r5 == r10) goto L_0x0071;
    L_0x006f:
        r12 = 1;
        goto L_0x0072;
    L_0x0071:
        r12 = 0;
    L_0x0072:
        r0 = r8.variants;
        r13 = r0[r10];
        r0 = r8.playlistTracker;
        r0 = r0.isSnapshotValid(r13);
        if (r0 != 0) goto L_0x008d;
    L_0x007e:
        r9.playlist = r13;
        r0 = r8.seenExpectedPlaylistError;
        r1 = r8.expectedPlaylistUrl;
        if (r1 != r13) goto L_0x0087;
    L_0x0086:
        r11 = 1;
    L_0x0087:
        r0 = r0 & r11;
        r8.seenExpectedPlaylistError = r0;
        r8.expectedPlaylistUrl = r13;
        return;
    L_0x008d:
        r0 = r8.playlistTracker;
        r1 = 1;
        r14 = r0.getPlaylistSnapshot(r13, r1);
        r0 = r14.hasIndependentSegments;
        r8.independentSegments = r0;
        r8.updateLiveEdgeTimeUs(r14);
        r0 = r14.startTimeUs;
        r2 = r8.playlistTracker;
        r2 = r2.getInitialStartTimeUs();
        r15 = r0 - r2;
        r0 = r39;
        r1 = r4;
        r2 = r12;
        r3 = r14;
        r31 = r4;
        r17 = r5;
        r4 = r15;
        r6 = r42;
        r0 = r0.getChunkMediaSequence(r1, r2, r3, r4, r6);
        r2 = r14.mediaSequence;
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 >= 0) goto L_0x00e5;
    L_0x00bb:
        if (r31 == 0) goto L_0x00dd;
    L_0x00bd:
        if (r12 == 0) goto L_0x00dd;
    L_0x00bf:
        r0 = r8.variants;
        r0 = r0[r17];
        r1 = r8.playlistTracker;
        r2 = 1;
        r14 = r1.getPlaylistSnapshot(r0, r2);
        r1 = r14.startTimeUs;
        r3 = r8.playlistTracker;
        r3 = r3.getInitialStartTimeUs();
        r15 = r1 - r3;
        r1 = r31.getNextChunkIndex();
        r25 = r1;
        r3 = r17;
        goto L_0x00e9;
    L_0x00dd:
        r0 = new com.google.android.exoplayer2.source.BehindLiveWindowException;
        r0.<init>();
        r8.fatalError = r0;
        return;
    L_0x00e5:
        r25 = r0;
        r3 = r10;
        r0 = r13;
    L_0x00e9:
        r1 = r14.mediaSequence;
        r1 = r25 - r1;
        r2 = (int) r1;
        r1 = r14.segments;
        r1 = r1.size();
        if (r2 < r1) goto L_0x010f;
    L_0x00f6:
        r1 = r14.hasEndTag;
        if (r1 == 0) goto L_0x00fe;
    L_0x00fa:
        r1 = 1;
        r9.endOfStream = r1;
        goto L_0x010e;
    L_0x00fe:
        r1 = 1;
        r9.playlist = r0;
        r2 = r8.seenExpectedPlaylistError;
        r3 = r8.expectedPlaylistUrl;
        if (r3 != r0) goto L_0x0108;
    L_0x0107:
        r11 = 1;
    L_0x0108:
        r1 = r2 & r11;
        r8.seenExpectedPlaylistError = r1;
        r8.expectedPlaylistUrl = r0;
    L_0x010e:
        return;
    L_0x010f:
        r8.seenExpectedPlaylistError = r11;
        r1 = 0;
        r8.expectedPlaylistUrl = r1;
        r4 = r14.segments;
        r2 = r4.get(r2);
        r2 = (com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment) r2;
        r4 = r2.fullSegmentEncryptionKeyUri;
        if (r4 == 0) goto L_0x015a;
    L_0x0120:
        r5 = r14.baseUri;
        r4 = com.google.android.exoplayer2.util.UriUtil.resolveToUri(r5, r4);
        r5 = r8.encryptionKeyUri;
        r5 = r4.equals(r5);
        if (r5 != 0) goto L_0x0148;
    L_0x012e:
        r2 = r2.encryptionIV;
        r0 = r8.trackSelection;
        r5 = r0.getSelectionReason();
        r0 = r8.trackSelection;
        r6 = r0.getSelectionData();
        r0 = r39;
        r1 = r4;
        r4 = r5;
        r5 = r6;
        r0 = r0.newEncryptionKeyChunk(r1, r2, r3, r4, r5);
        r9.chunk = r0;
        return;
    L_0x0148:
        r3 = r2.encryptionIV;
        r5 = r8.encryptionIvString;
        r3 = com.google.android.exoplayer2.util.Util.areEqual(r3, r5);
        if (r3 != 0) goto L_0x015d;
    L_0x0152:
        r3 = r2.encryptionIV;
        r5 = r8.encryptionKey;
        r8.setEncryptionData(r4, r3, r5);
        goto L_0x015d;
    L_0x015a:
        r39.clearEncryptionData();
    L_0x015d:
        r3 = r2.initializationSegment;
        if (r3 == 0) goto L_0x017a;
    L_0x0161:
        r1 = r14.baseUri;
        r4 = r3.url;
        r18 = com.google.android.exoplayer2.util.UriUtil.resolveToUri(r1, r4);
        r1 = new com.google.android.exoplayer2.upstream.DataSpec;
        r4 = r3.byterangeOffset;
        r6 = r3.byterangeLength;
        r23 = 0;
        r17 = r1;
        r19 = r4;
        r21 = r6;
        r17.<init>(r18, r19, r21, r23);
    L_0x017a:
        r3 = r2.relativeStartTimeUs;
        r3 = r3 + r15;
        r21 = r3;
        r5 = r14.discontinuitySequence;
        r6 = r2.relativeDiscontinuitySequence;
        r5 = r5 + r6;
        r27 = r5;
        r6 = r8.timestampAdjusterProvider;
        r30 = r6.getAdjuster(r5);
        r5 = r14.baseUri;
        r6 = r2.url;
        r33 = com.google.android.exoplayer2.util.UriUtil.resolveToUri(r5, r6);
        r32 = new com.google.android.exoplayer2.upstream.DataSpec;
        r15 = r32;
        r5 = r2.byterangeOffset;
        r10 = r2.byterangeLength;
        r38 = 0;
        r34 = r5;
        r36 = r10;
        r32.<init>(r33, r34, r36, r38);
        r5 = new com.google.android.exoplayer2.source.hls.HlsMediaChunk;
        r12 = r5;
        r13 = r8.extractorFactory;
        r14 = r8.mediaDataSource;
        r6 = r8.muxedCaptionFormats;
        r18 = r6;
        r6 = r8.trackSelection;
        r19 = r6.getSelectionReason();
        r6 = r8.trackSelection;
        r20 = r6.getSelectionData();
        r6 = r2.durationUs;
        r23 = r3 + r6;
        r3 = r2.hasGapTag;
        r28 = r3;
        r3 = r8.isTimestampMaster;
        r29 = r3;
        r2 = r2.drmInitData;
        r32 = r2;
        r2 = r8.encryptionKey;
        r33 = r2;
        r2 = r8.encryptionIv;
        r34 = r2;
        r16 = r1;
        r17 = r0;
        r12.<init>(r13, r14, r15, r16, r17, r18, r19, r20, r21, r23, r25, r27, r28, r29, r30, r31, r32, r33, r34);
        r9.chunk = r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.hls.HlsChunkSource.getNextChunk(long, long, java.util.List, com.google.android.exoplayer2.source.hls.HlsChunkSource$HlsChunkHolder):void");
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof EncryptionKeyChunk) {
            EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk) chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.f25iv, encryptionKeyChunk.getResult());
        }
    }

    public boolean maybeBlacklistTrack(Chunk chunk, long j) {
        TrackSelection trackSelection = this.trackSelection;
        return trackSelection.blacklist(trackSelection.indexOf(this.trackGroup.indexOf(chunk.trackFormat)), j);
    }

    public boolean onPlaylistError(HlsUrl hlsUrl, long j) {
        int indexOf = this.trackGroup.indexOf(hlsUrl.format);
        boolean z = true;
        if (indexOf == -1) {
            return true;
        }
        indexOf = this.trackSelection.indexOf(indexOf);
        if (indexOf == -1) {
            return true;
        }
        this.seenExpectedPlaylistError = (this.expectedPlaylistUrl == hlsUrl ? 1 : 0) | this.seenExpectedPlaylistError;
        if (!(j == -9223372036854775807L || this.trackSelection.blacklist(indexOf, j))) {
            z = false;
        }
        return z;
    }

    public MediaChunkIterator[] createMediaChunkIterators(HlsMediaChunk hlsMediaChunk, long j) {
        int i;
        Chunk chunk = hlsMediaChunk;
        if (chunk == null) {
            i = -1;
        } else {
            i = this.trackGroup.indexOf(chunk.trackFormat);
        }
        MediaChunkIterator[] mediaChunkIteratorArr = new MediaChunkIterator[this.trackSelection.length()];
        for (int i2 = 0; i2 < mediaChunkIteratorArr.length; i2++) {
            int indexInTrackGroup = this.trackSelection.getIndexInTrackGroup(i2);
            HlsUrl hlsUrl = this.variants[indexInTrackGroup];
            if (this.playlistTracker.isSnapshotValid(hlsUrl)) {
                HlsMediaPlaylist playlistSnapshot = this.playlistTracker.getPlaylistSnapshot(hlsUrl, false);
                long initialStartTimeUs = playlistSnapshot.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
                long j2 = initialStartTimeUs;
                long chunkMediaSequence = getChunkMediaSequence(hlsMediaChunk, indexInTrackGroup != i, playlistSnapshot, initialStartTimeUs, j);
                long j3 = playlistSnapshot.mediaSequence;
                if (chunkMediaSequence < j3) {
                    mediaChunkIteratorArr[i2] = MediaChunkIterator.EMPTY;
                } else {
                    mediaChunkIteratorArr[i2] = new HlsMediaPlaylistSegmentIterator(playlistSnapshot, j2, (int) (chunkMediaSequence - j3));
                }
            } else {
                mediaChunkIteratorArr[i2] = MediaChunkIterator.EMPTY;
            }
        }
        return mediaChunkIteratorArr;
    }

    private long getChunkMediaSequence(HlsMediaChunk hlsMediaChunk, boolean z, HlsMediaPlaylist hlsMediaPlaylist, long j, long j2) {
        if (hlsMediaChunk != null && !z) {
            return hlsMediaChunk.getNextChunkIndex();
        }
        long binarySearchFloor;
        long j3;
        long j4 = hlsMediaPlaylist.durationUs + j;
        if (!(hlsMediaChunk == null || this.independentSegments)) {
            j2 = hlsMediaChunk.startTimeUs;
        }
        if (hlsMediaPlaylist.hasEndTag || j2 < j4) {
            j2 -= j;
            List list = hlsMediaPlaylist.segments;
            Comparable valueOf = Long.valueOf(j2);
            boolean z2 = !this.playlistTracker.isLive() || hlsMediaChunk == null;
            binarySearchFloor = (long) Util.binarySearchFloor(list, valueOf, true, z2);
            j3 = hlsMediaPlaylist.mediaSequence;
        } else {
            binarySearchFloor = hlsMediaPlaylist.mediaSequence;
            j3 = (long) hlsMediaPlaylist.segments.size();
        }
        return binarySearchFloor + j3;
    }

    private long resolveTimeToLiveEdgeUs(long j) {
        if ((this.liveEdgeInPeriodTimeUs != -9223372036854775807L ? 1 : null) != null) {
            return this.liveEdgeInPeriodTimeUs - j;
        }
        return -9223372036854775807L;
    }

    private void updateLiveEdgeTimeUs(HlsMediaPlaylist hlsMediaPlaylist) {
        long j;
        if (hlsMediaPlaylist.hasEndTag) {
            j = -9223372036854775807L;
        } else {
            j = hlsMediaPlaylist.getEndTimeUs() - this.playlistTracker.getInitialStartTimeUs();
        }
        this.liveEdgeInPeriodTimeUs = j;
    }

    private EncryptionKeyChunk newEncryptionKeyChunk(Uri uri, String str, int i, int i2, Object obj) {
        return new EncryptionKeyChunk(this.encryptionDataSource, new DataSpec(uri, 0, -1, null, 1), this.variants[i].format, i2, obj, this.scratchSpace, str);
    }

    private void setEncryptionData(Uri uri, String str, byte[] bArr) {
        byte[] toByteArray = new BigInteger(Util.toLowerInvariant(str).startsWith("0x") ? str.substring(2) : str, 16).toByteArray();
        byte[] bArr2 = new byte[16];
        int length = toByteArray.length > 16 ? toByteArray.length - 16 : 0;
        System.arraycopy(toByteArray, length, bArr2, (bArr2.length - toByteArray.length) + length, toByteArray.length - length);
        this.encryptionKeyUri = uri;
        this.encryptionKey = bArr;
        this.encryptionIvString = str;
        this.encryptionIv = bArr2;
    }

    private void clearEncryptionData() {
        this.encryptionKeyUri = null;
        this.encryptionKey = null;
        this.encryptionIvString = null;
        this.encryptionIv = null;
    }
}
