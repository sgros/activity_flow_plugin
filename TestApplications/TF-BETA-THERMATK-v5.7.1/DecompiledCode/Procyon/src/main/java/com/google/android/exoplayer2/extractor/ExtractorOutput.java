// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

public interface ExtractorOutput
{
    void endTracks();
    
    void seekMap(final SeekMap p0);
    
    TrackOutput track(final int p0, final int p1);
}
