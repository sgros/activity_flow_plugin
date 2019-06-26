package android.support.v4.media;

import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class MediaBrowserCompatUtils {
   public static boolean areSameOptions(Bundle var0, Bundle var1) {
      boolean var2 = true;
      boolean var3 = true;
      boolean var4 = true;
      if (var0 == var1) {
         return true;
      } else if (var0 == null) {
         if (var1.getInt("android.media.browse.extra.PAGE", -1) != -1 || var1.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1) {
            var4 = false;
         }

         return var4;
      } else if (var1 == null) {
         if (var0.getInt("android.media.browse.extra.PAGE", -1) == -1 && var0.getInt("android.media.browse.extra.PAGE_SIZE", -1) == -1) {
            var4 = var2;
         } else {
            var4 = false;
         }

         return var4;
      } else {
         if (var0.getInt("android.media.browse.extra.PAGE", -1) == var1.getInt("android.media.browse.extra.PAGE", -1) && var0.getInt("android.media.browse.extra.PAGE_SIZE", -1) == var1.getInt("android.media.browse.extra.PAGE_SIZE", -1)) {
            var4 = var3;
         } else {
            var4 = false;
         }

         return var4;
      }
   }

   public static boolean hasDuplicatedItems(Bundle var0, Bundle var1) {
      int var2;
      if (var0 == null) {
         var2 = -1;
      } else {
         var2 = var0.getInt("android.media.browse.extra.PAGE", -1);
      }

      int var3;
      if (var1 == null) {
         var3 = -1;
      } else {
         var3 = var1.getInt("android.media.browse.extra.PAGE", -1);
      }

      int var4;
      if (var0 == null) {
         var4 = -1;
      } else {
         var4 = var0.getInt("android.media.browse.extra.PAGE_SIZE", -1);
      }

      int var5;
      if (var1 == null) {
         var5 = -1;
      } else {
         var5 = var1.getInt("android.media.browse.extra.PAGE_SIZE", -1);
      }

      int var6 = Integer.MAX_VALUE;
      if (var2 != -1 && var4 != -1) {
         var2 *= var4;
         int var7 = var4 + var2 - 1;
         var4 = var2;
         var2 = var7;
      } else {
         var2 = Integer.MAX_VALUE;
         var4 = 0;
      }

      if (var3 != -1 && var5 != -1) {
         var3 = var5 * var3;
         var5 = var5 + var3 - 1;
      } else {
         var3 = 0;
         var5 = var6;
      }

      if (var4 <= var3 && var3 <= var2) {
         return true;
      } else {
         return var4 <= var5 && var5 <= var2;
      }
   }
}
