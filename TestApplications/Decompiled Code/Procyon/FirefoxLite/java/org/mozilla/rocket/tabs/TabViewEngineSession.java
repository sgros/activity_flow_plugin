// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs;

import mozilla.components.concept.engine.EngineSession;
import android.webkit.CookieManager;
import org.mozilla.rocket.tabs.web.Download;
import android.webkit.WebChromeClient$FileChooserParams;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.graphics.Bitmap;
import android.webkit.GeolocationPermissions$Callback;
import android.os.Message;
import java.util.List;
import kotlin.jvm.functions.Function2;
import org.mozilla.rocket.tabs.web.DownloadCallback;
import android.arch.lifecycle.LifecycleOwner;
import kotlin.jvm.functions.Function1;
import kotlin.Unit;
import android.view.ViewParent;
import android.view.View;
import android.view.ViewGroup;
import kotlin.TypeCastException;
import mozilla.components.support.base.observer.ObserverRegistry;
import kotlin.jvm.internal.Intrinsics;
import android.os.Bundle;
import mozilla.components.support.base.observer.Observable;

public final class TabViewEngineSession implements Observable<Observer>
{
    private final Observable<Observer> delegate;
    private Client engineSessionClient;
    private TabView engineView;
    private Bundle webViewState;
    private WindowClient windowClient;
    
    public TabViewEngineSession() {
        this(null, 1, null);
    }
    
    public TabViewEngineSession(final Observable<Observer> delegate) {
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        this.delegate = delegate;
    }
    
    public final void destroy$feature_tabs_release() {
        this.unregisterObservers();
        this.detach();
        final TabView tabView = this.getTabView();
        if (tabView != null) {
            tabView.destroy();
        }
    }
    
    public final void detach() {
        final TabView tabView = this.getTabView();
        if (tabView != null) {
            final View view = tabView.getView();
            if (view != null) {
                final ViewParent parent = view.getParent();
                if (parent != null) {
                    if (parent == null) {
                        throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup");
                    }
                    ((ViewGroup)parent).removeView(view);
                }
            }
        }
    }
    
    public final Client getEngineSessionClient() {
        return this.engineSessionClient;
    }
    
    public final TabView getTabView() {
        return this.engineView;
    }
    
    public final Bundle getWebViewState() {
        return this.webViewState;
    }
    
    public final WindowClient getWindowClient() {
        return this.windowClient;
    }
    
