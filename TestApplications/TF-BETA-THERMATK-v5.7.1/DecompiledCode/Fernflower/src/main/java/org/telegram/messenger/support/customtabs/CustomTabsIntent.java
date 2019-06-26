package org.telegram.messenger.support.customtabs;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.BundleCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;

public final class CustomTabsIntent {
   public static final String EXTRA_ACTION_BUTTON_BUNDLE = "android.support.customtabs.extra.ACTION_BUTTON_BUNDLE";
   public static final String EXTRA_CLOSE_BUTTON_ICON = "android.support.customtabs.extra.CLOSE_BUTTON_ICON";
   public static final String EXTRA_DEFAULT_SHARE_MENU_ITEM = "android.support.customtabs.extra.SHARE_MENU_ITEM";
   public static final String EXTRA_ENABLE_INSTANT_APPS = "android.support.customtabs.extra.EXTRA_ENABLE_INSTANT_APPS";
   public static final String EXTRA_ENABLE_URLBAR_HIDING = "android.support.customtabs.extra.ENABLE_URLBAR_HIDING";
   public static final String EXTRA_EXIT_ANIMATION_BUNDLE = "android.support.customtabs.extra.EXIT_ANIMATION_BUNDLE";
   public static final String EXTRA_MENU_ITEMS = "android.support.customtabs.extra.MENU_ITEMS";
   public static final String EXTRA_REMOTEVIEWS = "android.support.customtabs.extra.EXTRA_REMOTEVIEWS";
   public static final String EXTRA_REMOTEVIEWS_CLICKED_ID = "android.support.customtabs.extra.EXTRA_REMOTEVIEWS_CLICKED_ID";
   public static final String EXTRA_REMOTEVIEWS_PENDINGINTENT = "android.support.customtabs.extra.EXTRA_REMOTEVIEWS_PENDINGINTENT";
   public static final String EXTRA_REMOTEVIEWS_VIEW_IDS = "android.support.customtabs.extra.EXTRA_REMOTEVIEWS_VIEW_IDS";
   public static final String EXTRA_SECONDARY_TOOLBAR_COLOR = "android.support.customtabs.extra.SECONDARY_TOOLBAR_COLOR";
   public static final String EXTRA_SESSION = "android.support.customtabs.extra.SESSION";
   public static final String EXTRA_TINT_ACTION_BUTTON = "android.support.customtabs.extra.TINT_ACTION_BUTTON";
   public static final String EXTRA_TITLE_VISIBILITY_STATE = "android.support.customtabs.extra.TITLE_VISIBILITY";
   public static final String EXTRA_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
   public static final String EXTRA_TOOLBAR_ITEMS = "android.support.customtabs.extra.TOOLBAR_ITEMS";
   private static final String EXTRA_USER_OPT_OUT_FROM_CUSTOM_TABS = "android.support.customtabs.extra.user_opt_out";
   public static final String KEY_DESCRIPTION = "android.support.customtabs.customaction.DESCRIPTION";
   public static final String KEY_ICON = "android.support.customtabs.customaction.ICON";
   public static final String KEY_ID = "android.support.customtabs.customaction.ID";
   public static final String KEY_MENU_ITEM_TITLE = "android.support.customtabs.customaction.MENU_ITEM_TITLE";
   public static final String KEY_PENDING_INTENT = "android.support.customtabs.customaction.PENDING_INTENT";
   private static final int MAX_TOOLBAR_ITEMS = 5;
   public static final int NO_TITLE = 0;
   public static final int SHOW_PAGE_TITLE = 1;
   public static final int TOOLBAR_ACTION_BUTTON_ID = 0;
   public final Intent intent;
   public final Bundle startAnimationBundle;

   private CustomTabsIntent(Intent var1, Bundle var2) {
      this.intent = var1;
      this.startAnimationBundle = var2;
   }

   // $FF: synthetic method
   CustomTabsIntent(Intent var1, Bundle var2, Object var3) {
      this(var1, var2);
   }

   public static int getMaxToolbarItems() {
      return 5;
   }

   public static Intent setAlwaysUseBrowserUI(Intent var0) {
      Intent var1 = var0;
      if (var0 == null) {
         var1 = new Intent("android.intent.action.VIEW");
      }

      var1.addFlags(268435456);
      var1.putExtra("android.support.customtabs.extra.user_opt_out", true);
      return var1;
   }

   public static boolean shouldAlwaysUseBrowserUI(Intent var0) {
      boolean var1 = false;
      boolean var2 = var1;
      if (var0.getBooleanExtra("android.support.customtabs.extra.user_opt_out", false)) {
         var2 = var1;
         if ((var0.getFlags() & 268435456) != 0) {
            var2 = true;
         }
      }

      return var2;
   }

   public void launchUrl(Context var1, Uri var2) {
      this.intent.setData(var2);
      ContextCompat.startActivity(var1, this.intent, this.startAnimationBundle);
   }

   public void setUseNewTask() {
      this.intent.addFlags(268435456);
   }

   public static final class Builder {
      private ArrayList mActionButtons;
      private boolean mInstantAppsEnabled;
      private final Intent mIntent;
      private ArrayList mMenuItems;
      private Bundle mStartAnimationBundle;

      public Builder() {
         this((CustomTabsSession)null);
      }

      public Builder(CustomTabsSession var1) {
         this.mIntent = new Intent("android.intent.action.VIEW");
         Object var2 = null;
         this.mMenuItems = null;
         this.mStartAnimationBundle = null;
         this.mActionButtons = null;
         this.mInstantAppsEnabled = true;
         if (var1 != null) {
            this.mIntent.setPackage(var1.getComponentName().getPackageName());
         }

         Bundle var3 = new Bundle();
         IBinder var4;
         if (var1 == null) {
            var4 = (IBinder)var2;
         } else {
            var4 = var1.getBinder();
         }

         BundleCompat.putBinder(var3, "android.support.customtabs.extra.SESSION", var4);
         this.mIntent.putExtras(var3);
      }

