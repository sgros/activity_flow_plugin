package com.google.zxing.oned.rss.expanded.decoders;

final class DecodedChar extends DecodedObject {
   static final char FNC1 = '$';
   private final char value;

   DecodedChar(int var1, char var2) {
      super(var1);
      this.value = (char)var2;
   }

   char getValue() {
      return this.value;
   }

   boolean isFNC1() {
      boolean var1;
      if (this.value == '$') {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
