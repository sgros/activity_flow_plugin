// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.qrcode.decoder;

public enum Mode
{
    ALPHANUMERIC(new int[] { 9, 11, 13 }, 2), 
    BYTE(new int[] { 8, 16, 16 }, 4), 
    ECI(new int[] { 0, 0, 0 }, 7), 
    FNC1_FIRST_POSITION(new int[] { 0, 0, 0 }, 5), 
    FNC1_SECOND_POSITION(new int[] { 0, 0, 0 }, 9), 
    HANZI(new int[] { 8, 10, 12 }, 13), 
    KANJI(new int[] { 8, 10, 12 }, 8), 
    NUMERIC(new int[] { 10, 12, 14 }, 1), 
    STRUCTURED_APPEND(new int[] { 0, 0, 0 }, 3), 
    TERMINATOR(new int[] { 0, 0, 0 }, 0);
    
    private final int bits;
    private final int[] characterCountBitsForVersions;
    
    private Mode(final int[] characterCountBitsForVersions, final int bits) {
        this.characterCountBitsForVersions = characterCountBitsForVersions;
        this.bits = bits;
    }
    
    public static Mode forBits(final int n) {
        Mode mode = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException();
            }
            case 0: {
                mode = Mode.TERMINATOR;
                break;
            }
            case 1: {
                mode = Mode.NUMERIC;
                break;
            }
            case 2: {
                mode = Mode.ALPHANUMERIC;
                break;
            }
            case 3: {
                mode = Mode.STRUCTURED_APPEND;
                break;
            }
            case 4: {
                mode = Mode.BYTE;
                break;
            }
            case 5: {
                mode = Mode.FNC1_FIRST_POSITION;
                break;
            }
            case 7: {
                mode = Mode.ECI;
                break;
            }
            case 8: {
                mode = Mode.KANJI;
                break;
            }
            case 9: {
                mode = Mode.FNC1_SECOND_POSITION;
                break;
            }
            case 13: {
                mode = Mode.HANZI;
                break;
            }
        }
        return mode;
    }
    
    public int getBits() {
        return this.bits;
    }
    
    public int getCharacterCountBits(final Version version) {
        final int versionNumber = version.getVersionNumber();
        int n;
        if (versionNumber <= 9) {
            n = 0;
        }
        else if (versionNumber <= 26) {
            n = 1;
        }
        else {
            n = 2;
        }
        return this.characterCountBitsForVersions[n];
    }
}
