package com.stripe.android.net;

public class RequestOptions {
   private final String mApiVersion;
   private final String mIdempotencyKey;
   private final String mPublishableApiKey;

   private RequestOptions(String var1, String var2, String var3) {
      this.mApiVersion = var1;
      this.mIdempotencyKey = var2;
      this.mPublishableApiKey = var3;
   }

   // $FF: synthetic method
   RequestOptions(String var1, String var2, String var3, Object var4) {
      this(var1, var2, var3);
   }

   public static RequestOptions.RequestOptionsBuilder builder(String var0) {
      return new RequestOptions.RequestOptionsBuilder(var0);
   }

   public String getApiVersion() {
      return this.mApiVersion;
   }

   public String getIdempotencyKey() {
      return this.mIdempotencyKey;
   }

   public String getPublishableApiKey() {
      return this.mPublishableApiKey;
   }

   public static final class RequestOptionsBuilder {
      private String apiVersion;
      private String idempotencyKey;
      private String publishableApiKey;

      public RequestOptionsBuilder(String var1) {
         this.publishableApiKey = var1;
      }

      public RequestOptions build() {
         return new RequestOptions(this.apiVersion, this.idempotencyKey, this.publishableApiKey);
      }
   }
}
