// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.regex.Pattern;

public final class EmailDoCoMoResultParser extends AbstractDoCoMoResultParser
{
    private static final Pattern ATEXT_ALPHANUMERIC;
    
    static {
        ATEXT_ALPHANUMERIC = Pattern.compile("[a-zA-Z0-9@.!#$%&'*+\\-/=?^_`{|}~]+");
    }
    
    static boolean isBasicallyValidEmailAddress(final String input) {
        return input != null && EmailDoCoMoResultParser.ATEXT_ALPHANUMERIC.matcher(input).matches() && input.indexOf(64) >= 0;
    }
    
    @Override
    public EmailAddressParsedResult parse(final Result result) {
        final EmailAddressParsedResult emailAddressParsedResult = null;
        final String massagedText = ResultParser.getMassagedText(result);
        EmailAddressParsedResult emailAddressParsedResult2;
        if (!massagedText.startsWith("MATMSG:")) {
            emailAddressParsedResult2 = emailAddressParsedResult;
        }
        else {
            final String[] matchDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("TO:", massagedText, true);
            emailAddressParsedResult2 = emailAddressParsedResult;
            if (matchDoCoMoPrefixedField != null) {
                for (int length = matchDoCoMoPrefixedField.length, i = 0; i < length; ++i) {
                    emailAddressParsedResult2 = emailAddressParsedResult;
                    if (!isBasicallyValidEmailAddress(matchDoCoMoPrefixedField[i])) {
                        return emailAddressParsedResult2;
                    }
                }
                emailAddressParsedResult2 = new EmailAddressParsedResult(matchDoCoMoPrefixedField, null, null, AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("SUB:", massagedText, false), AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("BODY:", massagedText, false));
            }
        }
        return emailAddressParsedResult2;
    }
}
