package org.mapsforge.graphics.android;

import android.graphics.BitmapFactory;
import java.io.InputStream;
import org.mapsforge.map.graphics.Bitmap;

class AndroidBitmap implements Bitmap {
   final android.graphics.Bitmap bitmap;

   AndroidBitmap(InputStream var1) {
      this.bitmap = BitmapFactory.decodeStream(var1);
   }

   public void destroy() {
      this.bitmap.recycle();
   }

   public int getHeight() {
      return this.bitmap.getHeight();
   }

   public int[] getPixels() {
      int var1 = this.getWidth();
      int var2 = this.getHeight();
      int[] var3 = new int[var1 * var2];
      this.bitmap.getPixels(var3, 0, var1, 0, 0, var1, var2);
      return var3;
   }

   public int getWidth() {
      return this.bitmap.getWidth();
   }
}
