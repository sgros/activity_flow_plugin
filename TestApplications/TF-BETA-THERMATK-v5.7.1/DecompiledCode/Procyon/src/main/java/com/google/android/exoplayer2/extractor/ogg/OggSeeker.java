// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;

interface OggSeeker
{
    SeekMap createSeekMap();
    
    long read(final ExtractorInput p0) throws IOException, InterruptedException;
    
    long startSeek(final long p0);
}
