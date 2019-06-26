package com.google.android.exoplayer2.source;

public interface ShuffleOrder {
   int getFirstIndex();

   int getLastIndex();

   int getLength();

   int getNextIndex(int var1);

   int getPreviousIndex(int var1);

   public static final class UnshuffledShuffleOrder implements ShuffleOrder {
      private final int length;

      public UnshuffledShuffleOrder(int var1) {
         this.length = var1;
      }

      public int getFirstIndex() {
         byte var1;
         if (this.length > 0) {
            var1 = 0;
         } else {
            var1 = -1;
         }

         return var1;
      }

      public int getLastIndex() {
         int var1 = this.length;
         if (var1 > 0) {
            --var1;
         } else {
            var1 = -1;
         }

         return var1;
      }

      public int getLength() {
         return this.length;
      }

      public int getNextIndex(int var1) {
         ++var1;
         if (var1 >= this.length) {
            var1 = -1;
         }

         return var1;
      }

      public int getPreviousIndex(int var1) {
         --var1;
         if (var1 < 0) {
            var1 = -1;
         }

         return var1;
      }
   }
}
