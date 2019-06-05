// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import java.util.Map;
import com.google.zxing.Result;
import java.util.regex.Pattern;

public final class EmailAddressResultParser extends ResultParser
{
    private static final Pattern COMMA;
    
    static {
        COMMA = Pattern.compile(",");
    }
    
    @Override
    public EmailAddressParsedResult parse(final Result result) {
        Object split = null;
        final String massagedText = ResultParser.getMassagedText(result);
        Label_0259: {
            if (!massagedText.startsWith("mailto:") && !massagedText.startsWith("MAILTO:")) {
                break Label_0259;
            }
            final String substring = massagedText.substring(7);
            final int index = substring.indexOf(63);
            String substring2 = substring;
            if (index >= 0) {
                substring2 = substring.substring(0, index);
            }
            while (true) {
                Object o;
                try {
                    final String urlDecode = ResultParser.urlDecode(substring2);
                    Object split2 = null;
                    if (!urlDecode.isEmpty()) {
                        split2 = EmailAddressResultParser.COMMA.split(urlDecode);
                    }
                    final Map<String, String> nameValuePairs = ResultParser.parseNameValuePairs(massagedText);
                    String[] array = null;
                    final String[] array2 = null;
                    String[] split3 = null;
                    final String[] array3 = null;
                    String s = null;
                    String s2 = null;
                    split = split2;
                    if (nameValuePairs != null) {
                        if ((split = split2) == null) {
                            final String input = nameValuePairs.get("to");
                            split = split2;
                            if (input != null) {
                                split = EmailAddressResultParser.COMMA.split(input);
                            }
                        }
                        final String input2 = nameValuePairs.get("cc");
                        String[] split4 = array2;
                        if (input2 != null) {
                            split4 = EmailAddressResultParser.COMMA.split(input2);
                        }
                        final String input3 = nameValuePairs.get("bcc");
                        split3 = array3;
                        if (input3 != null) {
                            split3 = EmailAddressResultParser.COMMA.split(input3);
                        }
                        s = nameValuePairs.get("subject");
                        s2 = nameValuePairs.get("body");
                        array = split4;
                    }
                    o = new EmailAddressParsedResult((String[])split, array, split3, s, s2);
                    Label_0257: {
                        return (EmailAddressParsedResult)o;
                    }
                    o = new EmailAddressParsedResult(massagedText);
                    return (EmailAddressParsedResult)o;
                    o = split;
                    // iftrue(Label_0257:, !EmailDoCoMoResultParser.isBasicallyValidEmailAddress(massagedText))
                    return new EmailAddressParsedResult(massagedText);
                }
                catch (IllegalArgumentException ex) {
                    o = split;
                    return (EmailAddressParsedResult)o;
                }
                return (EmailAddressParsedResult)o;
            }
        }
    }
}
