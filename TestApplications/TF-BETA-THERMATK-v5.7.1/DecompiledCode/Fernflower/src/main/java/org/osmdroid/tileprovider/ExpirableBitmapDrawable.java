package org.osmdroid.tileprovider;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ExpirableBitmapDrawable extends BitmapDrawable {
   private static final int[] settableStatuses = new int[]{-2, -3, -4};
   private int[] mState = new int[0];

   public ExpirableBitmapDrawable(Bitmap var1) {
      super(var1);
   }

   public static int getState(Drawable var0) {
      int[] var7 = var0.getState();
      int var1 = var7.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         int var3 = var7[var2];
         int[] var4 = settableStatuses;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            if (var3 == var4[var6]) {
               return var3;
            }
         }
      }

      return -1;
   }

   public static void setState(Drawable var0, int var1) {
      var0.setState(new int[]{var1});
   }

   public int[] getState() {
      return this.mState;
   }

   public boolean isStateful() {
      boolean var1;
      if (this.mState.length > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean setState(int[] var1) {
      this.mState = var1;
      return true;
   }
}
