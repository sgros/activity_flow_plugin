package com.google.zxing.oned.rss.expanded;

import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;

final class ExpandedPair {
   private final FinderPattern finderPattern;
   private final DataCharacter leftChar;
   private final boolean mayBeLast;
   private final DataCharacter rightChar;

   ExpandedPair(DataCharacter var1, DataCharacter var2, FinderPattern var3, boolean var4) {
      this.leftChar = var1;
      this.rightChar = var2;
      this.finderPattern = var3;
      this.mayBeLast = var4;
   }

   private static boolean equalsOrNull(Object var0, Object var1) {
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

   private static int hashNotNull(Object var0) {
      int var1;
      if (var0 == null) {
         var1 = 0;
      } else {
         var1 = var0.hashCode();
      }

      return var1;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof ExpandedPair)) {
         var3 = var2;
      } else {
         ExpandedPair var4 = (ExpandedPair)var1;
         var3 = var2;
         if (equalsOrNull(this.leftChar, var4.leftChar)) {
            var3 = var2;
            if (equalsOrNull(this.rightChar, var4.rightChar)) {
               var3 = var2;
               if (equalsOrNull(this.finderPattern, var4.finderPattern)) {
                  var3 = true;
               }
            }
         }
      }

      return var3;
   }

   FinderPattern getFinderPattern() {
      return this.finderPattern;
   }

   DataCharacter getLeftChar() {
      return this.leftChar;
   }

   DataCharacter getRightChar() {
      return this.rightChar;
   }

   public int hashCode() {
      return hashNotNull(this.leftChar) ^ hashNotNull(this.rightChar) ^ hashNotNull(this.finderPattern);
   }

   boolean mayBeLast() {
      return this.mayBeLast;
   }

   public boolean mustBeLast() {
      boolean var1;
      if (this.rightChar == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = (new StringBuilder("[ ")).append(this.leftChar).append(" , ").append(this.rightChar).append(" : ");
      Object var2;
      if (this.finderPattern == null) {
         var2 = "null";
      } else {
         var2 = this.finderPattern.getValue();
      }

      return var1.append(var2).append(" ]").toString();
   }
}
