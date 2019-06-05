// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.datamatrix.encoder;

final class EdifactEncoder implements Encoder
{
    private static void encodeChar(final char c, final StringBuilder sb) {
        if (c >= ' ' && c <= '?') {
            sb.append(c);
        }
        else if (c >= '@' && c <= '^') {
            sb.append((char)(c - '@'));
        }
        else {
            HighLevelEncoder.illegalCharacter(c);
        }
    }
    
    private static String encodeToCodewords(final CharSequence charSequence, int n) {
        int char1 = 0;
        final int n2 = charSequence.length() - n;
        if (n2 == 0) {
            throw new IllegalStateException("StringBuilder must not be empty");
        }
        final char char2 = charSequence.charAt(n);
        char char3;
        if (n2 >= 2) {
            char3 = charSequence.charAt(n + 1);
        }
        else {
            char3 = '\0';
        }
        char char4;
        if (n2 >= 3) {
            char4 = charSequence.charAt(n + 2);
        }
        else {
            char4 = '\0';
        }
        if (n2 >= 4) {
            char1 = charSequence.charAt(n + 3);
        }
        n = (char2 << 18) + (char3 << 12) + (char4 << 6) + char1;
        final char c = (char)(n >> 16 & 0xFF);
        final char c2 = (char)(n >> 8 & 0xFF);
        final char c3 = (char)(n & 0xFF);
        final StringBuilder sb = new StringBuilder(3);
        sb.append(c);
        if (n2 >= 2) {
            sb.append(c2);
        }
        if (n2 >= 3) {
            sb.append(c3);
        }
        return sb.toString();
    }
    
    private static void handleEOD(final EncoderContext encoderContext, final CharSequence charSequence) {
        while (true) {
            int n = 1;
            int length = 0;
            Label_0096: {
                try {
                    length = charSequence.length();
                    if (length != 0) {
                        if (length == 1) {
                            encoderContext.updateSymbolInfo();
                            final int dataCapacity = encoderContext.getSymbolInfo().getDataCapacity();
                            final int codewordCount = encoderContext.getCodewordCount();
                            if (encoderContext.getRemainingCharacters() == 0 && dataCapacity - codewordCount <= 2) {
                                encoderContext.signalEncoderChange(0);
                                return;
                            }
                        }
                        if (length > 4) {
                            throw new IllegalStateException("Count must not exceed 4");
                        }
                        break Label_0096;
                    }
                    return;
                }
                finally {
                    encoderContext.signalEncoderChange(0);
                }
            }
            --length;
            final CharSequence charSequence2;
            final String encodeToCodewords = encodeToCodewords(charSequence2, 0);
            boolean b;
            if (!encoderContext.hasMoreCharacters()) {
                b = true;
            }
            else {
                b = false;
            }
            int n2;
            if (b && length <= 2) {
                n2 = n;
            }
            else {
                n2 = 0;
            }
            n = n2;
            if (length <= 2) {
                encoderContext.updateSymbolInfo(encoderContext.getCodewordCount() + length);
                n = n2;
                if (encoderContext.getSymbolInfo().getDataCapacity() - encoderContext.getCodewordCount() >= 3) {
                    n = 0;
                    encoderContext.updateSymbolInfo(encoderContext.getCodewordCount() + encodeToCodewords.length());
                }
            }
            if (n != 0) {
                encoderContext.resetSymbolInfo();
                encoderContext.pos -= length;
            }
            else {
                encoderContext.writeCodewords(encodeToCodewords);
            }
            encoderContext.signalEncoderChange(0);
        }
    }
    
    @Override
    public void encode(final EncoderContext encoderContext) {
        final StringBuilder sb = new StringBuilder();
        while (encoderContext.hasMoreCharacters()) {
            encodeChar(encoderContext.getCurrentChar(), sb);
            ++encoderContext.pos;
            if (sb.length() >= 4) {
                encoderContext.writeCodewords(encodeToCodewords(sb, 0));
                sb.delete(0, 4);
                if (HighLevelEncoder.lookAheadTest(encoderContext.getMessage(), encoderContext.pos, this.getEncodingMode()) != this.getEncodingMode()) {
                    encoderContext.signalEncoderChange(0);
                    break;
                }
                continue;
            }
        }
        sb.append('\u001f');
        handleEOD(encoderContext, sb);
    }
    
    @Override
    public int getEncodingMode() {
        return 4;
    }
}
