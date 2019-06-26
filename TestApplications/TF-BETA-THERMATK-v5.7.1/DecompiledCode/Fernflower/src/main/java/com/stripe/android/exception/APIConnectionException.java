package com.stripe.android.exception;

public class APIConnectionException extends StripeException {
   public APIConnectionException(String var1) {
      super(var1, (String)null, 0);
   }

   public APIConnectionException(String var1, Throwable var2) {
      super(var1, (String)null, 0, var2);
   }
}
