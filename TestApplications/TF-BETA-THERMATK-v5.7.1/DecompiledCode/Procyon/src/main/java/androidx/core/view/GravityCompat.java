// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.view;

import android.view.Gravity;
import android.os.Build$VERSION;

public final class GravityCompat
{
    public static int getAbsoluteGravity(final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 17) {
            return Gravity.getAbsoluteGravity(n, n2);
        }
        return n & 0xFF7FFFFF;
    }
}
