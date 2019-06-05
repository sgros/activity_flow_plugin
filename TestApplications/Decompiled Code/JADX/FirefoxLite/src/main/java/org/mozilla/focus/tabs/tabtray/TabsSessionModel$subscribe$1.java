package org.mozilla.focus.tabs.tabtray;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.tabs.tabtray.TabTrayContract.Model.Observer;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;
import org.mozilla.rocket.tabs.TabViewEngineSession;

/* compiled from: TabsSessionModel.kt */
public final class TabsSessionModel$subscribe$1 extends SessionModelObserver {
    final /* synthetic */ Observer $observer;
    final /* synthetic */ TabsSessionModel this$0;

    public boolean handleExternalUrl(String str) {
        return false;
    }

    public void onHttpAuthRequest(HttpAuthCallback httpAuthCallback, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(httpAuthCallback, "callback");
    }

    public boolean onShowFileChooser(TabViewEngineSession tabViewEngineSession, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
        return false;
    }

    public void updateFailingUrl(String str, boolean z) {
    }

    TabsSessionModel$subscribe$1(TabsSessionModel tabsSessionModel, Observer observer) {
        this.this$0 = tabsSessionModel;
        this.$observer = observer;
    }

    public void onTabModelChanged$app_focusWebkitRelease(Session session) {
        Intrinsics.checkParameterIsNotNull(session, "session");
        this.$observer.onTabUpdate(session);
    }

    public void onSessionCountChanged(int i) {
        this.$observer.onUpdate(this.this$0.sessionManager.getTabs());
    }
}
