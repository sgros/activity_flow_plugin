// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.qrcode.decoder;

import java.io.UnsupportedEncodingException;
import com.google.zxing.common.StringUtils;
import java.util.List;
import java.util.Collection;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.FormatException;
import java.util.ArrayList;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.DecodeHintType;
import java.util.Map;

final class DecodedBitStreamParser
{
    private static final char[] ALPHANUMERIC_CHARS;
    private static final int GB2312_SUBSET = 1;
    
    static {
        ALPHANUMERIC_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:".toCharArray();
    }
    
    private DecodedBitStreamParser() {
    }
    
    static DecoderResult decode(final byte[] array, final Version version, final ErrorCorrectionLevel errorCorrectionLevel, final Map<DecodeHintType, ?> map) throws FormatException {
        final BitSource bitSource = new BitSource(array);
        final StringBuilder sb = new StringBuilder(50);
        final ArrayList<byte[]> list = new ArrayList<byte[]>(1);
        int n = -1;
        int n2 = -1;
        CharacterSetECI characterSetECI = null;
        boolean b = false;
        Mode mode = null;
        CharacterSetECI characterSetECIByValue;
        int bits;
        int bits2;
        boolean b2;
        Mode terminator;
        String string;
        List<byte[]> list2;
        String string2;
        int bits3;
        int bits4;
        int bits5;
        Label_0158_Outer:Label_0164_Outer:
        while (true) {
            while (true) {
            Label_0546:
                while (true) {
                Label_0540:
                    while (true) {
                        Label_0250: {
                            try {
                                while (true) {
                                    if (bitSource.available() < 4) {
                                        mode = Mode.TERMINATOR;
                                    }
                                    else {
                                        mode = Mode.forBits(bitSource.readBits(4));
                                    }
                                    characterSetECIByValue = characterSetECI;
                                    bits = n;
                                    bits2 = n2;
                                    b2 = b;
                                    if (mode != Mode.TERMINATOR) {
                                        if (mode == Mode.FNC1_FIRST_POSITION || mode == Mode.FNC1_SECOND_POSITION) {
                                            b2 = true;
                                            bits2 = n2;
                                            bits = n;
                                            characterSetECIByValue = characterSetECI;
                                        }
                                        else {
                                            if (mode != Mode.STRUCTURED_APPEND) {
                                                break Label_0250;
                                            }
                                            if (bitSource.available() < 16) {
                                                throw FormatException.getFormatInstance();
                                            }
                                            break;
                                        }
                                    }
                                    terminator = Mode.TERMINATOR;
                                    characterSetECI = characterSetECIByValue;
                                    n = bits;
                                    n2 = bits2;
                                    b = b2;
                                    if (mode == terminator) {
                                        string = sb.toString();
                                        if (!list.isEmpty()) {
                                            break Label_0540;
                                        }
                                        list2 = null;
                                        if (errorCorrectionLevel == null) {
                                            string2 = null;
                                            return new DecoderResult(array, string, list2, string2, bits, bits2);
                                        }
                                        break Label_0546;
                                    }
                                }
                            }
                            catch (IllegalArgumentException ex) {
                                throw FormatException.getFormatInstance();
                            }
                            bits = bitSource.readBits(8);
                            bits2 = bitSource.readBits(8);
                            characterSetECIByValue = characterSetECI;
                            b2 = b;
                            continue Label_0158_Outer;
                        }
                        if (mode == Mode.ECI) {
                            characterSetECIByValue = CharacterSetECI.getCharacterSetECIByValue(parseECIValue(bitSource));
                            bits = n;
                            bits2 = n2;
                            b2 = b;
                            if (characterSetECIByValue == null) {
                                throw FormatException.getFormatInstance();
                            }
                            continue Label_0158_Outer;
                        }
                        else if (mode == Mode.HANZI) {
                            bits3 = bitSource.readBits(4);
                            bits4 = bitSource.readBits(mode.getCharacterCountBits(version));
                            characterSetECIByValue = characterSetECI;
                            bits = n;
                            bits2 = n2;
                            b2 = b;
                            if (bits3 == 1) {
                                decodeHanziSegment(bitSource, sb, bits4);
                                characterSetECIByValue = characterSetECI;
                                bits = n;
                                bits2 = n2;
                                b2 = b;
                                continue Label_0158_Outer;
                            }
                            continue Label_0158_Outer;
                        }
                        else {
                            bits5 = bitSource.readBits(mode.getCharacterCountBits(version));
                            if (mode == Mode.NUMERIC) {
                                decodeNumericSegment(bitSource, sb, bits5);
                                characterSetECIByValue = characterSetECI;
                                bits = n;
                                bits2 = n2;
                                b2 = b;
                                continue Label_0158_Outer;
                            }
                            if (mode == Mode.ALPHANUMERIC) {
                                decodeAlphanumericSegment(bitSource, sb, bits5, b);
                                characterSetECIByValue = characterSetECI;
                                bits = n;
                                bits2 = n2;
                                b2 = b;
                                continue Label_0158_Outer;
                            }
                            if (mode == Mode.BYTE) {
                                decodeByteSegment(bitSource, sb, bits5, characterSetECI, list, map);
                                characterSetECIByValue = characterSetECI;
                                bits = n;
                                bits2 = n2;
                                b2 = b;
                                continue Label_0158_Outer;
                            }
                            if (mode == Mode.KANJI) {
                                decodeKanjiSegment(bitSource, sb, bits5);
                                characterSetECIByValue = characterSetECI;
                                bits = n;
                                bits2 = n2;
                                b2 = b;
                                continue Label_0158_Outer;
                            }
                            throw FormatException.getFormatInstance();
                        }
                        break;
                    }
                    list2 = list;
                    continue Label_0164_Outer;
                }
                string2 = errorCorrectionLevel.toString();
                continue;
            }
        }
    }
    
