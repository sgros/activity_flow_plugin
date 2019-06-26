// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.rawcc;

import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.ParserException;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.Extractor;

public final class RawCcExtractor implements Extractor
{
    private static final int HEADER_ID;
    private final ParsableByteArray dataScratch;
    private final Format format;
    private int parserState;
    private int remainingSampleCount;
    private int sampleBytesWritten;
    private long timestampUs;
    private TrackOutput trackOutput;
    private int version;
    
    static {
        HEADER_ID = Util.getIntegerCodeForString("RCC\u0001");
    }
    
    public RawCcExtractor(final Format format) {
        this.format = format;
        this.dataScratch = new ParsableByteArray(9);
        this.parserState = 0;
    }
    
    private boolean parseHeader(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        this.dataScratch.reset();
        if (!extractorInput.readFully(this.dataScratch.data, 0, 8, true)) {
            return false;
        }
        if (this.dataScratch.readInt() == RawCcExtractor.HEADER_ID) {
            this.version = this.dataScratch.readUnsignedByte();
            return true;
        }
        throw new IOException("Input not RawCC");
    }
    
    private void parseSamples(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        while (this.remainingSampleCount > 0) {
            this.dataScratch.reset();
            extractorInput.readFully(this.dataScratch.data, 0, 3);
            this.trackOutput.sampleData(this.dataScratch, 3);
            this.sampleBytesWritten += 3;
            --this.remainingSampleCount;
        }
        final int sampleBytesWritten = this.sampleBytesWritten;
        if (sampleBytesWritten > 0) {
            this.trackOutput.sampleMetadata(this.timestampUs, 1, sampleBytesWritten, 0, null);
        }
    }
    
    private boolean parseTimestampAndSampleCount(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        this.dataScratch.reset();
        final int version = this.version;
        if (version == 0) {
            if (!extractorInput.readFully(this.dataScratch.data, 0, 5, true)) {
                return false;
            }
            this.timestampUs = this.dataScratch.readUnsignedInt() * 1000L / 45L;
        }
        else {
            if (version != 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unsupported version number: ");
                sb.append(this.version);
                throw new ParserException(sb.toString());
            }
            if (!extractorInput.readFully(this.dataScratch.data, 0, 9, true)) {
                return false;
            }
            this.timestampUs = this.dataScratch.readLong();
        }
        this.remainingSampleCount = this.dataScratch.readUnsignedByte();
        this.sampleBytesWritten = 0;
        return true;
    }
    
    @Override
    public void init(final ExtractorOutput extractorOutput) {
        extractorOutput.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
        this.trackOutput = extractorOutput.track(0, 3);
        extractorOutput.endTracks();
        this.trackOutput.format(this.format);
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        while (true) {
            final int parserState = this.parserState;
            if (parserState != 0) {
                if (parserState != 1) {
                    if (parserState == 2) {
                        this.parseSamples(extractorInput);
                        this.parserState = 1;
                        return 0;
                    }
                    throw new IllegalStateException();
                }
                else {
                    if (!this.parseTimestampAndSampleCount(extractorInput)) {
                        this.parserState = 0;
                        return -1;
                    }
                    this.parserState = 2;
                }
            }
            else {
                if (!this.parseHeader(extractorInput)) {
                    return -1;
                }
                this.parserState = 1;
            }
        }
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        this.parserState = 0;
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        this.dataScratch.reset();
        final byte[] data = this.dataScratch.data;
        boolean b = false;
        extractorInput.peekFully(data, 0, 8);
        if (this.dataScratch.readInt() == RawCcExtractor.HEADER_ID) {
            b = true;
        }
        return b;
    }
}
