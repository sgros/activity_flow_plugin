package org.mozilla.focus.fragment;

import android.os.Bundle;
import android.support.p001v4.app.Fragment;

public abstract class PanelFragment extends Fragment implements PanelFragmentStatusListener {
    public abstract void tryLoadMore();

    /* Access modifiers changed, original: protected */
    public void closePanel() {
        ((ListPanelDialog) getParentFragment()).dismiss();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!(getParentFragment() instanceof ListPanelDialog)) {
            throw new RuntimeException("PanelFragments needs its parent to be an instance of ListPanelDialog");
        }
    }
}
