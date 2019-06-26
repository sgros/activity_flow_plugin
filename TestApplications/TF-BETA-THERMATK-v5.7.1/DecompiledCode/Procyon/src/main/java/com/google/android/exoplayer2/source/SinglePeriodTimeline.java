// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.Timeline;

public final class SinglePeriodTimeline extends Timeline
{
    private static final Object UID;
    private final boolean isDynamic;
    private final boolean isSeekable;
    private final long periodDurationUs;
    private final long presentationStartTimeMs;
    private final Object tag;
    private final long windowDefaultStartPositionUs;
    private final long windowDurationUs;
    private final long windowPositionInPeriodUs;
    private final long windowStartTimeMs;
    
    static {
        UID = new Object();
    }
    
    public SinglePeriodTimeline(final long presentationStartTimeMs, final long windowStartTimeMs, final long periodDurationUs, final long windowDurationUs, final long windowPositionInPeriodUs, final long windowDefaultStartPositionUs, final boolean isSeekable, final boolean isDynamic, final Object tag) {
        this.presentationStartTimeMs = presentationStartTimeMs;
        this.windowStartTimeMs = windowStartTimeMs;
        this.periodDurationUs = periodDurationUs;
        this.windowDurationUs = windowDurationUs;
        this.windowPositionInPeriodUs = windowPositionInPeriodUs;
        this.windowDefaultStartPositionUs = windowDefaultStartPositionUs;
        this.isSeekable = isSeekable;
        this.isDynamic = isDynamic;
        this.tag = tag;
    }
    
    public SinglePeriodTimeline(final long n, final long n2, final long n3, final long n4, final boolean b, final boolean b2, final Object o) {
        this(-9223372036854775807L, -9223372036854775807L, n, n2, n3, n4, b, b2, o);
    }
    
    public SinglePeriodTimeline(final long n, final boolean b, final boolean b2, final Object o) {
        this(n, n, 0L, 0L, b, b2, o);
    }
    
    @Override
    public int getIndexOfPeriod(final Object obj) {
        int n;
        if (SinglePeriodTimeline.UID.equals(obj)) {
            n = 0;
        }
        else {
            n = -1;
        }
        return n;
    }
    
    @Override
    public Period getPeriod(final int n, final Period period, final boolean b) {
        Assertions.checkIndex(n, 0, 1);
        Object uid;
        if (b) {
            uid = SinglePeriodTimeline.UID;
        }
        else {
            uid = null;
        }
        period.set(null, uid, 0, this.periodDurationUs, -this.windowPositionInPeriodUs);
        return period;
    }
    
    @Override
    public int getPeriodCount() {
        return 1;
    }
    
    @Override
    public Object getUidOfPeriod(final int n) {
        Assertions.checkIndex(n, 0, 1);
        return SinglePeriodTimeline.UID;
    }
    
    @Override
    public Window getWindow(final int n, final Window window, final boolean b, long n2) {
        Assertions.checkIndex(n, 0, 1);
        Object tag;
        if (b) {
            tag = this.tag;
        }
        else {
            tag = null;
        }
        long windowDefaultStartPositionUs;
        final long n3 = windowDefaultStartPositionUs = this.windowDefaultStartPositionUs;
        Label_0100: {
            Label_0096: {
                if (this.isDynamic) {
                    windowDefaultStartPositionUs = n3;
                    if (n2 != 0L) {
                        final long windowDurationUs = this.windowDurationUs;
                        if (windowDurationUs != -9223372036854775807L) {
                            n2 = (windowDefaultStartPositionUs = n3 + n2);
                            if (n2 <= windowDurationUs) {
                                break Label_0096;
                            }
                        }
                        n2 = -9223372036854775807L;
                        break Label_0100;
                    }
                }
            }
            n2 = windowDefaultStartPositionUs;
        }
        window.set(tag, this.presentationStartTimeMs, this.windowStartTimeMs, this.isSeekable, this.isDynamic, n2, this.windowDurationUs, 0, 0, this.windowPositionInPeriodUs);
        return window;
    }
    
    @Override
    public int getWindowCount() {
        return 1;
    }
}
