package org.mozilla.rocket.privately.browse;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Lifecycle.State;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.browser.session.Session.FindResult;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.locale.LocaleAwareFragment;
import org.mozilla.focus.menu.WebContextMenu;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.navigation.ScreenNavigator.BrowserScreen;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.focus.web.HttpAuthenticationDialogBuilder;
import org.mozilla.focus.web.HttpAuthenticationDialogBuilder.Builder;
import org.mozilla.focus.widget.AnimatedProgressBar;
import org.mozilla.focus.widget.BackKeyHandleable;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.widget.FragmentListener.TYPE;
import org.mozilla.permissionhandler.PermissionHandler;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.privately.SharedViewModel;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.Session.Observer.DefaultImpls;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.SessionManager.Factor;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabView.FullscreenCallback;
import org.mozilla.rocket.tabs.TabView.HitTarget;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;
import org.mozilla.rocket.tabs.TabViewEngineSession;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.rocket.tabs.utils.TabUtil;
import org.mozilla.rocket.tabs.web.Download;
import org.mozilla.rocket.tabs.web.DownloadCallback;
import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.urlutils.UrlUtils;

/* compiled from: BrowserFragment.kt */
public final class BrowserFragment extends LocaleAwareFragment implements BrowserScreen, BackKeyHandleable {
    private HashMap _$_findViewCache;
    private ViewGroup browserContainer;
    private ImageButton btnLoad;
    private ImageButton btnNext;
    private TextView displayUrlView;
    private boolean isLoading;
    private FragmentListener listener;
    private Observer observer;
    private PermissionHandler permissionHandler;
    private AnimatedProgressBar progressView;
    private SessionManager sessionManager;
    private ImageView siteIdentity;
    private int systemVisibility = -1;
    private ViewGroup tabViewSlot;
    private ViewGroup videoContainer;

    /* compiled from: BrowserFragment.kt */
    public static final class PrivateDownloadCallback implements DownloadCallback {
        private final BrowserFragment fragment;
        private final String refererUrl;

        public PrivateDownloadCallback(BrowserFragment browserFragment, String str) {
            Intrinsics.checkParameterIsNotNull(browserFragment, "fragment");
            this.fragment = browserFragment;
            this.refererUrl = str;
        }

        public void onDownloadStart(Download download) {
            Intrinsics.checkParameterIsNotNull(download, "download");
            FragmentActivity activity = this.fragment.getActivity();
            if (activity != null) {
                Intrinsics.checkExpressionValueIsNotNull(activity, "it");
                Lifecycle lifecycle = activity.getLifecycle();
                Intrinsics.checkExpressionValueIsNotNull(lifecycle, "it.lifecycle");
                if (!lifecycle.getCurrentState().isAtLeast(State.STARTED)) {
                    return;
                }
            }
            BrowserFragment.access$getPermissionHandler$p(this.fragment).tryAction((Fragment) this.fragment, "android.permission.WRITE_EXTERNAL_STORAGE", 0, (Parcelable) download);
        }
    }

    /* compiled from: BrowserFragment.kt */
    public static final class Observer implements org.mozilla.rocket.tabs.Session.Observer, org.mozilla.rocket.tabs.SessionManager.Observer {
        private FullscreenCallback callback;
        private final BrowserFragment fragment;
        private Session session;

        public boolean handleExternalUrl(String str) {
            return false;
        }

        public void onSessionAdded(Session session, Bundle bundle) {
            Intrinsics.checkParameterIsNotNull(session, "session");
        }

        public boolean onShowFileChooser(TabViewEngineSession tabViewEngineSession, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
            return false;
        }

        public void updateFailingUrl(String str, boolean z) {
        }

        public Observer(BrowserFragment browserFragment) {
            Intrinsics.checkParameterIsNotNull(browserFragment, "fragment");
            this.fragment = browserFragment;
        }

        public void onFindResult(Session session, FindResult findResult) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(findResult, "result");
            DefaultImpls.onFindResult(this, session, findResult);
        }

