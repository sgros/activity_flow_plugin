// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.util;

public final class Preconditions
{
    public static int checkArgumentNonnegative(final int n) {
        if (n >= 0) {
            return n;
        }
        throw new IllegalArgumentException();
    }
    
    public static <T> T checkNotNull(final T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }
}
