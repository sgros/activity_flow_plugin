package org.mozilla.focus.home;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
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
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.Inject;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.locale.LocaleAwareFragment;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.tabs.TabCounter;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.OnSwipeListener;
import org.mozilla.focus.utils.OnSwipeListener$_CC;
import org.mozilla.focus.utils.RemoteConfigConstants;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SwipeMotionDetector;
import org.mozilla.focus.utils.TopSitesUtils;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.widget.FragmentListener$_CC;
import org.mozilla.focus.widget.SwipeMotionLayout;
import org.mozilla.httptask.SimpleLoadUrlTask;
import org.mozilla.icon.FavIconUtils;
import org.mozilla.rocket.banner.BannerAdapter;
import org.mozilla.rocket.banner.BannerConfigViewModel;
import org.mozilla.rocket.banner.BannerViewHolder;
import org.mozilla.rocket.content.ContentPortalView;
import org.mozilla.rocket.content.NewsPresenter;
import org.mozilla.rocket.content.NewsViewContract;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;
import org.mozilla.rocket.home.pinsite.PinSiteManager;
import org.mozilla.rocket.home.pinsite.PinSiteManagerKt;
import org.mozilla.rocket.nightmode.themed.ThemedImageButton;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabViewClient;
import org.mozilla.rocket.tabs.TabViewEngineSession;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.rocket.theme.ThemeManager;
import org.mozilla.rocket.util.LoggerWrapper;
import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.urlutils.UrlUtils;

public class HomeFragment extends LocaleAwareFragment implements TopSitesContract.Model, TopSitesContract.View, ScreenNavigator.HomeScreen, NewsViewContract {
   private static final String UNIT_SEPARATOR = Character.toString('\u001f');
   private ImageButton arrow1;
   private ImageButton arrow2;
   private RecyclerView banner;
   private BannerConfigViewModel bannerConfigViewModel;
   private LinearLayoutManager bannerLayoutManager;
   final Observer bannerObserver = new _$$Lambda$HomeFragment$Y6w2k3vFU3uhQsU1sAqbNcJ0r8A(this);
   private ThemedImageButton btnMenu;
   private HomeFragment.SiteItemClickListener clickListener = new HomeFragment.SiteItemClickListener();
   private String[] configArray;
   private ContentPortalView contentPanel;
   private ImageView downloadIndicator;
   private LottieAnimationView downloadingIndicator;
   private ThemedTextView fakeInput;
   private HomeScreenBackground homeScreenBackground;
   private QueryHandler.AsyncUpdateListener mTopSiteUpdateListener = new _$$Lambda$HomeFragment$F55KXmKZ14h6rPSlCB5EojJPzKw(this);
   private QueryHandler.AsyncQueryListener mTopSitesQueryListener = new _$$Lambda$HomeFragment$Ht2Yxo_dd5eyf5eF_hvmXPMe380(this);
   private OnClickListener menuItemClickListener = new OnClickListener() {
      private void dispatchOnClick(View var1, FragmentListener var2) {
         int var3 = var1.getId();
         if (var3 != 2131296351) {
            if (var3 == 2131296357) {
               var2.onNotified(HomeFragment.this, FragmentListener.TYPE.SHOW_TAB_TRAY, (Object)null);
               TelemetryWrapper.showTabTrayHome();
            }
         } else {
            var2.onNotified(HomeFragment.this, FragmentListener.TYPE.SHOW_MENU, (Object)null);
            TelemetryWrapper.showMenuHome();
         }

      }

      public void onClick(View var1) {
         FragmentActivity var2 = HomeFragment.this.getActivity();
         if (var2 instanceof FragmentListener) {
            this.dispatchOnClick(var1, (FragmentListener)var2);
         }

      }
   };
   private NewsPresenter newsPresenter = null;
   private final HomeFragment.SessionManagerObserver observer = new HomeFragment.SessionManagerObserver();
   private HomeFragment.LoadRootConfigTask.OnRootConfigLoadedListener onRootConfigLoadedListener;
   private JSONArray orginalDefaultSites = null;
   private PinSiteManager pinSiteManager;
   private TopSitesContract.Presenter presenter;
   private BroadcastReceiver receiver;
   private RecyclerView recyclerView;
   private SessionManager sessionManager;
   private TabCounter tabCounter;
   private View themeOnboardingLayer;
   private Timer timer;
   private TopSiteAdapter topSiteAdapter;
   private Handler uiHandler = new Handler(Looper.getMainLooper()) {
      public void handleMessage(Message var1) {
         if (var1.what == 8269) {
            HomeFragment.this.refreshTopSites();
         }

      }
   };

   // $FF: synthetic method
   static void access$1200(Cursor var0, List var1, List var2) {
      parseCursorToSite(var0, var1, var2);
   }

   private void constructTopSiteList(List var1) {
      this.initDefaultSitesFromJSONArray(this.orginalDefaultSites);
      ArrayList var2 = new ArrayList(this.presenter.getSites());
      this.mergeHistorySiteToTopSites(var1, var2);
      this.mergePinSiteToTopSites(this.pinSiteManager.getPinSites(), var2);
      Collections.sort(var2, new TopSideComparator());
      Object var3 = var2;
      if (var2.size() > 8) {
         this.removeDefaultSites(var2.subList(8, var2.size()));
         var3 = var2.subList(0, 8);
      }

      this.presenter.setSites((List)var3);
      this.presenter.populateSites();
   }

   public static HomeFragment create() {
      return new HomeFragment();
   }

