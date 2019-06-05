package kotlin.text;

import kotlin.jvm.internal.Intrinsics;

class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt {
   public static final Integer toIntOrNull(String var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      return StringsKt.toIntOrNull(var0, 10);
   }

   public static final Integer toIntOrNull(String var0, int var1) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      CharsKt.checkRadix(var1);
      int var2 = var0.length();
      if (var2 == 0) {
         return null;
      } else {
         int var3;
         byte var4;
         int var6;
         boolean var7;
         int var11;
         label58: {
            var3 = 0;
            var4 = 0;
            char var5 = var0.charAt(0);
            var6 = -2147483647;
            if (var5 < '0') {
               if (var2 == 1) {
                  return null;
               }

               if (var5 == '-') {
                  var6 = Integer.MIN_VALUE;
                  var11 = 1;
                  var7 = true;
                  break label58;
               }

               if (var5 != '+') {
                  return null;
               }

               var11 = 1;
            } else {
               var11 = 0;
            }

            var7 = false;
         }

         int var8 = var6 / var1;
         --var2;
         if (var11 <= var2) {
            var3 = var4;

            while(true) {
               int var10 = CharsKt.digitOf(var0.charAt(var11), var1);
               if (var10 < 0) {
                  return null;
               }

               if (var3 < var8) {
                  return null;
               }

               var3 *= var1;
               if (var3 < var6 + var10) {
                  return null;
               }

               var10 = var3 - var10;
               var3 = var10;
               if (var11 == var2) {
                  break;
               }

               ++var11;
               var3 = var10;
            }
         }

         Integer var9;
         if (var7) {
            var9 = var3;
         } else {
            var9 = -var3;
         }

         return var9;
      }
   }
}
