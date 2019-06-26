package com.stripe.android.exception;

public class CardException extends StripeException {
   private String charge;
   private String code;
   private String declineCode;
   private String param;

   public CardException(String var1, String var2, String var3, String var4, String var5, String var6, Integer var7, Throwable var8) {
      super(var1, var2, var7, var8);
      this.code = var3;
      this.param = var4;
      this.declineCode = var5;
      this.charge = var6;
   }
}
