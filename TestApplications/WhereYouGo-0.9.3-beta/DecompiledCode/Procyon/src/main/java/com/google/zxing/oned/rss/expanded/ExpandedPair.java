// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned.rss.expanded;

import java.io.Serializable;
import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;

final class ExpandedPair
{
    private final FinderPattern finderPattern;
    private final DataCharacter leftChar;
    private final boolean mayBeLast;
    private final DataCharacter rightChar;
    
    ExpandedPair(final DataCharacter leftChar, final DataCharacter rightChar, final FinderPattern finderPattern, final boolean mayBeLast) {
        this.leftChar = leftChar;
        this.rightChar = rightChar;
        this.finderPattern = finderPattern;
        this.mayBeLast = mayBeLast;
    }
    
    private static boolean equalsOrNull(final Object o, final Object obj) {
        boolean equals;
        if (o == null) {
            equals = (obj == null);
        }
        else {
            equals = o.equals(obj);
        }
        return equals;
    }
    
    private static int hashNotNull(final Object o) {
        int hashCode;
        if (o == null) {
            hashCode = 0;
        }
        else {
            hashCode = o.hashCode();
        }
        return hashCode;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (!(o instanceof ExpandedPair)) {
            b2 = b;
        }
        else {
            final ExpandedPair expandedPair = (ExpandedPair)o;
            b2 = b;
            if (equalsOrNull(this.leftChar, expandedPair.leftChar)) {
                b2 = b;
                if (equalsOrNull(this.rightChar, expandedPair.rightChar)) {
                    b2 = b;
                    if (equalsOrNull(this.finderPattern, expandedPair.finderPattern)) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    FinderPattern getFinderPattern() {
        return this.finderPattern;
    }
    
    DataCharacter getLeftChar() {
        return this.leftChar;
    }
    
    DataCharacter getRightChar() {
        return this.rightChar;
    }
    
    @Override
    public int hashCode() {
        return hashNotNull(this.leftChar) ^ hashNotNull(this.rightChar) ^ hashNotNull(this.finderPattern);
    }
    
    boolean mayBeLast() {
        return this.mayBeLast;
    }
    
    public boolean mustBeLast() {
        return this.rightChar == null;
    }
    
    @Override
    public String toString() {
        final StringBuilder append = new StringBuilder("[ ").append(this.leftChar).append(" , ").append(this.rightChar).append(" : ");
        Serializable value;
        if (this.finderPattern == null) {
            value = "null";
        }
        else {
            value = this.finderPattern.getValue();
        }
        return append.append(value).append(" ]").toString();
    }
}
