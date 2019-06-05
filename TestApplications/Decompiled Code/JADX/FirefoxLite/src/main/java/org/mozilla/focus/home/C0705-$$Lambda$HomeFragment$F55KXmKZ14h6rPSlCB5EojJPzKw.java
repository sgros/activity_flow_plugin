package org.mozilla.focus.home;

import org.mozilla.focus.provider.QueryHandler.AsyncUpdateListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$F55KXmKZ14h6rPSlCB5EojJPzKw */
public final /* synthetic */ class C0705-$$Lambda$HomeFragment$F55KXmKZ14h6rPSlCB5EojJPzKw implements AsyncUpdateListener {
    private final /* synthetic */ HomeFragment f$0;

    public /* synthetic */ C0705-$$Lambda$HomeFragment$F55KXmKZ14h6rPSlCB5EojJPzKw(HomeFragment homeFragment) {
        this.f$0 = homeFragment;
    }

    public final void onUpdateComplete(int i) {
        this.f$0.refreshTopSites();
    }
}
