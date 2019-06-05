package com.bumptech.glide.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Looper;
import android.os.Build.VERSION;
import com.bumptech.glide.load.model.Model;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public final class Util {
   private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
   private static final char[] SHA_256_CHARS = new char[64];

   public static void assertMainThread() {
      if (!isOnMainThread()) {
         throw new IllegalArgumentException("You must call this method on the main thread");
      }
   }

   public static boolean bothModelsNullEquivalentOrEquals(Object var0, Object var1) {
      if (var0 == null) {
         boolean var2;
         if (var1 == null) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      } else {
         return var0 instanceof Model ? ((Model)var0).isEquivalentTo(var1) : var0.equals(var1);
      }
   }

   public static boolean bothNullOrEqual(Object var0, Object var1) {
      boolean var2;
      if (var0 == null) {
         if (var1 == null) {
            var2 = true;
         } else {
            var2 = false;
         }
      } else {
         var2 = var0.equals(var1);
      }

      return var2;
   }

   private static String bytesToHex(byte[] var0, char[] var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         int var3 = var0[var2] & 255;
         int var4 = var2 * 2;
         var1[var4] = (char)HEX_CHAR_ARRAY[var3 >>> 4];
         var1[var4 + 1] = (char)HEX_CHAR_ARRAY[var3 & 15];
      }

      return new String(var1);
   }

   public static Queue createQueue(int var0) {
      return new ArrayDeque(var0);
   }

   public static int getBitmapByteSize(int var0, int var1, Config var2) {
      return var0 * var1 * getBytesPerPixel(var2);
   }

   @TargetApi(19)
   public static int getBitmapByteSize(Bitmap var0) {
      if (var0.isRecycled()) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Cannot obtain size for recycled Bitmap: ");
         var2.append(var0);
         var2.append("[");
         var2.append(var0.getWidth());
         var2.append("x");
         var2.append(var0.getHeight());
         var2.append("] ");
         var2.append(var0.getConfig());
         throw new IllegalStateException(var2.toString());
      } else {
         if (VERSION.SDK_INT >= 19) {
            try {
               int var1 = var0.getAllocationByteCount();
               return var1;
            } catch (NullPointerException var3) {
            }
         }

         return var0.getHeight() * var0.getRowBytes();
      }
   }

   private static int getBytesPerPixel(Config var0) {
      Config var1 = var0;
      if (var0 == null) {
         var1 = Config.ARGB_8888;
      }

      byte var2;
      switch(var1) {
      case ALPHA_8:
         var2 = 1;
         break;
      case RGB_565:
      case ARGB_4444:
         var2 = 2;
         break;
      default:
         var2 = 4;
      }

      return var2;
   }

   public static List getSnapshot(Collection var0) {
      ArrayList var1 = new ArrayList(var0.size());
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         var1.add(var2.next());
      }

      return var1;
   }

   public static int hashCode(float var0) {
      return hashCode(var0, 17);
   }

   public static int hashCode(float var0, int var1) {
      return hashCode(Float.floatToIntBits(var0), var1);
   }

   public static int hashCode(int var0, int var1) {
      return var1 * 31 + var0;
   }

   public static int hashCode(Object var0, int var1) {
      int var2;
      if (var0 == null) {
         var2 = 0;
      } else {
         var2 = var0.hashCode();
      }

      return hashCode(var2, var1);
   }

   public static int hashCode(boolean var0, int var1) {
      return hashCode(var0, var1);
   }

   public static boolean isOnBackgroundThread() {
      return isOnMainThread() ^ true;
   }

   public static boolean isOnMainThread() {
      boolean var0;
      if (Looper.myLooper() == Looper.getMainLooper()) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   private static boolean isValidDimension(int var0) {
      boolean var1;
      if (var0 <= 0 && var0 != Integer.MIN_VALUE) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isValidDimensions(int var0, int var1) {
      boolean var2;
      if (isValidDimension(var0) && isValidDimension(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static String sha256BytesToHex(byte[] param0) {
      // $FF: Couldn't be decompiled
   }
}
