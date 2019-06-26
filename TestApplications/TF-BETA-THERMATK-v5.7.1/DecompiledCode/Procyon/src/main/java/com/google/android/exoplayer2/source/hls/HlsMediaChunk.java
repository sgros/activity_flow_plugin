// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.hls;

import android.util.Pair;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import java.io.EOFException;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.Format;
import java.util.List;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.android.exoplayer2.source.chunk.MediaChunk;

final class HlsMediaChunk extends MediaChunk
{
    private static final AtomicInteger uidSource;
    public final int discontinuitySequenceNumber;
    private final DrmInitData drmInitData;
    private Extractor extractor;
    private final HlsExtractorFactory extractorFactory;
    private final boolean hasGapTag;
    public final HlsMasterPlaylist.HlsUrl hlsUrl;
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
    
    static {
        uidSource = new AtomicInteger();
    }
    
    public HlsMediaChunk(final HlsExtractorFactory extractorFactory, final DataSource initDataSource, final DataSpec dataSpec, final DataSpec initDataSpec, final HlsMasterPlaylist.HlsUrl hlsUrl, final List<Format> muxedCaptionFormats, final int n, final Object o, final long n2, final long n3, final long n4, final int discontinuitySequenceNumber, final boolean hasGapTag, final boolean isMasterTimestampSource, final TimestampAdjuster timestampAdjuster, final HlsMediaChunk hlsMediaChunk, final DrmInitData drmInitData, final byte[] array, final byte[] array2) {
        super(buildDataSource(initDataSource, array, array2), dataSpec, hlsUrl.format, n, o, n2, n3, n4);
        this.discontinuitySequenceNumber = discontinuitySequenceNumber;
        this.initDataSpec = initDataSpec;
        this.hlsUrl = hlsUrl;
        this.isMasterTimestampSource = isMasterTimestampSource;
        this.timestampAdjuster = timestampAdjuster;
        final boolean b = true;
        this.isEncrypted = (array != null);
        this.hasGapTag = hasGapTag;
        this.extractorFactory = extractorFactory;
        this.muxedCaptionFormats = muxedCaptionFormats;
        this.drmInitData = drmInitData;
        final Extractor extractor = null;
        Extractor extractor2;
        if (hlsMediaChunk != null) {
            this.id3Decoder = hlsMediaChunk.id3Decoder;
            this.id3Data = hlsMediaChunk.id3Data;
            boolean shouldSpliceIn = b;
            if (hlsMediaChunk.hlsUrl == hlsUrl) {
                shouldSpliceIn = (!hlsMediaChunk.loadCompleted && b);
            }
            this.shouldSpliceIn = shouldSpliceIn;
            extractor2 = extractor;
            if (hlsMediaChunk.discontinuitySequenceNumber == discontinuitySequenceNumber) {
                if (this.shouldSpliceIn) {
                    extractor2 = extractor;
                }
                else {
                    extractor2 = hlsMediaChunk.extractor;
                }
            }
        }
        else {
            this.id3Decoder = new Id3Decoder();
            this.id3Data = new ParsableByteArray(10);
            this.shouldSpliceIn = false;
            extractor2 = extractor;
        }
        this.previousExtractor = extractor2;
        this.initDataSource = initDataSource;
        this.uid = HlsMediaChunk.uidSource.getAndIncrement();
    }
    
    private static DataSource buildDataSource(final DataSource dataSource, final byte[] array, final byte[] array2) {
        if (array != null) {
            return new Aes128DataSource(dataSource, array, array2);
        }
        return dataSource;
    }
    
    private void loadMedia() throws IOException, InterruptedException {
        final boolean isEncrypted = this.isEncrypted;
        final boolean b = false;
        DataSpec dataSpec = null;
        boolean b2 = false;
        Label_0052: {
            if (isEncrypted) {
                dataSpec = super.dataSpec;
                if (this.nextLoadPosition != 0) {
                    b2 = true;
                    dataSpec = dataSpec;
                    break Label_0052;
                }
            }
            else {
                dataSpec = super.dataSpec.subrange(this.nextLoadPosition);
            }
            b2 = false;
        }
        if (!this.isMasterTimestampSource) {
            this.timestampAdjuster.waitUntilInitialized();
        }
        else if (this.timestampAdjuster.getFirstSampleTimestampUs() == Long.MAX_VALUE) {
            this.timestampAdjuster.setFirstSampleTimestampUs(super.startTimeUs);
        }
        try {
            final DefaultExtractorInput prepareExtraction = this.prepareExtraction(super.dataSource, dataSpec);
            int i = b ? 1 : 0;
            if (b2) {
                prepareExtraction.skipFully(this.nextLoadPosition);
                i = (b ? 1 : 0);
            }
            while (i == 0) {
                try {
                    if (!this.loadCanceled) {
                        i = this.extractor.read(prepareExtraction, null);
                        continue;
                    }
                }
                finally {
                    this.nextLoadPosition = (int)(prepareExtraction.getPosition() - super.dataSpec.absoluteStreamPosition);
                }
                break;
            }
            this.nextLoadPosition = (int)(prepareExtraction.getPosition() - super.dataSpec.absoluteStreamPosition);
        }
        finally {
            Util.closeQuietly(super.dataSource);
        }
    }
    
