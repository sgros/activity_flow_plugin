package org.mozilla.focus.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Dialog;
import android.arch.lifecycle.Lifecycle.State;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.content.ContextCompat;
import android.support.p004v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebView;
import android.widget.CheckedTextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import mozilla.components.browser.session.Session.FindResult;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.Inject;
import org.mozilla.focus.download.EnqueueDownloadTask;
import org.mozilla.focus.locale.LocaleAwareFragment;
import org.mozilla.focus.menu.WebContextMenu;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.navigation.ScreenNavigator.BrowserScreen;
import org.mozilla.focus.screenshot.CaptureRunnable;
import org.mozilla.focus.screenshot.CaptureRunnable.CaptureStateListener;
import org.mozilla.focus.tabs.TabCounter;
import org.mozilla.focus.tabs.tabtray.TabTray;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.telemetry.TelemetryWrapper.FIND_IN_PAGE;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.utils.FileChooseAction;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.Settings.EventHistory;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.focus.web.GeoPermissionCache;
import org.mozilla.focus.web.HttpAuthenticationDialogBuilder;
import org.mozilla.focus.web.HttpAuthenticationDialogBuilder.Builder;
import org.mozilla.focus.widget.AnimatedProgressBar;
import org.mozilla.focus.widget.BackKeyHandleable;
import org.mozilla.focus.widget.FindInPage;
import org.mozilla.focus.widget.FragmentListener.C0572-CC;
import org.mozilla.focus.widget.FragmentListener.TYPE;
import org.mozilla.focus.widget.TabRestoreMonitor;
import org.mozilla.permissionhandler.PermissionHandle;
import org.mozilla.permissionhandler.PermissionHandler;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.content.HomeFragmentViewState;
import org.mozilla.rocket.download.DownloadIndicatorIntroViewHelper;
import org.mozilla.rocket.download.DownloadIndicatorViewModel.Status;
import org.mozilla.rocket.nightmode.themed.ThemedImageButton;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import org.mozilla.rocket.nightmode.themed.ThemedLinearLayout;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;
import org.mozilla.rocket.nightmode.themed.ThemedView;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.SessionManager.Factor;
import org.mozilla.rocket.tabs.SessionManager.Observer;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabView.FullscreenCallback;
import org.mozilla.rocket.tabs.TabView.HitTarget;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;
import org.mozilla.rocket.tabs.TabViewEngineSession;
import org.mozilla.rocket.tabs.TabViewEngineSession.Client;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.rocket.tabs.utils.TabUtil;
import org.mozilla.rocket.tabs.web.Download;
import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.urlutils.UrlUtils;

public class BrowserFragment extends LocaleAwareFragment implements LifecycleOwner, OnClickListener, OnLongClickListener, BrowserScreen, BackKeyHandleable {
    private static final Handler HANDLER = new Handler();
    private TransitionDrawable backgroundTransition;
    private ThemedRelativeLayout backgroundView;
    private ThemedView bottomMenuDivider;
    private ThemedLinearLayout browserContainer;
    private ThemedImageButton captureBtn;
    private CaptureStateListener captureStateListener;
    private DownloadCallback downloadCallback = new DownloadCallback();
    private ImageView downloadIndicator;
    private View downloadIndicatorIntro;
    private LottieAnimationView downloadingIndicator;
    private FileChooseAction fileChooseAction;
    private FindInPage findInPage;
    private FullscreenCallback fullscreenCallback;
    private AlertDialog geoDialog;
    private Callback geolocationCallback;
    private String geolocationOrigin;
    private boolean hasPendingScreenCaptureTask = false;
    private boolean isLoading = false;
    private WeakReference<LoadStateListener> loadStateListenerWeakReference = new WeakReference(null);
    final Observer managerObserver = new SessionManagerObserver(this.sessionObserver);
    private ThemedImageButton menuBtn;
    private ThemedImageButton newTabBtn;
    private PermissionHandler permissionHandler;
    private AnimatedProgressBar progressView;
    private ThemedImageButton searchBtn;
    private SessionManager sessionManager;
    private SessionObserver sessionObserver = new SessionObserver();
    private ThemedImageView siteIdentity;
    private int systemVisibility = -1;
    private TabCounter tabCounter;
    private ThemedLinearLayout toolbarRoot;
    private ThemedView urlBarDivider;
    private ThemedTextView urlView;
    private ViewGroup videoContainer;
    private Dialog webContextMenu;
    private ViewGroup webViewSlot;

    /* renamed from: org.mozilla.focus.fragment.BrowserFragment$2 */
    class C04622 implements OnCancelListener {
        C04622() {
        }

        public void onCancel(DialogInterface dialogInterface) {
            BrowserFragment.this.rejectGeoRequest(false);
        }
    }

    /* renamed from: org.mozilla.focus.fragment.BrowserFragment$3 */
    class C04633 implements OnDismissListener {
        C04633() {
        }

        public void onDismiss(DialogInterface dialogInterface) {
            BrowserFragment.this.rejectGeoRequest(false);
        }
    }

    private final class HistoryInserter {
        private WeakHashMap<Session, String> failingUrls;
        private WeakHashMap<Session, String> lastInsertedUrls;

        private HistoryInserter() {
            this.failingUrls = new WeakHashMap();
            this.lastInsertedUrls = new WeakHashMap();
        }

        /* synthetic */ HistoryInserter(BrowserFragment browserFragment, C06931 c06931) {
            this();
        }

        /* Access modifiers changed, original: 0000 */
        public void onTabStarted(Session session) {
            this.lastInsertedUrls.remove(session);
        }

