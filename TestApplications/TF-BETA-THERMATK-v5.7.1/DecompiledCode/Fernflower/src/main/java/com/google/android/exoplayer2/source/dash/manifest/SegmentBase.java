package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Util;
import java.util.List;

public abstract class SegmentBase {
   final RangedUri initialization;
   final long presentationTimeOffset;
   final long timescale;

   public SegmentBase(RangedUri var1, long var2, long var4) {
      this.initialization = var1;
      this.timescale = var2;
      this.presentationTimeOffset = var4;
   }

   public RangedUri getInitialization(Representation var1) {
      return this.initialization;
   }

   public long getPresentationTimeOffsetUs() {
      return Util.scaleLargeTimestamp(this.presentationTimeOffset, 1000000L, this.timescale);
   }

   public abstract static class MultiSegmentBase extends SegmentBase {
      final long duration;
      final List segmentTimeline;
      final long startNumber;

      public MultiSegmentBase(RangedUri var1, long var2, long var4, long var6, long var8, List var10) {
         super(var1, var2, var4);
         this.startNumber = var6;
         this.duration = var8;
         this.segmentTimeline = var10;
      }

      public long getFirstSegmentNum() {
         return this.startNumber;
      }

      public abstract int getSegmentCount(long var1);

      public final long getSegmentDurationUs(long var1, long var3) {
         List var5 = this.segmentTimeline;
         if (var5 != null) {
            return ((SegmentBase.SegmentTimelineElement)var5.get((int)(var1 - this.startNumber))).duration * 1000000L / super.timescale;
         } else {
            int var6 = this.getSegmentCount(var3);
            if (var6 != -1 && var1 == this.getFirstSegmentNum() + (long)var6 - 1L) {
               var1 = var3 - this.getSegmentTimeUs(var1);
            } else {
               var1 = this.duration * 1000000L / super.timescale;
            }

            return var1;
         }
      }

      public long getSegmentNum(long var1, long var3) {
         long var5 = this.getFirstSegmentNum();
         var3 = (long)this.getSegmentCount(var3);
         if (var3 == 0L) {
            return var5;
         } else {
            long var7;
            long var9;
            if (this.segmentTimeline == null) {
               var7 = this.duration * 1000000L / super.timescale;
               var9 = this.startNumber;
               var1 = var1 / var7 + var9;
               if (var1 < var5) {
                  var1 = var5;
               } else if (var3 != -1L) {
                  var1 = Math.min(var1, var5 + var3 - 1L);
               }

               return var1;
            } else {
               var9 = var3 + var5 - 1L;
               var3 = var5;

               while(var3 <= var9) {
                  var7 = (var9 - var3) / 2L + var3;
                  long var11 = this.getSegmentTimeUs(var7);
                  if (var11 < var1) {
                     var3 = var7 + 1L;
                  } else {
                     if (var11 <= var1) {
                        return var7;
                     }

                     var9 = var7 - 1L;
                  }
               }

               if (var3 != var5) {
                  var3 = var9;
               }

               return var3;
            }
         }
      }

      public final long getSegmentTimeUs(long var1) {
         List var3 = this.segmentTimeline;
         if (var3 != null) {
            var1 = ((SegmentBase.SegmentTimelineElement)var3.get((int)(var1 - this.startNumber))).startTime - super.presentationTimeOffset;
         } else {
            var1 = (var1 - this.startNumber) * this.duration;
         }

         return Util.scaleLargeTimestamp(var1, 1000000L, super.timescale);
      }

      public abstract RangedUri getSegmentUrl(Representation var1, long var2);

      public boolean isExplicit() {
         boolean var1;
         if (this.segmentTimeline != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static class SegmentList extends SegmentBase.MultiSegmentBase {
      final List mediaSegments;

      public SegmentList(RangedUri var1, long var2, long var4, long var6, long var8, List var10, List var11) {
         super(var1, var2, var4, var6, var8, var10);
         this.mediaSegments = var11;
      }

      public int getSegmentCount(long var1) {
         return this.mediaSegments.size();
      }

      public RangedUri getSegmentUrl(Representation var1, long var2) {
         return (RangedUri)this.mediaSegments.get((int)(var2 - super.startNumber));
      }

      public boolean isExplicit() {
         return true;
      }
   }

   public static class SegmentTemplate extends SegmentBase.MultiSegmentBase {
      final UrlTemplate initializationTemplate;
      final UrlTemplate mediaTemplate;

      public SegmentTemplate(RangedUri var1, long var2, long var4, long var6, long var8, List var10, UrlTemplate var11, UrlTemplate var12) {
         super(var1, var2, var4, var6, var8, var10);
         this.initializationTemplate = var11;
         this.mediaTemplate = var12;
      }

      public RangedUri getInitialization(Representation var1) {
         UrlTemplate var2 = this.initializationTemplate;
         if (var2 != null) {
            Format var3 = var1.format;
            return new RangedUri(var2.buildUri(var3.id, 0L, var3.bitrate, 0L), 0L, -1L);
         } else {
            return super.getInitialization(var1);
         }
      }

      public int getSegmentCount(long var1) {
         List var3 = super.segmentTimeline;
         if (var3 != null) {
            return var3.size();
         } else {
            return var1 != -9223372036854775807L ? (int)Util.ceilDivide(var1, super.duration * 1000000L / super.timescale) : -1;
         }
      }

      public RangedUri getSegmentUrl(Representation var1, long var2) {
         List var4 = super.segmentTimeline;
         long var5;
         if (var4 != null) {
            var5 = ((SegmentBase.SegmentTimelineElement)var4.get((int)(var2 - super.startNumber))).startTime;
         } else {
            var5 = (var2 - super.startNumber) * super.duration;
         }

         UrlTemplate var8 = this.mediaTemplate;
         Format var7 = var1.format;
         return new RangedUri(var8.buildUri(var7.id, var2, var7.bitrate, var5), 0L, -1L);
      }
   }

   public static class SegmentTimelineElement {
      final long duration;
      final long startTime;

      public SegmentTimelineElement(long var1, long var3) {
         this.startTime = var1;
         this.duration = var3;
      }
   }

   public static class SingleSegmentBase extends SegmentBase {
      final long indexLength;
      final long indexStart;

      public SingleSegmentBase() {
         this((RangedUri)null, 1L, 0L, 0L, 0L);
      }

      public SingleSegmentBase(RangedUri var1, long var2, long var4, long var6, long var8) {
         super(var1, var2, var4);
         this.indexStart = var6;
         this.indexLength = var8;
      }

      public RangedUri getIndex() {
         long var1 = this.indexLength;
         RangedUri var3;
         if (var1 <= 0L) {
            var3 = null;
         } else {
            var3 = new RangedUri((String)null, this.indexStart, var1);
         }

         return var3;
      }
   }
}
