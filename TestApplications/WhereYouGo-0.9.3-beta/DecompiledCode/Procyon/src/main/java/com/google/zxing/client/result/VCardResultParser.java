// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.Collection;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

public final class VCardResultParser extends ResultParser
{
    private static final Pattern BEGIN_VCARD;
    private static final Pattern COMMA;
    private static final Pattern CR_LF_SPACE_TAB;
    private static final Pattern EQUALS;
    private static final Pattern NEWLINE_ESCAPE;
    private static final Pattern SEMICOLON;
    private static final Pattern SEMICOLON_OR_COMMA;
    private static final Pattern UNESCAPED_SEMICOLONS;
    private static final Pattern VCARD_ESCAPES;
    private static final Pattern VCARD_LIKE_DATE;
    
    static {
        BEGIN_VCARD = Pattern.compile("BEGIN:VCARD", 2);
        VCARD_LIKE_DATE = Pattern.compile("\\d{4}-?\\d{2}-?\\d{2}");
        CR_LF_SPACE_TAB = Pattern.compile("\r\n[ \t]");
        NEWLINE_ESCAPE = Pattern.compile("\\\\[nN]");
        VCARD_ESCAPES = Pattern.compile("\\\\([,;\\\\])");
        EQUALS = Pattern.compile("=");
        SEMICOLON = Pattern.compile(";");
        UNESCAPED_SEMICOLONS = Pattern.compile("(?<!\\\\);+");
        COMMA = Pattern.compile(",");
        SEMICOLON_OR_COMMA = Pattern.compile("[;,]");
    }
    
    private static String decodeQuotedPrintable(final CharSequence charSequence, final String s) {
        final int length = charSequence.length();
        final StringBuilder sb = new StringBuilder(length);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
    Label_0102_Outer:
        while (i < length) {
            final char char1 = charSequence.charAt(i);
            int n = i;
            while (true) {
                switch (char1) {
                    default: {
                        maybeAppendFragment(byteArrayOutputStream, s, sb);
                        sb.append(char1);
                        n = i;
                    }
                    case 10:
                    case 13: {
                        i = n + 1;
                        continue Label_0102_Outer;
                    }
                    case 61: {
                        n = i;
                        if (i >= length - 2) {
                            continue;
                        }
                        final char char2 = charSequence.charAt(i + 1);
                        n = i;
                        if (char2 == '\r') {
                            continue;
                        }
                        n = i;
                        if (char2 != '\n') {
                            final char char3 = charSequence.charAt(i + 2);
                            final int hexDigit = ResultParser.parseHexDigit(char2);
                            final int hexDigit2 = ResultParser.parseHexDigit(char3);
                            if (hexDigit >= 0 && hexDigit2 >= 0) {
                                byteArrayOutputStream.write((hexDigit << 4) + hexDigit2);
                            }
                            n = i + 2;
                        }
                        continue;
                    }
                }
                break;
            }
        }
        maybeAppendFragment(byteArrayOutputStream, s, sb);
        return sb.toString();
    }
    
    private static void formatNames(final Iterable<List<String>> iterable) {
        if (iterable != null) {
            for (final List<String> list : iterable) {
                final String s = list.get(0);
                final String[] array = new String[5];
                int beginIndex = 0;
                int i;
                int index;
                for (i = 0; i < 4; ++i, beginIndex = index + 1) {
                    index = s.indexOf(59, beginIndex);
                    if (index < 0) {
                        break;
                    }
                    array[i] = s.substring(beginIndex, index);
                }
                array[i] = s.substring(beginIndex);
                final StringBuilder sb = new StringBuilder(100);
                maybeAppendComponent(array, 3, sb);
                maybeAppendComponent(array, 1, sb);
                maybeAppendComponent(array, 2, sb);
                maybeAppendComponent(array, 0, sb);
                maybeAppendComponent(array, 4, sb);
                list.set(0, sb.toString().trim());
            }
        }
    }
    
    private static boolean isLikeVCardDate(final CharSequence input) {
        return input == null || VCardResultParser.VCARD_LIKE_DATE.matcher(input).matches();
    }
    
    static List<String> matchSingleVCardPrefixedField(final CharSequence charSequence, final String s, final boolean b, final boolean b2) {
        final List<List<String>> matchVCardPrefixedField = matchVCardPrefixedField(charSequence, s, b, b2);
        List<String> list;
        if (matchVCardPrefixedField == null || matchVCardPrefixedField.isEmpty()) {
            list = null;
        }
        else {
            list = matchVCardPrefixedField.get(0);
        }
        return list;
    }
    
