package org.mozilla.focus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.mozilla.rocket.home.pinsite.PinSiteManager;
import org.mozilla.rocket.home.pinsite.PinSiteManagerKt;

public class NewFeatureNotice {
   private static NewFeatureNotice instance;
   private final boolean hasNewsPortal;
   private boolean hasShownEcShoppingLink = false;
   private final PinSiteManager pinSiteManager;
   private final SharedPreferences preferences;

   private NewFeatureNotice(Context var1) {
      this.preferences = PreferenceManager.getDefaultSharedPreferences(var1);
      this.hasNewsPortal = AppConfigWrapper.hasNewsPortal(var1);
      this.pinSiteManager = PinSiteManagerKt.getPinSiteManager(var1);
   }

   public static NewFeatureNotice getInstance(Context var0) {
      synchronized(NewFeatureNotice.class){}

      NewFeatureNotice var4;
      try {
         if (instance == null) {
            NewFeatureNotice var1 = new NewFeatureNotice(var0.getApplicationContext());
            instance = var1;
         }

         var4 = instance;
      } finally {
         ;
      }

      return var4;
   }

   private boolean isNewlyInstalled() {
      boolean var1;
      if (!this.hasShownFirstRun() && this.getLastShownFeatureVersion() == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void setEcShoppingLinkDidShow() {
      if (this.hasShownEcShoppingLink) {
         this.preferences.edit().putBoolean("ec_shoppingLink_shown", true).apply();
      }
   }

   private void setLastShownFeatureVersion(int var1) {
      if (this.getLastShownFeatureVersion() < var1) {
         this.preferences.edit().putInt("firstrun_upgrade_version", var1).apply();
      }
   }

   public boolean from21to40() {
      boolean var1;
      if (3 > this.getLastShownFeatureVersion()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean from40to114() {
      boolean var1;
      if (4 > this.getLastShownFeatureVersion() && this.hasNewsPortal) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public int getLastShownFeatureVersion() {
      return this.preferences.getInt("firstrun_upgrade_version", 0);
   }

   public void hasShownEcShoppingLink() {
      this.hasShownEcShoppingLink = true;
   }

   public boolean hasShownFirstRun() {
      return this.preferences.getBoolean("firstrun_shown", false);
   }

   public void setFirstRunDidShow() {
      this.preferences.edit().putBoolean("firstrun_shown", true).apply();
   }

   public void setLiteUpdateDidShow() {
      this.setFirstRunDidShow();
      this.setEcShoppingLinkDidShow();
      this.setLastShownFeatureVersion(4);
   }

   public void setPrivacyPolicyUpdateNoticeDidShow() {
      this.setLastShownFeatureVersion(2);
   }

   public boolean shouldShowEcShoppingLinkOnboarding() {
      boolean var1 = AppConfigWrapper.hasEcommerceShoppingLink();
      boolean var2 = false;
      boolean var3 = var2;
      if (var1) {
         var3 = var2;
         if (!this.preferences.getBoolean("ec_shoppingLink_shown", false)) {
            var3 = true;
         }
      }

      return var3;
   }

   public boolean shouldShowLiteUpdate() {
      boolean var1 = this.pinSiteManager.isEnabled();
      boolean var2 = true;
      boolean var3;
      if (var1 && this.pinSiteManager.isFirstTimeEnable()) {
         var3 = true;
      } else {
         var3 = false;
      }

      var1 = var2;
      if (!this.from21to40()) {
         var1 = var2;
         if (!this.from40to114()) {
            var1 = var2;
            if (!this.shouldShowEcShoppingLinkOnboarding()) {
               if (var3) {
                  var1 = var2;
               } else {
                  var1 = false;
               }
            }
         }
      }

      return var1;
   }

   public boolean shouldShowPrivacyPolicyUpdate() {
      boolean var1 = this.isNewlyInstalled();
      boolean var2 = false;
      if (var1) {
         this.setPrivacyPolicyUpdateNoticeDidShow();
         return false;
      } else {
         if (2 == this.getLastShownFeatureVersion() + 1) {
            var2 = true;
         }

         return var2;
      }
   }
}
