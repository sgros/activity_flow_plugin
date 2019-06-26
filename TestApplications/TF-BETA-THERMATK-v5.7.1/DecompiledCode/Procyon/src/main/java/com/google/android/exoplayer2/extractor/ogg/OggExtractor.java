// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.PositionHolder;
import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;

public class OggExtractor implements Extractor
{
    public static final ExtractorsFactory FACTORY;
    private ExtractorOutput output;
    private StreamReader streamReader;
    private boolean streamReaderInitialized;
    
    static {
        FACTORY = (ExtractorsFactory)_$$Lambda$OggExtractor$Ibu4KG2n586HVQ8R_UQJ8hUhsso.INSTANCE;
    }
    
    private static ParsableByteArray resetPosition(final ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(0);
        return parsableByteArray;
    }
    
    private boolean sniffInternal(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final OggPageHeader oggPageHeader = new OggPageHeader();
        if (oggPageHeader.populate(extractorInput, true)) {
            if ((oggPageHeader.type & 0x2) == 0x2) {
                final int min = Math.min(oggPageHeader.bodySize, 8);
                final ParsableByteArray parsableByteArray = new ParsableByteArray(min);
                extractorInput.peekFully(parsableByteArray.data, 0, min);
                resetPosition(parsableByteArray);
                if (FlacReader.verifyBitstreamType(parsableByteArray)) {
                    this.streamReader = new FlacReader();
                }
                else {
                    resetPosition(parsableByteArray);
                    if (VorbisReader.verifyBitstreamType(parsableByteArray)) {
                        this.streamReader = new VorbisReader();
                    }
                    else {
                        resetPosition(parsableByteArray);
                        if (!OpusReader.verifyBitstreamType(parsableByteArray)) {
                            return false;
                        }
                        this.streamReader = new OpusReader();
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void init(final ExtractorOutput output) {
        this.output = output;
    }
    
    @Override
    public int read(final ExtractorInput extractorInput, final PositionHolder positionHolder) throws IOException, InterruptedException {
        if (this.streamReader == null) {
            if (!this.sniffInternal(extractorInput)) {
                throw new ParserException("Failed to determine bitstream type");
            }
            extractorInput.resetPeekPosition();
        }
        if (!this.streamReaderInitialized) {
            final TrackOutput track = this.output.track(0, 1);
            this.output.endTracks();
            this.streamReader.init(this.output, track);
            this.streamReaderInitialized = true;
        }
        return this.streamReader.read(extractorInput, positionHolder);
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void seek(final long n, final long n2) {
        final StreamReader streamReader = this.streamReader;
        if (streamReader != null) {
            streamReader.seek(n, n2);
        }
    }
    
    @Override
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        try {
            return this.sniffInternal(extractorInput);
        }
        catch (ParserException ex) {
            return false;
        }
    }
}
