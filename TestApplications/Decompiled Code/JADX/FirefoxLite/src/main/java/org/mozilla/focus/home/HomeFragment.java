package org.mozilla.focus.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteQuery;
import android.arch.persistence.p000db.SupportSQLiteQueryBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.content.LocalBroadcastManager;
import android.support.p004v7.widget.LinearLayoutManager;
import android.support.p004v7.widget.PagerSnapHelper;
import android.support.p004v7.widget.PopupMenu;
import android.support.p004v7.widget.RecyclerView;
import android.support.p004v7.widget.RecyclerView.Adapter;
import android.support.p004v7.widget.RecyclerView.LayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.airbnb.lottie.LottieAnimationView;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.fileutils.FileUtils.DeleteFileRunnable;
import org.mozilla.fileutils.FileUtils.GetCache;
import org.mozilla.fileutils.FileUtils.ReadStringFromFileTask;
import org.mozilla.fileutils.FileUtils.WriteStringToFileRunnable;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.Inject;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.locale.LocaleAwareFragment;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.navigation.ScreenNavigator.HomeScreen;
import org.mozilla.focus.provider.QueryHandler.AsyncQueryListener;
import org.mozilla.focus.provider.QueryHandler.AsyncUpdateListener;
import org.mozilla.focus.tabs.TabCounter;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.OnSwipeListener;
import org.mozilla.focus.utils.OnSwipeListener.C0538-CC;
import org.mozilla.focus.utils.RemoteConfigConstants.SURVEY;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.Settings.EventHistory;
import org.mozilla.focus.utils.SwipeMotionDetector;
import org.mozilla.focus.utils.TopSitesUtils;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.widget.FragmentListener.C0572-CC;
import org.mozilla.focus.widget.FragmentListener.TYPE;
import org.mozilla.focus.widget.SwipeMotionLayout;
import org.mozilla.httptask.SimpleLoadUrlTask;
import org.mozilla.icon.FavIconUtils.Consumer;
import org.mozilla.icon.FavIconUtils.SaveBitmapsTask;
import org.mozilla.lite.partner.NewsItem;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.banner.BannerAdapter;
import org.mozilla.rocket.banner.BannerConfigViewModel;
import org.mozilla.rocket.banner.BannerViewHolder;
import org.mozilla.rocket.content.ContentPortalView;
import org.mozilla.rocket.content.NewsPresenter;
import org.mozilla.rocket.content.NewsViewContract;
import org.mozilla.rocket.download.DownloadIndicatorViewModel.Status;
import org.mozilla.rocket.home.pinsite.PinSiteManager;
import org.mozilla.rocket.home.pinsite.PinSiteManagerKt;
import org.mozilla.rocket.nightmode.themed.ThemedImageButton;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;
import org.mozilla.rocket.persistance.History.HistoryDatabase;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.SessionManager.Factor;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;
import org.mozilla.rocket.tabs.TabViewEngineSession;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.rocket.theme.ThemeManager;
import org.mozilla.rocket.util.LoggerWrapper;
import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.urlutils.UrlUtils;

