package org.mozilla.focus.locale;

import android.app.Application;
import android.content.res.Configuration;

public class LocaleAwareApplication extends Application {
   private boolean mInBackground;

   public void onActivityPause() {
      this.mInBackground = true;
   }

   public void onActivityResume() {
      this.mInBackground = true;
   }

   public void onConfigurationChanged(Configuration var1) {
      if (this.mInBackground) {
         super.onConfigurationChanged(var1);
      } else {
         try {
            LocaleManager.getInstance().correctLocale(this, this.getResources(), var1);
         } catch (IllegalStateException var3) {
         }

         super.onConfigurationChanged(var1);
      }
   }

   public void onCreate() {
      Locales.initializeLocale(this);
      super.onCreate();
   }
}
