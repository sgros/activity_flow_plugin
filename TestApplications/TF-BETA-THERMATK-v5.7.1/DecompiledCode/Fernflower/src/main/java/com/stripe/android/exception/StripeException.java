package com.stripe.android.exception;

public abstract class StripeException extends Exception {
   private String requestId;
   private Integer statusCode;

   public StripeException(String var1, String var2, Integer var3) {
      super(var1, (Throwable)null);
      this.requestId = var2;
      this.statusCode = var3;
   }

   public StripeException(String var1, String var2, Integer var3, Throwable var4) {
      super(var1, var4);
      this.statusCode = var3;
      this.requestId = var2;
   }

   public String toString() {
      String var3;
      if (this.requestId != null) {
         StringBuilder var1 = new StringBuilder();
         var1.append("; request-id: ");
         var1.append(this.requestId);
         var3 = var1.toString();
      } else {
         var3 = "";
      }

      StringBuilder var2 = new StringBuilder();
      var2.append(super.toString());
      var2.append(var3);
      return var2.toString();
   }
}
