package menion.android.whereyougo.gui.extension.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable.Callback;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Logger;

public class CustomActivity extends FragmentActivity {
   protected static void customOnCreate(Activity var0) {
      if (!(var0 instanceof CustomMainActivity)) {
         setScreenBasic(var0);
      }

      DisplayMetrics var1 = new DisplayMetrics();
      var0.getWindowManager().getDefaultDisplay().getMetrics(var1);
      Const.SCREEN_WIDTH = var1.widthPixels;
      Const.SCREEN_HEIGHT = var1.heightPixels;
      switch(Preferences.APPEARANCE_FONT_SIZE) {
      case 1:
         var0.setTheme(2131361803);
         break;
      case 2:
         var0.setTheme(2131361802);
         break;
      case 3:
         var0.setTheme(2131361801);
      }

   }

   protected static void customOnPause(Activity var0) {
      if (PreferenceValues.getCurrentActivity() == var0) {
         PreferenceValues.setCurrentActivity((Activity)null);
      }

      MainApplication.onActivityPause();
   }

   protected static void customOnResume(Activity var0) {
      PreferenceValues.setCurrentActivity(var0);
      PreferenceValues.enableWakeLock();
   }

   protected static void customOnStart(Activity var0) {
      setStatusbar(var0);
      setScreenFullscreen(var0);
   }

   protected static void setScreenBasic(Activity var0) {
      try {
         var0.requestWindowFeature(1);
      } catch (Exception var1) {
      }

   }

   public static void setScreenFullscreen(Activity var0) {
      try {
         if (Preferences.APPEARANCE_FULLSCREEN) {
            var0.getWindow().setFlags(1024, 1024);
         } else {
            var0.getWindow().clearFlags(1024);
         }
      } catch (Exception var1) {
      }

   }

   public static void setStatusbar(Activity var0) {
      try {
         NotificationManager var1 = (NotificationManager)var0.getSystemService("notification");
         if (Preferences.APPEARANCE_STATUSBAR) {
            Context var2 = var0.getApplicationContext();
            Intent var3 = new Intent(var2, MainActivity.class);
            var3.addCategory("android.intent.category.LAUNCHER");
            var3.setAction("android.intent.action.MAIN");
            PendingIntent var6 = PendingIntent.getActivity(var2, 0, var3, 0);
            NotificationCompat.Builder var5 = new NotificationCompat.Builder(var0);
            var1.notify(0, var5.setContentTitle(A.getAppName()).setSmallIcon(2130837551).setContentIntent(var6).setOngoing(true).build());
         } else {
            var1.cancel(0);
         }
      } catch (Exception var4) {
      }

   }

   private void unbindDrawables(View var1) {
      if (var1 != null) {
         if (var1.getBackground() != null) {
            var1.getBackground().setCallback((Callback)null);
         }

         if (var1 instanceof ViewGroup) {
            for(int var2 = 0; var2 < ((ViewGroup)var1).getChildCount(); ++var2) {
               this.unbindDrawables(((ViewGroup)var1).getChildAt(var2));
            }

            ((ViewGroup)var1).removeAllViews();
         }
      }

   }

   public void finish() {
      super.finish();
      this.overridePendingTransition(0, 0);
   }

   public int getParentViewId() {
      return -1;
   }

   public void onCreate(Bundle var1) {
      Logger.v(this.getLocalClassName(), "onCreate(), id:" + this.hashCode());

      try {
         super.onCreate(var1);
         customOnCreate(this);
      } catch (Exception var2) {
         Logger.e(this.getLocalClassName(), "onCreate()", var2);
      }

   }

   public void onDestroy() {
      Logger.v(this.getLocalClassName(), "onDestroy(), id:" + this.hashCode());

      try {
         super.onDestroy();
         if (this.getParentViewId() != -1) {
            this.unbindDrawables(this.findViewById(this.getParentViewId()));
            System.gc();
         }
      } catch (Exception var2) {
         Logger.e(this.getLocalClassName(), "onDestroy()", var2);
      }

   }

   protected void onPause() {
      Logger.v(this.getLocalClassName(), "onPause(), id:" + this.hashCode());

      try {
         super.onPause();
         customOnPause(this);
      } catch (Exception var2) {
         Logger.e(this.getLocalClassName(), "onPause()", var2);
      }

   }

   protected void onResume() {
      Logger.v(this.getLocalClassName(), "onResume(), id:" + this.hashCode());

      try {
         super.onResume();
         customOnResume(this);
         DisplayMetrics var1 = new DisplayMetrics();
         this.getWindowManager().getDefaultDisplay().getMetrics(var1);
         Const.SCREEN_WIDTH = var1.widthPixels;
         Const.SCREEN_HEIGHT = var1.heightPixels;
      } catch (Exception var2) {
         Logger.e(this.getLocalClassName(), "onResume()", var2);
      }

   }

   public void onStart() {
      Logger.v(this.getLocalClassName(), "onStart(), id:" + this.hashCode());

      try {
         super.onStart();
         customOnStart(this);
      } catch (Exception var2) {
         Logger.e(this.getLocalClassName(), "onStart()", var2);
      }

   }
}
