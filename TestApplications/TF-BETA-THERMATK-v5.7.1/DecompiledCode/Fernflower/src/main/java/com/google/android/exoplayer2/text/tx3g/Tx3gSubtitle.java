package com.google.android.exoplayer2.text.tx3g;

import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Collections;
import java.util.List;

final class Tx3gSubtitle implements Subtitle {
   public static final Tx3gSubtitle EMPTY = new Tx3gSubtitle();
   private final List cues;

   private Tx3gSubtitle() {
      this.cues = Collections.emptyList();
   }

   public Tx3gSubtitle(Cue var1) {
      this.cues = Collections.singletonList(var1);
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
