// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text;

import java.util.List;

public interface Subtitle
{
    List<Cue> getCues(final long p0);
    
    long getEventTime(final int p0);
    
    int getEventTimeCount();
    
    int getNextEventTimeIndex(final long p0);
}
