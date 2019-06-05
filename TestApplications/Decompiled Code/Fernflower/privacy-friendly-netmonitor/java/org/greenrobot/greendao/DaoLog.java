package org.greenrobot.greendao;

import android.util.Log;

public class DaoLog {
   public static final int ASSERT = 7;
   public static final int DEBUG = 3;
   public static final int ERROR = 6;
   public static final int INFO = 4;
   private static final String TAG = "greenDAO";
   public static final int VERBOSE = 2;
   public static final int WARN = 5;

   public static int d(String var0) {
      return Log.d("greenDAO", var0);
   }

   public static int d(String var0, Throwable var1) {
      return Log.d("greenDAO", var0, var1);
   }

   public static int e(String var0) {
      return Log.w("greenDAO", var0);
   }

   public static int e(String var0, Throwable var1) {
      return Log.e("greenDAO", var0, var1);
   }

   public static String getStackTraceString(Throwable var0) {
      return Log.getStackTraceString(var0);
   }

   public static int i(String var0) {
      return Log.i("greenDAO", var0);
   }

   public static int i(String var0, Throwable var1) {
      return Log.i("greenDAO", var0, var1);
   }

   public static boolean isLoggable(int var0) {
      return Log.isLoggable("greenDAO", var0);
   }

   public static int println(int var0, String var1) {
      return Log.println(var0, "greenDAO", var1);
   }

   public static int v(String var0) {
      return Log.v("greenDAO", var0);
   }

   public static int v(String var0, Throwable var1) {
      return Log.v("greenDAO", var0, var1);
   }

   public static int w(String var0) {
      return Log.w("greenDAO", var0);
   }

   public static int w(String var0, Throwable var1) {
      return Log.w("greenDAO", var0, var1);
   }

   public static int w(Throwable var0) {
      return Log.w("greenDAO", var0);
   }
}
