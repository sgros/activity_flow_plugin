// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public final class TabTrayViewModel extends ViewModel
{
    private MutableLiveData<Boolean> hasPrivateTab;
    
    public TabTrayViewModel() {
        this.hasPrivateTab = new MutableLiveData<Boolean>();
    }
    
    public final MutableLiveData<Boolean> hasPrivateTab() {
        return this.hasPrivateTab;
    }
}
