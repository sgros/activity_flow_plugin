// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.fragment;

import org.mozilla.focus.menu.WebContextMenu;
import org.mozilla.focus.web.HttpAuthenticationDialogBuilder;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.mozilla.focus.utils.IntentUtils;
import android.webkit.WebChromeClient$FileChooserParams;
import android.net.Uri;
import android.webkit.ValueCallback;
import org.mozilla.rocket.tabs.TabViewClient;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.ValueAnimator;
import java.util.WeakHashMap;
import android.arch.lifecycle.Lifecycle;
import org.mozilla.rocket.tabs.web.DownloadCallback;
import android.text.TextUtils;
import org.mozilla.focus.utils.ViewUtils;
import java.util.Iterator;
import android.arch.lifecycle.Observer;
import org.mozilla.focus.Inject;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import android.view.View$OnApplyWindowInsetsListener;
import android.os.Bundle;
import org.mozilla.rocket.content.HomeFragmentViewState;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.widget.FragmentListener$_CC;
import org.mozilla.focus.widget.FragmentListener;
import android.widget.Toast;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.os.Parcelable;
import org.mozilla.permissionhandler.PermissionHandle;
import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.rocket.tabs.utils.TabUtil;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.rocket.tabs.TabViewEngineSession;
import android.webkit.WebBackForwardList;
import org.mozilla.rocket.tabs.Session;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.view.LayoutInflater;
import org.mozilla.urlutils.UrlUtils;
import android.app.Activity;
import org.mozilla.focus.download.EnqueueDownloadTask;
import android.support.v4.app.Fragment;
import org.mozilla.rocket.download.DownloadIndicatorIntroViewHelper;
import android.content.Context;
import org.mozilla.focus.utils.Settings;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;
import android.widget.RelativeLayout$LayoutParams;
import android.view.WindowInsets;
import android.widget.CheckedTextView;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.widget.TabRestoreMonitor;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import org.mozilla.focus.tabs.tabtray.TabTray;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.util.DisplayMetrics;
import android.graphics.Bitmap;
import android.webkit.WebView;
import org.mozilla.rocket.tabs.web.Download;
import org.mozilla.focus.web.GeoPermissionCache;
import android.app.Dialog;
import android.view.ViewGroup;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;
import org.mozilla.focus.tabs.TabCounter;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import org.mozilla.focus.widget.AnimatedProgressBar;
import org.mozilla.permissionhandler.PermissionHandler;
import org.mozilla.rocket.tabs.SessionManager;
import java.lang.ref.WeakReference;
import android.webkit.GeolocationPermissions$Callback;
import android.support.v7.app.AlertDialog;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.focus.widget.FindInPage;
import org.mozilla.focus.utils.FileChooseAction;
import com.airbnb.lottie.LottieAnimationView;
import android.view.View;
import android.widget.ImageView;
import org.mozilla.focus.screenshot.CaptureRunnable;
import org.mozilla.rocket.nightmode.themed.ThemedImageButton;
import org.mozilla.rocket.nightmode.themed.ThemedLinearLayout;
import org.mozilla.rocket.nightmode.themed.ThemedView;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import org.mozilla.focus.widget.BackKeyHandleable;
import org.mozilla.focus.navigation.ScreenNavigator;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.arch.lifecycle.LifecycleOwner;
import org.mozilla.focus.locale.LocaleAwareFragment;

public class BrowserFragment extends LocaleAwareFragment implements LifecycleOwner, View$OnClickListener, View$OnLongClickListener, BrowserScreen, BackKeyHandleable
{
    private static final Handler HANDLER;
    private TransitionDrawable backgroundTransition;
    private ThemedRelativeLayout backgroundView;
    private ThemedView bottomMenuDivider;
    private ThemedLinearLayout browserContainer;
    private ThemedImageButton captureBtn;
    private CaptureRunnable.CaptureStateListener captureStateListener;
    private DownloadCallback downloadCallback;
    private ImageView downloadIndicator;
    private View downloadIndicatorIntro;
    private LottieAnimationView downloadingIndicator;
    private FileChooseAction fileChooseAction;
    private FindInPage findInPage;
    private TabView.FullscreenCallback fullscreenCallback;
    private AlertDialog geoDialog;
    private GeolocationPermissions$Callback geolocationCallback;
    private String geolocationOrigin;
    private boolean hasPendingScreenCaptureTask;
    private boolean isLoading;
    private WeakReference<LoadStateListener> loadStateListenerWeakReference;
    final SessionManager.Observer managerObserver;
    private ThemedImageButton menuBtn;
    private ThemedImageButton newTabBtn;
    private PermissionHandler permissionHandler;
    private AnimatedProgressBar progressView;
    private ThemedImageButton searchBtn;
    private SessionManager sessionManager;
    private SessionObserver sessionObserver;
    private ThemedImageView siteIdentity;
    private int systemVisibility;
    private TabCounter tabCounter;
    private ThemedLinearLayout toolbarRoot;
    private ThemedView urlBarDivider;
    private ThemedTextView urlView;
    private ViewGroup videoContainer;
    private Dialog webContextMenu;
    private ViewGroup webViewSlot;
    
    static {
        HANDLER = new Handler();
    }
    
    public BrowserFragment() {
        this.systemVisibility = -1;
        this.downloadCallback = new DownloadCallback();
        this.isLoading = false;
        this.loadStateListenerWeakReference = new WeakReference<LoadStateListener>(null);
        this.hasPendingScreenCaptureTask = false;
        this.sessionObserver = new SessionObserver();
        this.managerObserver = new SessionManagerObserver(this.sessionObserver);
    }
    
    private void acceptGeoRequest(final boolean b) {
        if (this.geolocationCallback == null) {
            return;
        }
        if (b) {
            GeoPermissionCache.putAllowed(this.geolocationOrigin, Boolean.TRUE);
        }
        this.geolocationCallback.invoke(this.geolocationOrigin, true, false);
        this.geolocationOrigin = "";
        this.geolocationCallback = null;
    }
    
