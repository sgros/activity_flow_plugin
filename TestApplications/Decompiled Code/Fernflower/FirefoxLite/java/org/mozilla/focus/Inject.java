package org.mozilla.focus;

import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import org.mozilla.focus.persistence.TabsDatabase;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.utils.RemoteConfigConstants;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;
import org.mozilla.rocket.download.DownloadInfoRepository;
import org.mozilla.rocket.download.DownloadInfoViewModel;
import org.mozilla.rocket.download.DownloadViewModelFactory;
import org.mozilla.rocket.urlinput.GlobalDataSource;
import org.mozilla.rocket.urlinput.LocaleDataSource;
import org.mozilla.rocket.urlinput.QuickSearchRepository;
import org.mozilla.rocket.urlinput.QuickSearchViewModel;
import org.mozilla.rocket.urlinput.QuickSearchViewModelFactory;

public class Inject {
   private static boolean sIsNewCreated;

   public static void enableStrictMode() {
      if (!AppConstants.isReleaseBuild()) {
         Builder var0 = (new Builder()).detectAll();
         android.os.StrictMode.VmPolicy.Builder var1 = (new android.os.StrictMode.VmPolicy.Builder()).detectAll();
         var0.penaltyLog().penaltyDialog();
         var1.penaltyLog();
         StrictMode.setThreadPolicy(var0.build());
         StrictMode.setVmPolicy(var1.build());
      }
   }

   public static final boolean getActivityNewlyCreatedFlag() {
      return sIsNewCreated;
   }

   public static RemoteConfigConstants.SURVEY getDefaultFeatureSurvey() {
      return RemoteConfigConstants.SURVEY.NONE;
   }

   public static String getDefaultTopSites(Context var0) {
      return PreferenceManager.getDefaultSharedPreferences(var0).getString("topsites_pref", (String)null);
   }

   public static TabsDatabase getTabsDatabase(Context var0) {
      return TabsDatabase.getInstance(var0);
   }

   public static boolean isTelemetryEnabled(Context var0) {
      ThreadPolicy var1 = StrictMode.allowThreadDiskReads();

      boolean var3;
      try {
         Resources var2 = var0.getResources();
         SharedPreferences var6 = PreferenceManager.getDefaultSharedPreferences(var0);
         var3 = AppConstants.isBuiltWithFirebase();
         var3 = var6.getBoolean(var2.getString(2131755331), var3);
      } finally {
         StrictMode.setThreadPolicy(var1);
      }

      return var3;
   }

   public static boolean isUnderEspressoTest() {
      return false;
   }

   public static DownloadIndicatorViewModel obtainDownloadIndicatorViewModel(FragmentActivity var0) {
      return (DownloadIndicatorViewModel)ViewModelProviders.of((FragmentActivity)var0, DownloadViewModelFactory.getInstance()).get(DownloadIndicatorViewModel.class);
   }

   public static DownloadInfoViewModel obtainDownloadInfoViewModel(FragmentActivity var0) {
      return (DownloadInfoViewModel)ViewModelProviders.of((FragmentActivity)var0, DownloadViewModelFactory.getInstance()).get(DownloadInfoViewModel.class);
   }

   public static QuickSearchViewModel obtainQuickSearchViewModel(FragmentActivity var0) {
      return (QuickSearchViewModel)ViewModelProviders.of((FragmentActivity)var0, new QuickSearchViewModelFactory(provideQuickSearchRepository(var0.getApplication()))).get(QuickSearchViewModel.class);
   }

   public static DownloadInfoRepository provideDownloadInfoRepository() {
      return DownloadInfoRepository.getInstance();
   }

   private static QuickSearchRepository provideQuickSearchRepository(Application var0) {
      return QuickSearchRepository.getInstance(GlobalDataSource.getInstance(var0), LocaleDataSource.getInstance(var0));
   }

   public static void setActivityNewlyCreatedFlag() {
      sIsNewCreated = false;
   }

   public static void startAnimation(View var0, Animation var1) {
      if (var0 != null) {
         var0.startAnimation(var1);
      }
   }
}
