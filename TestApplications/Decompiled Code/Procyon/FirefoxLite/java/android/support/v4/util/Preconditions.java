// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.util;

public class Preconditions
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
    
    public static <T> T checkNotNull(final T t, final Object obj) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(obj));
    }
}
