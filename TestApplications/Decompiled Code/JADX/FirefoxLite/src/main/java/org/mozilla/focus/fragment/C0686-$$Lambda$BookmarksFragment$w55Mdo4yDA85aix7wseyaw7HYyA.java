package org.mozilla.focus.fragment;

import android.arch.lifecycle.Observer;
import java.util.List;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.fragment.-$$Lambda$BookmarksFragment$w55Mdo4yDA85aix7wseyaw7HYyA */
public final /* synthetic */ class C0686-$$Lambda$BookmarksFragment$w55Mdo4yDA85aix7wseyaw7HYyA implements Observer {
    private final /* synthetic */ BookmarksFragment f$0;

    public /* synthetic */ C0686-$$Lambda$BookmarksFragment$w55Mdo4yDA85aix7wseyaw7HYyA(BookmarksFragment bookmarksFragment) {
        this.f$0 = bookmarksFragment;
    }

    public final void onChanged(Object obj) {
        this.f$0.adapter.setData((List) obj);
    }
}
