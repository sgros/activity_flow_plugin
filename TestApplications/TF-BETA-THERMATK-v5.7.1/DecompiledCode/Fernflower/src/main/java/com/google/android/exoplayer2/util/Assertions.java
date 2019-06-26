package com.google.android.exoplayer2.util;

import android.os.Looper;
import android.text.TextUtils;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;

public final class Assertions {
   private Assertions() {
   }

   public static void checkArgument(boolean var0) {
      if (!var0) {
         throw new IllegalArgumentException();
      }
   }

   public static void checkArgument(boolean var0, Object var1) {
      if (!var0) {
         throw new IllegalArgumentException(String.valueOf(var1));
      }
   }

   public static int checkIndex(int var0, int var1, int var2) {
      if (var0 >= var1 && var0 < var2) {
         return var0;
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public static void checkMainThread() {
      if (Looper.myLooper() != Looper.getMainLooper()) {
         throw new IllegalStateException("Not in applications main thread");
      }
   }

   @EnsuresNonNull({"#1"})
   public static String checkNotEmpty(String var0) {
      if (!TextUtils.isEmpty(var0)) {
         return var0;
      } else {
         throw new IllegalArgumentException();
      }
   }

   @EnsuresNonNull({"#1"})
   public static String checkNotEmpty(String var0, Object var1) {
      if (!TextUtils.isEmpty(var0)) {
         return var0;
      } else {
         throw new IllegalArgumentException(String.valueOf(var1));
      }
   }

   @EnsuresNonNull({"#1"})
   public static Object checkNotNull(Object var0) {
      if (var0 != null) {
         return var0;
      } else {
         throw new NullPointerException();
      }
   }

   @EnsuresNonNull({"#1"})
   public static Object checkNotNull(Object var0, Object var1) {
      if (var0 != null) {
         return var0;
      } else {
         throw new NullPointerException(String.valueOf(var1));
      }
   }

   public static void checkState(boolean var0) {
      if (!var0) {
         throw new IllegalStateException();
      }
   }

   public static void checkState(boolean var0, Object var1) {
      if (!var0) {
         throw new IllegalStateException(String.valueOf(var1));
      }
   }
}
