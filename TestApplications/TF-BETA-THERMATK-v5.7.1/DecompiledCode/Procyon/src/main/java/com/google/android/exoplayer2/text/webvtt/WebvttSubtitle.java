// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.webvtt;

import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Collections;
import android.text.SpannableStringBuilder;
import java.util.ArrayList;
import com.google.android.exoplayer2.text.Cue;
import java.util.Arrays;
import java.util.List;
import com.google.android.exoplayer2.text.Subtitle;

final class WebvttSubtitle implements Subtitle
{
    private final long[] cueTimesUs;
    private final List<WebvttCue> cues;
    private final int numCues;
    private final long[] sortedCueTimesUs;
    
    public WebvttSubtitle(final List<WebvttCue> cues) {
        this.cues = cues;
        this.numCues = cues.size();
        this.cueTimesUs = new long[this.numCues * 2];
        for (int i = 0; i < this.numCues; ++i) {
            final WebvttCue webvttCue = cues.get(i);
            final int n = i * 2;
            final long[] cueTimesUs = this.cueTimesUs;
            cueTimesUs[n] = webvttCue.startTime;
            cueTimesUs[n + 1] = webvttCue.endTime;
        }
        final long[] cueTimesUs2 = this.cueTimesUs;
        Arrays.sort(this.sortedCueTimesUs = Arrays.copyOf(cueTimesUs2, cueTimesUs2.length));
    }
    
    @Override
    public List<Cue> getCues(final long n) {
        Object o = null;
        int i = 0;
        ArrayList<ArrayList<ArrayList>> list;
        Object e = list = null;
        while (i < this.numCues) {
            final long[] cueTimesUs = this.cueTimesUs;
            final int n2 = i * 2;
            SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder)o;
            Object e2 = e;
            Object o2 = list;
            if (cueTimesUs[n2] <= n) {
                spannableStringBuilder = (SpannableStringBuilder)o;
                e2 = e;
                o2 = list;
                if (n < cueTimesUs[n2 + 1]) {
                    if ((o2 = list) == null) {
                        o2 = new ArrayList<ArrayList<ArrayList>>();
                    }
                    e2 = this.cues.get(i);
                    if (((WebvttCue)e2).isNormalCue()) {
                        if (e == null) {
                            spannableStringBuilder = (SpannableStringBuilder)o;
                        }
                        else if (o == null) {
                            spannableStringBuilder = new SpannableStringBuilder();
                            spannableStringBuilder.append(((Cue)e).text).append((CharSequence)"\n").append(((Cue)e2).text);
                            e2 = e;
                        }
                        else {
                            ((SpannableStringBuilder)o).append((CharSequence)"\n").append(((Cue)e2).text);
                            spannableStringBuilder = (SpannableStringBuilder)o;
                            e2 = e;
                        }
                    }
                    else {
                        ((ArrayList<ArrayList<ArrayList>>)o2).add((ArrayList<ArrayList>)e2);
                        e2 = e;
                        spannableStringBuilder = (SpannableStringBuilder)o;
                    }
                }
            }
            ++i;
            o = spannableStringBuilder;
            e = e2;
            list = (ArrayList<ArrayList<ArrayList>>)o2;
        }
        if (o != null) {
            list.add((ArrayList<ArrayList>)new WebvttCue((CharSequence)o));
        }
        else if (e != null) {
            list.add((ArrayList<ArrayList>)e);
        }
        if (list != null) {
            return (List<Cue>)list;
        }
        return Collections.emptyList();
    }
    
    @Override
    public long getEventTime(final int n) {
        final boolean b = true;
        Assertions.checkArgument(n >= 0);
        Assertions.checkArgument(n < this.sortedCueTimesUs.length && b);
        return this.sortedCueTimesUs[n];
    }
    
    @Override
    public int getEventTimeCount() {
        return this.sortedCueTimesUs.length;
    }
    
    @Override
    public int getNextEventTimeIndex(final long n) {
        int binarySearchCeil = Util.binarySearchCeil(this.sortedCueTimesUs, n, false, false);
        if (binarySearchCeil >= this.sortedCueTimesUs.length) {
            binarySearchCeil = -1;
        }
        return binarySearchCeil;
    }
}
