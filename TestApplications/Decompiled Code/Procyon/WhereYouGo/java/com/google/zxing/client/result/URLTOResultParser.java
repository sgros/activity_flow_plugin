// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class URLTOResultParser extends ResultParser
{
    @Override
    public URIParsedResult parse(final Result result) {
        final String s = null;
        final URIParsedResult uriParsedResult = null;
        final String massagedText = ResultParser.getMassagedText(result);
        URIParsedResult uriParsedResult2;
        if (!massagedText.startsWith("urlto:") && !massagedText.startsWith("URLTO:")) {
            uriParsedResult2 = uriParsedResult;
        }
        else {
            final int index = massagedText.indexOf(58, 6);
            uriParsedResult2 = uriParsedResult;
            if (index >= 0) {
                String substring;
                if (index <= 6) {
                    substring = s;
                }
                else {
                    substring = massagedText.substring(6, index);
                }
                uriParsedResult2 = new URIParsedResult(massagedText.substring(index + 1), substring);
            }
        }
        return uriParsedResult2;
    }
}
