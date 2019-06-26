package com.stripe.android.net;

import java.util.Map;

public class StripeResponse {
   private String mResponseBody;
   private int mResponseCode;
   private Map mResponseHeaders;

   public StripeResponse(int var1, String var2, Map var3) {
      this.mResponseCode = var1;
      this.mResponseBody = var2;
      this.mResponseHeaders = var3;
   }

   public String getResponseBody() {
      return this.mResponseBody;
   }

   public int getResponseCode() {
      return this.mResponseCode;
   }

   public Map getResponseHeaders() {
      return this.mResponseHeaders;
   }
}
