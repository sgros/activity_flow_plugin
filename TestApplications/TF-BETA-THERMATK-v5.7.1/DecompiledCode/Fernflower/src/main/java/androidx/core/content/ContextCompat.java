package androidx.core.content;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.TypedValue;
import java.io.File;

public class ContextCompat {
   private static final Object sLock = new Object();
   private static TypedValue sTempValue;

   public static ColorStateList getColorStateList(Context var0, int var1) {
      return VERSION.SDK_INT >= 23 ? var0.getColorStateList(var1) : var0.getResources().getColorStateList(var1);
   }

   public static Drawable getDrawable(Context var0, int var1) {
      int var2 = VERSION.SDK_INT;
      if (var2 >= 21) {
         return var0.getDrawable(var1);
      } else if (var2 >= 16) {
         return var0.getResources().getDrawable(var1);
      } else {
         Object var3 = sLock;
         synchronized(var3){}

         Throwable var10000;
         boolean var10001;
         label160: {
            try {
               if (sTempValue == null) {
                  TypedValue var4 = new TypedValue();
                  sTempValue = var4;
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label160;
            }

            label157:
            try {
               var0.getResources().getValue(var1, sTempValue, true);
               var1 = sTempValue.resourceId;
               return var0.getResources().getDrawable(var1);
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label157;
            }
         }

         while(true) {
            Throwable var17 = var10000;

            try {
               throw var17;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public static File[] getExternalCacheDirs(Context var0) {
      return VERSION.SDK_INT >= 19 ? var0.getExternalCacheDirs() : new File[]{var0.getExternalCacheDir()};
   }

   public static File[] getExternalFilesDirs(Context var0, String var1) {
      return VERSION.SDK_INT >= 19 ? var0.getExternalFilesDirs(var1) : new File[]{var0.getExternalFilesDir(var1)};
   }

   public static void startActivity(Context var0, Intent var1, Bundle var2) {
      if (VERSION.SDK_INT >= 16) {
         var0.startActivity(var1, var2);
      } else {
         var0.startActivity(var1);
      }

   }
}
