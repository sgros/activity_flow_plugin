// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class PanelFragment extends Fragment implements PanelFragmentStatusListener
{
    protected void closePanel() {
        ((ListPanelDialog)this.getParentFragment()).dismiss();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (this.getParentFragment() instanceof ListPanelDialog) {
            return;
        }
        throw new RuntimeException("PanelFragments needs its parent to be an instance of ListPanelDialog");
    }
    
    public abstract void tryLoadMore();
}
