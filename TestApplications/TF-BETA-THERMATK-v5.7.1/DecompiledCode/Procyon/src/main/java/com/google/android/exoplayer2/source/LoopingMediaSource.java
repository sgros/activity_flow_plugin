// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.Allocator;
import java.util.HashMap;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Map;

public final class LoopingMediaSource extends CompositeMediaSource<Void>
{
    private final Map<MediaPeriodId, MediaPeriodId> childMediaPeriodIdToMediaPeriodId;
    private final MediaSource childSource;
    private final int loopCount;
    private final Map<MediaPeriod, MediaPeriodId> mediaPeriodToChildMediaPeriodId;
    
    public LoopingMediaSource(final MediaSource mediaSource) {
        this(mediaSource, Integer.MAX_VALUE);
    }
    
    public LoopingMediaSource(final MediaSource childSource, final int loopCount) {
        Assertions.checkArgument(loopCount > 0);
        this.childSource = childSource;
        this.loopCount = loopCount;
        this.childMediaPeriodIdToMediaPeriodId = new HashMap<MediaPeriodId, MediaPeriodId>();
        this.mediaPeriodToChildMediaPeriodId = new HashMap<MediaPeriod, MediaPeriodId>();
    }
    
    @Override
    public MediaPeriod createPeriod(final MediaPeriodId mediaPeriodId, final Allocator allocator, final long n) {
        if (this.loopCount == Integer.MAX_VALUE) {
            return this.childSource.createPeriod(mediaPeriodId, allocator, n);
        }
        final MediaSource.MediaPeriodId copyWithPeriodUid = mediaPeriodId.copyWithPeriodUid(AbstractConcatenatedTimeline.getChildPeriodUidFromConcatenatedUid(mediaPeriodId.periodUid));
        this.childMediaPeriodIdToMediaPeriodId.put(copyWithPeriodUid, mediaPeriodId);
        final MediaPeriod period = this.childSource.createPeriod(copyWithPeriodUid, allocator, n);
        this.mediaPeriodToChildMediaPeriodId.put(period, copyWithPeriodUid);
        return period;
    }
    
    @Override
    protected MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(final Void void1, final MediaPeriodId mediaPeriodId) {
        MediaPeriodId mediaPeriodId2 = mediaPeriodId;
        if (this.loopCount != Integer.MAX_VALUE) {
            mediaPeriodId2 = this.childMediaPeriodIdToMediaPeriodId.get(mediaPeriodId);
        }
        return mediaPeriodId2;
    }
    
    @Override
    protected void onChildSourceInfoRefreshed(final Void void1, final MediaSource mediaSource, final Timeline timeline, final Object o) {
        final int loopCount = this.loopCount;
        Timeline timeline2;
        if (loopCount != Integer.MAX_VALUE) {
            timeline2 = new LoopingTimeline(timeline, loopCount);
        }
        else {
            timeline2 = new InfinitelyLoopingTimeline(timeline);
        }
        this.refreshSourceInfo(timeline2, o);
    }
    
    @Override
    public void prepareSourceInternal(final TransferListener transferListener) {
        super.prepareSourceInternal(transferListener);
        this.prepareChildSource(null, this.childSource);
    }
    
    @Override
    public void releasePeriod(final MediaPeriod mediaPeriod) {
        this.childSource.releasePeriod(mediaPeriod);
        final MediaPeriodId mediaPeriodId = this.mediaPeriodToChildMediaPeriodId.remove(mediaPeriod);
        if (mediaPeriodId != null) {
            this.childMediaPeriodIdToMediaPeriodId.remove(mediaPeriodId);
        }
    }
    
    private static final class InfinitelyLoopingTimeline extends ForwardingTimeline
    {
        public InfinitelyLoopingTimeline(final Timeline timeline) {
            super(timeline);
        }
        
        @Override
        public int getNextWindowIndex(int n, int n2, final boolean b) {
            n2 = (n = super.timeline.getNextWindowIndex(n, n2, b));
            if (n2 == -1) {
                n = this.getFirstWindowIndex(b);
            }
            return n;
        }
    }
    
    private static final class LoopingTimeline extends AbstractConcatenatedTimeline
    {
        private final int childPeriodCount;
        private final Timeline childTimeline;
        private final int childWindowCount;
        private final int loopCount;
        
        public LoopingTimeline(final Timeline childTimeline, final int loopCount) {
            final ShuffleOrder.UnshuffledShuffleOrder unshuffledShuffleOrder = new ShuffleOrder.UnshuffledShuffleOrder(loopCount);
            boolean b = false;
            super(false, unshuffledShuffleOrder);
            this.childTimeline = childTimeline;
            this.childPeriodCount = childTimeline.getPeriodCount();
            this.childWindowCount = childTimeline.getWindowCount();
            this.loopCount = loopCount;
            final int childPeriodCount = this.childPeriodCount;
            if (childPeriodCount > 0) {
                if (loopCount <= Integer.MAX_VALUE / childPeriodCount) {
                    b = true;
                }
                Assertions.checkState(b, "LoopingMediaSource contains too many periods");
            }
        }
        
        @Override
        protected int getChildIndexByChildUid(final Object o) {
            if (!(o instanceof Integer)) {
                return -1;
            }
            return (int)o;
        }
        
        @Override
        protected int getChildIndexByPeriodIndex(final int n) {
            return n / this.childPeriodCount;
        }
        
        @Override
        protected int getChildIndexByWindowIndex(final int n) {
            return n / this.childWindowCount;
        }
        
        @Override
        protected Object getChildUidByChildIndex(final int i) {
            return i;
        }
        
        @Override
        protected int getFirstPeriodIndexByChildIndex(final int n) {
            return n * this.childPeriodCount;
        }
        
        @Override
        protected int getFirstWindowIndexByChildIndex(final int n) {
            return n * this.childWindowCount;
        }
        
        @Override
        public int getPeriodCount() {
            return this.childPeriodCount * this.loopCount;
        }
        
        @Override
        protected Timeline getTimelineByChildIndex(final int n) {
            return this.childTimeline;
        }
        
        @Override
        public int getWindowCount() {
            return this.childWindowCount * this.loopCount;
        }
    }
}
