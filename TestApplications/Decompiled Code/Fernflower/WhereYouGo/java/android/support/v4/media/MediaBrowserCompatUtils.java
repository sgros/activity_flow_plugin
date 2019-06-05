package android.support.v4.media;

import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class MediaBrowserCompatUtils {
   public static boolean areSameOptions(Bundle var0, Bundle var1) {
      boolean var2 = true;
      if (var0 != var1) {
         if (var0 == null) {
            if (var1.getInt("android.media.browse.extra.PAGE", -1) != -1 || var1.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1) {
               var2 = false;
            }
         } else if (var1 == null) {
            if (var0.getInt("android.media.browse.extra.PAGE", -1) != -1 || var0.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1) {
               var2 = false;
            }
         } else if (var0.getInt("android.media.browse.extra.PAGE", -1) != var1.getInt("android.media.browse.extra.PAGE", -1) || var0.getInt("android.media.browse.extra.PAGE_SIZE", -1) != var1.getInt("android.media.browse.extra.PAGE_SIZE", -1)) {
            var2 = false;
         }
      }

      return var2;
   }

   public static boolean hasDuplicatedItems(Bundle var0, Bundle var1) {
      boolean var2 = true;
      int var3;
      if (var0 == null) {
         var3 = -1;
      } else {
         var3 = var0.getInt("android.media.browse.extra.PAGE", -1);
      }

      int var4;
      if (var1 == null) {
         var4 = -1;
      } else {
         var4 = var1.getInt("android.media.browse.extra.PAGE", -1);
      }

      int var5;
      if (var0 == null) {
         var5 = -1;
      } else {
         var5 = var0.getInt("android.media.browse.extra.PAGE_SIZE", -1);
      }

      int var6;
      if (var1 == null) {
         var6 = -1;
      } else {
         var6 = var1.getInt("android.media.browse.extra.PAGE_SIZE", -1);
      }

      if (var3 != -1 && var5 != -1) {
         var3 = var5 * var3;
         var5 = var3 + var5 - 1;
      } else {
         var3 = 0;
         var5 = Integer.MAX_VALUE;
      }

      if (var4 != -1 && var6 != -1) {
         int var7 = var6 * var4;
         var4 = var7 + var6 - 1;
         var6 = var7;
      } else {
         var6 = 0;
         var4 = Integer.MAX_VALUE;
      }

      if ((var3 > var6 || var6 > var5) && (var3 > var4 || var4 > var5)) {
         var2 = false;
      }

      return var2;
   }
}
