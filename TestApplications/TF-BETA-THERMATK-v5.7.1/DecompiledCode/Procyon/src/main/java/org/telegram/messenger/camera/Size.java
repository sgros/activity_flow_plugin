// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.camera;

public final class Size
{
    public final int mHeight;
    public final int mWidth;
    
    public Size(final int mWidth, final int mHeight) {
        this.mWidth = mWidth;
        this.mHeight = mHeight;
    }
    
    private static NumberFormatException invalidSize(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid Size: \"");
        sb.append(str);
        sb.append("\"");
        throw new NumberFormatException(sb.toString());
    }
    
    public static Size parseSize(final String s) throws NumberFormatException {
        int endIndex;
        if ((endIndex = s.indexOf(42)) < 0) {
            endIndex = s.indexOf(120);
        }
        if (endIndex >= 0) {
            try {
                return new Size(Integer.parseInt(s.substring(0, endIndex)), Integer.parseInt(s.substring(endIndex + 1)));
            }
            catch (NumberFormatException ex) {
                invalidSize(s);
                throw null;
            }
        }
        invalidSize(s);
        throw null;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        boolean b2 = b;
        if (o instanceof Size) {
            final Size size = (Size)o;
            b2 = b;
            if (this.mWidth == size.mWidth) {
                b2 = b;
                if (this.mHeight == size.mHeight) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public int getHeight() {
        return this.mHeight;
    }
    
    public int getWidth() {
        return this.mWidth;
    }
    
    @Override
    public int hashCode() {
        final int mHeight = this.mHeight;
        final int mWidth = this.mWidth;
        return mHeight ^ (mWidth >>> 16 | mWidth << 16);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mWidth);
        sb.append("x");
        sb.append(this.mHeight);
        return sb.toString();
    }
}
