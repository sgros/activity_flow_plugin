package com.google.zxing.oned.rss.expanded;

import java.util.ArrayList;
import java.util.List;

final class ExpandedRow {
   private final List pairs;
   private final int rowNumber;
   private final boolean wasReversed;

   ExpandedRow(List var1, int var2, boolean var3) {
      this.pairs = new ArrayList(var1);
      this.rowNumber = var2;
      this.wasReversed = var3;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof ExpandedRow)) {
         var3 = var2;
      } else {
         ExpandedRow var4 = (ExpandedRow)var1;
         var3 = var2;
         if (this.pairs.equals(var4.getPairs())) {
            var3 = var2;
            if (this.wasReversed == var4.wasReversed) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   List getPairs() {
      return this.pairs;
   }

   int getRowNumber() {
      return this.rowNumber;
   }

   public int hashCode() {
      return this.pairs.hashCode() ^ Boolean.valueOf(this.wasReversed).hashCode();
   }

   boolean isEquivalent(List var1) {
      return this.pairs.equals(var1);
   }

   boolean isReversed() {
      return this.wasReversed;
   }

   public String toString() {
      return "{ " + this.pairs + " }";
   }
}
