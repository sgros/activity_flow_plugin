// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class AddressBookDoCoMoResultParser extends AbstractDoCoMoResultParser
{
    private static String parseName(final String s) {
        final int index = s.indexOf(44);
        String string = s;
        if (index >= 0) {
            string = s.substring(index + 1) + ' ' + s.substring(0, index);
        }
        return string;
    }
    
    @Override
    public AddressBookParsedResult parse(final Result result) {
        final String massagedText = ResultParser.getMassagedText(result);
        AddressBookParsedResult addressBookParsedResult;
        if (!massagedText.startsWith("MECARD:")) {
            addressBookParsedResult = null;
        }
        else {
            final String[] matchDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("N:", massagedText, true);
            if (matchDoCoMoPrefixedField == null) {
                addressBookParsedResult = null;
            }
            else {
                final String name = parseName(matchDoCoMoPrefixedField[0]);
                final String matchSingleDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("SOUND:", massagedText, true);
                final String[] matchDoCoMoPrefixedField2 = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("TEL:", massagedText, true);
                final String[] matchDoCoMoPrefixedField3 = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("EMAIL:", massagedText, true);
                final String matchSingleDoCoMoPrefixedField2 = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("NOTE:", massagedText, false);
                final String[] matchDoCoMoPrefixedField4 = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("ADR:", massagedText, true);
                String matchSingleDoCoMoPrefixedField3;
                if (!ResultParser.isStringOfDigits(matchSingleDoCoMoPrefixedField3 = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("BDAY:", massagedText, true), 8)) {
                    matchSingleDoCoMoPrefixedField3 = null;
                }
                addressBookParsedResult = new AddressBookParsedResult(ResultParser.maybeWrap(name), null, matchSingleDoCoMoPrefixedField, matchDoCoMoPrefixedField2, null, matchDoCoMoPrefixedField3, null, null, matchSingleDoCoMoPrefixedField2, matchDoCoMoPrefixedField4, null, AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("ORG:", massagedText, true), matchSingleDoCoMoPrefixedField3, null, AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("URL:", massagedText, true), null);
            }
        }
        return addressBookParsedResult;
    }
}
