// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.datamatrix.encoder;

final class X12Encoder extends C40Encoder
{
    @Override
    public void encode(final EncoderContext encoderContext) {
        final StringBuilder sb = new StringBuilder();
        while (encoderContext.hasMoreCharacters()) {
            final char currentChar = encoderContext.getCurrentChar();
            ++encoderContext.pos;
            this.encodeChar(currentChar, sb);
            if (sb.length() % 3 == 0) {
                C40Encoder.writeNextTriplet(encoderContext, sb);
                final int lookAheadTest = HighLevelEncoder.lookAheadTest(encoderContext.getMessage(), encoderContext.pos, this.getEncodingMode());
                if (lookAheadTest != this.getEncodingMode()) {
                    encoderContext.signalEncoderChange(lookAheadTest);
                    break;
                }
                continue;
            }
        }
        this.handleEOD(encoderContext, sb);
    }
    
    @Override
    int encodeChar(final char c, final StringBuilder sb) {
        if (c == '\r') {
            sb.append('\0');
        }
        else if (c == '*') {
            sb.append('\u0001');
        }
        else if (c == '>') {
            sb.append('\u0002');
        }
        else if (c == ' ') {
            sb.append('\u0003');
        }
        else if (c >= '0' && c <= '9') {
            sb.append((char)(c - '0' + 4));
        }
        else if (c >= 'A' && c <= 'Z') {
            sb.append((char)(c - 'A' + 14));
        }
        else {
            HighLevelEncoder.illegalCharacter(c);
        }
        return 1;
    }
    
    @Override
    public int getEncodingMode() {
        return 3;
    }
    
    @Override
    void handleEOD(final EncoderContext encoderContext, final StringBuilder sb) {
        encoderContext.updateSymbolInfo();
        final int n = encoderContext.getSymbolInfo().getDataCapacity() - encoderContext.getCodewordCount();
        encoderContext.pos -= sb.length();
        if (encoderContext.getRemainingCharacters() > 1 || n > 1 || encoderContext.getRemainingCharacters() != n) {
            encoderContext.writeCodeword('\u00fe');
        }
        if (encoderContext.getNewEncoding() < 0) {
            encoderContext.signalEncoderChange(0);
        }
    }
}
