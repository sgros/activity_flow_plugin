package menion.android.whereyougo;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.Log;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.WherigoLib;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.SaveGame;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.openwig.WUI;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.ExceptionHandler;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.utils.StringToken;
import menion.android.whereyougo.utils.Utils;

public class MainApplication extends Application {
   public static final String APP_NAME = "WhereYouGo";
   private static final String TAG = "MainApplication";
   private static Context applicationContext;
   private static Timer mTimer;
   private static MainApplication.OnAppVisibilityChange onAppVisibilityChange;
   private Locale locale = null;
   private boolean mScreenOff = false;
   private MainApplication.ScreenReceiver mScreenReceiver;

   public static void appRestored() {
      onAppRestored();
      if (onAppVisibilityChange != null) {
         onAppVisibilityChange.onAppRestored();
      }

   }

   public static Context getContext() {
      return applicationContext;
   }

   private void initCore() {
      IntentFilter var1 = new IntentFilter("android.intent.action.SCREEN_ON");
      var1.addAction("android.intent.action.SCREEN_OFF");
      this.mScreenReceiver = new MainApplication.ScreenReceiver();
      this.registerReceiver(this.mScreenReceiver, var1);
      this.setRoot(Preferences.GLOBAL_ROOT);

      try {
         FileSystem.CACHE = this.getExternalCacheDir().getAbsolutePath();
      } catch (Exception var5) {
         try {
            FileSystem.CACHE = this.getCacheDir().getAbsolutePath();
         } catch (Exception var4) {
            FileSystem.CACHE = FileSystem.ROOT + "cache/";
         }
      }

      if (!FileSystem.CACHE.endsWith("/")) {
         FileSystem.CACHE = FileSystem.CACHE + "/";
      }

      FileSystem.CACHE_AUDIO = FileSystem.CACHE + "audio/";
      LocationState.init(this);
      Utils.getDpPixels(this, 1.0F);

      try {
         String var6 = String.format("%s, app:%s", A.getAppName(), A.getAppVersion());
         String var2 = String.format("Android %s", VERSION.RELEASE);
         WherigoLib.env.put("DeviceID", var6);
         WherigoLib.env.put("Platform", var2);
      } catch (Exception var3) {
      }

   }

   private void legacySupport4PreferencesFloat(int var1) {
      SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(this);
      String var3 = this.getString(var1);

      try {
         var2.getString(var3, "");
      } catch (Exception var7) {
         try {
            Log.d("MainApplication", "legecySupport4PreferencesFloat() - LEGECY SUPPORT: convert float to string");
            float var5 = var2.getFloat(var3, 0.0F);
            var2.edit().remove(var3).commit();
            var2.edit().putString(var3, String.valueOf(var5)).commit();
         } catch (Exception var6) {
            Log.e("MainApplication", "legecySupport4PreferencesFloat() - panic remove", var6);
            var2.edit().remove(var3).commit();
         }
      }

   }

   private void legacySupport4PreferencesInt(int var1) {
      SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(this);
      String var3 = this.getString(var1);

      try {
         var2.getString(var3, "");
      } catch (Exception var6) {
         try {
            Log.d("MainApplication", "legecySupport4PreferencesInt() - LEGECY SUPPORT: convert int to string");
            var1 = var2.getInt(var3, 0);
            var2.edit().remove(var3).commit();
            var2.edit().putString(var3, String.valueOf(var1)).commit();
         } catch (Exception var5) {
            Log.e("MainApplication", "legecySupportFloat2Int() - panic remove", var5);
            var2.edit().remove(var3).commit();
         }
      }

   }

   public static void onActivityPause() {
      if (mTimer != null) {
         mTimer.cancel();
      }

      mTimer = new Timer();
      mTimer.schedule(new TimerTask() {
         public void run() {
            if (!PreferenceValues.existCurrentActivity()) {
               MainApplication.onAppMinimized();
            }

            LocationState.onActivityPauseInstant(PreferenceValues.getCurrentActivity());
            MainApplication.mTimer = null;
         }
      }, 2000L);
   }

   private static void onAppMinimized() {
      Logger.w("MainApplication", "onAppMinimized()");
      if (onAppVisibilityChange != null) {
         onAppVisibilityChange.onAppMinimized();
      }

   }

   private static void onAppRestored() {
      Logger.w("MainApplication", "onAppRestored()");
   }

   public static void registerVisibilityHandler(MainApplication.OnAppVisibilityChange var0) {
      onAppVisibilityChange = var0;
   }

   public void destroy() {
      try {
         this.unregisterReceiver(this.mScreenReceiver);
      } catch (Exception var2) {
         Logger.w("MainApplication", "destroy(), e:" + var2);
      }

      if (mTimer != null) {
         mTimer.cancel();
         mTimer = null;
      }

      onAppVisibilityChange = null;
   }

   public boolean isScreenOff() {
      return this.mScreenOff;
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      if (this.locale != null) {
         var1.locale = this.locale;
         Locale.setDefault(this.locale);
         this.getBaseContext().getResources().updateConfiguration(var1, this.getBaseContext().getResources().getDisplayMetrics());
      }

   }

