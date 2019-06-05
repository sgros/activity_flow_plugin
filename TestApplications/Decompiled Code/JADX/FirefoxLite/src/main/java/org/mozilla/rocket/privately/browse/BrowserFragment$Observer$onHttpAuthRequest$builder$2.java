package org.mozilla.rocket.privately.browse;

import org.mozilla.focus.web.HttpAuthenticationDialogBuilder.CancelListener;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;

/* compiled from: BrowserFragment.kt */
final class BrowserFragment$Observer$onHttpAuthRequest$builder$2 implements CancelListener {
    final /* synthetic */ HttpAuthCallback $callback;

    BrowserFragment$Observer$onHttpAuthRequest$builder$2(HttpAuthCallback httpAuthCallback) {
        this.$callback = httpAuthCallback;
    }

    public final void onCancel() {
        this.$callback.cancel();
    }
}
