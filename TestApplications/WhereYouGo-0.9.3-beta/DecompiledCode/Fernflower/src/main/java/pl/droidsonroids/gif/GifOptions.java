package pl.droidsonroids.gif;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import pl.droidsonroids.gif.annotations.Beta;

@Beta
public class GifOptions {
   boolean inIsOpaque;
   char inSampleSize;

   public GifOptions() {
      this.reset();
   }

   private void reset() {
      this.inSampleSize = (char)1;
      this.inIsOpaque = false;
   }

   void setFrom(@Nullable GifOptions var1) {
      if (var1 == null) {
         this.reset();
      } else {
         this.inIsOpaque = var1.inIsOpaque;
         this.inSampleSize = (char)var1.inSampleSize;
      }

   }

   public void setInIsOpaque(boolean var1) {
      this.inIsOpaque = var1;
   }

   public void setInSampleSize(@IntRange(from = 1L,to = 65535L) int var1) {
      if (var1 >= 1 && var1 <= 65535) {
         this.inSampleSize = (char)((char)var1);
      } else {
         this.inSampleSize = (char)1;
      }

   }
}
