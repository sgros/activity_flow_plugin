package kotlin.text;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

class StringsKt__StringBuilderKt extends StringsKt__StringBuilderJVMKt {
   public static final void appendElement(Appendable var0, Object var1, Function1 var2) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      if (var2 != null) {
         var0.append((CharSequence)var2.invoke(var1));
      } else {
         boolean var3;
         if (var1 != null) {
            var3 = var1 instanceof CharSequence;
         } else {
            var3 = true;
         }

         if (var3) {
            var0.append((CharSequence)var1);
         } else if (var1 instanceof Character) {
            var0.append((Character)var1);
         } else {
            var0.append((CharSequence)String.valueOf(var1));
         }
      }

   }
}
