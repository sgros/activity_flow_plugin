// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.privately.browse;

import org.mozilla.urlutils.UrlUtils;
import android.webkit.WebChromeClient$FileChooserParams;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.graphics.Bitmap;
import org.mozilla.rocket.tabs.web.DownloadCallback;
import org.mozilla.focus.menu.WebContextMenu;
import org.mozilla.focus.web.HttpAuthenticationDialogBuilder;
import org.mozilla.rocket.tabs.TabViewClient;
import android.webkit.GeolocationPermissions$Callback;
import org.mozilla.focus.utils.ViewUtils;
import android.os.Parcelable;
import android.arch.lifecycle.Lifecycle;
import mozilla.components.browser.session.Download;
import android.text.TextUtils;
import android.app.Activity;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import android.view.View$OnApplyWindowInsetsListener;
import android.view.View$OnClickListener;
import android.view.View;
import android.view.LayoutInflater;
import org.mozilla.permissionhandler.PermissionHandle;
import android.os.Bundle;
import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.rocket.tabs.utils.TabUtil;
import org.mozilla.rocket.tabs.TabView;
import android.webkit.WebView;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import org.mozilla.rocket.privately.SharedViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import kotlin.TypeCastException;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import java.util.Iterator;
import android.content.Context;
import android.support.v4.app.Fragment;
import org.mozilla.rocket.tabs.TabViewEngineSession;
import org.mozilla.rocket.tabs.Session;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import android.widget.ImageView;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.focus.widget.AnimatedProgressBar;
import org.mozilla.permissionhandler.PermissionHandler;
import org.mozilla.focus.widget.FragmentListener;
import android.widget.TextView;
import android.widget.ImageButton;
import android.view.ViewGroup;
import java.util.HashMap;
import org.mozilla.focus.widget.BackKeyHandleable;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.locale.LocaleAwareFragment;

public final class BrowserFragment extends LocaleAwareFragment implements BrowserScreen, BackKeyHandleable
{
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
    private int systemVisibility;
    private ViewGroup tabViewSlot;
    private ViewGroup videoContainer;
    
    public BrowserFragment() {
        this.systemVisibility = -1;
    }
    
    public static final /* synthetic */ ViewGroup access$getBrowserContainer$p(final BrowserFragment browserFragment) {
        final ViewGroup browserContainer = browserFragment.browserContainer;
        if (browserContainer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("browserContainer");
        }
        return browserContainer;
    }
    
    public static final /* synthetic */ ImageButton access$getBtnLoad$p(final BrowserFragment browserFragment) {
        final ImageButton btnLoad = browserFragment.btnLoad;
        if (btnLoad == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnLoad");
        }
        return btnLoad;
    }
    
    public static final /* synthetic */ ImageButton access$getBtnNext$p(final BrowserFragment browserFragment) {
        final ImageButton btnNext = browserFragment.btnNext;
        if (btnNext == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnNext");
        }
        return btnNext;
    }
    
    public static final /* synthetic */ TextView access$getDisplayUrlView$p(final BrowserFragment browserFragment) {
        final TextView displayUrlView = browserFragment.displayUrlView;
        if (displayUrlView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
        }
        return displayUrlView;
    }
    
    public static final /* synthetic */ PermissionHandler access$getPermissionHandler$p(final BrowserFragment browserFragment) {
        final PermissionHandler permissionHandler = browserFragment.permissionHandler;
        if (permissionHandler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("permissionHandler");
        }
        return permissionHandler;
    }
    
    public static final /* synthetic */ AnimatedProgressBar access$getProgressView$p(final BrowserFragment browserFragment) {
        final AnimatedProgressBar progressView = browserFragment.progressView;
        if (progressView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("progressView");
        }
        return progressView;
    }
    
    public static final /* synthetic */ SessionManager access$getSessionManager$p(final BrowserFragment browserFragment) {
        final SessionManager sessionManager = browserFragment.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        return sessionManager;
    }
    