    static List<List<String>> matchVCardPrefixedField(final CharSequence obj, final String input, final boolean b, final boolean b2) {
        List<ArrayList<String>> list = null;
        int i = 0;
        final int length = input.length();
        while (i < length) {
            final Matcher matcher = Pattern.compile("(?:^|\n)" + (Object)obj + "(?:;([^:]*))?:", 2).matcher(input);
            int start;
            if ((start = i) > 0) {
                start = i - 1;
            }
            if (!matcher.find(start)) {
                break;
            }
            final int end = matcher.end(0);
            final String group = matcher.group(1);
            ArrayList<String> list2 = null;
            ArrayList<String> list3 = null;
            int n = 0;
            int n2 = 0;
            String s = null;
            String s2 = null;
            if (group != null) {
                final String[] split = VCardResultParser.SEMICOLON.split(group);
                final int length2 = split.length;
                int n3 = 0;
                while (true) {
                    list2 = list3;
                    n = n2;
                    s = s2;
                    if (n3 >= length2) {
                        break;
                    }
                    final String input2 = split[n3];
                    ArrayList<String> list4;
                    if ((list4 = list3) == null) {
                        list4 = new ArrayList<String>(1);
                    }
                    list4.add(input2);
                    final String[] split2 = VCardResultParser.EQUALS.split(input2, 2);
                    int n4 = n2;
                    String s3 = s2;
                    if (split2.length > 1) {
                        final String s4 = split2[0];
                        final String anotherString = split2[1];
                        if ("ENCODING".equalsIgnoreCase(s4) && "QUOTED-PRINTABLE".equalsIgnoreCase(anotherString)) {
                            n4 = 1;
                            s3 = s2;
                        }
                        else {
                            n4 = n2;
                            s3 = s2;
                            if ("CHARSET".equalsIgnoreCase(s4)) {
                                s3 = anotherString;
                                n4 = n2;
                            }
                        }
                    }
                    ++n3;
                    list3 = list4;
                    n2 = n4;
                    s2 = s3;
                }
            }
            int fromIndex = end;
            int index;
            while (true) {
                index = input.indexOf(10, fromIndex);
                if (index < 0) {
                    break;
                }
                if (index < input.length() - 1 && (input.charAt(index + 1) == ' ' || input.charAt(index + 1) == '\t')) {
                    fromIndex = index + 2;
                }
                else {
                    if (n == 0 || ((index <= 0 || input.charAt(index - 1) != '=') && (index < 2 || input.charAt(index - 2) != '='))) {
                        break;
                    }
                    fromIndex = index + 1;
                }
            }
            if (index < 0) {
                i = length;
            }
            else if (index > end) {
                List<ArrayList<String>> list5;
                if ((list5 = list) == null) {
                    list5 = new ArrayList<ArrayList<String>>(1);
                }
                if ((i = index) > 0) {
                    i = index;
                    if (input.charAt(index - 1) == '\r') {
                        i = index - 1;
                    }
                }
                String input3;
                final String s5 = input3 = input.substring(end, i);
                if (b) {
                    input3 = s5.trim();
                }
                String s6;
                if (n != 0) {
                    final String input4 = s6 = decodeQuotedPrintable(input3, s);
                    if (b2) {
                        s6 = VCardResultParser.UNESCAPED_SEMICOLONS.matcher(input4).replaceAll("\n").trim();
                    }
                }
                else {
                    String trim = input3;
                    if (b2) {
                        trim = VCardResultParser.UNESCAPED_SEMICOLONS.matcher(input3).replaceAll("\n").trim();
                    }
                    s6 = VCardResultParser.VCARD_ESCAPES.matcher(VCardResultParser.NEWLINE_ESCAPE.matcher(VCardResultParser.CR_LF_SPACE_TAB.matcher(trim).replaceAll("")).replaceAll("\n")).replaceAll("$1");
                }
                if (list2 == null) {
                    final ArrayList<String> list6 = new ArrayList<String>(1);
                    list6.add(s6);
                    list5.add(list6);
                }
                else {
                    list2.add(0, s6);
                    list5.add(list2);
                }
                ++i;
                list = list5;
            }
            else {
                i = index + 1;
            }
        }
        return (List<List<String>>)list;
    }
    
    private static void maybeAppendComponent(final String[] array, final int n, final StringBuilder sb) {
        if (array[n] != null && !array[n].isEmpty()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(array[n]);
        }
    }
    
    private static void maybeAppendFragment(final ByteArrayOutputStream byteArrayOutputStream, String o, final StringBuilder sb) {
        if (byteArrayOutputStream.size() > 0) {
            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (o == null) {
                o = new String(byteArray, Charset.forName("UTF-8"));
            }
            else {
                try {
                    o = new String(byteArray, (String)o);
                }
                catch (UnsupportedEncodingException ex) {
                    o = new String(byteArray, Charset.forName("UTF-8"));
                }
            }
            byteArrayOutputStream.reset();
            sb.append((String)o);
        }
    }
    
    private static String toPrimaryValue(final List<String> list) {
        String s;
        if (list == null || list.isEmpty()) {
            s = null;
        }
        else {
            s = list.get(0);
        }
        return s;
    }
    
    private static String[] toPrimaryValues(final Collection<List<String>> collection) {
        String[] array;
        if (collection == null || collection.isEmpty()) {
            array = null;
        }
        else {
            final ArrayList<String> list = new ArrayList<String>(collection.size());
            final Iterator<List<String>> iterator = collection.iterator();
            while (iterator.hasNext()) {
                final String s = iterator.next().get(0);
                if (s != null && !s.isEmpty()) {
                    list.add(s);
                }
            }
            array = list.toArray(new String[collection.size()]);
        }
        return array;
    }
    
