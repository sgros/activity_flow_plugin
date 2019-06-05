package org.mozilla.focus.tabs.tabtray;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/* compiled from: TabTrayViewModel.kt */
public final class TabTrayViewModel extends ViewModel {
    private MutableLiveData<Boolean> hasPrivateTab = new MutableLiveData();

    public final MutableLiveData<Boolean> hasPrivateTab() {
        return this.hasPrivateTab;
    }
}