   private void deleteCache(Context var1) {
      try {
         WeakReference var5 = new WeakReference(var1);
         FileUtils.GetCache var4 = new FileUtils.GetCache(var5);
         File var3 = new File(var4.get(), "CURRENT_BANNER_CONFIG");
         FileUtils.DeleteFileRunnable var2 = new FileUtils.DeleteFileRunnable(var3);
         ThreadUtils.postToBackgroundThread((Runnable)var2);
      } catch (InterruptedException | ExecutionException var6) {
         var6.printStackTrace();
         LoggerWrapper.throwOrWarn("HomeFragment", "Failed to open cache directory when deleting banner cache");
      }

   }

   private static void doWithActivity(Activity var0, HomeFragment.DoWithThemeManager var1) {
      if (var0 != null && !var0.isFinishing() && !var0.isDestroyed()) {
         if (var0 instanceof ThemeManager.ThemeHost) {
            var1.doIt(((ThemeManager.ThemeHost)var0).getThemeManager());
         }

      }
   }

   private void initBanner(Context var1) {
      try {
         WeakReference var4 = new WeakReference(var1);
         FileUtils.GetCache var3 = new FileUtils.GetCache(var4);
         FileUtils.ReadStringFromFileTask var2 = new FileUtils.ReadStringFromFileTask(var3.get(), "CURRENT_BANNER_CONFIG", this.bannerConfigViewModel.getConfig(), _$$Lambda$HomeFragment$3IVmlahMzFufutXTYk3NkFbUg0I.INSTANCE);
         var2.execute(new Void[0]);
      } catch (InterruptedException | ExecutionException var5) {
         var5.printStackTrace();
         LoggerWrapper.throwOrWarn("HomeFragment", "Failed to open Cache directory when reading cached banner config");
      }

      String var6 = AppConfigWrapper.getBannerRootConfig(var1);
      if (TextUtils.isEmpty(var6)) {
         this.deleteCache(var1);
         this.banner.setAdapter((RecyclerView.Adapter)null);
         this.showBanner(false);
      } else {
         this.onRootConfigLoadedListener = new _$$Lambda$HomeFragment$E3qF6zcvBwKUOoV8AnaHHon_CA4(this, var1);
         (new HomeFragment.LoadRootConfigTask(new WeakReference(this.onRootConfigLoadedListener))).execute(new String[]{var6, WebViewProvider.getUserAgentString(this.getActivity()), Integer.toString(10002)});
      }

   }

   private void initDefaultSites() {
      String var1 = Inject.getDefaultTopSites(this.getContext());
      if (var1 == null) {
         this.orginalDefaultSites = TopSitesUtils.getDefaultSitesJsonArrayFromAssets(this.getContext());
      } else {
         try {
            JSONArray var2 = new JSONArray(var1);
            this.orginalDefaultSites = var2;
         } catch (JSONException var3) {
            var3.printStackTrace();
            return;
         }
      }

      this.initDefaultSitesFromJSONArray(this.orginalDefaultSites);
   }

   private void initDefaultSitesFromJSONArray(JSONArray var1) {
      List var2 = TopSitesUtils.paresJsonToList(this.getContext(), var1);
      this.presenter.setSites(var2);
   }

   private void initFeatureSurveyViewIfNecessary(View var1) {
      RemoteConfigConstants.SURVEY var2 = RemoteConfigConstants.SURVEY.Companion.parseLong(AppConfigWrapper.getFeatureSurvey(this.getContext()));
      ImageView var3 = (ImageView)var1.findViewById(2131296470);
      Settings.EventHistory var4 = Settings.getInstance(this.getContext()).getEventHistory();
      if (var2 == RemoteConfigConstants.SURVEY.WIFI_FINDING && !var4.contains("feature_survey_wifi_finding")) {
         var3.setImageResource(2131230865);
         var3.setVisibility(0);
         if (this.getContext() != null) {
            var3.setOnClickListener(new FeatureSurveyViewHelper(this.getContext(), var2));
         }
      } else if (var2 == RemoteConfigConstants.SURVEY.VPN && !var4.contains("feature_survey_vpn")) {
         var3.setImageResource(2131230984);
         var3.setVisibility(0);
         if (this.getContext() != null) {
            var3.setOnClickListener(new FeatureSurveyViewHelper(this.getContext(), var2));
         }
      } else if (var2 == RemoteConfigConstants.SURVEY.VPN_RECOMMENDER && !var4.contains("vpn_recommender_ignore")) {
         Object var5 = null;
         String var6 = AppConfigWrapper.getVpnRecommenderPackage(this.getActivity());

         PackageInfo var10;
         label48: {
            NameNotFoundException var10000;
            label66: {
               FragmentActivity var7;
               boolean var10001;
               try {
                  var7 = this.getActivity();
               } catch (NameNotFoundException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label66;
               }

               var10 = (PackageInfo)var5;
               if (var7 == null) {
                  break label48;
               }

               try {
                  var10 = var7.getPackageManager().getPackageInfo(var6, 0);
                  break label48;
               } catch (NameNotFoundException var8) {
                  var10000 = var8;
                  var10001 = false;
               }
            }

            NameNotFoundException var11 = var10000;
            var11.printStackTrace();
            var10 = (PackageInfo)var5;
         }

         if (var10 != null) {
            var4.add("vpn_app_was_downloaded");
            var3.setImageResource(2131230984);
            var3.setVisibility(0);
            var3.setOnClickListener(new _$$Lambda$HomeFragment$y9mK7LxbJRaTHVOTIMr4dniYppM(this, var6));
            TelemetryWrapper.showVpnRecommender(true);
         } else if (var4.contains("vpn_app_was_downloaded")) {
            var3.setVisibility(8);
         } else {
            if (this.getContext() != null) {
               var3.setOnClickListener(new FeatureSurveyViewHelper(this.getContext(), var2));
               var3.setVisibility(0);
            }

            TelemetryWrapper.showVpnRecommender(false);
         }
      } else {
         var3.setVisibility(8);
      }

   }

