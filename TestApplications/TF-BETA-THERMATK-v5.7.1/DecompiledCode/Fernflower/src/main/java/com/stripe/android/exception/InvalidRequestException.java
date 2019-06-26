package com.stripe.android.exception;

public class InvalidRequestException extends StripeException {
   private final String param;

   public InvalidRequestException(String var1, String var2, String var3, Integer var4, Throwable var5) {
      super(var1, var3, var4, var5);
      this.param = var2;
   }
}
