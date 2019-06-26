package androidx.core.view;

import android.os.Build.VERSION;
import android.view.WindowInsets;

public class WindowInsetsCompat {
    private final Object mInsets;

    private WindowInsetsCompat(Object obj) {
        this.mInsets = obj;
    }

    public int getSystemWindowInsetLeft() {
        return VERSION.SDK_INT >= 20 ? ((WindowInsets) this.mInsets).getSystemWindowInsetLeft() : 0;
    }

    public int getSystemWindowInsetTop() {
        return VERSION.SDK_INT >= 20 ? ((WindowInsets) this.mInsets).getSystemWindowInsetTop() : 0;
    }

    public int getSystemWindowInsetRight() {
        return VERSION.SDK_INT >= 20 ? ((WindowInsets) this.mInsets).getSystemWindowInsetRight() : 0;
    }

    public int getSystemWindowInsetBottom() {
        return VERSION.SDK_INT >= 20 ? ((WindowInsets) this.mInsets).getSystemWindowInsetBottom() : 0;
    }

    public boolean isConsumed() {
        return VERSION.SDK_INT >= 21 ? ((WindowInsets) this.mInsets).isConsumed() : false;
    }

    public WindowInsetsCompat replaceSystemWindowInsets(int i, int i2, int i3, int i4) {
        return VERSION.SDK_INT >= 20 ? new WindowInsetsCompat(((WindowInsets) this.mInsets).replaceSystemWindowInsets(i, i2, i3, i4)) : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || WindowInsetsCompat.class != obj.getClass()) {
            return false;
        }
        WindowInsetsCompat windowInsetsCompat = (WindowInsetsCompat) obj;
        Object obj2 = this.mInsets;
        if (obj2 != null) {
            z = obj2.equals(windowInsetsCompat.mInsets);
        } else if (windowInsetsCompat.mInsets != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        Object obj = this.mInsets;
        return obj == null ? 0 : obj.hashCode();
    }

    static WindowInsetsCompat wrap(Object obj) {
        return obj == null ? null : new WindowInsetsCompat(obj);
    }

    static Object unwrap(WindowInsetsCompat windowInsetsCompat) {
        return windowInsetsCompat == null ? null : windowInsetsCompat.mInsets;
    }
}