        /* Access modifiers changed, original: 0000 */
        public void onTabFinished(Session session) {
            insertBrowsingHistory(session);
        }

        /* Access modifiers changed, original: 0000 */
        public void updateFailingUrl(Session session, String str, boolean z) {
            String str2 = (String) this.failingUrls.get(session);
            if (z || str.equals(str2)) {
                this.failingUrls.put(session, str);
            } else {
                this.failingUrls.remove(session);
            }
        }

        private void insertBrowsingHistory(Session session) {
            String url = BrowserFragment.this.getUrl();
            String lastInsertedUrl = getLastInsertedUrl(session);
            if (!TextUtils.isEmpty(url) && !url.equals(getFailingUrl(session)) && !url.equals(lastInsertedUrl)) {
                TabView tabView = session.getEngineSession().getTabView();
                if (tabView != null) {
                    tabView.insertBrowsingHistory();
                }
                this.lastInsertedUrls.put(session, url);
            }
        }

        private String getFailingUrl(Session session) {
            String str = (String) this.failingUrls.get(session);
            return TextUtils.isEmpty(str) ? "" : str;
        }

        private String getLastInsertedUrl(Session session) {
            String str = (String) this.lastInsertedUrls.get(session);
            return TextUtils.isEmpty(str) ? "" : str;
        }
    }

    public interface LoadStateListener {
        void isLoadingChanged(boolean z);
    }

    public interface ScreenshotCallback {
        void onCaptureComplete(String str, String str2, Bitmap bitmap);
    }

    /* renamed from: org.mozilla.focus.fragment.BrowserFragment$1 */
    class C06931 implements PermissionHandle {
        C06931() {
        }

