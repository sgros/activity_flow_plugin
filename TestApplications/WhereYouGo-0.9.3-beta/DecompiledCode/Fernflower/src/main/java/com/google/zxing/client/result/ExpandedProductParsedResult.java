package com.google.zxing.client.result;

import java.util.Map;

public final class ExpandedProductParsedResult extends ParsedResult {
   public static final String KILOGRAM = "KG";
   public static final String POUND = "LB";
   private final String bestBeforeDate;
   private final String expirationDate;
   private final String lotNumber;
   private final String packagingDate;
   private final String price;
   private final String priceCurrency;
   private final String priceIncrement;
   private final String productID;
   private final String productionDate;
   private final String rawText;
   private final String sscc;
   private final Map uncommonAIs;
   private final String weight;
   private final String weightIncrement;
   private final String weightType;

   public ExpandedProductParsedResult(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, Map var15) {
      super(ParsedResultType.PRODUCT);
      this.rawText = var1;
      this.productID = var2;
      this.sscc = var3;
      this.lotNumber = var4;
      this.productionDate = var5;
      this.packagingDate = var6;
      this.bestBeforeDate = var7;
      this.expirationDate = var8;
      this.weight = var9;
      this.weightType = var10;
      this.weightIncrement = var11;
      this.price = var12;
      this.priceIncrement = var13;
      this.priceCurrency = var14;
      this.uncommonAIs = var15;
   }

   private static boolean equalsOrNull(Object var0, Object var1) {
      boolean var2;
      if (var0 == null) {
         if (var1 == null) {
            var2 = true;
         } else {
            var2 = false;
         }
      } else {
         var2 = var0.equals(var1);
      }

      return var2;
   }

   private static int hashNotNull(Object var0) {
      int var1;
      if (var0 == null) {
         var1 = 0;
      } else {
         var1 = var0.hashCode();
      }

      return var1;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof ExpandedProductParsedResult)) {
         var3 = var2;
      } else {
         ExpandedProductParsedResult var4 = (ExpandedProductParsedResult)var1;
         var3 = var2;
         if (equalsOrNull(this.productID, var4.productID)) {
            var3 = var2;
            if (equalsOrNull(this.sscc, var4.sscc)) {
               var3 = var2;
               if (equalsOrNull(this.lotNumber, var4.lotNumber)) {
                  var3 = var2;
                  if (equalsOrNull(this.productionDate, var4.productionDate)) {
                     var3 = var2;
                     if (equalsOrNull(this.bestBeforeDate, var4.bestBeforeDate)) {
                        var3 = var2;
                        if (equalsOrNull(this.expirationDate, var4.expirationDate)) {
                           var3 = var2;
                           if (equalsOrNull(this.weight, var4.weight)) {
                              var3 = var2;
                              if (equalsOrNull(this.weightType, var4.weightType)) {
                                 var3 = var2;
                                 if (equalsOrNull(this.weightIncrement, var4.weightIncrement)) {
                                    var3 = var2;
                                    if (equalsOrNull(this.price, var4.price)) {
                                       var3 = var2;
                                       if (equalsOrNull(this.priceIncrement, var4.priceIncrement)) {
                                          var3 = var2;
                                          if (equalsOrNull(this.priceCurrency, var4.priceCurrency)) {
                                             var3 = var2;
                                             if (equalsOrNull(this.uncommonAIs, var4.uncommonAIs)) {
                                                var3 = true;
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   public String getBestBeforeDate() {
      return this.bestBeforeDate;
   }

   public String getDisplayResult() {
      return String.valueOf(this.rawText);
   }

   public String getExpirationDate() {
      return this.expirationDate;
   }

   public String getLotNumber() {
      return this.lotNumber;
   }

   public String getPackagingDate() {
      return this.packagingDate;
   }

   public String getPrice() {
      return this.price;
   }

   public String getPriceCurrency() {
      return this.priceCurrency;
   }

   public String getPriceIncrement() {
      return this.priceIncrement;
   }

   public String getProductID() {
      return this.productID;
   }

   public String getProductionDate() {
      return this.productionDate;
   }

   public String getRawText() {
      return this.rawText;
   }

   public String getSscc() {
      return this.sscc;
   }

   public Map getUncommonAIs() {
      return this.uncommonAIs;
   }

   public String getWeight() {
      return this.weight;
   }

   public String getWeightIncrement() {
      return this.weightIncrement;
   }

   public String getWeightType() {
      return this.weightType;
   }

   public int hashCode() {
      return hashNotNull(this.productID) ^ 0 ^ hashNotNull(this.sscc) ^ hashNotNull(this.lotNumber) ^ hashNotNull(this.productionDate) ^ hashNotNull(this.bestBeforeDate) ^ hashNotNull(this.expirationDate) ^ hashNotNull(this.weight) ^ hashNotNull(this.weightType) ^ hashNotNull(this.weightIncrement) ^ hashNotNull(this.price) ^ hashNotNull(this.priceIncrement) ^ hashNotNull(this.priceCurrency) ^ hashNotNull(this.uncommonAIs);
   }
}
