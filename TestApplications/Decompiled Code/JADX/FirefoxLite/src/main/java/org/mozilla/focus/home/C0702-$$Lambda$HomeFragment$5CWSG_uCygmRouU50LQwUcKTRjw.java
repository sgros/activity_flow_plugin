package org.mozilla.focus.home;

import org.mozilla.focus.history.model.Site;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$5CWSG_uCygmRouU50LQwUcKTRjw */
public final /* synthetic */ class C0702-$$Lambda$HomeFragment$5CWSG_uCygmRouU50LQwUcKTRjw implements OnRemovedListener {
    public static final /* synthetic */ C0702-$$Lambda$HomeFragment$5CWSG_uCygmRouU50LQwUcKTRjw INSTANCE = new C0702-$$Lambda$HomeFragment$5CWSG_uCygmRouU50LQwUcKTRjw();

    private /* synthetic */ C0702-$$Lambda$HomeFragment$5CWSG_uCygmRouU50LQwUcKTRjw() {
    }

    public final void onRemoved(Site site) {
        HomeFragment.lambda$mergePinSiteToTopSites$11(site);
    }
}
