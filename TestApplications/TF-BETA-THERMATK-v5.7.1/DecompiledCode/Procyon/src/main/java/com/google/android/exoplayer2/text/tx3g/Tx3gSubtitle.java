// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.tx3g;

import com.google.android.exoplayer2.util.Assertions;
import java.util.Collections;
import com.google.android.exoplayer2.text.Cue;
import java.util.List;
import com.google.android.exoplayer2.text.Subtitle;

final class Tx3gSubtitle implements Subtitle
{
    public static final Tx3gSubtitle EMPTY;
    private final List<Cue> cues;
    
    static {
        EMPTY = new Tx3gSubtitle();
    }
    
    private Tx3gSubtitle() {
        this.cues = Collections.emptyList();
    }
    
    public Tx3gSubtitle(final Cue o) {
        this.cues = Collections.singletonList(o);
    }
    
    @Override
    public List<Cue> getCues(final long n) {
        List<Cue> list;
        if (n >= 0L) {
            list = this.cues;
        }
        else {
            list = Collections.emptyList();
        }
        return list;
    }
    
    @Override
    public long getEventTime(final int n) {
        Assertions.checkArgument(n == 0);
        return 0L;
    }
    
    @Override
    public int getEventTimeCount() {
        return 1;
    }
    
    @Override
    public int getNextEventTimeIndex(final long n) {
        int n2;
        if (n < 0L) {
            n2 = 0;
        }
        else {
            n2 = -1;
        }
        return n2;
    }
}
