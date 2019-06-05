package com.google.zxing;

public final class ChecksumException extends ReaderException {
   private static final ChecksumException INSTANCE;

   static {
      ChecksumException var0 = new ChecksumException();
      INSTANCE = var0;
      var0.setStackTrace(NO_TRACE);
   }

   private ChecksumException() {
   }

   private ChecksumException(Throwable var1) {
      super(var1);
   }

   public static ChecksumException getChecksumInstance() {
      ChecksumException var0;
      if (isStackTrace) {
         var0 = new ChecksumException();
      } else {
         var0 = INSTANCE;
      }

      return var0;
   }

   public static ChecksumException getChecksumInstance(Throwable var0) {
      ChecksumException var1;
      if (isStackTrace) {
         var1 = new ChecksumException(var0);
      } else {
         var1 = INSTANCE;
      }

      return var1;
   }
}
