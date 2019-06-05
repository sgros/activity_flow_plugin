// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit.matcher.util;

public abstract class FocusString
{
    final int offsetEnd;
    final int offsetStart;
    protected final String string;
    
    private FocusString(final String string, final int offsetStart, final int offsetEnd) {
        this.string = string;
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
        if (offsetStart <= offsetEnd && offsetStart >= 0 && offsetEnd >= 0) {
            return;
        }
        throw new StringIndexOutOfBoundsException("Cannot create negative-length String");
    }
    
    public static FocusString create(final String s) {
        return new ForwardString(s, 0, s.length());
    }
    
    public abstract char charAt(final int p0);
    
    protected abstract boolean isReversed();
    
    public int length() {
        return this.offsetEnd - this.offsetStart;
    }
    
    public FocusString reverse() {
        if (this.isReversed()) {
            return new ForwardString(this.string, this.offsetStart, this.offsetEnd);
        }
        return new ReverseString(this.string, this.offsetStart, this.offsetEnd);
    }
    
    public abstract FocusString substring(final int p0);
    
    private static class ForwardString extends FocusString
    {
        public ForwardString(final String s, final int n, final int n2) {
            super(s, n, n2, null);
        }
        
        @Override
        public char charAt(final int n) {
            if (n <= this.length()) {
                return this.string.charAt(n + this.offsetStart);
            }
            throw new StringIndexOutOfBoundsException();
        }
        
        @Override
        protected boolean isReversed() {
            return false;
        }
        
        @Override
        public FocusString substring(final int n) {
            return new ForwardString(this.string, this.offsetStart + n, this.offsetEnd);
        }
    }
    
    private static class ReverseString extends FocusString
    {
        public ReverseString(final String s, final int n, final int n2) {
            super(s, n, n2, null);
        }
        
        @Override
        public char charAt(final int n) {
            if (n <= this.length()) {
                return this.string.charAt(this.length() - 1 - n + this.offsetStart);
            }
            throw new StringIndexOutOfBoundsException();
        }
        
        @Override
        protected boolean isReversed() {
            return true;
        }
        
        @Override
        public FocusString substring(final int n) {
            return new ReverseString(this.string, this.offsetStart, this.offsetEnd - n);
        }
    }
}
