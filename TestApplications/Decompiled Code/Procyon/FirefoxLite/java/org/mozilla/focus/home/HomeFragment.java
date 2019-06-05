// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.home;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.webkit.WebChromeClient$FileChooserParams;
import android.net.Uri;
import android.webkit.ValueCallback;
import org.mozilla.rocket.tabs.TabViewEngineSession;
import org.mozilla.rocket.tabs.TabViewClient;
import org.mozilla.rocket.tabs.Session;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteDatabase;
import org.mozilla.icon.FavIconUtils;
import android.graphics.Bitmap$CompressFormat;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;
import org.mozilla.rocket.persistance.History.HistoryDatabase;
import java.util.concurrent.atomic.AtomicInteger;
import org.mozilla.httptask.SimpleLoadUrlTask;
import android.preference.PreferenceManager;
import org.mozilla.lite.partner.NewsItem;
import org.mozilla.focus.utils.ViewUtils;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import org.mozilla.focus.widget.SwipeMotionLayout;
import android.support.v7.widget.PagerSnapHelper;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import android.view.View$OnLongClickListener;
import android.view.LayoutInflater;
import org.mozilla.rocket.home.pinsite.PinSiteManagerKt;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import org.mozilla.rocket.banner.BannerViewHolder;
import android.view.View$OnTouchListener;
import org.mozilla.focus.utils.SwipeMotionDetector;
import org.mozilla.focus.utils.OnSwipeListener$_CC;
import org.mozilla.focus.utils.OnSwipeListener;
import java.util.TimerTask;
import org.mozilla.rocket.banner.OnClickListener;
import org.mozilla.rocket.banner.BannerAdapter;
import java.util.Arrays;
import org.mozilla.urlutils.UrlUtils;
import org.json.JSONObject;
import org.mozilla.focus.history.BrowsingHistoryManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;
import android.view.ViewGroup;
import org.mozilla.focus.widget.FragmentListener$_CC;
import java.util.Iterator;
import org.mozilla.focus.activity.MainActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.RemoteConfigConstants;
import org.json.JSONException;
import org.mozilla.focus.utils.TopSitesUtils;
import org.mozilla.focus.Inject;
import org.mozilla.focus.web.WebViewProvider;
import android.text.TextUtils;
import org.mozilla.focus.utils.AppConfigWrapper;
import android.arch.lifecycle.MutableLiveData;
import org.mozilla.rocket.theme.ThemeManager;
import java.util.concurrent.ExecutionException;
import org.mozilla.rocket.util.LoggerWrapper;
import org.mozilla.threadutils.ThreadUtils;
import java.io.File;
import org.mozilla.fileutils.FileUtils;
import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import org.mozilla.focus.history.model.Site;
import android.content.Context;
import android.app.Activity;
import java.util.List;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.support.v4.app.Fragment;
import org.mozilla.focus.widget.FragmentListener;
import android.os.Message;
import android.os.Looper;
import android.os.Handler;
import java.util.Timer;
import android.view.View;
import org.mozilla.focus.tabs.TabCounter;
import org.mozilla.rocket.tabs.SessionManager;
import android.content.BroadcastReceiver;
import org.mozilla.rocket.home.pinsite.PinSiteManager;
import org.json.JSONArray;
import org.mozilla.rocket.content.NewsPresenter;
import android.view.View$OnClickListener;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;
import com.airbnb.lottie.LottieAnimationView;
import android.widget.ImageView;
import org.mozilla.rocket.content.ContentPortalView;
import org.mozilla.rocket.nightmode.themed.ThemedImageButton;
import android.arch.lifecycle.Observer;
import android.support.v7.widget.LinearLayoutManager;
import org.mozilla.rocket.banner.BannerConfigViewModel;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import org.mozilla.rocket.content.NewsViewContract;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.locale.LocaleAwareFragment;

public class HomeFragment extends LocaleAwareFragment implements Model, View, HomeScreen, NewsViewContract
{
    private static final String UNIT_SEPARATOR;
    private ImageButton arrow1;
    private ImageButton arrow2;
    private RecyclerView banner;
    private BannerConfigViewModel bannerConfigViewModel;
    private LinearLayoutManager bannerLayoutManager;
    final Observer<String[]> bannerObserver;
    private ThemedImageButton btnMenu;
    private SiteItemClickListener clickListener;
    private String[] configArray;
    private ContentPortalView contentPanel;
    private ImageView downloadIndicator;
    private LottieAnimationView downloadingIndicator;
    private ThemedTextView fakeInput;
    private HomeScreenBackground homeScreenBackground;
    private QueryHandler.AsyncUpdateListener mTopSiteUpdateListener;
    private QueryHandler.AsyncQueryListener mTopSitesQueryListener;
    private View$OnClickListener menuItemClickListener;
    private NewsPresenter newsPresenter;
    private final SessionManagerObserver observer;
    private OnRootConfigLoadedListener onRootConfigLoadedListener;
    private JSONArray orginalDefaultSites;
    private PinSiteManager pinSiteManager;
    private Presenter presenter;
    private BroadcastReceiver receiver;
    private RecyclerView recyclerView;
    private SessionManager sessionManager;
    private TabCounter tabCounter;
    private android.view.View themeOnboardingLayer;
    private Timer timer;
    private TopSiteAdapter topSiteAdapter;
    private Handler uiHandler;
    
