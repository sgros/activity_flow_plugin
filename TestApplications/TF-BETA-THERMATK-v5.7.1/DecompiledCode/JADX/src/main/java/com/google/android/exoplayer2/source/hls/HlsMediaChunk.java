package com.google.android.exoplayer2.source.hls;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

final class HlsMediaChunk extends MediaChunk {
    private static final AtomicInteger uidSource = new AtomicInteger();
    public final int discontinuitySequenceNumber;
    private final DrmInitData drmInitData;
    private Extractor extractor;
    private final HlsExtractorFactory extractorFactory;
    private final boolean hasGapTag;
    public final HlsUrl hlsUrl;
    private final ParsableByteArray id3Data;
    private final Id3Decoder id3Decoder;
    private final DataSource initDataSource;
    private final DataSpec initDataSpec;
    private boolean initLoadCompleted;
    private int initSegmentBytesLoaded;
    private final boolean isEncrypted;
    private final boolean isMasterTimestampSource;
    private volatile boolean loadCanceled;
    private boolean loadCompleted;
    private final List<Format> muxedCaptionFormats;
    private int nextLoadPosition;
    private HlsSampleStreamWrapper output;
    private final Extractor previousExtractor;
    private final boolean shouldSpliceIn;
    private final TimestampAdjuster timestampAdjuster;
    public final int uid;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:33:0x0079 in {4, 5, 6, 9, 12, 16, 22, 26, 29, 32} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private void loadMedia() throws java.io.IOException, java.lang.InterruptedException {
        /*
        r8 = this;
        r0 = r8.isEncrypted;
        r1 = 0;
        if (r0 == 0) goto L_0x000d;
        r0 = r8.dataSpec;
        r2 = r8.nextLoadPosition;
        if (r2 == 0) goto L_0x0016;
        r2 = 1;
        goto L_0x0017;
        r0 = r8.dataSpec;
        r2 = r8.nextLoadPosition;
        r2 = (long) r2;
        r0 = r0.subrange(r2);
        r2 = 0;
        r3 = r8.isMasterTimestampSource;
        if (r3 != 0) goto L_0x0021;
        r3 = r8.timestampAdjuster;
        r3.waitUntilInitialized();
        goto L_0x0037;
        r3 = r8.timestampAdjuster;
        r3 = r3.getFirstSampleTimestampUs();
        r5 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 != 0) goto L_0x0037;
        r3 = r8.timestampAdjuster;
        r4 = r8.startTimeUs;
        r3.setFirstSampleTimestampUs(r4);
        r3 = r8.dataSource;	 Catch:{ all -> 0x0072 }
        r0 = r8.prepareExtraction(r3, r0);	 Catch:{ all -> 0x0072 }
        if (r2 == 0) goto L_0x0044;	 Catch:{ all -> 0x0072 }
        r2 = r8.nextLoadPosition;	 Catch:{ all -> 0x0072 }
        r0.skipFully(r2);	 Catch:{ all -> 0x0072 }
        if (r1 != 0) goto L_0x0060;
        r1 = r8.loadCanceled;	 Catch:{ all -> 0x0052 }
        if (r1 != 0) goto L_0x0060;	 Catch:{ all -> 0x0052 }
        r1 = r8.extractor;	 Catch:{ all -> 0x0052 }
        r2 = 0;	 Catch:{ all -> 0x0052 }
        r1 = r1.read(r0, r2);	 Catch:{ all -> 0x0052 }
        goto L_0x0044;
        r1 = move-exception;
        r2 = r0.getPosition();	 Catch:{ all -> 0x0072 }
        r0 = r8.dataSpec;	 Catch:{ all -> 0x0072 }
        r4 = r0.absoluteStreamPosition;	 Catch:{ all -> 0x0072 }
        r2 = r2 - r4;	 Catch:{ all -> 0x0072 }
        r0 = (int) r2;	 Catch:{ all -> 0x0072 }
        r8.nextLoadPosition = r0;	 Catch:{ all -> 0x0072 }
        throw r1;	 Catch:{ all -> 0x0072 }
        r0 = r0.getPosition();	 Catch:{ all -> 0x0072 }
        r2 = r8.dataSpec;	 Catch:{ all -> 0x0072 }
        r2 = r2.absoluteStreamPosition;	 Catch:{ all -> 0x0072 }
        r0 = r0 - r2;	 Catch:{ all -> 0x0072 }
        r1 = (int) r0;	 Catch:{ all -> 0x0072 }
        r8.nextLoadPosition = r1;	 Catch:{ all -> 0x0072 }
        r0 = r8.dataSource;
        com.google.android.exoplayer2.util.Util.closeQuietly(r0);
        return;
        r0 = move-exception;
        r1 = r8.dataSource;
        com.google.android.exoplayer2.util.Util.closeQuietly(r1);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.hls.HlsMediaChunk.loadMedia():void");
    }

    public HlsMediaChunk(HlsExtractorFactory hlsExtractorFactory, DataSource dataSource, DataSpec dataSpec, DataSpec dataSpec2, HlsUrl hlsUrl, List<Format> list, int i, Object obj, long j, long j2, long j3, int i2, boolean z, boolean z2, TimestampAdjuster timestampAdjuster, HlsMediaChunk hlsMediaChunk, DrmInitData drmInitData, byte[] bArr, byte[] bArr2) {
        HlsUrl hlsUrl2 = hlsUrl;
        int i3 = i2;
        HlsMediaChunk hlsMediaChunk2 = hlsMediaChunk;
        super(buildDataSource(dataSource, bArr, bArr2), dataSpec, hlsUrl2.format, i, obj, j, j2, j3);
        this.discontinuitySequenceNumber = i3;
        this.initDataSpec = dataSpec2;
        this.hlsUrl = hlsUrl2;
        this.isMasterTimestampSource = z2;
        this.timestampAdjuster = timestampAdjuster;
        boolean z3 = true;
        this.isEncrypted = bArr != null;
        this.hasGapTag = z;
        this.extractorFactory = hlsExtractorFactory;
        this.muxedCaptionFormats = list;
        this.drmInitData = drmInitData;
        Extractor extractor = null;
        if (hlsMediaChunk2 != null) {
            this.id3Decoder = hlsMediaChunk2.id3Decoder;
            this.id3Data = hlsMediaChunk2.id3Data;
            if (hlsMediaChunk2.hlsUrl == hlsUrl2 && hlsMediaChunk2.loadCompleted) {
                z3 = false;
            }
            this.shouldSpliceIn = z3;
            if (hlsMediaChunk2.discontinuitySequenceNumber == i3 && !this.shouldSpliceIn) {
                extractor = hlsMediaChunk2.extractor;
            }
        } else {
            this.id3Decoder = new Id3Decoder();
            this.id3Data = new ParsableByteArray(10);
            this.shouldSpliceIn = false;
        }
        this.previousExtractor = extractor;
        this.initDataSource = dataSource;
        this.uid = uidSource.getAndIncrement();
    }

    public void init(HlsSampleStreamWrapper hlsSampleStreamWrapper) {
        this.output = hlsSampleStreamWrapper;
    }

    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }

    public void cancelLoad() {
        this.loadCanceled = true;
    }

    public void load() throws IOException, InterruptedException {
        maybeLoadInitData();
        if (!this.loadCanceled) {
            if (!this.hasGapTag) {
                loadMedia();
            }
            this.loadCompleted = true;
        }
    }

    private void maybeLoadInitData() throws IOException, InterruptedException {
        if (!this.initLoadCompleted) {
            DataSpec dataSpec = this.initDataSpec;
            if (dataSpec != null) {
                DefaultExtractorInput prepareExtraction;
                try {
                    prepareExtraction = prepareExtraction(this.initDataSource, dataSpec.subrange((long) this.initSegmentBytesLoaded));
                    int i = 0;
                    while (i == 0) {
                        if (this.loadCanceled) {
                            break;
                        }
                        i = this.extractor.read(prepareExtraction, null);
                    }
                    this.initSegmentBytesLoaded = (int) (prepareExtraction.getPosition() - this.initDataSpec.absoluteStreamPosition);
                    Util.closeQuietly(this.initDataSource);
                    this.initLoadCompleted = true;
                } catch (Throwable th) {
                    Util.closeQuietly(this.initDataSource);
                }
            }
        }
    }

    private DefaultExtractorInput prepareExtraction(DataSource dataSource, DataSpec dataSpec) throws IOException, InterruptedException {
        DataSpec dataSpec2 = dataSpec;
        DefaultExtractorInput defaultExtractorInput = new DefaultExtractorInput(dataSource, dataSpec2.absoluteStreamPosition, dataSource.open(dataSpec));
        if (this.extractor != null) {
            return defaultExtractorInput;
        }
        long peekId3PrivTimestamp = peekId3PrivTimestamp(defaultExtractorInput);
        defaultExtractorInput.resetPeekPosition();
        DefaultExtractorInput defaultExtractorInput2 = defaultExtractorInput;
        Pair createExtractor = this.extractorFactory.createExtractor(this.previousExtractor, dataSpec2.uri, this.trackFormat, this.muxedCaptionFormats, this.drmInitData, this.timestampAdjuster, dataSource.getResponseHeaders(), defaultExtractorInput2);
        this.extractor = (Extractor) createExtractor.first;
        boolean z = true;
        boolean z2 = this.extractor == this.previousExtractor;
        if (((Boolean) createExtractor.second).booleanValue()) {
            this.output.setSampleOffsetUs(peekId3PrivTimestamp != -9223372036854775807L ? this.timestampAdjuster.adjustTsTimestamp(peekId3PrivTimestamp) : this.startTimeUs);
        }
        if (!z2 || this.initDataSpec == null) {
            z = false;
        }
        this.initLoadCompleted = z;
        this.output.init(this.uid, this.shouldSpliceIn, z2);
        if (z2) {
            return defaultExtractorInput2;
        }
        this.extractor.init(this.output);
        return defaultExtractorInput2;
    }

    private long peekId3PrivTimestamp(ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.resetPeekPosition();
        try {
            extractorInput.peekFully(this.id3Data.data, 0, 10);
            this.id3Data.reset(10);
            if (this.id3Data.readUnsignedInt24() != Id3Decoder.ID3_TAG) {
                return -9223372036854775807L;
            }
            this.id3Data.skipBytes(3);
            int readSynchSafeInt = this.id3Data.readSynchSafeInt();
            int i = readSynchSafeInt + 10;
            if (i > this.id3Data.capacity()) {
                ParsableByteArray parsableByteArray = this.id3Data;
                byte[] bArr = parsableByteArray.data;
                parsableByteArray.reset(i);
                System.arraycopy(bArr, 0, this.id3Data.data, 0, 10);
            }
            extractorInput.peekFully(this.id3Data.data, 10, readSynchSafeInt);
            Metadata decode = this.id3Decoder.decode(this.id3Data.data, readSynchSafeInt);
            if (decode == null) {
                return -9223372036854775807L;
            }
            readSynchSafeInt = decode.length();
            for (int i2 = 0; i2 < readSynchSafeInt; i2++) {
                Entry entry = decode.get(i2);
                if (entry instanceof PrivFrame) {
                    PrivFrame privFrame = (PrivFrame) entry;
                    if ("com.apple.streaming.transportStreamTimestamp".equals(privFrame.owner)) {
                        System.arraycopy(privFrame.privateData, 0, this.id3Data.data, 0, 8);
                        this.id3Data.reset(8);
                        return this.id3Data.readLong() & 8589934591L;
                    }
                }
            }
            return -9223372036854775807L;
        } catch (EOFException unused) {
        }
    }

    private static DataSource buildDataSource(DataSource dataSource, byte[] bArr, byte[] bArr2) {
        return bArr != null ? new Aes128DataSource(dataSource, bArr, bArr2) : dataSource;
    }
}
