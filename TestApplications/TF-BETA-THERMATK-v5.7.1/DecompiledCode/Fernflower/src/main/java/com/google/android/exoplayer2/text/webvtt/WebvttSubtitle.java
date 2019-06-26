package com.google.android.exoplayer2.text.webvtt;

import android.text.SpannableStringBuilder;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class WebvttSubtitle implements Subtitle {
   private final long[] cueTimesUs;
   private final List cues;
   private final int numCues;
   private final long[] sortedCueTimesUs;

   public WebvttSubtitle(List var1) {
      this.cues = var1;
      this.numCues = var1.size();
      this.cueTimesUs = new long[this.numCues * 2];

      for(int var2 = 0; var2 < this.numCues; ++var2) {
         WebvttCue var3 = (WebvttCue)var1.get(var2);
         int var4 = var2 * 2;
         long[] var5 = this.cueTimesUs;
         var5[var4] = var3.startTime;
         var5[var4 + 1] = var3.endTime;
      }

      long[] var6 = this.cueTimesUs;
      this.sortedCueTimesUs = Arrays.copyOf(var6, var6.length);
      Arrays.sort(this.sortedCueTimesUs);
   }

   public List getCues(long var1) {
      SpannableStringBuilder var3 = null;
      int var4 = 0;
      WebvttCue var5 = null;

      Object var6;
      Object var11;
      for(var6 = var5; var4 < this.numCues; var6 = var11) {
         long[] var7 = this.cueTimesUs;
         int var8 = var4 * 2;
         SpannableStringBuilder var9 = var3;
         WebvttCue var10 = var5;
         var11 = var6;
         if (var7[var8] <= var1) {
            var9 = var3;
            var10 = var5;
            var11 = var6;
            if (var1 < var7[var8 + 1]) {
               var11 = var6;
               if (var6 == null) {
                  var11 = new ArrayList();
               }

               var10 = (WebvttCue)this.cues.get(var4);
               if (var10.isNormalCue()) {
                  if (var5 == null) {
                     var9 = var3;
                  } else if (var3 == null) {
                     var9 = new SpannableStringBuilder();
                     var9.append(var5.text).append("\n").append(var10.text);
                     var10 = var5;
                  } else {
                     var3.append("\n").append(var10.text);
                     var9 = var3;
                     var10 = var5;
                  }
               } else {
                  ((ArrayList)var11).add(var10);
                  var10 = var5;
                  var9 = var3;
               }
            }
         }

         ++var4;
         var3 = var9;
         var5 = var10;
      }

      if (var3 != null) {
         ((ArrayList)var6).add(new WebvttCue(var3));
      } else if (var5 != null) {
         ((ArrayList)var6).add(var5);
      }

      if (var6 != null) {
         return (List)var6;
      } else {
         return Collections.emptyList();
      }
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
      if (var1 < this.sortedCueTimesUs.length) {
         var3 = var2;
      } else {
         var3 = false;
      }

      Assertions.checkArgument(var3);
      return this.sortedCueTimesUs[var1];
   }

   public int getEventTimeCount() {
      return this.sortedCueTimesUs.length;
   }

   public int getNextEventTimeIndex(long var1) {
      int var3 = Util.binarySearchCeil(this.sortedCueTimesUs, var1, false, false);
      if (var3 >= this.sortedCueTimesUs.length) {
         var3 = -1;
      }

      return var3;
   }
}
