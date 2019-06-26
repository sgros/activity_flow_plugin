package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Assertions;

public final class PlaybackParameters {
   public static final PlaybackParameters DEFAULT = new PlaybackParameters(1.0F);
   public final float pitch;
   private final int scaledUsPerMs;
   public final boolean skipSilence;
   public final float speed;

   public PlaybackParameters(float var1) {
      this(var1, 1.0F, false);
   }

   public PlaybackParameters(float var1, float var2) {
      this(var1, var2, false);
   }

   public PlaybackParameters(float var1, float var2, boolean var3) {
      boolean var4 = true;
      boolean var5;
      if (var1 > 0.0F) {
         var5 = true;
      } else {
         var5 = false;
      }

      Assertions.checkArgument(var5);
      if (var2 > 0.0F) {
         var5 = var4;
      } else {
         var5 = false;
      }

      Assertions.checkArgument(var5);
      this.speed = var1;
      this.pitch = var2;
      this.skipSilence = var3;
      this.scaledUsPerMs = Math.round(var1 * 1000.0F);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && PlaybackParameters.class == var1.getClass()) {
         PlaybackParameters var3 = (PlaybackParameters)var1;
         if (this.speed != var3.speed || this.pitch != var3.pitch || this.skipSilence != var3.skipSilence) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public long getMediaTimeUsForPlayoutTimeMs(long var1) {
      return var1 * (long)this.scaledUsPerMs;
   }

   public int hashCode() {
      return ((527 + Float.floatToRawIntBits(this.speed)) * 31 + Float.floatToRawIntBits(this.pitch)) * 31 + this.skipSilence;
   }
}
