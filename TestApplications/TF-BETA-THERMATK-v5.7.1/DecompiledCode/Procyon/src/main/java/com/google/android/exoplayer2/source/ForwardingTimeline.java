// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Timeline;

public abstract class ForwardingTimeline extends Timeline
{
    protected final Timeline timeline;
    
    public ForwardingTimeline(final Timeline timeline) {
        this.timeline = timeline;
    }
    
    @Override
    public int getFirstWindowIndex(final boolean b) {
        return this.timeline.getFirstWindowIndex(b);
    }
    
    @Override
    public int getIndexOfPeriod(final Object o) {
        return this.timeline.getIndexOfPeriod(o);
    }
    
    @Override
    public int getLastWindowIndex(final boolean b) {
        return this.timeline.getLastWindowIndex(b);
    }
    
    @Override
    public Period getPeriod(final int n, final Period period, final boolean b) {
        return this.timeline.getPeriod(n, period, b);
    }
    
    @Override
    public int getPeriodCount() {
        return this.timeline.getPeriodCount();
    }
    
    @Override
    public Object getUidOfPeriod(final int n) {
        return this.timeline.getUidOfPeriod(n);
    }
    
    @Override
    public Window getWindow(final int n, final Window window, final boolean b, final long n2) {
        return this.timeline.getWindow(n, window, b, n2);
    }
    
    @Override
    public int getWindowCount() {
        return this.timeline.getWindowCount();
    }
}
