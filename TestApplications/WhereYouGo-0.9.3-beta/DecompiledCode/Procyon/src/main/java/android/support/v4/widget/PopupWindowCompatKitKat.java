// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.view.View;
import android.widget.PopupWindow;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(19)
@RequiresApi(19)
class PopupWindowCompatKitKat
{
    public static void showAsDropDown(final PopupWindow popupWindow, final View view, final int n, final int n2, final int n3) {
        popupWindow.showAsDropDown(view, n, n2, n3);
    }
}
