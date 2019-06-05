// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.datamatrix.encoder;

final class ASCIIEncoder implements Encoder
{
    private static char encodeASCIIDigits(final char c, final char c2) {
        if (HighLevelEncoder.isDigit(c) && HighLevelEncoder.isDigit(c2)) {
            return (char)((c - '0') * 10 + (c2 - '0') + 130);
        }
        throw new IllegalArgumentException("not digits: " + c + c2);
    }
    
    @Override
    public void encode(final EncoderContext encoderContext) {
        if (HighLevelEncoder.determineConsecutiveDigitCount(encoderContext.getMessage(), encoderContext.pos) >= 2) {
            encoderContext.writeCodeword(encodeASCIIDigits(encoderContext.getMessage().charAt(encoderContext.pos), encoderContext.getMessage().charAt(encoderContext.pos + 1)));
            encoderContext.pos += 2;
        }
        else {
            final char currentChar = encoderContext.getCurrentChar();
            final int lookAheadTest = HighLevelEncoder.lookAheadTest(encoderContext.getMessage(), encoderContext.pos, this.getEncodingMode());
            if (lookAheadTest != this.getEncodingMode()) {
                switch (lookAheadTest) {
                    default: {
                        throw new IllegalStateException("Illegal mode: " + lookAheadTest);
                    }
                    case 5: {
                        encoderContext.writeCodeword('\u00e7');
                        encoderContext.signalEncoderChange(5);
                        break;
                    }
                    case 1: {
                        encoderContext.writeCodeword('\u00e6');
                        encoderContext.signalEncoderChange(1);
                        break;
                    }
                    case 3: {
                        encoderContext.writeCodeword('\u00ee');
                        encoderContext.signalEncoderChange(3);
                        break;
                    }
                    case 2: {
                        encoderContext.writeCodeword('\u00ef');
                        encoderContext.signalEncoderChange(2);
                        break;
                    }
                    case 4: {
                        encoderContext.writeCodeword('\u00f0');
                        encoderContext.signalEncoderChange(4);
                        break;
                    }
                }
            }
            else if (HighLevelEncoder.isExtendedASCII(currentChar)) {
                encoderContext.writeCodeword('\u00eb');
                encoderContext.writeCodeword((char)(currentChar - '\u0080' + 1));
                ++encoderContext.pos;
            }
            else {
                encoderContext.writeCodeword((char)(currentChar + '\u0001'));
                ++encoderContext.pos;
            }
        }
    }
    
    @Override
    public int getEncodingMode() {
        return 0;
    }
}
