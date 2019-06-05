package com.airbnb.lottie;

import android.graphics.Bitmap;

public class LottieImageAsset {
   private Bitmap bitmap;
   private final String dirName;
   private final String fileName;
   private final int height;
   private final String id;
   private final int width;

   public LottieImageAsset(int var1, int var2, String var3, String var4, String var5) {
      this.width = var1;
      this.height = var2;
      this.id = var3;
      this.fileName = var4;
      this.dirName = var5;
   }

   public Bitmap getBitmap() {
      return this.bitmap;
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getId() {
      return this.id;
   }

   public void setBitmap(Bitmap var1) {
      this.bitmap = var1;
   }
}
