// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.pdf417.decoder;

import java.util.Arrays;
import java.util.List;
import com.google.zxing.FormatException;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.pdf417.PDF417ResultMetadata;
import com.google.zxing.common.DecoderResult;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;

final class DecodedBitStreamParser
{
    private static final int AL = 28;
    private static final int AS = 27;
    private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
    private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
    private static final int BYTE_COMPACTION_MODE_LATCH = 901;
    private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
    private static final Charset DEFAULT_ENCODING;
    private static final int ECI_CHARSET = 927;
    private static final int ECI_GENERAL_PURPOSE = 926;
    private static final int ECI_USER_DEFINED = 925;
    private static final BigInteger[] EXP900;
    private static final int LL = 27;
    private static final int MACRO_PDF417_TERMINATOR = 922;
    private static final int MAX_NUMERIC_CODEWORDS = 15;
    private static final char[] MIXED_CHARS;
    private static final int ML = 28;
    private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
    private static final int NUMBER_OF_SEQUENCE_CODEWORDS = 2;
    private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
    private static final int PAL = 29;
    private static final int PL = 25;
    private static final int PS = 29;
    private static final char[] PUNCT_CHARS;
    private static final int TEXT_COMPACTION_MODE_LATCH = 900;
    
    static {
        PUNCT_CHARS = ";<>@[\\]_`~!\r\t,:\n-.$/\"|*()?{}'".toCharArray();
        MIXED_CHARS = "0123456789&\r\t,:#-.$/+%*=^".toCharArray();
        DEFAULT_ENCODING = Charset.forName("ISO-8859-1");
        (EXP900 = new BigInteger[16])[0] = BigInteger.ONE;
        final BigInteger value = BigInteger.valueOf(900L);
        DecodedBitStreamParser.EXP900[1] = value;
        for (int i = 2; i < DecodedBitStreamParser.EXP900.length; ++i) {
            DecodedBitStreamParser.EXP900[i] = DecodedBitStreamParser.EXP900[i - 1].multiply(value);
        }
    }
    
    private DecodedBitStreamParser() {
    }
    
