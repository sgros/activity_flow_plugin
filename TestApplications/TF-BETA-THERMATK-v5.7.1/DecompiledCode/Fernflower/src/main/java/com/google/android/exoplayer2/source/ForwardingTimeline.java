package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Timeline;

public abstract class ForwardingTimeline extends Timeline {
   protected final Timeline timeline;

   public ForwardingTimeline(Timeline var1) {
      this.timeline = var1;
   }

   public int getFirstWindowIndex(boolean var1) {
      return this.timeline.getFirstWindowIndex(var1);
   }

   public int getIndexOfPeriod(Object var1) {
      return this.timeline.getIndexOfPeriod(var1);
   }

   public int getLastWindowIndex(boolean var1) {
      return this.timeline.getLastWindowIndex(var1);
   }

   public Timeline.Period getPeriod(int var1, Timeline.Period var2, boolean var3) {
      return this.timeline.getPeriod(var1, var2, var3);
   }

   public int getPeriodCount() {
      return this.timeline.getPeriodCount();
   }

   public Object getUidOfPeriod(int var1) {
      return this.timeline.getUidOfPeriod(var1);
   }

   public Timeline.Window getWindow(int var1, Timeline.Window var2, boolean var3, long var4) {
      return this.timeline.getWindow(var1, var2, var3, var4);
   }

   public int getWindowCount() {
      return this.timeline.getWindowCount();
   }
}
