package kotlin.internal;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public final class PlatformImplementationsKt {
   public static final PlatformImplementations IMPLEMENTATIONS;

   static {
      // $FF: Couldn't be decompiled
   }

   private static final int getJavaVersion() {
      String var0 = System.getProperty("java.specification.version");
      int var1 = 65542;
      if (var0 == null) {
         return 65542;
      } else {
         CharSequence var2 = (CharSequence)var0;
         int var3 = StringsKt.indexOf$default(var2, '.', 0, false, 6, (Object)null);
         int var4;
         if (var3 < 0) {
            try {
               var4 = Integer.parseInt(var0);
            } catch (NumberFormatException var7) {
               return var1;
            }

            var1 = var4 * 65536;
            return var1;
         } else {
            int var5 = var3 + 1;
            int var6 = StringsKt.indexOf$default(var2, '.', var5, false, 4, (Object)null);
            var4 = var6;
            if (var6 < 0) {
               var4 = var0.length();
            }

            if (var0 == null) {
               throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            } else {
               String var9 = var0.substring(0, var3);
               Intrinsics.checkExpressionValueIsNotNull(var9, "(this as java.lang.Strin…ing(startIndex, endIndex)");
               if (var0 != null) {
                  var0 = var0.substring(var5, var4);
                  Intrinsics.checkExpressionValueIsNotNull(var0, "(this as java.lang.Strin…ing(startIndex, endIndex)");

                  try {
                     var4 = Integer.parseInt(var9);
                     var6 = Integer.parseInt(var0);
                  } catch (NumberFormatException var8) {
                     return var1;
                  }

                  var1 = var4 * 65536 + var6;
                  return var1;
               } else {
                  throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
               }
            }
         }
      }
   }
}
