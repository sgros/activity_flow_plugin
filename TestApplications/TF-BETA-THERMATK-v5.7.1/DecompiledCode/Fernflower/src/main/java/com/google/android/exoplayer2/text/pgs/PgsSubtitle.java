package com.google.android.exoplayer2.text.pgs;

import com.google.android.exoplayer2.text.Subtitle;
import java.util.List;

final class PgsSubtitle implements Subtitle {
   private final List cues;

   public PgsSubtitle(List var1) {
      this.cues = var1;
   }

   public List getCues(long var1) {
      return this.cues;
   }

   public long getEventTime(int var1) {
      return 0L;
   }

   public int getEventTimeCount() {
      return 1;
   }

   public int getNextEventTimeIndex(long var1) {
      return -1;
   }
}
