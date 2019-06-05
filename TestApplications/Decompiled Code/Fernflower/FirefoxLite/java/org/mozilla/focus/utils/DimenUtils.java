package org.mozilla.focus.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import org.mozilla.icon.FavIconUtils;

public class DimenUtils {
   private static int getDefaultFaviconBitmapSize(Resources var0) {
      return var0.getDimensionPixelSize(2131165339);
   }

   private static float getDefaultFaviconTextSize(Resources var0) {
      return var0.getDimension(2131165337);
   }

   @Deprecated
   public static int getFavIconType(Resources var0, Bitmap var1) {
      if (var1 != null && var1.getWidth() >= var0.getDimensionPixelSize(2131165338)) {
         return var1.getWidth() > var0.getDimensionPixelSize(2131165336) ? 1 : 0;
      } else {
         return 2;
      }
   }

   public static Bitmap getInitialBitmap(Resources var0, char var1, int var2) {
      return FavIconUtils.getInitialBitmap(var1, var2, getDefaultFaviconTextSize(var0), getDefaultFaviconBitmapSize(var0));
   }

   public static Bitmap getInitialBitmap(Resources var0, Bitmap var1, char var2) {
      return FavIconUtils.getInitialBitmap(var1, var2, getDefaultFaviconTextSize(var0), getDefaultFaviconBitmapSize(var0));
   }

   @Deprecated
   public static Bitmap getRefinedBitmap(Resources var0, Bitmap var1, char var2) {
      switch(getFavIconType(var0, var1)) {
      case 0:
         return var1;
      case 1:
         int var3 = var0.getDimensionPixelSize(2131165339);
         return Bitmap.createScaledBitmap(var1, var3, var3, false);
      case 2:
         return getInitialBitmap(var0, var1, var2);
      default:
         return getInitialBitmap(var0, var1, var2);
      }
   }

   static Bitmap getRefinedShortcutIcon(Resources var0, Bitmap var1, char var2) {
      int var3 = var0.getDimensionPixelSize(2131165439);
      if (var1 != null && var1.getWidth() >= var3) {
         return var1.getWidth() > var3 ? Bitmap.createScaledBitmap(var1, var3, var3, false) : var1;
      } else {
         return getInitialBitmap(var0, var1, var2);
      }
   }

   public static boolean iconTooBlurry(Resources var0, int var1) {
      boolean var2;
      if (var1 < var0.getDimensionPixelSize(2131165338)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
