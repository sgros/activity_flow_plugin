package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.util.Util;

final class MediaPeriodInfo {
   public final long contentPositionUs;
   public final long durationUs;
   public final MediaSource.MediaPeriodId id;
   public final boolean isFinal;
   public final boolean isLastInTimelinePeriod;
   public final long startPositionUs;

   MediaPeriodInfo(MediaSource.MediaPeriodId var1, long var2, long var4, long var6, boolean var8, boolean var9) {
      this.id = var1;
      this.startPositionUs = var2;
      this.contentPositionUs = var4;
      this.durationUs = var6;
      this.isLastInTimelinePeriod = var8;
      this.isFinal = var9;
   }

   public MediaPeriodInfo copyWithStartPositionUs(long var1) {
      return new MediaPeriodInfo(this.id, var1, this.contentPositionUs, this.durationUs, this.isLastInTimelinePeriod, this.isFinal);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && MediaPeriodInfo.class == var1.getClass()) {
         MediaPeriodInfo var3 = (MediaPeriodInfo)var1;
         if (this.startPositionUs != var3.startPositionUs || this.contentPositionUs != var3.contentPositionUs || this.durationUs != var3.durationUs || this.isLastInTimelinePeriod != var3.isLastInTimelinePeriod || this.isFinal != var3.isFinal || !Util.areEqual(this.id, var3.id)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (((((527 + this.id.hashCode()) * 31 + (int)this.startPositionUs) * 31 + (int)this.contentPositionUs) * 31 + (int)this.durationUs) * 31 + this.isLastInTimelinePeriod) * 31 + this.isFinal;
   }
}
