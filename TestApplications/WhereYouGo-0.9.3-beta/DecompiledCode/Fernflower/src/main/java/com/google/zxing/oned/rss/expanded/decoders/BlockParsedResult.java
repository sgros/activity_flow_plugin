package com.google.zxing.oned.rss.expanded.decoders;

final class BlockParsedResult {
   private final DecodedInformation decodedInformation;
   private final boolean finished;

   BlockParsedResult(DecodedInformation var1, boolean var2) {
      this.finished = var2;
      this.decodedInformation = var1;
   }

   BlockParsedResult(boolean var1) {
      this((DecodedInformation)null, var1);
   }

   DecodedInformation getDecodedInformation() {
      return this.decodedInformation;
   }

   boolean isFinished() {
      return this.finished;
   }
}
