package org.mozilla.focus.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

public class ViewUtils {
    public static void showKeyboard(View view) {
        ((InputMethodManager) view.getContext().getSystemService("input_method")).showSoftInput(view, 1);
    }

    public static void hideKeyboard(View view) {
        ((InputMethodManager) view.getContext().getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 1);
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    public static int switchToImmersiveMode(Activity activity) {
        if (activity == null) {
            return -1;
        }
        Window window = activity.getWindow();
        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        window.addFlags(128);
        window.getDecorView().setSystemUiVisibility(5894);
        activity.setRequestedOrientation(4);
        return systemUiVisibility;
    }

    public static void exitImmersiveMode(int i, Activity activity) {
        if (activity != null) {
            Window window = activity.getWindow();
            window.clearFlags(128);
            window.getDecorView().setSystemUiVisibility(i);
            activity.setRequestedOrientation(1);
        }
    }

    public static void updateStatusBarStyle(boolean z, Window window) {
        if (VERSION.SDK_INT >= 23) {
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(!z ? systemUiVisibility & -8193 : systemUiVisibility | 8192);
        }
    }
}
