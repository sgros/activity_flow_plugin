// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.List;

public final class VEventResultParser extends ResultParser
{
    private static String matchSingleVCardPrefixedField(final CharSequence charSequence, final String s, final boolean b) {
        final List<String> matchSingleVCardPrefixedField = VCardResultParser.matchSingleVCardPrefixedField(charSequence, s, b, false);
        String s2;
        if (matchSingleVCardPrefixedField == null || matchSingleVCardPrefixedField.isEmpty()) {
            s2 = null;
        }
        else {
            s2 = matchSingleVCardPrefixedField.get(0);
        }
        return s2;
    }
    
    private static String[] matchVCardPrefixedField(final CharSequence charSequence, final String s, final boolean b) {
        final List<List<String>> matchVCardPrefixedField = VCardResultParser.matchVCardPrefixedField(charSequence, s, b, false);
        String[] array;
        if (matchVCardPrefixedField == null || matchVCardPrefixedField.isEmpty()) {
            array = null;
        }
        else {
            final int size = matchVCardPrefixedField.size();
            final String[] array2 = new String[size];
            int n = 0;
            while (true) {
                array = array2;
                if (n >= size) {
                    break;
                }
                array2[n] = matchVCardPrefixedField.get(n).get(0);
                ++n;
            }
        }
        return array;
    }
    
    private static String stripMailto(final String s) {
        String substring = s;
        if (s != null) {
            if (!s.startsWith("mailto:")) {
                substring = s;
                if (!s.startsWith("MAILTO:")) {
                    return substring;
                }
            }
            substring = s.substring(7);
        }
        return substring;
    }
    
    @Override
    public CalendarParsedResult parse(final Result result) {
        final String massagedText = ResultParser.getMassagedText(result);
        CalendarParsedResult calendarParsedResult;
        if (massagedText.indexOf("BEGIN:VEVENT") < 0) {
            calendarParsedResult = null;
        }
        else {
            final String matchSingleVCardPrefixedField = matchSingleVCardPrefixedField("SUMMARY", massagedText, true);
            final String matchSingleVCardPrefixedField2 = matchSingleVCardPrefixedField("DTSTART", massagedText, true);
            if (matchSingleVCardPrefixedField2 == null) {
                calendarParsedResult = null;
            }
            else {
                final String matchSingleVCardPrefixedField3 = matchSingleVCardPrefixedField("DTEND", massagedText, true);
                final String matchSingleVCardPrefixedField4 = matchSingleVCardPrefixedField("DURATION", massagedText, true);
                final String matchSingleVCardPrefixedField5 = matchSingleVCardPrefixedField("LOCATION", massagedText, true);
                final String stripMailto = stripMailto(matchSingleVCardPrefixedField("ORGANIZER", massagedText, true));
                final String[] matchVCardPrefixedField = matchVCardPrefixedField("ATTENDEE", massagedText, true);
                if (matchVCardPrefixedField != null) {
                    for (int i = 0; i < matchVCardPrefixedField.length; ++i) {
                        matchVCardPrefixedField[i] = stripMailto(matchVCardPrefixedField[i]);
                    }
                }
                final String matchSingleVCardPrefixedField6 = matchSingleVCardPrefixedField("DESCRIPTION", massagedText, true);
                final String matchSingleVCardPrefixedField7 = matchSingleVCardPrefixedField("GEO", massagedText, true);
                double double1;
                double double2;
                if (matchSingleVCardPrefixedField7 == null) {
                    double1 = Double.NaN;
                    double2 = Double.NaN;
                }
                else {
                    final int index = matchSingleVCardPrefixedField7.indexOf(59);
                    if (index < 0) {
                        calendarParsedResult = null;
                        return calendarParsedResult;
                    }
                    try {
                        double1 = Double.parseDouble(matchSingleVCardPrefixedField7.substring(0, index));
                        double2 = Double.parseDouble(matchSingleVCardPrefixedField7.substring(index + 1));
                    }
                    catch (NumberFormatException ex) {
                        calendarParsedResult = null;
                        return calendarParsedResult;
                    }
                }
                try {
                    calendarParsedResult = new CalendarParsedResult(matchSingleVCardPrefixedField, matchSingleVCardPrefixedField2, matchSingleVCardPrefixedField3, matchSingleVCardPrefixedField4, matchSingleVCardPrefixedField5, stripMailto, matchVCardPrefixedField, matchSingleVCardPrefixedField6, double1, double2);
                }
                catch (IllegalArgumentException ex2) {
                    calendarParsedResult = null;
                }
            }
        }
        return calendarParsedResult;
    }
}
