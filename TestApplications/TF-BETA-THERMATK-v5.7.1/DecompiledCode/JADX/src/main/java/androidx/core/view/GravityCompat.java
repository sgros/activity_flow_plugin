package androidx.core.view;

import android.os.Build.VERSION;
import android.view.Gravity;

public final class GravityCompat {
    public static int getAbsoluteGravity(int i, int i2) {
        return VERSION.SDK_INT >= 17 ? Gravity.getAbsoluteGravity(i, i2) : i & -8388609;
    }
}
