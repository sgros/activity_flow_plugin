// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.audio.Ac3Util;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public final class Ac3Extractor implements Extractor
{
    public static final ExtractorsFactory FACTORY;
    private static final int ID3_TAG;
    private final long firstSampleTimestampUs;
    private final Ac3Reader reader;
    private final ParsableByteArray sampleData;
    private boolean startedPacket;
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$Ac3Extractor$c2Fqr1pF6vjFNOhLk9sPPtkNnGE.INSTANCE;
        ID3_TAG = Util.getIntegerCodeForString("ID3");
    }
    
    public Ac3Extractor() {
        this(0L);
    }
    
    public Ac3Extractor(final long firstSampleTimestampUs) {
        this.firstSampleTimestampUs = firstSampleTimestampUs;
        this.reader = new Ac3Reader();
        this.sampleData = new ParsableByteArray(2786);
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        this.reader.createTracks(extractorOutput, new TsPayloadReader.TrackIdGenerator(0, 1));
        extractorOutput.endTracks();
        extractorOutput.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        final int read = extractorInput.read(this.sampleData.data, 0, 2786);
        if (read == -1) {
            return -1;
        }
        this.sampleData.setPosition(0);
        this.sampleData.setLimit(read);
        if (!this.startedPacket) {
            this.reader.packetStarted(this.firstSampleTimestampUs, 4);
            this.startedPacket = true;
        }
        this.reader.consume(this.sampleData);
        return 0;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        this.startedPacket = false;
        this.reader.seek();
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final ParsableByteArray parsableByteArray = new ParsableByteArray(10);
        int n = 0;
        while (true) {
            extractorInput.peekFully(parsableByteArray.data, 0, 10);
            parsableByteArray.setPosition(0);
            if (parsableByteArray.readUnsignedInt24() != Ac3Extractor.ID3_TAG) {
                break;
            }
            parsableByteArray.skipBytes(3);
            final int synchSafeInt = parsableByteArray.readSynchSafeInt();
            n += synchSafeInt + 10;
            extractorInput.advancePeekPosition(synchSafeInt);
        }
        extractorInput.resetPeekPosition();
        extractorInput.advancePeekPosition(n);
        int n2 = n;
        while (true) {
            int n3 = 0;
            while (true) {
                extractorInput.peekFully(parsableByteArray.data, 0, 6);
                parsableByteArray.setPosition(0);
                if (parsableByteArray.readUnsignedShort() != 2935) {
                    extractorInput.resetPeekPosition();
                    if (++n2 - n >= 8192) {
                        return false;
                    }
                    extractorInput.advancePeekPosition(n2);
                    break;
                }
                else {
                    if (++n3 >= 4) {
                        return true;
                    }
                    final int ac3SyncframeSize = Ac3Util.parseAc3SyncframeSize(parsableByteArray.data);
                    if (ac3SyncframeSize == -1) {
                        return false;
                    }
                    extractorInput.advancePeekPosition(ac3SyncframeSize - 6);
                }
            }
        }
    }
}
