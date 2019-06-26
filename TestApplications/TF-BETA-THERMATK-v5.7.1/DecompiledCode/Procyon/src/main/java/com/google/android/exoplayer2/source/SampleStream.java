// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.FormatHolder;
import java.io.IOException;

public interface SampleStream
{
    boolean isReady();
    
    void maybeThrowError() throws IOException;
    
    int readData(final FormatHolder p0, final DecoderInputBuffer p1, final boolean p2);
    
    int skipData(final long p0);
}
