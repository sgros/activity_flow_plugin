package kotlin.text;

class CharsKt__CharKt extends CharsKt__CharJVMKt {
   public static final boolean equals(char var0, char var1, boolean var2) {
      if (var0 == var1) {
         return true;
      } else if (!var2) {
         return false;
      } else if (Character.toUpperCase(var0) == Character.toUpperCase(var1)) {
         return true;
      } else {
         return Character.toLowerCase(var0) == Character.toLowerCase(var1);
      }
   }
}
