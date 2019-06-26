// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.datamatrix.encoder;

final class TextEncoder extends C40Encoder
{
    @Override
    int encodeChar(final char c, final StringBuilder sb) {
        int n = 1;
        if (c == ' ') {
            sb.append('\u0003');
        }
        else if (c >= '0' && c <= '9') {
            sb.append((char)(c - '0' + 4));
        }
        else if (c >= 'a' && c <= 'z') {
            sb.append((char)(c - 'a' + 14));
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
        else if (c == '`') {
            sb.append('\u0002');
            sb.append((char)(c - '`'));
            n = 2;
        }
        else if (c >= 'A' && c <= 'Z') {
            sb.append('\u0002');
            sb.append((char)(c - 'A' + 1));
            n = 2;
        }
        else if (c >= '{' && c <= '\u007f') {
            sb.append('\u0002');
            sb.append((char)(c - '{' + 27));
            n = 2;
        }
        else if (c >= '\u0080') {
            sb.append("\u0001\u001e");
            n = this.encodeChar((char)(c - '\u0080'), sb) + 2;
        }
        else {
            HighLevelEncoder.illegalCharacter(c);
            n = -1;
        }
        return n;
    }
    
    @Override
    public int getEncodingMode() {
        return 2;
    }
}
