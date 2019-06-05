// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import java.util.Map;

public final class ExpandedProductParsedResult extends ParsedResult
{
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
    private final Map<String, String> uncommonAIs;
    private final String weight;
    private final String weightIncrement;
    private final String weightType;
    
    public ExpandedProductParsedResult(final String rawText, final String productID, final String sscc, final String lotNumber, final String productionDate, final String packagingDate, final String bestBeforeDate, final String expirationDate, final String weight, final String weightType, final String weightIncrement, final String price, final String priceIncrement, final String priceCurrency, final Map<String, String> uncommonAIs) {
        super(ParsedResultType.PRODUCT);
        this.rawText = rawText;
        this.productID = productID;
        this.sscc = sscc;
        this.lotNumber = lotNumber;
        this.productionDate = productionDate;
        this.packagingDate = packagingDate;
        this.bestBeforeDate = bestBeforeDate;
        this.expirationDate = expirationDate;
        this.weight = weight;
        this.weightType = weightType;
        this.weightIncrement = weightIncrement;
        this.price = price;
        this.priceIncrement = priceIncrement;
        this.priceCurrency = priceCurrency;
        this.uncommonAIs = uncommonAIs;
    }
    
    private static boolean equalsOrNull(final Object o, final Object obj) {
        boolean equals;
        if (o == null) {
            equals = (obj == null);
        }
        else {
            equals = o.equals(obj);
        }
        return equals;
    }
    
    private static int hashNotNull(final Object o) {
        int hashCode;
        if (o == null) {
            hashCode = 0;
        }
        else {
            hashCode = o.hashCode();
        }
        return hashCode;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (!(o instanceof ExpandedProductParsedResult)) {
            b2 = b;
        }
        else {
            final ExpandedProductParsedResult expandedProductParsedResult = (ExpandedProductParsedResult)o;
            b2 = b;
            if (equalsOrNull(this.productID, expandedProductParsedResult.productID)) {
                b2 = b;
                if (equalsOrNull(this.sscc, expandedProductParsedResult.sscc)) {
                    b2 = b;
                    if (equalsOrNull(this.lotNumber, expandedProductParsedResult.lotNumber)) {
                        b2 = b;
                        if (equalsOrNull(this.productionDate, expandedProductParsedResult.productionDate)) {
                            b2 = b;
                            if (equalsOrNull(this.bestBeforeDate, expandedProductParsedResult.bestBeforeDate)) {
                                b2 = b;
                                if (equalsOrNull(this.expirationDate, expandedProductParsedResult.expirationDate)) {
                                    b2 = b;
                                    if (equalsOrNull(this.weight, expandedProductParsedResult.weight)) {
                                        b2 = b;
                                        if (equalsOrNull(this.weightType, expandedProductParsedResult.weightType)) {
                                            b2 = b;
                                            if (equalsOrNull(this.weightIncrement, expandedProductParsedResult.weightIncrement)) {
                                                b2 = b;
                                                if (equalsOrNull(this.price, expandedProductParsedResult.price)) {
                                                    b2 = b;
                                                    if (equalsOrNull(this.priceIncrement, expandedProductParsedResult.priceIncrement)) {
                                                        b2 = b;
                                                        if (equalsOrNull(this.priceCurrency, expandedProductParsedResult.priceCurrency)) {
                                                            b2 = b;
                                                            if (equalsOrNull(this.uncommonAIs, expandedProductParsedResult.uncommonAIs)) {
                                                                b2 = true;
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
        return b2;
    }
    
    public String getBestBeforeDate() {
        return this.bestBeforeDate;
    }
    
    @Override
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
    
    public Map<String, String> getUncommonAIs() {
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
    
    @Override
    public int hashCode() {
        return hashNotNull(this.productID) ^ 0x0 ^ hashNotNull(this.sscc) ^ hashNotNull(this.lotNumber) ^ hashNotNull(this.productionDate) ^ hashNotNull(this.bestBeforeDate) ^ hashNotNull(this.expirationDate) ^ hashNotNull(this.weight) ^ hashNotNull(this.weightType) ^ hashNotNull(this.weightIncrement) ^ hashNotNull(this.price) ^ hashNotNull(this.priceIncrement) ^ hashNotNull(this.priceCurrency) ^ hashNotNull(this.uncommonAIs);
    }
}
