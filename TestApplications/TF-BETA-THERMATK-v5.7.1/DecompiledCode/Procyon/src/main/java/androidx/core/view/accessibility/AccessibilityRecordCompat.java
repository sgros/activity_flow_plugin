// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.view.accessibility;

import android.view.View;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityRecord;

public class AccessibilityRecordCompat
{
    public static void setMaxScrollX(final AccessibilityRecord accessibilityRecord, final int maxScrollX) {
        if (Build$VERSION.SDK_INT >= 15) {
            accessibilityRecord.setMaxScrollX(maxScrollX);
        }
    }
    
    public static void setMaxScrollY(final AccessibilityRecord accessibilityRecord, final int maxScrollY) {
        if (Build$VERSION.SDK_INT >= 15) {
            accessibilityRecord.setMaxScrollY(maxScrollY);
        }
    }
    
    public static void setSource(final AccessibilityRecord accessibilityRecord, final View view, final int n) {
        if (Build$VERSION.SDK_INT >= 16) {
            accessibilityRecord.setSource(view, n);
        }
    }
}
