package org.mozilla.focus.urlinput;

import android.arch.lifecycle.Observer;
import java.util.ArrayList;
import org.mozilla.rocket.urlinput.QuickSearch;
import org.mozilla.rocket.urlinput.QuickSearchAdapter;

/* compiled from: UrlInputFragment.kt */
final class UrlInputFragment$initQuickSearch$1<T> implements Observer<ArrayList<QuickSearch>> {
    final /* synthetic */ QuickSearchAdapter $quickSearchAdapter;

    UrlInputFragment$initQuickSearch$1(QuickSearchAdapter quickSearchAdapter) {
        this.$quickSearchAdapter = quickSearchAdapter;
    }

    public final void onChanged(ArrayList<QuickSearch> arrayList) {
        this.$quickSearchAdapter.submitList(arrayList);
    }
}
