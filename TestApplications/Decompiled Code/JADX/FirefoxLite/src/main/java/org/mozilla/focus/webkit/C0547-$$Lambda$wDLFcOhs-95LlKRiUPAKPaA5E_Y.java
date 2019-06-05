package org.mozilla.focus.webkit;

import android.webkit.WebView.FindListener;
import org.mozilla.rocket.tabs.TabView;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.webkit.-$$Lambda$wDLFcOhs-95LlKRiUPAKPaA5E_Y */
public final /* synthetic */ class C0547-$$Lambda$wDLFcOhs-95LlKRiUPAKPaA5E_Y implements FindListener {
    private final /* synthetic */ TabView.FindListener f$0;

    public /* synthetic */ C0547-$$Lambda$wDLFcOhs-95LlKRiUPAKPaA5E_Y(TabView.FindListener findListener) {
        this.f$0 = findListener;
    }

    public final void onFindResultReceived(int i, int i2, boolean z) {
        this.f$0.onFindResultReceived(i, i2, z);
    }
}
