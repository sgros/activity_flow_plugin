package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class BitmapPoolAdapter implements BitmapPool {
   public void clearMemory() {
   }

   public Bitmap get(int var1, int var2, Config var3) {
      return Bitmap.createBitmap(var1, var2, var3);
   }

   public Bitmap getDirty(int var1, int var2, Config var3) {
      return this.get(var1, var2, var3);
   }

   public void put(Bitmap var1) {
      var1.recycle();
   }

   public void trimMemory(int var1) {
   }
}
