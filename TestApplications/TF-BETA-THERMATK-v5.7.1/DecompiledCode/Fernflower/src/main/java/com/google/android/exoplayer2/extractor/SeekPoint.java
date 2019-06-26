package com.google.android.exoplayer2.extractor;

public final class SeekPoint {
   public static final SeekPoint START = new SeekPoint(0L, 0L);
   public final long position;
   public final long timeUs;

   public SeekPoint(long var1, long var3) {
      this.timeUs = var1;
      this.position = var3;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && SeekPoint.class == var1.getClass()) {
         SeekPoint var3 = (SeekPoint)var1;
         if (this.timeUs != var3.timeUs || this.position != var3.position) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (int)this.timeUs * 31 + (int)this.position;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[timeUs=");
      var1.append(this.timeUs);
      var1.append(", position=");
      var1.append(this.position);
      var1.append("]");
      return var1.toString();
   }
}