    static {
        UNIT_SEPARATOR = Character.toString('\u001f');
    }
    
    public HomeFragment() {
        this.clickListener = new SiteItemClickListener();
        this.orginalDefaultSites = null;
        this.observer = new SessionManagerObserver();
        this.bannerObserver = (Observer<String[]>)new _$$Lambda$HomeFragment$Y6w2k3vFU3uhQsU1sAqbNcJ0r8A(this);
        this.newsPresenter = null;
        this.uiHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(final Message message) {
                if (message.what == 8269) {
                    HomeFragment.this.refreshTopSites();
                }
            }
        };
        this.mTopSitesQueryListener = new _$$Lambda$HomeFragment$Ht2Yxo_dd5eyf5eF_hvmXPMe380(this);
        this.mTopSiteUpdateListener = new _$$Lambda$HomeFragment$F55KXmKZ14h6rPSlCB5EojJPzKw(this);
        this.menuItemClickListener = (View$OnClickListener)new View$OnClickListener() {
            private void dispatchOnClick(final android.view.View view, final FragmentListener fragmentListener) {
                final int id = view.getId();
                if (id != 2131296351) {
                    if (id == 2131296357) {
                        fragmentListener.onNotified(HomeFragment.this, FragmentListener.TYPE.SHOW_TAB_TRAY, null);
                        TelemetryWrapper.showTabTrayHome();
                    }
                }
                else {
                    fragmentListener.onNotified(HomeFragment.this, FragmentListener.TYPE.SHOW_MENU, null);
                    TelemetryWrapper.showMenuHome();
                }
            }
            
            public void onClick(final android.view.View view) {
                final FragmentActivity activity = HomeFragment.this.getActivity();
                if (activity instanceof FragmentListener) {
                    this.dispatchOnClick(view, (FragmentListener)activity);
                }
            }
        };
    }
    
    private void constructTopSiteList(final List<Site> list) {
        this.initDefaultSitesFromJSONArray(this.orginalDefaultSites);
        final ArrayList<Object> list2 = new ArrayList<Object>(this.presenter.getSites());
        this.mergeHistorySiteToTopSites(list, (List<Site>)list2);
        this.mergePinSiteToTopSites(this.pinSiteManager.getPinSites(), (List<Site>)list2);
        Collections.sort(list2, (Comparator<? super Object>)new TopSideComparator());
        Object subList = list2;
        if (list2.size() > 8) {
            this.removeDefaultSites((List<Site>)list2.subList(8, list2.size()));
            subList = list2.subList(0, 8);
        }
        this.presenter.setSites((List<Site>)subList);
        this.presenter.populateSites();
    }
    
    public static HomeFragment create() {
        return new HomeFragment();
    }
    
    private void deleteCache(final Context referent) {
        try {
            ThreadUtils.postToBackgroundThread(new FileUtils.DeleteFileRunnable(new File(new FileUtils.GetCache(new WeakReference<Context>(referent)).get(), "CURRENT_BANNER_CONFIG")));
        }
        catch (ExecutionException | InterruptedException ex) {
            final Throwable t;
            t.printStackTrace();
            LoggerWrapper.throwOrWarn("HomeFragment", "Failed to open cache directory when deleting banner cache");
        }
    }
    
    private static void doWithActivity(final Activity activity, final DoWithThemeManager doWithThemeManager) {
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            if (activity instanceof ThemeManager.ThemeHost) {
                doWithThemeManager.doIt(((ThemeManager.ThemeHost)activity).getThemeManager());
            }
        }
    }
    
    private void initBanner(final Context referent) {
        try {
            new FileUtils.ReadStringFromFileTask(new FileUtils.GetCache(new WeakReference<Context>(referent)).get(), "CURRENT_BANNER_CONFIG", this.bannerConfigViewModel.getConfig(), (FileUtils.LiveDataTask.Function)_$$Lambda$HomeFragment$3IVmlahMzFufutXTYk3NkFbUg0I.INSTANCE).execute((Object[])new Void[0]);
        }
        catch (ExecutionException | InterruptedException ex) {
            final Throwable t;
            t.printStackTrace();
            LoggerWrapper.throwOrWarn("HomeFragment", "Failed to open Cache directory when reading cached banner config");
        }
        final String bannerRootConfig = AppConfigWrapper.getBannerRootConfig(referent);
        if (TextUtils.isEmpty((CharSequence)bannerRootConfig)) {
            this.deleteCache(referent);
            this.banner.setAdapter(null);
            this.showBanner(false);
        }
        else {
            this.onRootConfigLoadedListener = (OnRootConfigLoadedListener)new _$$Lambda$HomeFragment$E3qF6zcvBwKUOoV8AnaHHon_CA4(this, referent);
            new LoadRootConfigTask(new WeakReference<OnRootConfigLoadedListener>(this.onRootConfigLoadedListener)).execute((Object[])new String[] { bannerRootConfig, WebViewProvider.getUserAgentString((Context)this.getActivity()), Integer.toString(10002) });
        }
    }
    
    private void initDefaultSites() {
        final String defaultTopSites = Inject.getDefaultTopSites(this.getContext());
        Label_0040: {
            if (defaultTopSites == null) {
                this.orginalDefaultSites = TopSitesUtils.getDefaultSitesJsonArrayFromAssets(this.getContext());
                break Label_0040;
            }
            try {
                this.orginalDefaultSites = new JSONArray(defaultTopSites);
                this.initDefaultSitesFromJSONArray(this.orginalDefaultSites);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void initDefaultSitesFromJSONArray(final JSONArray jsonArray) {
        this.presenter.setSites(TopSitesUtils.paresJsonToList(this.getContext(), jsonArray));
    }
    
    private void initFeatureSurveyViewIfNecessary(final android.view.View view) {
        final RemoteConfigConstants.SURVEY long1 = RemoteConfigConstants.SURVEY.Companion.parseLong(AppConfigWrapper.getFeatureSurvey(this.getContext()));
        final ImageView imageView = (ImageView)view.findViewById(2131296470);
        final Settings.EventHistory eventHistory = Settings.getInstance(this.getContext()).getEventHistory();
        if (long1 == RemoteConfigConstants.SURVEY.WIFI_FINDING && !eventHistory.contains("feature_survey_wifi_finding")) {
            imageView.setImageResource(2131230865);
            imageView.setVisibility(0);
            if (this.getContext() != null) {
                imageView.setOnClickListener((View$OnClickListener)new FeatureSurveyViewHelper(this.getContext(), long1));
            }
        }
        else if (long1 == RemoteConfigConstants.SURVEY.VPN && !eventHistory.contains("feature_survey_vpn")) {
            imageView.setImageResource(2131230984);
            imageView.setVisibility(0);
            if (this.getContext() != null) {
                imageView.setOnClickListener((View$OnClickListener)new FeatureSurveyViewHelper(this.getContext(), long1));
            }
        }
        else if (long1 == RemoteConfigConstants.SURVEY.VPN_RECOMMENDER && !eventHistory.contains("vpn_recommender_ignore")) {
            final PackageInfo packageInfo = null;
            final String vpnRecommenderPackage = AppConfigWrapper.getVpnRecommenderPackage((Context)this.getActivity());
            PackageInfo packageInfo2;
            try {
                final FragmentActivity activity = this.getActivity();
                packageInfo2 = packageInfo;
                if (activity != null) {
                    packageInfo2 = activity.getPackageManager().getPackageInfo(vpnRecommenderPackage, 0);
                }
            }
            catch (PackageManager$NameNotFoundException ex) {
                ex.printStackTrace();
                packageInfo2 = packageInfo;
            }
            if (packageInfo2 != null) {
                eventHistory.add("vpn_app_was_downloaded");
                imageView.setImageResource(2131230984);
                imageView.setVisibility(0);
                imageView.setOnClickListener((View$OnClickListener)new _$$Lambda$HomeFragment$y9mK7LxbJRaTHVOTIMr4dniYppM(this, vpnRecommenderPackage));
                TelemetryWrapper.showVpnRecommender(true);
            }
            else if (eventHistory.contains("vpn_app_was_downloaded")) {
                imageView.setVisibility(8);
            }
            else {
                if (this.getContext() != null) {
                    imageView.setOnClickListener((View$OnClickListener)new FeatureSurveyViewHelper(this.getContext(), long1));
                    imageView.setVisibility(0);
                }
                TelemetryWrapper.showVpnRecommender(false);
            }
        }
        else {
            imageView.setVisibility(8);
        }
    }
    
    private boolean isTabRestoredComplete() {
        return this.getActivity() instanceof MainActivity && ((MainActivity)this.getActivity()).isTabRestoredComplete();
    }
    
    private void mergeHistorySiteToTopSites(final List<Site> list, final List<Site> list2) {
        for (final Site site : list2) {
            this.removeDuplicatedSites(list, site, (OnRemovedListener)new _$$Lambda$HomeFragment$iYsX7OQrdrr_G4JZR_T6E_agQj8(site));
        }
        list2.addAll(list);
    }
    
    private void mergePinSiteToTopSites(final List<Site> list, final List<Site> list2) {
        final Iterator<Site> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.removeDuplicatedSites(list2, iterator.next(), (OnRemovedListener)_$$Lambda$HomeFragment$5CWSG_uCygmRouU50LQwUcKTRjw.INSTANCE);
        }
        list2.addAll(list);
    }
    
    private static void parseCursorToSite(final Cursor cursor, final List<String> list, final List<byte[]> list2) {
        final String string = cursor.getString(cursor.getColumnIndex("url"));
        final byte[] blob = cursor.getBlob(cursor.getColumnIndex("fav_icon"));
        list.add(string);
        list2.add(blob);
    }
    
    private void playContentPortalAnimation() {
        final Animation loadAnimation = AnimationUtils.loadAnimation((Context)this.getActivity(), 2130771981);
        final Animation loadAnimation2 = AnimationUtils.loadAnimation((Context)this.getActivity(), 2130771980);
        Inject.startAnimation((android.view.View)this.arrow1, loadAnimation);
        Inject.startAnimation((android.view.View)this.arrow2, loadAnimation2);
    }
    
    private void refreshTopSites() {
        BrowsingHistoryManager.getInstance().queryTopSites(8, 6, this.mTopSitesQueryListener);
    }
    
    private void removeDefaultSites(final List<Site> list) {
        int i = 0;
        boolean b = false;
        while (i < list.size()) {
            final Site site = list.get(i);
            if (site.getId() < 0L) {
                this.removeDefaultSites(site);
                b = true;
            }
            ++i;
        }
        if (b) {
            TopSitesUtils.saveDefaultSites(this.getContext(), this.orginalDefaultSites);
        }
    }
    
    private void removeDefaultSites(final Site site) {
        try {
            if (this.orginalDefaultSites != null) {
                Block_4: {
                    for (int i = 0; i < this.orginalDefaultSites.length(); ++i) {
                        if (((JSONObject)this.orginalDefaultSites.get(i)).getLong("id") == site.getId()) {
                            break Block_4;
                        }
                    }
                    return;
                }
                int i = 0;
                this.orginalDefaultSites.remove(i);
            }
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    private void removeDuplicatedSites(final List<Site> list, final Site site, final OnRemovedListener onRemovedListener) {
        final Iterator<Site> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Site site2 = iterator.next();
            if (UrlUtils.urlsMatchExceptForTrailingSlash(site2.getUrl(), site.getUrl())) {
                iterator.remove();
                onRemovedListener.onRemoved(site2);
            }
        }
    }
    
    private static void scheduleRefresh(final Handler handler) {
        handler.dispatchMessage(handler.obtainMessage(8269));
    }
    
    private void setUpBannerFromConfig(final String[] array) {
        if (Arrays.equals(this.configArray, array)) {
            return;
        }
        final boolean b = this.configArray != null;
        this.configArray = array;
        if (array != null && array.length != 0) {
            try {
                final BannerAdapter adapter = new BannerAdapter(array, new _$$Lambda$HomeFragment$DUsg3hhcZ8pxzytL2HDqVpMbzjY(this));
                this.banner.setAdapter((RecyclerView.Adapter)adapter);
                this.showBanner(true);
                if (b) {
                    TelemetryWrapper.showBannerNew(adapter.getFirstDAOId());
                }
                else {
                    TelemetryWrapper.showBannerUpdate(adapter.getFirstDAOId());
                }
            }
            catch (JSONException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid Config: ");
                sb.append(ex.getMessage());
                LoggerWrapper.throwOrWarn("HomeFragment", sb.toString());
            }
            return;
        }
        this.showBanner(false);
    }
    
    private void setupBannerTimer() {
        (this.timer = new Timer()).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final RecyclerView.Adapter adapter = HomeFragment.this.banner.getAdapter();
                if (adapter == null) {
                    this.cancel();
                    return;
                }
                HomeFragment.this.banner.smoothScrollToPosition((HomeFragment.this.bannerLayoutManager.findFirstVisibleItemPosition() + 1) % adapter.getItemCount());
            }
        }, 10000L, 10000L);
    }
    
    private void setupContentPortalView(android.view.View viewById) {
        this.arrow1 = (ImageButton)viewById.findViewById(2131296296);
        this.arrow2 = (ImageButton)viewById.findViewById(2131296297);
        this.contentPanel = (ContentPortalView)viewById.findViewById(2131296378);
        viewById = viewById.findViewById(2131296298);
        if (viewById != null) {
            viewById.setOnTouchListener((View$OnTouchListener)new SwipeMotionDetector(this.getContext(), new OnSwipeListener() {
                @Override
                public boolean onSingleTapConfirmed() {
                    HomeFragment.this.showContentPortal();
                    return true;
                }
                
                @Override
                public void onSwipeUp() {
                    HomeFragment.this.showContentPortal();
                }
            }));
        }
    }
    
    private void showBanner(final boolean b) {
        if (b) {
            this.banner.setVisibility(0);
        }
        else {
            this.banner.setVisibility(8);
        }
    }
    
    private void showContentPortal() {
        if (this.contentPanel != null) {
            this.contentPanel.show(true);
            if (AppConfigWrapper.hasEcommerceShoppingLink()) {
                TelemetryWrapper.openLifeFeedEc();
            }
            else {
                TelemetryWrapper.openLifeFeedNews();
            }
        }
    }
    
    private void showCurrentBannerTelemetry() {
        if (this.banner.getVisibility() != 0 || this.bannerLayoutManager == null) {
            return;
        }
        final android.view.View child = this.banner.getChildAt(0);
        if (child == null) {
            return;
        }
        final String id = ((BannerViewHolder)this.banner.getChildViewHolder(child)).getId();
        if (id == null) {
            return;
        }
        TelemetryWrapper.showBannerReturn(id);
    }
    
    private void stopAnimation() {
        if (this.arrow1 != null && this.arrow1.getAnimation() != null) {
            this.arrow1.getAnimation().cancel();
        }
        if (this.arrow2 != null && this.arrow2.getAnimation() != null) {
            this.arrow2.getAnimation().cancel();
        }
    }
    
    private String stringArrayToString(final String[] array) {
        return TextUtils.join((CharSequence)HomeFragment.UNIT_SEPARATOR, (Object[])array);
    }
    
    private static String[] stringToStringArray(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return new String[0];
        }
        return s.split(HomeFragment.UNIT_SEPARATOR);
    }
    
    private void updateTabCounter() {
        int tabsCount;
        if (this.sessionManager != null) {
            tabsCount = this.sessionManager.getTabsCount();
        }
        else {
            tabsCount = 0;
        }
        if (this.isTabRestoredComplete()) {
            this.tabCounter.setCount(tabsCount);
        }
        if (tabsCount == 0) {
            this.tabCounter.setEnabled(false);
            this.tabCounter.setAlpha(0.3f);
        }
        else {
            this.tabCounter.setEnabled(true);
            this.tabCounter.setAlpha(1.0f);
        }
    }
    
    private void writeToCache(final Context referent, final String[] array) {
        try {
            ThreadUtils.postToBackgroundThread(new FileUtils.WriteStringToFileRunnable(new File(new FileUtils.GetCache(new WeakReference<Context>(referent)).get(), "CURRENT_BANNER_CONFIG"), this.stringArrayToString(array)));
        }
        catch (ExecutionException | InterruptedException ex) {
            final Throwable t;
            t.printStackTrace();
            LoggerWrapper.throwOrWarn("HomeFragment", "Failed to open cache directory when writing banner config to cache");
        }
    }
    
    @Override
    public void applyLocale() {
        this.fakeInput.setText(2131755426);
    }
    
    @Override
    public Fragment getFragment() {
        return this;
    }
    
    public boolean hideContentPortal() {
        return this.contentPanel != null && this.contentPanel.hide();
    }
    
    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        doWithActivity(this.getActivity(), (DoWithThemeManager)new _$$Lambda$HomeFragment$tUWOs_ryJciFQgYEqFIG0QAoSRs(this));
        final Context context = this.getContext();
        this.bannerConfigViewModel = ViewModelProviders.of(this).get(BannerConfigViewModel.class);
        this.bannerConfigViewModel.getConfig().observe(this, this.bannerObserver);
        this.initBanner(context);
        if (context != null) {
            this.pinSiteManager = PinSiteManagerKt.getPinSiteManager(context);
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        (this.presenter = new TopSitesPresenter()).setView(this);
        this.presenter.setModel(this);
    }
    
    @Override
    public android.view.View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final boolean hasNewsPortal = AppConfigWrapper.hasNewsPortal(this.getContext());
        android.view.View inflate;
        if (!hasNewsPortal && !AppConfigWrapper.hasEcommerceShoppingLink()) {
            inflate = layoutInflater.inflate(2131492959, viewGroup, false);
        }
        else {
            final android.view.View inflate2 = layoutInflater.inflate(2131492960, viewGroup, false);
            this.setupContentPortalView(inflate2);
            inflate = inflate2;
            if (hasNewsPortal) {
                inflate = inflate2;
                if (this.contentPanel != null) {
                    this.newsPresenter = new NewsPresenter(this);
                    this.contentPanel.setNewsListListener((ContentPortalView.NewsListListener)this.newsPresenter);
                    inflate = inflate2;
                }
            }
        }
        this.recyclerView = (RecyclerView)inflate.findViewById(2131296500);
        (this.btnMenu = (ThemedImageButton)inflate.findViewById(2131296351)).setOnClickListener(this.menuItemClickListener);
        this.btnMenu.setOnLongClickListener((View$OnLongClickListener)new _$$Lambda$HomeFragment$zGgcPvdy6jYmbj_Kvpr_qyx4AWM(this));
        (this.sessionManager = TabsSessionProvider.getOrThrow(this.getActivity())).register((SessionManager.Observer)this.observer);
        (this.tabCounter = (TabCounter)inflate.findViewById(2131296357)).setOnClickListener(this.menuItemClickListener);
        this.updateTabCounter();
        (this.fakeInput = (ThemedTextView)inflate.findViewById(2131296467)).setOnClickListener((View$OnClickListener)new _$$Lambda$HomeFragment$UIHSgCmoFPI7_45MO2Yzk1mIzNk(this));
        this.banner = (RecyclerView)inflate.findViewById(2131296304);
        this.bannerLayoutManager = new LinearLayoutManager(this.getContext(), 0, false);
        this.banner.setLayoutManager((RecyclerView.LayoutManager)this.bannerLayoutManager);
        new PagerSnapHelper() {
            private void sendSwipeTelemetry(int n, final int a) {
                final RecyclerView.Adapter adapter = HomeFragment.this.banner.getAdapter();
                if (adapter == null) {
                    return;
                }
                final int itemCount = adapter.getItemCount();
                if (n >= itemCount) {
                    n = itemCount - 1;
                }
                TelemetryWrapper.swipeBannerItem(a / Math.abs(a), n);
            }
            
            private void sendSwipeToIdTelemetry(final int n) {
                final android.view.View viewByPosition = HomeFragment.this.bannerLayoutManager.findViewByPosition(n);
                if (viewByPosition == null) {
                    return;
                }
                final String id = ((BannerViewHolder)HomeFragment.this.banner.getChildViewHolder(viewByPosition)).getId();
                if (id == null) {
                    return;
                }
                TelemetryWrapper.showBannerSwipe(id);
            }
            
            private void sendTelemetry(final int n, final int n2) {
                this.sendSwipeTelemetry(n, n2);
                this.sendSwipeToIdTelemetry(n);
            }
            
            @Override
            public int findTargetSnapPosition(final LayoutManager layoutManager, final int n, int targetSnapPosition) {
                targetSnapPosition = super.findTargetSnapPosition(layoutManager, n, targetSnapPosition);
                this.sendTelemetry(targetSnapPosition, n);
                return targetSnapPosition;
            }
        }.attachToRecyclerView(this.banner);
        final SwipeMotionLayout swipeMotionLayout = (SwipeMotionLayout)inflate.findViewById(2131296466);
        swipeMotionLayout.setOnSwipeListener(new GestureListenerAdapter());
        if (ThemeManager.shouldShowOnboarding(inflate.getContext())) {
            LayoutInflater.from(inflate.getContext()).inflate(2131492966, (ViewGroup)swipeMotionLayout);
            (this.themeOnboardingLayer = swipeMotionLayout.findViewById(2131296449)).setOnClickListener((View$OnClickListener)new _$$Lambda$HomeFragment$h_QZudQdj4vHU5Xy6NRh6Tq3k3Q(this));
        }
        this.homeScreenBackground = (HomeScreenBackground)inflate.findViewById(2131296465);
        this.downloadingIndicator = (LottieAnimationView)inflate.findViewById(2131296418);
        this.downloadIndicator = (ImageView)inflate.findViewById(2131296417);
        Inject.obtainDownloadIndicatorViewModel(this.getActivity()).getDownloadIndicatorObservable().observe(this.getViewLifecycleOwner(), new _$$Lambda$HomeFragment$aKgQg76STLRCTef8h8RsAwe8_q4(this));
        if (this.newsPresenter != null) {
            this.newsPresenter.setupNewsViewModel(this.getActivity());
        }
        return inflate;
    }
    
    @Override
    public void onDestroyView() {
        this.sessionManager.unregister((SessionManager.Observer)this.observer);
        doWithActivity(this.getActivity(), (DoWithThemeManager)new _$$Lambda$HomeFragment$56pfa0bwmcBYtyZ82ODqVQewkWU(this));
        this.bannerConfigViewModel.getConfig().removeObserver(this.bannerObserver);
        super.onDestroyView();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final Context context = this.getContext();
        if (context != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this.receiver);
        }
        this.timer.cancel();
        this.timer = null;
        this.stopAnimation();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Firebase_ready");
        this.receiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                HomeFragment.this.initBanner(context);
            }
        };
        final Context context = this.getContext();
        if (context != null) {
            LocalBroadcastManager.getInstance(context).registerReceiver(this.receiver, intentFilter);
        }
        this.updateTopSitesData();
        this.setupBannerTimer();
        this.setNightModeEnabled(Settings.getInstance((Context)this.getActivity()).isNightModeEnable());
        final android.view.View view = this.getView();
        if (view != null) {
            this.initFeatureSurveyViewIfNecessary(view);
        }
        this.playContentPortalAnimation();
        if (this.contentPanel != null) {
            this.contentPanel.onResume();
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.showCurrentBannerTelemetry();
        TelemetryWrapper.showHome();
    }
    
    @Override
    public void onUrlInputScreenVisible(final boolean b) {
        int visibility;
        if (b) {
            visibility = 4;
        }
        else {
            visibility = 0;
        }
        this.fakeInput.setVisibility(visibility);
    }
    
    @Override
    public void pinSite(final Site site, final Runnable runnable) {
        this.pinSiteManager.pin(site);
        runnable.run();
    }
    
    @Override
    public void removeSite(final Site site) {
        this.topSiteAdapter.setSites(this.presenter.getSites());
    }
    
    public void setNightModeEnabled(final boolean nightMode) {
        this.fakeInput.setNightMode(nightMode);
        this.btnMenu.setNightMode(nightMode);
        this.tabCounter.setNightMode(nightMode);
        this.homeScreenBackground.setNightMode(nightMode);
        for (int i = 0; i < this.recyclerView.getChildCount(); ++i) {
            ((ThemedTextView)this.recyclerView.getChildAt(i).findViewById(2131296685)).setNightMode(nightMode);
        }
        final FragmentActivity activity = this.getActivity();
        if (activity != null) {
            ViewUtils.updateStatusBarStyle(nightMode ^ true, activity.getWindow());
        }
    }
    
    @Override
    public void showSites(final List<Site> list) {
        Collections.sort((List<Object>)list, (Comparator<? super Object>)new TopSideComparator());
        if (this.topSiteAdapter == null) {
            this.topSiteAdapter = new TopSiteAdapter(list, (View$OnClickListener)this.clickListener, (View$OnLongClickListener)this.clickListener, this.pinSiteManager);
            this.recyclerView.setAdapter((RecyclerView.Adapter)this.topSiteAdapter);
        }
        else {
            this.recyclerView.setAdapter((RecyclerView.Adapter)this.topSiteAdapter);
            this.topSiteAdapter.setSites(list);
        }
    }
    
    @Override
    public void updateNews(final List<? extends NewsItem> newsContent) {
        this.contentPanel.setNewsContent(newsContent);
    }
    
    public void updateTopSitesData() {
        this.initDefaultSites();
        if (PreferenceManager.getDefaultSharedPreferences(this.getContext()).contains("top_sites_v2_complete")) {
            this.refreshTopSites();
        }
        else {
            new Thread(new MigrateHistoryRunnable(this.uiHandler, this.getContext())).start();
        }
    }
    
    private interface DoWithThemeManager
    {
        void doIt(final ThemeManager p0);
    }
    
    private class GestureListenerAdapter implements OnSwipeListener
    {
        @Override
        public boolean onDoubleTap() {
            if (Settings.getInstance((Context)HomeFragment.this.getActivity()).isNightModeEnable()) {
                return true;
            }
            doWithActivity(HomeFragment.this.getActivity(), (DoWithThemeManager)_$$Lambda$HomeFragment$GestureListenerAdapter$hfUYXF8Tlz2VOkjOAN_ypxg1zHg.INSTANCE);
            return true;
        }
        
        @Override
        public void onLongPress() {
            if (Settings.getInstance((Context)HomeFragment.this.getActivity()).isNightModeEnable()) {
                return;
            }
            doWithActivity(HomeFragment.this.getActivity(), (DoWithThemeManager)_$$Lambda$HomeFragment$GestureListenerAdapter$vTUnsrmQd6bO8rT32IiTUUjEHnA.INSTANCE);
        }
        
        @Override
        public void onSwipeDown() {
            if (HomeFragment.this.contentPanel == null) {
                HomeFragment.this.fakeInput.performClick();
            }
        }
        
        @Override
        public void onSwipeUp() {
            if (HomeFragment.this.contentPanel != null) {
                HomeFragment.this.showContentPortal();
            }
            else {
                HomeFragment.this.btnMenu.performClick();
            }
        }
    }
    
    private static class LoadConfigTask extends SimpleLoadUrlTask
    {
        private int index;
        private WeakReference<OnConfigLoadedListener> onConfigLoadedListenerRef;
        
        LoadConfigTask(final WeakReference<OnConfigLoadedListener> onConfigLoadedListenerRef, final int index) {
            this.onConfigLoadedListenerRef = onConfigLoadedListenerRef;
            this.index = index;
        }
        
        protected void onPostExecute(final String s) {
            final String replace = s.replace("\n", "").replace("\r", "");
            final OnConfigLoadedListener onConfigLoadedListener = this.onConfigLoadedListenerRef.get();
            if (onConfigLoadedListener != null) {
                onConfigLoadedListener.onConfigLoaded(replace, this.index);
            }
        }
        
        private interface OnConfigLoadedListener
        {
            void onConfigLoaded(final String p0, final int p1);
        }
    }
    
    private static class LoadRootConfigTask extends SimpleLoadUrlTask
    {
        private AtomicInteger countdown;
        WeakReference<OnRootConfigLoadedListener> onRootConfigLoadedListenerRef;
        private String userAgent;
        
        LoadRootConfigTask(final WeakReference<OnRootConfigLoadedListener> onRootConfigLoadedListenerRef) {
            this.onRootConfigLoadedListenerRef = onRootConfigLoadedListenerRef;
        }
        
        @Override
        protected String doInBackground(final String... array) {
            this.userAgent = array[1];
            return super.doInBackground(array);
        }
        
        protected void onPostExecute(final String s) {
            if (s == null || TextUtils.isEmpty((CharSequence)s)) {
                return;
            }
            final OnRootConfigLoadedListener onRootConfigLoadedListener = this.onRootConfigLoadedListenerRef.get();
            if (onRootConfigLoadedListener == null) {
                return;
            }
            try {
                final JSONArray jsonArray = new JSONArray(s);
                final int length = jsonArray.length();
                final String[] array = new String[length];
                this.countdown = new AtomicInteger(length);
                final _$$Lambda$HomeFragment$LoadRootConfigTask$N7ozq39_Jbe9oQNEx5wgAWpoCWU referent = new _$$Lambda$HomeFragment$LoadRootConfigTask$N7ozq39_Jbe9oQNEx5wgAWpoCWU(this, array, onRootConfigLoadedListener);
                for (int i = 0; i < length; ++i) {
                    new LoadConfigTask(new WeakReference<OnConfigLoadedListener>((OnConfigLoadedListener)referent), i).execute((Object[])new String[] { jsonArray.getString(i), this.userAgent, Integer.toString(10002) });
                }
            }
            catch (JSONException ex) {
                onRootConfigLoadedListener.onRootConfigLoaded(null);
            }
        }
        
        private interface OnRootConfigLoadedListener
        {
            void onRootConfigLoaded(final String[] p0);
        }
    }
    
    private static class MigrateHistoryRunnable implements Runnable
    {
        private WeakReference<Context> contextWeakReference;
        private WeakReference<Handler> handlerWeakReference;
        
        MigrateHistoryRunnable(final Handler referent, final Context referent2) {
            this.handlerWeakReference = new WeakReference<Handler>(referent);
            this.contextWeakReference = new WeakReference<Context>(referent2);
        }
        
        @Override
        public void run() {
            final Context context = this.contextWeakReference.get();
            if (context == null) {
                return;
            }
            final SupportSQLiteDatabase writableDatabase = HistoryDatabase.getInstance(context).getOpenHelper().getWritableDatabase();
            writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS browsing_history_legacy (_id INTEGER PRIMARY KEY NOT NULL,url TEXT NOT NULL,fav_icon BLOB);");
            final SupportSQLiteQueryBuilder builder = SupportSQLiteQueryBuilder.builder("browsing_history_legacy");
            builder.columns(new String[] { "_id", "url", "fav_icon" });
            final SupportSQLiteQuery create = builder.create();
            final File faviconFolder = FileUtils.getFaviconFolder(context);
            final ArrayList<String> list = new ArrayList<String>();
            final ArrayList<byte[]> list2 = new ArrayList<byte[]>();
            final Cursor query = writableDatabase.query(create);
            Handler handler2;
            final Handler handler = handler2 = null;
            while (true) {
                try {
                    try {
                        if (query.moveToFirst()) {
                            handler2 = handler;
                            parseCursorToSite(query, list, list2);
                        }
                        while (true) {
                            handler2 = handler;
                            if (!query.moveToNext()) {
                                break;
                            }
                            handler2 = handler;
                            parseCursorToSite(query, list, list2);
                        }
                        if (query != null) {
                            query.close();
                        }
                        handler2 = this.handlerWeakReference.get();
                        if (handler2 == null) {
                            return;
                        }
                        if (list2.size() == 0) {
                            scheduleRefresh(handler2);
                        }
                        else {
                            new FavIconUtils.SaveBitmapsTask(faviconFolder, list, list2, new UpdateHistoryWrapper((List)list, (WeakReference)this.handlerWeakReference), Bitmap$CompressFormat.PNG, 0).execute((Object[])new Void[0]);
                        }
                        writableDatabase.execSQL("DROP TABLE browsing_history_legacy");
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("top_sites_v2_complete", true).apply();
                        return;
                    }
                    finally {
                        if (query != null) {
                            if (handler2 != null) {
                                final Cursor cursor = query;
                                cursor.close();
                            }
                            else {
                                query.close();
                            }
                        }
                    }
                }
                catch (Throwable t) {}
                try {
                    final Cursor cursor = query;
                    cursor.close();
                    continue;
                }
                catch (Throwable t2) {}
                break;
            }
        }
    }
    
    private interface OnRemovedListener
    {
        void onRemoved(final Site p0);
    }
    
    private class SessionManagerObserver implements Observer
    {
        @Override
        public boolean handleExternalUrl(final String s) {
            return false;
        }
        
        @Override
        public void onFocusChanged(final Session session, final Factor factor) {
        }
        
        @Override
        public void onHttpAuthRequest(final TabViewClient.HttpAuthCallback httpAuthCallback, final String s, final String s2) {
        }
        
        @Override
        public void onSessionAdded(final Session session, final Bundle bundle) {
        }
        
        @Override
        public void onSessionCountChanged(final int n) {
            HomeFragment.this.updateTabCounter();
        }
        
        @Override
        public boolean onShowFileChooser(final TabViewEngineSession tabViewEngineSession, final ValueCallback<Uri[]> valueCallback, final WebChromeClient$FileChooserParams webChromeClient$FileChooserParams) {
            return false;
        }
        
        @Override
        public void updateFailingUrl(final String s, final boolean b) {
        }
    }
    
    private class SiteItemClickListener implements View$OnClickListener, View$OnLongClickListener
    {
        public void onClick(final android.view.View view) {
            final Site site = (Site)view.getTag();
            final FragmentActivity activity = HomeFragment.this.getActivity();
            if (site != null && activity instanceof FragmentListener) {
                ScreenNavigator.get(view.getContext()).showBrowserScreen(site.getUrl(), true, false);
                if (view.getParent() instanceof ViewGroup) {
                    final int indexOfChild = ((ViewGroup)view.getParent()).indexOfChild(view);
                    String title;
                    if (site.isDefault()) {
                        title = site.getTitle();
                    }
                    else {
                        title = "";
                    }
                    TelemetryWrapper.clickTopSiteOn(indexOfChild, title);
                }
            }
        }
        
        public boolean onLongClick(final android.view.View view) {
            final Site site = (Site)view.getTag();
            final boolean b = false;
            if (site == null) {
                return false;
            }
            final PopupMenu popupMenu = new PopupMenu(view.getContext(), view, 8);
            popupMenu.getMenuInflater().inflate(2131558405, popupMenu.getMenu());
            final MenuItem item = popupMenu.getMenu().findItem(2131296565);
            if (item != null) {
                boolean visible = b;
                if (HomeFragment.this.pinSiteManager.isEnabled()) {
                    visible = b;
                    if (!HomeFragment.this.pinSiteManager.isPinned(site)) {
                        visible = true;
                    }
                }
                item.setVisible(visible);
            }
            popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener)new _$$Lambda$HomeFragment$SiteItemClickListener$4IB3ndGZ_ufqnkodgjuU_qaCzyA(this, site));
            popupMenu.show();
            return true;
        }
    }
    
    private static class UpdateHistoryWrapper implements Consumer<List<String>>
    {
        private WeakReference<Handler> handlerWeakReference;
        private List<String> urls;
        
        private UpdateHistoryWrapper(final List<String> urls, final WeakReference<Handler> handlerWeakReference) {
            this.urls = urls;
            this.handlerWeakReference = handlerWeakReference;
        }
        
        public void accept(final List<String> list) {
            final _$$Lambda$HomeFragment$UpdateHistoryWrapper$VD7UQe2V_rm661eh_ychjYTSyLE $$Lambda$HomeFragment$UpdateHistoryWrapper$VD7UQe2V_rm661eh_ychjYTSyLE = new _$$Lambda$HomeFragment$UpdateHistoryWrapper$VD7UQe2V_rm661eh_ychjYTSyLE(this);
            for (int i = 0; i < list.size(); ++i) {
                if (i == list.size() - 1) {
                    BrowsingHistoryManager.updateHistory(null, this.urls.get(i), list.get(i), $$Lambda$HomeFragment$UpdateHistoryWrapper$VD7UQe2V_rm661eh_ychjYTSyLE);
                }
                else {
                    BrowsingHistoryManager.updateHistory(null, this.urls.get(i), list.get(i));
                }
            }
        }
    }
}
