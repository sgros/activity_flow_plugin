// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.view.menu;

import android.widget.ListView;

public interface ShowableListMenu
{
    void dismiss();
    
    ListView getListView();
    
    boolean isShowing();
    
    void show();
}
