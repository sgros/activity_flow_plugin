// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import kotlin.ranges.IntRange;

class CharsKt__CharJVMKt
{
    public static final int checkRadix(final int i) {
        if (2 <= i && 36 >= i) {
            return i;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("radix ");
        sb.append(i);
        sb.append(" was not in valid range ");
        sb.append(new IntRange(2, 36));
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static final int digitOf(final char codePoint, final int radix) {
        return Character.digit((int)codePoint, radix);
    }
    
    public static final boolean isWhitespace(final char c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c);
    }
}