    public final Unit goBack() {
        final TabView tabView = this.getTabView();
        Unit instance;
        if (tabView != null) {
            tabView.goBack();
            instance = Unit.INSTANCE;
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    public final Unit goForward() {
        final TabView tabView = this.getTabView();
        Unit instance;
        if (tabView != null) {
            tabView.goForward();
            instance = Unit.INSTANCE;
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    @Override
    public void notifyObservers(final Function1<? super Observer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.delegate.notifyObservers(function1);
    }
    
    @Override
    public void register(final Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.delegate.register(observer);
    }
    
    @Override
    public void register(final Observer observer, final LifecycleOwner lifecycleOwner, final boolean b) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        Intrinsics.checkParameterIsNotNull(lifecycleOwner, "owner");
        this.delegate.register(observer, lifecycleOwner, b);
    }
    
    public final Unit reload() {
        final TabView tabView = this.getTabView();
        Unit instance;
        if (tabView != null) {
            tabView.reload();
            instance = Unit.INSTANCE;
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    public final void saveState() {
        if (this.webViewState == null) {
            this.webViewState = new Bundle();
        }
        final TabView tabView = this.getTabView();
        if (tabView != null) {
            tabView.saveViewState(this.webViewState);
        }
    }
    
    public final void setEngineSessionClient(final Client engineSessionClient) {
        this.engineSessionClient = engineSessionClient;
    }
    
    public final void setTabView(final TabView engineView) {
        if (engineView != null) {
            engineView.setViewClient(new ViewClient(this));
        }
        if (engineView != null) {
            engineView.setChromeClient(new ChromeClient(this));
        }
        if (engineView != null) {
            engineView.setFindListener((TabView.FindListener)new FindListener(this));
        }
        if (engineView != null) {
            engineView.setDownloadCallback(new DownloadCallback(this));
        }
        final TabView engineView2 = this.engineView;
        if (engineView2 != null) {
            engineView2.setViewClient(null);
        }
        final TabView engineView3 = this.engineView;
        if (engineView3 != null) {
            engineView3.setChromeClient(null);
        }
        final TabView engineView4 = this.engineView;
        if (engineView4 != null) {
            engineView4.setFindListener(null);
        }
        final TabView engineView5 = this.engineView;
        if (engineView5 != null) {
            engineView5.setDownloadCallback(null);
        }
        this.engineView = engineView;
    }
    
    public final void setWebViewState(final Bundle webViewState) {
        this.webViewState = webViewState;
    }
    
    public final void setWindowClient(final WindowClient windowClient) {
        this.windowClient = windowClient;
    }
    
    public final Unit stopLoading() {
        final TabView tabView = this.getTabView();
        Unit instance;
        if (tabView != null) {
            tabView.stopLoading();
            instance = Unit.INSTANCE;
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    @Override
    public void unregister(final Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.delegate.unregister(observer);
    }
    
    @Override
    public void unregisterObservers() {
        this.delegate.unregisterObservers();
    }
    
    @Override
    public <R> List<Function1<R, Boolean>> wrapConsumers(final Function2<? super Observer, ? super R, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        return this.delegate.wrapConsumers(function2);
    }
    
    public static final class ChromeClient extends TabChromeClient
    {
        private final TabViewEngineSession es;
        
        public ChromeClient(final TabViewEngineSession es) {
            Intrinsics.checkParameterIsNotNull(es, "es");
            this.es = es;
        }
        
        @Override
        public void onCloseWindow(final TabView tabView) {
            final WindowClient windowClient = this.es.getWindowClient();
            if (windowClient != null) {
                windowClient.onCloseWindow(this.es);
            }
        }
        
        @Override
        public boolean onCreateWindow(final boolean b, final boolean b2, final Message message) {
            final WindowClient windowClient = this.es.getWindowClient();
            return windowClient != null && windowClient.onCreateWindow(b, b2, message);
        }
        
        @Override
        public void onEnterFullScreen(final TabView.FullscreenCallback fullscreenCallback, final View view) {
            Intrinsics.checkParameterIsNotNull(fullscreenCallback, "callback");
            this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ChromeClient$onEnterFullScreen.TabViewEngineSession$ChromeClient$onEnterFullScreen$1(fullscreenCallback, view));
        }
        
        @Override
        public void onExitFullScreen() {
            this.es.notifyObservers((Function1<? super Observer, Unit>)TabViewEngineSession$ChromeClient$onExitFullScreen.TabViewEngineSession$ChromeClient$onExitFullScreen$1.INSTANCE);
        }
        
        @Override
        public void onGeolocationPermissionsShowPrompt(final String s, final GeolocationPermissions$Callback geolocationPermissions$Callback) {
            Intrinsics.checkParameterIsNotNull(s, "origin");
            this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ChromeClient$onGeolocationPermissionsShowPrompt.TabViewEngineSession$ChromeClient$onGeolocationPermissionsShowPrompt$1(s, geolocationPermissions$Callback));
        }
        
        @Override
        public void onLongPress(final TabView.HitTarget hitTarget) {
            Intrinsics.checkParameterIsNotNull(hitTarget, "hitTarget");
            this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ChromeClient$onLongPress.TabViewEngineSession$ChromeClient$onLongPress$1(hitTarget));
        }
        
        @Override
        public void onProgressChanged(final int n) {
            this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ChromeClient$onProgressChanged.TabViewEngineSession$ChromeClient$onProgressChanged$1(n));
        }
        
        @Override
        public void onReceivedIcon(final TabView tabView, final Bitmap bitmap) {
            Intrinsics.checkParameterIsNotNull(tabView, "view");
            this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ChromeClient$onReceivedIcon.TabViewEngineSession$ChromeClient$onReceivedIcon$1(bitmap));
        }
        
        @Override
        public void onReceivedTitle(final TabView tabView, String url) {
            Intrinsics.checkParameterIsNotNull(tabView, "view");
            if (url != null) {
                this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ChromeClient$onReceivedTitle.TabViewEngineSession$ChromeClient$onReceivedTitle$1(url));
            }
            url = tabView.getUrl();
            if (url != null) {
                this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ChromeClient$onReceivedTitle$2.TabViewEngineSession$ChromeClient$onReceivedTitle$2$1(url));
            }
            this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ChromeClient$onReceivedTitle.TabViewEngineSession$ChromeClient$onReceivedTitle$3(tabView));
        }
        
