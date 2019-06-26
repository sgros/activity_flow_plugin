// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.ParsableBitArray;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import android.util.SparseArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public final class PsExtractor implements Extractor
{
    public static final ExtractorsFactory FACTORY;
    private final PsDurationReader durationReader;
    private boolean foundAllTracks;
    private boolean foundAudioTrack;
    private boolean foundVideoTrack;
    private boolean hasOutputSeekMap;
    private long lastTrackPosition;
    private ExtractorOutput output;
    private PsBinarySearchSeeker psBinarySearchSeeker;
    private final ParsableByteArray psPacketBuffer;
    private final SparseArray<PesReader> psPayloadReaders;
    private final TimestampAdjuster timestampAdjuster;
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$PsExtractor$U8l9TedlJUwsYwV9EOSFo_ngcXY.INSTANCE;
    }
    
    public PsExtractor() {
        this(new TimestampAdjuster(0L));
    }
    
    public PsExtractor(final TimestampAdjuster timestampAdjuster) {
        this.timestampAdjuster = timestampAdjuster;
        this.psPacketBuffer = new ParsableByteArray(4096);
        this.psPayloadReaders = (SparseArray<PesReader>)new SparseArray();
        this.durationReader = new PsDurationReader();
    }
    
    private void maybeOutputSeekMap(final long n) {
        if (!this.hasOutputSeekMap) {
            this.hasOutputSeekMap = true;
            if (this.durationReader.getDurationUs() != -9223372036854775807L) {
                this.psBinarySearchSeeker = new PsBinarySearchSeeker(this.durationReader.getScrTimestampAdjuster(), this.durationReader.getDurationUs(), n);
                this.output.seekMap(this.psBinarySearchSeeker.getSeekMap());
            }
            else {
                this.output.seekMap(new SeekMap.Unseekable(this.durationReader.getDurationUs()));
            }
        }
    }
    
    @Override
    public void init(final ExtractorOutput output) {
        this.output = output;
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final long length = extractorInput.getLength();
        if (length != -1L && !this.durationReader.isDurationReadFinished()) {
            return this.durationReader.readDuration(extractorInput, positionHolder);
        }
        this.maybeOutputSeekMap(length);
        final PsBinarySearchSeeker psBinarySearchSeeker = this.psBinarySearchSeeker;
        final ElementaryStreamReader elementaryStreamReader = null;
        if (psBinarySearchSeeker != null && psBinarySearchSeeker.isSeeking()) {
            return this.psBinarySearchSeeker.handlePendingSeek(extractorInput, positionHolder, null);
        }
        extractorInput.resetPeekPosition();
        long n;
        if (length != -1L) {
            n = length - extractorInput.getPeekPosition();
        }
        else {
            n = -1L;
        }
        if (n != -1L && n < 4L) {
            return -1;
        }
        if (!extractorInput.peekFully(this.psPacketBuffer.data, 0, 4, true)) {
            return -1;
        }
        this.psPacketBuffer.setPosition(0);
        final int int1 = this.psPacketBuffer.readInt();
        if (int1 == 441) {
            return -1;
        }
        if (int1 == 442) {
            extractorInput.peekFully(this.psPacketBuffer.data, 0, 10);
            this.psPacketBuffer.setPosition(9);
            extractorInput.skipFully((this.psPacketBuffer.readUnsignedByte() & 0x7) + 14);
            return 0;
        }
        if (int1 == 443) {
            extractorInput.peekFully(this.psPacketBuffer.data, 0, 2);
            this.psPacketBuffer.setPosition(0);
            extractorInput.skipFully(this.psPacketBuffer.readUnsignedShort() + 6);
            return 0;
        }
        if ((int1 & 0xFFFFFF00) >> 8 != 1) {
            extractorInput.skipFully(1);
            return 0;
        }
        final int n2 = int1 & 0xFF;
        PesReader pesReader2;
        final PesReader pesReader = pesReader2 = (PesReader)this.psPayloadReaders.get(n2);
        if (!this.foundAllTracks) {
            PesReader pesReader3;
            if ((pesReader3 = pesReader) == null) {
                ElementaryStreamReader elementaryStreamReader2;
                if (n2 == 189) {
                    elementaryStreamReader2 = new Ac3Reader();
                    this.foundAudioTrack = true;
                    this.lastTrackPosition = extractorInput.getPosition();
                }
                else if ((n2 & 0xE0) == 0xC0) {
                    elementaryStreamReader2 = new MpegAudioReader();
                    this.foundAudioTrack = true;
                    this.lastTrackPosition = extractorInput.getPosition();
                }
                else {
                    elementaryStreamReader2 = elementaryStreamReader;
                    if ((n2 & 0xF0) == 0xE0) {
                        elementaryStreamReader2 = new H262Reader();
                        this.foundVideoTrack = true;
                        this.lastTrackPosition = extractorInput.getPosition();
                    }
                }
                pesReader3 = pesReader;
                if (elementaryStreamReader2 != null) {
                    elementaryStreamReader2.createTracks(this.output, new TsPayloadReader.TrackIdGenerator(n2, 256));
                    pesReader3 = new PesReader(elementaryStreamReader2, this.timestampAdjuster);
                    this.psPayloadReaders.put(n2, (Object)pesReader3);
                }
            }
            long n3;
            if (this.foundAudioTrack && this.foundVideoTrack) {
                n3 = this.lastTrackPosition + 8192L;
            }
            else {
                n3 = 1048576L;
            }
            pesReader2 = pesReader3;
            if (extractorInput.getPosition() > n3) {
                this.foundAllTracks = true;
                this.output.endTracks();
                pesReader2 = pesReader3;
            }
        }
        extractorInput.peekFully(this.psPacketBuffer.data, 0, 2);
        this.psPacketBuffer.setPosition(0);
        final int n4 = this.psPacketBuffer.readUnsignedShort() + 6;
        if (pesReader2 == null) {
            extractorInput.skipFully(n4);
        }
        else {
            this.psPacketBuffer.reset(n4);
            extractorInput.readFully(this.psPacketBuffer.data, 0, n4);
            this.psPacketBuffer.setPosition(6);
            pesReader2.consume(this.psPacketBuffer);
            final ParsableByteArray psPacketBuffer = this.psPacketBuffer;
            psPacketBuffer.setLimit(psPacketBuffer.capacity());
        }
        return 0;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(long timestampOffsetUs, final long n) {
        timestampOffsetUs = this.timestampAdjuster.getTimestampOffsetUs();
        final int n2 = 0;
        if (timestampOffsetUs == -9223372036854775807L || (this.timestampAdjuster.getFirstSampleTimestampUs() != 0L && this.timestampAdjuster.getFirstSampleTimestampUs() != n)) {
            this.timestampAdjuster.reset();
            this.timestampAdjuster.setFirstSampleTimestampUs(n);
        }
        final PsBinarySearchSeeker psBinarySearchSeeker = this.psBinarySearchSeeker;
        int i = n2;
        if (psBinarySearchSeeker != null) {
            psBinarySearchSeeker.setSeekTargetUs(n);
            i = n2;
        }
        while (i < this.psPayloadReaders.size()) {
            ((PesReader)this.psPayloadReaders.valueAt(i)).seek();
            ++i;
        }
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final byte[] array = new byte[14];
        boolean b = false;
        extractorInput.peekFully(array, 0, 14);
        if (0x1BA != ((array[0] & 0xFF) << 24 | (array[1] & 0xFF) << 16 | (array[2] & 0xFF) << 8 | (array[3] & 0xFF))) {
            return false;
        }
        if ((array[4] & 0xC4) != 0x44) {
            return false;
        }
        if ((array[6] & 0x4) != 0x4) {
            return false;
        }
        if ((array[8] & 0x4) != 0x4) {
            return false;
        }
        if ((array[9] & 0x1) != 0x1) {
            return false;
        }
        if ((array[12] & 0x3) != 0x3) {
            return false;
        }
        extractorInput.advancePeekPosition(array[13] & 0x7);
        extractorInput.peekFully(array, 0, 3);
        if (0x1 == ((array[0] & 0xFF) << 16 | (array[1] & 0xFF) << 8 | (array[2] & 0xFF))) {
            b = true;
        }
        return b;
    }
    
    private static final class PesReader
    {
        private boolean dtsFlag;
        private int extendedHeaderLength;
        private final ElementaryStreamReader pesPayloadReader;
        private final ParsableBitArray pesScratch;
        private boolean ptsFlag;
        private boolean seenFirstDts;
        private long timeUs;
        private final TimestampAdjuster timestampAdjuster;
        
        public PesReader(final ElementaryStreamReader pesPayloadReader, final TimestampAdjuster timestampAdjuster) {
            this.pesPayloadReader = pesPayloadReader;
            this.timestampAdjuster = timestampAdjuster;
            this.pesScratch = new ParsableBitArray(new byte[64]);
        }
        
        private void parseHeader() {
            this.pesScratch.skipBits(8);
            this.ptsFlag = this.pesScratch.readBit();
            this.dtsFlag = this.pesScratch.readBit();
            this.pesScratch.skipBits(6);
            this.extendedHeaderLength = this.pesScratch.readBits(8);
        }
        
        private void parseHeaderExtension() {
            this.timeUs = 0L;
            if (this.ptsFlag) {
                this.pesScratch.skipBits(4);
                final long n = this.pesScratch.readBits(3);
                this.pesScratch.skipBits(1);
                final long n2 = this.pesScratch.readBits(15) << 15;
                this.pesScratch.skipBits(1);
                final long n3 = this.pesScratch.readBits(15);
                this.pesScratch.skipBits(1);
                if (!this.seenFirstDts && this.dtsFlag) {
                    this.pesScratch.skipBits(4);
                    final long n4 = this.pesScratch.readBits(3);
                    this.pesScratch.skipBits(1);
                    final long n5 = this.pesScratch.readBits(15) << 15;
                    this.pesScratch.skipBits(1);
                    final long n6 = this.pesScratch.readBits(15);
                    this.pesScratch.skipBits(1);
                    this.timestampAdjuster.adjustTsTimestamp(n4 << 30 | n5 | n6);
                    this.seenFirstDts = true;
                }
                this.timeUs = this.timestampAdjuster.adjustTsTimestamp(n << 30 | n2 | n3);
            }
        }
        
        public void consume(final ParsableByteArray parsableByteArray) throws ParserException {
            parsableByteArray.readBytes(this.pesScratch.data, 0, 3);
            this.pesScratch.setPosition(0);
            this.parseHeader();
            parsableByteArray.readBytes(this.pesScratch.data, 0, this.extendedHeaderLength);
            this.pesScratch.setPosition(0);
            this.parseHeaderExtension();
            this.pesPayloadReader.packetStarted(this.timeUs, 4);
            this.pesPayloadReader.consume(parsableByteArray);
            this.pesPayloadReader.packetFinished();
        }
        
        public void seek() {
            this.seenFirstDts = false;
            this.pesPayloadReader.seek();
        }
    }
}
