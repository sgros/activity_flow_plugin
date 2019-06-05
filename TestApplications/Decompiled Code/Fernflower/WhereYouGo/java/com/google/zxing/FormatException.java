package com.google.zxing;

public final class FormatException extends ReaderException {
   private static final FormatException INSTANCE;

   static {
      FormatException var0 = new FormatException();
      INSTANCE = var0;
      var0.setStackTrace(NO_TRACE);
   }

   private FormatException() {
   }

   private FormatException(Throwable var1) {
      super(var1);
   }

   public static FormatException getFormatInstance() {
      FormatException var0;
      if (isStackTrace) {
         var0 = new FormatException();
      } else {
         var0 = INSTANCE;
      }

      return var0;
   }

   public static FormatException getFormatInstance(Throwable var0) {
      FormatException var1;
      if (isStackTrace) {
         var1 = new FormatException(var0);
      } else {
         var1 = INSTANCE;
      }

      return var1;
   }
}
