// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.pgs;

import com.google.android.exoplayer2.text.Cue;
import java.util.List;
import com.google.android.exoplayer2.text.Subtitle;

final class PgsSubtitle implements Subtitle
{
    private final List<Cue> cues;
    
    public PgsSubtitle(final List<Cue> cues) {
        this.cues = cues;
    }
    
    @Override
    public List<Cue> getCues(final long n) {
        return this.cues;
    }
    
    @Override
    public long getEventTime(final int n) {
        return 0L;
    }
    
    @Override
    public int getEventTimeCount() {
        return 1;
    }
    
    @Override
    public int getNextEventTimeIndex(final long n) {
        return -1;
    }
}
