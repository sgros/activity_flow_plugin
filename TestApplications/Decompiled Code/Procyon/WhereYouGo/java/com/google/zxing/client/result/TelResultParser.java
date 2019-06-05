// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class TelResultParser extends ResultParser
{
    @Override
    public TelParsedResult parse(final Result result) {
        final TelParsedResult telParsedResult = null;
        final String massagedText = ResultParser.getMassagedText(result);
        TelParsedResult telParsedResult2;
        if (!massagedText.startsWith("tel:") && !massagedText.startsWith("TEL:")) {
            telParsedResult2 = telParsedResult;
        }
        else {
            String string;
            if (massagedText.startsWith("TEL:")) {
                string = "tel:" + massagedText.substring(4);
            }
            else {
                string = massagedText;
            }
            final int index = massagedText.indexOf(63, 4);
            String s;
            if (index < 0) {
                s = massagedText.substring(4);
            }
            else {
                s = massagedText.substring(4, index);
            }
            telParsedResult2 = new TelParsedResult(s, string, null);
        }
        return telParsedResult2;
    }
}
