// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import java.util.Arrays;
import com.google.zxing.Result;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import com.google.zxing.common.BitArray;
import com.google.zxing.NotFoundException;

public final class CodaBarReader extends OneDReader
{
    static final char[] ALPHABET;
    private static final String ALPHABET_STRING = "0123456789-$:/.+ABCD";
    static final int[] CHARACTER_ENCODINGS;
    private static final float MAX_ACCEPTABLE = 2.0f;
    private static final int MIN_CHARACTER_LENGTH = 3;
    private static final float PADDING = 1.5f;
    private static final char[] STARTEND_ENCODING;
    private int counterLength;
    private int[] counters;
    private final StringBuilder decodeRowResult;
    
    static {
        ALPHABET = "0123456789-$:/.+ABCD".toCharArray();
        CHARACTER_ENCODINGS = new int[] { 3, 6, 9, 96, 18, 66, 33, 36, 48, 72, 12, 24, 69, 81, 84, 21, 26, 41, 11, 14 };
        STARTEND_ENCODING = new char[] { 'A', 'B', 'C', 'D' };
    }
    
    public CodaBarReader() {
        this.decodeRowResult = new StringBuilder(20);
        this.counters = new int[80];
        this.counterLength = 0;
    }
    
    static boolean arrayContains(final char[] array, final char c) {
        boolean b = false;
        if (array != null) {
            final int length = array.length;
            int n = 0;
            while (true) {
                b = b;
                if (n >= length) {
                    break;
                }
                if (array[n] == c) {
                    b = true;
                    break;
                }
                ++n;
            }
        }
        return b;
    }
    
    private void counterAppend(final int n) {
        this.counters[this.counterLength] = n;
        ++this.counterLength;
        if (this.counterLength >= this.counters.length) {
            final int[] counters = new int[this.counterLength << 1];
            System.arraycopy(this.counters, 0, counters, 0, this.counterLength);
            this.counters = counters;
        }
    }
    
