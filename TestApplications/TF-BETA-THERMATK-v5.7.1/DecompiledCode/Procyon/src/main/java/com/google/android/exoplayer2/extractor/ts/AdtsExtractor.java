// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.ConstantBitrateSeekMap;
import com.google.android.exoplayer2.extractor.SeekMap;
import java.io.IOException;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public final class AdtsExtractor implements Extractor
{
    public static final ExtractorsFactory FACTORY;
    private static final int ID3_TAG;
    private int averageFrameSize;
    private ExtractorOutput extractorOutput;
    private long firstFramePosition;
    private long firstSampleTimestampUs;
    private final long firstStreamSampleTimestampUs;
    private final int flags;
    private boolean hasCalculatedAverageFrameSize;
    private boolean hasOutputSeekMap;
    private final ParsableByteArray packetBuffer;
    private final AdtsReader reader;
    private final ParsableByteArray scratch;
    private final ParsableBitArray scratchBits;
    private boolean startedPacket;
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$AdtsExtractor$cqGYwjddB4W6E3ogPGiWfjTa23c.INSTANCE;
        ID3_TAG = Util.getIntegerCodeForString("ID3");
    }
    
    public AdtsExtractor() {
        this(0L);
    }
    
    public AdtsExtractor(final long n) {
        this(n, 0);
    }
    
    public AdtsExtractor(final long n, final int flags) {
        this.firstStreamSampleTimestampUs = n;
        this.firstSampleTimestampUs = n;
        this.flags = flags;
        this.reader = new AdtsReader(true);
        this.packetBuffer = new ParsableByteArray(2048);
        this.averageFrameSize = -1;
        this.firstFramePosition = -1L;
        this.scratch = new ParsableByteArray(10);
        this.scratchBits = new ParsableBitArray(this.scratch.data);
    }
    
    private void calculateAverageFrameSize(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.hasCalculatedAverageFrameSize) {
            return;
        }
        this.averageFrameSize = -1;
        extractorInput.resetPeekPosition();
        final long position = extractorInput.getPosition();
        long n = 0L;
        if (position == 0L) {
            this.peekId3Header(extractorInput);
        }
        int n2 = 0;
        while (true) {
            int bits;
            do {
                int n3 = n2;
                long n4 = n;
                if (extractorInput.peekFully(this.scratch.data, 0, 2, true)) {
                    this.scratch.setPosition(0);
                    if (!AdtsReader.isAdtsSyncWord(this.scratch.readUnsignedShort())) {
                        n3 = 0;
                        n4 = n;
                    }
                    else if (!extractorInput.peekFully(this.scratch.data, 0, 4, true)) {
                        n3 = n2;
                        n4 = n;
                    }
                    else {
                        this.scratchBits.setPosition(14);
                        bits = this.scratchBits.readBits(13);
                        if (bits <= 6) {
                            this.hasCalculatedAverageFrameSize = true;
                            throw new ParserException("Malformed ADTS stream");
                        }
                        n4 = n + bits;
                        n3 = n2 + 1;
                        if (n3 != 1000) {
                            n2 = n3;
                            n = n4;
                            continue;
                        }
                    }
                }
                extractorInput.resetPeekPosition();
                if (n3 > 0) {
                    this.averageFrameSize = (int)(n4 / n3);
                }
                else {
                    this.averageFrameSize = -1;
                }
                this.hasCalculatedAverageFrameSize = true;
                return;
            } while (extractorInput.advancePeekPosition(bits - 6, true));
            continue;
        }
    }
    
    private static int getBitrateFromFrameSize(final int n, final long n2) {
        return (int)(n * 8 * 1000000L / n2);
    }
    
    private SeekMap getConstantBitrateSeekMap(final long n) {
        return new ConstantBitrateSeekMap(n, this.firstFramePosition, getBitrateFromFrameSize(this.averageFrameSize, this.reader.getSampleDurationUs()), this.averageFrameSize);
    }
    
    private void maybeOutputSeekMap(final long n, final boolean b, final boolean b2) {
        if (this.hasOutputSeekMap) {
            return;
        }
        final boolean b3 = b && this.averageFrameSize > 0;
        if (b3 && this.reader.getSampleDurationUs() == -9223372036854775807L && !b2) {
            return;
        }
        final ExtractorOutput extractorOutput = this.extractorOutput;
        Assertions.checkNotNull(extractorOutput);
        final ExtractorOutput extractorOutput2 = extractorOutput;
        if (b3 && this.reader.getSampleDurationUs() != -9223372036854775807L) {
            extractorOutput2.seekMap(this.getConstantBitrateSeekMap(n));
        }
        else {
            extractorOutput2.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
        }
        this.hasOutputSeekMap = true;
    }
    
    private int peekId3Header(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        int n = 0;
        while (true) {
            extractorInput.peekFully(this.scratch.data, 0, 10);
            this.scratch.setPosition(0);
            if (this.scratch.readUnsignedInt24() != AdtsExtractor.ID3_TAG) {
                break;
            }
            this.scratch.skipBytes(3);
            final int synchSafeInt = this.scratch.readSynchSafeInt();
            n += synchSafeInt + 10;
            extractorInput.advancePeekPosition(synchSafeInt);
        }
        extractorInput.resetPeekPosition();
        extractorInput.advancePeekPosition(n);
        if (this.firstFramePosition == -1L) {
            this.firstFramePosition = n;
        }
        return n;
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.reader.createTracks(extractorOutput, new TsPayloadReader.TrackIdGenerator(0, 1));
        extractorOutput.endTracks();
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final long length = extractorInput.getLength();
        final boolean b = (this.flags & 0x1) != 0x0 && length != -1L;
        if (b) {
            this.calculateAverageFrameSize(extractorInput);
        }
        final int read = extractorInput.read(this.packetBuffer.data, 0, 2048);
        final boolean b2 = read == -1;
        this.maybeOutputSeekMap(length, b, b2);
        if (b2) {
            return -1;
        }
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(read);
        if (!this.startedPacket) {
            this.reader.packetStarted(this.firstSampleTimestampUs, 4);
            this.startedPacket = true;
        }
        this.reader.consume(this.packetBuffer);
        return 0;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        this.startedPacket = false;
        this.reader.seek();
        this.firstSampleTimestampUs = this.firstStreamSampleTimestampUs + n2;
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        int peekId3Header;
        final int n = peekId3Header = this.peekId3Header(extractorInput);
        while (true) {
            int n2 = 0;
            int n3 = 0;
            while (true) {
                extractorInput.peekFully(this.scratch.data, 0, 2);
                this.scratch.setPosition(0);
                if (!AdtsReader.isAdtsSyncWord(this.scratch.readUnsignedShort())) {
                    extractorInput.resetPeekPosition();
                    if (++peekId3Header - n >= 8192) {
                        return false;
                    }
                    extractorInput.advancePeekPosition(peekId3Header);
                    break;
                }
                else {
                    if (++n2 >= 4 && n3 > 188) {
                        return true;
                    }
                    extractorInput.peekFully(this.scratch.data, 0, 4);
                    this.scratchBits.setPosition(14);
                    final int bits = this.scratchBits.readBits(13);
                    if (bits <= 6) {
                        return false;
                    }
                    extractorInput.advancePeekPosition(bits - 6);
                    n3 += bits;
                }
            }
        }
    }
}
