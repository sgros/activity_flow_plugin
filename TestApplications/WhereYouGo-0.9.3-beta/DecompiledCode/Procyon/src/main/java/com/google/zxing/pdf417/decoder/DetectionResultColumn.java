// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.pdf417.decoder;

import java.util.Formatter;

class DetectionResultColumn
{
    private static final int MAX_NEARBY_DISTANCE = 5;
    private final BoundingBox boundingBox;
    private final Codeword[] codewords;
    
    DetectionResultColumn(final BoundingBox boundingBox) {
        this.boundingBox = new BoundingBox(boundingBox);
        this.codewords = new Codeword[boundingBox.getMaxY() - boundingBox.getMinY() + 1];
    }
    
    final BoundingBox getBoundingBox() {
        return this.boundingBox;
    }
    
    final Codeword getCodeword(final int n) {
        return this.codewords[this.imageRowToCodewordIndex(n)];
    }
    
    final Codeword getCodewordNearby(final int n) {
        Codeword codeword = this.getCodeword(n);
        if (codeword == null) {
            for (int i = 1; i < 5; ++i) {
                final int n2 = this.imageRowToCodewordIndex(n) - i;
                if (n2 >= 0) {
                    codeword = this.codewords[n2];
                    if (codeword != null) {
                        return codeword;
                    }
                }
                final int n3 = this.imageRowToCodewordIndex(n) + i;
                if (n3 < this.codewords.length) {
                    codeword = this.codewords[n3];
                    if (codeword != null) {
                        return codeword;
                    }
                }
            }
            codeword = null;
        }
        return codeword;
    }
    
    final Codeword[] getCodewords() {
        return this.codewords;
    }
    
    final int imageRowToCodewordIndex(final int n) {
        return n - this.boundingBox.getMinY();
    }
    
    final void setCodeword(final int n, final Codeword codeword) {
        this.codewords[this.imageRowToCodewordIndex(n)] = codeword;
    }
    
    @Override
    public String toString() {
        final Formatter formatter = new Formatter();
        final Codeword[] codewords = this.codewords;
        final int length = codewords.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            final Codeword codeword = codewords[i];
            if (codeword == null) {
                final int n2 = n + 1;
                formatter.format("%3d:    |   %n", n);
                n = n2;
            }
            else {
                final int n3 = n + 1;
                formatter.format("%3d: %3d|%3d%n", n, codeword.getRowNumber(), codeword.getValue());
                n = n3;
            }
            ++i;
        }
        final String string = formatter.toString();
        formatter.close();
        return string;
    }
}
