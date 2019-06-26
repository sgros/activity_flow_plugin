package com.google.zxing;

public final class NotFoundException extends ReaderException {
   private static final NotFoundException INSTANCE;

   static {
      NotFoundException var0 = new NotFoundException();
      INSTANCE = var0;
      var0.setStackTrace(NO_TRACE);
   }

   private NotFoundException() {
   }

   public static NotFoundException getNotFoundInstance() {
      return INSTANCE;
   }
}
