// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.widget.ListView;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(19)
@RequiresApi(19)
class ListViewCompatKitKat
{
    static void scrollListBy(final ListView listView, final int n) {
        listView.scrollListBy(n);
    }
}
