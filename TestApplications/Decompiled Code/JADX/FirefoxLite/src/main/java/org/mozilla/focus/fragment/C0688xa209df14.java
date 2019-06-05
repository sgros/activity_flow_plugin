package org.mozilla.focus.fragment;

import org.mozilla.focus.web.HttpAuthenticationDialogBuilder.OkListener;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.fragment.-$$Lambda$BrowserFragment$SessionObserver$3ofKWXqzOJQXPDqF9Nkl-b0cv-s */
public final /* synthetic */ class C0688xa209df14 implements OkListener {
    private final /* synthetic */ HttpAuthCallback f$0;

    public /* synthetic */ C0688xa209df14(HttpAuthCallback httpAuthCallback) {
        this.f$0 = httpAuthCallback;
    }

    public final void onOk(String str, String str2, String str3, String str4) {
        this.f$0.proceed(str3, str4);
    }
}