    private void dismissDownloadIndicatorIntroView() {
        if (this.downloadIndicatorIntro != null) {
            this.downloadIndicatorIntro.setVisibility(8);
        }
    }
    
    private Bitmap getPageBitmap(final WebView webView) {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        try {
            final Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), (int)(webView.getContentHeight() * displayMetrics.density), Bitmap$Config.RGB_565);
            webView.draw(new Canvas(bitmap));
            return bitmap;
        }
        catch (Exception | OutOfMemoryError ex) {
            return null;
        }
    }
    
    private void hideFindInPage() {
        this.findInPage.hide();
    }
    
    private void initialiseNormalBrowserUi() {
        this.urlView.setOnClickListener((View$OnClickListener)this);
    }
    
    private boolean isPopupWindowAllowed() {
        return ScreenNavigator.get(this.getContext()).isBrowserInForeground() && !TabTray.isShowing(this.getFragmentManager());
    }
    
    private boolean isStartedFromExternalApp() {
        final FragmentActivity activity = this.getActivity();
        final boolean b = false;
        if (activity == null) {
            return false;
        }
        final Intent intent = activity.getIntent();
        final boolean b2 = intent != null && intent.getBooleanExtra("is_internal_request", false);
        boolean b3 = b;
        if (intent != null) {
            b3 = b;
            if ("android.intent.action.VIEW".equals(intent.getAction())) {
                b3 = b;
                if (!b2) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    private boolean isTabRestoredComplete() {
        if (this.getActivity() instanceof TabRestoreMonitor) {
            return ((TabRestoreMonitor)this.getActivity()).isTabRestoredComplete();
        }
        if (!AppConstants.isDevBuild()) {
            return true;
        }
        throw new RuntimeException("Base activity needs to implement TabRestoreMonitor");
    }
    
    private void queueDownload(final Download download) {
        if (this.getActivity() != null && download != null) {
            new EnqueueDownloadTask(this.getActivity(), download, this.getUrl()).execute((Object[])new Void[0]);
        }
    }
    
    private void rejectGeoRequest(final boolean b) {
        if (this.geolocationCallback == null) {
            return;
        }
        if (b) {
            GeoPermissionCache.putAllowed(this.geolocationOrigin, Boolean.FALSE);
        }
        this.geolocationCallback.invoke(this.geolocationOrigin, false, false);
        this.geolocationOrigin = "";
        this.geolocationCallback = null;
    }
    
    private void showGeolocationPermissionPrompt() {
        if (!this.isPopupWindowAllowed()) {
            return;
        }
        if (this.geolocationCallback == null) {
            return;
        }
        if (this.geoDialog != null && this.geoDialog.isShowing()) {
            return;
        }
        final Boolean allowed = GeoPermissionCache.getAllowed(this.geolocationOrigin);
        if (allowed != null) {
            this.geolocationCallback.invoke(this.geolocationOrigin, (boolean)allowed, false);
        }
        else {
            (this.geoDialog = this.buildGeoPromptDialog()).show();
        }
    }
    
    private void showLoadingAndCapture() {
        if (!this.isResumed()) {
            return;
        }
        this.hasPendingScreenCaptureTask = false;
        final ScreenCaptureDialogFragment instance = ScreenCaptureDialogFragment.newInstance();
        instance.show(this.getChildFragmentManager(), "capturingFragment");
        BrowserFragment.HANDLER.postDelayed((Runnable)new CaptureRunnable(this.getContext(), this, instance, this.getActivity().findViewById(2131296374)), 150L);
    }
    
    private void updateIsLoading(final boolean isLoading) {
        this.isLoading = isLoading;
        final LoadStateListener loadStateListener = this.loadStateListenerWeakReference.get();
        if (loadStateListener != null) {
            loadStateListener.isLoadingChanged(isLoading);
        }
    }
    
    private void updateURL(final String s) {
        if (UrlUtils.isInternalErrorURL(s)) {
            return;
        }
        this.urlView.setText((CharSequence)UrlUtils.stripUserInfo(s));
    }
    
    @Override
    public void applyLocale() {
        new WebView(this.getContext()).destroy();
    }
    
    public AlertDialog buildGeoPromptDialog() {
        final View inflate = LayoutInflater.from(this.getContext()).inflate(2131492943, (ViewGroup)null);
        final CheckedTextView checkedTextView = (CheckedTextView)inflate.findViewById(2131296360);
        checkedTextView.setText((CharSequence)this.getString(2131755223, new Object[] { this.getString(2131755062) }));
        checkedTextView.setOnClickListener((View$OnClickListener)new _$$Lambda$BrowserFragment$qv_dY4EfQ8BL4okjukxNbzZQYjI(checkedTextView));
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setView(inflate).setMessage(this.getString(2131755222, new Object[] { this.geolocationOrigin })).setCancelable(true).setPositiveButton(this.getString(2131755220), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                BrowserFragment.this.acceptGeoRequest(checkedTextView.isChecked());
            }
        }).setNegativeButton(this.getString(2131755221), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                BrowserFragment.this.rejectGeoRequest(checkedTextView.isChecked());
            }
        }).setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                BrowserFragment.this.rejectGeoRequest(false);
            }
        }).setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                BrowserFragment.this.rejectGeoRequest(false);
            }
        });
        return builder.create();
    }
    
    public boolean canGoBack() {
        return this.sessionManager.getFocusSession() != null && this.sessionManager.getFocusSession().getCanGoBack();
    }
    
    public boolean canGoForward() {
        return this.sessionManager.getFocusSession() != null && this.sessionManager.getFocusSession().getCanGoForward();
    }
    
    public boolean capturePage(final ScreenshotCallback screenshotCallback) {
        final Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession == null) {
            return false;
        }
        final TabView tabView = focusSession.getEngineSession().getTabView();
        if (tabView == null || !(tabView instanceof WebView)) {
            return false;
        }
        final Bitmap pageBitmap = this.getPageBitmap((WebView)tabView);
        if (pageBitmap == null) {
            return false;
        }
        screenshotCallback.onCaptureComplete(tabView.getTitle(), tabView.getUrl(), pageBitmap);
        return true;
    }
    
    public void dismissGeoDialog() {
        if (this.geoDialog != null) {
            this.geoDialog.dismiss();
            this.geoDialog = null;
        }
    }
    
    public void dismissWebContextMenu() {
        if (this.webContextMenu != null) {
            this.webContextMenu.dismiss();
            this.webContextMenu = null;
        }
    }
    
    public CaptureRunnable.CaptureStateListener getCaptureStateListener() {
        return this.captureStateListener;
    }
    
    public Fragment getFragment() {
        return this;
    }
    
    public String getUrl() {
        return this.urlView.getText().toString();
    }
    
    public void goBack() {
        final Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabView tabView = focusSession.getEngineSession().getTabView();
            if (tabView == null || !tabView.canGoBack()) {
                return;
            }
            final WebBackForwardList copyBackForwardList = ((WebView)tabView).copyBackForwardList();
            this.updateURL(copyBackForwardList.getItemAtIndex(copyBackForwardList.getCurrentIndex() - 1).getUrl());
            tabView.goBack();
        }
    }
    
    public void goBackground() {
        final Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                engineSession.detach();
                final TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    this.webViewSlot.removeView(tabView.getView());
                }
            }
        }
    }
    
    public void goForeground() {
        final Session focusSession = this.sessionManager.getFocusSession();
        if (this.webViewSlot.getChildCount() == 0 && focusSession != null) {
            final TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                final TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    this.webViewSlot.addView(tabView.getView());
                }
            }
        }
        this.setNightModeEnabled(Settings.getInstance((Context)this.getActivity()).isNightModeEnable());
    }
    
    public void goForward() {
        final Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabView tabView = focusSession.getEngineSession().getTabView();
            if (tabView == null) {
                return;
            }
            final WebBackForwardList copyBackForwardList = ((WebView)tabView).copyBackForwardList();
            this.updateURL(copyBackForwardList.getItemAtIndex(copyBackForwardList.getCurrentIndex() + 1).getUrl());
            tabView.goForward();
        }
    }
    
    public boolean isLoading() {
        return this.isLoading;
    }
    
    public void loadUrl(final String str, final boolean b, final boolean b2, final Runnable runnable) {
        this.updateURL(str);
        if (SupportUtils.isUrl(str)) {
            if (b) {
                this.sessionManager.addTab(str, TabUtil.argument(null, b2, true));
                this.dismissDownloadIndicatorIntroView();
                ThreadUtils.postToMainThread(runnable);
            }
            else {
                final Session focusSession = this.sessionManager.getFocusSession();
                if (focusSession != null && focusSession.getEngineSession().getTabView() != null) {
                    focusSession.getEngineSession().getTabView().loadUrl(str);
                    runnable.run();
                }
                else {
                    this.sessionManager.addTab(str, TabUtil.argument(null, b2, true));
                    ThreadUtils.postToMainThread(runnable);
                }
            }
        }
        else if (AppConstants.isDevBuild()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("trying to open a invalid url: ");
            sb.append(str);
            throw new RuntimeException(sb.toString());
        }
    }
    
    public void onActivityResult(int n, final int n2, final Intent intent) {
        this.permissionHandler.onActivityResult(this.getActivity(), n, n2, intent);
        if (n == 103) {
            if (this.fileChooseAction != null && !this.fileChooseAction.onFileChose(n2, intent)) {
                n = 0;
            }
            else {
                n = 1;
            }
            if (n != 0) {
                this.fileChooseAction = null;
            }
        }
    }
    
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.permissionHandler = new PermissionHandler(new PermissionHandle() {
            private void actionCaptureGranted() {
                BrowserFragment.this.hasPendingScreenCaptureTask = true;
            }
            
            private void actionDownloadGranted(final Parcelable parcelable) {
                BrowserFragment.this.queueDownload((Download)parcelable);
            }
            
            private void actionPickFileGranted() {
                if (BrowserFragment.this.fileChooseAction != null) {
                    BrowserFragment.this.fileChooseAction.startChooserActivity();
                }
            }
            
            private void doActionGrantedOrSetting(final String s, final int n, final Parcelable parcelable) {
                switch (n) {
                    default: {
                        throw new IllegalArgumentException("Unknown actionId");
                    }
                    case 3: {
                        this.actionCaptureGranted();
                        break;
                    }
                    case 2: {
                        BrowserFragment.this.showGeolocationPermissionPrompt();
                        break;
                    }
                    case 1: {
                        this.actionPickFileGranted();
                        break;
                    }
                    case 0: {
                        this.actionDownloadGranted(parcelable);
                        break;
                    }
                }
            }
            
            private int getAskAgainSnackBarString(final int n) {
                if (n == 0 || n == 1 || n == 3) {
                    return 2131755291;
                }
                if (n == 2) {
                    return 2131755289;
                }
                throw new IllegalArgumentException("Unknown Action");
            }
            
            private int getPermissionDeniedToastString(final int n) {
                if (n == 0 || n == 1 || n == 3) {
                    return 2131755292;
                }
                if (n == 2) {
                    return 2131755290;
                }
                throw new IllegalArgumentException("Unknown Action");
            }
            
            @Override
            public void doActionDirect(final String s, final int n, final Parcelable parcelable) {
                switch (n) {
                    default: {
                        throw new IllegalArgumentException("Unknown actionId");
                    }
                    case 3: {
                        BrowserFragment.this.showLoadingAndCapture();
                        break;
                    }
                    case 2: {
                        BrowserFragment.this.showGeolocationPermissionPrompt();
                        break;
                    }
                    case 1: {
                        BrowserFragment.this.fileChooseAction.startChooserActivity();
                        break;
                    }
                    case 0: {
                        if (BrowserFragment.this.getContext() == null) {
                            Log.w("browser_screen", "No context to use, abort callback onDownloadStart");
                            return;
                        }
                        final Download download = (Download)parcelable;
                        if (ContextCompat.checkSelfPermission(BrowserFragment.this.getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                            BrowserFragment.this.queueDownload(download);
                            break;
                        }
                        break;
                    }
                }
            }
            
            @Override
            public void doActionGranted(final String s, final int n, final Parcelable parcelable) {
                this.doActionGrantedOrSetting(s, n, parcelable);
            }
            
            @Override
            public void doActionNoPermission(final String s, final int n, final Parcelable parcelable) {
                switch (n) {
                    default: {
                        throw new IllegalArgumentException("Unknown actionId");
                    }
                    case 2: {
                        if (BrowserFragment.this.geolocationCallback != null) {
                            ThreadUtils.postToMainThread(new _$$Lambda$BrowserFragment$1$O1zuVOux1ZaId_gKigNygb5Rzpc(this));
                        }
                    }
                    case 1: {
                        if (BrowserFragment.this.fileChooseAction != null) {
                            BrowserFragment.this.fileChooseAction.cancel();
                            BrowserFragment.this.fileChooseAction = null;
                        }
                    }
                    case 0:
                    case 3: {}
                }
            }
            
            @Override
            public void doActionSetting(final String s, final int n, final Parcelable parcelable) {
                this.doActionGrantedOrSetting(s, n, parcelable);
            }
            
            @Override
            public Snackbar makeAskAgainSnackBar(final int n) {
                return PermissionHandler.makeAskAgainSnackBar(BrowserFragment.this, BrowserFragment.this.getActivity().findViewById(2131296374), this.getAskAgainSnackBarString(n));
            }
            
            @Override
            public void permissionDeniedToast(final int n) {
                Toast.makeText(BrowserFragment.this.getContext(), this.getPermissionDeniedToastString(n), 1).show();
            }
            
            @Override
            public void requestPermissions(final int n) {
                switch (n) {
                    default: {
                        throw new IllegalArgumentException("Unknown Action");
                    }
                    case 2: {
                        BrowserFragment.this.requestPermissions(new String[] { "android.permission.ACCESS_FINE_LOCATION" }, n);
                        break;
                    }
                    case 1: {
                        BrowserFragment.this.requestPermissions(new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, n);
                        break;
                    }
                    case 0:
                    case 3: {
                        BrowserFragment.this.requestPermissions(new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, n);
                        break;
                    }
                }
            }
        });
    }
    
    public boolean onBackPressed() {
        if (this.findInPage.onBackPressed()) {
            return true;
        }
        if (this.canGoBack()) {
            this.goBack();
        }
        else {
            final Session focusSession = this.sessionManager.getFocusSession();
            if (focusSession == null) {
                return false;
            }
            if (!focusSession.isFromExternal() && !focusSession.hasParentTab()) {
                ScreenNavigator.get(this.getContext()).popToHomeScreen(true);
            }
            else {
                this.sessionManager.closeTab(focusSession.getId());
            }
        }
        return true;
    }
    
    public void onCaptureClicked() {
        this.permissionHandler.tryAction(this, "android.permission.WRITE_EXTERNAL_STORAGE", 3, null);
    }
    
    public void onClick(final View view) {
        switch (view.getId()) {
            default: {
                throw new IllegalArgumentException("Unhandled menu item in BrowserFragment");
            }
            case 2131296411: {
                FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_URL_INPUT, this.getUrl());
                TelemetryWrapper.clickUrlbar();
                break;
            }
            case 2131296357: {
                FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_TAB_TRAY, null);
                TelemetryWrapper.showTabTrayToolbar();
                break;
            }
            case 2131296356: {
                FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_URL_INPUT, this.getUrl());
                TelemetryWrapper.clickToolbarSearch();
                break;
            }
            case 2131296354: {
                HomeFragmentViewState.reset();
                ScreenNavigator.get(this.getContext()).addHomeScreen(true);
                TelemetryWrapper.clickAddTabToolbar();
                break;
            }
            case 2131296349: {
                FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_MENU, null);
                TelemetryWrapper.showMenuToolbar();
                break;
            }
            case 2131296345: {
                this.onCaptureClicked();
                break;
            }
        }
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2131492954, viewGroup, false);
        this.videoContainer = (ViewGroup)inflate.findViewById(2131296717);
        this.browserContainer = (ThemedLinearLayout)inflate.findViewById(2131296337);
        this.urlView = (ThemedTextView)inflate.findViewById(2131296411);
        this.backgroundView = (ThemedRelativeLayout)inflate.findViewById(2131296301);
        inflate.findViewById(2131296295).setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)_$$Lambda$BrowserFragment$E4DFpfvxoIczJjyXxreM27CrkUE.INSTANCE);
        this.backgroundTransition = (TransitionDrawable)this.backgroundView.getBackground();
        this.tabCounter = (TabCounter)inflate.findViewById(2131296357);
        this.newTabBtn = (ThemedImageButton)inflate.findViewById(2131296354);
        this.searchBtn = (ThemedImageButton)inflate.findViewById(2131296356);
        this.captureBtn = (ThemedImageButton)inflate.findViewById(2131296345);
        this.menuBtn = (ThemedImageButton)inflate.findViewById(2131296349);
        this.toolbarRoot = (ThemedLinearLayout)inflate.findViewById(2131296701);
        this.bottomMenuDivider = (ThemedView)inflate.findViewById(2131296327);
        this.urlBarDivider = (ThemedView)inflate.findViewById(2131296713);
        if (this.tabCounter != null) {
            this.tabCounter.setOnClickListener((View$OnClickListener)this);
        }
        if (this.newTabBtn != null) {
            this.newTabBtn.setOnClickListener((View$OnClickListener)this);
        }
        if (this.searchBtn != null) {
            this.searchBtn.setOnClickListener((View$OnClickListener)this);
        }
        if (this.captureBtn != null) {
            this.captureBtn.setOnClickListener((View$OnClickListener)this);
        }
        if (this.menuBtn != null) {
            this.menuBtn.setOnClickListener((View$OnClickListener)this);
            this.menuBtn.setOnLongClickListener((View$OnLongClickListener)this);
        }
        this.siteIdentity = (ThemedImageView)inflate.findViewById(2131296648);
        this.findInPage = new FindInPage(inflate);
        this.progressView = (AnimatedProgressBar)inflate.findViewById(2131296576);
        this.initialiseNormalBrowserUi();
        this.webViewSlot = (ViewGroup)inflate.findViewById(2131296723);
        (this.sessionManager = TabsSessionProvider.getOrThrow(this.getActivity())).register(this.managerObserver, (LifecycleOwner)this, false);
        if (this.tabCounter != null && this.isTabRestoredComplete()) {
            this.tabCounter.setCount(this.sessionManager.getTabsCount());
        }
        this.setNightModeEnabled(Settings.getInstance((Context)this.getActivity()).isNightModeEnable());
        this.downloadingIndicator = (LottieAnimationView)inflate.findViewById(2131296418);
        this.downloadIndicator = (ImageView)inflate.findViewById(2131296417);
        viewGroup = (ViewGroup)inflate.findViewById(2131296338);
        Inject.obtainDownloadIndicatorViewModel(this.getActivity()).getDownloadIndicatorObservable().observe(this.getViewLifecycleOwner(), new _$$Lambda$BrowserFragment$l94_q85PEWz_WW25q4mSUZ5WGEE(this, viewGroup));
        return inflate;
    }
    
    public void onDestroyView() {
        this.sessionManager.unregister(this.managerObserver);
        super.onDestroyView();
    }
    
    public boolean onLongClick(final View view) {
        if (view.getId() == 2131296349) {
            FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_DOWNLOAD_PANEL, null);
            TelemetryWrapper.longPressDownloadIndicator();
            return false;
        }
        throw new IllegalArgumentException("Unhandled long click menu item in BrowserFragment");
    }
    
    public void onPause() {
        this.sessionManager.pause();
        super.onPause();
    }
    
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        this.permissionHandler.onRequestPermissionsResult(this.getContext(), n, array, array2);
    }
    
    @Override
    public void onResume() {
        this.sessionManager.resume();
        super.onResume();
        if (this.hasPendingScreenCaptureTask) {
            this.showLoadingAndCapture();
            this.hasPendingScreenCaptureTask = false;
        }
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        this.permissionHandler.onSaveInstanceState(bundle);
        if (this.sessionManager.getFocusSession() != null) {
            final TabViewEngineSession engineSession = this.sessionManager.getFocusSession().getEngineSession();
            if (engineSession != null && engineSession.getTabView() != null) {
                engineSession.getTabView().saveViewState(bundle);
            }
        }
        if (bundle.containsKey("WEBVIEW_CHROMIUM_STATE") && bundle.getByteArray("WEBVIEW_CHROMIUM_STATE").length > 300000) {
            bundle.remove("WEBVIEW_CHROMIUM_STATE");
        }
        super.onSaveInstanceState(bundle);
    }
    
    public void onStop() {
        if (this.systemVisibility != -1) {
            final Session focusSession = this.sessionManager.getFocusSession();
            if (focusSession != null) {
                final TabView tabView = focusSession.getEngineSession().getTabView();
                if (tabView != null) {
                    tabView.performExitFullScreen();
                }
            }
        }
        this.dismissGeoDialog();
        super.onStop();
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle != null) {
            final Session focusSession = this.sessionManager.getFocusSession();
            if (focusSession != null) {
                final TabView tabView = focusSession.getEngineSession().getTabView();
                if (tabView != null) {
                    tabView.restoreViewState(bundle);
                }
                else {
                    this.sessionManager.switchToTab(focusSession.getId());
                }
            }
        }
    }
    
    public void onViewStateRestored(final Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (bundle != null) {
            this.permissionHandler.onRestoreInstanceState(bundle);
        }
    }
    
    public void reload() {
        final Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabView tabView = focusSession.getEngineSession().getTabView();
            if (tabView == null) {
                return;
            }
            tabView.reload();
        }
    }
    
    public void setContentBlockingEnabled(final boolean contentBlockingEnabled) {
        final Iterator<Session> iterator = this.sessionManager.getTabs().iterator();
        while (iterator.hasNext()) {
            final TabViewEngineSession engineSession = iterator.next().getEngineSession();
            if (engineSession != null && engineSession.getTabView() != null) {
                engineSession.getTabView().setContentBlockingEnabled(contentBlockingEnabled);
            }
        }
    }
    
    public void setImageBlockingEnabled(final boolean imageBlockingEnabled) {
        final Iterator<Session> iterator = this.sessionManager.getTabs().iterator();
        while (iterator.hasNext()) {
            final TabViewEngineSession engineSession = iterator.next().getEngineSession();
            if (engineSession != null && engineSession.getTabView() != null) {
                engineSession.getTabView().setImageBlockingEnabled(imageBlockingEnabled);
            }
        }
    }
    
    public void setNightModeEnabled(final boolean b) {
        this.browserContainer.setNightMode(b);
        this.toolbarRoot.setNightMode(b);
        this.urlView.setNightMode(b);
        this.siteIdentity.setNightMode(b);
        this.newTabBtn.setNightMode(b);
        this.searchBtn.setNightMode(b);
        this.captureBtn.setNightMode(b);
        this.menuBtn.setNightMode(b);
        this.tabCounter.setNightMode(b);
        this.bottomMenuDivider.setNightMode(b);
        this.backgroundView.setNightMode(b);
        this.urlBarDivider.setNightMode(b);
        ViewUtils.updateStatusBarStyle(b ^ true, this.getActivity().getWindow());
    }
    
    public void showFindInPage() {
        final Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            this.findInPage.show(focusSession);
            TelemetryWrapper.findInPage(TelemetryWrapper.FIND_IN_PAGE.OPEN_BY_MENU);
        }
    }
    
    public void showMyShotOnBoarding() {
        final FragmentActivity activity = this.getActivity();
        if (activity == null) {
            return;
        }
        final Settings.EventHistory eventHistory = Settings.getInstance((Context)activity).getEventHistory();
        if (!eventHistory.contains("show_my_shot_on_boarding_dialog")) {
            eventHistory.add("show_my_shot_on_boarding_dialog");
            FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_MY_SHOT_ON_BOARDING, null);
        }
    }
    
    public void stop() {
        final Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            final TabView tabView = focusSession.getEngineSession().getTabView();
            if (tabView == null) {
                return;
            }
            tabView.stopLoading();
        }
    }
    
    public void switchToTab(final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            this.sessionManager.switchToTab(s);
        }
    }
    
    class DownloadCallback implements org.mozilla.rocket.tabs.web.DownloadCallback
    {
        @Override
        public void onDownloadStart(final Download download) {
            final FragmentActivity activity = BrowserFragment.this.getActivity();
            if (activity != null && activity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                BrowserFragment.this.permissionHandler.tryAction(BrowserFragment.this, "android.permission.WRITE_EXTERNAL_STORAGE", 0, (Parcelable)download);
            }
        }
    }
    
    private final class HistoryInserter
    {
        private WeakHashMap<Session, String> failingUrls;
        private WeakHashMap<Session, String> lastInsertedUrls;
        
        private HistoryInserter() {
            this.failingUrls = new WeakHashMap<Session, String>();
            this.lastInsertedUrls = new WeakHashMap<Session, String>();
        }
        
        private String getFailingUrl(final Session key) {
            String s;
            if (TextUtils.isEmpty((CharSequence)(s = this.failingUrls.get(key)))) {
                s = "";
            }
            return s;
        }
        
        private String getLastInsertedUrl(final Session key) {
            String s;
            if (TextUtils.isEmpty((CharSequence)(s = this.lastInsertedUrls.get(key)))) {
                s = "";
            }
            return s;
        }
        
        private void insertBrowsingHistory(final Session key) {
            final String url = BrowserFragment.this.getUrl();
            final String lastInsertedUrl = this.getLastInsertedUrl(key);
            if (TextUtils.isEmpty((CharSequence)url)) {
                return;
            }
            if (url.equals(this.getFailingUrl(key))) {
                return;
            }
            if (url.equals(lastInsertedUrl)) {
                return;
            }
            final TabView tabView = key.getEngineSession().getTabView();
            if (tabView != null) {
                tabView.insertBrowsingHistory();
            }
            this.lastInsertedUrls.put(key, url);
        }
        
        void onTabFinished(final Session session) {
            this.insertBrowsingHistory(session);
        }
        
        void onTabStarted(final Session key) {
            this.lastInsertedUrls.remove(key);
        }
        
        void updateFailingUrl(final Session key, final String value, final boolean b) {
            final String anObject = this.failingUrls.get(key);
            if (!b && !value.equals(anObject)) {
                this.failingUrls.remove(key);
            }
            else {
                this.failingUrls.put(key, value);
            }
        }
    }
    
    public interface LoadStateListener
    {
        void isLoadingChanged(final boolean p0);
    }
    
    public interface ScreenshotCallback
    {
        void onCaptureComplete(final String p0, final String p1, final Bitmap p2);
    }
    
    class SessionManagerObserver implements Observer
    {
        private SessionObserver sessionObserver;
        private ValueAnimator tabTransitionAnimator;
        
        SessionManagerObserver(final SessionObserver sessionObserver) {
            this.sessionObserver = sessionObserver;
        }
        
        private View findExistingTabView(final ViewGroup viewGroup) {
            for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = viewGroup.getChildAt(i);
                if (child instanceof TabView) {
                    return ((TabView)child).getView();
                }
            }
            return null;
        }
        
        private void onTabAddedByContextMenu(final Session session, final Bundle bundle) {
            if (!TabUtil.toFocus(bundle)) {
                Snackbar.make((View)BrowserFragment.this.webViewSlot, 2131755272, 0).setAction(2131755273, (View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        BrowserFragment.this.sessionManager.switchToTab(session.getId());
                    }
                }).show();
            }
        }
        
        private void refreshChrome(final Session session) {
            BrowserFragment.this.geolocationOrigin = "";
            BrowserFragment.this.geolocationCallback = null;
            BrowserFragment.this.dismissGeoDialog();
            BrowserFragment.this.updateURL(session.getUrl());
            BrowserFragment.this.progressView.setProgress(0);
            BrowserFragment.this.siteIdentity.setImageLevel((int)(session.getSecurityInfo().getSecure() ? 1 : 0));
            BrowserFragment.this.hideFindInPage();
        }
        
        private void startTransitionAnimation(final View view, final View view2, final Runnable runnable) {
            this.stopTabTransition();
            view2.setAlpha(0.0f);
            if (view != null) {
                view.setAlpha(1.0f);
            }
            (this.tabTransitionAnimator = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f }).setDuration((long)view2.getResources().getInteger(2131361813))).addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    final float floatValue = (float)valueAnimator.getAnimatedValue();
                    if (view != null) {
                        view.setAlpha(1.0f - floatValue);
                    }
                    view2.setAlpha(floatValue);
                }
            });
            this.tabTransitionAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    if (runnable != null) {
                        runnable.run();
                    }
                    view2.setAlpha(1.0f);
                    if (view != null) {
                        view.setAlpha(1.0f);
                    }
                }
            });
            this.tabTransitionAnimator.start();
        }
        
        private void stopTabTransition() {
            if (this.tabTransitionAnimator != null && this.tabTransitionAnimator.isRunning()) {
                this.tabTransitionAnimator.end();
            }
        }
        
        private void transitToTab(final Session session) {
            final TabView tabView = session.getEngineSession().getTabView();
            if (tabView != null) {
                if (session.getEngineSession() != null) {
                    session.getEngineSession().detach();
                }
                BrowserFragment.this.webViewSlot.removeView(this.findExistingTabView(BrowserFragment.this.webViewSlot));
                final View view = tabView.getView();
                BrowserFragment.this.webViewSlot.addView(view);
                this.sessionObserver.changeSession(session);
                this.startTransitionAnimation(null, view, null);
                return;
            }
            throw new RuntimeException("Tabview should be created at this moment and never be null");
        }
        
        @Override
        public boolean handleExternalUrl(final String s) {
            return this.sessionObserver.handleExternalUrl(s);
        }
        
        @Override
        public void onFocusChanged(final Session session, final Factor factor) {
            if (session == null) {
                if (factor == Factor.FACTOR_NO_FOCUS && !BrowserFragment.this.isStartedFromExternalApp()) {
                    ScreenNavigator.get(BrowserFragment.this.getContext()).popToHomeScreen(true);
                }
                else {
                    BrowserFragment.this.getActivity().finish();
                }
            }
            else {
                this.transitToTab(session);
                this.refreshChrome(session);
            }
        }
        
        @Override
        public void onHttpAuthRequest(final TabViewClient.HttpAuthCallback httpAuthCallback, final String s, final String s2) {
            this.sessionObserver.onHttpAuthRequest(httpAuthCallback, s, s2);
        }
        
        @Override
        public void onSessionAdded(final Session session, final Bundle bundle) {
            if (bundle == null) {
                return;
            }
            if (bundle.getInt("extra_bkg_tab_src", -1) == 0) {
                this.onTabAddedByContextMenu(session, bundle);
            }
        }
        
        @Override
        public void onSessionCountChanged(final int countWithAnimation) {
            if (BrowserFragment.this.isTabRestoredComplete()) {
                BrowserFragment.this.tabCounter.setCountWithAnimation(countWithAnimation);
            }
        }
        
        @Override
        public boolean onShowFileChooser(final TabViewEngineSession tabViewEngineSession, final ValueCallback<Uri[]> valueCallback, final WebChromeClient$FileChooserParams webChromeClient$FileChooserParams) {
            return this.sessionObserver.onShowFileChooser(tabViewEngineSession, valueCallback, webChromeClient$FileChooserParams);
        }
        
        @Override
        public void updateFailingUrl(final String s, final boolean b) {
            this.sessionObserver.updateFailingUrl(s, b);
        }
    }
    
    class SessionObserver implements Session.Observer, Client
    {
        private HistoryInserter historyInserter;
        private String loadedUrl;
        private Session session;
        
        SessionObserver() {
            this.historyInserter = new HistoryInserter();
            this.loadedUrl = null;
        }
        
        private boolean isForegroundSession(final Session session) {
            return BrowserFragment.this.sessionManager.getFocusSession() == session;
        }
        
        private void updateUrlFromWebView(final Session session) {
            if (BrowserFragment.this.sessionManager.getFocusSession() != null) {
                this.onUrlChanged(session, BrowserFragment.this.sessionManager.getFocusSession().getUrl());
            }
        }
        
        void changeSession(final Session session) {
            if (this.session != null) {
                this.session.unregister((Session.Observer)this);
            }
            this.session = session;
            if (this.session != null) {
                this.session.register((Session.Observer)this);
            }
        }
        
        @Override
        public boolean handleExternalUrl(final String s) {
            if (BrowserFragment.this.getContext() == null) {
                Log.w("browser_screen", "No context to use, abort callback handleExternalUrl");
                return false;
            }
            return IntentUtils.handleExternalUri(BrowserFragment.this.getContext(), s);
        }
        
        @Override
        public boolean onDownload(final Session session, final mozilla.components.browser.session.Download download) {
            final FragmentActivity activity = BrowserFragment.this.getActivity();
            if (activity != null && activity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                BrowserFragment.this.permissionHandler.tryAction(BrowserFragment.this, "android.permission.WRITE_EXTERNAL_STORAGE", 0, (Parcelable)new Download(download.getUrl(), download.getFileName(), download.getUserAgent(), "", download.getContentType(), download.getContentLength(), false));
                return true;
            }
            return false;
        }
        
        @Override
        public void onEnterFullScreen(final TabView.FullscreenCallback fullscreenCallback, final View view) {
            if (this.session == null) {
                return;
            }
            if (!this.isForegroundSession(this.session)) {
                fullscreenCallback.fullScreenExited();
                return;
            }
            BrowserFragment.this.fullscreenCallback = fullscreenCallback;
            if (this.session.getEngineSession().getTabView() != null && view != null) {
                BrowserFragment.this.browserContainer.setVisibility(4);
                BrowserFragment.this.videoContainer.addView(view, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
                BrowserFragment.this.videoContainer.setVisibility(0);
                BrowserFragment.this.systemVisibility = ViewUtils.switchToImmersiveMode(BrowserFragment.this.getActivity());
            }
        }
        
        @Override
        public void onExitFullScreen() {
            if (this.session == null) {
                return;
            }
            BrowserFragment.this.videoContainer.removeAllViews();
            BrowserFragment.this.videoContainer.setVisibility(8);
            BrowserFragment.this.browserContainer.setVisibility(0);
            if (BrowserFragment.this.systemVisibility != -1) {
                ViewUtils.exitImmersiveMode(BrowserFragment.this.systemVisibility, BrowserFragment.this.getActivity());
            }
            if (BrowserFragment.this.fullscreenCallback != null) {
                BrowserFragment.this.fullscreenCallback.fullScreenExited();
                BrowserFragment.this.fullscreenCallback = null;
            }
            if (this.session.getEngineSession().getTabView() instanceof WebView) {
                ((WebView)this.session.getEngineSession().getTabView()).clearFocus();
            }
        }
        
        @Override
        public void onFindResult(final Session session, final mozilla.components.browser.session.Session.FindResult findResult) {
            BrowserFragment.this.findInPage.onFindResultReceived(findResult);
        }
        
        @Override
        public void onGeolocationPermissionsShowPrompt(final String s, final GeolocationPermissions$Callback geolocationPermissions$Callback) {
            if (this.session == null) {
                return;
            }
            if (this.isForegroundSession(this.session) && BrowserFragment.this.isPopupWindowAllowed()) {
                BrowserFragment.this.geolocationOrigin = s;
                BrowserFragment.this.geolocationCallback = geolocationPermissions$Callback;
                BrowserFragment.this.permissionHandler.tryAction(BrowserFragment.this, "android.permission.ACCESS_FINE_LOCATION", 2, null);
            }
        }
        
        @Override
        public void onHttpAuthRequest(final TabViewClient.HttpAuthCallback httpAuthCallback, final String s, final String s2) {
            final HttpAuthenticationDialogBuilder.Builder setOkListener = new HttpAuthenticationDialogBuilder.Builder((Context)BrowserFragment.this.getActivity(), s, s2).setOkListener(new _$$Lambda$BrowserFragment$SessionObserver$3ofKWXqzOJQXPDqF9Nkl_b0cv_s(httpAuthCallback));
            httpAuthCallback.getClass();
            final HttpAuthenticationDialogBuilder build = setOkListener.setCancelListener(new _$$Lambda$2vHgiayWabNOvt9IdkeaydjRUNI(httpAuthCallback)).build();
            build.createDialog();
            build.show();
        }
        
        @Override
        public void onLoadingStateChanged(final Session session, final boolean b) {
            BrowserFragment.this.isLoading = b;
            if (b) {
                this.historyInserter.onTabStarted(session);
            }
            else {
                this.historyInserter.onTabFinished(session);
            }
            if (!this.isForegroundSession(session)) {
                return;
            }
            if (b) {
                this.loadedUrl = null;
                BrowserFragment.this.updateIsLoading(true);
                BrowserFragment.this.updateURL(session.getUrl());
                BrowserFragment.this.backgroundTransition.resetTransition();
            }
            else {
                this.updateUrlFromWebView(session);
                BrowserFragment.this.updateIsLoading(false);
                FragmentListener$_CC.notifyParent(BrowserFragment.this, FragmentListener.TYPE.UPDATE_MENU, null);
                BrowserFragment.this.backgroundTransition.startTransition(300);
            }
        }
        
        @Override
        public void onLongPress(final Session session, final TabView.HitTarget hitTarget) {
            if (BrowserFragment.this.getActivity() == null) {
                Log.w("browser_screen", "No context to use, abort callback onLongPress");
                return;
            }
            BrowserFragment.this.webContextMenu = WebContextMenu.show(false, BrowserFragment.this.getActivity(), BrowserFragment.this.downloadCallback, hitTarget);
        }
        
        @Override
        public void onNavigationStateChanged(final Session session, final boolean b, final boolean b2) {
        }
        
        @Override
        public void onProgress(final Session session, final int progress) {
            if (!this.isForegroundSession(session)) {
                return;
            }
            BrowserFragment.this.hideFindInPage();
            if (BrowserFragment.this.sessionManager.getFocusSession() != null) {
                final String url = BrowserFragment.this.sessionManager.getFocusSession().getUrl();
                final boolean equals = TextUtils.equals((CharSequence)url, (CharSequence)this.loadedUrl);
                if (BrowserFragment.this.progressView.getMax() != BrowserFragment.this.progressView.getProgress() && progress == BrowserFragment.this.progressView.getMax()) {
                    this.loadedUrl = url;
                }
                if (equals) {
                    return;
                }
            }
            BrowserFragment.this.progressView.setProgress(progress);
        }
        
        @Override
        public void onReceivedIcon(final Bitmap bitmap) {
        }
        
        @Override
        public void onSecurityChanged(final Session session, final boolean imageLevel) {
            BrowserFragment.this.siteIdentity.setImageLevel((int)(imageLevel ? 1 : 0));
        }
        
        @Override
        public boolean onShowFileChooser(final TabViewEngineSession tabViewEngineSession, final ValueCallback<Uri[]> valueCallback, final WebChromeClient$FileChooserParams webChromeClient$FileChooserParams) {
            if (!this.isForegroundSession(this.session)) {
                return false;
            }
            TelemetryWrapper.browseFilePermissionEvent();
            try {
                BrowserFragment.this.fileChooseAction = new FileChooseAction(BrowserFragment.this, valueCallback, webChromeClient$FileChooserParams);
                BrowserFragment.this.permissionHandler.tryAction(BrowserFragment.this, "android.permission.READ_EXTERNAL_STORAGE", 1, null);
                return true;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        
        @Override
        public void onTitleChanged(final Session session, final String s) {
            if (session == null) {
                return;
            }
            if (!this.isForegroundSession(session)) {
                return;
            }
            if (!BrowserFragment.this.getUrl().equals(session.getUrl())) {
                BrowserFragment.this.updateURL(session.getUrl());
            }
        }
        
        @Override
        public void onUrlChanged(final Session session, final String s) {
            if (!this.isForegroundSession(session)) {
                return;
            }
            BrowserFragment.this.updateURL(s);
        }
        
        @Override
        public void updateFailingUrl(final String s, final boolean b) {
            if (this.session == null) {
                return;
            }
            this.historyInserter.updateFailingUrl(this.session, s, b);
        }
    }
}
