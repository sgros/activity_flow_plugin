// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

import android.app.Application;
import menion.android.whereyougo.geo.orientation.Orientation;
import menion.android.whereyougo.audio.ManagerAudio;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import menion.android.whereyougo.guide.GuideContent;
import menion.android.whereyougo.MainApplication;

public class A
{
    private static final String TAG = "A";
    private static MainApplication app;
    private static GuideContent guidingContent;
    private static CustomMainActivity main;
    private static ManagerAudio managerAudio;
    private static Orientation rotator;
    
    public static void destroy() {
        A.guidingContent = null;
        A.managerAudio = null;
        A.main = null;
        if (A.rotator != null) {
            A.rotator.removeAllListeners();
            A.rotator = null;
        }
        if (A.app != null) {
            A.app.destroy();
        }
        A.app = null;
    }
    
    public static Application getApp() {
        return A.app;
    }
    
    public static String getAppName() {
        try {
            return A.app.getPackageManager().getApplicationLabel(A.app.getApplicationInfo()).toString();
        }
        catch (Exception ex) {
            return "WhereYouGo";
        }
    }
    
    public static String getAppVersion() {
        try {
            return A.app.getPackageManager().getPackageInfo(A.app.getPackageName(), 0).versionName;
        }
        catch (Exception ex) {
            return "0.9.3";
        }
    }
    
    public static GuideContent getGuidingContent() {
        if (A.guidingContent == null) {
            A.guidingContent = new GuideContent();
        }
        return A.guidingContent;
    }
    
    public static CustomMainActivity getMain() {
        return A.main;
    }
    
    public static ManagerAudio getManagerAudio() {
        if (A.managerAudio == null) {
            A.managerAudio = new ManagerAudio();
        }
        return A.managerAudio;
    }
    
    public static Orientation getRotator() {
        if (A.rotator == null) {
            A.rotator = new Orientation();
        }
        return A.rotator;
    }
    
    public static void printState() {
        Logger.i("A", "printState() - STATIC VARIABLES");
        Logger.i("A", "app:" + A.app);
        Logger.i("A", "managerAudio:" + A.managerAudio);
        Logger.i("A", "main:" + A.main);
        Logger.i("A", "guidingContent:" + A.guidingContent);
        Logger.i("A", "rotator:" + A.rotator);
    }
    
    public static void registerApp(final MainApplication app) {
        A.app = app;
    }
    
    public static void registerMain(final CustomMainActivity main) {
        A.main = main;
    }
}