        public void doActionDirect(String str, int i, Parcelable parcelable) {
            switch (i) {
                case 0:
                    if (BrowserFragment.this.getContext() != null) {
                        Download download = (Download) parcelable;
                        if (ContextCompat.checkSelfPermission(BrowserFragment.this.getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                            BrowserFragment.this.queueDownload(download);
                            break;
                        }
                    }
                    Log.w("browser_screen", "No context to use, abort callback onDownloadStart");
                    return;
                    break;
                case 1:
                    BrowserFragment.this.fileChooseAction.startChooserActivity();
                    break;
                case 2:
                    BrowserFragment.this.showGeolocationPermissionPrompt();
                    break;
                case 3:
                    BrowserFragment.this.showLoadingAndCapture();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown actionId");
            }
        }

        private void actionDownloadGranted(Parcelable parcelable) {
            BrowserFragment.this.queueDownload((Download) parcelable);
        }

        private void actionPickFileGranted() {
            if (BrowserFragment.this.fileChooseAction != null) {
                BrowserFragment.this.fileChooseAction.startChooserActivity();
            }
        }

        private void actionCaptureGranted() {
            BrowserFragment.this.hasPendingScreenCaptureTask = true;
        }

        private void doActionGrantedOrSetting(String str, int i, Parcelable parcelable) {
            switch (i) {
                case 0:
                    actionDownloadGranted(parcelable);
                    return;
                case 1:
                    actionPickFileGranted();
                    return;
                case 2:
                    BrowserFragment.this.showGeolocationPermissionPrompt();
                    return;
                case 3:
                    actionCaptureGranted();
                    return;
                default:
                    throw new IllegalArgumentException("Unknown actionId");
            }
        }

        public void doActionGranted(String str, int i, Parcelable parcelable) {
            doActionGrantedOrSetting(str, i, parcelable);
        }

        public void doActionSetting(String str, int i, Parcelable parcelable) {
            doActionGrantedOrSetting(str, i, parcelable);
        }

        public void doActionNoPermission(String str, int i, Parcelable parcelable) {
            switch (i) {
                case 0:
                case 3:
                    return;
                case 1:
                    if (BrowserFragment.this.fileChooseAction != null) {
                        BrowserFragment.this.fileChooseAction.cancel();
                        BrowserFragment.this.fileChooseAction = null;
                        return;
                    }
                    return;
                case 2:
                    if (BrowserFragment.this.geolocationCallback != null) {
                        ThreadUtils.postToMainThread(new C0458-$$Lambda$BrowserFragment$1$O1zuVOux1ZaId-gKigNygb5Rzpc(this));
                        return;
                    }
                    return;
                default:
                    throw new IllegalArgumentException("Unknown actionId");
            }
        }

        public Snackbar makeAskAgainSnackBar(int i) {
            return PermissionHandler.makeAskAgainSnackBar(BrowserFragment.this, BrowserFragment.this.getActivity().findViewById(2131296374), getAskAgainSnackBarString(i));
        }

        private int getAskAgainSnackBarString(int i) {
            if (i == 0 || i == 1 || i == 3) {
                return C0769R.string.permission_toast_storage;
            }
            if (i == 2) {
                return C0769R.string.permission_toast_location;
            }
            throw new IllegalArgumentException("Unknown Action");
        }

        private int getPermissionDeniedToastString(int i) {
            if (i == 0 || i == 1 || i == 3) {
                return C0769R.string.permission_toast_storage_deny;
            }
            if (i == 2) {
                return C0769R.string.permission_toast_location_deny;
            }
            throw new IllegalArgumentException("Unknown Action");
        }

        public void requestPermissions(int i) {
            switch (i) {
                case 0:
                case 3:
                    BrowserFragment.this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, i);
                    return;
                case 1:
                    BrowserFragment.this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, i);
                    return;
                case 2:
                    BrowserFragment.this.requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, i);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown Action");
            }
        }

        public void permissionDeniedToast(int i) {
            Toast.makeText(BrowserFragment.this.getContext(), getPermissionDeniedToastString(i), 1).show();
        }
    }

    class DownloadCallback implements org.mozilla.rocket.tabs.web.DownloadCallback {
        DownloadCallback() {
        }

        public void onDownloadStart(Download download) {
            FragmentActivity activity = BrowserFragment.this.getActivity();
            if (activity != null && activity.getLifecycle().getCurrentState().isAtLeast(State.STARTED)) {
                BrowserFragment.this.permissionHandler.tryAction(BrowserFragment.this, "android.permission.WRITE_EXTERNAL_STORAGE", 0, (Parcelable) download);
            }
        }
    }

    class SessionObserver implements Session.Observer, Client {
        private HistoryInserter historyInserter = new HistoryInserter(BrowserFragment.this, null);
        private String loadedUrl = null;
        private Session session;

        public void onNavigationStateChanged(Session session, boolean z, boolean z2) {
        }

        public void onReceivedIcon(Bitmap bitmap) {
        }

        SessionObserver() {
        }

        public void onLoadingStateChanged(Session session, boolean z) {
            BrowserFragment.this.isLoading = z;
            if (z) {
                this.historyInserter.onTabStarted(session);
            } else {
                this.historyInserter.onTabFinished(session);
            }
            if (isForegroundSession(session)) {
                if (z) {
                    this.loadedUrl = null;
                    BrowserFragment.this.updateIsLoading(true);
                    BrowserFragment.this.updateURL(session.getUrl());
                    BrowserFragment.this.backgroundTransition.resetTransition();
                } else {
                    updateUrlFromWebView(session);
                    BrowserFragment.this.updateIsLoading(false);
                    C0572-CC.notifyParent(BrowserFragment.this, TYPE.UPDATE_MENU, null);
                    BrowserFragment.this.backgroundTransition.startTransition(300);
                }
            }
        }

        public void onSecurityChanged(Session session, boolean z) {
            BrowserFragment.this.siteIdentity.setImageLevel(z);
        }

        public void onUrlChanged(Session session, String str) {
            if (isForegroundSession(session)) {
                BrowserFragment.this.updateURL(str);
            }
        }

        public boolean handleExternalUrl(String str) {
            if (BrowserFragment.this.getContext() != null) {
                return IntentUtils.handleExternalUri(BrowserFragment.this.getContext(), str);
            }
            Log.w("browser_screen", "No context to use, abort callback handleExternalUrl");
            return false;
        }

        public void updateFailingUrl(String str, boolean z) {
            if (this.session != null) {
                this.historyInserter.updateFailingUrl(this.session, str, z);
            }
        }

        public void onProgress(Session session, int i) {
            if (isForegroundSession(session)) {
                BrowserFragment.this.hideFindInPage();
                if (BrowserFragment.this.sessionManager.getFocusSession() != null) {
                    String url = BrowserFragment.this.sessionManager.getFocusSession().getUrl();
                    boolean equals = TextUtils.equals(url, this.loadedUrl);
                    Object obj = (BrowserFragment.this.progressView.getMax() == BrowserFragment.this.progressView.getProgress() || i != BrowserFragment.this.progressView.getMax()) ? null : 1;
                    if (obj != null) {
                        this.loadedUrl = url;
                    }
                    if (equals) {
                        return;
                    }
                }
                BrowserFragment.this.progressView.setProgress(i);
            }
        }

        public boolean onShowFileChooser(TabViewEngineSession tabViewEngineSession, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            if (!isForegroundSession(this.session)) {
                return false;
            }
            TelemetryWrapper.browseFilePermissionEvent();
            try {
                BrowserFragment.this.fileChooseAction = new FileChooseAction(BrowserFragment.this, valueCallback, fileChooserParams);
                BrowserFragment.this.permissionHandler.tryAction(BrowserFragment.this, "android.permission.READ_EXTERNAL_STORAGE", 1, null);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public void onTitleChanged(Session session, String str) {
            if (!(session == null || !isForegroundSession(session) || BrowserFragment.this.getUrl().equals(session.getUrl()))) {
                BrowserFragment.this.updateURL(session.getUrl());
            }
        }

        public void onLongPress(Session session, HitTarget hitTarget) {
            if (BrowserFragment.this.getActivity() == null) {
                Log.w("browser_screen", "No context to use, abort callback onLongPress");
            } else {
                BrowserFragment.this.webContextMenu = WebContextMenu.show(false, BrowserFragment.this.getActivity(), BrowserFragment.this.downloadCallback, hitTarget);
            }
        }

        public void onEnterFullScreen(FullscreenCallback fullscreenCallback, View view) {
            if (this.session != null) {
                if (isForegroundSession(this.session)) {
                    BrowserFragment.this.fullscreenCallback = fullscreenCallback;
                    if (!(this.session.getEngineSession().getTabView() == null || view == null)) {
                        BrowserFragment.this.browserContainer.setVisibility(4);
                        BrowserFragment.this.videoContainer.addView(view, new LayoutParams(-1, -1));
                        BrowserFragment.this.videoContainer.setVisibility(0);
                        BrowserFragment.this.systemVisibility = ViewUtils.switchToImmersiveMode(BrowserFragment.this.getActivity());
                    }
                    return;
                }
                fullscreenCallback.fullScreenExited();
            }
        }

        public void onExitFullScreen() {
            if (this.session != null) {
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
                    ((WebView) this.session.getEngineSession().getTabView()).clearFocus();
                }
            }
        }

        public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
            if (this.session != null && isForegroundSession(this.session) && BrowserFragment.this.isPopupWindowAllowed()) {
                BrowserFragment.this.geolocationOrigin = str;
                BrowserFragment.this.geolocationCallback = callback;
                BrowserFragment.this.permissionHandler.tryAction(BrowserFragment.this, "android.permission.ACCESS_FINE_LOCATION", 2, null);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void changeSession(Session session) {
            if (this.session != null) {
                this.session.unregister((Session.Observer) this);
            }
            this.session = session;
            if (this.session != null) {
                this.session.register((Session.Observer) this);
            }
        }

        private void updateUrlFromWebView(Session session) {
            if (BrowserFragment.this.sessionManager.getFocusSession() != null) {
                onUrlChanged(session, BrowserFragment.this.sessionManager.getFocusSession().getUrl());
            }
        }

        private boolean isForegroundSession(Session session) {
            return BrowserFragment.this.sessionManager.getFocusSession() == session;
        }

        public void onFindResult(Session session, FindResult findResult) {
            BrowserFragment.this.findInPage.onFindResultReceived(findResult);
        }

        public boolean onDownload(Session session, mozilla.components.browser.session.Download download) {
            FragmentActivity activity = BrowserFragment.this.getActivity();
            if (activity == null || !activity.getLifecycle().getCurrentState().isAtLeast(State.STARTED)) {
                return false;
            }
            BrowserFragment.this.permissionHandler.tryAction(BrowserFragment.this, "android.permission.WRITE_EXTERNAL_STORAGE", 0, new Download(download.getUrl(), download.getFileName(), download.getUserAgent(), "", download.getContentType(), download.getContentLength().longValue(), false));
            return true;
        }

        public void onHttpAuthRequest(HttpAuthCallback httpAuthCallback, String str, String str2) {
            Builder okListener = new Builder(BrowserFragment.this.getActivity(), str, str2).setOkListener(new C0688xa209df14(httpAuthCallback));
            httpAuthCallback.getClass();
            HttpAuthenticationDialogBuilder build = okListener.setCancelListener(new C0685-$$Lambda$2vHgiayWabNOvt9IdkeaydjRUNI(httpAuthCallback)).build();
            build.createDialog();
            build.show();
        }
    }

    class SessionManagerObserver implements Observer {
        private SessionObserver sessionObserver;
        private ValueAnimator tabTransitionAnimator;

        SessionManagerObserver(SessionObserver sessionObserver) {
            this.sessionObserver = sessionObserver;
        }

        public void onFocusChanged(Session session, Factor factor) {
            if (session != null) {
                transitToTab(session);
                refreshChrome(session);
            } else if (factor != Factor.FACTOR_NO_FOCUS || BrowserFragment.this.isStartedFromExternalApp()) {
                BrowserFragment.this.getActivity().finish();
            } else {
                ScreenNavigator.get(BrowserFragment.this.getContext()).popToHomeScreen(true);
            }
        }

        public void onSessionAdded(Session session, Bundle bundle) {
            if (bundle != null && bundle.getInt("extra_bkg_tab_src", -1) == 0) {
                onTabAddedByContextMenu(session, bundle);
            }
        }

        public void onSessionCountChanged(int i) {
            if (BrowserFragment.this.isTabRestoredComplete()) {
                BrowserFragment.this.tabCounter.setCountWithAnimation(i);
            }
        }

        private void transitToTab(Session session) {
            TabView tabView = session.getEngineSession().getTabView();
            if (tabView != null) {
                if (session.getEngineSession() != null) {
                    session.getEngineSession().detach();
                }
                BrowserFragment.this.webViewSlot.removeView(findExistingTabView(BrowserFragment.this.webViewSlot));
                View view = tabView.getView();
                BrowserFragment.this.webViewSlot.addView(view);
                this.sessionObserver.changeSession(session);
                startTransitionAnimation(null, view, null);
                return;
            }
            throw new RuntimeException("Tabview should be created at this moment and never be null");
        }

        private void refreshChrome(Session session) {
            BrowserFragment.this.geolocationOrigin = "";
            BrowserFragment.this.geolocationCallback = null;
            BrowserFragment.this.dismissGeoDialog();
            BrowserFragment.this.updateURL(session.getUrl());
            BrowserFragment.this.progressView.setProgress(0);
            BrowserFragment.this.siteIdentity.setImageLevel(session.getSecurityInfo().getSecure());
            BrowserFragment.this.hideFindInPage();
        }

        private void startTransitionAnimation(final View view, final View view2, final Runnable runnable) {
            stopTabTransition();
            view2.setAlpha(0.0f);
            if (view != null) {
                view.setAlpha(1.0f);
            }
            this.tabTransitionAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration((long) view2.getResources().getInteger(C0769R.integer.tab_transition_time));
            this.tabTransitionAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    if (view != null) {
                        view.setAlpha(1.0f - floatValue);
                    }
                    view2.setAlpha(floatValue);
                }
            });
            this.tabTransitionAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
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

        private View findExistingTabView(ViewGroup viewGroup) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt instanceof TabView) {
                    return ((TabView) childAt).getView();
                }
            }
            return null;
        }

        private void stopTabTransition() {
            if (this.tabTransitionAnimator != null && this.tabTransitionAnimator.isRunning()) {
                this.tabTransitionAnimator.end();
            }
        }

        private void onTabAddedByContextMenu(final Session session, Bundle bundle) {
            if (!TabUtil.toFocus(bundle)) {
                Snackbar.make(BrowserFragment.this.webViewSlot, (int) C0769R.string.new_background_tab_hint, 0).setAction((int) C0769R.string.new_background_tab_switch, new OnClickListener() {
                    public void onClick(View view) {
                        BrowserFragment.this.sessionManager.switchToTab(session.getId());
                    }
                }).show();
            }
        }

        public void updateFailingUrl(String str, boolean z) {
            this.sessionObserver.updateFailingUrl(str, z);
        }

        public boolean handleExternalUrl(String str) {
            return this.sessionObserver.handleExternalUrl(str);
        }

        public boolean onShowFileChooser(TabViewEngineSession tabViewEngineSession, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            return this.sessionObserver.onShowFileChooser(tabViewEngineSession, valueCallback, fileChooserParams);
        }

        public void onHttpAuthRequest(HttpAuthCallback httpAuthCallback, String str, String str2) {
            this.sessionObserver.onHttpAuthRequest(httpAuthCallback, str, str2);
        }
    }

    public Fragment getFragment() {
        return this;
    }

    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (bundle != null) {
            this.permissionHandler.onRestoreInstanceState(bundle);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.permissionHandler = new PermissionHandler(new C06931());
    }

    public void onPause() {
        this.sessionManager.pause();
        super.onPause();
    }

    public void applyLocale() {
        new WebView(getContext()).destroy();
    }

    public void onResume() {
        this.sessionManager.resume();
        super.onResume();
        if (this.hasPendingScreenCaptureTask) {
            showLoadingAndCapture();
            this.hasPendingScreenCaptureTask = false;
        }
    }

    private void updateURL(String str) {
        if (!UrlUtils.isInternalErrorURL(str)) {
            this.urlView.setText(UrlUtils.stripUserInfo(str));
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0769R.layout.fragment_browser, viewGroup, false);
        this.videoContainer = (ViewGroup) inflate.findViewById(C0427R.C0426id.video_container);
        this.browserContainer = (ThemedLinearLayout) inflate.findViewById(C0427R.C0426id.browser_container);
        this.urlView = (ThemedTextView) inflate.findViewById(C0427R.C0426id.display_url);
        this.backgroundView = (ThemedRelativeLayout) inflate.findViewById(C0427R.C0426id.background);
        inflate.findViewById(C0427R.C0426id.appbar).setOnApplyWindowInsetsListener(C0459-$$Lambda$BrowserFragment$E4DFpfvxoIczJjyXxreM27CrkUE.INSTANCE);
        this.backgroundTransition = (TransitionDrawable) this.backgroundView.getBackground();
        this.tabCounter = (TabCounter) inflate.findViewById(C0427R.C0426id.btn_tab_tray);
        this.newTabBtn = (ThemedImageButton) inflate.findViewById(C0427R.C0426id.btn_open_new_tab);
        this.searchBtn = (ThemedImageButton) inflate.findViewById(C0427R.C0426id.btn_search);
        this.captureBtn = (ThemedImageButton) inflate.findViewById(C0427R.C0426id.btn_capture);
        this.menuBtn = (ThemedImageButton) inflate.findViewById(C0427R.C0426id.btn_menu);
        this.toolbarRoot = (ThemedLinearLayout) inflate.findViewById(C0427R.C0426id.toolbar_root);
        this.bottomMenuDivider = (ThemedView) inflate.findViewById(C0427R.C0426id.bottom_menu_divider);
        this.urlBarDivider = (ThemedView) inflate.findViewById(C0427R.C0426id.url_bar_divider);
        if (this.tabCounter != null) {
            this.tabCounter.setOnClickListener(this);
        }
        if (this.newTabBtn != null) {
            this.newTabBtn.setOnClickListener(this);
        }
        if (this.searchBtn != null) {
            this.searchBtn.setOnClickListener(this);
        }
        if (this.captureBtn != null) {
            this.captureBtn.setOnClickListener(this);
        }
        if (this.menuBtn != null) {
            this.menuBtn.setOnClickListener(this);
            this.menuBtn.setOnLongClickListener(this);
        }
        this.siteIdentity = (ThemedImageView) inflate.findViewById(C0427R.C0426id.site_identity);
        this.findInPage = new FindInPage(inflate);
        this.progressView = (AnimatedProgressBar) inflate.findViewById(C0427R.C0426id.progress);
        initialiseNormalBrowserUi();
        this.webViewSlot = (ViewGroup) inflate.findViewById(C0427R.C0426id.webview_slot);
        this.sessionManager = TabsSessionProvider.getOrThrow(getActivity());
        this.sessionManager.register(this.managerObserver, (LifecycleOwner) this, false);
        if (this.tabCounter != null && isTabRestoredComplete()) {
            this.tabCounter.setCount(this.sessionManager.getTabsCount());
        }
        setNightModeEnabled(Settings.getInstance(getActivity()).isNightModeEnable());
        this.downloadingIndicator = (LottieAnimationView) inflate.findViewById(C0427R.C0426id.downloading_indicator);
        this.downloadIndicator = (ImageView) inflate.findViewById(C0427R.C0426id.download_unread_indicator);
        Inject.obtainDownloadIndicatorViewModel(getActivity()).getDownloadIndicatorObservable().observe(getViewLifecycleOwner(), new C0689-$$Lambda$BrowserFragment$l94-q85PEWz_WW25q4mSUZ5WGEE(this, (ViewGroup) inflate.findViewById(C0427R.C0426id.browser_root_view)));
        return inflate;
    }

    public static /* synthetic */ void lambda$onCreateView$2(BrowserFragment browserFragment, ViewGroup viewGroup, Status status) {
        if (status == Status.DOWNLOADING) {
            browserFragment.downloadIndicator.setVisibility(8);
            browserFragment.downloadingIndicator.setVisibility(0);
            if (!browserFragment.downloadingIndicator.isAnimating()) {
                browserFragment.downloadingIndicator.playAnimation();
            }
        } else if (status == Status.UNREAD) {
            browserFragment.downloadingIndicator.setVisibility(8);
            browserFragment.downloadIndicator.setVisibility(0);
            browserFragment.downloadIndicator.setImageResource(2131230948);
        } else if (status == Status.WARNING) {
            browserFragment.downloadingIndicator.setVisibility(8);
            browserFragment.downloadIndicator.setVisibility(0);
            browserFragment.downloadIndicator.setImageResource(2131230949);
        } else {
            browserFragment.downloadingIndicator.setVisibility(8);
            browserFragment.downloadIndicator.setVisibility(8);
        }
        EventHistory eventHistory = Settings.getInstance(browserFragment.getActivity()).getEventHistory();
        if (!eventHistory.contains("dl_indicator_intro") && status != Status.DEFAULT) {
            eventHistory.add("dl_indicator_intro");
            DownloadIndicatorIntroViewHelper.INSTANCE.initDownloadIndicatorIntroView(browserFragment, browserFragment.menuBtn, viewGroup, new C0687-$$Lambda$BrowserFragment$4s_5qYgCoiDUfvrmBOxvJDEcYeU(browserFragment));
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle != null) {
            Session focusSession = this.sessionManager.getFocusSession();
            if (focusSession != null) {
                TabView tabView = focusSession.getEngineSession().getTabView();
                if (tabView != null) {
                    tabView.restoreViewState(bundle);
                } else {
                    this.sessionManager.switchToTab(focusSession.getId());
                }
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        this.permissionHandler.onActivityResult(getActivity(), i, i2, intent);
        if (i == 103) {
            Object obj = (this.fileChooseAction == null || this.fileChooseAction.onFileChose(i2, intent)) ? 1 : null;
            if (obj != null) {
                this.fileChooseAction = null;
            }
        }
    }

    public void onCaptureClicked() {
        this.permissionHandler.tryAction((Fragment) this, "android.permission.WRITE_EXTERNAL_STORAGE", 3, null);
    }

    public void goBackground() {
        Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                engineSession.detach();
                TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    this.webViewSlot.removeView(tabView.getView());
                }
            }
        }
    }

    public void goForeground() {
        Session focusSession = this.sessionManager.getFocusSession();
        if (this.webViewSlot.getChildCount() == 0 && focusSession != null) {
            TabViewEngineSession engineSession = focusSession.getEngineSession();
            if (engineSession != null) {
                TabView tabView = engineSession.getTabView();
                if (tabView != null) {
                    this.webViewSlot.addView(tabView.getView());
                }
            }
        }
        setNightModeEnabled(Settings.getInstance(getActivity()).isNightModeEnable());
    }

    private void initialiseNormalBrowserUi() {
        this.urlView.setOnClickListener(this);
    }

    public void onSaveInstanceState(Bundle bundle) {
        this.permissionHandler.onSaveInstanceState(bundle);
        if (this.sessionManager.getFocusSession() != null) {
            TabViewEngineSession engineSession = this.sessionManager.getFocusSession().getEngineSession();
            if (!(engineSession == null || engineSession.getTabView() == null)) {
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
            Session focusSession = this.sessionManager.getFocusSession();
            if (focusSession != null) {
                TabView tabView = focusSession.getEngineSession().getTabView();
                if (tabView != null) {
                    tabView.performExitFullScreen();
                }
            }
        }
        dismissGeoDialog();
        super.onStop();
    }

    public void onDestroyView() {
        this.sessionManager.unregister(this.managerObserver);
        super.onDestroyView();
    }

    public void setContentBlockingEnabled(boolean z) {
        for (Session engineSession : this.sessionManager.getTabs()) {
            TabViewEngineSession engineSession2 = engineSession.getEngineSession();
            if (!(engineSession2 == null || engineSession2.getTabView() == null)) {
                engineSession2.getTabView().setContentBlockingEnabled(z);
            }
        }
    }

    public void setImageBlockingEnabled(boolean z) {
        for (Session engineSession : this.sessionManager.getTabs()) {
            TabViewEngineSession engineSession2 = engineSession.getEngineSession();
            if (!(engineSession2 == null || engineSession2.getTabView() == null)) {
                engineSession2.getTabView().setImageBlockingEnabled(z);
            }
        }
    }

    public CaptureStateListener getCaptureStateListener() {
        return this.captureStateListener;
    }

    private void showLoadingAndCapture() {
        if (isResumed()) {
            this.hasPendingScreenCaptureTask = false;
            ScreenCaptureDialogFragment newInstance = ScreenCaptureDialogFragment.newInstance();
            newInstance.show(getChildFragmentManager(), "capturingFragment");
            HANDLER.postDelayed(new CaptureRunnable(getContext(), this, newInstance, getActivity().findViewById(2131296374)), 150);
        }
    }

    private void updateIsLoading(boolean z) {
        this.isLoading = z;
        LoadStateListener loadStateListener = (LoadStateListener) this.loadStateListenerWeakReference.get();
        if (loadStateListener != null) {
            loadStateListener.isLoadingChanged(z);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        this.permissionHandler.onRequestPermissionsResult(getContext(), i, strArr, iArr);
    }

    private void queueDownload(Download download) {
        if (getActivity() != null && download != null) {
            new EnqueueDownloadTask(getActivity(), download, getUrl()).execute(new Void[0]);
        }
    }

    private void showGeolocationPermissionPrompt() {
        if (!isPopupWindowAllowed() || this.geolocationCallback == null) {
            return;
        }
        if (this.geoDialog == null || !this.geoDialog.isShowing()) {
            Boolean allowed = GeoPermissionCache.getAllowed(this.geolocationOrigin);
            if (allowed != null) {
                this.geolocationCallback.invoke(this.geolocationOrigin, allowed.booleanValue(), false);
            } else {
                this.geoDialog = buildGeoPromptDialog();
                this.geoDialog.show();
            }
        }
    }

    public void dismissGeoDialog() {
        if (this.geoDialog != null) {
            this.geoDialog.dismiss();
            this.geoDialog = null;
        }
    }

    public AlertDialog buildGeoPromptDialog() {
        View inflate = LayoutInflater.from(getContext()).inflate(C0769R.layout.dialog_permission_request, null);
        final CheckedTextView checkedTextView = (CheckedTextView) inflate.findViewById(C0427R.C0426id.cache_my_decision);
        checkedTextView.setText(getString(C0769R.string.geolocation_dialog_message_cache_it, getString(C0769R.string.app_name)));
        checkedTextView.setOnClickListener(new C0460-$$Lambda$BrowserFragment$qv_dY4EfQ8BL4okjukxNbzZQYjI(checkedTextView));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(inflate).setMessage(getString(C0769R.string.geolocation_dialog_message, this.geolocationOrigin)).setCancelable(true).setPositiveButton(getString(C0769R.string.geolocation_dialog_allow), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                BrowserFragment.this.acceptGeoRequest(checkedTextView.isChecked());
            }
        }).setNegativeButton(getString(C0769R.string.geolocation_dialog_block), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                BrowserFragment.this.rejectGeoRequest(checkedTextView.isChecked());
            }
        }).setOnDismissListener(new C04633()).setOnCancelListener(new C04622());
        return builder.create();
    }

    private void acceptGeoRequest(boolean z) {
        if (this.geolocationCallback != null) {
            if (z) {
                GeoPermissionCache.putAllowed(this.geolocationOrigin, Boolean.TRUE);
            }
            this.geolocationCallback.invoke(this.geolocationOrigin, true, false);
            this.geolocationOrigin = "";
            this.geolocationCallback = null;
        }
    }

    private void rejectGeoRequest(boolean z) {
        if (this.geolocationCallback != null) {
            if (z) {
                GeoPermissionCache.putAllowed(this.geolocationOrigin, Boolean.FALSE);
            }
            this.geolocationCallback.invoke(this.geolocationOrigin, false, false);
            this.geolocationOrigin = "";
            this.geolocationCallback = null;
        }
    }

    private boolean isStartedFromExternalApp() {
        FragmentActivity activity = getActivity();
        boolean z = false;
        if (activity == null) {
            return false;
        }
        Intent intent = activity.getIntent();
        Object obj = (intent == null || !intent.getBooleanExtra("is_internal_request", false)) ? null : 1;
        if (intent != null && "android.intent.action.VIEW".equals(intent.getAction()) && obj == null) {
            z = true;
        }
        return z;
    }

    public boolean onBackPressed() {
        if (this.findInPage.onBackPressed()) {
            return true;
        }
        if (canGoBack()) {
            goBack();
        } else {
            Session focusSession = this.sessionManager.getFocusSession();
            if (focusSession == null) {
                return false;
            }
            if (focusSession.isFromExternal() || focusSession.hasParentTab()) {
                this.sessionManager.closeTab(focusSession.getId());
            } else {
                ScreenNavigator.get(getContext()).popToHomeScreen(true);
            }
        }
        return true;
    }

    public void loadUrl(String str, boolean z, boolean z2, Runnable runnable) {
        updateURL(str);
        if (SupportUtils.isUrl(str)) {
            if (z) {
                this.sessionManager.addTab(str, TabUtil.argument(null, z2, true));
                dismissDownloadIndicatorIntroView();
                ThreadUtils.postToMainThread(runnable);
                return;
            }
            Session focusSession = this.sessionManager.getFocusSession();
            if (focusSession == null || focusSession.getEngineSession().getTabView() == null) {
                this.sessionManager.addTab(str, TabUtil.argument(null, z2, true));
                ThreadUtils.postToMainThread(runnable);
                return;
            }
            focusSession.getEngineSession().getTabView().loadUrl(str);
            runnable.run();
        } else if (AppConstants.isDevBuild()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("trying to open a invalid url: ");
            stringBuilder.append(str);
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public void switchToTab(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.sessionManager.switchToTab(str);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0427R.C0426id.btn_capture /*2131296345*/:
                onCaptureClicked();
                return;
            case C0427R.C0426id.btn_menu /*2131296349*/:
                C0572-CC.notifyParent(this, TYPE.SHOW_MENU, null);
                TelemetryWrapper.showMenuToolbar();
                return;
            case C0427R.C0426id.btn_open_new_tab /*2131296354*/:
                HomeFragmentViewState.reset();
                ScreenNavigator.get(getContext()).addHomeScreen(true);
                TelemetryWrapper.clickAddTabToolbar();
                return;
            case C0427R.C0426id.btn_search /*2131296356*/:
                C0572-CC.notifyParent(this, TYPE.SHOW_URL_INPUT, getUrl());
                TelemetryWrapper.clickToolbarSearch();
                return;
            case C0427R.C0426id.btn_tab_tray /*2131296357*/:
                C0572-CC.notifyParent(this, TYPE.SHOW_TAB_TRAY, null);
                TelemetryWrapper.showTabTrayToolbar();
                return;
            case C0427R.C0426id.display_url /*2131296411*/:
                C0572-CC.notifyParent(this, TYPE.SHOW_URL_INPUT, getUrl());
                TelemetryWrapper.clickUrlbar();
                return;
            default:
                throw new IllegalArgumentException("Unhandled menu item in BrowserFragment");
        }
    }

    public boolean onLongClick(View view) {
        if (view.getId() == C0427R.C0426id.btn_menu) {
            C0572-CC.notifyParent(this, TYPE.SHOW_DOWNLOAD_PANEL, null);
            TelemetryWrapper.longPressDownloadIndicator();
            return false;
        }
        throw new IllegalArgumentException("Unhandled long click menu item in BrowserFragment");
    }

    public String getUrl() {
        return this.urlView.getText().toString();
    }

    public boolean canGoForward() {
        return this.sessionManager.getFocusSession() != null && this.sessionManager.getFocusSession().getCanGoForward();
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public boolean canGoBack() {
        return this.sessionManager.getFocusSession() != null && this.sessionManager.getFocusSession().getCanGoBack();
    }

    public void goBack() {
        Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            TabView tabView = focusSession.getEngineSession().getTabView();
            if (tabView != null && tabView.canGoBack()) {
                WebBackForwardList copyBackForwardList = ((WebView) tabView).copyBackForwardList();
                updateURL(copyBackForwardList.getItemAtIndex(copyBackForwardList.getCurrentIndex() - 1).getUrl());
                tabView.goBack();
            }
        }
    }

    public void goForward() {
        Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            TabView tabView = focusSession.getEngineSession().getTabView();
            if (tabView != null) {
                WebBackForwardList copyBackForwardList = ((WebView) tabView).copyBackForwardList();
                updateURL(copyBackForwardList.getItemAtIndex(copyBackForwardList.getCurrentIndex() + 1).getUrl());
                tabView.goForward();
            }
        }
    }

    public void reload() {
        Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            TabView tabView = focusSession.getEngineSession().getTabView();
            if (tabView != null) {
                tabView.reload();
            }
        }
    }

    public void stop() {
        Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            TabView tabView = focusSession.getEngineSession().getTabView();
            if (tabView != null) {
                tabView.stopLoading();
            }
        }
    }

    public boolean capturePage(ScreenshotCallback screenshotCallback) {
        Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession == null) {
            return false;
        }
        TabView tabView = focusSession.getEngineSession().getTabView();
        if (tabView == null || !(tabView instanceof WebView)) {
            return false;
        }
        Bitmap pageBitmap = getPageBitmap((WebView) tabView);
        if (pageBitmap == null) {
            return false;
        }
        screenshotCallback.onCaptureComplete(tabView.getTitle(), tabView.getUrl(), pageBitmap);
        return true;
    }

    public void dismissWebContextMenu() {
        if (this.webContextMenu != null) {
            this.webContextMenu.dismiss();
            this.webContextMenu = null;
        }
    }

    private Bitmap getPageBitmap(WebView webView) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        try {
            Bitmap createBitmap = Bitmap.createBitmap(webView.getWidth(), (int) (((float) webView.getContentHeight()) * displayMetrics.density), Config.RGB_565);
            webView.draw(new Canvas(createBitmap));
            return createBitmap;
        } catch (Exception | OutOfMemoryError unused) {
            return null;
        }
    }

    private boolean isPopupWindowAllowed() {
        return ScreenNavigator.get(getContext()).isBrowserInForeground() && !TabTray.isShowing(getFragmentManager());
    }

    private boolean isTabRestoredComplete() {
        if (getActivity() instanceof TabRestoreMonitor) {
            return ((TabRestoreMonitor) getActivity()).isTabRestoredComplete();
        }
        if (!AppConstants.isDevBuild()) {
            return true;
        }
        throw new RuntimeException("Base activity needs to implement TabRestoreMonitor");
    }

    public void showFindInPage() {
        Session focusSession = this.sessionManager.getFocusSession();
        if (focusSession != null) {
            this.findInPage.show(focusSession);
            TelemetryWrapper.findInPage(FIND_IN_PAGE.OPEN_BY_MENU);
        }
    }

    private void hideFindInPage() {
        this.findInPage.hide();
    }

    public void showMyShotOnBoarding() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            EventHistory eventHistory = Settings.getInstance(activity).getEventHistory();
            if (!eventHistory.contains("show_my_shot_on_boarding_dialog")) {
                eventHistory.add("show_my_shot_on_boarding_dialog");
                C0572-CC.notifyParent(this, TYPE.SHOW_MY_SHOT_ON_BOARDING, null);
            }
        }
    }

    public void setNightModeEnabled(boolean z) {
        this.browserContainer.setNightMode(z);
        this.toolbarRoot.setNightMode(z);
        this.urlView.setNightMode(z);
        this.siteIdentity.setNightMode(z);
        this.newTabBtn.setNightMode(z);
        this.searchBtn.setNightMode(z);
        this.captureBtn.setNightMode(z);
        this.menuBtn.setNightMode(z);
        this.tabCounter.setNightMode(z);
        this.bottomMenuDivider.setNightMode(z);
        this.backgroundView.setNightMode(z);
        this.urlBarDivider.setNightMode(z);
        ViewUtils.updateStatusBarStyle(z ^ 1, getActivity().getWindow());
    }

    private void dismissDownloadIndicatorIntroView() {
        if (this.downloadIndicatorIntro != null) {
            this.downloadIndicatorIntro.setVisibility(8);
        }
    }
}
