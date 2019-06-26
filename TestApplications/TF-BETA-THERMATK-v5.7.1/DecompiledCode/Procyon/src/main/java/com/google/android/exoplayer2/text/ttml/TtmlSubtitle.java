// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.ttml;

import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.text.Cue;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import com.google.android.exoplayer2.text.Subtitle;

final class TtmlSubtitle implements Subtitle
{
    private final long[] eventTimesUs;
    private final Map<String, TtmlStyle> globalStyles;
    private final Map<String, String> imageMap;
    private final Map<String, TtmlRegion> regionMap;
    private final TtmlNode root;
    
    public TtmlSubtitle(final TtmlNode root, final Map<String, TtmlStyle> m, final Map<String, TtmlRegion> regionMap, final Map<String, String> imageMap) {
        this.root = root;
        this.regionMap = regionMap;
        this.imageMap = imageMap;
        Map<String, TtmlStyle> globalStyles;
        if (m != null) {
            globalStyles = Collections.unmodifiableMap((Map<? extends String, ? extends TtmlStyle>)m);
        }
        else {
            globalStyles = Collections.emptyMap();
        }
        this.globalStyles = globalStyles;
        this.eventTimesUs = root.getEventTimesUs();
    }
    
    @Override
    public List<Cue> getCues(final long n) {
        return this.root.getCues(n, this.globalStyles, this.regionMap, this.imageMap);
    }
    
    @Override
    public long getEventTime(final int n) {
        return this.eventTimesUs[n];
    }
    
    @Override
    public int getEventTimeCount() {
        return this.eventTimesUs.length;
    }
    
    @Override
    public int getNextEventTimeIndex(final long n) {
        int binarySearchCeil = Util.binarySearchCeil(this.eventTimesUs, n, false, false);
        if (binarySearchCeil >= this.eventTimesUs.length) {
            binarySearchCeil = -1;
        }
        return binarySearchCeil;
    }
}