   private boolean isTabRestoredComplete() {
      boolean var1;
      if (this.getActivity() instanceof MainActivity && ((MainActivity)this.getActivity()).isTabRestoredComplete()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   public static String[] lambda$3IVmlahMzFufutXTYk3NkFbUg0I(String var0) {
      return stringToStringArray(var0);
   }

   // $FF: synthetic method
   public static void lambda$Y6w2k3vFU3uhQsU1sAqbNcJ0r8A(HomeFragment var0, String[] var1) {
      var0.setUpBannerFromConfig(var1);
   }

   // $FF: synthetic method
   public static void lambda$initBanner$0(HomeFragment var0, Context var1, String[] var2) {
      var0.writeToCache(var1, var2);
      var0.bannerConfigViewModel.getConfig().setValue(var2);
      var0.onRootConfigLoadedListener = null;
   }

   // $FF: synthetic method
   public static void lambda$initFeatureSurveyViewIfNecessary$12(HomeFragment var0, String var1, View var2) {
      var0.startActivity(var0.getActivity().getPackageManager().getLaunchIntentForPackage(var1));
      TelemetryWrapper.clickVpnRecommender(true);
   }

   // $FF: synthetic method
   static void lambda$mergeHistorySiteToTopSites$10(Site var0, Site var1) {
      var0.setViewCount(var0.getViewCount() + var1.getViewCount());
   }

   // $FF: synthetic method
   static void lambda$mergePinSiteToTopSites$11(Site var0) {
   }

   // $FF: synthetic method
   public static void lambda$new$8(HomeFragment var0, List var1) {
      ArrayList var2 = new ArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Object var3 = var4.next();
         if (var3 instanceof Site) {
            var2.add((Site)var3);
         }
      }

      var0.constructTopSiteList(var2);
   }

   // $FF: synthetic method
   public static void lambda$new$9(HomeFragment var0, int var1) {
      var0.refreshTopSites();
   }

   // $FF: synthetic method
   public static void lambda$onActivityCreated$6(HomeFragment var0, ThemeManager var1) {
      var1.subscribeThemeChange(var0.homeScreenBackground);
   }

   // $FF: synthetic method
   public static boolean lambda$onCreateView$2(HomeFragment var0, View var1) {
      FragmentListener$_CC.notifyParent(var0, FragmentListener.TYPE.SHOW_DOWNLOAD_PANEL, (Object)null);
      TelemetryWrapper.longPressDownloadIndicator();
      return false;
   }

   // $FF: synthetic method
   public static void lambda$onCreateView$3(HomeFragment var0, View var1) {
      FragmentActivity var2 = var0.getActivity();
      if (var2 instanceof FragmentListener) {
         ((FragmentListener)var2).onNotified(var0, FragmentListener.TYPE.SHOW_URL_INPUT, (Object)null);
      }

      TelemetryWrapper.showSearchBarHome();
   }

   // $FF: synthetic method
   public static void lambda$onCreateView$4(HomeFragment var0, View var1) {
      if (var0.themeOnboardingLayer != null) {
         ThemeManager.dismissOnboarding(var0.themeOnboardingLayer.getContext().getApplicationContext());
         ((ViewGroup)var0.themeOnboardingLayer.getParent()).removeView(var0.themeOnboardingLayer);
         var0.themeOnboardingLayer = null;
      }

   }

   // $FF: synthetic method
   public static void lambda$onCreateView$5(HomeFragment var0, DownloadIndicatorViewModel.Status var1) {
      if (var1 == DownloadIndicatorViewModel.Status.DOWNLOADING) {
         var0.downloadIndicator.setVisibility(8);
         var0.downloadingIndicator.setVisibility(0);
         if (!var0.downloadingIndicator.isAnimating()) {
            var0.downloadingIndicator.playAnimation();
         }
      } else if (var1 == DownloadIndicatorViewModel.Status.UNREAD) {
         var0.downloadingIndicator.setVisibility(8);
         var0.downloadIndicator.setVisibility(0);
         var0.downloadIndicator.setImageResource(2131230948);
      } else if (var1 == DownloadIndicatorViewModel.Status.WARNING) {
         var0.downloadingIndicator.setVisibility(8);
         var0.downloadIndicator.setVisibility(0);
         var0.downloadIndicator.setImageResource(2131230949);
      } else {
         var0.downloadingIndicator.setVisibility(8);
         var0.downloadIndicator.setVisibility(8);
      }

   }

   // $FF: synthetic method
   public static void lambda$onDestroyView$7(HomeFragment var0, ThemeManager var1) {
      var1.unsubscribeThemeChange(var0.homeScreenBackground);
   }

   // $FF: synthetic method
   public static void lambda$setUpBannerFromConfig$1(HomeFragment var0, String var1) {
      FragmentListener$_CC.notifyParent(var0, FragmentListener.TYPE.OPEN_URL_IN_NEW_TAB, var1);
   }

   private void mergeHistorySiteToTopSites(List var1, List var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Site var4 = (Site)var3.next();
         this.removeDuplicatedSites(var1, var4, new _$$Lambda$HomeFragment$iYsX7OQrdrr_G4JZR_T6E_agQj8(var4));
      }

      var2.addAll(var1);
   }

   private void mergePinSiteToTopSites(List var1, List var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         this.removeDuplicatedSites(var2, (Site)var3.next(), _$$Lambda$HomeFragment$5CWSG_uCygmRouU50LQwUcKTRjw.INSTANCE);
      }

