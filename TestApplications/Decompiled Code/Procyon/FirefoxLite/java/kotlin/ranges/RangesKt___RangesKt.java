// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.ranges;

class RangesKt___RangesKt extends RangesKt__RangesKt
{
    public static final int coerceAtLeast(final int n, final int n2) {
        int n3 = n;
        if (n < n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final long coerceAtLeast(final long n, final long n2) {
        long n3 = n;
        if (n < n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final int coerceAtMost(final int n, final int n2) {
        int n3 = n;
        if (n > n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final long coerceAtMost(final long n, final long n2) {
        long n3 = n;
        if (n > n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final int coerceIn(final int n, final int i, final int j) {
        if (i > j) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: maximum ");
            sb.append(j);
            sb.append(" is less than minimum ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < i) {
            return i;
        }
        if (n > j) {
            return j;
        }
        return n;
    }
    
    public static final IntProgression downTo(final int n, final int n2) {
        return IntProgression.Companion.fromClosedRange(n, n2, -1);
    }
    
    public static final IntRange until(final int n, final int n2) {
        if (n2 <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(n, n2 - 1);
    }
}
