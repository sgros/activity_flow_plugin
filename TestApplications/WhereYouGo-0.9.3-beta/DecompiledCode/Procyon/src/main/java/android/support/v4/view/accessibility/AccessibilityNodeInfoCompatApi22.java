// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view.accessibility;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(22)
@RequiresApi(22)
class AccessibilityNodeInfoCompatApi22
{
    public static Object getTraversalAfter(final Object o) {
        return ((AccessibilityNodeInfo)o).getTraversalAfter();
    }
    
    public static Object getTraversalBefore(final Object o) {
        return ((AccessibilityNodeInfo)o).getTraversalBefore();
    }
    
    public static void setTraversalAfter(final Object o, final View traversalAfter) {
        ((AccessibilityNodeInfo)o).setTraversalAfter(traversalAfter);
    }
    
    public static void setTraversalAfter(final Object o, final View view, final int n) {
        ((AccessibilityNodeInfo)o).setTraversalAfter(view, n);
    }
    
    public static void setTraversalBefore(final Object o, final View traversalBefore) {
        ((AccessibilityNodeInfo)o).setTraversalBefore(traversalBefore);
    }
    
    public static void setTraversalBefore(final Object o, final View view, final int n) {
        ((AccessibilityNodeInfo)o).setTraversalBefore(view, n);
    }
}
