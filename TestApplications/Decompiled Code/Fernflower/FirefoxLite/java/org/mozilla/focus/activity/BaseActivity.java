package org.mozilla.focus.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import java.util.Locale;
import org.mozilla.focus.locale.LocaleAwareApplication;
import org.mozilla.focus.locale.LocaleManager;
import org.mozilla.focus.locale.Locales;
import org.mozilla.focus.utils.Settings;

public abstract class BaseActivity extends AppCompatActivity {
   private volatile Locale mLastLocale;

   public static void setLayoutDirection(View var0, Locale var1) {
      if (TextUtilsCompat.getLayoutDirectionFromLocale(var1) != 1) {
         ViewCompat.setLayoutDirection(var0, 0);
      } else {
         ViewCompat.setLayoutDirection(var0, 1);
      }

   }

   private void updateScreenBrightness() {
      Settings var1 = Settings.getInstance(this);
      float var2 = var1.getNightModeBrightnessValue();
      Window var3 = this.getWindow();
      if (var3 != null) {
         LayoutParams var4 = var3.getAttributes();
         if (!var1.isNightModeEnable()) {
            var2 = -1.0F;
         }

         var4.screenBrightness = var2;
         var3.setAttributes(var4);
      }

   }

   public abstract void applyLocale();

   protected void onActivityResult(int var1, int var2, Intent var3) {
      super.onActivityResult(var1, var2, var3);
      this.onConfigurationChanged(this.getResources().getConfiguration());
      if (var2 == 1) {
         this.applyLocale();
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      LocaleManager var2 = LocaleManager.getInstance();
      var2.correctLocale(this, this.getResources(), this.getResources().getConfiguration());
      Locale var3 = var2.onSystemConfigurationChanged(this, this.getResources(), var1, this.mLastLocale);
      if (var3 != null) {
         this.mLastLocale = var3;
         LocaleManager.getInstance().updateConfiguration(this, var3);
         this.applyLocale();
         setLayoutDirection(this.getWindow().getDecorView(), var3);
      }

      super.onConfigurationChanged(var1);
   }

   protected void onCreate(Bundle var1) {
      Locales.initializeLocale(this);
      this.mLastLocale = LocaleManager.getInstance().getCurrentLocale(this.getApplicationContext());
      LocaleManager.getInstance().updateConfiguration(this, this.mLastLocale);
      super.onCreate(var1);
   }

   protected void onPause() {
      super.onPause();
      ((LocaleAwareApplication)this.getApplicationContext()).onActivityPause();
   }

   protected void onResume() {
      super.onResume();
      this.updateScreenBrightness();
      ((LocaleAwareApplication)this.getApplicationContext()).onActivityResume();
   }

   public void openPreferences() {
      this.startActivityForResult(new Intent(this, SettingsActivity.class), 0);
   }
}
