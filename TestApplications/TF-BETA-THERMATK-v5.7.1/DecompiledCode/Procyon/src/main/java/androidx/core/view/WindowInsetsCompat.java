// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.view;

import android.view.WindowInsets;
import android.os.Build$VERSION;

public class WindowInsetsCompat
{
    private final Object mInsets;
    
    private WindowInsetsCompat(final Object mInsets) {
        this.mInsets = mInsets;
    }
    
    static Object unwrap(final WindowInsetsCompat windowInsetsCompat) {
        Object mInsets;
        if (windowInsetsCompat == null) {
            mInsets = null;
        }
        else {
            mInsets = windowInsetsCompat.mInsets;
        }
        return mInsets;
    }
    
    static WindowInsetsCompat wrap(final Object o) {
        WindowInsetsCompat windowInsetsCompat;
        if (o == null) {
            windowInsetsCompat = null;
        }
        else {
            windowInsetsCompat = new WindowInsetsCompat(o);
        }
        return windowInsetsCompat;
    }
    
    @Override
    public boolean equals(Object mInsets) {
        boolean equals = true;
        if (this == mInsets) {
            return true;
        }
        if (mInsets != null && WindowInsetsCompat.class == mInsets.getClass()) {
            final WindowInsetsCompat windowInsetsCompat = (WindowInsetsCompat)mInsets;
            mInsets = this.mInsets;
            if (mInsets == null) {
                if (windowInsetsCompat.mInsets != null) {
                    equals = false;
                }
            }
            else {
                equals = mInsets.equals(windowInsetsCompat.mInsets);
            }
            return equals;
        }
        return false;
    }
    
    public int getSystemWindowInsetBottom() {
        if (Build$VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetBottom();
        }
        return 0;
    }
    
    public int getSystemWindowInsetLeft() {
        if (Build$VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetLeft();
        }
        return 0;
    }
    
    public int getSystemWindowInsetRight() {
        if (Build$VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetRight();
        }
        return 0;
    }
    
    public int getSystemWindowInsetTop() {
        if (Build$VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetTop();
        }
        return 0;
    }
    
    @Override
    public int hashCode() {
        final Object mInsets = this.mInsets;
        int hashCode;
        if (mInsets == null) {
            hashCode = 0;
        }
        else {
            hashCode = mInsets.hashCode();
        }
        return hashCode;
    }
    
    public boolean isConsumed() {
        return Build$VERSION.SDK_INT >= 21 && ((WindowInsets)this.mInsets).isConsumed();
    }
    
    public WindowInsetsCompat replaceSystemWindowInsets(final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 20) {
            return new WindowInsetsCompat(((WindowInsets)this.mInsets).replaceSystemWindowInsets(n, n2, n3, n4));
        }
        return null;
    }
}
