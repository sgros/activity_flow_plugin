// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import java.util.Iterator;
import java.util.ArrayList;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.EncodeHintType;
import java.util.Map;
import com.google.zxing.BarcodeFormat;

public final class Code128Writer extends OneDimensionalCodeWriter
{
    private static final int CODE_CODE_B = 100;
    private static final int CODE_CODE_C = 99;
    private static final int CODE_FNC_1 = 102;
    private static final int CODE_FNC_2 = 97;
    private static final int CODE_FNC_3 = 96;
    private static final int CODE_FNC_4_B = 100;
    private static final int CODE_START_B = 104;
    private static final int CODE_START_C = 105;
    private static final int CODE_STOP = 106;
    private static final char ESCAPE_FNC_1 = '\u00f1';
    private static final char ESCAPE_FNC_2 = '\u00f2';
    private static final char ESCAPE_FNC_3 = '\u00f3';
    private static final char ESCAPE_FNC_4 = '\u00f4';
    
    private static int chooseCode(final CharSequence charSequence, int n, final int n2) {
        final CType cType = findCType(charSequence, n);
        int n3;
        if (cType == CType.UNCODABLE || cType == CType.ONE_DIGIT) {
            n3 = 100;
        }
        else if ((n3 = n2) != 99) {
            if (n2 == 100) {
                n3 = n2;
                if (cType != CType.FNC_1) {
                    final CType cType2 = findCType(charSequence, n + 2);
                    n3 = n2;
                    if (cType2 != CType.UNCODABLE) {
                        n3 = n2;
                        if (cType2 != CType.ONE_DIGIT) {
                            if (cType2 == CType.FNC_1) {
                                if (findCType(charSequence, n + 3) == CType.TWO_DIGITS) {
                                    n3 = 99;
                                }
                                else {
                                    n3 = 100;
                                }
                            }
                            else {
                                n += 4;
                                CType cType3;
                                while (true) {
                                    cType3 = findCType(charSequence, n);
                                    if (cType3 != CType.TWO_DIGITS) {
                                        break;
                                    }
                                    n += 2;
                                }
                                if (cType3 == CType.ONE_DIGIT) {
                                    n3 = 100;
                                }
                                else {
                                    n3 = 99;
                                }
                            }
                        }
                    }
                }
            }
            else {
                CType cType4;
                if ((cType4 = cType) == CType.FNC_1) {
                    cType4 = findCType(charSequence, n + 1);
                }
                if (cType4 == CType.TWO_DIGITS) {
                    n3 = 99;
                }
                else {
                    n3 = 100;
                }
            }
        }
        return n3;
    }
    
    private static CType findCType(final CharSequence charSequence, int char1) {
        final int length = charSequence.length();
        CType cType;
        if (char1 >= length) {
            cType = CType.UNCODABLE;
        }
        else {
            final char char2 = charSequence.charAt(char1);
            if (char2 == '\u00f1') {
                cType = CType.FNC_1;
            }
            else if (char2 < '0' || char2 > '9') {
                cType = CType.UNCODABLE;
            }
            else if (char1 + 1 >= length) {
                cType = CType.ONE_DIGIT;
            }
            else {
                char1 = charSequence.charAt(char1 + 1);
                if (char1 < 48 || char1 > 57) {
                    cType = CType.ONE_DIGIT;
                }
                else {
                    cType = CType.TWO_DIGITS;
                }
            }
        }
        return cType;
    }
    
    @Override
    public BitMatrix encode(final String s, final BarcodeFormat obj, final int n, final int n2, final Map<EncodeHintType, ?> map) throws WriterException {
        if (obj != BarcodeFormat.CODE_128) {
            throw new IllegalArgumentException("Can only encode CODE_128, but got " + obj);
        }
        return super.encode(s, obj, n, n2, map);
    }
    
    @Override
    public boolean[] encode(final String s) {
        final int length = s.length();
        if (length <= 0 || length > 80) {
            throw new IllegalArgumentException("Contents length should be between 1 and 80 characters, but got " + length);
        }
        for (int i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 < ' ' || char1 > '~') {
                switch (char1) {
                    default: {
                        throw new IllegalArgumentException("Bad character in input: " + char1);
                    }
                    case 241:
                    case 242:
                    case 243:
                    case 244: {
                        break;
                    }
                }
            }
        }
        final ArrayList<int[]> list = new ArrayList<int[]>();
        int n = 0;
        int n2 = 1;
        int n3 = 0;
        int n4;
        for (int j = 0; j < length; j = n4) {
            int chooseCode = chooseCode(s, j, n3);
            int int1 = 0;
            if (chooseCode == n3) {
                switch (s.charAt(j)) {
                    default: {
                        if (n3 == 100) {
                            int1 = s.charAt(j) - ' ';
                            break;
                        }
                        int1 = Integer.parseInt(s.substring(j, j + 2));
                        ++j;
                        break;
                    }
                    case '\u00f1': {
                        int1 = 102;
                        break;
                    }
                    case '\u00f2': {
                        int1 = 97;
                        break;
                    }
                    case '\u00f3': {
                        int1 = 96;
                        break;
                    }
                    case '\u00f4': {
                        int1 = 100;
                        break;
                    }
                }
                n4 = j + 1;
                chooseCode = n3;
            }
            else {
                if (n3 == 0) {
                    if (chooseCode == 100) {
                        int1 = 104;
                    }
                    else {
                        int1 = 105;
                    }
                }
                else {
                    int1 = chooseCode;
                }
                n4 = j;
            }
            list.add(Code128Reader.CODE_PATTERNS[int1]);
            final int n5 = n += int1 * n2;
            n3 = chooseCode;
            j = n4;
            if (n4 != 0) {
                ++n2;
                n = n5;
                n3 = chooseCode;
            }
        }
        list.add(Code128Reader.CODE_PATTERNS[n % 103]);
        list.add(Code128Reader.CODE_PATTERNS[106]);
        int n6 = 0;
        for (final int[] array : list) {
            final int length2 = array.length;
            int n7 = 0;
            int n8 = n6;
            while (true) {
                n6 = n8;
                if (n7 >= length2) {
                    break;
                }
                n8 += array[n7];
                ++n7;
            }
        }
        final boolean[] array2 = new boolean[n6];
        int n9 = 0;
        final Iterator<Object> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            n9 += OneDimensionalCodeWriter.appendPattern(array2, n9, iterator2.next(), true);
        }
        return array2;
    }
    
    private enum CType
    {
        FNC_1, 
        ONE_DIGIT, 
        TWO_DIGITS, 
        UNCODABLE;
    }
}
