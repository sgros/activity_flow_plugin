package org.mozilla.rocket.privately.browse;

import org.mozilla.focus.web.HttpAuthenticationDialogBuilder.OkListener;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;

/* compiled from: BrowserFragment.kt */
final class BrowserFragment$Observer$onHttpAuthRequest$builder$1 implements OkListener {
    final /* synthetic */ HttpAuthCallback $callback;

    BrowserFragment$Observer$onHttpAuthRequest$builder$1(HttpAuthCallback httpAuthCallback) {
        this.$callback = httpAuthCallback;
    }

    public final void onOk(String str, String str2, String str3, String str4) {
        this.$callback.proceed(str3, str4);
    }
}
