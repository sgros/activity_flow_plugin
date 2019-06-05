// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus;

import android.view.animation.Animation;
import android.view.View;
import org.mozilla.rocket.urlinput.QuickSearchDataSource;
import org.mozilla.rocket.urlinput.LocaleDataSource;
import org.mozilla.rocket.urlinput.GlobalDataSource;
import org.mozilla.rocket.urlinput.QuickSearchRepository;
import android.app.Application;
import org.mozilla.rocket.download.DownloadInfoRepository;
import org.mozilla.rocket.urlinput.QuickSearchViewModelFactory;
import org.mozilla.rocket.urlinput.QuickSearchViewModel;
import org.mozilla.rocket.download.DownloadInfoViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import org.mozilla.rocket.download.DownloadViewModelFactory;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;
import android.support.v4.app.FragmentActivity;
import android.os.StrictMode$ThreadPolicy;
import org.mozilla.focus.persistence.TabsDatabase;
import android.preference.PreferenceManager;
import android.content.Context;
import org.mozilla.focus.utils.RemoteConfigConstants;
import android.os.StrictMode;
import android.os.StrictMode$VmPolicy$Builder;
import android.os.StrictMode$ThreadPolicy$Builder;
import org.mozilla.focus.utils.AppConstants;

public class Inject
{
    private static boolean sIsNewCreated = true;
    
    public static void enableStrictMode() {
        if (AppConstants.isReleaseBuild()) {
            return;
        }
        final StrictMode$ThreadPolicy$Builder detectAll = new StrictMode$ThreadPolicy$Builder().detectAll();
        final StrictMode$VmPolicy$Builder detectAll2 = new StrictMode$VmPolicy$Builder().detectAll();
        detectAll.penaltyLog().penaltyDialog();
        detectAll2.penaltyLog();
        StrictMode.setThreadPolicy(detectAll.build());
        StrictMode.setVmPolicy(detectAll2.build());
    }
    
    public static final boolean getActivityNewlyCreatedFlag() {
        return Inject.sIsNewCreated;
    }
    
    public static RemoteConfigConstants.SURVEY getDefaultFeatureSurvey() {
        return RemoteConfigConstants.SURVEY.NONE;
    }
    
    public static String getDefaultTopSites(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("topsites_pref", (String)null);
    }
    
    public static TabsDatabase getTabsDatabase(final Context context) {
        return TabsDatabase.getInstance(context);
    }
    
    public static boolean isTelemetryEnabled(final Context context) {
        final StrictMode$ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getResources().getString(2131755331), AppConstants.isBuiltWithFirebase());
        }
        finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }
    
    public static boolean isUnderEspressoTest() {
        return false;
    }
    
    public static DownloadIndicatorViewModel obtainDownloadIndicatorViewModel(final FragmentActivity fragmentActivity) {
        return ViewModelProviders.of(fragmentActivity, DownloadViewModelFactory.getInstance()).get(DownloadIndicatorViewModel.class);
    }
    
    public static DownloadInfoViewModel obtainDownloadInfoViewModel(final FragmentActivity fragmentActivity) {
        return ViewModelProviders.of(fragmentActivity, DownloadViewModelFactory.getInstance()).get(DownloadInfoViewModel.class);
    }
    
    public static QuickSearchViewModel obtainQuickSearchViewModel(final FragmentActivity fragmentActivity) {
        return ViewModelProviders.of(fragmentActivity, new QuickSearchViewModelFactory(provideQuickSearchRepository(fragmentActivity.getApplication()))).get(QuickSearchViewModel.class);
    }
    
    public static DownloadInfoRepository provideDownloadInfoRepository() {
        return DownloadInfoRepository.getInstance();
    }
    
    private static QuickSearchRepository provideQuickSearchRepository(final Application application) {
        return QuickSearchRepository.getInstance(GlobalDataSource.getInstance((Context)application), LocaleDataSource.getInstance((Context)application));
    }
    
    public static void setActivityNewlyCreatedFlag() {
        Inject.sIsNewCreated = false;
    }
    
    public static void startAnimation(final View view, final Animation animation) {
        if (view == null) {
            return;
        }
        view.startAnimation(animation);
    }
}
