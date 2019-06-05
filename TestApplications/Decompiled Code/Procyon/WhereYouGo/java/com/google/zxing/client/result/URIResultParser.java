// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class URIResultParser extends ResultParser
{
    private static final Pattern URL_WITHOUT_PROTOCOL_PATTERN;
    private static final Pattern URL_WITH_PROTOCOL_PATTERN;
    
    static {
        URL_WITH_PROTOCOL_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9+-.]+:");
        URL_WITHOUT_PROTOCOL_PATTERN = Pattern.compile("([a-zA-Z0-9\\-]+\\.){1,6}[a-zA-Z]{2,}(:\\d{1,5})?(/|\\?|$)");
    }
    
    static boolean isBasicallyValidURI(final String s) {
        final boolean b = false;
        boolean b2;
        if (s.contains(" ")) {
            b2 = b;
        }
        else {
            final Matcher matcher = URIResultParser.URL_WITH_PROTOCOL_PATTERN.matcher(s);
            if (matcher.find() && matcher.start() == 0) {
                b2 = true;
            }
            else {
                final Matcher matcher2 = URIResultParser.URL_WITHOUT_PROTOCOL_PATTERN.matcher(s);
                b2 = b;
                if (matcher2.find()) {
                    b2 = b;
                    if (matcher2.start() == 0) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public URIParsedResult parse(final Result result) {
        final String massagedText = ResultParser.getMassagedText(result);
        URIParsedResult uriParsedResult;
        if (massagedText.startsWith("URL:") || massagedText.startsWith("URI:")) {
            uriParsedResult = new URIParsedResult(massagedText.substring(4).trim(), null);
        }
        else {
            final String trim = massagedText.trim();
            if (isBasicallyValidURI(trim)) {
                uriParsedResult = new URIParsedResult(trim, null);
            }
            else {
                uriParsedResult = null;
            }
        }
        return uriParsedResult;
    }
}
