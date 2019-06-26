package com.google.zxing.oned.rss.expanded.decoders;

final class CurrentParsingState {
   private CurrentParsingState.State encoding;
   private int position = 0;

   CurrentParsingState() {
      this.encoding = CurrentParsingState.State.NUMERIC;
   }

   int getPosition() {
      return this.position;
   }

   void incrementPosition(int var1) {
      this.position += var1;
   }

   boolean isAlpha() {
      boolean var1;
      if (this.encoding == CurrentParsingState.State.ALPHA) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean isIsoIec646() {
      boolean var1;
      if (this.encoding == CurrentParsingState.State.ISO_IEC_646) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean isNumeric() {
      boolean var1;
      if (this.encoding == CurrentParsingState.State.NUMERIC) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   void setAlpha() {
      this.encoding = CurrentParsingState.State.ALPHA;
   }

   void setIsoIec646() {
      this.encoding = CurrentParsingState.State.ISO_IEC_646;
   }

   void setNumeric() {
      this.encoding = CurrentParsingState.State.NUMERIC;
   }

   void setPosition(int var1) {
      this.position = var1;
   }

   private static enum State {
      ALPHA,
      ISO_IEC_646,
      NUMERIC;
   }
}
