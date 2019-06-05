package org.mozilla.focus.tabs.tabtray;

import org.mozilla.rocket.tabs.Session;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.tabs.tabtray.-$$Lambda$TabTrayFragment$0IDPD452c9ZrZB9tm2xPMJgiQtc */
public final /* synthetic */ class C0516-$$Lambda$TabTrayFragment$0IDPD452c9ZrZB9tm2xPMJgiQtc implements Runnable {
    private final /* synthetic */ TabTrayFragment f$0;
    private final /* synthetic */ Session f$1;

    public /* synthetic */ C0516-$$Lambda$TabTrayFragment$0IDPD452c9ZrZB9tm2xPMJgiQtc(TabTrayFragment tabTrayFragment, Session session) {
        this.f$0 = tabTrayFragment;
        this.f$1 = session;
    }

    public final void run() {
        TabTrayFragment.lambda$refreshData$1(this.f$0, this.f$1);
    }
}
