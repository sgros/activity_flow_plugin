package com.google.android.exoplayer2.text;

import com.google.android.exoplayer2.decoder.OutputBuffer;
import java.util.List;

public abstract class SubtitleOutputBuffer extends OutputBuffer implements Subtitle {
   private long subsampleOffsetUs;
   private Subtitle subtitle;

   public void clear() {
      super.clear();
      this.subtitle = null;
   }

   public List getCues(long var1) {
      return this.subtitle.getCues(var1 - this.subsampleOffsetUs);
   }

   public long getEventTime(int var1) {
      return this.subtitle.getEventTime(var1) + this.subsampleOffsetUs;
   }

   public int getEventTimeCount() {
      return this.subtitle.getEventTimeCount();
   }

   public int getNextEventTimeIndex(long var1) {
      return this.subtitle.getNextEventTimeIndex(var1 - this.subsampleOffsetUs);
   }

   public void setContent(long var1, Subtitle var3, long var4) {
      super.timeUs = var1;
      this.subtitle = var3;
      var1 = var4;
      if (var4 == Long.MAX_VALUE) {
         var1 = super.timeUs;
      }

      this.subsampleOffsetUs = var1;
   }
}
