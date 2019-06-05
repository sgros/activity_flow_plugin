// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs;

import android.graphics.Bitmap;
import android.webkit.GeolocationPermissions$Callback;
import java.util.Collection;
import java.util.List;
import mozilla.components.browser.session.Download;
import mozilla.components.support.base.observer.Consumable;
import android.os.Environment;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;

public final class TabViewEngineObserver implements Observer
{
    private final Session session;
    
    public TabViewEngineObserver(final Session session) {
        Intrinsics.checkParameterIsNotNull(session, "session");
        this.session = session;
    }
    
    public final Session getSession() {
        return this.session;
    }
    
    @Override
    public void onEnterFullScreen(final TabView.FullscreenCallback fullscreenCallback, final View view) {
        Intrinsics.checkParameterIsNotNull(fullscreenCallback, "callback");
        this.session.notifyObservers((Function1<? super Session.Observer, Unit>)new TabViewEngineObserver$onEnterFullScreen.TabViewEngineObserver$onEnterFullScreen$1(fullscreenCallback, view));
    }
    
    @Override
    public void onExitFullScreen() {
        this.session.notifyObservers((Function1<? super Session.Observer, Unit>)TabViewEngineObserver$onExitFullScreen.TabViewEngineObserver$onExitFullScreen$1.INSTANCE);
    }
    
    @Override
    public void onExternalResource(final String s, final String s2, final Long n, final String s3, String directory_DOWNLOADS, final String s4) {
        Intrinsics.checkParameterIsNotNull(s, "url");
        directory_DOWNLOADS = Environment.DIRECTORY_DOWNLOADS;
        Intrinsics.checkExpressionValueIsNotNull(directory_DOWNLOADS, "Environment.DIRECTORY_DOWNLOADS");
        this.session.setDownload(Consumable.Companion.from(new Download(s, s2, s3, n, s4, directory_DOWNLOADS)));
    }
    
    @Override
    public void onFindResult(final int n, final int n2, final boolean b) {
        final Session session = this.session;
        session.setFindResults(CollectionsKt___CollectionsKt.plus((Collection<? extends mozilla.components.browser.session.Session.FindResult>)session.getFindResults(), new mozilla.components.browser.session.Session.FindResult(n, n2, b)));
    }
    
    @Override
    public void onGeolocationPermissionsShowPrompt(final String s, final GeolocationPermissions$Callback geolocationPermissions$Callback) {
        Intrinsics.checkParameterIsNotNull(s, "origin");
        this.session.notifyObservers((Function1<? super Session.Observer, Unit>)new TabViewEngineObserver$onGeolocationPermissionsShowPrompt.TabViewEngineObserver$onGeolocationPermissionsShowPrompt$1(s, geolocationPermissions$Callback));
    }
    
    @Override
    public void onLoadingStateChange(final boolean loading) {
        this.session.setLoading(loading);
    }
    
    @Override
    public void onLocationChange(final String url) {
        Intrinsics.checkParameterIsNotNull(url, "url");
        this.session.setUrl(url);
    }
    
    @Override
    public void onLongPress(final TabView.HitTarget hitTarget) {
        Intrinsics.checkParameterIsNotNull(hitTarget, "hitTarget");
        this.session.notifyObservers((Function1<? super Session.Observer, Unit>)new TabViewEngineObserver$onLongPress.TabViewEngineObserver$onLongPress$1(this, hitTarget));
    }
    
    @Override
    public void onNavigationStateChange(final Boolean b, final Boolean b2) {
        if (b != null) {
            b;
            this.session.setCanGoBack(b);
        }
        if (b2 != null) {
            b2;
            this.session.setCanGoForward(b2);
        }
    }
    
    @Override
    public void onProgress(final int progress) {
        this.session.setProgress(progress);
    }
    
    @Override
    public void onReceivedIcon(final Bitmap favicon) {
        this.session.setFavicon(favicon);
        this.session.notifyObservers((Function1<? super Session.Observer, Unit>)new TabViewEngineObserver$onReceivedIcon.TabViewEngineObserver$onReceivedIcon$1(favicon));
    }
    
    @Override
    public void onSecurityChange(final boolean b, String s, String s2) {
        final Session session = this.session;
        if (s == null) {
            s = "";
        }
        if (s2 == null) {
            s2 = "";
        }
        session.setSecurityInfo(new mozilla.components.browser.session.Session.SecurityInfo(b, s, s2));
    }
    
    @Override
    public void onTitleChange(final String title) {
        Intrinsics.checkParameterIsNotNull(title, "title");
        this.session.setTitle(title);
    }
}
