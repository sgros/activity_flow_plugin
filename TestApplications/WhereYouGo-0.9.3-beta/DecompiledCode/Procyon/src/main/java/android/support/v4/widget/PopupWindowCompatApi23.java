// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.widget.PopupWindow;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(23)
@RequiresApi(23)
class PopupWindowCompatApi23
{
    static boolean getOverlapAnchor(final PopupWindow popupWindow) {
        return popupWindow.getOverlapAnchor();
    }
    
    static int getWindowLayoutType(final PopupWindow popupWindow) {
        return popupWindow.getWindowLayoutType();
    }
    
    static void setOverlapAnchor(final PopupWindow popupWindow, final boolean overlapAnchor) {
        popupWindow.setOverlapAnchor(overlapAnchor);
    }
    
    static void setWindowLayoutType(final PopupWindow popupWindow, final int windowLayoutType) {
        popupWindow.setWindowLayoutType(windowLayoutType);
    }
}
