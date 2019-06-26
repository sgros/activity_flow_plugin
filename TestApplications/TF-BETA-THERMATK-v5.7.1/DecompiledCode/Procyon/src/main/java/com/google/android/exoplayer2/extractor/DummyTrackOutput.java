// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import java.io.EOFException;
import com.google.android.exoplayer2.Format;

public final class DummyTrackOutput implements TrackOutput
{
    @Override
    public void format(final Format format) {
    }
    
    @Override
    public int sampleData(final ExtractorInput extractorInput, int skip, final boolean b) throws IOException, InterruptedException {
        skip = extractorInput.skip(skip);
        if (skip != -1) {
            return skip;
        }
        if (b) {
            return -1;
        }
        throw new EOFException();
    }
    
    @Override
    public void sampleData(final ParsableByteArray parsableByteArray, final int n) {
        parsableByteArray.skipBytes(n);
    }
    
    @Override
    public void sampleMetadata(final long n, final int n2, final int n3, final int n4, final CryptoData cryptoData) {
    }
}