public class HomeFragment extends LocaleAwareFragment implements Model, View, HomeScreen, NewsViewContract {
    private static final String UNIT_SEPARATOR = Character.toString(31);
    private ImageButton arrow1;
    private ImageButton arrow2;
    private RecyclerView banner;
    private BannerConfigViewModel bannerConfigViewModel;
    private LinearLayoutManager bannerLayoutManager;
    final Observer<String[]> bannerObserver = new C0712-$$Lambda$HomeFragment$Y6w2k3vFU3uhQsU1sAqbNcJ0r8A(this);
    private ThemedImageButton btnMenu;
    private SiteItemClickListener clickListener = new SiteItemClickListener(this, null);
    private String[] configArray;
    private ContentPortalView contentPanel;
    private ImageView downloadIndicator;
    private LottieAnimationView downloadingIndicator;
    private ThemedTextView fakeInput;
    private HomeScreenBackground homeScreenBackground;
    private AsyncUpdateListener mTopSiteUpdateListener = new C0705-$$Lambda$HomeFragment$F55KXmKZ14h6rPSlCB5EojJPzKw(this);
    private AsyncQueryListener mTopSitesQueryListener = new C0708-$$Lambda$HomeFragment$Ht2Yxo-dd5eyf5eF-hvmXPMe380(this);
    private OnClickListener menuItemClickListener = new C04925();
    private NewsPresenter newsPresenter = null;
    private final SessionManagerObserver observer = new SessionManagerObserver(this, null);
    private OnRootConfigLoadedListener onRootConfigLoadedListener;
    private JSONArray orginalDefaultSites = null;
    private PinSiteManager pinSiteManager;
    private Presenter presenter;
    private BroadcastReceiver receiver;
    private RecyclerView recyclerView;
    private SessionManager sessionManager;
    private TabCounter tabCounter;
    private View themeOnboardingLayer;
    private Timer timer;
    private TopSiteAdapter topSiteAdapter;
    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            if (message.what == 8269) {
                HomeFragment.this.refreshTopSites();
            }
        }
    };

    /* renamed from: org.mozilla.focus.home.HomeFragment$3 */
    class C04903 extends BroadcastReceiver {
        C04903() {
        }

        public void onReceive(Context context, Intent intent) {
            HomeFragment.this.initBanner(context);
        }
    }

    /* renamed from: org.mozilla.focus.home.HomeFragment$4 */
    class C04914 extends TimerTask {
        C04914() {
        }

        public void run() {
            Adapter adapter = HomeFragment.this.banner.getAdapter();
            if (adapter == null) {
                cancel();
                return;
            }
            HomeFragment.this.banner.smoothScrollToPosition((HomeFragment.this.bannerLayoutManager.findFirstVisibleItemPosition() + 1) % adapter.getItemCount());
        }
    }

    /* renamed from: org.mozilla.focus.home.HomeFragment$5 */
    class C04925 implements OnClickListener {
        C04925() {
        }

        public void onClick(View view) {
            FragmentActivity activity = HomeFragment.this.getActivity();
            if (activity instanceof FragmentListener) {
                dispatchOnClick(view, (FragmentListener) activity);
            }
        }

        private void dispatchOnClick(View view, FragmentListener fragmentListener) {
            int id = view.getId();
            if (id == C0427R.C0426id.btn_menu_home) {
                fragmentListener.onNotified(HomeFragment.this, TYPE.SHOW_MENU, null);
                TelemetryWrapper.showMenuHome();
            } else if (id == C0427R.C0426id.btn_tab_tray) {
                fragmentListener.onNotified(HomeFragment.this, TYPE.SHOW_TAB_TRAY, null);
                TelemetryWrapper.showTabTrayHome();
            }
        }
    }

    private interface DoWithThemeManager {
        void doIt(ThemeManager themeManager);
    }

    private static class MigrateHistoryRunnable implements Runnable {
        private WeakReference<Context> contextWeakReference;
        private WeakReference<Handler> handlerWeakReference;

        MigrateHistoryRunnable(Handler handler, Context context) {
            this.handlerWeakReference = new WeakReference(handler);
            this.contextWeakReference = new WeakReference(context);
        }

        public void run() {
            Throwable th;
            Context context = (Context) this.contextWeakReference.get();
            if (context != null) {
                SupportSQLiteDatabase writableDatabase = HistoryDatabase.getInstance(context).getOpenHelper().getWritableDatabase();
                writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS browsing_history_legacy (_id INTEGER PRIMARY KEY NOT NULL,url TEXT NOT NULL,fav_icon BLOB);");
                SupportSQLiteQueryBuilder builder = SupportSQLiteQueryBuilder.builder("browsing_history_legacy");
                builder.columns(new String[]{"_id", "url", "fav_icon"});
                SupportSQLiteQuery create = builder.create();
                File faviconFolder = FileUtils.getFaviconFolder(context);
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                Cursor query = writableDatabase.query(create);
                try {
                    if (query.moveToFirst()) {
                        HomeFragment.parseCursorToSite(query, arrayList, arrayList2);
                    }
                    while (query.moveToNext()) {
                        HomeFragment.parseCursorToSite(query, arrayList, arrayList2);
                    }
                    if (query != null) {
                        query.close();
                    }
                    Handler handler = (Handler) this.handlerWeakReference.get();
                    if (handler != null) {
                        if (arrayList2.size() == 0) {
                            HomeFragment.scheduleRefresh(handler);
                        } else {
                            new SaveBitmapsTask(faviconFolder, arrayList, arrayList2, new UpdateHistoryWrapper(arrayList, this.handlerWeakReference, null), CompressFormat.PNG, 0).execute(new Void[0]);
                        }
                        writableDatabase.execSQL("DROP TABLE browsing_history_legacy");
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("top_sites_v2_complete", true).apply();
                    }
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
        }
    }

    private interface OnRemovedListener {
        void onRemoved(Site site);
    }

    private class SiteItemClickListener implements OnClickListener, OnLongClickListener {
        private SiteItemClickListener() {
        }

        /* synthetic */ SiteItemClickListener(HomeFragment homeFragment, C04891 c04891) {
            this();
        }

        public void onClick(View view) {
            Site site = (Site) view.getTag();
            FragmentActivity activity = HomeFragment.this.getActivity();
            if (site != null && (activity instanceof FragmentListener)) {
                ScreenNavigator.get(view.getContext()).showBrowserScreen(site.getUrl(), true, false);
                if (view.getParent() instanceof ViewGroup) {
                    TelemetryWrapper.clickTopSiteOn(((ViewGroup) view.getParent()).indexOfChild(view), site.isDefault() ? site.getTitle() : "");
                }
            }
        }

        public boolean onLongClick(View view) {
            Site site = (Site) view.getTag();
            boolean z = false;
            if (site == null) {
                return false;
            }
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view, 8);
            popupMenu.getMenuInflater().inflate(2131558405, popupMenu.getMenu());
            MenuItem findItem = popupMenu.getMenu().findItem(2131296565);
            if (findItem != null) {
                if (HomeFragment.this.pinSiteManager.isEnabled() && !HomeFragment.this.pinSiteManager.isPinned(site)) {
                    z = true;
                }
                findItem.setVisible(z);
            }
            popupMenu.setOnMenuItemClickListener(new C0710x3b34535e(this, site));
            popupMenu.show();
            return true;
        }

        public static /* synthetic */ boolean lambda$onLongClick$1(SiteItemClickListener siteItemClickListener, Site site, MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == 2131296565) {
                HomeFragment.this.presenter.pinSite(site, new C0484x246d8c21(HomeFragment.this));
            } else if (itemId == C0427R.C0426id.remove) {
                if (site.getId() < 0) {
                    HomeFragment.this.presenter.removeSite(site);
                    HomeFragment.this.removeDefaultSites(site);
                    TopSitesUtils.saveDefaultSites(HomeFragment.this.getContext(), HomeFragment.this.orginalDefaultSites);
                    HomeFragment.this.refreshTopSites();
                    TelemetryWrapper.removeTopSite(true);
                } else {
                    site.setViewCount(1);
                    BrowsingHistoryManager.getInstance().updateLastEntry(site, HomeFragment.this.mTopSiteUpdateListener);
                    TelemetryWrapper.removeTopSite(false);
                }
                HomeFragment.this.pinSiteManager.unpinned(site);
            } else {
                throw new IllegalStateException("Unhandled menu item");
            }
            return true;
        }
    }

    /* renamed from: org.mozilla.focus.home.HomeFragment$6 */
    class C07166 implements OnSwipeListener {
        public /* synthetic */ boolean onDoubleTap() {
            return C0538-CC.$default$onDoubleTap(this);
        }

        public /* synthetic */ void onLongPress() {
            C0538-CC.$default$onLongPress(this);
        }

        public /* synthetic */ void onSwipeDown() {
            C0538-CC.$default$onSwipeDown(this);
        }

        public /* synthetic */ void onSwipeLeft() {
            C0538-CC.$default$onSwipeLeft(this);
        }

        public /* synthetic */ void onSwipeRight() {
            C0538-CC.$default$onSwipeRight(this);
        }

        C07166() {
        }

        public boolean onSingleTapConfirmed() {
            HomeFragment.this.showContentPortal();
            return true;
        }

        public void onSwipeUp() {
            HomeFragment.this.showContentPortal();
        }
    }

    private class GestureListenerAdapter implements OnSwipeListener {
        public /* synthetic */ boolean onSingleTapConfirmed() {
            return C0538-CC.$default$onSingleTapConfirmed(this);
        }

        public /* synthetic */ void onSwipeLeft() {
            C0538-CC.$default$onSwipeLeft(this);
        }

        public /* synthetic */ void onSwipeRight() {
            C0538-CC.$default$onSwipeRight(this);
        }

        private GestureListenerAdapter() {
        }

        /* synthetic */ GestureListenerAdapter(HomeFragment homeFragment, C04891 c04891) {
            this();
        }

        public void onSwipeUp() {
            if (HomeFragment.this.contentPanel != null) {
                HomeFragment.this.showContentPortal();
            } else {
                HomeFragment.this.btnMenu.performClick();
            }
        }

        public void onSwipeDown() {
            if (HomeFragment.this.contentPanel == null) {
                HomeFragment.this.fakeInput.performClick();
            }
        }

        public void onLongPress() {
            if (!Settings.getInstance(HomeFragment.this.getActivity()).isNightModeEnable()) {
                HomeFragment.doWithActivity(HomeFragment.this.getActivity(), C0707xdeb72a3f.INSTANCE);
            }
        }

        static /* synthetic */ void lambda$onLongPress$0(ThemeManager themeManager) {
            themeManager.resetDefaultTheme();
            TelemetryWrapper.resetThemeToDefault();
        }

        public boolean onDoubleTap() {
            if (Settings.getInstance(HomeFragment.this.getActivity()).isNightModeEnable()) {
                return true;
            }
            HomeFragment.doWithActivity(HomeFragment.this.getActivity(), C0706xcd841af4.INSTANCE);
            return true;
        }
    }

    private static class LoadConfigTask extends SimpleLoadUrlTask {
        private int index;
        private WeakReference<OnConfigLoadedListener> onConfigLoadedListenerRef;

        private interface OnConfigLoadedListener {
            void onConfigLoaded(String str, int i);
        }

        LoadConfigTask(WeakReference<OnConfigLoadedListener> weakReference, int i) {
            this.onConfigLoadedListenerRef = weakReference;
            this.index = i;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(String str) {
            str = str.replace("\n", "").replace("\r", "");
            OnConfigLoadedListener onConfigLoadedListener = (OnConfigLoadedListener) this.onConfigLoadedListenerRef.get();
            if (onConfigLoadedListener != null) {
                onConfigLoadedListener.onConfigLoaded(str, this.index);
            }
        }
    }

    private static class LoadRootConfigTask extends SimpleLoadUrlTask {
        private AtomicInteger countdown;
        WeakReference<OnRootConfigLoadedListener> onRootConfigLoadedListenerRef;
        private String userAgent;

        private interface OnRootConfigLoadedListener {
            void onRootConfigLoaded(String[] strArr);
        }

        LoadRootConfigTask(WeakReference<OnRootConfigLoadedListener> weakReference) {
            this.onRootConfigLoadedListenerRef = weakReference;
        }

        /* Access modifiers changed, original: protected|varargs */
        public String doInBackground(String... strArr) {
            this.userAgent = strArr[1];
            return super.doInBackground(strArr);
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(String str) {
            if (str != null && !TextUtils.isEmpty(str)) {
                OnRootConfigLoadedListener onRootConfigLoadedListener = (OnRootConfigLoadedListener) this.onRootConfigLoadedListenerRef.get();
                if (onRootConfigLoadedListener != null) {
                    try {
                        int length = new JSONArray(str).length();
                        String[] strArr = new String[length];
                        this.countdown = new AtomicInteger(length);
                        C0709x8b1130ee c0709x8b1130ee = new C0709x8b1130ee(this, strArr, onRootConfigLoadedListener);
                        for (int i = 0; i < length; i++) {
                            new LoadConfigTask(new WeakReference(c0709x8b1130ee), i).execute(new String[]{r1.getString(i), this.userAgent, Integer.toString(10002)});
                        }
                    } catch (JSONException unused) {
                        onRootConfigLoadedListener.onRootConfigLoaded(null);
                    }
                }
            }
        }

        public static /* synthetic */ void lambda$onPostExecute$0(LoadRootConfigTask loadRootConfigTask, String[] strArr, OnRootConfigLoadedListener onRootConfigLoadedListener, String str, int i) {
            strArr[i] = str;
            if (loadRootConfigTask.countdown.decrementAndGet() == 0) {
                onRootConfigLoadedListener.onRootConfigLoaded(strArr);
            }
        }
    }

    private static class UpdateHistoryWrapper implements Consumer<List<String>> {
        private WeakReference<Handler> handlerWeakReference;
        private List<String> urls;

        /* synthetic */ UpdateHistoryWrapper(List list, WeakReference weakReference, C04891 c04891) {
            this(list, weakReference);
        }

        private UpdateHistoryWrapper(List<String> list, WeakReference<Handler> weakReference) {
            this.urls = list;
            this.handlerWeakReference = weakReference;
        }

        public void accept(List<String> list) {
            C0711x3568e9ac c0711x3568e9ac = new C0711x3568e9ac(this);
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    BrowsingHistoryManager.updateHistory(null, (String) this.urls.get(i), (String) list.get(i), c0711x3568e9ac);
                } else {
                    BrowsingHistoryManager.updateHistory(null, (String) this.urls.get(i), (String) list.get(i));
                }
            }
        }

        public static /* synthetic */ void lambda$accept$0(UpdateHistoryWrapper updateHistoryWrapper, int i) {
            Handler handler = (Handler) updateHistoryWrapper.handlerWeakReference.get();
            if (handler != null) {
                HomeFragment.scheduleRefresh(handler);
            }
        }
    }

    private class SessionManagerObserver implements SessionManager.Observer {
        public boolean handleExternalUrl(String str) {
            return false;
        }

        public void onFocusChanged(Session session, Factor factor) {
        }

        public void onHttpAuthRequest(HttpAuthCallback httpAuthCallback, String str, String str2) {
        }

        public void onSessionAdded(Session session, Bundle bundle) {
        }

        public boolean onShowFileChooser(TabViewEngineSession tabViewEngineSession, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            return false;
        }

        public void updateFailingUrl(String str, boolean z) {
        }

        private SessionManagerObserver() {
        }

        /* synthetic */ SessionManagerObserver(HomeFragment homeFragment, C04891 c04891) {
            this();
        }

        public void onSessionCountChanged(int i) {
            HomeFragment.this.updateTabCounter();
        }
    }

    /* renamed from: org.mozilla.focus.home.HomeFragment$2 */
    class C07672 extends PagerSnapHelper {
        C07672() {
        }

        private void sendTelemetry(int i, int i2) {
            sendSwipeTelemetry(i, i2);
            sendSwipeToIdTelemetry(i);
        }

        private void sendSwipeTelemetry(int i, int i2) {
            Adapter adapter = HomeFragment.this.banner.getAdapter();
            if (adapter != null) {
                int itemCount = adapter.getItemCount();
                if (i >= itemCount) {
                    i = itemCount - 1;
                }
                TelemetryWrapper.swipeBannerItem(i2 / Math.abs(i2), i);
            }
        }

        private void sendSwipeToIdTelemetry(int i) {
            View findViewByPosition = HomeFragment.this.bannerLayoutManager.findViewByPosition(i);
            if (findViewByPosition != null) {
                String id = ((BannerViewHolder) HomeFragment.this.banner.getChildViewHolder(findViewByPosition)).getId();
                if (id != null) {
                    TelemetryWrapper.showBannerSwipe(id);
                }
            }
        }

        public int findTargetSnapPosition(LayoutManager layoutManager, int i, int i2) {
            int findTargetSnapPosition = super.findTargetSnapPosition(layoutManager, i, i2);
            sendTelemetry(findTargetSnapPosition, i);
            return findTargetSnapPosition;
        }
    }

    static /* synthetic */ void lambda$mergePinSiteToTopSites$11(Site site) {
    }

    public Fragment getFragment() {
        return this;
    }

    public static HomeFragment create() {
        return new HomeFragment();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.presenter = new TopSitesPresenter();
        this.presenter.setView(this);
        this.presenter.setModel(this);
    }

    public void onStart() {
        super.onStart();
        showCurrentBannerTelemetry();
        TelemetryWrapper.showHome();
    }

    private void showCurrentBannerTelemetry() {
        if (this.banner.getVisibility() == 0 && this.bannerLayoutManager != null) {
            View childAt = this.banner.getChildAt(0);
            if (childAt != null) {
                String id = ((BannerViewHolder) this.banner.getChildViewHolder(childAt)).getId();
                if (id != null) {
                    TelemetryWrapper.showBannerReturn(id);
                }
            }
        }
    }

    private void showBanner(boolean z) {
        if (z) {
            this.banner.setVisibility(0);
        } else {
            this.banner.setVisibility(8);
        }
    }

    public boolean hideContentPortal() {
        return this.contentPanel != null ? this.contentPanel.hide() : false;
    }

    public void pinSite(Site site, Runnable runnable) {
        this.pinSiteManager.pin(site);
        runnable.run();
    }

    public void updateNews(List<? extends NewsItem> list) {
        this.contentPanel.setNewsContent(list);
    }

    private void initBanner(Context context) {
        try {
            new ReadStringFromFileTask(new GetCache(new WeakReference(context)).get(), "CURRENT_BANNER_CONFIG", this.bannerConfigViewModel.getConfig(), C0700-$$Lambda$HomeFragment$3IVmlahMzFufutXTYk3NkFbUg0I.INSTANCE).execute(new Void[0]);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            LoggerWrapper.throwOrWarn("HomeFragment", "Failed to open Cache directory when reading cached banner config");
        }
        if (TextUtils.isEmpty(AppConfigWrapper.getBannerRootConfig(context))) {
            deleteCache(context);
            this.banner.setAdapter(null);
            showBanner(false);
            return;
        }
        this.onRootConfigLoadedListener = new C0704-$$Lambda$HomeFragment$E3qF6zcvBwKUOoV8AnaHHon_CA4(this, context);
        new LoadRootConfigTask(new WeakReference(this.onRootConfigLoadedListener)).execute(new String[]{r1, WebViewProvider.getUserAgentString(getActivity()), Integer.toString(10002)});
    }

    public static /* synthetic */ void lambda$initBanner$0(HomeFragment homeFragment, Context context, String[] strArr) {
        homeFragment.writeToCache(context, strArr);
        homeFragment.bannerConfigViewModel.getConfig().setValue(strArr);
        homeFragment.onRootConfigLoadedListener = null;
    }

    private void setUpBannerFromConfig(String[] strArr) {
        if (!Arrays.equals(this.configArray, strArr)) {
            Object obj = this.configArray != null ? 1 : null;
            this.configArray = strArr;
            if (strArr == null || strArr.length == 0) {
                showBanner(false);
                return;
            }
            try {
                BannerAdapter bannerAdapter = new BannerAdapter(strArr, new C0703-$$Lambda$HomeFragment$DUsg3hhcZ8pxzytL2HDqVpMbzjY(this));
                this.banner.setAdapter(bannerAdapter);
                showBanner(true);
                if (obj != null) {
                    TelemetryWrapper.showBannerNew(bannerAdapter.getFirstDAOId());
                } else {
                    TelemetryWrapper.showBannerUpdate(bannerAdapter.getFirstDAOId());
                }
            } catch (JSONException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid Config: ");
                stringBuilder.append(e.getMessage());
                LoggerWrapper.throwOrWarn("HomeFragment", stringBuilder.toString());
            }
        }
    }

    private void writeToCache(Context context, String[] strArr) {
        try {
            ThreadUtils.postToBackgroundThread(new WriteStringToFileRunnable(new File(new GetCache(new WeakReference(context)).get(), "CURRENT_BANNER_CONFIG"), stringArrayToString(strArr)));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            LoggerWrapper.throwOrWarn("HomeFragment", "Failed to open cache directory when writing banner config to cache");
        }
    }

    private void deleteCache(Context context) {
        try {
            ThreadUtils.postToBackgroundThread(new DeleteFileRunnable(new File(new GetCache(new WeakReference(context)).get(), "CURRENT_BANNER_CONFIG")));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            LoggerWrapper.throwOrWarn("HomeFragment", "Failed to open cache directory when deleting banner cache");
        }
    }

    private String stringArrayToString(String[] strArr) {
        return TextUtils.join(UNIT_SEPARATOR, strArr);
    }

    private static String[] stringToStringArray(String str) {
        if (TextUtils.isEmpty(str)) {
            return new String[0];
        }
        return str.split(UNIT_SEPARATOR);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate;
        boolean hasNewsPortal = AppConfigWrapper.hasNewsPortal(getContext());
        if (hasNewsPortal || AppConfigWrapper.hasEcommerceShoppingLink()) {
            inflate = layoutInflater.inflate(C0769R.layout.fragment_homescreen_content, viewGroup, false);
            setupContentPortalView(inflate);
            if (hasNewsPortal && this.contentPanel != null) {
                this.newsPresenter = new NewsPresenter(this);
                this.contentPanel.setNewsListListener(this.newsPresenter);
            }
        } else {
            inflate = layoutInflater.inflate(C0769R.layout.fragment_homescreen, viewGroup, false);
        }
        this.recyclerView = (RecyclerView) inflate.findViewById(C0427R.C0426id.main_list);
        this.btnMenu = (ThemedImageButton) inflate.findViewById(C0427R.C0426id.btn_menu_home);
        this.btnMenu.setOnClickListener(this.menuItemClickListener);
        this.btnMenu.setOnLongClickListener(new C0488-$$Lambda$HomeFragment$zGgcPvdy6jYmbj_Kvpr_qyx4AWM(this));
        this.sessionManager = TabsSessionProvider.getOrThrow(getActivity());
        this.sessionManager.register(this.observer);
        this.tabCounter = (TabCounter) inflate.findViewById(C0427R.C0426id.btn_tab_tray);
        this.tabCounter.setOnClickListener(this.menuItemClickListener);
        updateTabCounter();
        this.fakeInput = (ThemedTextView) inflate.findViewById(C0427R.C0426id.home_fragment_fake_input);
        this.fakeInput.setOnClickListener(new C0485-$$Lambda$HomeFragment$UIHSgCmoFPI7-45MO2Yzk1mIzNk(this));
        this.banner = (RecyclerView) inflate.findViewById(C0427R.C0426id.banner);
        this.bannerLayoutManager = new LinearLayoutManager(getContext(), 0, false);
        this.banner.setLayoutManager(this.bannerLayoutManager);
        new C07672().attachToRecyclerView(this.banner);
        SwipeMotionLayout swipeMotionLayout = (SwipeMotionLayout) inflate.findViewById(C0427R.C0426id.home_container);
        swipeMotionLayout.setOnSwipeListener(new GestureListenerAdapter(this, null));
        if (ThemeManager.shouldShowOnboarding(inflate.getContext())) {
            LayoutInflater.from(inflate.getContext()).inflate(C0769R.layout.fragment_homescreen_themetoy, swipeMotionLayout);
            this.themeOnboardingLayer = swipeMotionLayout.findViewById(C0427R.C0426id.fragment_homescreen_theme_onboarding);
            this.themeOnboardingLayer.setOnClickListener(new C0486-$$Lambda$HomeFragment$h_QZudQdj4vHU5Xy6NRh6Tq3k3Q(this));
        }
        this.homeScreenBackground = (HomeScreenBackground) inflate.findViewById(C0427R.C0426id.home_background);
        this.downloadingIndicator = (LottieAnimationView) inflate.findViewById(C0427R.C0426id.downloading_indicator);
        this.downloadIndicator = (ImageView) inflate.findViewById(C0427R.C0426id.download_unread_indicator);
        Inject.obtainDownloadIndicatorViewModel(getActivity()).getDownloadIndicatorObservable().observe(getViewLifecycleOwner(), new C0713-$$Lambda$HomeFragment$aKgQg76STLRCTef8h8RsAwe8_q4(this));
        if (this.newsPresenter != null) {
            this.newsPresenter.setupNewsViewModel(getActivity());
        }
        return inflate;
    }

    public static /* synthetic */ boolean lambda$onCreateView$2(HomeFragment homeFragment, View view) {
        C0572-CC.notifyParent(homeFragment, TYPE.SHOW_DOWNLOAD_PANEL, null);
        TelemetryWrapper.longPressDownloadIndicator();
        return false;
    }

    public static /* synthetic */ void lambda$onCreateView$3(HomeFragment homeFragment, View view) {
        FragmentActivity activity = homeFragment.getActivity();
        if (activity instanceof FragmentListener) {
            ((FragmentListener) activity).onNotified(homeFragment, TYPE.SHOW_URL_INPUT, null);
        }
        TelemetryWrapper.showSearchBarHome();
    }

    public static /* synthetic */ void lambda$onCreateView$4(HomeFragment homeFragment, View view) {
        if (homeFragment.themeOnboardingLayer != null) {
            ThemeManager.dismissOnboarding(homeFragment.themeOnboardingLayer.getContext().getApplicationContext());
            ((ViewGroup) homeFragment.themeOnboardingLayer.getParent()).removeView(homeFragment.themeOnboardingLayer);
            homeFragment.themeOnboardingLayer = null;
        }
    }

    public static /* synthetic */ void lambda$onCreateView$5(HomeFragment homeFragment, Status status) {
        if (status == Status.DOWNLOADING) {
            homeFragment.downloadIndicator.setVisibility(8);
            homeFragment.downloadingIndicator.setVisibility(0);
            if (!homeFragment.downloadingIndicator.isAnimating()) {
                homeFragment.downloadingIndicator.playAnimation();
            }
        } else if (status == Status.UNREAD) {
            homeFragment.downloadingIndicator.setVisibility(8);
            homeFragment.downloadIndicator.setVisibility(0);
            homeFragment.downloadIndicator.setImageResource(2131230948);
        } else if (status == Status.WARNING) {
            homeFragment.downloadingIndicator.setVisibility(8);
            homeFragment.downloadIndicator.setVisibility(0);
            homeFragment.downloadIndicator.setImageResource(2131230949);
        } else {
            homeFragment.downloadingIndicator.setVisibility(8);
            homeFragment.downloadIndicator.setVisibility(8);
        }
    }

    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Firebase_ready");
        this.receiver = new C04903();
        Context context = getContext();
        if (context != null) {
            LocalBroadcastManager.getInstance(context).registerReceiver(this.receiver, intentFilter);
        }
        updateTopSitesData();
        setupBannerTimer();
        setNightModeEnabled(Settings.getInstance(getActivity()).isNightModeEnable());
        View view = getView();
        if (view != null) {
            initFeatureSurveyViewIfNecessary(view);
        }
        playContentPortalAnimation();
        if (this.contentPanel != null) {
            this.contentPanel.onResume();
        }
    }

    private void playContentPortalAnimation() {
        Animation loadAnimation = AnimationUtils.loadAnimation(getActivity(), C0769R.anim.arrow_fade_out);
        Animation loadAnimation2 = AnimationUtils.loadAnimation(getActivity(), C0769R.anim.arrow_fade_in);
        Inject.startAnimation(this.arrow1, loadAnimation);
        Inject.startAnimation(this.arrow2, loadAnimation2);
    }

    private void setupBannerTimer() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new C04914(), 10000, 10000);
    }

    public void onPause() {
        super.onPause();
        Context context = getContext();
        if (context != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this.receiver);
        }
        this.timer.cancel();
        this.timer = null;
        stopAnimation();
    }

    private void stopAnimation() {
        if (!(this.arrow1 == null || this.arrow1.getAnimation() == null)) {
            this.arrow1.getAnimation().cancel();
        }
        if (this.arrow2 != null && this.arrow2.getAnimation() != null) {
            this.arrow2.getAnimation().cancel();
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        doWithActivity(getActivity(), new C0715-$$Lambda$HomeFragment$tUWOs_ryJciFQgYEqFIG0QAoSRs(this));
        Context context = getContext();
        this.bannerConfigViewModel = (BannerConfigViewModel) ViewModelProviders.m0of((Fragment) this).get(BannerConfigViewModel.class);
        this.bannerConfigViewModel.getConfig().observe(this, this.bannerObserver);
        initBanner(context);
        if (context != null) {
            this.pinSiteManager = PinSiteManagerKt.getPinSiteManager(context);
        }
    }

    public void onDestroyView() {
        this.sessionManager.unregister(this.observer);
        doWithActivity(getActivity(), new C0701-$$Lambda$HomeFragment$56pfa0bwmcBYtyZ82ODqVQewkWU(this));
        this.bannerConfigViewModel.getConfig().removeObserver(this.bannerObserver);
        super.onDestroyView();
    }

    public void showSites(List<Site> list) {
        Collections.sort(list, new TopSideComparator());
        if (this.topSiteAdapter == null) {
            this.topSiteAdapter = new TopSiteAdapter(list, this.clickListener, this.clickListener, this.pinSiteManager);
            this.recyclerView.setAdapter(this.topSiteAdapter);
            return;
        }
        this.recyclerView.setAdapter(this.topSiteAdapter);
        this.topSiteAdapter.setSites(list);
    }

    public void applyLocale() {
        this.fakeInput.setText(C0769R.string.urlbar_hint);
    }

    public void removeSite(Site site) {
        this.topSiteAdapter.setSites(this.presenter.getSites());
    }

    public void onUrlInputScreenVisible(boolean z) {
        this.fakeInput.setVisibility(z ? 4 : 0);
    }

    private void updateTabCounter() {
        int tabsCount = this.sessionManager != null ? this.sessionManager.getTabsCount() : 0;
        if (isTabRestoredComplete()) {
            this.tabCounter.setCount(tabsCount);
        }
        if (tabsCount == 0) {
            this.tabCounter.setEnabled(false);
            this.tabCounter.setAlpha(0.3f);
            return;
        }
        this.tabCounter.setEnabled(true);
        this.tabCounter.setAlpha(1.0f);
    }

    private boolean isTabRestoredComplete() {
        return (getActivity() instanceof MainActivity) && ((MainActivity) getActivity()).isTabRestoredComplete();
    }

    public static /* synthetic */ void lambda$new$8(HomeFragment homeFragment, List list) {
        ArrayList arrayList = new ArrayList();
        for (Object next : list) {
            if (next instanceof Site) {
                arrayList.add((Site) next);
            }
        }
        homeFragment.constructTopSiteList(arrayList);
    }

    private void refreshTopSites() {
        BrowsingHistoryManager.getInstance().queryTopSites(8, 6, this.mTopSitesQueryListener);
    }

    private void constructTopSiteList(List<Site> list) {
        initDefaultSitesFromJSONArray(this.orginalDefaultSites);
        List arrayList = new ArrayList(this.presenter.getSites());
        mergeHistorySiteToTopSites(list, arrayList);
        mergePinSiteToTopSites(this.pinSiteManager.getPinSites(), arrayList);
        Collections.sort(arrayList, new TopSideComparator());
        if (arrayList.size() > 8) {
            removeDefaultSites(arrayList.subList(8, arrayList.size()));
            arrayList = arrayList.subList(0, 8);
        }
        this.presenter.setSites(arrayList);
        this.presenter.populateSites();
    }

    private void mergeHistorySiteToTopSites(List<Site> list, List<Site> list2) {
        for (Site site : list2) {
            removeDuplicatedSites(list, site, new C0714-$$Lambda$HomeFragment$iYsX7OQrdrr_G4JZR-T6E-agQj8(site));
        }
        list2.addAll(list);
    }

    private void mergePinSiteToTopSites(List<Site> list, List<Site> list2) {
        for (Site removeDuplicatedSites : list) {
            removeDuplicatedSites(list2, removeDuplicatedSites, C0702-$$Lambda$HomeFragment$5CWSG_uCygmRouU50LQwUcKTRjw.INSTANCE);
        }
        list2.addAll(list);
    }

    private void initDefaultSites() {
        String defaultTopSites = Inject.getDefaultTopSites(getContext());
        if (defaultTopSites == null) {
            this.orginalDefaultSites = TopSitesUtils.getDefaultSitesJsonArrayFromAssets(getContext());
        } else {
            try {
                this.orginalDefaultSites = new JSONArray(defaultTopSites);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
        initDefaultSitesFromJSONArray(this.orginalDefaultSites);
    }

    private void initDefaultSitesFromJSONArray(JSONArray jSONArray) {
        this.presenter.setSites(TopSitesUtils.paresJsonToList(getContext(), jSONArray));
    }

    private void removeDefaultSites(List<Site> list) {
        Object obj = null;
        for (int i = 0; i < list.size(); i++) {
            Site site = (Site) list.get(i);
            if (site.getId() < 0) {
                removeDefaultSites(site);
                obj = 1;
            }
        }
        if (obj != null) {
            TopSitesUtils.saveDefaultSites(getContext(), this.orginalDefaultSites);
        }
    }

    private void removeDefaultSites(Site site) {
        try {
            if (this.orginalDefaultSites != null) {
                for (int i = 0; i < this.orginalDefaultSites.length(); i++) {
                    if (((JSONObject) this.orginalDefaultSites.get(i)).getLong("id") == site.getId()) {
                        this.orginalDefaultSites.remove(i);
                        return;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initFeatureSurveyViewIfNecessary(View view) {
        SURVEY parseLong = SURVEY.Companion.parseLong(AppConfigWrapper.getFeatureSurvey(getContext()));
        ImageView imageView = (ImageView) view.findViewById(C0427R.C0426id.home_wifi_vpn_survey);
        EventHistory eventHistory = Settings.getInstance(getContext()).getEventHistory();
        if (parseLong == SURVEY.WIFI_FINDING && !eventHistory.contains("feature_survey_wifi_finding")) {
            imageView.setImageResource(2131230865);
            imageView.setVisibility(0);
            if (getContext() != null) {
                imageView.setOnClickListener(new FeatureSurveyViewHelper(getContext(), parseLong));
            }
        } else if (parseLong == SURVEY.VPN && !eventHistory.contains("feature_survey_vpn")) {
            imageView.setImageResource(2131230984);
            imageView.setVisibility(0);
            if (getContext() != null) {
                imageView.setOnClickListener(new FeatureSurveyViewHelper(getContext(), parseLong));
            }
        } else if (parseLong != SURVEY.VPN_RECOMMENDER || eventHistory.contains("vpn_recommender_ignore")) {
            imageView.setVisibility(8);
        } else {
            PackageInfo packageInfo = null;
            String vpnRecommenderPackage = AppConfigWrapper.getVpnRecommenderPackage(getActivity());
            try {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    packageInfo = activity.getPackageManager().getPackageInfo(vpnRecommenderPackage, 0);
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            if (packageInfo != null) {
                eventHistory.add("vpn_app_was_downloaded");
                imageView.setImageResource(2131230984);
                imageView.setVisibility(0);
                imageView.setOnClickListener(new C0487-$$Lambda$HomeFragment$y9mK7LxbJRaTHVOTIMr4dniYppM(this, vpnRecommenderPackage));
                TelemetryWrapper.showVpnRecommender(true);
            } else if (eventHistory.contains("vpn_app_was_downloaded")) {
                imageView.setVisibility(8);
            } else {
                if (getContext() != null) {
                    imageView.setOnClickListener(new FeatureSurveyViewHelper(getContext(), parseLong));
                    imageView.setVisibility(0);
                }
                TelemetryWrapper.showVpnRecommender(false);
            }
        }
    }

    public static /* synthetic */ void lambda$initFeatureSurveyViewIfNecessary$12(HomeFragment homeFragment, String str, View view) {
        homeFragment.startActivity(homeFragment.getActivity().getPackageManager().getLaunchIntentForPackage(str));
        TelemetryWrapper.clickVpnRecommender(true);
    }

    public void updateTopSitesData() {
        initDefaultSites();
        if (PreferenceManager.getDefaultSharedPreferences(getContext()).contains("top_sites_v2_complete")) {
            refreshTopSites();
        } else {
            new Thread(new MigrateHistoryRunnable(this.uiHandler, getContext())).start();
        }
    }

    private void removeDuplicatedSites(List<Site> list, Site site, OnRemovedListener onRemovedListener) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Site site2 = (Site) it.next();
            if (UrlUtils.urlsMatchExceptForTrailingSlash(site2.getUrl(), site.getUrl())) {
                it.remove();
                onRemovedListener.onRemoved(site2);
            }
        }
    }

    private static void parseCursorToSite(Cursor cursor, List<String> list, List<byte[]> list2) {
        String string = cursor.getString(cursor.getColumnIndex("url"));
        byte[] blob = cursor.getBlob(cursor.getColumnIndex("fav_icon"));
        list.add(string);
        list2.add(blob);
    }

    /* JADX WARNING: Missing block: B:9:0x001d, code skipped:
            return;
     */
    private static void doWithActivity(android.app.Activity r1, org.mozilla.focus.home.HomeFragment.DoWithThemeManager r2) {
        /*
        if (r1 == 0) goto L_0x001d;
    L_0x0002:
        r0 = r1.isFinishing();
        if (r0 != 0) goto L_0x001d;
    L_0x0008:
        r0 = r1.isDestroyed();
        if (r0 == 0) goto L_0x000f;
    L_0x000e:
        goto L_0x001d;
    L_0x000f:
        r0 = r1 instanceof org.mozilla.rocket.theme.ThemeManager.ThemeHost;
        if (r0 == 0) goto L_0x001c;
    L_0x0013:
        r1 = (org.mozilla.rocket.theme.ThemeManager.ThemeHost) r1;
        r1 = r1.getThemeManager();
        r2.doIt(r1);
    L_0x001c:
        return;
    L_0x001d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.home.HomeFragment.doWithActivity(android.app.Activity, org.mozilla.focus.home.HomeFragment$DoWithThemeManager):void");
    }

    private static void scheduleRefresh(Handler handler) {
        handler.dispatchMessage(handler.obtainMessage(8269));
    }

    public void setNightModeEnabled(boolean z) {
        this.fakeInput.setNightMode(z);
        this.btnMenu.setNightMode(z);
        this.tabCounter.setNightMode(z);
        this.homeScreenBackground.setNightMode(z);
        for (int i = 0; i < this.recyclerView.getChildCount(); i++) {
            ((ThemedTextView) this.recyclerView.getChildAt(i).findViewById(2131296685)).setNightMode(z);
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ViewUtils.updateStatusBarStyle(z ^ 1, activity.getWindow());
        }
    }

    private void showContentPortal() {
        if (this.contentPanel != null) {
            this.contentPanel.show(true);
            if (AppConfigWrapper.hasEcommerceShoppingLink()) {
                TelemetryWrapper.openLifeFeedEc();
            } else {
                TelemetryWrapper.openLifeFeedNews();
            }
        }
    }

    private void setupContentPortalView(View view) {
        this.arrow1 = (ImageButton) view.findViewById(C0427R.C0426id.arrow1);
        this.arrow2 = (ImageButton) view.findViewById(C0427R.C0426id.arrow2);
        this.contentPanel = (ContentPortalView) view.findViewById(C0427R.C0426id.content_panel);
        view = view.findViewById(C0427R.C0426id.arrow_container);
        if (view != null) {
            view.setOnTouchListener(new SwipeMotionDetector(getContext(), new C07166()));
        }
    }
}
