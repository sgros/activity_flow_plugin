// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.widget;

import androidx.core.view.ViewCompat;
import android.util.Log;
import android.graphics.Rect;
import android.view.View;
import android.os.Build$VERSION;
import java.lang.reflect.Method;

public class ViewUtils
{
    private static Method sComputeFitSystemWindowsMethod;
    
    static {
        if (Build$VERSION.SDK_INT >= 18) {
            try {
                ViewUtils.sComputeFitSystemWindowsMethod = View.class.getDeclaredMethod("computeFitSystemWindows", Rect.class, Rect.class);
                if (!ViewUtils.sComputeFitSystemWindowsMethod.isAccessible()) {
                    ViewUtils.sComputeFitSystemWindowsMethod.setAccessible(true);
                }
            }
            catch (NoSuchMethodException ex) {
                Log.d("ViewUtils", "Could not find method computeFitSystemWindows. Oh well.");
            }
        }
    }
    
    public static void computeFitSystemWindows(final View obj, final Rect rect, final Rect rect2) {
        final Method sComputeFitSystemWindowsMethod = ViewUtils.sComputeFitSystemWindowsMethod;
        if (sComputeFitSystemWindowsMethod != null) {
            try {
                sComputeFitSystemWindowsMethod.invoke(obj, rect, rect2);
            }
            catch (Exception ex) {
                Log.d("ViewUtils", "Could not invoke computeFitSystemWindows", (Throwable)ex);
            }
        }
    }
    
    public static boolean isLayoutRtl(final View view) {
        final int layoutDirection = ViewCompat.getLayoutDirection(view);
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        return b;
    }
}
