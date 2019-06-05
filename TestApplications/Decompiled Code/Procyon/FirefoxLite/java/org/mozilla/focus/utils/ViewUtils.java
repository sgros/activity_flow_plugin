// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.os.Build$VERSION;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.graphics.Rect;
import android.view.Window;
import android.app.Activity;

public class ViewUtils
{
    public static void exitImmersiveMode(final int systemUiVisibility, final Activity activity) {
        if (activity == null) {
            return;
        }
        final Window window = activity.getWindow();
        window.clearFlags(128);
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        activity.setRequestedOrientation(1);
    }
    
    public static int getStatusBarHeight(final Activity activity) {
        final Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }
    
    public static void hideKeyboard(final View view) {
        ((InputMethodManager)view.getContext().getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 1);
    }
    
    public static void showKeyboard(final View view) {
        ((InputMethodManager)view.getContext().getSystemService("input_method")).showSoftInput(view, 1);
    }
    
    public static int switchToImmersiveMode(final Activity activity) {
        if (activity == null) {
            return -1;
        }
        final Window window = activity.getWindow();
        final int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        window.addFlags(128);
        window.getDecorView().setSystemUiVisibility(5894);
        activity.setRequestedOrientation(4);
        return systemUiVisibility;
    }
    
    public static void updateStatusBarStyle(final boolean b, final Window window) {
        if (Build$VERSION.SDK_INT < 23) {
            return;
        }
        final int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        int systemUiVisibility2;
        if (!b) {
            systemUiVisibility2 = (systemUiVisibility & 0xFFFFDFFF);
        }
        else {
            systemUiVisibility2 = (systemUiVisibility | 0x2000);
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility2);
    }
}
