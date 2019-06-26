// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.util.Assertions;
import android.util.Pair;

public abstract class Timeline
{
    public static final Timeline EMPTY;
    
    static {
        EMPTY = new Timeline() {
            @Override
            public int getIndexOfPeriod(final Object o) {
                return -1;
            }
            
            @Override
            public Period getPeriod(final int n, final Period period, final boolean b) {
                throw new IndexOutOfBoundsException();
            }
            
            @Override
            public int getPeriodCount() {
                return 0;
            }
            
            @Override
            public Object getUidOfPeriod(final int n) {
                throw new IndexOutOfBoundsException();
            }
            
            @Override
            public Window getWindow(final int n, final Window window, final boolean b, final long n2) {
                throw new IndexOutOfBoundsException();
            }
            
            @Override
            public int getWindowCount() {
                return 0;
            }
        };
    }
    
    public int getFirstWindowIndex(final boolean b) {
        int n;
        if (this.isEmpty()) {
            n = -1;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    public abstract int getIndexOfPeriod(final Object p0);
    
    public int getLastWindowIndex(final boolean b) {
        int n;
        if (this.isEmpty()) {
            n = -1;
        }
        else {
            n = this.getWindowCount() - 1;
        }
        return n;
    }
    
    public final int getNextPeriodIndex(int nextWindowIndex, final Period period, final Window window, final int n, final boolean b) {
        final int windowIndex = this.getPeriod(nextWindowIndex, period).windowIndex;
        if (this.getWindow(windowIndex, window).lastPeriodIndex != nextWindowIndex) {
            return nextWindowIndex + 1;
        }
        nextWindowIndex = this.getNextWindowIndex(windowIndex, n, b);
        if (nextWindowIndex == -1) {
            return -1;
        }
        return this.getWindow(nextWindowIndex, window).firstPeriodIndex;
    }
    
    public int getNextWindowIndex(int firstWindowIndex, final int n, final boolean b) {
        if (n == 0) {
            if (firstWindowIndex == this.getLastWindowIndex(b)) {
                firstWindowIndex = -1;
            }
            else {
                ++firstWindowIndex;
            }
            return firstWindowIndex;
        }
        if (n == 1) {
            return firstWindowIndex;
        }
        if (n == 2) {
            if (firstWindowIndex == this.getLastWindowIndex(b)) {
                firstWindowIndex = this.getFirstWindowIndex(b);
            }
            else {
                ++firstWindowIndex;
            }
            return firstWindowIndex;
        }
        throw new IllegalStateException();
    }
    
    public final Period getPeriod(final int n, final Period period) {
        return this.getPeriod(n, period, false);
    }
    
    public abstract Period getPeriod(final int p0, final Period p1, final boolean p2);
    
    public Period getPeriodByUid(final Object o, final Period period) {
        return this.getPeriod(this.getIndexOfPeriod(o), period, true);
    }
    
    public abstract int getPeriodCount();
    
    public final Pair<Object, Long> getPeriodPosition(final Window window, final Period period, final int n, final long n2) {
        final Pair<Object, Long> periodPosition = this.getPeriodPosition(window, period, n, n2, 0L);
        Assertions.checkNotNull(periodPosition);
        return periodPosition;
    }
    
    public final Pair<Object, Long> getPeriodPosition(final Window window, final Period period, int firstPeriodIndex, long n, long defaultPositionUs) {
        Assertions.checkIndex(firstPeriodIndex, 0, this.getWindowCount());
        this.getWindow(firstPeriodIndex, window, false, defaultPositionUs);
        defaultPositionUs = n;
        if (n == -9223372036854775807L) {
            n = (defaultPositionUs = window.getDefaultPositionUs());
            if (n == -9223372036854775807L) {
                return null;
            }
        }
        for (firstPeriodIndex = window.firstPeriodIndex, defaultPositionUs += window.getPositionInFirstPeriodUs(), n = this.getPeriod(firstPeriodIndex, period, true).getDurationUs(); n != -9223372036854775807L && defaultPositionUs >= n && firstPeriodIndex < window.lastPeriodIndex; defaultPositionUs -= n, ++firstPeriodIndex, n = this.getPeriod(firstPeriodIndex, period, true).getDurationUs()) {}
        final Object uid = period.uid;
        Assertions.checkNotNull(uid);
        return (Pair<Object, Long>)Pair.create(uid, (Object)defaultPositionUs);
    }
    
    public abstract Object getUidOfPeriod(final int p0);
    
    public final Window getWindow(final int n, final Window window) {
        return this.getWindow(n, window, false);
    }
    
    public final Window getWindow(final int n, final Window window, final boolean b) {
        return this.getWindow(n, window, b, 0L);
    }
    
    public abstract Window getWindow(final int p0, final Window p1, final boolean p2, final long p3);
    
    public abstract int getWindowCount();
    
    public final boolean isEmpty() {
        return this.getWindowCount() == 0;
    }
    
    public final boolean isLastPeriod(final int n, final Period period, final Window window, final int n2, final boolean b) {
        return this.getNextPeriodIndex(n, period, window, n2, b) == -1;
    }
    
    public static final class Period
    {
        private AdPlaybackState adPlaybackState;
        public long durationUs;
        public Object id;
        private long positionInWindowUs;
        public Object uid;
        public int windowIndex;
        
        public Period() {
            this.adPlaybackState = AdPlaybackState.NONE;
        }
        
        public int getAdCountInAdGroup(final int n) {
            return this.adPlaybackState.adGroups[n].count;
        }
        
        public long getAdDurationUs(final int n, final int n2) {
            final AdPlaybackState.AdGroup adGroup = this.adPlaybackState.adGroups[n];
            long n3;
            if (adGroup.count != -1) {
                n3 = adGroup.durationsUs[n2];
            }
            else {
                n3 = -9223372036854775807L;
            }
            return n3;
        }
        
        public int getAdGroupCount() {
            return this.adPlaybackState.adGroupCount;
        }
        
        public int getAdGroupIndexAfterPositionUs(final long n) {
            return this.adPlaybackState.getAdGroupIndexAfterPositionUs(n, this.durationUs);
        }
        
        public int getAdGroupIndexForPositionUs(final long n) {
            return this.adPlaybackState.getAdGroupIndexForPositionUs(n);
        }
        
        public long getAdGroupTimeUs(final int n) {
            return this.adPlaybackState.adGroupTimesUs[n];
        }
        
        public long getAdResumePositionUs() {
            return this.adPlaybackState.adResumePositionUs;
        }
        
        public long getDurationMs() {
            return C.usToMs(this.durationUs);
        }
        
        public long getDurationUs() {
            return this.durationUs;
        }
        
        public int getFirstAdIndexToPlay(final int n) {
            return this.adPlaybackState.adGroups[n].getFirstAdIndexToPlay();
        }
        
        public int getNextAdIndexToPlay(final int n, final int n2) {
            return this.adPlaybackState.adGroups[n].getNextAdIndexToPlay(n2);
        }
        
        public long getPositionInWindowMs() {
            return C.usToMs(this.positionInWindowUs);
        }
        
        public boolean isAdAvailable(final int n, final int n2) {
            final AdPlaybackState.AdGroup adGroup = this.adPlaybackState.adGroups[n];
            return adGroup.count != -1 && adGroup.states[n2] != 0;
        }
        
        public Period set(final Object o, final Object o2, final int n, final long n2, final long n3) {
            this.set(o, o2, n, n2, n3, AdPlaybackState.NONE);
            return this;
        }
        
        public Period set(final Object id, final Object uid, final int windowIndex, final long durationUs, final long positionInWindowUs, final AdPlaybackState adPlaybackState) {
            this.id = id;
            this.uid = uid;
            this.windowIndex = windowIndex;
            this.durationUs = durationUs;
            this.positionInWindowUs = positionInWindowUs;
            this.adPlaybackState = adPlaybackState;
            return this;
        }
    }
    
    public static final class Window
    {
        public long defaultPositionUs;
        public long durationUs;
        public int firstPeriodIndex;
        public boolean isDynamic;
        public boolean isSeekable;
        public int lastPeriodIndex;
        public long positionInFirstPeriodUs;
        public long presentationStartTimeMs;
        public Object tag;
        public long windowStartTimeMs;
        
        public long getDefaultPositionMs() {
            return C.usToMs(this.defaultPositionUs);
        }
        
        public long getDefaultPositionUs() {
            return this.defaultPositionUs;
        }
        
        public long getDurationMs() {
            return C.usToMs(this.durationUs);
        }
        
        public long getPositionInFirstPeriodUs() {
            return this.positionInFirstPeriodUs;
        }
        
        public Window set(final Object tag, final long presentationStartTimeMs, final long windowStartTimeMs, final boolean isSeekable, final boolean isDynamic, final long defaultPositionUs, final long durationUs, final int firstPeriodIndex, final int lastPeriodIndex, final long positionInFirstPeriodUs) {
            this.tag = tag;
            this.presentationStartTimeMs = presentationStartTimeMs;
            this.windowStartTimeMs = windowStartTimeMs;
            this.isSeekable = isSeekable;
            this.isDynamic = isDynamic;
            this.defaultPositionUs = defaultPositionUs;
            this.durationUs = durationUs;
            this.firstPeriodIndex = firstPeriodIndex;
            this.lastPeriodIndex = lastPeriodIndex;
            this.positionInFirstPeriodUs = positionInFirstPeriodUs;
            return this;
        }
    }
}
