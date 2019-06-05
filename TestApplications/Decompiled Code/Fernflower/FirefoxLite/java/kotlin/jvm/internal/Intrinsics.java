package kotlin.jvm.internal;

import java.util.Arrays;
import java.util.List;
import kotlin.KotlinNullPointerException;
import kotlin.UninitializedPropertyAccessException;

public class Intrinsics {
   private Intrinsics() {
   }

   public static boolean areEqual(Object var0, Object var1) {
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

   public static void checkExpressionValueIsNotNull(Object var0, String var1) {
      if (var0 == null) {
         StringBuilder var2 = new StringBuilder();
         var2.append(var1);
         var2.append(" must not be null");
         throw (IllegalStateException)sanitizeStackTrace(new IllegalStateException(var2.toString()));
      }
   }

   public static void checkParameterIsNotNull(Object var0, String var1) {
      if (var0 == null) {
         throwParameterIsNullException(var1);
      }

   }

   private static Throwable sanitizeStackTrace(Throwable var0) {
      return sanitizeStackTrace(var0, Intrinsics.class.getName());
   }

   static Throwable sanitizeStackTrace(Throwable var0, String var1) {
      StackTraceElement[] var2 = var0.getStackTrace();
      int var3 = var2.length;
      int var4 = -1;

      for(int var5 = 0; var5 < var3; ++var5) {
         if (var1.equals(var2[var5].getClassName())) {
            var4 = var5;
         }
      }

      List var6 = Arrays.asList(var2).subList(var4 + 1, var3);
      var0.setStackTrace((StackTraceElement[])var6.toArray(new StackTraceElement[var6.size()]));
      return var0;
   }

   public static void throwNpe() {
      throw (KotlinNullPointerException)sanitizeStackTrace(new KotlinNullPointerException());
   }

   private static void throwParameterIsNullException(String var0) {
      StackTraceElement var1 = Thread.currentThread().getStackTrace()[3];
      String var2 = var1.getClassName();
      String var4 = var1.getMethodName();
      StringBuilder var3 = new StringBuilder();
      var3.append("Parameter specified as non-null is null: method ");
      var3.append(var2);
      var3.append(".");
      var3.append(var4);
      var3.append(", parameter ");
      var3.append(var0);
      throw (IllegalArgumentException)sanitizeStackTrace(new IllegalArgumentException(var3.toString()));
   }

   public static void throwUninitializedProperty(String var0) {
      throw (UninitializedPropertyAccessException)sanitizeStackTrace(new UninitializedPropertyAccessException(var0));
   }

   public static void throwUninitializedPropertyAccessException(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("lateinit property ");
      var1.append(var0);
      var1.append(" has not been initialized");
      throwUninitializedProperty(var1.toString());
   }
}
