// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.ParsableByteArray;

public interface ElementaryStreamReader
{
    void consume(final ParsableByteArray p0) throws ParserException;
    
    void createTracks(final ExtractorOutput p0, final TsPayloadReader.TrackIdGenerator p1);
    
    void packetFinished();
    
    void packetStarted(final long p0, final int p1);
    
    void seek();
}
