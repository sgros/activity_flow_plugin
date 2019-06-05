package menion.android.whereyougo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import menion.android.whereyougo.preferences.Preferences;

public class Images {
   public static final Bitmap IMAGE_EMPTY_B = getImageB(2130837578);
   public static final int SIZE_BIG = (int)Utils.getDpPixels(32.0F);
   public static final int SIZE_HUGE = (int)Utils.getDpPixels(48.0F);
   public static final int SIZE_MEDIUM = (int)Utils.getDpPixels(24.0F);
   public static final int SIZE_SMALL = (int)Utils.getDpPixels(16.0F);
   private static final String TAG = "Images";

   public static Bitmap getImageB(int var0) {
      Exception var10000;
      Bitmap var1;
      boolean var10001;
      if (var0 <= 0) {
         try {
            var1 = IMAGE_EMPTY_B;
            return var1;
         } catch (Exception var2) {
            var10000 = var2;
            var10001 = false;
         }
      } else {
         try {
            var1 = BitmapFactory.decodeResource(A.getApp().getResources(), var0);
            return var1;
         } catch (Exception var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      Exception var4 = var10000;
      Logger.w("Images", "getImageB(" + var0 + "), e:" + var4.toString());
      var1 = IMAGE_EMPTY_B;
      return var1;
   }

   public static Bitmap getImageB(int var0, int var1) {
      return resizeBitmap(getImageB(var0), var1);
   }

   public static Drawable getImageD(int var0) {
      Drawable var1;
      try {
         var1 = A.getApp().getResources().getDrawable(var0);
         var1.setBounds(0, 0, var1.getIntrinsicWidth(), var1.getIntrinsicHeight());
      } catch (Exception var2) {
         var1 = null;
      }

      return var1;
   }

   public static Drawable getImageD(int var0, int var1) {
      Drawable var2;
      if (A.getApp() == null) {
         var2 = null;
      } else {
         var2 = getSizeOptimizedIcon(A.getApp().getResources().getDrawable(var0), var1);
      }

      return var2;
   }

   public static Drawable getImageD(Bitmap var0) {
      BitmapDrawable var1 = new BitmapDrawable(var0);
      var1.setBounds(0, 0, var1.getIntrinsicWidth(), var1.getIntrinsicHeight());
      return var1;
   }

   public static Drawable getSizeOptimizedIcon(Drawable var0, int var1) {
      if (var0 == null) {
         var0 = getImageD(2130837578);
      } else {
         var0.setBounds(0, 0, var1, var1);
         var0.invalidateSelf();
      }

      return var0;
   }

   public static Bitmap overlayBitmapToCenter(Bitmap var0, Bitmap var1) {
      int var2 = Math.max(var0.getWidth(), var1.getWidth());
      int var3 = Math.max(var0.getHeight(), var1.getHeight());
      float var4 = (float)(var2 - var0.getWidth());
      float var5 = (float)(var3 - var0.getHeight());
      float var6 = (float)(var2 - var1.getWidth());
      float var7 = (float)(var3 - var1.getHeight());
      Bitmap var8 = Bitmap.createBitmap(var2, var3, var0.getConfig());
      Canvas var9 = new Canvas(var8);
      var9.drawBitmap(var0, var4 * 0.5F, var5 * 0.5F, (Paint)null);
      var9.drawBitmap(var1, var6 * 0.5F, var7 * 0.5F, (Paint)null);
      return var8;
   }

   public static Bitmap resizeBitmap(Bitmap var0) {
      Bitmap var1 = var0;
      if (Preferences.APPEARANCE_IMAGE_STRETCH) {
         var1 = resizeBitmap(var0, Const.SCREEN_WIDTH);
      }

      return var1;
   }

   public static Bitmap resizeBitmap(Bitmap var0, int var1) {
      if (var0 == null) {
         var0 = null;
      } else {
         var0 = resizeBitmap(var0, var1, var0.getHeight() * var1 / var0.getWidth());
      }

      return var0;
   }

   public static Bitmap resizeBitmap(Bitmap var0, int var1, int var2) {
      Bitmap var3 = var0;
      if (var0 != null) {
         var3 = var0;
         if (var1 > 0) {
            if (var0.getWidth() == var1) {
               var3 = var0;
            } else {
               var3 = Bitmap.createScaledBitmap(var0, var1, var2, true);
            }
         }
      }

      return var3;
   }
}
