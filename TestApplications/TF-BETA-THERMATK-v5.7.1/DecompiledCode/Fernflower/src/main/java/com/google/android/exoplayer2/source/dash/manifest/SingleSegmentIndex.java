package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.source.dash.DashSegmentIndex;

final class SingleSegmentIndex implements DashSegmentIndex {
   private final RangedUri uri;

   public SingleSegmentIndex(RangedUri var1) {
      this.uri = var1;
   }

   public long getDurationUs(long var1, long var3) {
      return var3;
   }

   public long getFirstSegmentNum() {
      return 0L;
   }

   public int getSegmentCount(long var1) {
      return 1;
   }

   public long getSegmentNum(long var1, long var3) {
      return 0L;
   }

   public RangedUri getSegmentUrl(long var1) {
      return this.uri;
   }

   public long getTimeUs(long var1) {
      return 0L;
   }

   public boolean isExplicit() {
      return true;
   }
}
