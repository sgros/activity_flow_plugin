package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import java.util.HashMap;
import java.util.Map;

public final class LoopingMediaSource extends CompositeMediaSource {
   private final Map childMediaPeriodIdToMediaPeriodId;
   private final MediaSource childSource;
   private final int loopCount;
   private final Map mediaPeriodToChildMediaPeriodId;

   public LoopingMediaSource(MediaSource var1) {
      this(var1, Integer.MAX_VALUE);
   }

   public LoopingMediaSource(MediaSource var1, int var2) {
      boolean var3;
      if (var2 > 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      Assertions.checkArgument(var3);
      this.childSource = var1;
      this.loopCount = var2;
      this.childMediaPeriodIdToMediaPeriodId = new HashMap();
      this.mediaPeriodToChildMediaPeriodId = new HashMap();
   }

   public MediaPeriod createPeriod(MediaSource.MediaPeriodId var1, Allocator var2, long var3) {
      if (this.loopCount == Integer.MAX_VALUE) {
         return this.childSource.createPeriod(var1, var2, var3);
      } else {
         MediaSource.MediaPeriodId var5 = var1.copyWithPeriodUid(AbstractConcatenatedTimeline.getChildPeriodUidFromConcatenatedUid(var1.periodUid));
         this.childMediaPeriodIdToMediaPeriodId.put(var5, var1);
         MediaPeriod var6 = this.childSource.createPeriod(var5, var2, var3);
         this.mediaPeriodToChildMediaPeriodId.put(var6, var5);
         return var6;
      }
   }

   protected MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(Void var1, MediaSource.MediaPeriodId var2) {
      MediaSource.MediaPeriodId var3 = var2;
      if (this.loopCount != Integer.MAX_VALUE) {
         var3 = (MediaSource.MediaPeriodId)this.childMediaPeriodIdToMediaPeriodId.get(var2);
      }

      return var3;
   }

   protected void onChildSourceInfoRefreshed(Void var1, MediaSource var2, Timeline var3, Object var4) {
      int var5 = this.loopCount;
      Object var6;
      if (var5 != Integer.MAX_VALUE) {
         var6 = new LoopingMediaSource.LoopingTimeline(var3, var5);
      } else {
         var6 = new LoopingMediaSource.InfinitelyLoopingTimeline(var3);
      }

      this.refreshSourceInfo((Timeline)var6, var4);
   }

   public void prepareSourceInternal(TransferListener var1) {
      super.prepareSourceInternal(var1);
      this.prepareChildSource((Object)null, this.childSource);
   }

   public void releasePeriod(MediaPeriod var1) {
      this.childSource.releasePeriod(var1);
      MediaSource.MediaPeriodId var2 = (MediaSource.MediaPeriodId)this.mediaPeriodToChildMediaPeriodId.remove(var1);
      if (var2 != null) {
         this.childMediaPeriodIdToMediaPeriodId.remove(var2);
      }

   }

   private static final class InfinitelyLoopingTimeline extends ForwardingTimeline {
      public InfinitelyLoopingTimeline(Timeline var1) {
         super(var1);
      }

      public int getNextWindowIndex(int var1, int var2, boolean var3) {
         var2 = super.timeline.getNextWindowIndex(var1, var2, var3);
         var1 = var2;
         if (var2 == -1) {
            var1 = this.getFirstWindowIndex(var3);
         }

         return var1;
      }
   }

   private static final class LoopingTimeline extends AbstractConcatenatedTimeline {
      private final int childPeriodCount;
      private final Timeline childTimeline;
      private final int childWindowCount;
      private final int loopCount;

      public LoopingTimeline(Timeline var1, int var2) {
         ShuffleOrder.UnshuffledShuffleOrder var3 = new ShuffleOrder.UnshuffledShuffleOrder(var2);
         boolean var4 = false;
         super(false, var3);
         this.childTimeline = var1;
         this.childPeriodCount = var1.getPeriodCount();
         this.childWindowCount = var1.getWindowCount();
         this.loopCount = var2;
         int var5 = this.childPeriodCount;
         if (var5 > 0) {
            if (var2 <= Integer.MAX_VALUE / var5) {
               var4 = true;
            }

            Assertions.checkState(var4, "LoopingMediaSource contains too many periods");
         }

      }

      protected int getChildIndexByChildUid(Object var1) {
         return !(var1 instanceof Integer) ? -1 : (Integer)var1;
      }

      protected int getChildIndexByPeriodIndex(int var1) {
         return var1 / this.childPeriodCount;
      }

      protected int getChildIndexByWindowIndex(int var1) {
         return var1 / this.childWindowCount;
      }

      protected Object getChildUidByChildIndex(int var1) {
         return var1;
      }

      protected int getFirstPeriodIndexByChildIndex(int var1) {
         return var1 * this.childPeriodCount;
      }

      protected int getFirstWindowIndexByChildIndex(int var1) {
         return var1 * this.childWindowCount;
      }

      public int getPeriodCount() {
         return this.childPeriodCount * this.loopCount;
      }

      protected Timeline getTimelineByChildIndex(int var1) {
         return this.childTimeline;
      }

      public int getWindowCount() {
         return this.childWindowCount * this.loopCount;
      }
   }
}