    private int findStartPattern() throws NotFoundException {
        for (int i = 1; i < this.counterLength; i += 2) {
            final int narrowWidePattern = this.toNarrowWidePattern(i);
            if (narrowWidePattern != -1 && arrayContains(CodaBarReader.STARTEND_ENCODING, CodaBarReader.ALPHABET[narrowWidePattern])) {
                int n = 0;
                for (int j = i; j < i + 7; ++j) {
                    n += this.counters[j];
                }
                if (i == 1 || this.counters[i - 1] >= n / 2) {
                    return i;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }
    
    private void setCounters(final BitArray bitArray) throws NotFoundException {
        this.counterLength = 0;
        int i = bitArray.getNextUnset(0);
        final int size = bitArray.getSize();
        if (i >= size) {
            throw NotFoundException.getNotFoundInstance();
        }
        int n = 1;
        int n2 = 0;
        while (i < size) {
            boolean b;
            int n3;
            if (((bitArray.get(i) ? 1 : 0) ^ n) != 0x0) {
                ++n2;
                b = (n != 0);
                n3 = n2;
            }
            else {
                this.counterAppend(n2);
                final int n4 = 1;
                b = (n == 0);
                n3 = n4;
            }
            ++i;
            n2 = n3;
            n = (b ? 1 : 0);
        }
        this.counterAppend(n2);
    }
    
    private int toNarrowWidePattern(int n) {
        final int n2 = n + 7;
        if (n2 >= this.counterLength) {
            n = -1;
        }
        else {
            final int[] counters = this.counters;
            int n3 = 0;
            int n4 = Integer.MAX_VALUE;
            int n6;
            int n7;
            for (int i = n; i < n2; i += 2, n3 = n7, n4 = n6) {
                final int n5 = counters[i];
                if (n5 < (n6 = n4)) {
                    n6 = n5;
                }
                if (n5 > (n7 = n3)) {
                    n7 = n5;
                }
            }
            final int n8 = (n4 + n3) / 2;
            int n9 = 0;
            int n10 = Integer.MAX_VALUE;
            int n12;
            int n13;
            for (int j = n + 1; j < n2; j += 2, n9 = n13, n10 = n12) {
                final int n11 = counters[j];
                if (n11 < (n12 = n10)) {
                    n12 = n11;
                }
                if (n11 > (n13 = n9)) {
                    n13 = n11;
                }
            }
            final int n14 = (n10 + n9) / 2;
            int n15 = 128;
            int n16 = 0;
            int n18;
            for (int k = 0; k < 7; ++k, n16 = n18) {
                int n17;
                if ((k & 0x1) == 0x0) {
                    n17 = n8;
                }
                else {
                    n17 = n14;
                }
                n15 >>= 1;
                n18 = n16;
                if (counters[n + k] > n17) {
                    n18 = (n16 | n15);
                }
            }
            for (int l = 0; l < CodaBarReader.CHARACTER_ENCODINGS.length; ++l) {
                n = l;
                if (CodaBarReader.CHARACTER_ENCODINGS[l] == n16) {
                    return n;
                }
            }
            n = -1;
        }
        return n;
    }
    
    private void validatePattern(int n) throws NotFoundException {
        final int[] array2;
        final int[] array = array2 = new int[4];
        array2[1] = (array2[0] = 0);
        array2[3] = (array2[2] = 0);
        final int[] array4;
        final int[] array3 = array4 = new int[4];
        array4[1] = (array4[0] = 0);
        array4[3] = (array4[2] = 0);
        final int n2 = this.decodeRowResult.length() - 1;
        int n3 = n;
        int n4 = 0;
        while (true) {
            int n5 = CodaBarReader.CHARACTER_ENCODINGS[this.decodeRowResult.charAt(n4)];
            for (int i = 6; i >= 0; --i) {
                final int n6 = (i & 0x1) + ((n5 & 0x1) << 1);
                array[n6] += this.counters[n3 + i];
                ++array3[n6];
                n5 >>= 1;
            }
            if (n4 >= n2) {
                break;
            }
            n3 += 8;
            ++n4;
        }
        final float[] array5 = new float[4];
        final float[] array6 = new float[4];
        for (int j = 0; j < 2; ++j) {
            array6[j] = 0.0f;
            array5[j] = (array6[j + 2] = (array[j] / (float)array3[j] + array[j + 2] / (float)array3[j + 2]) / 2.0f);
            array5[j + 2] = (array[j + 2] * 2.0f + 1.5f) / array3[j + 2];
        }
        final int n7 = 0;
        int n8 = n;
        n = n7;
        while (true) {
            int n9 = CodaBarReader.CHARACTER_ENCODINGS[this.decodeRowResult.charAt(n)];
            for (int k = 6; k >= 0; --k) {
                final int n10 = (k & 0x1) + ((n9 & 0x1) << 1);
                final int n11 = this.counters[n8 + k];
                if (n11 < array6[n10] || n11 > array5[n10]) {
                    throw NotFoundException.getNotFoundInstance();
                }
                n9 >>= 1;
            }
            if (n >= n2) {
                return;
            }
            n8 += 8;
            ++n;
        }
    }
    
    @Override
    public Result decodeRow(final int n, final BitArray counters, final Map<DecodeHintType, ?> map) throws NotFoundException {
        Arrays.fill(this.counters, 0);
        this.setCounters(counters);
        int startPattern;
        int i = startPattern = this.findStartPattern();
        this.decodeRowResult.setLength(0);
        int narrowWidePattern;
        int n2;
        do {
            narrowWidePattern = this.toNarrowWidePattern(startPattern);
            if (narrowWidePattern == -1) {
                throw NotFoundException.getNotFoundInstance();
            }
            this.decodeRowResult.append((char)narrowWidePattern);
            n2 = startPattern + 8;
        } while ((this.decodeRowResult.length() <= 1 || !arrayContains(CodaBarReader.STARTEND_ENCODING, CodaBarReader.ALPHABET[narrowWidePattern])) && (startPattern = n2) < this.counterLength);
        final int n3 = this.counters[n2 - 1];
        int n4 = 0;
        for (int j = -8; j < -1; ++j) {
            n4 += this.counters[n2 + j];
        }
        if (n2 < this.counterLength && n3 < n4 / 2) {
            throw NotFoundException.getNotFoundInstance();
        }
        this.validatePattern(i);
        for (int k = 0; k < this.decodeRowResult.length(); ++k) {
            this.decodeRowResult.setCharAt(k, CodaBarReader.ALPHABET[this.decodeRowResult.charAt(k)]);
        }
        if (!arrayContains(CodaBarReader.STARTEND_ENCODING, this.decodeRowResult.charAt(0))) {
            throw NotFoundException.getNotFoundInstance();
        }
        if (!arrayContains(CodaBarReader.STARTEND_ENCODING, this.decodeRowResult.charAt(this.decodeRowResult.length() - 1))) {
            throw NotFoundException.getNotFoundInstance();
        }
        if (this.decodeRowResult.length() <= 3) {
            throw NotFoundException.getNotFoundInstance();
        }
        if (map == null || !map.containsKey(DecodeHintType.RETURN_CODABAR_START_END)) {
            this.decodeRowResult.deleteCharAt(this.decodeRowResult.length() - 1);
            this.decodeRowResult.deleteCharAt(0);
        }
        int n5 = 0;
        for (int l = 0; l < i; ++l) {
            n5 += this.counters[l];
        }
        final float n6 = (float)n5;
        while (i < n2 - 1) {
            n5 += this.counters[i];
            ++i;
        }
        return new Result(this.decodeRowResult.toString(), null, new ResultPoint[] { new ResultPoint(n6, (float)n), new ResultPoint((float)n5, (float)n) }, BarcodeFormat.CODABAR);
    }
}
