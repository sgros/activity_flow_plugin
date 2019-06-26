// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import android.util.Pair;
import com.google.android.exoplayer2.Timeline;

abstract class AbstractConcatenatedTimeline extends Timeline
{
    private final int childCount;
    private final boolean isAtomic;
    private final ShuffleOrder shuffleOrder;
    
    public AbstractConcatenatedTimeline(final boolean isAtomic, final ShuffleOrder shuffleOrder) {
        this.isAtomic = isAtomic;
        this.shuffleOrder = shuffleOrder;
        this.childCount = shuffleOrder.getLength();
    }
    
    public static Object getChildPeriodUidFromConcatenatedUid(final Object o) {
        return ((Pair)o).second;
    }
    
    public static Object getChildTimelineUidFromConcatenatedUid(final Object o) {
        return ((Pair)o).first;
    }
    
    public static Object getConcatenatedUid(final Object o, final Object o2) {
        return Pair.create(o, o2);
    }
    
    private int getNextChildIndex(int nextIndex, final boolean b) {
        if (b) {
            nextIndex = this.shuffleOrder.getNextIndex(nextIndex);
        }
        else if (nextIndex < this.childCount - 1) {
            ++nextIndex;
        }
        else {
            nextIndex = -1;
        }
        return nextIndex;
    }
    
    private int getPreviousChildIndex(int previousIndex, final boolean b) {
        if (b) {
            previousIndex = this.shuffleOrder.getPreviousIndex(previousIndex);
        }
        else if (previousIndex > 0) {
            --previousIndex;
        }
        else {
            previousIndex = -1;
        }
        return previousIndex;
    }
    
    protected abstract int getChildIndexByChildUid(final Object p0);
    
    protected abstract int getChildIndexByPeriodIndex(final int p0);
    
    protected abstract int getChildIndexByWindowIndex(final int p0);
    
    protected abstract Object getChildUidByChildIndex(final int p0);
    
    protected abstract int getFirstPeriodIndexByChildIndex(final int p0);
    
    @Override
    public int getFirstWindowIndex(boolean b) {
        if (this.childCount == 0) {
            return -1;
        }
        final boolean isAtomic = this.isAtomic;
        int n = 0;
        if (isAtomic) {
            b = false;
        }
        if (b) {
            n = this.shuffleOrder.getFirstIndex();
        }
        while (this.getTimelineByChildIndex(n).isEmpty()) {
            if ((n = this.getNextChildIndex(n, b)) == -1) {
                return -1;
            }
        }
        return this.getFirstWindowIndexByChildIndex(n) + this.getTimelineByChildIndex(n).getFirstWindowIndex(b);
    }
    
    protected abstract int getFirstWindowIndexByChildIndex(final int p0);
    
    @Override
    public final int getIndexOfPeriod(Object childPeriodUidFromConcatenatedUid) {
        final boolean b = childPeriodUidFromConcatenatedUid instanceof Pair;
        int n = -1;
        if (!b) {
            return -1;
        }
        final Object childTimelineUidFromConcatenatedUid = getChildTimelineUidFromConcatenatedUid(childPeriodUidFromConcatenatedUid);
        childPeriodUidFromConcatenatedUid = getChildPeriodUidFromConcatenatedUid(childPeriodUidFromConcatenatedUid);
        final int childIndexByChildUid = this.getChildIndexByChildUid(childTimelineUidFromConcatenatedUid);
        if (childIndexByChildUid == -1) {
            return -1;
        }
        final int indexOfPeriod = this.getTimelineByChildIndex(childIndexByChildUid).getIndexOfPeriod(childPeriodUidFromConcatenatedUid);
        if (indexOfPeriod != -1) {
            n = this.getFirstPeriodIndexByChildIndex(childIndexByChildUid) + indexOfPeriod;
        }
        return n;
    }
    
    @Override
    public int getLastWindowIndex(boolean b) {
        if (this.childCount == 0) {
            return -1;
        }
        if (this.isAtomic) {
            b = false;
        }
        int n;
        if (b) {
            n = this.shuffleOrder.getLastIndex();
        }
        else {
            n = this.childCount - 1;
        }
        while (this.getTimelineByChildIndex(n).isEmpty()) {
            if ((n = this.getPreviousChildIndex(n, b)) == -1) {
                return -1;
            }
        }
        return this.getFirstWindowIndexByChildIndex(n) + this.getTimelineByChildIndex(n).getLastWindowIndex(b);
    }
    
