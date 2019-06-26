package com.google.android.exoplayer2.text.subrip;

import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.Collections;
import java.util.List;

final class SubripSubtitle implements Subtitle {
   private final long[] cueTimesUs;
   private final Cue[] cues;

   public SubripSubtitle(Cue[] var1, long[] var2) {
      this.cues = var1;
      this.cueTimesUs = var2;
   }

   public List getCues(long var1) {
      int var3 = Util.binarySearchFloor(this.cueTimesUs, var1, true, false);
      if (var3 != -1) {
         Cue[] var4 = this.cues;
         if (var4[var3] != null) {
            return Collections.singletonList(var4[var3]);
         }
      }

      return Collections.emptyList();
   }

   public long getEventTime(int var1) {
      boolean var2 = true;
      boolean var3;
      if (var1 >= 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      Assertions.checkArgument(var3);
      if (var1 < this.cueTimesUs.length) {
         var3 = var2;
      } else {
         var3 = false;
      }

      Assertions.checkArgument(var3);
      return this.cueTimesUs[var1];
   }

   public int getEventTimeCount() {
      return this.cueTimesUs.length;
   }

   public int getNextEventTimeIndex(long var1) {
      int var3 = Util.binarySearchCeil(this.cueTimesUs, var1, false, false);
      if (var3 >= this.cueTimesUs.length) {
         var3 = -1;
      }

      return var3;
   }
}
