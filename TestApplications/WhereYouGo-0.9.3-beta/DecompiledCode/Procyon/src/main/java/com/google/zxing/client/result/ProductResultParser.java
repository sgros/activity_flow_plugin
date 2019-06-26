// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.oned.UPCEReader;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public final class ProductResultParser extends ResultParser
{
    @Override
    public ProductParsedResult parse(final Result result) {
        final ProductParsedResult productParsedResult = null;
        final BarcodeFormat barcodeFormat = result.getBarcodeFormat();
        ProductParsedResult productParsedResult2;
        if (barcodeFormat != BarcodeFormat.UPC_A && barcodeFormat != BarcodeFormat.UPC_E && barcodeFormat != BarcodeFormat.EAN_8 && barcodeFormat != BarcodeFormat.EAN_13) {
            productParsedResult2 = productParsedResult;
        }
        else {
            final String massagedText = ResultParser.getMassagedText(result);
            productParsedResult2 = productParsedResult;
            if (ResultParser.isStringOfDigits(massagedText, massagedText.length())) {
                String convertUPCEtoUPCA;
                if (barcodeFormat == BarcodeFormat.UPC_E && massagedText.length() == 8) {
                    convertUPCEtoUPCA = UPCEReader.convertUPCEtoUPCA(massagedText);
                }
                else {
                    convertUPCEtoUPCA = massagedText;
                }
                productParsedResult2 = new ProductParsedResult(massagedText, convertUPCEtoUPCA);
            }
        }
        return productParsedResult2;
    }
}
