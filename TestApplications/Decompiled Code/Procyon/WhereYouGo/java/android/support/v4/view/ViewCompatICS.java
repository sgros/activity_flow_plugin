// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view;

import android.view.View$AccessibilityDelegate;
import android.support.annotation.Nullable;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;
import android.view.View;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(14)
@RequiresApi(14)
class ViewCompatICS
{
    public static boolean canScrollHorizontally(final View view, final int n) {
        return view.canScrollHorizontally(n);
    }
    
    public static boolean canScrollVertically(final View view, final int n) {
        return view.canScrollVertically(n);
    }
    
    public static void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        view.onInitializeAccessibilityEvent(accessibilityEvent);
    }
    
    public static void onInitializeAccessibilityNodeInfo(final View view, final Object o) {
        view.onInitializeAccessibilityNodeInfo((AccessibilityNodeInfo)o);
    }
    
    public static void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        view.onPopulateAccessibilityEvent(accessibilityEvent);
    }
    
    public static void setAccessibilityDelegate(final View view, @Nullable final Object o) {
        view.setAccessibilityDelegate((View$AccessibilityDelegate)o);
    }
    
    public static void setFitsSystemWindows(final View view, final boolean fitsSystemWindows) {
        view.setFitsSystemWindows(fitsSystemWindows);
    }
}
