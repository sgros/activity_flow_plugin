// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.ParsableByteArray;

public interface SectionPayloadReader
{
    void consume(final ParsableByteArray p0);
    
    void init(final TimestampAdjuster p0, final ExtractorOutput p1, final TsPayloadReader.TrackIdGenerator p2);
}
