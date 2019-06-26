// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mkv;

import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;

interface EbmlReader
{
    void init(final EbmlReaderOutput p0);
    
    boolean read(final ExtractorInput p0) throws IOException, InterruptedException;
    
    void reset();
}
