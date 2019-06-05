package kotlin.text;

import java.util.Collection;
import java.util.Iterator;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;

class StringsKt__StringsJVMKt extends StringsKt__StringNumberConversionsKt {
   public static final boolean isBlank(CharSequence var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      int var1 = var0.length();
      boolean var2 = false;
      if (var1 != 0) {
         boolean var4;
         label35: {
            Iterable var3 = (Iterable)StringsKt.getIndices(var0);
            if (!(var3 instanceof Collection) || !((Collection)var3).isEmpty()) {
               Iterator var5 = var3.iterator();

               while(var5.hasNext()) {
                  if (!CharsKt.isWhitespace(var0.charAt(((IntIterator)var5).nextInt()))) {
                     var4 = false;
                     break label35;
                  }
               }
            }

            var4 = true;
         }

         if (!var4) {
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   public static final boolean regionMatches(String var0, int var1, String var2, int var3, int var4, boolean var5) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var2, "other");
      if (!var5) {
         var5 = var0.regionMatches(var1, var2, var3, var4);
      } else {
         var5 = var0.regionMatches(var5, var1, var2, var3, var4);
      }

      return var5;
   }

   public static final boolean startsWith(String var0, String var1, boolean var2) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "prefix");
      return !var2 ? var0.startsWith(var1) : StringsKt.regionMatches(var0, 0, var1, 0, var1.length(), var2);
   }

   // $FF: synthetic method
   public static boolean startsWith$default(String var0, String var1, boolean var2, int var3, Object var4) {
      if ((var3 & 2) != 0) {
         var2 = false;
      }

      return StringsKt.startsWith(var0, var1, var2);
   }
}
