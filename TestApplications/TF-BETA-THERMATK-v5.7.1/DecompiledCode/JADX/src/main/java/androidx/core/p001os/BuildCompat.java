package androidx.core.p001os;

import android.os.Build.VERSION;

/* renamed from: androidx.core.os.BuildCompat */
public class BuildCompat {
    @Deprecated
    public static boolean isAtLeastNMR1() {
        return VERSION.SDK_INT >= 25;
    }
}
