// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class BookmarkDoCoMoResultParser extends AbstractDoCoMoResultParser
{
    @Override
    public URIParsedResult parse(final Result result) {
        final URIParsedResult uriParsedResult = null;
        final String text = result.getText();
        URIParsedResult uriParsedResult2;
        if (!text.startsWith("MEBKM:")) {
            uriParsedResult2 = uriParsedResult;
        }
        else {
            final String matchSingleDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("TITLE:", text, true);
            final String[] matchDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("URL:", text, true);
            uriParsedResult2 = uriParsedResult;
            if (matchDoCoMoPrefixedField != null) {
                final String s = matchDoCoMoPrefixedField[0];
                uriParsedResult2 = uriParsedResult;
                if (URIResultParser.isBasicallyValidURI(s)) {
                    uriParsedResult2 = new URIParsedResult(s, matchSingleDoCoMoPrefixedField);
                }
            }
        }
        return uriParsedResult2;
    }
}