    private void maybeLoadInitData() throws IOException, InterruptedException {
        if (!this.initLoadCompleted) {
            final DataSpec initDataSpec = this.initDataSpec;
            if (initDataSpec != null) {
                final DataSpec subrange = initDataSpec.subrange(this.initSegmentBytesLoaded);
                try {
                    final DefaultExtractorInput prepareExtraction = this.prepareExtraction(this.initDataSource, subrange);
                    int i = 0;
                    while (i == 0) {
                        try {
                            if (!this.loadCanceled) {
                                i = this.extractor.read(prepareExtraction, null);
                                continue;
                            }
                        }
                        finally {
                            this.initSegmentBytesLoaded = (int)(prepareExtraction.getPosition() - this.initDataSpec.absoluteStreamPosition);
                        }
                        break;
                    }
                    this.initSegmentBytesLoaded = (int)(prepareExtraction.getPosition() - this.initDataSpec.absoluteStreamPosition);
                    Util.closeQuietly(this.initDataSource);
                    this.initLoadCompleted = true;
                }
                finally {
                    Util.closeQuietly(this.initDataSource);
                }
            }
        }
    }
    
    private long peekId3PrivTimestamp(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.resetPeekPosition();
        try {
            extractorInput.peekFully(this.id3Data.data, 0, 10);
            this.id3Data.reset(10);
            if (this.id3Data.readUnsignedInt24() != Id3Decoder.ID3_TAG) {
                return -9223372036854775807L;
            }
            this.id3Data.skipBytes(3);
            final int synchSafeInt = this.id3Data.readSynchSafeInt();
            final int n = synchSafeInt + 10;
            if (n > this.id3Data.capacity()) {
                final ParsableByteArray id3Data = this.id3Data;
                final byte[] data = id3Data.data;
                id3Data.reset(n);
                System.arraycopy(data, 0, this.id3Data.data, 0, 10);
            }
            extractorInput.peekFully(this.id3Data.data, 10, synchSafeInt);
            final Metadata decode = this.id3Decoder.decode(this.id3Data.data, synchSafeInt);
            if (decode == null) {
                return -9223372036854775807L;
            }
            for (int length = decode.length(), i = 0; i < length; ++i) {
                final Metadata.Entry value = decode.get(i);
                if (value instanceof PrivFrame) {
                    final PrivFrame privFrame = (PrivFrame)value;
                    if ("com.apple.streaming.transportStreamTimestamp".equals(privFrame.owner)) {
                        System.arraycopy(privFrame.privateData, 0, this.id3Data.data, 0, 8);
                        this.id3Data.reset(8);
                        return this.id3Data.readLong() & 0x1FFFFFFFFL;
                    }
                }
            }
            return -9223372036854775807L;
        }
        catch (EOFException ex) {
            return -9223372036854775807L;
        }
    }
    
    private DefaultExtractorInput prepareExtraction(final DataSource dataSource, final DataSpec dataSpec) throws IOException, InterruptedException {
        final DefaultExtractorInput defaultExtractorInput = new DefaultExtractorInput(dataSource, dataSpec.absoluteStreamPosition, dataSource.open(dataSpec));
        if (this.extractor == null) {
            final long peekId3PrivTimestamp = this.peekId3PrivTimestamp(defaultExtractorInput);
            defaultExtractorInput.resetPeekPosition();
            final Pair<Extractor, Boolean> extractor = this.extractorFactory.createExtractor(this.previousExtractor, dataSpec.uri, super.trackFormat, this.muxedCaptionFormats, this.drmInitData, this.timestampAdjuster, dataSource.getResponseHeaders(), defaultExtractorInput);
            this.extractor = (Extractor)extractor.first;
            final Extractor extractor2 = this.extractor;
            final Extractor previousExtractor = this.previousExtractor;
            boolean initLoadCompleted = true;
            final boolean b = extractor2 == previousExtractor;
            if (extractor.second) {
                final HlsSampleStreamWrapper output = this.output;
                long sampleOffsetUs;
                if (peekId3PrivTimestamp != -9223372036854775807L) {
                    sampleOffsetUs = this.timestampAdjuster.adjustTsTimestamp(peekId3PrivTimestamp);
                }
                else {
                    sampleOffsetUs = super.startTimeUs;
                }
                output.setSampleOffsetUs(sampleOffsetUs);
            }
            if (!b || this.initDataSpec == null) {
                initLoadCompleted = false;
            }
            this.initLoadCompleted = initLoadCompleted;
            this.output.init(this.uid, this.shouldSpliceIn, b);
            if (!b) {
                this.extractor.init(this.output);
            }
        }
        return defaultExtractorInput;
    }
    
    @Override
    public void cancelLoad() {
        this.loadCanceled = true;
    }
    
    public void init(final HlsSampleStreamWrapper output) {
        this.output = output;
    }
    
    @Override
    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }
    
    @Override
    public void load() throws IOException, InterruptedException {
        this.maybeLoadInitData();
        if (!this.loadCanceled) {
            if (!this.hasGapTag) {
                this.loadMedia();
            }
            this.loadCompleted = true;
        }
    }
}
