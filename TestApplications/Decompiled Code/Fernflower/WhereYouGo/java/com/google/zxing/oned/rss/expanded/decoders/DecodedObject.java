package com.google.zxing.oned.rss.expanded.decoders;

abstract class DecodedObject {
   private final int newPosition;

   DecodedObject(int var1) {
      this.newPosition = var1;
   }

   final int getNewPosition() {
      return this.newPosition;
   }
}
