package org.mozilla.focus.fragment;

import org.mozilla.focus.web.HttpAuthenticationDialogBuilder.CancelListener;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.fragment.-$$Lambda$2vHgiayWabNOvt9IdkeaydjRUNI */
public final /* synthetic */ class C0685-$$Lambda$2vHgiayWabNOvt9IdkeaydjRUNI implements CancelListener {
    private final /* synthetic */ HttpAuthCallback f$0;

    public /* synthetic */ C0685-$$Lambda$2vHgiayWabNOvt9IdkeaydjRUNI(HttpAuthCallback httpAuthCallback) {
        this.f$0 = httpAuthCallback;
    }

    public final void onCancel() {
        this.f$0.cancel();
    }
}
