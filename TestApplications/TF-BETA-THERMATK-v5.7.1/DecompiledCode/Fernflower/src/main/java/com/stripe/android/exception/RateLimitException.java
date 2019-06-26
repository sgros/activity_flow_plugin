package com.stripe.android.exception;

public class RateLimitException extends InvalidRequestException {
   public RateLimitException(String var1, String var2, String var3, Integer var4, Throwable var5) {
      super(var1, var2, var3, var4, var5);
   }
}
