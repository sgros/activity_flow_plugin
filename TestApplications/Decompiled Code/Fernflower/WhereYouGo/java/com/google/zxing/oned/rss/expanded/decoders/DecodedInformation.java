package com.google.zxing.oned.rss.expanded.decoders;

final class DecodedInformation extends DecodedObject {
   private final String newString;
   private final boolean remaining;
   private final int remainingValue;

   DecodedInformation(int var1, String var2) {
      super(var1);
      this.newString = var2;
      this.remaining = false;
      this.remainingValue = 0;
   }

   DecodedInformation(int var1, String var2, int var3) {
      super(var1);
      this.remaining = true;
      this.remainingValue = var3;
      this.newString = var2;
   }

   String getNewString() {
      return this.newString;
   }

   int getRemainingValue() {
      return this.remainingValue;
   }

   boolean isRemaining() {
      return this.remaining;
   }
}
