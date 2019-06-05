// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.internal;

import android.graphics.PorterDuff$Mode;
import android.support.v4.view.ViewCompat;
import android.view.View;

public class ViewUtils
{
    public static boolean isLayoutRtl(final View view) {
        final int layoutDirection = ViewCompat.getLayoutDirection(view);
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        return b;
    }
    
    public static PorterDuff$Mode parseTintMode(final int n, final PorterDuff$Mode porterDuff$Mode) {
        if (n == 3) {
            return PorterDuff$Mode.SRC_OVER;
        }
        if (n == 5) {
            return PorterDuff$Mode.SRC_IN;
        }
        if (n == 9) {
            return PorterDuff$Mode.SRC_ATOP;
        }
        switch (n) {
            default: {
                return porterDuff$Mode;
            }
            case 16: {
                return PorterDuff$Mode.ADD;
            }
            case 15: {
                return PorterDuff$Mode.SCREEN;
            }
            case 14: {
                return PorterDuff$Mode.MULTIPLY;
            }
        }
    }
}