      var2.addAll(var1);
   }

   private static void parseCursorToSite(Cursor var0, List var1, List var2) {
      String var3 = var0.getString(var0.getColumnIndex("url"));
      byte[] var4 = var0.getBlob(var0.getColumnIndex("fav_icon"));
      var1.add(var3);
      var2.add(var4);
   }

   private void playContentPortalAnimation() {
      Animation var1 = AnimationUtils.loadAnimation(this.getActivity(), 2130771981);
      Animation var2 = AnimationUtils.loadAnimation(this.getActivity(), 2130771980);
      Inject.startAnimation(this.arrow1, var1);
      Inject.startAnimation(this.arrow2, var2);
   }

   private void refreshTopSites() {
      BrowsingHistoryManager.getInstance().queryTopSites(8, 6, this.mTopSitesQueryListener);
   }

   private void removeDefaultSites(List var1) {
      int var2 = 0;

      boolean var3;
      for(var3 = false; var2 < var1.size(); ++var2) {
         Site var4 = (Site)var1.get(var2);
         if (var4.getId() < 0L) {
            this.removeDefaultSites(var4);
            var3 = true;
         }
      }

      if (var3) {
         TopSitesUtils.saveDefaultSites(this.getContext(), this.orginalDefaultSites);
      }

   }

   private void removeDefaultSites(Site param1) {
      // $FF: Couldn't be decompiled
   }

   private void removeDuplicatedSites(List var1, Site var2, HomeFragment.OnRemovedListener var3) {
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Site var5 = (Site)var4.next();
         if (UrlUtils.urlsMatchExceptForTrailingSlash(var5.getUrl(), var2.getUrl())) {
            var4.remove();
            var3.onRemoved(var5);
         }
      }

   }

   private static void scheduleRefresh(Handler var0) {
      var0.dispatchMessage(var0.obtainMessage(8269));
   }

   private void setUpBannerFromConfig(String[] var1) {
      if (!Arrays.equals(this.configArray, var1)) {
         boolean var2;
         if (this.configArray != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.configArray = var1;
         if (var1 != null && var1.length != 0) {
            JSONException var10000;
            label37: {
               boolean var10001;
               BannerAdapter var3;
               try {
                  _$$Lambda$HomeFragment$DUsg3hhcZ8pxzytL2HDqVpMbzjY var4 = new _$$Lambda$HomeFragment$DUsg3hhcZ8pxzytL2HDqVpMbzjY(this);
                  var3 = new BannerAdapter(var1, var4);
                  this.banner.setAdapter(var3);
                  this.showBanner(true);
               } catch (JSONException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label37;
               }

               if (var2) {
                  try {
                     TelemetryWrapper.showBannerNew(var3.getFirstDAOId());
                     return;
                  } catch (JSONException var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               } else {
                  try {
                     TelemetryWrapper.showBannerUpdate(var3.getFirstDAOId());
                     return;
                  } catch (JSONException var6) {
                     var10000 = var6;
                     var10001 = false;
                  }
               }
            }

            JSONException var8 = var10000;
            StringBuilder var9 = new StringBuilder();
            var9.append("Invalid Config: ");
            var9.append(var8.getMessage());
            LoggerWrapper.throwOrWarn("HomeFragment", var9.toString());
         } else {
            this.showBanner(false);
         }
      }
   }

   private void setupBannerTimer() {
      this.timer = new Timer();
      this.timer.scheduleAtFixedRate(new TimerTask() {
         public void run() {
            RecyclerView.Adapter var1 = HomeFragment.this.banner.getAdapter();
            if (var1 == null) {
               this.cancel();
            } else {
               int var2 = HomeFragment.this.bannerLayoutManager.findFirstVisibleItemPosition();
               int var3 = var1.getItemCount();
               HomeFragment.this.banner.smoothScrollToPosition((var2 + 1) % var3);
            }
         }
      }, 10000L, 10000L);
   }

   private void setupContentPortalView(View var1) {
      this.arrow1 = (ImageButton)var1.findViewById(2131296296);
      this.arrow2 = (ImageButton)var1.findViewById(2131296297);
      this.contentPanel = (ContentPortalView)var1.findViewById(2131296378);
      var1 = var1.findViewById(2131296298);
      if (var1 != null) {
         var1.setOnTouchListener(new SwipeMotionDetector(this.getContext(), new OnSwipeListener() {
            // $FF: synthetic method
            public boolean onDoubleTap() {
               return OnSwipeListener$_CC.$default$onDoubleTap(this);
            }

            // $FF: synthetic method
            public void onLongPress() {
               OnSwipeListener$_CC.$default$onLongPress(this);
            }

            public boolean onSingleTapConfirmed() {
               HomeFragment.this.showContentPortal();
               return true;
            }

            // $FF: synthetic method
            public void onSwipeDown() {
               OnSwipeListener$_CC.$default$onSwipeDown(this);
            }

            // $FF: synthetic method
            public void onSwipeLeft() {
               OnSwipeListener$_CC.$default$onSwipeLeft(this);
            }

            // $FF: synthetic method
            public void onSwipeRight() {
               OnSwipeListener$_CC.$default$onSwipeRight(this);
            }

            public void onSwipeUp() {
               HomeFragment.this.showContentPortal();
            }
         }));
      }

   }

   private void showBanner(boolean var1) {
      if (var1) {
         this.banner.setVisibility(0);
      } else {
         this.banner.setVisibility(8);
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

   private void showCurrentBannerTelemetry() {
      if (this.banner.getVisibility() == 0 && this.bannerLayoutManager != null) {
         View var1 = this.banner.getChildAt(0);
         if (var1 != null) {
            String var2 = ((BannerViewHolder)this.banner.getChildViewHolder(var1)).getId();
            if (var2 != null) {
               TelemetryWrapper.showBannerReturn(var2);
            }
         }
      }
   }

   private void stopAnimation() {
      if (this.arrow1 != null && this.arrow1.getAnimation() != null) {
         this.arrow1.getAnimation().cancel();
      }

      if (this.arrow2 != null && this.arrow2.getAnimation() != null) {
         this.arrow2.getAnimation().cancel();
      }

   }

   private String stringArrayToString(String[] var1) {
      return TextUtils.join(UNIT_SEPARATOR, var1);
   }

   private static String[] stringToStringArray(String var0) {
      return TextUtils.isEmpty(var0) ? new String[0] : var0.split(UNIT_SEPARATOR);
   }

   private void updateTabCounter() {
      int var1;
      if (this.sessionManager != null) {
         var1 = this.sessionManager.getTabsCount();
      } else {
         var1 = 0;
      }

      if (this.isTabRestoredComplete()) {
         this.tabCounter.setCount(var1);
      }

      if (var1 == 0) {
         this.tabCounter.setEnabled(false);
         this.tabCounter.setAlpha(0.3F);
      } else {
         this.tabCounter.setEnabled(true);
         this.tabCounter.setAlpha(1.0F);
      }

   }

   private void writeToCache(Context var1, String[] var2) {
      try {
         WeakReference var6 = new WeakReference(var1);
         FileUtils.GetCache var5 = new FileUtils.GetCache(var6);
         File var4 = new File(var5.get(), "CURRENT_BANNER_CONFIG");
         FileUtils.WriteStringToFileRunnable var3 = new FileUtils.WriteStringToFileRunnable(var4, this.stringArrayToString(var2));
         ThreadUtils.postToBackgroundThread((Runnable)var3);
      } catch (InterruptedException | ExecutionException var7) {
         var7.printStackTrace();
         LoggerWrapper.throwOrWarn("HomeFragment", "Failed to open cache directory when writing banner config to cache");
      }

   }

   public void applyLocale() {
      this.fakeInput.setText(2131755426);
   }

   public Fragment getFragment() {
      return this;
   }

   public boolean hideContentPortal() {
      return this.contentPanel != null ? this.contentPanel.hide() : false;
   }

   public void onActivityCreated(Bundle var1) {
      super.onActivityCreated(var1);
      doWithActivity(this.getActivity(), new _$$Lambda$HomeFragment$tUWOs_ryJciFQgYEqFIG0QAoSRs(this));
      Context var2 = this.getContext();
      this.bannerConfigViewModel = (BannerConfigViewModel)ViewModelProviders.of((Fragment)this).get(BannerConfigViewModel.class);
      this.bannerConfigViewModel.getConfig().observe(this, this.bannerObserver);
      this.initBanner(var2);
      if (var2 != null) {
         this.pinSiteManager = PinSiteManagerKt.getPinSiteManager(var2);
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.presenter = new TopSitesPresenter();
      this.presenter.setView(this);
      this.presenter.setModel(this);
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      boolean var4 = AppConfigWrapper.hasNewsPortal(this.getContext());
      View var5;
      if (!var4 && !AppConfigWrapper.hasEcommerceShoppingLink()) {
         var5 = var1.inflate(2131492959, var2, false);
      } else {
         View var6 = var1.inflate(2131492960, var2, false);
         this.setupContentPortalView(var6);
         var5 = var6;
         if (var4) {
            var5 = var6;
            if (this.contentPanel != null) {
               this.newsPresenter = new NewsPresenter(this);
               this.contentPanel.setNewsListListener(this.newsPresenter);
               var5 = var6;
            }
         }
      }

      this.recyclerView = (RecyclerView)var5.findViewById(2131296500);
      this.btnMenu = (ThemedImageButton)var5.findViewById(2131296351);
      this.btnMenu.setOnClickListener(this.menuItemClickListener);
      this.btnMenu.setOnLongClickListener(new _$$Lambda$HomeFragment$zGgcPvdy6jYmbj_Kvpr_qyx4AWM(this));
      this.sessionManager = TabsSessionProvider.getOrThrow(this.getActivity());
      this.sessionManager.register((SessionManager.Observer)this.observer);
      this.tabCounter = (TabCounter)var5.findViewById(2131296357);
      this.tabCounter.setOnClickListener(this.menuItemClickListener);
      this.updateTabCounter();
      this.fakeInput = (ThemedTextView)var5.findViewById(2131296467);
      this.fakeInput.setOnClickListener(new _$$Lambda$HomeFragment$UIHSgCmoFPI7_45MO2Yzk1mIzNk(this));
      this.banner = (RecyclerView)var5.findViewById(2131296304);
      this.bannerLayoutManager = new LinearLayoutManager(this.getContext(), 0, false);
      this.banner.setLayoutManager(this.bannerLayoutManager);
      (new PagerSnapHelper() {
         private void sendSwipeTelemetry(int var1, int var2) {
            RecyclerView.Adapter var3 = HomeFragment.this.banner.getAdapter();
            if (var3 != null) {
               int var4 = var3.getItemCount();
               if (var1 >= var4) {
                  var1 = var4 - 1;
               }

               TelemetryWrapper.swipeBannerItem(var2 / Math.abs(var2), var1);
            }
         }

         private void sendSwipeToIdTelemetry(int var1) {
            View var2 = HomeFragment.this.bannerLayoutManager.findViewByPosition(var1);
            if (var2 != null) {
               String var3 = ((BannerViewHolder)HomeFragment.this.banner.getChildViewHolder(var2)).getId();
               if (var3 != null) {
                  TelemetryWrapper.showBannerSwipe(var3);
               }
            }
         }

         private void sendTelemetry(int var1, int var2) {
            this.sendSwipeTelemetry(var1, var2);
            this.sendSwipeToIdTelemetry(var1);
         }

         public int findTargetSnapPosition(RecyclerView.LayoutManager var1, int var2, int var3) {
            var3 = super.findTargetSnapPosition(var1, var2, var3);
            this.sendTelemetry(var3, var2);
            return var3;
         }
      }).attachToRecyclerView(this.banner);
      SwipeMotionLayout var7 = (SwipeMotionLayout)var5.findViewById(2131296466);
      var7.setOnSwipeListener(new HomeFragment.GestureListenerAdapter());
      if (ThemeManager.shouldShowOnboarding(var5.getContext())) {
         LayoutInflater.from(var5.getContext()).inflate(2131492966, var7);
         this.themeOnboardingLayer = var7.findViewById(2131296449);
         this.themeOnboardingLayer.setOnClickListener(new _$$Lambda$HomeFragment$h_QZudQdj4vHU5Xy6NRh6Tq3k3Q(this));
      }

      this.homeScreenBackground = (HomeScreenBackground)var5.findViewById(2131296465);
      this.downloadingIndicator = (LottieAnimationView)var5.findViewById(2131296418);
      this.downloadIndicator = (ImageView)var5.findViewById(2131296417);
      Inject.obtainDownloadIndicatorViewModel(this.getActivity()).getDownloadIndicatorObservable().observe(this.getViewLifecycleOwner(), new _$$Lambda$HomeFragment$aKgQg76STLRCTef8h8RsAwe8_q4(this));
      if (this.newsPresenter != null) {
         this.newsPresenter.setupNewsViewModel(this.getActivity());
      }

      return var5;
   }

   public void onDestroyView() {
      this.sessionManager.unregister((SessionManager.Observer)this.observer);
      doWithActivity(this.getActivity(), new _$$Lambda$HomeFragment$56pfa0bwmcBYtyZ82ODqVQewkWU(this));
      this.bannerConfigViewModel.getConfig().removeObserver(this.bannerObserver);
      super.onDestroyView();
   }

   public void onPause() {
      super.onPause();
      Context var1 = this.getContext();
      if (var1 != null) {
         LocalBroadcastManager.getInstance(var1).unregisterReceiver(this.receiver);
      }

      this.timer.cancel();
      this.timer = null;
      this.stopAnimation();
   }

   public void onResume() {
      super.onResume();
      IntentFilter var1 = new IntentFilter();
      var1.addAction("Firebase_ready");
      this.receiver = new BroadcastReceiver() {
         public void onReceive(Context var1, Intent var2) {
            HomeFragment.this.initBanner(var1);
         }
      };
      Context var2 = this.getContext();
      if (var2 != null) {
         LocalBroadcastManager.getInstance(var2).registerReceiver(this.receiver, var1);
      }

      this.updateTopSitesData();
      this.setupBannerTimer();
      this.setNightModeEnabled(Settings.getInstance(this.getActivity()).isNightModeEnable());
      View var3 = this.getView();
      if (var3 != null) {
         this.initFeatureSurveyViewIfNecessary(var3);
      }

      this.playContentPortalAnimation();
      if (this.contentPanel != null) {
         this.contentPanel.onResume();
      }

   }

   public void onStart() {
      super.onStart();
      this.showCurrentBannerTelemetry();
      TelemetryWrapper.showHome();
   }

   public void onUrlInputScreenVisible(boolean var1) {
      byte var2;
      if (var1) {
         var2 = 4;
      } else {
         var2 = 0;
      }

      this.fakeInput.setVisibility(var2);
   }

   public void pinSite(Site var1, Runnable var2) {
      this.pinSiteManager.pin(var1);
      var2.run();
   }

   public void removeSite(Site var1) {
      this.topSiteAdapter.setSites(this.presenter.getSites());
   }

   public void setNightModeEnabled(boolean var1) {
      this.fakeInput.setNightMode(var1);
      this.btnMenu.setNightMode(var1);
      this.tabCounter.setNightMode(var1);
      this.homeScreenBackground.setNightMode(var1);

      for(int var2 = 0; var2 < this.recyclerView.getChildCount(); ++var2) {
         ((ThemedTextView)this.recyclerView.getChildAt(var2).findViewById(2131296685)).setNightMode(var1);
      }

      FragmentActivity var3 = this.getActivity();
      if (var3 != null) {
         ViewUtils.updateStatusBarStyle(var1 ^ true, var3.getWindow());
      }

   }

   public void showSites(List var1) {
      Collections.sort(var1, new TopSideComparator());
      if (this.topSiteAdapter == null) {
         this.topSiteAdapter = new TopSiteAdapter(var1, this.clickListener, this.clickListener, this.pinSiteManager);
         this.recyclerView.setAdapter(this.topSiteAdapter);
      } else {
         this.recyclerView.setAdapter(this.topSiteAdapter);
         this.topSiteAdapter.setSites(var1);
      }

   }

   public void updateNews(List var1) {
      this.contentPanel.setNewsContent(var1);
   }

   public void updateTopSitesData() {
      this.initDefaultSites();
      if (PreferenceManager.getDefaultSharedPreferences(this.getContext()).contains("top_sites_v2_complete")) {
         this.refreshTopSites();
      } else {
         (new Thread(new HomeFragment.MigrateHistoryRunnable(this.uiHandler, this.getContext()))).start();
      }

   }

   private interface DoWithThemeManager {
      void doIt(ThemeManager var1);
   }

   private class GestureListenerAdapter implements OnSwipeListener {
      private GestureListenerAdapter() {
      }

      // $FF: synthetic method
      GestureListenerAdapter(Object var2) {
         this();
      }

      // $FF: synthetic method
      static void lambda$onDoubleTap$1(ThemeManager var0) {
         TelemetryWrapper.changeThemeTo(var0.toggleNextTheme().name());
      }

      // $FF: synthetic method
      static void lambda$onLongPress$0(ThemeManager var0) {
         var0.resetDefaultTheme();
         TelemetryWrapper.resetThemeToDefault();
      }

      public boolean onDoubleTap() {
         if (Settings.getInstance(HomeFragment.this.getActivity()).isNightModeEnable()) {
            return true;
         } else {
            HomeFragment.doWithActivity(HomeFragment.this.getActivity(), _$$Lambda$HomeFragment$GestureListenerAdapter$hfUYXF8Tlz2VOkjOAN_ypxg1zHg.INSTANCE);
            return true;
         }
      }

      public void onLongPress() {
         if (!Settings.getInstance(HomeFragment.this.getActivity()).isNightModeEnable()) {
            HomeFragment.doWithActivity(HomeFragment.this.getActivity(), _$$Lambda$HomeFragment$GestureListenerAdapter$vTUnsrmQd6bO8rT32IiTUUjEHnA.INSTANCE);
         }
      }

      // $FF: synthetic method
      public boolean onSingleTapConfirmed() {
         return OnSwipeListener$_CC.$default$onSingleTapConfirmed(this);
      }

      public void onSwipeDown() {
         if (HomeFragment.this.contentPanel == null) {
            HomeFragment.this.fakeInput.performClick();
         }

      }

      // $FF: synthetic method
      public void onSwipeLeft() {
         OnSwipeListener$_CC.$default$onSwipeLeft(this);
      }

      // $FF: synthetic method
      public void onSwipeRight() {
         OnSwipeListener$_CC.$default$onSwipeRight(this);
      }

      public void onSwipeUp() {
         if (HomeFragment.this.contentPanel != null) {
            HomeFragment.this.showContentPortal();
         } else {
            HomeFragment.this.btnMenu.performClick();
         }

      }
   }

   private static class LoadConfigTask extends SimpleLoadUrlTask {
      private int index;
      private WeakReference onConfigLoadedListenerRef;

      LoadConfigTask(WeakReference var1, int var2) {
         this.onConfigLoadedListenerRef = var1;
         this.index = var2;
      }

      protected void onPostExecute(String var1) {
         String var2 = var1.replace("\n", "").replace("\r", "");
         HomeFragment.LoadConfigTask.OnConfigLoadedListener var3 = (HomeFragment.LoadConfigTask.OnConfigLoadedListener)this.onConfigLoadedListenerRef.get();
         if (var3 != null) {
            var3.onConfigLoaded(var2, this.index);
         }

      }

      private interface OnConfigLoadedListener {
         void onConfigLoaded(String var1, int var2);
      }
   }

   private static class LoadRootConfigTask extends SimpleLoadUrlTask {
      private AtomicInteger countdown;
      WeakReference onRootConfigLoadedListenerRef;
      private String userAgent;

      LoadRootConfigTask(WeakReference var1) {
         this.onRootConfigLoadedListenerRef = var1;
      }

      // $FF: synthetic method
      public static void lambda$onPostExecute$0(HomeFragment.LoadRootConfigTask var0, String[] var1, HomeFragment.LoadRootConfigTask.OnRootConfigLoadedListener var2, String var3, int var4) {
         var1[var4] = var3;
         if (var0.countdown.decrementAndGet() == 0) {
            var2.onRootConfigLoaded(var1);
         }

      }

      protected String doInBackground(String... var1) {
         this.userAgent = var1[1];
         return super.doInBackground(var1);
      }

      protected void onPostExecute(String var1) {
         if (var1 != null && !TextUtils.isEmpty(var1)) {
            HomeFragment.LoadRootConfigTask.OnRootConfigLoadedListener var2 = (HomeFragment.LoadRootConfigTask.OnRootConfigLoadedListener)this.onRootConfigLoadedListenerRef.get();
            if (var2 != null) {
               label41: {
                  boolean var10001;
                  JSONArray var3;
                  int var4;
                  _$$Lambda$HomeFragment$LoadRootConfigTask$N7ozq39_Jbe9oQNEx5wgAWpoCWU var11;
                  try {
                     var3 = new JSONArray(var1);
                     var4 = var3.length();
                     String[] var5 = new String[var4];
                     AtomicInteger var10 = new AtomicInteger(var4);
                     this.countdown = var10;
                     var11 = new _$$Lambda$HomeFragment$LoadRootConfigTask$N7ozq39_Jbe9oQNEx5wgAWpoCWU(this, var5, var2);
                  } catch (JSONException var9) {
                     var10001 = false;
                     break label41;
                  }

                  int var6 = 0;

                  while(true) {
                     if (var6 >= var4) {
                        return;
                     }

                     try {
                        WeakReference var12 = new WeakReference(var11);
                        HomeFragment.LoadConfigTask var7 = new HomeFragment.LoadConfigTask(var12, var6);
                        var7.execute(new String[]{var3.getString(var6), this.userAgent, Integer.toString(10002)});
                     } catch (JSONException var8) {
                        var10001 = false;
                        break;
                     }

                     ++var6;
                  }
               }

               var2.onRootConfigLoaded((String[])null);
            }
         }
      }

      private interface OnRootConfigLoadedListener {
         void onRootConfigLoaded(String[] var1);
      }
   }

   private static class MigrateHistoryRunnable implements Runnable {
      private WeakReference contextWeakReference;
      private WeakReference handlerWeakReference;

      MigrateHistoryRunnable(Handler var1, Context var2) {
         this.handlerWeakReference = new WeakReference(var1);
         this.contextWeakReference = new WeakReference(var2);
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }

   private interface OnRemovedListener {
      void onRemoved(Site var1);
   }

   private class SessionManagerObserver implements SessionManager.Observer {
      private SessionManagerObserver() {
      }

      // $FF: synthetic method
      SessionManagerObserver(Object var2) {
         this();
      }

      public boolean handleExternalUrl(String var1) {
         return false;
      }

      public void onFocusChanged(Session var1, SessionManager.Factor var2) {
      }

      public void onHttpAuthRequest(TabViewClient.HttpAuthCallback var1, String var2, String var3) {
      }

      public void onSessionAdded(Session var1, Bundle var2) {
      }

      public void onSessionCountChanged(int var1) {
         HomeFragment.this.updateTabCounter();
      }

      public boolean onShowFileChooser(TabViewEngineSession var1, ValueCallback var2, FileChooserParams var3) {
         return false;
      }

      public void updateFailingUrl(String var1, boolean var2) {
      }
   }

   private class SiteItemClickListener implements OnClickListener, OnLongClickListener {
      private SiteItemClickListener() {
      }

      // $FF: synthetic method
      SiteItemClickListener(Object var2) {
         this();
      }

      // $FF: synthetic method
      static void lambda$null$0(HomeFragment var0) {
         var0.refreshTopSites();
      }

      // $FF: synthetic method
      public static boolean lambda$onLongClick$1(HomeFragment.SiteItemClickListener var0, Site var1, MenuItem var2) {
         int var3 = var2.getItemId();
         if (var3 != 2131296565) {
            if (var3 != 2131296587) {
               throw new IllegalStateException("Unhandled menu item");
            }

            if (var1.getId() < 0L) {
               HomeFragment.this.presenter.removeSite(var1);
               HomeFragment.this.removeDefaultSites(var1);
               TopSitesUtils.saveDefaultSites(HomeFragment.this.getContext(), HomeFragment.this.orginalDefaultSites);
               HomeFragment.this.refreshTopSites();
               TelemetryWrapper.removeTopSite(true);
            } else {
               var1.setViewCount(1L);
               BrowsingHistoryManager.getInstance().updateLastEntry(var1, HomeFragment.this.mTopSiteUpdateListener);
               TelemetryWrapper.removeTopSite(false);
            }

            HomeFragment.this.pinSiteManager.unpinned(var1);
         } else {
            HomeFragment.this.presenter.pinSite(var1, new _$$Lambda$HomeFragment$SiteItemClickListener$PZPLkgWVD_Z_t9I_U5rI2pdPjbg(HomeFragment.this));
         }

         return true;
      }

      public void onClick(View var1) {
         Site var2 = (Site)var1.getTag();
         FragmentActivity var3 = HomeFragment.this.getActivity();
         if (var2 != null && var3 instanceof FragmentListener) {
            ScreenNavigator.get(var1.getContext()).showBrowserScreen(var2.getUrl(), true, false);
            if (var1.getParent() instanceof ViewGroup) {
               int var4 = ((ViewGroup)var1.getParent()).indexOfChild(var1);
               String var5;
               if (var2.isDefault()) {
                  var5 = var2.getTitle();
               } else {
                  var5 = "";
               }

               TelemetryWrapper.clickTopSiteOn(var4, var5);
            }
         }

      }

      public boolean onLongClick(View var1) {
         Site var2 = (Site)var1.getTag();
         boolean var3 = false;
         if (var2 == null) {
            return false;
         } else {
            PopupMenu var4 = new PopupMenu(var1.getContext(), var1, 8);
            var4.getMenuInflater().inflate(2131558405, var4.getMenu());
            MenuItem var6 = var4.getMenu().findItem(2131296565);
            if (var6 != null) {
               boolean var5 = var3;
               if (HomeFragment.this.pinSiteManager.isEnabled()) {
                  var5 = var3;
                  if (!HomeFragment.this.pinSiteManager.isPinned(var2)) {
                     var5 = true;
                  }
               }

               var6.setVisible(var5);
            }

            var4.setOnMenuItemClickListener(new _$$Lambda$HomeFragment$SiteItemClickListener$4IB3ndGZ_ufqnkodgjuU_qaCzyA(this, var2));
            var4.show();
            return true;
         }
      }
   }

   private static class UpdateHistoryWrapper implements FavIconUtils.Consumer {
      private WeakReference handlerWeakReference;
      private List urls;

      private UpdateHistoryWrapper(List var1, WeakReference var2) {
         this.urls = var1;
         this.handlerWeakReference = var2;
      }

      // $FF: synthetic method
      UpdateHistoryWrapper(List var1, WeakReference var2, Object var3) {
         this(var1, var2);
      }

      // $FF: synthetic method
      public static void lambda$accept$0(HomeFragment.UpdateHistoryWrapper var0, int var1) {
         Handler var2 = (Handler)var0.handlerWeakReference.get();
         if (var2 != null) {
            HomeFragment.scheduleRefresh(var2);
         }
      }

      public void accept(List var1) {
         _$$Lambda$HomeFragment$UpdateHistoryWrapper$VD7UQe2V_rm661eh_ychjYTSyLE var2 = new _$$Lambda$HomeFragment$UpdateHistoryWrapper$VD7UQe2V_rm661eh_ychjYTSyLE(this);

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            if (var3 == var1.size() - 1) {
               BrowsingHistoryManager.updateHistory((String)null, (String)this.urls.get(var3), (String)var1.get(var3), var2);
            } else {
               BrowsingHistoryManager.updateHistory((String)null, (String)this.urls.get(var3), (String)var1.get(var3));
            }
         }

      }
   }
}
