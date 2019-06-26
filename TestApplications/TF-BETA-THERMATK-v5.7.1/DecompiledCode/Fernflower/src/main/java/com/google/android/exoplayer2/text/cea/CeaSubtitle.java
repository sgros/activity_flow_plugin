package com.google.android.exoplayer2.text.cea;

import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Collections;
import java.util.List;

final class CeaSubtitle implements Subtitle {
   private final List cues;

   public CeaSubtitle(List var1) {
      this.cues = var1;
   }

   public List getCues(long var1) {
      List var3;
      if (var1 >= 0L) {
         var3 = this.cues;
      } else {
         var3 = Collections.emptyList();
      }

      return var3;
   }

   public long getEventTime(int var1) {
      boolean var2;
      if (var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkArgument(var2);
      return 0L;
   }

   public int getEventTimeCount() {
      return 1;
   }

   public int getNextEventTimeIndex(long var1) {
      byte var3;
      if (var1 < 0L) {
         var3 = 0;
      } else {
         var3 = -1;
      }

      return var3;
   }
}
