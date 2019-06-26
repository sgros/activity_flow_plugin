// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.FormatHolder;
import java.io.IOException;

public final class EmptySampleStream implements SampleStream
{
    @Override
    public boolean isReady() {
        return true;
    }
    
    @Override
    public void maybeThrowError() throws IOException {
    }
    
    @Override
    public int readData(final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b) {
        decoderInputBuffer.setFlags(4);
        return -4;
    }
    
    @Override
    public int skipData(final long n) {
        return 0;
    }
}
