// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import java.util.Collections;
import java.util.List;

public class Period
{
    public final List<AdaptationSet> adaptationSets;
    public final List<EventStream> eventStreams;
    public final String id;
    public final long startMs;
    
    public Period(final String id, final long startMs, final List<AdaptationSet> list, final List<EventStream> list2) {
        this.id = id;
        this.startMs = startMs;
        this.adaptationSets = Collections.unmodifiableList((List<? extends AdaptationSet>)list);
        this.eventStreams = Collections.unmodifiableList((List<? extends EventStream>)list2);
    }
    
    public int getAdaptationSetIndex(final int n) {
        for (int size = this.adaptationSets.size(), i = 0; i < size; ++i) {
            if (this.adaptationSets.get(i).type == n) {
                return i;
            }
        }
        return -1;
    }
}
