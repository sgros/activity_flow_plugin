package org.mozilla.focus.web;

import android.content.Context;
import org.mozilla.focus.utils.DebugUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.rocket.C0769R;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.web.-$$Lambda$WebViewProvider$sInVC8y955eZKzWXUqprXxIrcMA */
public final /* synthetic */ class C0545-$$Lambda$WebViewProvider$sInVC8y955eZKzWXUqprXxIrcMA implements Runnable {
    private final /* synthetic */ Context f$0;

    public /* synthetic */ C0545-$$Lambda$WebViewProvider$sInVC8y955eZKzWXUqprXxIrcMA(Context context) {
        this.f$0 = context;
    }

    public final void run() {
        Settings.updatePrefString(this.f$0, this.f$0.getString(C0769R.string.pref_key_webview_version), DebugUtils.parseWebViewVersion(WebViewProvider.userAgentString));
    }
}
