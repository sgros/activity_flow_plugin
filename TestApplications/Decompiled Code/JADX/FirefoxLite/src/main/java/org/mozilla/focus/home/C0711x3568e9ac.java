package org.mozilla.focus.home;

import org.mozilla.focus.provider.QueryHandler.AsyncUpdateListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$UpdateHistoryWrapper$VD7UQe2V-rm661eh-ychjYTSyLE */
public final /* synthetic */ class C0711x3568e9ac implements AsyncUpdateListener {
    private final /* synthetic */ UpdateHistoryWrapper f$0;

    public /* synthetic */ C0711x3568e9ac(UpdateHistoryWrapper updateHistoryWrapper) {
        this.f$0 = updateHistoryWrapper;
    }

    public final void onUpdateComplete(int i) {
        UpdateHistoryWrapper.lambda$accept$0(this.f$0, i);
    }
}
