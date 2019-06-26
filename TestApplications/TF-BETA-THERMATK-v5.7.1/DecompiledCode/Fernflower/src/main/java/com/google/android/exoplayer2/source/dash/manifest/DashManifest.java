package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.offline.FilterableManifest;
import java.util.Collections;
import java.util.List;

public class DashManifest implements FilterableManifest {
   public final long availabilityStartTimeMs;
   public final long durationMs;
   public final boolean dynamic;
   public final Uri location;
   public final long minBufferTimeMs;
   public final long minUpdatePeriodMs;
   private final List periods;
   public final ProgramInformation programInformation;
   public final long publishTimeMs;
   public final long suggestedPresentationDelayMs;
   public final long timeShiftBufferDepthMs;
   public final UtcTimingElement utcTiming;

   public DashManifest(long var1, long var3, long var5, boolean var7, long var8, long var10, long var12, long var14, ProgramInformation var16, UtcTimingElement var17, Uri var18, List var19) {
      this.availabilityStartTimeMs = var1;
      this.durationMs = var3;
      this.minBufferTimeMs = var5;
      this.dynamic = var7;
      this.minUpdatePeriodMs = var8;
      this.timeShiftBufferDepthMs = var10;
      this.suggestedPresentationDelayMs = var12;
      this.publishTimeMs = var14;
      this.programInformation = var16;
      this.utcTiming = var17;
      this.location = var18;
      if (var19 == null) {
         var19 = Collections.emptyList();
      }

      this.periods = var19;
   }

   public final Period getPeriod(int var1) {
      return (Period)this.periods.get(var1);
   }

   public final int getPeriodCount() {
      return this.periods.size();
   }

   public final long getPeriodDurationMs(int var1) {
      long var2;
      if (var1 == this.periods.size() - 1) {
         var2 = this.durationMs;
         if (var2 == -9223372036854775807L) {
            var2 = -9223372036854775807L;
         } else {
            var2 -= ((Period)this.periods.get(var1)).startMs;
         }
      } else {
         var2 = ((Period)this.periods.get(var1 + 1)).startMs - ((Period)this.periods.get(var1)).startMs;
      }

      return var2;
   }

   public final long getPeriodDurationUs(int var1) {
      return C.msToUs(this.getPeriodDurationMs(var1));
   }
}
