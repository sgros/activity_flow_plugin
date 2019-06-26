package com.google.android.exoplayer2.source.dash.manifest;

import java.util.Collections;
import java.util.List;

public class Period {
   public final List adaptationSets;
   public final List eventStreams;
   public final String id;
   public final long startMs;

   public Period(String var1, long var2, List var4, List var5) {
      this.id = var1;
      this.startMs = var2;
      this.adaptationSets = Collections.unmodifiableList(var4);
      this.eventStreams = Collections.unmodifiableList(var5);
   }

   public int getAdaptationSetIndex(int var1) {
      int var2 = this.adaptationSets.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (((AdaptationSet)this.adaptationSets.get(var3)).type == var1) {
            return var3;
         }
      }

      return -1;
   }
}