    private static String[] toTypes(final Collection<List<String>> collection) {
        String[] array;
        if (collection == null || collection.isEmpty()) {
            array = null;
        }
        else {
            final ArrayList<String> list = new ArrayList<String>(collection.size());
        Label_0105_Outer:
            for (final List<String> list2 : collection) {
                final String s = null;
                int n = 1;
                while (true) {
                    String substring = null;
                    int index = 0;
                    Block_4: {
                        while (true) {
                            substring = s;
                            if (n >= list2.size()) {
                                break;
                            }
                            substring = list2.get(n);
                            index = substring.indexOf(61);
                            if (index < 0) {
                                break;
                            }
                            if ("TYPE".equalsIgnoreCase(substring.substring(0, index))) {
                                break Block_4;
                            }
                            ++n;
                        }
                        list.add(substring);
                        continue Label_0105_Outer;
                    }
                    substring = substring.substring(index + 1);
                    continue;
                }
            }
            array = list.toArray(new String[collection.size()]);
        }
        return array;
    }
    
    @Override
    public AddressBookParsedResult parse(final Result result) {
        final String massagedText = ResultParser.getMassagedText(result);
        final Matcher matcher = VCardResultParser.BEGIN_VCARD.matcher(massagedText);
        AddressBookParsedResult addressBookParsedResult;
        if (!matcher.find() || matcher.start() != 0) {
            addressBookParsedResult = null;
        }
        else {
            List<List<String>> list;
            if ((list = matchVCardPrefixedField("FN", massagedText, true, false)) == null) {
                list = matchVCardPrefixedField("N", massagedText, true, false);
                formatNames(list);
            }
            final List<String> matchSingleVCardPrefixedField = matchSingleVCardPrefixedField("NICKNAME", massagedText, true, false);
            String[] split;
            if (matchSingleVCardPrefixedField == null) {
                split = null;
            }
            else {
                split = VCardResultParser.COMMA.split(matchSingleVCardPrefixedField.get(0));
            }
            final List<List<String>> matchVCardPrefixedField = matchVCardPrefixedField("TEL", massagedText, true, false);
            final List<List<String>> matchVCardPrefixedField2 = matchVCardPrefixedField("EMAIL", massagedText, true, false);
            final List<String> matchSingleVCardPrefixedField2 = matchSingleVCardPrefixedField("NOTE", massagedText, false, false);
            final List<List<String>> matchVCardPrefixedField3 = matchVCardPrefixedField("ADR", massagedText, true, true);
            final List<String> matchSingleVCardPrefixedField3 = matchSingleVCardPrefixedField("ORG", massagedText, true, true);
            List<String> matchSingleVCardPrefixedField4;
            final List<String> list2 = matchSingleVCardPrefixedField4 = matchSingleVCardPrefixedField("BDAY", massagedText, (boolean)(1 != 0), (boolean)(0 != 0));
            if (list2 != null) {
                matchSingleVCardPrefixedField4 = list2;
                if (!isLikeVCardDate(list2.get(0))) {
                    matchSingleVCardPrefixedField4 = null;
                }
            }
            final List<String> matchSingleVCardPrefixedField5 = matchSingleVCardPrefixedField("TITLE", massagedText, true, false);
            final List<List<String>> matchVCardPrefixedField4 = matchVCardPrefixedField("URL", massagedText, true, false);
            final List<String> matchSingleVCardPrefixedField6 = matchSingleVCardPrefixedField("IMPP", massagedText, true, false);
            final List<String> matchSingleVCardPrefixedField7 = matchSingleVCardPrefixedField("GEO", massagedText, true, false);
            String[] split2;
            if (matchSingleVCardPrefixedField7 == null) {
                split2 = null;
            }
            else {
                split2 = VCardResultParser.SEMICOLON_OR_COMMA.split(matchSingleVCardPrefixedField7.get(0));
            }
            String[] array = split2;
            if (split2 != null) {
                array = split2;
                if (split2.length != 2) {
                    array = null;
                }
            }
            addressBookParsedResult = new AddressBookParsedResult(toPrimaryValues(list), split, null, toPrimaryValues(matchVCardPrefixedField), toTypes(matchVCardPrefixedField), toPrimaryValues(matchVCardPrefixedField2), toTypes(matchVCardPrefixedField2), toPrimaryValue(matchSingleVCardPrefixedField6), toPrimaryValue(matchSingleVCardPrefixedField2), toPrimaryValues(matchVCardPrefixedField3), toTypes(matchVCardPrefixedField3), toPrimaryValue(matchSingleVCardPrefixedField3), toPrimaryValue(matchSingleVCardPrefixedField4), toPrimaryValue(matchSingleVCardPrefixedField5), toPrimaryValues(matchVCardPrefixedField4), array);
        }
        return addressBookParsedResult;
    }
}
