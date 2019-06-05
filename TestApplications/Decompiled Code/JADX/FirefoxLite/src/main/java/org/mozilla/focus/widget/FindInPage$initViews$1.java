package org.mozilla.focus.widget;

import android.webkit.WebView;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabViewEngineSession;

/* compiled from: FindInPage.kt */
final class FindInPage$initViews$1 extends Lambda implements Function0<WebView> {
    final /* synthetic */ FindInPage this$0;

    FindInPage$initViews$1(FindInPage findInPage) {
        this.this$0 = findInPage;
        super(0);
    }

    public final WebView invoke() {
        Session access$getSession$p = this.this$0.session;
        if (access$getSession$p != null) {
            TabViewEngineSession engineSession = access$getSession$p.getEngineSession();
            if (engineSession != null) {
                TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    if (tabView != null) {
                        return (WebView) tabView;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type android.webkit.WebView");
                }
            }
        }
        return null;
    }
}
