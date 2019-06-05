package android.support.p001v4.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityEvent;

/* renamed from: android.support.v4.view.accessibility.AccessibilityEventCompat */
public final class AccessibilityEventCompat {
    public static void setContentChangeTypes(AccessibilityEvent accessibilityEvent, int i) {
        if (VERSION.SDK_INT >= 19) {
            accessibilityEvent.setContentChangeTypes(i);
        }
    }

    public static int getContentChangeTypes(AccessibilityEvent accessibilityEvent) {
        return VERSION.SDK_INT >= 19 ? accessibilityEvent.getContentChangeTypes() : 0;
    }
}