    public static final /* synthetic */ ImageView access$getSiteIdentity$p(final BrowserFragment browserFragment) {
        final ImageView siteIdentity = browserFragment.siteIdentity;
        if (siteIdentity == null) {
            Intrinsics.throwUninitializedPropertyAccessException("siteIdentity");
        }
        return siteIdentity;
    }
    
    public static final /* synthetic */ int access$getSystemVisibility$p(final BrowserFragment browserFragment) {
        return browserFragment.systemVisibility;
    }
    
    public static final /* synthetic */ ViewGroup access$getVideoContainer$p(final BrowserFragment browserFragment) {
        final ViewGroup videoContainer = browserFragment.videoContainer;
        if (videoContainer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("videoContainer");
        }
        return videoContainer;
    }
    
    public static final /* synthetic */ void access$setLoading$p(final BrowserFragment browserFragment, final boolean isLoading) {
        browserFragment.isLoading = isLoading;
    }
    
    public static final /* synthetic */ void access$setSystemVisibility$p(final BrowserFragment browserFragment, final int systemVisibility) {
        browserFragment.systemVisibility = systemVisibility;
    }
    
    private final Unit goBack() {
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        final Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                return engineSession.goBack();
            }
        }
        return null;
    }
    
    private final Unit goForward() {
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        final Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                return engineSession.goForward();
            }
        }
        return null;
    }
    
    private final void onDeleteClicked() {
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        for (final Session session : sessionManager.getTabs()) {
            final SessionManager sessionManager2 = this.sessionManager;
            if (sessionManager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }
            sessionManager2.dropTab(session.getId());
        }
        final FragmentListener listener = this.listener;
        if (listener != null) {
            listener.onNotified(this, FragmentListener.TYPE.DROP_BROWSING_PAGES, null);
        }
        ScreenNavigator.get((Context)this.getActivity()).popToHomeScreen(true);
    }
    
    private final void onLoadClicked() {
        if (this.isLoading) {
            this.stop();
        }
        else {
            this.reload();
        }
    }
    
    private final void onModeClicked() {
        final FragmentActivity activity = this.getActivity();
        if (activity != null) {
            ((FragmentListener)activity).onNotified(this, FragmentListener.TYPE.TOGGLE_PRIVATE_MODE, null);
            TelemetryWrapper.togglePrivateMode(false);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.widget.FragmentListener");
    }
    
    private final void onNextClicked() {
        this.goForward();
    }
    
    private final void onSearchClicked() {
        final FragmentActivity activity = this.getActivity();
        if (activity != null) {
            final FragmentListener fragmentListener = (FragmentListener)activity;
            final BrowserFragment browserFragment = this;
            final FragmentListener.TYPE show_URL_INPUT = FragmentListener.TYPE.SHOW_URL_INPUT;
            final TextView displayUrlView = this.displayUrlView;
            if (displayUrlView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
            }
            fragmentListener.onNotified(browserFragment, show_URL_INPUT, displayUrlView.getText());
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.widget.FragmentListener");
    }
    
    private final void registerData(final FragmentActivity fragmentActivity) {
        final SharedViewModel value = ViewModelProviders.of(fragmentActivity).get(SharedViewModel.class);
        Intrinsics.checkExpressionValueIsNotNull(value, "ViewModelProviders.of(ac\u2026redViewModel::class.java)");
        value.getUrl().observe(this, (android.arch.lifecycle.Observer<String>)new BrowserFragment$registerData.BrowserFragment$registerData$1(this));
    }
    
    private final Unit reload() {
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        final Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                return engineSession.reload();
            }
        }
        return null;
    }
    
    private final Unit stop() {
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        final Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                return engineSession.stopLoading();
            }
        }
        return null;
    }
    
    private final void unregisterData(final FragmentActivity fragmentActivity) {
        final SharedViewModel value = ViewModelProviders.of(fragmentActivity).get(SharedViewModel.class);
        Intrinsics.checkExpressionValueIsNotNull(value, "ViewModelProviders.of(ac\u2026redViewModel::class.java)");
        value.getUrl().removeObservers(this);
    }
    
    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }
    
    @Override
    public void applyLocale() {
        new WebView(this.getContext()).destroy();
    }
    
    @Override
    public Fragment getFragment() {
        return this;
    }
    
    @Override
    public void goBackground() {
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        final Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                final TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    final TabViewEngineSession engineSession2 = focusSession.getEngineSession();
                    if (engineSession2 != null) {
                        engineSession2.detach();
                    }
                    final ViewGroup tabViewSlot = this.tabViewSlot;
                    if (tabViewSlot == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("tabViewSlot");
                    }
                    tabViewSlot.removeView(tabView.getView());
                }
            }
        }
    }
    
    @Override
    public void goForeground() {
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        final Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                final TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    final ViewGroup tabViewSlot = this.tabViewSlot;
                    if (tabViewSlot == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("tabViewSlot");
                    }
                    if (tabViewSlot.getChildCount() == 0) {
                        final ViewGroup tabViewSlot2 = this.tabViewSlot;
                        if (tabViewSlot2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("tabViewSlot");
                        }
                        tabViewSlot2.addView(tabView.getView());
                    }
                }
            }
        }
    }
    
    @Override
    public void loadUrl(final String s, final boolean b, final boolean b2, final Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(s, "url");
        final String text = s;
        if (StringsKt__StringsJVMKt.isBlank(text) ^ true) {
            final TextView displayUrlView = this.displayUrlView;
            if (displayUrlView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
            }
            displayUrlView.setText((CharSequence)text);
            final SessionManager sessionManager = this.sessionManager;
            if (sessionManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }
            if (sessionManager.getTabsCount() == 0) {
                final SessionManager sessionManager2 = this.sessionManager;
                if (sessionManager2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
                }
                final Bundle argument = TabUtil.argument(null, false, true);
                Intrinsics.checkExpressionValueIsNotNull(argument, "TabUtil.argument(null, false, true)");
                sessionManager2.addTab(s, argument);
            }
            else {
                final SessionManager sessionManager3 = this.sessionManager;
                if (sessionManager3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
                }
                final Session focusSession = sessionManager3.getFocusSession();
                if (focusSession == null) {
                    Intrinsics.throwNpe();
                }
                final TabViewEngineSession engineSession = focusSession.getEngineSession();
                if (engineSession != null) {
                    final TabView tabView = engineSession.getTabView();
                    if (tabView != null) {
                        tabView.loadUrl(s);
                    }
                }
            }
            ThreadUtils.postToMainThread(runnable);
        }
    }
    
    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        this.getActivity();
    }
    
    @Override
    public void onAttach(final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super.onAttach(context);
        this.permissionHandler = new PermissionHandler((PermissionHandle)new BrowserFragment$onAttach.BrowserFragment$onAttach$1(this));
        if (context instanceof FragmentListener) {
            this.listener = (FragmentListener)context;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(context.toString());
        sb.append(" must implement OnFragmentInteractionListener");
        throw new RuntimeException(sb.toString());
    }
    
    @Override
    public boolean onBackPressed() {
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        final Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                final TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    if (tabView.canGoBack()) {
                        this.goBack();
                        return true;
                    }
                    final SessionManager sessionManager2 = this.sessionManager;
                    if (sessionManager2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
                    }
                    sessionManager2.dropTab(focusSession.getId());
                    ScreenNavigator.get((Context)this.getActivity()).popToHomeScreen(true);
                    final FragmentListener listener = this.listener;
                    if (listener != null) {
                        listener.onNotified(this, FragmentListener.TYPE.DROP_BROWSING_PAGES, null);
                    }
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        return layoutInflater.inflate(2131492969, viewGroup, false);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final FragmentActivity activity = this.getActivity();
        if (activity != null) {
            Intrinsics.checkExpressionValueIsNotNull(activity, "it");
            this.unregisterData(activity);
        }
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        final Session focusSession = sessionManager.getFocusSession();
        if (focusSession != null) {
            final Observer observer = this.observer;
            if (observer == null) {
                Intrinsics.throwUninitializedPropertyAccessException("observer");
            }
            focusSession.unregister((Session.Observer)observer);
        }
        final SessionManager sessionManager2 = this.sessionManager;
        if (sessionManager2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        final Observer observer2 = this.observer;
        if (observer2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("observer");
        }
        sessionManager2.unregister((SessionManager.Observer)observer2);
        this._$_clearFindViewByIdCache();
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        sessionManager.pause();
    }
    
    @Override
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        Intrinsics.checkParameterIsNotNull(array, "permissions");
        Intrinsics.checkParameterIsNotNull(array2, "grantResults");
        final PermissionHandler permissionHandler = this.permissionHandler;
        if (permissionHandler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("permissionHandler");
        }
        permissionHandler.onRequestPermissionsResult(this.getContext(), n, array, array2);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
        }
        sessionManager.resume();
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        final View viewById = view.findViewById(2131296411);
        Intrinsics.checkExpressionValueIsNotNull(viewById, "view.findViewById(R.id.display_url)");
        this.displayUrlView = (TextView)viewById;
        final TextView displayUrlView = this.displayUrlView;
        if (displayUrlView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
        }
        displayUrlView.setOnClickListener((View$OnClickListener)new BrowserFragment$onViewCreated.BrowserFragment$onViewCreated$1(this));
        final View viewById2 = view.findViewById(2131296648);
        Intrinsics.checkExpressionValueIsNotNull(viewById2, "view.findViewById(R.id.site_identity)");
        this.siteIdentity = (ImageView)viewById2;
        final View viewById3 = view.findViewById(2131296337);
        Intrinsics.checkExpressionValueIsNotNull(viewById3, "view.findViewById(R.id.browser_container)");
        this.browserContainer = (ViewGroup)viewById3;
        final View viewById4 = view.findViewById(2131296717);
        Intrinsics.checkExpressionValueIsNotNull(viewById4, "view.findViewById(R.id.video_container)");
        this.videoContainer = (ViewGroup)viewById4;
        final View viewById5 = view.findViewById(2131296680);
        Intrinsics.checkExpressionValueIsNotNull(viewById5, "view.findViewById(R.id.tab_view_slot)");
        this.tabViewSlot = (ViewGroup)viewById5;
        final View viewById6 = view.findViewById(2131296576);
        Intrinsics.checkExpressionValueIsNotNull(viewById6, "view.findViewById(R.id.progress)");
        this.progressView = (AnimatedProgressBar)viewById6;
        view.findViewById(2131296352).setOnClickListener((View$OnClickListener)new BrowserFragment$onViewCreated.BrowserFragment$onViewCreated$2(this));
        view.findViewById(2131296356).setOnClickListener((View$OnClickListener)new BrowserFragment$onViewCreated.BrowserFragment$onViewCreated$3(this));
        view.findViewById(2131296347).setOnClickListener((View$OnClickListener)new BrowserFragment$onViewCreated.BrowserFragment$onViewCreated$4(this));
        final View viewById7 = view.findViewById(2131296348);
        final ImageButton btnLoad = (ImageButton)viewById7;
        btnLoad.setOnClickListener((View$OnClickListener)new BrowserFragment$onViewCreated$$inlined$also$lambda.BrowserFragment$onViewCreated$$inlined$also$lambda$1(this));
        Intrinsics.checkExpressionValueIsNotNull(viewById7, "(view.findViewById<Image\u2026ner { onLoadClicked() } }");
        this.btnLoad = btnLoad;
        final View viewById8 = view.findViewById(2131296353);
        if (viewById8 != null) {
            final ImageButton btnNext = (ImageButton)viewById8;
            btnNext.setEnabled(false);
            btnNext.setOnClickListener((View$OnClickListener)new BrowserFragment$onViewCreated$$inlined$also$lambda.BrowserFragment$onViewCreated$$inlined$also$lambda$2(this));
            this.btnNext = btnNext;
            view.findViewById(2131296295).setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)BrowserFragment$onViewCreated.BrowserFragment$onViewCreated$7.INSTANCE);
            final SessionManager orThrow = TabsSessionProvider.getOrThrow(this.getActivity());
            Intrinsics.checkExpressionValueIsNotNull(orThrow, "TabsSessionProvider.getOrThrow( activity)");
            this.sessionManager = orThrow;
            this.observer = new Observer(this);
            final SessionManager sessionManager = this.sessionManager;
            if (sessionManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }
            final Observer observer = this.observer;
            if (observer == null) {
                Intrinsics.throwUninitializedPropertyAccessException("observer");
            }
            sessionManager.register((SessionManager.Observer)observer);
            final SessionManager sessionManager2 = this.sessionManager;
            if (sessionManager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }
            final Session focusSession = sessionManager2.getFocusSession();
            if (focusSession != null) {
                final Observer observer2 = this.observer;
                if (observer2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("observer");
                }
                focusSession.register((Session.Observer)observer2);
            }
            final FragmentActivity activity = this.getActivity();
            if (activity != null) {
                Intrinsics.checkExpressionValueIsNotNull(activity, "it");
                this.registerData(activity);
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.widget.ImageButton");
    }
    
    @Override
    public void switchToTab(final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            final SessionManager sessionManager = this.sessionManager;
            if (sessionManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }
            if (s == null) {
                Intrinsics.throwNpe();
            }
            sessionManager.switchToTab(s);
        }
    }
    
    public static final class Observer implements Session.Observer, SessionManager.Observer
    {
        private TabView.FullscreenCallback callback;
        private final BrowserFragment fragment;
        private Session session;
        
        public Observer(final BrowserFragment fragment) {
            Intrinsics.checkParameterIsNotNull(fragment, "fragment");
            this.fragment = fragment;
        }
        
        @Override
        public boolean handleExternalUrl(final String s) {
            return false;
        }
        
        @Override
        public boolean onDownload(final Session session, final Download download) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(download, "download");
            final FragmentActivity activity = this.fragment.getActivity();
            if (activity != null) {
                final Lifecycle lifecycle = activity.getLifecycle();
                Intrinsics.checkExpressionValueIsNotNull(lifecycle, "activity.lifecycle");
                if (lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    final String url = download.getUrl();
                    final String fileName = download.getFileName();
                    final String userAgent = download.getUserAgent();
                    if (userAgent == null) {
                        Intrinsics.throwNpe();
                    }
                    final String contentType = download.getContentType();
                    if (contentType == null) {
                        Intrinsics.throwNpe();
                    }
                    final Long contentLength = download.getContentLength();
                    if (contentLength == null) {
                        Intrinsics.throwNpe();
                    }
                    BrowserFragment.access$getPermissionHandler$p(this.fragment).tryAction(this.fragment, "android.permission.WRITE_EXTERNAL_STORAGE", 0, (Parcelable)new org.mozilla.rocket.tabs.web.Download(url, fileName, userAgent, "", contentType, contentLength, false));
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public void onEnterFullScreen(final TabView.FullscreenCallback fullscreenCallback, final View view) {
            Intrinsics.checkParameterIsNotNull(fullscreenCallback, "callback");
            final BrowserFragment fragment = this.fragment;
            BrowserFragment.access$getBrowserContainer$p(fragment).setVisibility(4);
            BrowserFragment.access$getVideoContainer$p(fragment).setVisibility(0);
            BrowserFragment.access$getVideoContainer$p(fragment).addView(view);
            BrowserFragment.access$setSystemVisibility$p(fragment, ViewUtils.switchToImmersiveMode(fragment.getActivity()));
        }
        
        @Override
        public void onExitFullScreen() {
            final BrowserFragment fragment = this.fragment;
            BrowserFragment.access$getBrowserContainer$p(fragment).setVisibility(0);
            BrowserFragment.access$getVideoContainer$p(fragment).setVisibility(4);
            BrowserFragment.access$getVideoContainer$p(fragment).removeAllViews();
            if (BrowserFragment.access$getSystemVisibility$p(fragment) != -1) {
                ViewUtils.exitImmersiveMode(BrowserFragment.access$getSystemVisibility$p(fragment), fragment.getActivity());
            }
            final TabView.FullscreenCallback callback = this.callback;
            if (callback != null) {
                callback.fullScreenExited();
            }
            this.callback = null;
            final Session session = this.session;
            if (session != null) {
                final TabViewEngineSession engineSession = session.getEngineSession();
                if (engineSession != null) {
                    final TabView tabView = engineSession.getTabView();
                    if (tabView != null && tabView instanceof WebView) {
                        ((WebView)tabView).clearFocus();
                    }
                }
            }
        }
        
        @Override
        public void onFindResult(final Session session, final mozilla.components.browser.session.Session.FindResult findResult) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(findResult, "result");
            Session.Observer.DefaultImpls.onFindResult((Session.Observer)this, session, findResult);
        }
        
        @Override
        public void onFocusChanged(final Session session, final Factor factor) {
            Intrinsics.checkParameterIsNotNull(factor, "factor");
            SessionManager.Observer.DefaultImpls.onFocusChanged((SessionManager.Observer)this, session, factor);
        }
        
        @Override
        public void onGeolocationPermissionsShowPrompt(final String s, final GeolocationPermissions$Callback geolocationPermissions$Callback) {
            Intrinsics.checkParameterIsNotNull(s, "origin");
            Session.Observer.DefaultImpls.onGeolocationPermissionsShowPrompt((Session.Observer)this, s, geolocationPermissions$Callback);
        }
        
        @Override
        public void onHttpAuthRequest(final TabViewClient.HttpAuthCallback httpAuthCallback, final String s, final String s2) {
            Intrinsics.checkParameterIsNotNull(httpAuthCallback, "callback");
            final HttpAuthenticationDialogBuilder build = new HttpAuthenticationDialogBuilder.Builder((Context)this.fragment.getActivity(), s, s2).setOkListener((HttpAuthenticationDialogBuilder.OkListener)new BrowserFragment$Observer$onHttpAuthRequest$builder.BrowserFragment$Observer$onHttpAuthRequest$builder$1(httpAuthCallback)).setCancelListener((HttpAuthenticationDialogBuilder.CancelListener)new BrowserFragment$Observer$onHttpAuthRequest$builder.BrowserFragment$Observer$onHttpAuthRequest$builder$2(httpAuthCallback)).build();
            build.createDialog();
            build.show();
        }
        
        @Override
        public void onLoadingStateChanged(Session focusSession, final boolean b) {
            Intrinsics.checkParameterIsNotNull(focusSession, "session");
            BrowserFragment.access$setLoading$p(this.fragment, b);
            if (!b) {
                focusSession = BrowserFragment.access$getSessionManager$p(this.fragment).getFocusSession();
                if (focusSession != null) {
                    final TabViewEngineSession engineSession = focusSession.getEngineSession();
                    if (engineSession != null) {
                        final ImageButton access$getBtnNext$p = BrowserFragment.access$getBtnNext$p(this.fragment);
                        final TabView tabView = engineSession.getTabView();
                        access$getBtnNext$p.setEnabled(tabView != null && tabView.canGoForward());
                        BrowserFragment.access$getBtnLoad$p(this.fragment).setImageResource(2131230900);
                    }
                }
                return;
            }
            BrowserFragment.access$getBtnLoad$p(this.fragment).setImageResource(2131230879);
        }
        
        @Override
        public void onLongPress(final Session session, final TabView.HitTarget hitTarget) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Intrinsics.checkParameterIsNotNull(hitTarget, "hitTarget");
            final FragmentActivity activity = this.fragment.getActivity();
            if (activity != null) {
                WebContextMenu.show(true, activity, new PrivateDownloadCallback(this.fragment, session.getUrl()), hitTarget);
            }
        }
        
        @Override
        public void onNavigationStateChanged(final Session session, final boolean b, final boolean b2) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            Session.Observer.DefaultImpls.onNavigationStateChanged((Session.Observer)this, session, b, b2);
        }
        
        @Override
        public void onProgress(final Session session, final int progress) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            BrowserFragment.access$getProgressView$p(this.fragment).setProgress(progress);
        }
        
        @Override
        public void onReceivedIcon(final Bitmap bitmap) {
            Session.Observer.DefaultImpls.onReceivedIcon((Session.Observer)this, bitmap);
        }
        
        @Override
        public void onSecurityChanged(final Session session, final boolean imageLevel) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            BrowserFragment.access$getSiteIdentity$p(this.fragment).setImageLevel((int)(imageLevel ? 1 : 0));
        }
        
        @Override
        public void onSessionAdded(final Session session, final Bundle bundle) {
            Intrinsics.checkParameterIsNotNull(session, "session");
        }
        
        @Override
        public void onSessionCountChanged(final int n) {
            if (n == 0) {
                final Session session = this.session;
                if (session != null) {
                    session.unregister((Session.Observer)this);
                }
            }
            else {
                this.session = BrowserFragment.access$getSessionManager$p(this.fragment).getFocusSession();
                final Session session2 = this.session;
                if (session2 != null) {
                    session2.register((Session.Observer)this);
                }
            }
        }
        
        @Override
        public boolean onShowFileChooser(final TabViewEngineSession tabViewEngineSession, final ValueCallback<Uri[]> valueCallback, final WebChromeClient$FileChooserParams webChromeClient$FileChooserParams) {
            Intrinsics.checkParameterIsNotNull(tabViewEngineSession, "es");
            return false;
        }
        
        @Override
        public void onTitleChanged(final Session session, final String s) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            if (!BrowserFragment.access$getDisplayUrlView$p(this.fragment).getText().toString().equals(session.getUrl())) {
                BrowserFragment.access$getDisplayUrlView$p(this.fragment).setText((CharSequence)session.getUrl());
            }
        }
        
        @Override
        public void onUrlChanged(final Session session, final String s) {
            Intrinsics.checkParameterIsNotNull(session, "session");
            if (!UrlUtils.isInternalErrorURL(s)) {
                BrowserFragment.access$getDisplayUrlView$p(this.fragment).setText((CharSequence)s);
            }
        }
        
        @Override
        public void updateFailingUrl(final String s, final boolean b) {
        }
    }
    
    public static final class PrivateDownloadCallback implements DownloadCallback
    {
        private final BrowserFragment fragment;
        private final String refererUrl;
        
        public PrivateDownloadCallback(final BrowserFragment fragment, final String refererUrl) {
            Intrinsics.checkParameterIsNotNull(fragment, "fragment");
            this.fragment = fragment;
            this.refererUrl = refererUrl;
        }
        
        @Override
        public void onDownloadStart(final org.mozilla.rocket.tabs.web.Download download) {
            Intrinsics.checkParameterIsNotNull(download, "download");
            final FragmentActivity activity = this.fragment.getActivity();
            if (activity != null) {
                Intrinsics.checkExpressionValueIsNotNull(activity, "it");
                final Lifecycle lifecycle = activity.getLifecycle();
                Intrinsics.checkExpressionValueIsNotNull(lifecycle, "it.lifecycle");
                if (!lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    return;
                }
            }
            BrowserFragment.access$getPermissionHandler$p(this.fragment).tryAction(this.fragment, "android.permission.WRITE_EXTERNAL_STORAGE", 0, (Parcelable)download);
        }
    }
}
