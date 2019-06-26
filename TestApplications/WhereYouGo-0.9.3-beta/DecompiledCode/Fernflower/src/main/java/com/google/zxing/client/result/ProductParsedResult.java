package com.google.zxing.client.result;

public final class ProductParsedResult extends ParsedResult {
   private final String normalizedProductID;
   private final String productID;

   ProductParsedResult(String var1) {
      this(var1, var1);
   }

   ProductParsedResult(String var1, String var2) {
      super(ParsedResultType.PRODUCT);
      this.productID = var1;
      this.normalizedProductID = var2;
   }

   public String getDisplayResult() {
      return this.productID;
   }

   public String getNormalizedProductID() {
      return this.normalizedProductID;
   }

   public String getProductID() {
      return this.productID;
   }
}