      public CustomTabsIntent.Builder addDefaultShareMenuItem() {
         this.mIntent.putExtra("android.support.customtabs.extra.SHARE_MENU_ITEM", true);
         return this;
      }

      public CustomTabsIntent.Builder addMenuItem(String var1, PendingIntent var2) {
         if (this.mMenuItems == null) {
            this.mMenuItems = new ArrayList();
         }

         Bundle var3 = new Bundle();
         var3.putString("android.support.customtabs.customaction.MENU_ITEM_TITLE", var1);
         var3.putParcelable("android.support.customtabs.customaction.PENDING_INTENT", var2);
         this.mMenuItems.add(var3);
         return this;
      }

      @Deprecated
      public CustomTabsIntent.Builder addToolbarItem(int var1, Bitmap var2, String var3, PendingIntent var4) throws IllegalStateException {
         if (this.mActionButtons == null) {
            this.mActionButtons = new ArrayList();
         }

         if (this.mActionButtons.size() < 5) {
            Bundle var5 = new Bundle();
            var5.putInt("android.support.customtabs.customaction.ID", var1);
            var5.putParcelable("android.support.customtabs.customaction.ICON", var2);
            var5.putString("android.support.customtabs.customaction.DESCRIPTION", var3);
            var5.putParcelable("android.support.customtabs.customaction.PENDING_INTENT", var4);
            this.mActionButtons.add(var5);
            return this;
         } else {
            throw new IllegalStateException("Exceeded maximum toolbar item count of 5");
         }
      }

      public CustomTabsIntent build() {
         ArrayList var1 = this.mMenuItems;
         if (var1 != null) {
            this.mIntent.putParcelableArrayListExtra("android.support.customtabs.extra.MENU_ITEMS", var1);
         }

         var1 = this.mActionButtons;
         if (var1 != null) {
            this.mIntent.putParcelableArrayListExtra("android.support.customtabs.extra.TOOLBAR_ITEMS", var1);
         }

         this.mIntent.putExtra("android.support.customtabs.extra.EXTRA_ENABLE_INSTANT_APPS", this.mInstantAppsEnabled);
         return new CustomTabsIntent(this.mIntent, this.mStartAnimationBundle);
      }

      public CustomTabsIntent.Builder enableUrlBarHiding() {
         this.mIntent.putExtra("android.support.customtabs.extra.ENABLE_URLBAR_HIDING", true);
         return this;
      }

      public CustomTabsIntent.Builder setActionButton(Bitmap var1, String var2, PendingIntent var3) {
         return this.setActionButton(var1, var2, var3, false);
      }

      public CustomTabsIntent.Builder setActionButton(Bitmap var1, String var2, PendingIntent var3, boolean var4) {
         Bundle var5 = new Bundle();
         var5.putInt("android.support.customtabs.customaction.ID", 0);
         var5.putParcelable("android.support.customtabs.customaction.ICON", var1);
         var5.putString("android.support.customtabs.customaction.DESCRIPTION", var2);
         var5.putParcelable("android.support.customtabs.customaction.PENDING_INTENT", var3);
         this.mIntent.putExtra("android.support.customtabs.extra.ACTION_BUTTON_BUNDLE", var5);
         this.mIntent.putExtra("android.support.customtabs.extra.TINT_ACTION_BUTTON", var4);
         return this;
      }

      public CustomTabsIntent.Builder setCloseButtonIcon(Bitmap var1) {
         this.mIntent.putExtra("android.support.customtabs.extra.CLOSE_BUTTON_ICON", var1);
         return this;
      }

      public CustomTabsIntent.Builder setExitAnimations(Context var1, int var2, int var3) {
         Bundle var4 = ActivityOptionsCompat.makeCustomAnimation(var1, var2, var3).toBundle();
         this.mIntent.putExtra("android.support.customtabs.extra.EXIT_ANIMATION_BUNDLE", var4);
         return this;
      }

      public CustomTabsIntent.Builder setInstantAppsEnabled(boolean var1) {
         this.mInstantAppsEnabled = var1;
         return this;
      }

      public CustomTabsIntent.Builder setSecondaryToolbarColor(int var1) {
         this.mIntent.putExtra("android.support.customtabs.extra.SECONDARY_TOOLBAR_COLOR", var1);
         return this;
      }

      public CustomTabsIntent.Builder setSecondaryToolbarViews(RemoteViews var1, int[] var2, PendingIntent var3) {
         this.mIntent.putExtra("android.support.customtabs.extra.EXTRA_REMOTEVIEWS", var1);
         this.mIntent.putExtra("android.support.customtabs.extra.EXTRA_REMOTEVIEWS_VIEW_IDS", var2);
         this.mIntent.putExtra("android.support.customtabs.extra.EXTRA_REMOTEVIEWS_PENDINGINTENT", var3);
         return this;
      }

      public CustomTabsIntent.Builder setShowTitle(boolean var1) {
         this.mIntent.putExtra("android.support.customtabs.extra.TITLE_VISIBILITY", var1);
         return this;
      }

      public CustomTabsIntent.Builder setStartAnimations(Context var1, int var2, int var3) {
         this.mStartAnimationBundle = ActivityOptionsCompat.makeCustomAnimation(var1, var2, var3).toBundle();
         return this;
      }

      public CustomTabsIntent.Builder setToolbarColor(int var1) {
         this.mIntent.putExtra("android.support.customtabs.extra.TOOLBAR_COLOR", var1);
         return this;
      }
   }
}
