package org.mozilla.focus;

import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.os.StrictMode.VmPolicy;
import android.preference.PreferenceManager;
import android.support.p001v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import org.mozilla.focus.persistence.TabsDatabase;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.utils.RemoteConfigConstants.SURVEY;
import org.mozilla.rocket.C0769R;
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
    private static boolean sIsNewCreated = true;

    public static boolean isUnderEspressoTest() {
        return false;
    }

    public static String getDefaultTopSites(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("topsites_pref", null);
    }

    public static TabsDatabase getTabsDatabase(Context context) {
        return TabsDatabase.getInstance(context);
    }

    public static boolean isTelemetryEnabled(Context context) {
        ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            Resources resources = context.getResources();
            boolean z = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(resources.getString(C0769R.string.pref_key_telemetry), AppConstants.isBuiltWithFirebase());
            return z;
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    public static void enableStrictMode() {
        if (!AppConstants.isReleaseBuild()) {
            Builder detectAll = new Builder().detectAll();
            VmPolicy.Builder detectAll2 = new VmPolicy.Builder().detectAll();
            detectAll.penaltyLog().penaltyDialog();
            detectAll2.penaltyLog();
            StrictMode.setThreadPolicy(detectAll.build());
            StrictMode.setVmPolicy(detectAll2.build());
        }
    }

    public static final boolean getActivityNewlyCreatedFlag() {
        return sIsNewCreated;
    }

    public static void setActivityNewlyCreatedFlag() {
        sIsNewCreated = false;
    }

    public static SURVEY getDefaultFeatureSurvey() {
        return SURVEY.NONE;
    }

    public static DownloadInfoRepository provideDownloadInfoRepository() {
        return DownloadInfoRepository.getInstance();
    }

    public static DownloadIndicatorViewModel obtainDownloadIndicatorViewModel(FragmentActivity fragmentActivity) {
        return (DownloadIndicatorViewModel) ViewModelProviders.m3of(fragmentActivity, DownloadViewModelFactory.getInstance()).get(DownloadIndicatorViewModel.class);
    }

    public static DownloadInfoViewModel obtainDownloadInfoViewModel(FragmentActivity fragmentActivity) {
        return (DownloadInfoViewModel) ViewModelProviders.m3of(fragmentActivity, DownloadViewModelFactory.getInstance()).get(DownloadInfoViewModel.class);
    }

    private static QuickSearchRepository provideQuickSearchRepository(Application application) {
        return QuickSearchRepository.getInstance(GlobalDataSource.getInstance(application), LocaleDataSource.getInstance(application));
    }

    public static QuickSearchViewModel obtainQuickSearchViewModel(FragmentActivity fragmentActivity) {
        return (QuickSearchViewModel) ViewModelProviders.m3of(fragmentActivity, new QuickSearchViewModelFactory(provideQuickSearchRepository(fragmentActivity.getApplication()))).get(QuickSearchViewModel.class);
    }

    public static void startAnimation(View view, Animation animation) {
        if (view != null) {
            view.startAnimation(animation);
        }
    }
}
