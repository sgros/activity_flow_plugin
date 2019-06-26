// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(9)
@RequiresApi(9)
class ListViewCompatGingerbread
{
    static void scrollListBy(final ListView listView, final int n) {
        final int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (firstVisiblePosition != -1) {
            final View child = listView.getChildAt(0);
            if (child != null) {
                listView.setSelectionFromTop(firstVisiblePosition, child.getTop() - n);
            }
        }
    }
}
