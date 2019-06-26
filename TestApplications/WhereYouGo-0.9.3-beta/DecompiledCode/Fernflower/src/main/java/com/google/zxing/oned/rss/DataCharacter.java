package com.google.zxing.oned.rss;

public class DataCharacter {
   private final int checksumPortion;
   private final int value;

   public DataCharacter(int var1, int var2) {
      this.value = var1;
      this.checksumPortion = var2;
   }

   public final boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof DataCharacter)) {
         var3 = var2;
      } else {
         DataCharacter var4 = (DataCharacter)var1;
         var3 = var2;
         if (this.value == var4.value) {
            var3 = var2;
            if (this.checksumPortion == var4.checksumPortion) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   public final int getChecksumPortion() {
      return this.checksumPortion;
   }

   public final int getValue() {
      return this.value;
   }

   public final int hashCode() {
      return this.value ^ this.checksumPortion;
   }

   public final String toString() {
      return this.value + "(" + this.checksumPortion + ')';
   }
}
