// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.os;

import android.os.Build$VERSION;

public class BuildCompat
{
    @Deprecated
    public static boolean isAtLeastNMR1() {
        return Build$VERSION.SDK_INT >= 25;
    }
}
