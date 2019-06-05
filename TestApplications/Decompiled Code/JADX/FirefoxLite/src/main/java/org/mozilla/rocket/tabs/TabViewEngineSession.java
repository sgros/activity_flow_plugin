package org.mozilla.rocket.tabs;

import android.arch.lifecycle.LifecycleOwner;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.support.base.observer.Observable;
import mozilla.components.support.base.observer.ObserverRegistry;
import org.mozilla.rocket.tabs.TabView.FullscreenCallback;
import org.mozilla.rocket.tabs.TabView.HitTarget;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;
import org.mozilla.rocket.tabs.web.Download;

/* compiled from: TabViewEngineSession.kt */
public final class TabViewEngineSession implements Observable<Observer> {
    private final Observable<Observer> delegate;
    private Client engineSessionClient;
    private TabView engineView;
    private Bundle webViewState;
    private WindowClient windowClient;

    /* compiled from: TabViewEngineSession.kt */
    public interface Client {
        boolean handleExternalUrl(String str);

        void onHttpAuthRequest(HttpAuthCallback httpAuthCallback, String str, String str2);

        boolean onShowFileChooser(TabViewEngineSession tabViewEngineSession, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams);

        void updateFailingUrl(String str, boolean z);
    }

    /* compiled from: TabViewEngineSession.kt */
    public interface WindowClient {
        void onCloseWindow(TabViewEngineSession tabViewEngineSession);

        boolean onCreateWindow(boolean z, boolean z2, Message message);
    }

    /* compiled from: TabViewEngineSession.kt */
    public static final class ChromeClient extends TabChromeClient {
        /* renamed from: es */
        private final TabViewEngineSession f71es;

        public ChromeClient(TabViewEngineSession tabViewEngineSession) {
            Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
            this.f71es = tabViewEngineSession;
        }

        public boolean onCreateWindow(boolean z, boolean z2, Message message) {
            WindowClient windowClient = this.f71es.getWindowClient();
            return windowClient != null ? windowClient.onCreateWindow(z, z2, message) : false;
        }

        public void onCloseWindow(TabView tabView) {
            WindowClient windowClient = this.f71es.getWindowClient();
            if (windowClient != null) {
                windowClient.onCloseWindow(this.f71es);
            }
        }

        public void onProgressChanged(int i) {
            this.f71es.notifyObservers(new TabViewEngineSession$ChromeClient$onProgressChanged$1(i));
        }

        public boolean onShowFileChooser(TabView tabView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            Intrinsics.checkParameterIsNotNull(tabView, "tabView");
            Client engineSessionClient = this.f71es.getEngineSessionClient();
            return engineSessionClient != null ? engineSessionClient.onShowFileChooser(this.f71es, valueCallback, fileChooserParams) : false;
        }

        public void onReceivedTitle(TabView tabView, String str) {
            Intrinsics.checkParameterIsNotNull(tabView, "view");
            if (str != null) {
                this.f71es.notifyObservers(new TabViewEngineSession$ChromeClient$onReceivedTitle$1(str));
            }
            str = tabView.getUrl();
            if (str != null) {
                this.f71es.notifyObservers(new TabViewEngineSession$ChromeClient$onReceivedTitle$2$1(str));
            }
            this.f71es.notifyObservers(new TabViewEngineSession$ChromeClient$onReceivedTitle$3(tabView));
        }

        public void onReceivedIcon(TabView tabView, Bitmap bitmap) {
            Intrinsics.checkParameterIsNotNull(tabView, "view");
            this.f71es.notifyObservers(new TabViewEngineSession$ChromeClient$onReceivedIcon$1(bitmap));
        }

        public void onLongPress(HitTarget hitTarget) {
            Intrinsics.checkParameterIsNotNull(hitTarget, "hitTarget");
            this.f71es.notifyObservers(new TabViewEngineSession$ChromeClient$onLongPress$1(hitTarget));
        }

        public void onEnterFullScreen(FullscreenCallback fullscreenCallback, View view) {
            Intrinsics.checkParameterIsNotNull(fullscreenCallback, "callback");
            this.f71es.notifyObservers(new TabViewEngineSession$ChromeClient$onEnterFullScreen$1(fullscreenCallback, view));
        }

