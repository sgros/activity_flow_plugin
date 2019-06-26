// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class WifiResultParser extends ResultParser
{
    @Override
    public WifiParsedResult parse(final Result result) {
        final WifiParsedResult wifiParsedResult = null;
        final String massagedText = ResultParser.getMassagedText(result);
        WifiParsedResult wifiParsedResult2;
        if (!massagedText.startsWith("WIFI:")) {
            wifiParsedResult2 = wifiParsedResult;
        }
        else {
            final String matchSinglePrefixedField = ResultParser.matchSinglePrefixedField("S:", massagedText, ';', false);
            wifiParsedResult2 = wifiParsedResult;
            if (matchSinglePrefixedField != null) {
                wifiParsedResult2 = wifiParsedResult;
                if (!matchSinglePrefixedField.isEmpty()) {
                    final String matchSinglePrefixedField2 = ResultParser.matchSinglePrefixedField("P:", massagedText, ';', false);
                    String matchSinglePrefixedField3;
                    if ((matchSinglePrefixedField3 = ResultParser.matchSinglePrefixedField("T:", massagedText, ';', false)) == null) {
                        matchSinglePrefixedField3 = "nopass";
                    }
                    wifiParsedResult2 = new WifiParsedResult(matchSinglePrefixedField3, matchSinglePrefixedField, matchSinglePrefixedField2, Boolean.parseBoolean(ResultParser.matchSinglePrefixedField("H:", massagedText, ';', false)));
                }
            }
        }
        return wifiParsedResult2;
    }
}
