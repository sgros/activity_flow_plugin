// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.ChecksumException;
import java.util.Arrays;
import com.google.zxing.Result;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.FormatException;

public final class Code39Reader extends OneDReader
{
    static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%";
    static final int ASTERISK_ENCODING;
    static final int[] CHARACTER_ENCODINGS;
    private static final String CHECK_DIGIT_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%";
    private final int[] counters;
    private final StringBuilder decodeRowResult;
    private final boolean extendedMode;
    private final boolean usingCheckDigit;
    
    static {
        final int[] array;
        final int[] character_ENCODINGS = array = new int[44];
        array[0] = 52;
        array[1] = 289;
        array[2] = 97;
        array[3] = 352;
        array[4] = 49;
        array[5] = 304;
        array[6] = 112;
        array[7] = 37;
        array[8] = 292;
        array[9] = 100;
        array[10] = 265;
        array[11] = 73;
        array[12] = 328;
        array[13] = 25;
        array[14] = 280;
        array[15] = 88;
        array[16] = 13;
        array[17] = 268;
        array[18] = 76;
        array[19] = 28;
        array[20] = 259;
        array[21] = 67;
        array[22] = 322;
        array[23] = 19;
        array[24] = 274;
        array[25] = 82;
        array[26] = 7;
        array[27] = 262;
        array[28] = 70;
        array[29] = 22;
        array[30] = 385;
        array[31] = 193;
        array[32] = 448;
        array[33] = 145;
        array[34] = 400;
        array[35] = 208;
        array[36] = 133;
        array[37] = 388;
        array[38] = 196;
        array[39] = 148;
        array[40] = 168;
        array[41] = 162;
        array[42] = 138;
        array[43] = 42;
        CHARACTER_ENCODINGS = character_ENCODINGS;
        ASTERISK_ENCODING = character_ENCODINGS[39];
    }
    
    public Code39Reader() {
        this(false);
    }
    
    public Code39Reader(final boolean b) {
        this(b, false);
    }
    
    public Code39Reader(final boolean usingCheckDigit, final boolean extendedMode) {
        this.usingCheckDigit = usingCheckDigit;
        this.extendedMode = extendedMode;
        this.decodeRowResult = new StringBuilder(20);
        this.counters = new int[9];
    }
    