    private static int byteCompaction(int i, final int[] array, final Charset charset, int j, final StringBuilder sb) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int n8;
        if (i == 901) {
            final int n = 0;
            long n2 = 0L;
            final int[] array2 = new int[6];
            int n3 = 0;
            int n4 = array[j];
            i = j + 1;
            j = n;
            while (i < array[0] && n3 == 0) {
                final int n5 = j + 1;
                array2[j] = n4;
                n2 = 900L * n2 + n4;
                j = i + 1;
                n4 = array[i];
                if (n4 == 900 || n4 == 901 || n4 == 902 || n4 == 924 || n4 == 928 || n4 == 923 || n4 == 922) {
                    i = j - 1;
                    n3 = 1;
                    j = n5;
                }
                else if (n5 % 5 == 0 && n5 > 0) {
                    for (i = 0; i < 6; ++i) {
                        byteArrayOutputStream.write((byte)(n2 >> (5 - i) * 8));
                    }
                    n2 = 0L;
                    final int n6 = 0;
                    i = j;
                    j = n6;
                }
                else {
                    i = j;
                    j = n5;
                }
            }
            int n7 = j;
            if (i == array[0]) {
                n7 = j;
                if (n4 < 900) {
                    array2[j] = n4;
                    n7 = j + 1;
                }
            }
            j = 0;
            while (true) {
                n8 = i;
                if (j >= n7) {
                    break;
                }
                byteArrayOutputStream.write((byte)array2[j]);
                ++j;
            }
        }
        else {
            n8 = j;
            if (i == 924) {
                i = 0;
                long n9 = 0L;
                int n10 = 0;
                int n11 = j;
                j = i;
                while ((n8 = n11) < array[0]) {
                    n8 = n11;
                    if (n10 != 0) {
                        break;
                    }
                    i = n11 + 1;
                    final int n12 = array[n11];
                    int n13;
                    long n14;
                    int n15;
                    if (n12 < 900) {
                        n13 = j + 1;
                        n14 = 900L * n9 + n12;
                        n15 = n10;
                    }
                    else if (n12 == 900 || n12 == 901 || n12 == 902 || n12 == 924 || n12 == 928 || n12 == 923 || n12 == 922) {
                        --i;
                        n15 = 1;
                        n13 = j;
                        n14 = n9;
                    }
                    else {
                        n13 = j;
                        n15 = n10;
                        n14 = n9;
                    }
                    j = n13;
                    n10 = n15;
                    n9 = n14;
                    n11 = i;
                    if (n13 % 5 != 0) {
                        continue;
                    }
                    j = n13;
                    n10 = n15;
                    n9 = n14;
                    n11 = i;
                    if (n13 <= 0) {
                        continue;
                    }
                    for (j = 0; j < 6; ++j) {
                        byteArrayOutputStream.write((byte)(n14 >> (5 - j) * 8));
                    }
                    n9 = 0L;
                    j = 0;
                    n10 = n15;
                    n11 = i;
                }
            }
        }
        sb.append(new String(byteArrayOutputStream.toByteArray(), charset));
        return n8;
    }
    
    static DecoderResult decode(final int[] array, final String s) throws FormatException {
        final StringBuilder sb = new StringBuilder(array.length << 1);
        Charset charset = DecodedBitStreamParser.DEFAULT_ENCODING;
        int i = 1 + 1;
        int n = array[1];
        final PDF417ResultMetadata other = new PDF417ResultMetadata();
        while (i < array[0]) {
            switch (n) {
                default: {
                    i = textCompaction(array, i - 1, sb);
                    break;
                }
                case 900: {
                    i = textCompaction(array, i, sb);
                    break;
                }
                case 901:
                case 924: {
                    i = byteCompaction(n, array, charset, i, sb);
                    break;
                }
                case 913: {
                    sb.append((char)array[i]);
                    ++i;
                    break;
                }
                case 902: {
                    i = numericCompaction(array, i, sb);
                    break;
                }
                case 927: {
                    charset = Charset.forName(CharacterSetECI.getCharacterSetECIByValue(array[i]).name());
                    ++i;
                    break;
                }
                case 926: {
                    i += 2;
                    break;
                }
                case 925: {
                    ++i;
                    break;
                }
                case 928: {
                    i = decodeMacroBlock(array, i, other);
                    break;
                }
                case 922:
                case 923: {
                    throw FormatException.getFormatInstance();
                }
            }
            if (i >= array.length) {
                throw FormatException.getFormatInstance();
            }
            n = array[i];
            ++i;
        }
        if (sb.length() == 0) {
            throw FormatException.getFormatInstance();
        }
        final DecoderResult decoderResult = new DecoderResult(null, sb.toString(), null, s);
        decoderResult.setOther(other);
        return decoderResult;
    }
    
    private static String decodeBase900toBase10(final int[] array, final int n) throws FormatException {
        BigInteger bigInteger = BigInteger.ZERO;
        for (int i = 0; i < n; ++i) {
            bigInteger = bigInteger.add(DecodedBitStreamParser.EXP900[n - i - 1].multiply(BigInteger.valueOf(array[i])));
        }
        final String string = bigInteger.toString();
        if (string.charAt(0) != '1') {
            throw FormatException.getFormatInstance();
        }
        return string.substring(1);
    }
    
    private static int decodeMacroBlock(final int[] array, int n, final PDF417ResultMetadata pdf417ResultMetadata) throws FormatException {
        if (n + 2 > array[0]) {
            throw FormatException.getFormatInstance();
        }
        final int[] array2 = new int[2];
        for (int i = 0; i < 2; ++i, ++n) {
            array2[i] = array[n];
        }
        pdf417ResultMetadata.setSegmentIndex(Integer.parseInt(decodeBase900toBase10(array2, 2)));
        final StringBuilder sb = new StringBuilder();
        final int textCompaction = textCompaction(array, n, sb);
        pdf417ResultMetadata.setFileId(sb.toString());
        if (array[textCompaction] == 923) {
            n = textCompaction + 1;
            final int[] original = new int[array[0] - n];
            int newLength = 0;
            int n2 = 0;
            while (n < array[0] && n2 == 0) {
                final int n3 = n + 1;
                n = array[n];
                if (n < 900) {
                    original[newLength] = n;
                    ++newLength;
                    n = n3;
                }
                else {
                    switch (n) {
                        default: {
                            throw FormatException.getFormatInstance();
                        }
                        case 922: {
                            pdf417ResultMetadata.setLastSegment(true);
                            n = n3 + 1;
                            n2 = 1;
                            continue;
                        }
                    }
                }
            }
            pdf417ResultMetadata.setOptionalData(Arrays.copyOf(original, newLength));
        }
        else {
            n = textCompaction;
            if (array[textCompaction] == 922) {
                pdf417ResultMetadata.setLastSegment(true);
                n = textCompaction + 1;
            }
        }
        return n;
    }
    
    private static void decodeTextCompaction(final int[] array, final int[] array2, final int n, final StringBuilder sb) {
        Mode mode = Mode.ALPHA;
        Mode alpha = Mode.ALPHA;
        Mode mode2 = null;
        for (int i = 0; i < n; ++i, alpha = mode2) {
            final int n2 = array[i];
            final char c = '\0';
            char c2 = '\0';
            switch (mode) {
                default: {
                    mode2 = alpha;
                    c2 = c;
                    break;
                }
                case ALPHA: {
                    if (n2 < 26) {
                        c2 = (char)(n2 + 65);
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 26) {
                        c2 = ' ';
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 27) {
                        mode = Mode.LOWER;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 28) {
                        mode = Mode.MIXED;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 29) {
                        final Mode punct_SHIFT = Mode.PUNCT_SHIFT;
                        c2 = c;
                        mode2 = mode;
                        mode = punct_SHIFT;
                        break;
                    }
                    if (n2 == 913) {
                        sb.append((char)array2[i]);
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    c2 = c;
                    mode2 = alpha;
                    if (n2 == 900) {
                        mode = Mode.ALPHA;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    break;
                }
                case LOWER: {
                    if (n2 < 26) {
                        c2 = (char)(n2 + 97);
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 26) {
                        c2 = ' ';
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 27) {
                        final Mode alpha_SHIFT = Mode.ALPHA_SHIFT;
                        c2 = c;
                        mode2 = mode;
                        mode = alpha_SHIFT;
                        break;
                    }
                    if (n2 == 28) {
                        mode = Mode.MIXED;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 29) {
                        final Mode punct_SHIFT2 = Mode.PUNCT_SHIFT;
                        c2 = c;
                        mode2 = mode;
                        mode = punct_SHIFT2;
                        break;
                    }
                    if (n2 == 913) {
                        sb.append((char)array2[i]);
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    c2 = c;
                    mode2 = alpha;
                    if (n2 == 900) {
                        mode = Mode.ALPHA;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    break;
                }
                case MIXED: {
                    if (n2 < 25) {
                        c2 = DecodedBitStreamParser.MIXED_CHARS[n2];
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 25) {
                        mode = Mode.PUNCT;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 26) {
                        c2 = ' ';
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 27) {
                        mode = Mode.LOWER;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 28) {
                        mode = Mode.ALPHA;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 29) {
                        final Mode punct_SHIFT3 = Mode.PUNCT_SHIFT;
                        c2 = c;
                        mode2 = mode;
                        mode = punct_SHIFT3;
                        break;
                    }
                    if (n2 == 913) {
                        sb.append((char)array2[i]);
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    c2 = c;
                    mode2 = alpha;
                    if (n2 == 900) {
                        mode = Mode.ALPHA;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    break;
                }
                case PUNCT: {
                    if (n2 < 29) {
                        c2 = DecodedBitStreamParser.PUNCT_CHARS[n2];
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 29) {
                        mode = Mode.ALPHA;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 913) {
                        sb.append((char)array2[i]);
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    c2 = c;
                    mode2 = alpha;
                    if (n2 == 900) {
                        mode = Mode.ALPHA;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    break;
                }
                case ALPHA_SHIFT: {
                    mode = alpha;
                    if (n2 < 26) {
                        c2 = (char)(n2 + 65);
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 26) {
                        c2 = ' ';
                        mode2 = alpha;
                        break;
                    }
                    c2 = c;
                    mode2 = alpha;
                    if (n2 == 900) {
                        mode = Mode.ALPHA;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    break;
                }
                case PUNCT_SHIFT: {
                    mode = alpha;
                    if (n2 < 29) {
                        c2 = DecodedBitStreamParser.PUNCT_CHARS[n2];
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 29) {
                        mode = Mode.ALPHA;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    if (n2 == 913) {
                        sb.append((char)array2[i]);
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    c2 = c;
                    mode2 = alpha;
                    if (n2 == 900) {
                        mode = Mode.ALPHA;
                        c2 = c;
                        mode2 = alpha;
                        break;
                    }
                    break;
                }
            }
            if (c2 != '\0') {
                sb.append(c2);
            }
        }
    }
    
    private static int numericCompaction(final int[] array, int n, final StringBuilder sb) throws FormatException {
        int n2 = 0;
        int n3 = 0;
        final int[] array2 = new int[15];
        int n4 = n;
        while (n4 < array[0] && n3 == 0) {
            n = n4 + 1;
            final int n5 = array[n4];
            int n6 = n3;
            if (n == array[0]) {
                n6 = 1;
            }
            int n7;
            if (n5 < 900) {
                array2[n2] = n5;
                n7 = n2 + 1;
            }
            else if (n5 == 900 || n5 == 901 || n5 == 924 || n5 == 928 || n5 == 923 || n5 == 922) {
                --n;
                n6 = 1;
                n7 = n2;
            }
            else {
                n7 = n2;
            }
            if (n7 % 15 != 0 && n5 != 902) {
                n2 = n7;
                n3 = n6;
                n4 = n;
                if (n6 == 0) {
                    continue;
                }
            }
            n2 = n7;
            n3 = n6;
            n4 = n;
            if (n7 > 0) {
                sb.append(decodeBase900toBase10(array2, n7));
                n2 = 0;
                n3 = n6;
                n4 = n;
            }
        }
        return n4;
    }
    
    private static int textCompaction(final int[] array, int n, final StringBuilder sb) {
        final int[] array2 = new int[array[0] - n << 1];
        final int[] array3 = new int[array[0] - n << 1];
        int n2 = 0;
        int n3 = 0;
        while (n < array[0] && n3 == 0) {
            final int n4 = n + 1;
            n = array[n];
            if (n < 900) {
                array2[n2] = n / 30;
                array2[n2 + 1] = n % 30;
                n2 += 2;
                n = n4;
            }
            else {
                switch (n) {
                    default: {
                        n = n4;
                        continue;
                    }
                    case 900: {
                        array2[n2] = 900;
                        ++n2;
                        n = n4;
                        continue;
                    }
                    case 901:
                    case 902:
                    case 922:
                    case 923:
                    case 924:
                    case 928: {
                        n = n4 - 1;
                        n3 = 1;
                        continue;
                    }
                    case 913: {
                        array2[n2] = 913;
                        n = n4 + 1;
                        array3[n2] = array[n4];
                        ++n2;
                        continue;
                    }
                }
            }
        }
        decodeTextCompaction(array2, array3, n2, sb);
        return n;
    }
    
    private enum Mode
    {
        ALPHA, 
        ALPHA_SHIFT, 
        LOWER, 
        MIXED, 
        PUNCT, 
        PUNCT_SHIFT;
    }
}
