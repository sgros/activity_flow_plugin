package org.mozilla.focus.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

public class DrawableUtils {
   public static Drawable getAndroidDrawable(Context var0, String var1) {
      String var2 = var0.getPackageName();
      int var3 = var0.getResources().getIdentifier(var1, "drawable", var2);
      return var3 == 0 ? null : var0.getDrawable(var3);
   }

   public static Bitmap getBitmap(Drawable var0) {
      if (var0 instanceof BitmapDrawable) {
         return ((BitmapDrawable)var0).getBitmap();
      } else {
         Bitmap var1 = Bitmap.createBitmap(var0.getIntrinsicWidth(), var0.getIntrinsicHeight(), Config.ARGB_8888);
         Canvas var2 = new Canvas(var1);
         var0.setBounds(0, 0, var2.getWidth(), var2.getHeight());
         var0.draw(var2);
         return var1;
      }
   }

   public static Drawable loadAndTintDrawable(Context var0, int var1, int var2) {
      Drawable var3 = DrawableCompat.wrap(var0.getResources().getDrawable(var1, var0.getTheme()).mutate());
      DrawableCompat.setTint(var3, var2);
      return var3;
   }
}
