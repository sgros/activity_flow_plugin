package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.Assertions;

public interface SeekMap {
   long getDurationUs();

   SeekMap.SeekPoints getSeekPoints(long var1);

   boolean isSeekable();

   public static final class SeekPoints {
      public final SeekPoint first;
      public final SeekPoint second;

      public SeekPoints(SeekPoint var1) {
         this(var1, var1);
      }

      public SeekPoints(SeekPoint var1, SeekPoint var2) {
         Assertions.checkNotNull(var1);
         this.first = (SeekPoint)var1;
         Assertions.checkNotNull(var2);
         this.second = (SeekPoint)var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && SeekMap.SeekPoints.class == var1.getClass()) {
            SeekMap.SeekPoints var3 = (SeekMap.SeekPoints)var1;
            if (!this.first.equals(var3.first) || !this.second.equals(var3.second)) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.first.hashCode() * 31 + this.second.hashCode();
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("[");
         var1.append(this.first);
         String var2;
         if (this.first.equals(this.second)) {
            var2 = "";
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append(", ");
            var3.append(this.second);
            var2 = var3.toString();
         }

         var1.append(var2);
         var1.append("]");
         return var1.toString();
      }
   }

   public static final class Unseekable implements SeekMap {
      private final long durationUs;
      private final SeekMap.SeekPoints startSeekPoints;

      public Unseekable(long var1) {
         this(var1, 0L);
      }

      public Unseekable(long var1, long var3) {
         this.durationUs = var1;
         SeekPoint var5;
         if (var3 == 0L) {
            var5 = SeekPoint.START;
         } else {
            var5 = new SeekPoint(0L, var3);
         }

         this.startSeekPoints = new SeekMap.SeekPoints(var5);
      }

      public long getDurationUs() {
         return this.durationUs;
      }

      public SeekMap.SeekPoints getSeekPoints(long var1) {
         return this.startSeekPoints;
      }

      public boolean isSeekable() {
         return false;
      }
   }
}
