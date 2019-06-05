// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned.rss.expanded.decoders;

final class DecodedChar extends DecodedObject
{
    static final char FNC1 = '$';
    private final char value;
    
    DecodedChar(final int n, final char c) {
        super(n);
        this.value = c;
    }
    
    char getValue() {
        return this.value;
    }
    
    boolean isFNC1() {
        return this.value == '$';
    }
}
