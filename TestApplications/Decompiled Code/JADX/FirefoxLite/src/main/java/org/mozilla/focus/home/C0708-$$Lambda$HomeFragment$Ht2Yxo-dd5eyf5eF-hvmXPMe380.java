package org.mozilla.focus.home;

import java.util.List;
import org.mozilla.focus.provider.QueryHandler.AsyncQueryListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$Ht2Yxo-dd5eyf5eF-hvmXPMe380 */
public final /* synthetic */ class C0708-$$Lambda$HomeFragment$Ht2Yxo-dd5eyf5eF-hvmXPMe380 implements AsyncQueryListener {
    private final /* synthetic */ HomeFragment f$0;

    public /* synthetic */ C0708-$$Lambda$HomeFragment$Ht2Yxo-dd5eyf5eF-hvmXPMe380(HomeFragment homeFragment) {
        this.f$0 = homeFragment;
    }

    public final void onQueryComplete(List list) {
        HomeFragment.lambda$new$8(this.f$0, list);
    }
}