    private static void decodeAlphanumericSegment(final BitSource bitSource, final StringBuilder sb, int i, final boolean b) throws FormatException {
        final int length = sb.length();
        while (i > 1) {
            if (bitSource.available() < 11) {
                throw FormatException.getFormatInstance();
            }
            final int bits = bitSource.readBits(11);
            sb.append(toAlphaNumericChar(bits / 45));
            sb.append(toAlphaNumericChar(bits % 45));
            i -= 2;
        }
        if (i == 1) {
            if (bitSource.available() < 6) {
                throw FormatException.getFormatInstance();
            }
            sb.append(toAlphaNumericChar(bitSource.readBits(6)));
        }
        if (b) {
            for (i = length; i < sb.length(); ++i) {
                if (sb.charAt(i) == '%') {
                    if (i < sb.length() - 1 && sb.charAt(i + 1) == '%') {
                        sb.deleteCharAt(i + 1);
                    }
                    else {
                        sb.setCharAt(i, '\u001d');
                    }
                }
            }
        }
    }
    
    private static void decodeByteSegment(final BitSource bitSource, final StringBuilder sb, final int n, final CharacterSetECI characterSetECI, final Collection<byte[]> collection, final Map<DecodeHintType, ?> map) throws FormatException {
        if (n << 3 > bitSource.available()) {
            throw FormatException.getFormatInstance();
        }
        final byte[] bytes = new byte[n];
        for (int i = 0; i < n; ++i) {
            bytes[i] = (byte)bitSource.readBits(8);
        }
        Label_0087: {
            if (characterSetECI != null) {
                break Label_0087;
            }
            String charsetName = StringUtils.guessEncoding(bytes, map);
            try {
                while (true) {
                    sb.append(new String(bytes, charsetName));
                    collection.add(bytes);
                    return;
                    charsetName = characterSetECI.name();
                    continue;
                }
            }
            catch (UnsupportedEncodingException ex) {
                throw FormatException.getFormatInstance();
            }
        }
    }
    
