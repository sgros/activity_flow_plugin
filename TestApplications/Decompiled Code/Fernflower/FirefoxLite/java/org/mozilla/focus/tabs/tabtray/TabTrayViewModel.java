package org.mozilla.focus.tabs.tabtray;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public final class TabTrayViewModel extends ViewModel {
   private MutableLiveData hasPrivateTab = new MutableLiveData();

   public final MutableLiveData hasPrivateTab() {
      return this.hasPrivateTab;
   }
}
