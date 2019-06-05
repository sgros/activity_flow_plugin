// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned.rss;

final class Pair extends DataCharacter
{
    private int count;
    private final FinderPattern finderPattern;
    
    Pair(final int n, final int n2, final FinderPattern finderPattern) {
        super(n, n2);
        this.finderPattern = finderPattern;
    }
    
    int getCount() {
        return this.count;
    }
    
    FinderPattern getFinderPattern() {
        return this.finderPattern;
    }
    
    void incrementCount() {
        ++this.count;
    }
}
