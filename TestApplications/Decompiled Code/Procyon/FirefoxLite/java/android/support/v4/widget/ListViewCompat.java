// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.view.View;
import android.os.Build$VERSION;
import android.widget.ListView;

public final class ListViewCompat
{
    public static void scrollListBy(final ListView listView, final int n) {
        if (Build$VERSION.SDK_INT >= 19) {
            listView.scrollListBy(n);
        }
        else {
            final int firstVisiblePosition = listView.getFirstVisiblePosition();
            if (firstVisiblePosition == -1) {
                return;
            }
            final View child = listView.getChildAt(0);
            if (child == null) {
                return;
            }
            listView.setSelectionFromTop(firstVisiblePosition, child.getTop() - n);
        }
    }
}
