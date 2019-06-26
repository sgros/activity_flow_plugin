package menion.android.whereyougo.utils;

import android.app.Application;
import menion.android.whereyougo.BuildConfig;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.audio.ManagerAudio;
import menion.android.whereyougo.geo.orientation.Orientation;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import menion.android.whereyougo.guide.GuideContent;

/* renamed from: menion.android.whereyougo.utils.A */
public class C0322A {
    private static final String TAG = "A";
    private static MainApplication app;
    private static GuideContent guidingContent;
    private static CustomMainActivity main;
    private static ManagerAudio managerAudio;
    private static Orientation rotator;

    public static void destroy() {
        guidingContent = null;
        managerAudio = null;
        main = null;
        if (rotator != null) {
            rotator.removeAllListeners();
            rotator = null;
        }
        if (app != null) {
            app.destroy();
        }
        app = null;
    }

    public static Application getApp() {
        return app;
    }

    public static GuideContent getGuidingContent() {
        if (guidingContent == null) {
            guidingContent = new GuideContent();
        }
        return guidingContent;
    }

    public static CustomMainActivity getMain() {
        return main;
    }

    public static ManagerAudio getManagerAudio() {
        if (managerAudio == null) {
            managerAudio = new ManagerAudio();
        }
        return managerAudio;
    }

    public static Orientation getRotator() {
        if (rotator == null) {
            rotator = new Orientation();
        }
        return rotator;
    }

    public static void printState() {
        Logger.m24i(TAG, "printState() - STATIC VARIABLES");
        Logger.m24i(TAG, "app:" + app);
        Logger.m24i(TAG, "managerAudio:" + managerAudio);
        Logger.m24i(TAG, "main:" + main);
        Logger.m24i(TAG, "guidingContent:" + guidingContent);
        Logger.m24i(TAG, "rotator:" + rotator);
    }

    public static void registerApp(MainApplication app) {
        app = app;
    }

    public static void registerMain(CustomMainActivity main) {
        main = main;
    }

    public static String getAppName() {
        try {
            return app.getPackageManager().getApplicationLabel(app.getApplicationInfo()).toString();
        } catch (Exception e) {
            return MainApplication.APP_NAME;
        }
    }

    public static String getAppVersion() {
        try {
            return app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return BuildConfig.VERSION_NAME;
        }
    }
}
