// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.util.regex.Pattern;

public final class VINResultParser extends ResultParser
{
    private static final Pattern AZ09;
    private static final Pattern IOQ;
    
    static {
        IOQ = Pattern.compile("[IOQ]");
        AZ09 = Pattern.compile("[A-Z0-9]{17}");
    }
    
    private static char checkChar(int n) {
        char c;
        if (n < 10) {
            n = (c = (char)(n + 48));
        }
        else {
            if (n != 10) {
                throw new IllegalArgumentException();
            }
            n = (c = 'X');
        }
        return c;
    }
    
    private static boolean checkChecksum(final CharSequence charSequence) {
        int n = 0;
        for (int i = 0; i < charSequence.length(); ++i) {
            n += vinPositionWeight(i + 1) * vinCharValue(charSequence.charAt(i));
        }
        return charSequence.charAt(8) == checkChar(n % 11);
    }
    
    private static String countryCode(final CharSequence charSequence) {
        final char char1 = charSequence.charAt(0);
        final char char2 = charSequence.charAt(1);
        switch (char1) {
            case 49:
            case 52:
            case 53: {
                return "US";
            }
            case 50: {
                return "CA";
            }
            case 51: {
                if (char2 >= 'A' && char2 <= 'W') {
                    return "MX";
                }
                break;
            }
            case 57: {
                if ((char2 >= 'A' && char2 <= 'E') || (char2 >= '3' && char2 <= '9')) {
                    return "BR";
                }
                break;
            }
            case 74: {
                if (char2 >= 'A' && char2 <= 'T') {
                    return "JP";
                }
                break;
            }
            case 75: {
                if (char2 >= 'L' && char2 <= 'R') {
                    return "KO";
                }
                break;
            }
            case 76: {
                return "CN";
            }
            case 77: {
                if (char2 >= 'A' && char2 <= 'E') {
                    return "IN";
                }
                break;
            }
            case 83: {
                if (char2 >= 'A' && char2 <= 'M') {
                    return "UK";
                }
                if (char2 >= 'N' && char2 <= 'T') {
                    return "DE";
                }
                break;
            }
            case 86: {
                if (char2 >= 'F' && char2 <= 'R') {
                    return "FR";
                }
                if (char2 >= 'S' && char2 <= 'W') {
                    return "ES";
                }
                break;
            }
            case 87: {
                return "DE";
            }
            case 88: {
                if (char2 == '0' || (char2 >= '3' && char2 <= '9')) {
                    return "RU";
                }
                break;
            }
            case 90: {
                if (char2 >= 'A' && char2 <= 'R') {
                    return "IT";
                }
                break;
            }
        }
        return null;
    }
    
    private static int modelYear(final char c) {
        int n;
        if (c >= 'E' && c <= 'H') {
            n = c - 'E' + 1984;
        }
        else if (c >= 'J' && c <= 'N') {
            n = c - 'J' + 1988;
        }
        else if (c == 'P') {
            n = 1993;
        }
        else if (c >= 'R' && c <= 'T') {
            n = c - 'R' + 1994;
        }
        else if (c >= 'V' && c <= 'Y') {
            n = c - 'V' + 1997;
        }
        else if (c >= '1' && c <= '9') {
            n = c - '1' + 2001;
        }
        else {
            if (c < 'A' || c > 'D') {
                throw new IllegalArgumentException();
            }
            n = c - 'A' + 2010;
        }
        return n;
    }
    
    private static int vinCharValue(char c) {
        if (c >= 'A' && c <= 'I') {
            c = (char)(c - 'A' + 1);
        }
        else if (c >= 'J' && c <= 'R') {
            c = (char)(c - 'J' + 1);
        }
        else if (c >= 'S' && c <= 'Z') {
            c = (char)(c - 'S' + 2);
        }
        else {
            if (c < '0' || c > '9') {
                throw new IllegalArgumentException();
            }
            c -= 48;
        }
        return c;
    }
    
    private static int vinPositionWeight(final int n) {
        int n2 = 10;
        if (n > 0 && n <= 7) {
            n2 = 9 - n;
        }
        else if (n != 8) {
            if (n == 9) {
                n2 = 0;
            }
            else {
                if (n < 10 || n > 17) {
                    throw new IllegalArgumentException();
                }
                n2 = 19 - n;
            }
        }
        return n2;
    }
    
    @Override
    public VINParsedResult parse(final Result result) {
        VINParsedResult vinParsedResult;
        if (result.getBarcodeFormat() != BarcodeFormat.CODE_39) {
            vinParsedResult = null;
        }
        else {
            final String trim = VINResultParser.IOQ.matcher(result.getText()).replaceAll("").trim();
            if (!VINResultParser.AZ09.matcher(trim).matches()) {
                vinParsedResult = null;
            }
            else {
                try {
                    if (!checkChecksum(trim)) {
                        vinParsedResult = null;
                    }
                    else {
                        final String substring = trim.substring(0, 3);
                        vinParsedResult = new VINParsedResult(trim, substring, trim.substring(3, 9), trim.substring(9, 17), countryCode(substring), trim.substring(3, 8), modelYear(trim.charAt(9)), trim.charAt(10), trim.substring(11));
                    }
                }
                catch (IllegalArgumentException ex) {
                    vinParsedResult = null;
                }
            }
        }
        return vinParsedResult;
    }
}
