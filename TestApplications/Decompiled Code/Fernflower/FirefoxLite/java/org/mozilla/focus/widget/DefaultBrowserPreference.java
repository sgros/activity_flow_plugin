package org.mozilla.focus.widget;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.preference.Preference;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Switch;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.components.ComponentToggleService;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.Browsers;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;

@TargetApi(24)
public class DefaultBrowserPreference extends Preference {
   private DefaultBrowserPreference.DefaultBrowserAction action;
   private Switch switchView;

   public DefaultBrowserPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setWidgetLayoutResource(2131493011);
      this.init();
   }

   public DefaultBrowserPreference(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.setWidgetLayoutResource(2131493011);
      this.init();
   }

   private void clearDefaultBrowser(Context var1) {
      Intent var2 = new Intent();
      var2.setComponent(new ComponentName(var1, ComponentToggleService.class));
      var1.startService(var2);
   }

   private void init() {
      if (this.action == null) {
         Object var1;
         if (VERSION.SDK_INT >= 23) {
            var1 = new DefaultBrowserPreference.DefaultAction(this);
         } else {
            var1 = new DefaultBrowserPreference.LowSdkAction(this);
         }

         this.action = (DefaultBrowserPreference.DefaultBrowserAction)var1;
      }

   }

   private void openAppDetailSettings(Context var1) {
      Intent var2 = new Intent();
      var2.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
      var2.setData(Uri.fromParts("package", var1.getPackageName(), (String)null));
      var1.startActivity(var2);
   }

   private void openSumoPage(Context var1) {
      var1.startActivity(InfoActivity.getIntentFor(var1, SupportUtils.getSumoURLForTopic(var1, "rocket-default"), this.getTitle().toString()));
   }

   private void triggerWebOpen() {
      Intent var1 = new Intent("android.intent.action.VIEW");
      var1.setData(Uri.parse("http://mozilla.org"));
      var1.putExtra("_intent_to_resolve_browser_", true);
      this.getContext().startActivity(var1);
   }

   protected void onBindView(View var1) {
      super.onBindView(var1);
      this.switchView = (Switch)var1.findViewById(2131296677);
      this.update();
   }

   protected void onClick() {
      this.action.onPrefClicked();
   }

   public void onFragmentPause() {
      this.action.onFragmentPause();
   }

   public void onFragmentResume() {
      this.update();
      this.action.onFragmentResume();
   }

   public void update() {
      if (this.switchView != null) {
         boolean var1 = Browsers.isDefaultBrowser(this.getContext());
         this.switchView.setChecked(var1);
         if (ComponentToggleService.isAlive(this.getContext())) {
            this.setEnabled(false);
            this.setSummary(2131755344);
         } else {
            this.setEnabled(true);
            this.setSummary((CharSequence)null);
         }

         Settings.updatePrefDefaultBrowserIfNeeded(this.getContext(), var1);
      }

   }

   private static class DefaultAction implements DefaultBrowserPreference.DefaultBrowserAction {
      DefaultBrowserPreference pref;

      DefaultAction(DefaultBrowserPreference var1) {
         this.pref = var1;
      }

      public void onFragmentPause() {
      }

      public void onFragmentResume() {
      }

      public void onPrefClicked() {
         if (!IntentUtils.openDefaultAppsSettings(this.pref.getContext())) {
            this.pref.openSumoPage(this.pref.getContext());
         }

      }
   }

   private interface DefaultBrowserAction {
      void onFragmentPause();

      void onFragmentResume();

      void onPrefClicked();
   }

   private static class LowSdkAction implements DefaultBrowserPreference.DefaultBrowserAction {
      DefaultBrowserPreference pref;
      BroadcastReceiver receiver;

      LowSdkAction(DefaultBrowserPreference var1) {
         this.pref = var1;
         this.receiver = new DefaultBrowserPreference.ServiceReceiver(var1);
      }

      public void onFragmentPause() {
         if (VERSION.SDK_INT < 24) {
            LocalBroadcastManager.getInstance(this.pref.getContext()).unregisterReceiver(this.receiver);
         }

      }

      public void onFragmentResume() {
         LocalBroadcastManager.getInstance(this.pref.getContext()).registerReceiver(this.receiver, ComponentToggleService.SERVICE_STOP_INTENT_FILTER);
      }

      public void onPrefClicked() {
         Context var1 = this.pref.getContext();
         boolean var2 = Browsers.isDefaultBrowser(var1);
         boolean var3 = Browsers.hasDefaultBrowser(var1);
         if (var2) {
            this.pref.openAppDetailSettings(var1);
         } else if (var3) {
            this.pref.setEnabled(false);
            this.pref.setSummary(2131755344);
            this.pref.clearDefaultBrowser(var1);
         } else {
            this.pref.triggerWebOpen();
         }

      }
   }

   private static class ServiceReceiver extends BroadcastReceiver {
      DefaultBrowserPreference pref;

      ServiceReceiver(DefaultBrowserPreference var1) {
         this.pref = var1;
      }

      public void onReceive(Context var1, Intent var2) {
         this.pref.update();
         NotificationManagerCompat.from(var1).cancel(57083);
         boolean var3 = Browsers.isDefaultBrowser(var1);
         boolean var4 = Browsers.hasDefaultBrowser(var1);
         if (var4 && !var3) {
            TelemetryWrapper.onDefaultBrowserServiceFailed();
         }

         if (!var3 && !var4) {
            this.pref.triggerWebOpen();
         }

      }
   }
}
