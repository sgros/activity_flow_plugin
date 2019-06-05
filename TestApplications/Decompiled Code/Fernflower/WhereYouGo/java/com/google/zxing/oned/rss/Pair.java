package com.google.zxing.oned.rss;

final class Pair extends DataCharacter {
   private int count;
   private final FinderPattern finderPattern;

   Pair(int var1, int var2, FinderPattern var3) {
      super(var1, var2);
      this.finderPattern = var3;
   }

   int getCount() {
      return this.count;
   }

   FinderPattern getFinderPattern() {
      return this.finderPattern;
   }

   void incrementCount() {
      ++this.count;
   }
}
