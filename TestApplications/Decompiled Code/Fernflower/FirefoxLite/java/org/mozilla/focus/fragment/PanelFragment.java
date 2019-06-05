package org.mozilla.focus.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class PanelFragment extends Fragment implements PanelFragmentStatusListener {
   protected void closePanel() {
      ((ListPanelDialog)this.getParentFragment()).dismiss();
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (!(this.getParentFragment() instanceof ListPanelDialog)) {
         throw new RuntimeException("PanelFragments needs its parent to be an instance of ListPanelDialog");
      }
   }

   public abstract void tryLoadMore();
}
