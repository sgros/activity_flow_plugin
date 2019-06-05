// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

public final class MathUtils
{
    public static float dist(final float n, final float n2, final float n3, final float n4) {
        return (float)Math.hypot(n3 - n, n4 - n2);
    }
    
    public static float distanceToFurthestCorner(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        return max(dist(n, n2, n3, n4), dist(n, n2, n5, n4), dist(n, n2, n5, n6), dist(n, n2, n3, n6));
    }
    
    public static boolean geq(final float n, final float n2, final float n3) {
        return n + n3 >= n2;
    }
    
    public static float lerp(final float n, final float n2, final float n3) {
        return (1.0f - n3) * n + n3 * n2;
    }
    
    private static float max(float n, final float n2, final float n3, final float n4) {
        if (n <= n2 || n <= n3 || n <= n4) {
            if (n2 > n3 && n2 > n4) {
                n = n2;
            }
            else if (n3 > n4) {
                n = n3;
            }
            else {
                n = n4;
            }
        }
        return n;
    }
}
