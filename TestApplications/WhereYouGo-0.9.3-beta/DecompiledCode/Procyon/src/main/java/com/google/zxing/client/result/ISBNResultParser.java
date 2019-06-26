// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public final class ISBNResultParser extends ResultParser
{
    @Override
    public ISBNParsedResult parse(final Result result) {
        final ISBNParsedResult isbnParsedResult = null;
        ISBNParsedResult isbnParsedResult2;
        if (result.getBarcodeFormat() != BarcodeFormat.EAN_13) {
            isbnParsedResult2 = isbnParsedResult;
        }
        else {
            final String massagedText = ResultParser.getMassagedText(result);
            isbnParsedResult2 = isbnParsedResult;
            if (massagedText.length() == 13) {
                if (!massagedText.startsWith("978")) {
                    isbnParsedResult2 = isbnParsedResult;
                    if (!massagedText.startsWith("979")) {
                        return isbnParsedResult2;
                    }
                }
                isbnParsedResult2 = new ISBNParsedResult(massagedText);
            }
        }
        return isbnParsedResult2;
    }
}
