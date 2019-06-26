package org.aspectj.lang;

public class NoAspectBoundException extends RuntimeException {
   Throwable cause;

   public NoAspectBoundException(String var1, Throwable var2) {
      if (var2 != null) {
         StringBuffer var3 = new StringBuffer();
         var3.append("Exception while initializing ");
         var3.append(var1);
         var3.append(": ");
         var3.append(var2);
         var1 = var3.toString();
      }

      super(var1);
      this.cause = var2;
   }

   public Throwable getCause() {
      return this.cause;
   }
}