    private static String decodeExtended(final CharSequence charSequence) throws FormatException {
        final int length = charSequence.length();
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            final char char1 = charSequence.charAt(i);
            if (char1 == '+' || char1 == '$' || char1 == '%' || char1 == '/') {
                final char char2 = charSequence.charAt(i + 1);
                final char c = '\0';
                char c2 = '\0';
                switch (char1) {
                    default: {
                        c2 = c;
                        break;
                    }
                    case 43: {
                        if (char2 >= 'A' && char2 <= 'Z') {
                            c2 = (char)(char2 + ' ');
                            break;
                        }
                        throw FormatException.getFormatInstance();
                    }
                    case 36: {
                        if (char2 >= 'A' && char2 <= 'Z') {
                            c2 = (char)(char2 - '@');
                            break;
                        }
                        throw FormatException.getFormatInstance();
                    }
                    case 37: {
                        if (char2 >= 'A' && char2 <= 'E') {
                            c2 = (char)(char2 - '&');
                            break;
                        }
                        if (char2 >= 'F' && char2 <= 'W') {
                            c2 = (char)(char2 - '\u000b');
                            break;
                        }
                        throw FormatException.getFormatInstance();
                    }
                    case 47: {
                        if (char2 >= 'A' && char2 <= 'O') {
                            c2 = (char)(char2 - ' ');
                            break;
                        }
                        if (char2 == 'Z') {
                            c2 = ':';
                            break;
                        }
                        throw FormatException.getFormatInstance();
                    }
                }
                sb.append(c2);
                ++i;
            }
            else {
                sb.append(char1);
            }
        }
        return sb.toString();
    }
    
    private static int[] findAsteriskPattern(final BitArray bitArray, final int[] array) throws NotFoundException {
        final int size = bitArray.getSize();
        int i = bitArray.getNextSet(0);
        int n = 0;
        int n2 = i;
        boolean b = false;
        final int length = array.length;
        while (i < size) {
            if (bitArray.get(i) ^ b) {
                ++array[n];
            }
            else {
                if (n == length - 1) {
                    if (toNarrowWidePattern(array) == Code39Reader.ASTERISK_ENCODING && bitArray.isRange(Math.max(0, n2 - (i - n2) / 2), n2, false)) {
                        return new int[] { n2, i };
                    }
                    n2 += array[0] + array[1];
                    System.arraycopy(array, 2, array, 0, length - 2);
                    array[length - 1] = (array[length - 2] = 0);
                    --n;
                }
                else {
                    ++n;
                }
                array[n] = 1;
                b = !b;
            }
            ++i;
        }
        throw NotFoundException.getNotFoundInstance();
    }
    
    private static char patternToChar(final int n) throws NotFoundException {
        for (int i = 0; i < Code39Reader.CHARACTER_ENCODINGS.length; ++i) {
            if (Code39Reader.CHARACTER_ENCODINGS[i] == n) {
                return "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".charAt(i);
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }
    
    private static int toNarrowWidePattern(final int[] array) {
        final int length = array.length;
        int n = 0;
        int i;
        do {
            int n2 = Integer.MAX_VALUE;
            int n4;
            for (int length2 = array.length, j = 0; j < length2; ++j, n2 = n4) {
                final int n3 = array[j];
                if (n3 < (n4 = n2)) {
                    n4 = n2;
                    if (n3 > n) {
                        n4 = n3;
                    }
                }
            }
            n = n2;
            i = 0;
            int n5 = 0;
            int n6 = 0;
            int n8;
            int n9;
            int n10;
            for (int k = 0; k < length; ++k, n6 = n8, n5 = n9, i = n10) {
                final int n7 = array[k];
                n8 = n6;
                n9 = n5;
                n10 = i;
                if (n7 > n) {
                    n8 = (n6 | 1 << length - 1 - k);
                    n10 = i + 1;
                    n9 = n5 + n7;
                }
            }
            if (i == 3) {
                int n11 = 0;
                int n12 = i;
                int n13;
                while (true) {
                    n13 = n6;
                    if (n11 >= length) {
                        break;
                    }
                    n13 = n6;
                    if (n12 <= 0) {
                        break;
                    }
                    final int n14 = array[n11];
                    int n15 = n12;
                    if (n14 > n) {
                        n15 = n12 - 1;
                        if (n14 << 1 >= n5) {
                            n13 = -1;
                            break;
                        }
                    }
                    ++n11;
                    n12 = n15;
                }
                return n13;
            }
        } while (i > 3);
        return -1;
    }
    
    @Override
    public Result decodeRow(final int n, final BitArray bitArray, final Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException {
        final int[] counters = this.counters;
        Arrays.fill(counters, 0);
        final StringBuilder decodeRowResult = this.decodeRowResult;
        decodeRowResult.setLength(0);
        final int[] asteriskPattern = findAsteriskPattern(bitArray, counters);
        int n2 = bitArray.getNextSet(asteriskPattern[1]);
        final int size = bitArray.getSize();
        char patternToChar;
        int n3;
        int n5;
        do {
            n3 = n2;
            OneDReader.recordPattern(bitArray, n3, counters);
            final int narrowWidePattern = toNarrowWidePattern(counters);
            if (narrowWidePattern < 0) {
                throw NotFoundException.getNotFoundInstance();
            }
            patternToChar = patternToChar(narrowWidePattern);
            decodeRowResult.append(patternToChar);
            final int length = counters.length;
            int i = 0;
            int n4 = n3;
            while (i < length) {
                n4 += counters[i];
                ++i;
            }
            n5 = (n2 = bitArray.getNextSet(n4));
        } while (patternToChar != '*');
        decodeRowResult.setLength(decodeRowResult.length() - 1);
        int n6 = 0;
        for (int length2 = counters.length, j = 0; j < length2; ++j) {
            n6 += counters[j];
        }
        if (n5 != size && n5 - n3 - n6 << 1 < n6) {
            throw NotFoundException.getNotFoundInstance();
        }
        if (this.usingCheckDigit) {
            final int length3 = decodeRowResult.length() - 1;
            int n7 = 0;
            for (int k = 0; k < length3; ++k) {
                n7 += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%".indexOf(this.decodeRowResult.charAt(k));
            }
            if (decodeRowResult.charAt(length3) != "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%".charAt(n7 % 43)) {
                throw ChecksumException.getChecksumInstance();
            }
            decodeRowResult.setLength(length3);
        }
        if (decodeRowResult.length() == 0) {
            throw NotFoundException.getNotFoundInstance();
        }
        String s;
        if (this.extendedMode) {
            s = decodeExtended(decodeRowResult);
        }
        else {
            s = decodeRowResult.toString();
        }
        return new Result(s, null, new ResultPoint[] { new ResultPoint((asteriskPattern[1] + asteriskPattern[0]) / 2.0f, (float)n), new ResultPoint(n3 + n6 / 2.0f, (float)n) }, BarcodeFormat.CODE_39);
    }
}