        public void onExitFullScreen() {
            this.f71es.notifyObservers(TabViewEngineSession$ChromeClient$onExitFullScreen$1.INSTANCE);
        }

        public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
            Intrinsics.checkParameterIsNotNull(str, "origin");
            this.f71es.notifyObservers(new C0768xfa022780(str, callback));
        }
    }

    /* compiled from: TabViewEngineSession.kt */
    public static final class DownloadCallback implements org.mozilla.rocket.tabs.web.DownloadCallback {
        /* renamed from: es */
        private final TabViewEngineSession f72es;

        public DownloadCallback(TabViewEngineSession tabViewEngineSession) {
            Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
            this.f72es = tabViewEngineSession;
        }

        public void onDownloadStart(Download download) {
            Intrinsics.checkParameterIsNotNull(download, "download");
            this.f72es.notifyObservers(new TabViewEngineSession$DownloadCallback$onDownloadStart$1(download, CookieManager.getInstance().getCookie(download.getUrl())));
        }
    }

    /* compiled from: TabViewEngineSession.kt */
    public static final class FindListener implements org.mozilla.rocket.tabs.TabView.FindListener {
        /* renamed from: es */
        private final TabViewEngineSession f73es;

        public FindListener(TabViewEngineSession tabViewEngineSession) {
            Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
            this.f73es = tabViewEngineSession;
        }

        public void onFindResultReceived(int i, int i2, boolean z) {
            this.f73es.notifyObservers(new TabViewEngineSession$FindListener$onFindResultReceived$1(i, i2, z));
        }
    }

    /* compiled from: TabViewEngineSession.kt */
    public interface Observer extends mozilla.components.concept.engine.EngineSession.Observer {
        void onEnterFullScreen(FullscreenCallback fullscreenCallback, View view);

        void onExitFullScreen();

        void onGeolocationPermissionsShowPrompt(String str, Callback callback);

        void onLongPress(HitTarget hitTarget);

        void onReceivedIcon(Bitmap bitmap);
    }

    /* compiled from: TabViewEngineSession.kt */
    public static final class ViewClient extends TabViewClient {
        /* renamed from: es */
        private final TabViewEngineSession f74es;

        public ViewClient(TabViewEngineSession tabViewEngineSession) {
            Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
            this.f74es = tabViewEngineSession;
        }

        public void onPageStarted(String str) {
            this.f74es.notifyObservers(TabViewEngineSession$ViewClient$onPageStarted$1.INSTANCE);
            if (str != null) {
                this.f74es.notifyObservers(new TabViewEngineSession$ViewClient$onPageStarted$2$1(str));
            }
            TabView tabView = this.f74es.getTabView();
            if (tabView != null) {
                this.f74es.notifyObservers(new TabViewEngineSession$ViewClient$onPageStarted$3$1(tabView));
            }
        }

        public void onPageFinished(boolean z) {
            this.f74es.notifyObservers(TabViewEngineSession$ViewClient$onPageFinished$1.INSTANCE);
            this.f74es.notifyObservers(new TabViewEngineSession$ViewClient$onPageFinished$2(z));
            TabView tabView = this.f74es.getTabView();
            if (tabView != null) {
                this.f74es.notifyObservers(new TabViewEngineSession$ViewClient$onPageFinished$3$1(tabView));
            }
        }

        public void onURLChanged(String str) {
            if (str != null) {
                this.f74es.notifyObservers(new TabViewEngineSession$ViewClient$onURLChanged$1$1(str));
            }
        }

        public void updateFailingUrl(String str, boolean z) {
            Client engineSessionClient = this.f74es.getEngineSessionClient();
            if (engineSessionClient != null) {
                engineSessionClient.updateFailingUrl(str, z);
            }
        }

        public boolean handleExternalUrl(String str) {
            Client engineSessionClient = this.f74es.getEngineSessionClient();
            return engineSessionClient != null ? engineSessionClient.handleExternalUrl(str) : false;
        }

        public void onHttpAuthRequest(HttpAuthCallback httpAuthCallback, String str, String str2) {
            Intrinsics.checkParameterIsNotNull(httpAuthCallback, "callback");
            Client engineSessionClient = this.f74es.getEngineSessionClient();
            if (engineSessionClient != null) {
                engineSessionClient.onHttpAuthRequest(httpAuthCallback, str, str2);
            }
        }
    }

    public TabViewEngineSession() {
        this(null, 1, null);
    }

    public void notifyObservers(Function1<? super Observer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.delegate.notifyObservers(function1);
    }

    public void register(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.delegate.register(observer);
    }

    public void register(Observer observer, LifecycleOwner lifecycleOwner, boolean z) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        Intrinsics.checkParameterIsNotNull(lifecycleOwner, "owner");
        this.delegate.register(observer, lifecycleOwner, z);
    }

    public void unregister(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "observer");
        this.delegate.unregister(observer);
    }

    public void unregisterObservers() {
        this.delegate.unregisterObservers();
    }

    public <R> List<Function1<R, Boolean>> wrapConsumers(Function2<? super Observer, ? super R, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        return this.delegate.wrapConsumers(function2);
    }

    public TabViewEngineSession(Observable<Observer> observable) {
        Intrinsics.checkParameterIsNotNull(observable, "delegate");
        this.delegate = observable;
    }

    public /* synthetic */ TabViewEngineSession(Observable observable, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            observable = new ObserverRegistry();
        }
        this(observable);
    }

    public final Bundle getWebViewState() {
        return this.webViewState;
    }

    public final void setWebViewState(Bundle bundle) {
        this.webViewState = bundle;
    }

    public final Client getEngineSessionClient() {
        return this.engineSessionClient;
    }

    public final void setEngineSessionClient(Client client) {
        this.engineSessionClient = client;
    }

    public final WindowClient getWindowClient() {
        return this.windowClient;
    }

    public final void setWindowClient(WindowClient windowClient) {
        this.windowClient = windowClient;
    }

    public final void setTabView(TabView tabView) {
        if (tabView != null) {
            tabView.setViewClient(new ViewClient(this));
        }
        if (tabView != null) {
            tabView.setChromeClient(new ChromeClient(this));
        }
        if (tabView != null) {
            tabView.setFindListener(new FindListener(this));
        }
        if (tabView != null) {
            tabView.setDownloadCallback(new DownloadCallback(this));
        }
        TabView tabView2 = this.engineView;
        if (tabView2 != null) {
            tabView2.setViewClient(null);
        }
        tabView2 = this.engineView;
        if (tabView2 != null) {
            tabView2.setChromeClient(null);
        }
        tabView2 = this.engineView;
        if (tabView2 != null) {
            tabView2.setFindListener(null);
        }
        tabView2 = this.engineView;
        if (tabView2 != null) {
            tabView2.setDownloadCallback(null);
        }
        this.engineView = tabView;
    }

    public final TabView getTabView() {
        return this.engineView;
    }

    public final Unit goBack() {
        TabView tabView = getTabView();
        if (tabView == null) {
            return null;
        }
        tabView.goBack();
        return Unit.INSTANCE;
    }

    public final Unit goForward() {
        TabView tabView = getTabView();
        if (tabView == null) {
            return null;
        }
        tabView.goForward();
        return Unit.INSTANCE;
    }

    public final Unit reload() {
        TabView tabView = getTabView();
        if (tabView == null) {
            return null;
        }
        tabView.reload();
        return Unit.INSTANCE;
    }

    public final Unit stopLoading() {
        TabView tabView = getTabView();
        if (tabView == null) {
            return null;
        }
        tabView.stopLoading();
        return Unit.INSTANCE;
    }

    public final void saveState() {
        if (this.webViewState == null) {
            this.webViewState = new Bundle();
        }
        TabView tabView = getTabView();
        if (tabView != null) {
            tabView.saveViewState(this.webViewState);
        }
    }

    public final void detach() {
        TabView tabView = getTabView();
        if (tabView != null) {
            View view = tabView.getView();
            if (view != null) {
                ViewParent parent = view.getParent();
                if (parent == null) {
                    return;
                }
                if (parent != null) {
                    ((ViewGroup) parent).removeView(view);
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup");
            }
        }
    }

    public final void destroy$feature_tabs_release() {
        unregisterObservers();
        detach();
        TabView tabView = getTabView();
        if (tabView != null) {
            tabView.destroy();
        }
    }
}
