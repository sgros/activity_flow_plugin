// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class SMSTOMMSTOResultParser extends ResultParser
{
    @Override
    public SMSParsedResult parse(final Result result) {
        final SMSParsedResult smsParsedResult = null;
        final String massagedText = ResultParser.getMassagedText(result);
        SMSParsedResult smsParsedResult2;
        if (!massagedText.startsWith("smsto:") && !massagedText.startsWith("SMSTO:") && !massagedText.startsWith("mmsto:") && !massagedText.startsWith("MMSTO:")) {
            smsParsedResult2 = smsParsedResult;
        }
        else {
            final String substring = massagedText.substring(6);
            String substring2 = null;
            final int index = substring.indexOf(58);
            String substring3 = substring;
            if (index >= 0) {
                substring2 = substring.substring(index + 1);
                substring3 = substring.substring(0, index);
            }
            smsParsedResult2 = new SMSParsedResult(substring3, null, null, substring2);
        }
        return smsParsedResult2;
    }
}
