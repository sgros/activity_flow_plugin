// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view.accessibility;

import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityEvent;

public final class AccessibilityEventCompat
{
    public static int getContentChangeTypes(final AccessibilityEvent accessibilityEvent) {
        if (Build$VERSION.SDK_INT >= 19) {
            return accessibilityEvent.getContentChangeTypes();
        }
        return 0;
    }
    
    public static void setContentChangeTypes(final AccessibilityEvent accessibilityEvent, final int contentChangeTypes) {
        if (Build$VERSION.SDK_INT >= 19) {
            accessibilityEvent.setContentChangeTypes(contentChangeTypes);
        }
    }
}