        public void onFocusChanged(Session session, Factor factor) {
            Intrinsics.checkParameterIsNotNull(factor, "factor");
            org.mozilla.rocket.tabs.SessionManager.Observer.DefaultImpls.onFocusChanged(this, session, factor);
        }

        public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
            Intrinsics.checkParameterIsNotNull(str, "origin");
            DefaultImpls.onGeolocationPermissionsShowPrompt(this, str, callback);
        }

        public void onNavigationStateChanged(Session session, boolean z, boolean z2) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            DefaultImpls.onNavigationStateChanged(this, session, z, z2);
        }

        public void onReceivedIcon(Bitmap bitmap) {
            DefaultImpls.onReceivedIcon(this, bitmap);
        }

        public void onProgress(Session session, int i) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            BrowserFragment.access$getProgressView$p(this.fragment).setProgress(i);
        }

        public void onTitleChanged(Session session, String str) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            if (!BrowserFragment.access$getDisplayUrlView$p(this.fragment).getText().toString().equals(session.getUrl())) {
                BrowserFragment.access$getDisplayUrlView$p(this.fragment).setText(session.getUrl());
            }
        }

        public void onLongPress(Session session, HitTarget hitTarget) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(hitTarget, "hitTarget");
            FragmentActivity activity = this.fragment.getActivity();
            if (activity != null) {
                WebContextMenu.show(true, activity, new PrivateDownloadCallback(this.fragment, session.getUrl()), hitTarget);
            }
        }

        public void onEnterFullScreen(FullscreenCallback fullscreenCallback, View view) {
            Intrinsics.checkParameterIsNotNull(fullscreenCallback, "callback");
            BrowserFragment browserFragment = this.fragment;
            BrowserFragment.access$getBrowserContainer$p(browserFragment).setVisibility(4);
            BrowserFragment.access$getVideoContainer$p(browserFragment).setVisibility(0);
            BrowserFragment.access$getVideoContainer$p(browserFragment).addView(view);
            browserFragment.systemVisibility = ViewUtils.switchToImmersiveMode(browserFragment.getActivity());
        }

        public void onExitFullScreen() {
            BrowserFragment browserFragment = this.fragment;
            BrowserFragment.access$getBrowserContainer$p(browserFragment).setVisibility(0);
            BrowserFragment.access$getVideoContainer$p(browserFragment).setVisibility(4);
            BrowserFragment.access$getVideoContainer$p(browserFragment).removeAllViews();
            if (browserFragment.systemVisibility != -1) {
                ViewUtils.exitImmersiveMode(browserFragment.systemVisibility, browserFragment.getActivity());
            }
            FullscreenCallback fullscreenCallback = this.callback;
            if (fullscreenCallback != null) {
                fullscreenCallback.fullScreenExited();
            }
            this.callback = (FullscreenCallback) null;
            Session session = this.session;
            if (session != null) {
                TabViewEngineSession engineSession = session.getEngineSession();
                if (engineSession != null) {
                    TabView tabView = engineSession.getTabView();
                    if (tabView != null && (tabView instanceof WebView)) {
                        ((WebView) tabView).clearFocus();
                    }
                }
            }
        }

        public void onUrlChanged(Session session, String str) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            if (!UrlUtils.isInternalErrorURL(str)) {
                BrowserFragment.access$getDisplayUrlView$p(this.fragment).setText(str);
            }
        }

        public void onLoadingStateChanged(Session session, boolean z) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            this.fragment.isLoading = z;
            if (z) {
                BrowserFragment.access$getBtnLoad$p(this.fragment).setImageResource(2131230879);
            } else {
                session = BrowserFragment.access$getSessionManager$p(this.fragment).getFocusSession();
                if (session != null) {
                    TabViewEngineSession engineSession = session.getEngineSession();
                    if (engineSession != null) {
                        ImageButton access$getBtnNext$p = BrowserFragment.access$getBtnNext$p(this.fragment);
                        TabView tabView = engineSession.getTabView();
                        access$getBtnNext$p.setEnabled(tabView != null ? tabView.canGoForward() : false);
                        BrowserFragment.access$getBtnLoad$p(this.fragment).setImageResource(2131230900);
                    }
                }
            }
        }

        public void onSecurityChanged(Session session, boolean z) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            BrowserFragment.access$getSiteIdentity$p(this.fragment).setImageLevel(z);
        }

        public void onSessionCountChanged(int i) {
            Session session;
            if (i == 0) {
                session = this.session;
                if (session != null) {
                    session.unregister((org.mozilla.rocket.tabs.Session.Observer) this);
                    return;
                }
                return;
            }
            this.session = BrowserFragment.access$getSessionManager$p(this.fragment).getFocusSession();
            session = this.session;
            if (session != null) {
                session.register((org.mozilla.rocket.tabs.Session.Observer) this);
            }
        }

        public boolean onDownload(Session session, mozilla.components.browser.session.Download download) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(download, "download");
            FragmentActivity activity = this.fragment.getActivity();
            if (activity != null) {
                Lifecycle lifecycle = activity.getLifecycle();
                Intrinsics.checkExpressionValueIsNotNull(lifecycle, "activity.lifecycle");
                if (lifecycle.getCurrentState().isAtLeast(State.STARTED)) {
                    String url = download.getUrl();
                    String fileName = download.getFileName();
                    String userAgent = download.getUserAgent();
                    if (userAgent == null) {
                        Intrinsics.throwNpe();
                    }
                    String str = "";
                    String contentType = download.getContentType();
                    if (contentType == null) {
                        Intrinsics.throwNpe();
                    }
                    Long contentLength = download.getContentLength();
                    if (contentLength == null) {
                        Intrinsics.throwNpe();
                    }
                    BrowserFragment.access$getPermissionHandler$p(this.fragment).tryAction((Fragment) this.fragment, "android.permission.WRITE_EXTERNAL_STORAGE", 0, (Parcelable) new Download(url, fileName, userAgent, str, contentType, contentLength.longValue(), false));
                    return true;
                }
            }
            return false;
        }

        public void onHttpAuthRequest(HttpAuthCallback httpAuthCallback, String str, String str2) {
            Intrinsics.checkParameterIsNotNull(httpAuthCallback, "callback");
            HttpAuthenticationDialogBuilder build = new Builder(this.fragment.getActivity(), str, str2).setOkListener(new BrowserFragment$Observer$onHttpAuthRequest$builder$1(httpAuthCallback)).setCancelListener(new BrowserFragment$Observer$onHttpAuthRequest$builder$2(httpAuthCallback)).build();
            build.createDialog();
            build.show();
        }
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }

    public static final /* synthetic */ ViewGroup access$getBrowserContainer$p(BrowserFragment browserFragment) {
        ViewGroup viewGroup = browserFragment.browserContainer;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("browserContainer");
        }
        return viewGroup;
    }

    public static final /* synthetic */ ImageButton access$getBtnLoad$p(BrowserFragment browserFragment) {
        ImageButton imageButton = browserFragment.btnLoad;
        if (imageButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnLoad");
        }
        return imageButton;
    }

    public static final /* synthetic */ ImageButton access$getBtnNext$p(BrowserFragment browserFragment) {
        ImageButton imageButton = browserFragment.btnNext;
        if (imageButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnNext");
        }
        return imageButton;
    }

    public static final /* synthetic */ TextView access$getDisplayUrlView$p(BrowserFragment browserFragment) {
        TextView textView = browserFragment.displayUrlView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
        }
        return textView;
    }

    public static final /* synthetic */ PermissionHandler access$getPermissionHandler$p(BrowserFragment browserFragment) {
        PermissionHandler permissionHandler = browserFragment.permissionHandler;
        if (permissionHandler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("permissionHandler");
        }
        return permissionHandler;
    }

    public static final /* synthetic */ AnimatedProgressBar access$getProgressView$p(BrowserFragment browserFragment) {
        AnimatedProgressBar animatedProgressBar = browserFragment.progressView;
        if (animatedProgressBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("progressView");
        }
        return animatedProgressBar;
    }

    public static final /* synthetic */ SessionManager access$getSessionManager$p(BrowserFragment browserFragment) {
        SessionManager sessionManager = browserFragment.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        return sessionManager;
    }

    public static final /* synthetic */ ImageView access$getSiteIdentity$p(BrowserFragment browserFragment) {
        ImageView imageView = browserFragment.siteIdentity;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("siteIdentity");
        }
        return imageView;
    }

    public static final /* synthetic */ ViewGroup access$getVideoContainer$p(BrowserFragment browserFragment) {
        ViewGroup viewGroup = browserFragment.videoContainer;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("videoContainer");
        }
        return viewGroup;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        return layoutInflater.inflate(C0769R.layout.fragment_private_browser, viewGroup, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        FragmentActivity activity = getActivity();
    }

    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        View findViewById = view.findViewById(C0427R.C0426id.display_url);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.display_url)");
        this.displayUrlView = (TextView) findViewById;
        TextView textView = this.displayUrlView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
        }
        textView.setOnClickListener(new BrowserFragment$onViewCreated$1(this));
        findViewById = view.findViewById(C0427R.C0426id.site_identity);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.site_identity)");
        this.siteIdentity = (ImageView) findViewById;
        findViewById = view.findViewById(C0427R.C0426id.browser_container);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.browser_container)");
        this.browserContainer = (ViewGroup) findViewById;
        findViewById = view.findViewById(C0427R.C0426id.video_container);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.video_container)");
        this.videoContainer = (ViewGroup) findViewById;
        findViewById = view.findViewById(C0427R.C0426id.tab_view_slot);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.tab_view_slot)");
        this.tabViewSlot = (ViewGroup) findViewById;
        findViewById = view.findViewById(C0427R.C0426id.progress);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "view.findViewById(R.id.progress)");
        this.progressView = (AnimatedProgressBar) findViewById;
        view.findViewById(C0427R.C0426id.btn_mode).setOnClickListener(new BrowserFragment$onViewCreated$2(this));
        view.findViewById(C0427R.C0426id.btn_search).setOnClickListener(new BrowserFragment$onViewCreated$3(this));
        view.findViewById(C0427R.C0426id.btn_delete).setOnClickListener(new BrowserFragment$onViewCreated$4(this));
        findViewById = view.findViewById(C0427R.C0426id.btn_load);
        ImageButton imageButton = (ImageButton) findViewById;
        imageButton.setOnClickListener(new BrowserFragment$onViewCreated$$inlined$also$lambda$1(this));
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "(view.findViewById<Image…ner { onLoadClicked() } }");
        this.btnLoad = imageButton;
        findViewById = view.findViewById(C0427R.C0426id.btn_next);
        if (findViewById != null) {
            ImageButton imageButton2 = (ImageButton) findViewById;
            imageButton2.setEnabled(false);
            imageButton2.setOnClickListener(new BrowserFragment$onViewCreated$$inlined$also$lambda$2(this));
            this.btnNext = imageButton2;
            view.findViewById(C0427R.C0426id.appbar).setOnApplyWindowInsetsListener(BrowserFragment$onViewCreated$7.INSTANCE);
            SessionManager orThrow = TabsSessionProvider.getOrThrow(getActivity());
            Intrinsics.checkExpressionValueIsNotNull(orThrow, "TabsSessionProvider.getOrThrow( activity)");
            this.sessionManager = orThrow;
            this.observer = new Observer(this);
            orThrow = this.sessionManager;
            if (orThrow == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }
            Observer observer = this.observer;
            if (observer == null) {
                Intrinsics.throwUninitializedPropertyAccessException("observer");
            }
            orThrow.register((org.mozilla.rocket.tabs.SessionManager.Observer) observer);
            orThrow = this.sessionManager;
            if (orThrow == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }
            Session focusSession = orThrow.getFocusSession();
            if (focusSession != null) {
                observer = this.observer;
                if (observer == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("observer");
                }
                focusSession.register((org.mozilla.rocket.tabs.Session.Observer) observer);
            }
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Intrinsics.checkExpressionValueIsNotNull(activity, "it");
                registerData(activity);
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.widget.ImageButton");
    }

    public void onDestroyView() {
        Observer observer;
        super.onDestroyView();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Intrinsics.checkExpressionValueIsNotNull(activity, "it");
            unregisterData(activity);
        }
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            observer = this.observer;
            if (observer == null) {
                Intrinsics.throwUninitializedPropertyAccessException("observer");
            }
            focusSession.unregister((org.mozilla.rocket.tabs.Session.Observer) observer);
        }
        sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        observer = this.observer;
        if (observer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("observer");
        }
        sessionManager.unregister((org.mozilla.rocket.tabs.SessionManager.Observer) observer);
        _$_clearFindViewByIdCache();
    }

    public void onResume() {
        super.onResume();
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        sessionManager.resume();
    }

    public void onPause() {
        super.onPause();
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        sessionManager.pause();
    }

    public void onAttach(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super.onAttach(context);
        this.permissionHandler = new PermissionHandler(new BrowserFragment$onAttach$1(this));
        if (context instanceof FragmentListener) {
            this.listener = (FragmentListener) context;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.toString());
        stringBuilder.append(" must implement OnFragmentInteractionListener");
        throw new RuntimeException(stringBuilder.toString());
    }

    public void onDetach() {
        super.onDetach();
        this.listener = (FragmentListener) null;
    }

    public void applyLocale() {
        new WebView(getContext()).destroy();
    }

    public boolean onBackPressed() {
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        Session focusSession = sessionManager.getFocusSession();
        if (focusSession == null) {
            return false;
        }
        TabViewEngineSession engineSession = focusSession.getEngineSession();
        if (engineSession != null) {
            TabView tabView = engineSession.getTabView();
            if (tabView != null) {
                if (tabView.canGoBack()) {
                    goBack();
                    return true;
                }
                SessionManager sessionManager2 = this.sessionManager;
                if (sessionManager2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
                }
                sessionManager2.dropTab(focusSession.getId());
                ScreenNavigator.get(getActivity()).popToHomeScreen(true);
                FragmentListener fragmentListener = this.listener;
                if (fragmentListener != null) {
                    fragmentListener.onNotified(this, TYPE.DROP_BROWSING_PAGES, null);
                }
                return true;
            }
        }
        return false;
    }

    public Fragment getFragment() {
        return this;
    }

    public void switchToTab(String str) {
        if (!TextUtils.isEmpty(str)) {
            SessionManager sessionManager = this.sessionManager;
            if (sessionManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }
            if (str == null) {
                Intrinsics.throwNpe();
            }
            sessionManager.switchToTab(str);
        }
    }

    public void goForeground() {
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    ViewGroup viewGroup = this.tabViewSlot;
                    if (viewGroup == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("tabViewSlot");
                    }
                    if (viewGroup.getChildCount() == 0) {
                        viewGroup = this.tabViewSlot;
                        if (viewGroup == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("tabViewSlot");
                        }
                        viewGroup.addView(tabView.getView());
                    }
                }
            }
        }
    }

    public void goBackground() {
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    TabViewEngineSession engineSession2 = focusSession.getEngineSession();
                    if (engineSession2 != null) {
                        engineSession2.detach();
                    }
                    ViewGroup viewGroup = this.tabViewSlot;
                    if (viewGroup == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("tabViewSlot");
                    }
                    viewGroup.removeView(tabView.getView());
                }
            }
        }
    }

    public void loadUrl(String str, boolean z, boolean z2, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        CharSequence charSequence = str;
        if ((StringsKt__StringsJVMKt.isBlank(charSequence) ^ 1) != 0) {
            TextView textView = this.displayUrlView;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
            }
            textView.setText(charSequence);
            SessionManager sessionManager = this.sessionManager;
            if (sessionManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }
            if (sessionManager.getTabsCount() == 0) {
                sessionManager = this.sessionManager;
                if (sessionManager == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
                }
                Bundle argument = TabUtil.argument(null, false, true);
                Intrinsics.checkExpressionValueIsNotNull(argument, "TabUtil.argument(null, false, true)");
                sessionManager.addTab(str, argument);
            } else {
                sessionManager = this.sessionManager;
                if (sessionManager == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
                }
                Session focusSession = sessionManager.getFocusSession();
                if (focusSession == null) {
                    Intrinsics.throwNpe();
                }
                TabViewEngineSession engineSession = focusSession.getEngineSession();
                if (engineSession != null) {
                    TabView tabView = engineSession.getTabView();
                    if (tabView != null) {
                        tabView.loadUrl(str);
                    }
                }
            }
            ThreadUtils.postToMainThread(runnable);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "permissions");
        Intrinsics.checkParameterIsNotNull(iArr, "grantResults");
        PermissionHandler permissionHandler = this.permissionHandler;
        if (permissionHandler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("permissionHandler");
        }
        permissionHandler.onRequestPermissionsResult(getContext(), i, strArr, iArr);
    }

    private final Unit goBack() {
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                return engineSession.goBack();
            }
        }
        return null;
    }

    private final Unit goForward() {
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                return engineSession.goForward();
            }
        }
        return null;
    }

    private final Unit stop() {
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                return engineSession.stopLoading();
            }
        }
        return null;
    }

    private final Unit reload() {
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                return engineSession.reload();
            }
        }
        return null;
    }

    private final void registerData(FragmentActivity fragmentActivity) {
        ViewModel viewModel = ViewModelProviders.m2of(fragmentActivity).get(SharedViewModel.class);
        Intrinsics.checkExpressionValueIsNotNull(viewModel, "ViewModelProviders.of(ac…redViewModel::class.java)");
        ((SharedViewModel) viewModel).getUrl().observe(this, new BrowserFragment$registerData$1(this));
    }

    private final void unregisterData(FragmentActivity fragmentActivity) {
        ViewModel viewModel = ViewModelProviders.m2of(fragmentActivity).get(SharedViewModel.class);
        Intrinsics.checkExpressionValueIsNotNull(viewModel, "ViewModelProviders.of(ac…redViewModel::class.java)");
        ((SharedViewModel) viewModel).getUrl().removeObservers(this);
    }

    private final void onModeClicked() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((FragmentListener) activity).onNotified(this, TYPE.TOGGLE_PRIVATE_MODE, null);
            TelemetryWrapper.togglePrivateMode(false);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.widget.FragmentListener");
    }

    private final void onNextClicked() {
        goForward();
    }

    private final void onSearchClicked() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            FragmentListener fragmentListener = (FragmentListener) activity;
            Fragment fragment = this;
            TYPE type = TYPE.SHOW_URL_INPUT;
            TextView textView = this.displayUrlView;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
            }
            fragmentListener.onNotified(fragment, type, textView.getText());
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.widget.FragmentListener");
    }

    private final void onLoadClicked() {
        if (this.isLoading) {
            stop();
        } else {
            reload();
        }
    }

    private final void onDeleteClicked() {
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        for (Session session : sessionManager.getTabs()) {
            SessionManager sessionManager2 = this.sessionManager;
            if (sessionManager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }
            sessionManager2.dropTab(session.getId());
        }
        FragmentListener fragmentListener = this.listener;
        if (fragmentListener != null) {
            fragmentListener.onNotified(this, TYPE.DROP_BROWSING_PAGES, null);
        }
        ScreenNavigator.get(getActivity()).popToHomeScreen(true);
    }
}
