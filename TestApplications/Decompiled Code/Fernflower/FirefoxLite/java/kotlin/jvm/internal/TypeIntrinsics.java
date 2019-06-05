package kotlin.jvm.internal;

import kotlin.Function;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function9;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.jvm.internal.markers.KMutableIterable;

public class TypeIntrinsics {
   public static Iterable asMutableIterable(Object var0) {
      if (var0 instanceof KMappedMarker && !(var0 instanceof KMutableIterable)) {
         throwCce(var0, "kotlin.collections.MutableIterable");
      }

      return castToIterable(var0);
   }

   public static Object beforeCheckcastToFunctionOfArity(Object var0, int var1) {
      if (var0 != null && !isFunctionOfArity(var0, var1)) {
         StringBuilder var2 = new StringBuilder();
         var2.append("kotlin.jvm.functions.Function");
         var2.append(var1);
         throwCce(var0, var2.toString());
      }

      return var0;
   }

   public static Iterable castToIterable(Object var0) {
      try {
         Iterable var2 = (Iterable)var0;
         return var2;
      } catch (ClassCastException var1) {
         throw throwCce(var1);
      }
   }

   public static int getFunctionArity(Object var0) {
      if (var0 instanceof FunctionBase) {
         return ((FunctionBase)var0).getArity();
      } else if (var0 instanceof Function0) {
         return 0;
      } else if (var0 instanceof Function1) {
         return 1;
      } else if (var0 instanceof Function2) {
         return 2;
      } else if (var0 instanceof Function3) {
         return 3;
      } else if (var0 instanceof Function4) {
         return 4;
      } else if (var0 instanceof Function5) {
         return 5;
      } else if (var0 instanceof Function6) {
         return 6;
      } else if (var0 instanceof Function7) {
         return 7;
      } else if (var0 instanceof Function8) {
         return 8;
      } else if (var0 instanceof Function9) {
         return 9;
      } else if (var0 instanceof Function10) {
         return 10;
      } else if (var0 instanceof Function11) {
         return 11;
      } else if (var0 instanceof Function12) {
         return 12;
      } else if (var0 instanceof Function13) {
         return 13;
      } else if (var0 instanceof Function14) {
         return 14;
      } else if (var0 instanceof Function15) {
         return 15;
      } else if (var0 instanceof Function16) {
         return 16;
      } else if (var0 instanceof Function17) {
         return 17;
      } else if (var0 instanceof Function18) {
         return 18;
      } else if (var0 instanceof Function19) {
         return 19;
      } else if (var0 instanceof Function20) {
         return 20;
      } else if (var0 instanceof Function21) {
         return 21;
      } else {
         return var0 instanceof Function22 ? 22 : -1;
      }
   }

   public static boolean isFunctionOfArity(Object var0, int var1) {
      boolean var2;
      if (var0 instanceof Function && getFunctionArity(var0) == var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static Throwable sanitizeStackTrace(Throwable var0) {
      return Intrinsics.sanitizeStackTrace(var0, TypeIntrinsics.class.getName());
   }

   public static ClassCastException throwCce(ClassCastException var0) {
      throw (ClassCastException)sanitizeStackTrace(var0);
   }

   public static void throwCce(Object var0, String var1) {
      String var3;
      if (var0 == null) {
         var3 = "null";
      } else {
         var3 = var0.getClass().getName();
      }

      StringBuilder var2 = new StringBuilder();
      var2.append(var3);
      var2.append(" cannot be cast to ");
      var2.append(var1);
      throwCce(var2.toString());
   }

   public static void throwCce(String var0) {
      throw throwCce(new ClassCastException(var0));
   }
}