    private static void decodeHanziSegment(final BitSource bitSource, final StringBuilder sb, int i) throws FormatException {
        if (i * 13 > bitSource.available()) {
            throw FormatException.getFormatInstance();
        }
        final byte[] bytes = new byte[i * 2];
        int n = 0;
        while (i > 0) {
            final int bits = bitSource.readBits(13);
            final int n2 = bits / 96 << 8 | bits % 96;
            int n3;
            if (n2 < 959) {
                n3 = n2 + 41377;
            }
            else {
                n3 = n2 + 42657;
            }
            bytes[n] = (byte)(n3 >> 8);
            bytes[n + 1] = (byte)n3;
            n += 2;
            --i;
        }
        try {
            sb.append(new String(bytes, "GB2312"));
        }
        catch (UnsupportedEncodingException ex) {
            throw FormatException.getFormatInstance();
        }
    }
    
    private static void decodeKanjiSegment(final BitSource bitSource, final StringBuilder sb, int i) throws FormatException {
        if (i * 13 > bitSource.available()) {
            throw FormatException.getFormatInstance();
        }
        final byte[] bytes = new byte[i * 2];
        int n = 0;
        while (i > 0) {
            final int bits = bitSource.readBits(13);
            final int n2 = bits / 192 << 8 | bits % 192;
            int n3;
            if (n2 < 7936) {
                n3 = n2 + 33088;
            }
            else {
                n3 = n2 + 49472;
            }
            bytes[n] = (byte)(n3 >> 8);
            bytes[n + 1] = (byte)n3;
            n += 2;
            --i;
        }
        try {
            sb.append(new String(bytes, "SJIS"));
        }
        catch (UnsupportedEncodingException ex) {
            throw FormatException.getFormatInstance();
        }
    }
    
    private static void decodeNumericSegment(final BitSource bitSource, final StringBuilder sb, int i) throws FormatException {
        while (i >= 3) {
            if (bitSource.available() < 10) {
                throw FormatException.getFormatInstance();
            }
            final int bits = bitSource.readBits(10);
            if (bits >= 1000) {
                throw FormatException.getFormatInstance();
            }
            sb.append(toAlphaNumericChar(bits / 100));
            sb.append(toAlphaNumericChar(bits / 10 % 10));
            sb.append(toAlphaNumericChar(bits % 10));
            i -= 3;
        }
        if (i == 2) {
            if (bitSource.available() < 7) {
                throw FormatException.getFormatInstance();
            }
            i = bitSource.readBits(7);
            if (i >= 100) {
                throw FormatException.getFormatInstance();
            }
            sb.append(toAlphaNumericChar(i / 10));
            sb.append(toAlphaNumericChar(i % 10));
        }
        else if (i == 1) {
            if (bitSource.available() < 4) {
                throw FormatException.getFormatInstance();
            }
            i = bitSource.readBits(4);
            if (i >= 10) {
                throw FormatException.getFormatInstance();
            }
            sb.append(toAlphaNumericChar(i));
        }
    }
    
    private static int parseECIValue(final BitSource bitSource) throws FormatException {
        final int bits = bitSource.readBits(8);
        int n;
        if ((bits & 0x80) == 0x0) {
            n = (bits & 0x7F);
        }
        else if ((bits & 0xC0) == 0x80) {
            n = ((bits & 0x3F) << 8 | bitSource.readBits(8));
        }
        else {
            if ((bits & 0xE0) != 0xC0) {
                throw FormatException.getFormatInstance();
            }
            n = ((bits & 0x1F) << 16 | bitSource.readBits(16));
        }
        return n;
    }
    
    private static char toAlphaNumericChar(final int n) throws FormatException {
        if (n >= DecodedBitStreamParser.ALPHANUMERIC_CHARS.length) {
            throw FormatException.getFormatInstance();
        }
        return DecodedBitStreamParser.ALPHANUMERIC_CHARS[n];
    }
}
