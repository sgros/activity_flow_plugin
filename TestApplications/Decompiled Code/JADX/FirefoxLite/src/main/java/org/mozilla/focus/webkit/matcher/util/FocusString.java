package org.mozilla.focus.webkit.matcher.util;

public abstract class FocusString {
    final int offsetEnd;
    final int offsetStart;
    protected final String string;

    private static class ForwardString extends FocusString {
        /* Access modifiers changed, original: protected */
        public boolean isReversed() {
            return false;
        }

        public ForwardString(String str, int i, int i2) {
            super(str, i, i2);
        }

        public char charAt(int i) {
            if (i <= length()) {
                return this.string.charAt(i + this.offsetStart);
            }
            throw new StringIndexOutOfBoundsException();
        }

        public FocusString substring(int i) {
            return new ForwardString(this.string, this.offsetStart + i, this.offsetEnd);
        }
    }

    private static class ReverseString extends FocusString {
        /* Access modifiers changed, original: protected */
        public boolean isReversed() {
            return true;
        }

        public ReverseString(String str, int i, int i2) {
            super(str, i, i2);
        }

        public char charAt(int i) {
            if (i <= length()) {
                return this.string.charAt(((length() - 1) - i) + this.offsetStart);
            }
            throw new StringIndexOutOfBoundsException();
        }

        public FocusString substring(int i) {
            return new ReverseString(this.string, this.offsetStart, this.offsetEnd - i);
        }
    }

    public abstract char charAt(int i);

    public abstract boolean isReversed();

    public abstract FocusString substring(int i);

    private FocusString(String str, int i, int i2) {
        this.string = str;
        this.offsetStart = i;
        this.offsetEnd = i2;
        if (i > i2 || i < 0 || i2 < 0) {
            throw new StringIndexOutOfBoundsException("Cannot create negative-length String");
        }
    }

    public static FocusString create(String str) {
        return new ForwardString(str, 0, str.length());
    }

    public int length() {
        return this.offsetEnd - this.offsetStart;
    }

    public FocusString reverse() {
        if (isReversed()) {
            return new ForwardString(this.string, this.offsetStart, this.offsetEnd);
        }
        return new ReverseString(this.string, this.offsetStart, this.offsetEnd);
    }
}