        @Override
        public boolean onShowFileChooser(final TabView tabView, final ValueCallback<Uri[]> valueCallback, final WebChromeClient$FileChooserParams webChromeClient$FileChooserParams) {
            Intrinsics.checkParameterIsNotNull(tabView, "tabView");
            final Client engineSessionClient = this.es.getEngineSessionClient();
            return engineSessionClient != null && engineSessionClient.onShowFileChooser(this.es, valueCallback, webChromeClient$FileChooserParams);
        }
    }
    
    public interface Client
    {
        boolean handleExternalUrl(final String p0);
        
        void onHttpAuthRequest(final TabViewClient.HttpAuthCallback p0, final String p1, final String p2);
        
        boolean onShowFileChooser(final TabViewEngineSession p0, final ValueCallback<Uri[]> p1, final WebChromeClient$FileChooserParams p2);
        
        void updateFailingUrl(final String p0, final boolean p1);
    }
    
    public static final class DownloadCallback implements org.mozilla.rocket.tabs.web.DownloadCallback
    {
        private final TabViewEngineSession es;
        
        public DownloadCallback(final TabViewEngineSession es) {
            Intrinsics.checkParameterIsNotNull(es, "es");
            this.es = es;
        }
        
        @Override
        public void onDownloadStart(final Download download) {
            Intrinsics.checkParameterIsNotNull(download, "download");
            this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$DownloadCallback$onDownloadStart.TabViewEngineSession$DownloadCallback$onDownloadStart$1(download, CookieManager.getInstance().getCookie(download.getUrl())));
        }
    }
    
    public static final class FindListener implements TabView.FindListener
    {
        private final TabViewEngineSession es;
        
        public FindListener(final TabViewEngineSession es) {
            Intrinsics.checkParameterIsNotNull(es, "es");
            this.es = es;
        }
        
        @Override
        public void onFindResultReceived(final int n, final int n2, final boolean b) {
            this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$FindListener$onFindResultReceived.TabViewEngineSession$FindListener$onFindResultReceived$1(n, n2, b));
        }
    }
    
    public interface Observer extends EngineSession.Observer
    {
        void onEnterFullScreen(final TabView.FullscreenCallback p0, final View p1);
        
        void onExitFullScreen();
        
        void onGeolocationPermissionsShowPrompt(final String p0, final GeolocationPermissions$Callback p1);
        
        void onLongPress(final TabView.HitTarget p0);
        
        void onReceivedIcon(final Bitmap p0);
    }
    
    public static final class ViewClient extends TabViewClient
    {
        private final TabViewEngineSession es;
        
        public ViewClient(final TabViewEngineSession es) {
            Intrinsics.checkParameterIsNotNull(es, "es");
            this.es = es;
        }
        
        @Override
        public boolean handleExternalUrl(final String s) {
            final Client engineSessionClient = this.es.getEngineSessionClient();
            return engineSessionClient != null && engineSessionClient.handleExternalUrl(s);
        }
        
        @Override
        public void onHttpAuthRequest(final HttpAuthCallback httpAuthCallback, final String s, final String s2) {
            Intrinsics.checkParameterIsNotNull(httpAuthCallback, "callback");
            final Client engineSessionClient = this.es.getEngineSessionClient();
            if (engineSessionClient != null) {
                engineSessionClient.onHttpAuthRequest(httpAuthCallback, s, s2);
            }
        }
        
        @Override
        public void onPageFinished(final boolean b) {
            this.es.notifyObservers((Function1<? super Observer, Unit>)TabViewEngineSession$ViewClient$onPageFinished.TabViewEngineSession$ViewClient$onPageFinished$1.INSTANCE);
            this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ViewClient$onPageFinished.TabViewEngineSession$ViewClient$onPageFinished$2(b));
            final TabView tabView = this.es.getTabView();
            if (tabView != null) {
                this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ViewClient$onPageFinished$3.TabViewEngineSession$ViewClient$onPageFinished$3$1(tabView));
            }
        }
        
        @Override
        public void onPageStarted(final String s) {
            this.es.notifyObservers((Function1<? super Observer, Unit>)TabViewEngineSession$ViewClient$onPageStarted.TabViewEngineSession$ViewClient$onPageStarted$1.INSTANCE);
            if (s != null) {
                this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ViewClient$onPageStarted$2.TabViewEngineSession$ViewClient$onPageStarted$2$1(s));
            }
            final TabView tabView = this.es.getTabView();
            if (tabView != null) {
                this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ViewClient$onPageStarted$3.TabViewEngineSession$ViewClient$onPageStarted$3$1(tabView));
            }
        }
        
        @Override
        public void onURLChanged(final String s) {
            if (s != null) {
                this.es.notifyObservers((Function1<? super Observer, Unit>)new TabViewEngineSession$ViewClient$onURLChanged$1.TabViewEngineSession$ViewClient$onURLChanged$1$1(s));
            }
        }
        
        @Override
        public void updateFailingUrl(final String s, final boolean b) {
            final Client engineSessionClient = this.es.getEngineSessionClient();
            if (engineSessionClient != null) {
                engineSessionClient.updateFailingUrl(s, b);
            }
        }
    }
    
    public interface WindowClient
    {
        void onCloseWindow(final TabViewEngineSession p0);
        
        boolean onCreateWindow(final boolean p0, final boolean p1, final Message p2);
    }
}
