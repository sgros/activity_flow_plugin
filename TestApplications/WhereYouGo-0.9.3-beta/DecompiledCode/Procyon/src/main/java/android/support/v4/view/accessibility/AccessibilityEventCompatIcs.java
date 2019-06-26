// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityRecord;
import android.view.accessibility.AccessibilityEvent;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(14)
@RequiresApi(14)
class AccessibilityEventCompatIcs
{
    public static void appendRecord(final AccessibilityEvent accessibilityEvent, final Object o) {
        accessibilityEvent.appendRecord((AccessibilityRecord)o);
    }
    
    public static Object getRecord(final AccessibilityEvent accessibilityEvent, final int n) {
        return accessibilityEvent.getRecord(n);
    }
    
    public static int getRecordCount(final AccessibilityEvent accessibilityEvent) {
        return accessibilityEvent.getRecordCount();
    }
    
    public static void setScrollable(final AccessibilityEvent accessibilityEvent, final boolean scrollable) {
        accessibilityEvent.setScrollable(scrollable);
    }
}
