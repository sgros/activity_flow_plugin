package org.mozilla.rocket.tabs;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.browser.session.Download;
import mozilla.components.browser.session.Session.FindResult;
import mozilla.components.browser.session.Session.SecurityInfo;
import mozilla.components.support.base.observer.Consumable;
import org.mozilla.rocket.tabs.TabView.FullscreenCallback;
import org.mozilla.rocket.tabs.TabView.HitTarget;
import org.mozilla.rocket.tabs.TabViewEngineSession.Observer;

/* compiled from: TabViewEngineObserver.kt */
public final class TabViewEngineObserver implements Observer {
    private final Session session;

    public TabViewEngineObserver(Session session) {
        Intrinsics.checkParameterIsNotNull(session, "session");
        this.session = session;
    }

    public final Session getSession() {
        return this.session;
    }

    public void onTitleChange(String str) {
        Intrinsics.checkParameterIsNotNull(str, "title");
        this.session.setTitle(str);
    }

    public void onLoadingStateChange(boolean z) {
        this.session.setLoading(z);
    }

    public void onNavigationStateChange(Boolean bool, Boolean bool2) {
        if (bool != null) {
            bool.booleanValue();
            this.session.setCanGoBack(bool.booleanValue());
        }
        if (bool2 != null) {
            bool2.booleanValue();
            this.session.setCanGoForward(bool2.booleanValue());
        }
    }

    public void onSecurityChange(boolean z, String str, String str2) {
        Session session = this.session;
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        session.setSecurityInfo(new SecurityInfo(z, str, str2));
    }

    public void onLocationChange(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        this.session.setUrl(str);
    }

    public void onProgress(int i) {
        this.session.setProgress(i);
    }

    public void onReceivedIcon(Bitmap bitmap) {
        this.session.setFavicon(bitmap);
        this.session.notifyObservers(new TabViewEngineObserver$onReceivedIcon$1(bitmap));
    }

    public void onLongPress(HitTarget hitTarget) {
        Intrinsics.checkParameterIsNotNull(hitTarget, "hitTarget");
        this.session.notifyObservers(new TabViewEngineObserver$onLongPress$1(this, hitTarget));
    }

    public void onEnterFullScreen(FullscreenCallback fullscreenCallback, View view) {
        Intrinsics.checkParameterIsNotNull(fullscreenCallback, "callback");
        this.session.notifyObservers(new TabViewEngineObserver$onEnterFullScreen$1(fullscreenCallback, view));
    }

    public void onExitFullScreen() {
        this.session.notifyObservers(TabViewEngineObserver$onExitFullScreen$1.INSTANCE);
    }

    public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
        Intrinsics.checkParameterIsNotNull(str, "origin");
        this.session.notifyObservers(new TabViewEngineObserver$onGeolocationPermissionsShowPrompt$1(str, callback));
    }

    public void onFindResult(int i, int i2, boolean z) {
        Session session = this.session;
        session.setFindResults(CollectionsKt___CollectionsKt.plus(session.getFindResults(), new FindResult(i, i2, z)));
    }

    public void onExternalResource(String str, String str2, Long l, String str3, String str4, String str5) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        String str6 = Environment.DIRECTORY_DOWNLOADS;
        Intrinsics.checkExpressionValueIsNotNull(str6, "Environment.DIRECTORY_DOWNLOADS");
        this.session.setDownload(Consumable.Companion.from(new Download(str, str2, str3, l, str5, str6)));
    }
}
