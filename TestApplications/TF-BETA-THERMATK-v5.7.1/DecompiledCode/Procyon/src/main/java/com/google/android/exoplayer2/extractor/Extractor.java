// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import java.io.IOException;

public interface Extractor
{
    void init(final ExtractorOutput p0);
    
    int read(final ExtractorInput p0, final PositionHolder p1) throws IOException, InterruptedException;
    
    void release();
    
    void seek(final long p0, final long p1);
    
    boolean sniff(final ExtractorInput p0) throws IOException, InterruptedException;
}
