// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.ssa;

import com.google.android.exoplayer2.util.Assertions;
import java.util.Collections;
import com.google.android.exoplayer2.util.Util;
import java.util.List;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;

final class SsaSubtitle implements Subtitle
{
    private final long[] cueTimesUs;
    private final Cue[] cues;
    
    public SsaSubtitle(final Cue[] cues, final long[] cueTimesUs) {
        this.cues = cues;
        this.cueTimesUs = cueTimesUs;
    }
    
    @Override
    public List<Cue> getCues(final long n) {
        final int binarySearchFloor = Util.binarySearchFloor(this.cueTimesUs, n, true, false);
        if (binarySearchFloor != -1) {
            final Cue[] cues = this.cues;
            if (cues[binarySearchFloor] != null) {
                return Collections.singletonList(cues[binarySearchFloor]);
            }
        }
        return Collections.emptyList();
    }
    
    @Override
    public long getEventTime(final int n) {
        final boolean b = true;
        Assertions.checkArgument(n >= 0);
        Assertions.checkArgument(n < this.cueTimesUs.length && b);
        return this.cueTimesUs[n];
    }
    
    @Override
    public int getEventTimeCount() {
        return this.cueTimesUs.length;
    }
    
    @Override
    public int getNextEventTimeIndex(final long n) {
        int binarySearchCeil = Util.binarySearchCeil(this.cueTimesUs, n, false, false);
        if (binarySearchCeil >= this.cueTimesUs.length) {
            binarySearchCeil = -1;
        }
        return binarySearchCeil;
    }
}
