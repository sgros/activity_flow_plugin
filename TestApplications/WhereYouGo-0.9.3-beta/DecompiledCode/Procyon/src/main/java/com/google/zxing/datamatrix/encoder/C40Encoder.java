// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.datamatrix.encoder;

class C40Encoder implements Encoder
{
    private int backtrackOneCharacter(final EncoderContext encoderContext, final StringBuilder sb, final StringBuilder sb2, int encodeChar) {
        final int length = sb.length();
        sb.delete(length - encodeChar, length);
        --encoderContext.pos;
        encodeChar = this.encodeChar(encoderContext.getCurrentChar(), sb2);
        encoderContext.resetSymbolInfo();
        return encodeChar;
    }
    
    private static String encodeToCodewords(final CharSequence charSequence, int n) {
        n = charSequence.charAt(n) * '\u0640' + charSequence.charAt(n + 1) * '(' + charSequence.charAt(n + 2) + 1;
        return new String(new char[] { (char)(n / 256), (char)(n % 256) });
    }
    
    static void writeNextTriplet(final EncoderContext encoderContext, final StringBuilder sb) {
        encoderContext.writeCodewords(encodeToCodewords(sb, 0));
        sb.delete(0, 3);
    }
    
    @Override
    public void encode(final EncoderContext encoderContext) {
        final StringBuilder sb = new StringBuilder();
        while (encoderContext.hasMoreCharacters()) {
            final char currentChar = encoderContext.getCurrentChar();
            ++encoderContext.pos;
            final int encodeChar = this.encodeChar(currentChar, sb);
            final int n = encoderContext.getCodewordCount() + (sb.length() / 3 << 1);
            encoderContext.updateSymbolInfo(n);
            final int n2 = encoderContext.getSymbolInfo().getDataCapacity() - n;
            if (!encoderContext.hasMoreCharacters()) {
                final StringBuilder sb2 = new StringBuilder();
                int n3 = encodeChar;
                Label_0133: {
                    if (sb.length() % 3 == 2) {
                        if (n2 >= 2) {
                            n3 = encodeChar;
                            if (n2 <= 2) {
                                break Label_0133;
                            }
                        }
                        n3 = this.backtrackOneCharacter(encoderContext, sb, sb2, encodeChar);
                    }
                }
                while (sb.length() % 3 == 1 && ((n3 <= 3 && n2 != 1) || n3 > 3)) {
                    n3 = this.backtrackOneCharacter(encoderContext, sb, sb2, n3);
                }
                break;
            }
            if (sb.length() % 3 != 0) {
                continue;
            }
            final int lookAheadTest = HighLevelEncoder.lookAheadTest(encoderContext.getMessage(), encoderContext.pos, this.getEncodingMode());
            if (lookAheadTest != this.getEncodingMode()) {
                encoderContext.signalEncoderChange(lookAheadTest);
                break;
            }
        }
        this.handleEOD(encoderContext, sb);
    }
    
    int encodeChar(final char c, final StringBuilder sb) {
        int n = 1;
        if (c == ' ') {
            sb.append('\u0003');
        }
        else if (c >= '0' && c <= '9') {
            sb.append((char)(c - '0' + 4));
        }
        else if (c >= 'A' && c <= 'Z') {
            sb.append((char)(c - 'A' + 14));
        }
        else if (c >= '\0' && c <= '\u001f') {
            sb.append('\0');
            sb.append(c);
            n = 2;
        }
        else if (c >= '!' && c <= '/') {
            sb.append('\u0001');
            sb.append((char)(c - '!'));
            n = 2;
        }
        else if (c >= ':' && c <= '@') {
            sb.append('\u0001');
            sb.append((char)(c - ':' + 15));
            n = 2;
        }
        else if (c >= '[' && c <= '_') {
            sb.append('\u0001');
            sb.append((char)(c - '[' + 22));
            n = 2;
        }
        else if (c >= '`' && c <= '\u007f') {
            sb.append('\u0002');
            sb.append((char)(c - '`'));
            n = 2;
        }
        else {
            if (c < '\u0080') {
                throw new IllegalArgumentException("Illegal character: " + c);
            }
            sb.append("\u0001\u001e");
            n = this.encodeChar((char)(c - '\u0080'), sb) + 2;
        }
        return n;
    }
    
    @Override
    public int getEncodingMode() {
        return 1;
    }
    
    void handleEOD(final EncoderContext encoderContext, final StringBuilder sb) {
        final int n = sb.length() / 3;
        final int n2 = sb.length() % 3;
        final int n3 = encoderContext.getCodewordCount() + (n << 1);
        encoderContext.updateSymbolInfo(n3);
        final int n4 = encoderContext.getSymbolInfo().getDataCapacity() - n3;
        if (n2 == 2) {
            sb.append('\0');
            while (sb.length() >= 3) {
                writeNextTriplet(encoderContext, sb);
            }
            if (encoderContext.hasMoreCharacters()) {
                encoderContext.writeCodeword('\u00fe');
            }
        }
        else if (n4 == 1 && n2 == 1) {
            while (sb.length() >= 3) {
                writeNextTriplet(encoderContext, sb);
            }
            if (encoderContext.hasMoreCharacters()) {
                encoderContext.writeCodeword('\u00fe');
            }
            --encoderContext.pos;
        }
        else {
            if (n2 != 0) {
                throw new IllegalStateException("Unexpected case. Please report!");
            }
            while (sb.length() >= 3) {
                writeNextTriplet(encoderContext, sb);
            }
            if (n4 > 0 || encoderContext.hasMoreCharacters()) {
                encoderContext.writeCodeword('\u00fe');
            }
        }
        encoderContext.signalEncoderChange(0);
    }
}
