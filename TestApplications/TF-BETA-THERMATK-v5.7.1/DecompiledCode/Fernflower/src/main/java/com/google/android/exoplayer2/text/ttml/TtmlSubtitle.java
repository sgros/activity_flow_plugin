package com.google.android.exoplayer2.text.ttml;

import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.Util;
import java.util.Collections;
import java.util.List;
import java.util.Map;

final class TtmlSubtitle implements Subtitle {
   private final long[] eventTimesUs;
   private final Map globalStyles;
   private final Map imageMap;
   private final Map regionMap;
   private final TtmlNode root;

   public TtmlSubtitle(TtmlNode var1, Map var2, Map var3, Map var4) {
      this.root = var1;
      this.regionMap = var3;
      this.imageMap = var4;
      if (var2 != null) {
         var2 = Collections.unmodifiableMap(var2);
      } else {
         var2 = Collections.emptyMap();
      }

      this.globalStyles = var2;
      this.eventTimesUs = var1.getEventTimesUs();
   }

   public List getCues(long var1) {
      return this.root.getCues(var1, this.globalStyles, this.regionMap, this.imageMap);
   }

   public long getEventTime(int var1) {
      return this.eventTimesUs[var1];
   }

   public int getEventTimeCount() {
      return this.eventTimesUs.length;
   }

   public int getNextEventTimeIndex(long var1) {
      int var3 = Util.binarySearchCeil(this.eventTimesUs, var1, false, false);
      if (var3 >= this.eventTimesUs.length) {
         var3 = -1;
      }

      return var3;
   }
}
