package com.google.zxing;

public abstract class ReaderException extends Exception {
   protected static final StackTraceElement[] NO_TRACE;
   protected static final boolean isStackTrace;

   static {
      boolean var0;
      if (System.getProperty("surefire.test.class.path") != null) {
         var0 = true;
      } else {
         var0 = false;
      }

      isStackTrace = var0;
      NO_TRACE = new StackTraceElement[0];
   }

   ReaderException() {
   }

   ReaderException(Throwable var1) {
      super(var1);
   }

   public final Throwable fillInStackTrace() {
      synchronized(this){}
      return null;
   }
}
