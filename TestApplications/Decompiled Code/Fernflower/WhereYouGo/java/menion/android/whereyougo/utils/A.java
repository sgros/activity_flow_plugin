package menion.android.whereyougo.utils;

import android.app.Application;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.audio.ManagerAudio;
import menion.android.whereyougo.geo.orientation.Orientation;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import menion.android.whereyougo.guide.GuideContent;

public class A {
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

   public static String getAppName() {
      String var0;
      try {
         var0 = app.getPackageManager().getApplicationLabel(app.getApplicationInfo()).toString();
      } catch (Exception var1) {
         var0 = "WhereYouGo";
      }

      return var0;
   }

   public static String getAppVersion() {
      String var0;
      try {
         var0 = app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName;
      } catch (Exception var1) {
         var0 = "0.9.3";
      }

      return var0;
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
      Logger.i("A", "printState() - STATIC VARIABLES");
      Logger.i("A", "app:" + app);
      Logger.i("A", "managerAudio:" + managerAudio);
      Logger.i("A", "main:" + main);
      Logger.i("A", "guidingContent:" + guidingContent);
      Logger.i("A", "rotator:" + rotator);
   }

   public static void registerApp(MainApplication var0) {
      app = var0;
   }

   public static void registerMain(CustomMainActivity var0) {
      main = var0;
   }
}