    @Override
    public int getNextWindowIndex(int n, int n2, boolean b) {
        final boolean isAtomic = this.isAtomic;
        final int n3 = 0;
        int n4 = n2;
        if (isAtomic) {
            if ((n4 = n2) == 1) {
                n4 = 2;
            }
            b = false;
        }
        final int childIndexByWindowIndex = this.getChildIndexByWindowIndex(n);
        final int firstWindowIndexByChildIndex = this.getFirstWindowIndexByChildIndex(childIndexByWindowIndex);
        final Timeline timelineByChildIndex = this.getTimelineByChildIndex(childIndexByWindowIndex);
        if (n4 == 2) {
            n2 = n3;
        }
        else {
            n2 = n4;
        }
        n = timelineByChildIndex.getNextWindowIndex(n - firstWindowIndexByChildIndex, n2, b);
        if (n != -1) {
            return firstWindowIndexByChildIndex + n;
        }
        for (n = this.getNextChildIndex(childIndexByWindowIndex, b); n != -1 && this.getTimelineByChildIndex(n).isEmpty(); n = this.getNextChildIndex(n, b)) {}
        if (n != -1) {
            return this.getFirstWindowIndexByChildIndex(n) + this.getTimelineByChildIndex(n).getFirstWindowIndex(b);
        }
        if (n4 == 2) {
            return this.getFirstWindowIndex(b);
        }
        return -1;
    }
    
    @Override
    public final Period getPeriod(final int n, final Period period, final boolean b) {
        final int childIndexByPeriodIndex = this.getChildIndexByPeriodIndex(n);
        final int firstWindowIndexByChildIndex = this.getFirstWindowIndexByChildIndex(childIndexByPeriodIndex);
        this.getTimelineByChildIndex(childIndexByPeriodIndex).getPeriod(n - this.getFirstPeriodIndexByChildIndex(childIndexByPeriodIndex), period, b);
        period.windowIndex += firstWindowIndexByChildIndex;
        if (b) {
            period.uid = getConcatenatedUid(this.getChildUidByChildIndex(childIndexByPeriodIndex), period.uid);
        }
        return period;
    }
    
    @Override
    public final Period getPeriodByUid(final Object uid, final Period period) {
        final Object childTimelineUidFromConcatenatedUid = getChildTimelineUidFromConcatenatedUid(uid);
        final Object childPeriodUidFromConcatenatedUid = getChildPeriodUidFromConcatenatedUid(uid);
        final int childIndexByChildUid = this.getChildIndexByChildUid(childTimelineUidFromConcatenatedUid);
        final int firstWindowIndexByChildIndex = this.getFirstWindowIndexByChildIndex(childIndexByChildUid);
        this.getTimelineByChildIndex(childIndexByChildUid).getPeriodByUid(childPeriodUidFromConcatenatedUid, period);
        period.windowIndex += firstWindowIndexByChildIndex;
        period.uid = uid;
        return period;
    }
    
    protected abstract Timeline getTimelineByChildIndex(final int p0);
    
    @Override
    public final Object getUidOfPeriod(final int n) {
        final int childIndexByPeriodIndex = this.getChildIndexByPeriodIndex(n);
        return getConcatenatedUid(this.getChildUidByChildIndex(childIndexByPeriodIndex), this.getTimelineByChildIndex(childIndexByPeriodIndex).getUidOfPeriod(n - this.getFirstPeriodIndexByChildIndex(childIndexByPeriodIndex)));
    }
    
    @Override
    public final Window getWindow(final int n, final Window window, final boolean b, final long n2) {
        final int childIndexByWindowIndex = this.getChildIndexByWindowIndex(n);
        final int firstWindowIndexByChildIndex = this.getFirstWindowIndexByChildIndex(childIndexByWindowIndex);
        final int firstPeriodIndexByChildIndex = this.getFirstPeriodIndexByChildIndex(childIndexByWindowIndex);
        this.getTimelineByChildIndex(childIndexByWindowIndex).getWindow(n - firstWindowIndexByChildIndex, window, b, n2);
        window.firstPeriodIndex += firstPeriodIndexByChildIndex;
        window.lastPeriodIndex += firstPeriodIndexByChildIndex;
        return window;
    }
}