   public void onCreate() {
      super.onCreate();
      applicationContext = this;
      Log.d("MainApplication", "onCreate()");
      Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

      try {
         this.legacySupport4PreferencesFloat(2131165565);
         this.legacySupport4PreferencesFloat(2131165566);
         this.legacySupport4PreferencesFloat(2131165564);
         this.legacySupport4PreferencesInt(2131165568);
      } catch (Exception var4) {
         Log.e("MainApplication", "onCreate() - PANIC! Wipe out preferences", var4);
         PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
      }

      PreferenceManager.setDefaultValues(this, 2131034113, false);
      Preferences.setContext(this);
      Preferences.init(this);
      Configuration var1 = this.getBaseContext().getResources().getConfiguration();
      String var2 = Preferences.getStringPreference(2131165580);
      if (!var2.equals(this.getString(2131165641)) && !var1.locale.getLanguage().equals(var2)) {
         ArrayList var3 = StringToken.parse(var2, "_");
         if (var3.size() == 1) {
            this.locale = new Locale(var2);
         } else {
            this.locale = new Locale((String)var3.get(0), (String)var3.get(1));
         }

         var1.locale = this.locale;
         this.getBaseContext().getResources().updateConfiguration(var1, this.getBaseContext().getResources().getDisplayMetrics());
      }

      this.initCore();
   }

   public void onLowMemory() {
      super.onLowMemory();
      Log.d("MainApplication", "onLowMemory()");
   }

   public void onTerminate() {
      super.onTerminate();
      Log.d("MainApplication", "onTerminate()");
   }

   public void onTrimMemory(int var1) {
      super.onTrimMemory(var1);
      Logger.i("MainApplication", String.format("onTrimMemory(%d)", var1));

      label53: {
         boolean var10001;
         try {
            if (!Preferences.GLOBAL_SAVEGAME_AUTO) {
               return;
            }
         } catch (Exception var8) {
            var10001 = false;
            break label53;
         }

         if (var1 != 20) {
            return;
         }

         final Activity var2;
         try {
            if (MainActivity.selectedFile == null || Engine.instance == null) {
               return;
            }

            var2 = PreferenceValues.getCurrentActivity();
         } catch (Exception var7) {
            var10001 = false;
            break label53;
         }

         if (var2 == null) {
            return;
         }

         try {
            if (MainActivity.wui != null) {
               WUI var3 = MainActivity.wui;
               Runnable var4 = new Runnable() {
                  public void run() {
                     ManagerNotify.toastShortMessage(var2, MainApplication.this.getString(2131165400));
                     MainActivity.wui.setOnSavingFinished((Runnable)null);
                  }
               };
               var3.setOnSavingFinished(var4);
            }
         } catch (Exception var6) {
            var10001 = false;
            break label53;
         }

         try {
            SaveGame var9 = new SaveGame(var2);
            var9.execute(new Void[0]);
            return;
         } catch (Exception var5) {
            var10001 = false;
         }
      }

      Logger.e("MainApplication", String.format("onTrimMemory(%d): savegame failed", var1));
   }

   public boolean setRoot(String var1) {
      String var2 = null;

      String var3;
      label47: {
         try {
            var3 = this.getExternalFilesDir((String)null).getAbsolutePath();
         } catch (Exception var8) {
            break label47;
         }

         var2 = var3;
      }

      var3 = null;

      label42: {
         String var4;
         try {
            var4 = this.getFilesDir().getAbsolutePath();
         } catch (Exception var7) {
            break label42;
         }

         var3 = var4;
      }

      boolean var6;
      label51: {
         boolean var5 = true;
         if (var1 != null) {
            var6 = var5;
            if (FileSystem.setRootDirectory(var1)) {
               break label51;
            }
         }

         if (var2 != null) {
            var6 = var5;
            if (FileSystem.setRootDirectory(var2)) {
               break label51;
            }
         }

         if (var3 != null) {
            var6 = var5;
            if (FileSystem.setRootDirectory(var3)) {
               break label51;
            }
         }

         var6 = false;
      }

      Preferences.GLOBAL_ROOT = FileSystem.ROOT;
      Preferences.setStringPreference(2131165582, Preferences.GLOBAL_ROOT);
      if (!var6) {
         ManagerNotify.toastShortMessage(this, this.getString(2131165203));
      }

      return var6;
   }

   public interface OnAppVisibilityChange {
      void onAppMinimized();

      void onAppRestored();
   }

   private class ScreenReceiver extends BroadcastReceiver {
      private ScreenReceiver() {
      }

      // $FF: synthetic method
      ScreenReceiver(Object var2) {
         this();
      }

      public void onReceive(Context var1, Intent var2) {
         if (var2.getAction().equals("android.intent.action.SCREEN_OFF")) {
            MainApplication.this.mScreenOff = true;
         } else if (var2.getAction().equals("android.intent.action.SCREEN_ON")) {
            LocationState.onScreenOn(false);
            MainApplication.this.mScreenOff = false;
         }

      }
   }
}
