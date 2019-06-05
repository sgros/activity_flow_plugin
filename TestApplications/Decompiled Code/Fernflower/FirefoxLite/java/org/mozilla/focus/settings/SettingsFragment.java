package org.mozilla.focus.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import java.util.Locale;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.locale.LocaleManager;
import org.mozilla.focus.locale.Locales;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.utils.DialogUtils;
import org.mozilla.focus.utils.FirebaseHelper;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.widget.DefaultBrowserPreference;
import org.mozilla.rocket.nightmode.AdjustBrightnessDialog;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
   private static int debugClicks;
   private boolean hasContentPortal = false;
   private boolean localeUpdated;

   private boolean debugingFirebase() {
      ++debugClicks;
      if (debugClicks > 19) {
         Intent var1 = new Intent();
         var1.setAction("android.intent.action.SEND");
         var1.setType("text/plain");
         var1.putExtra("android.intent.extra.TEXT", FirebaseHelper.getFcmToken());
         this.startActivity(Intent.createChooser(var1, "This token is only for QA to test in Nightly and debug build"));
         return true;
      } else {
         return false;
      }
   }

   public void onAttach(Context var1) {
      super.onAttach(var1);
      this.hasContentPortal = AppConfigWrapper.hasNewsPortal(var1);
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.addPreferencesFromResource(2132017154);
      PreferenceScreen var2 = (PreferenceScreen)this.findPreference("root_preferences");
      if (!AppConstants.isDevBuild() && !AppConstants.isFirebaseBuild() && !AppConstants.isNightlyBuild()) {
         var2.removePreference(this.findPreference(this.getString(2131755298)));
      }

      if (!this.hasContentPortal) {
         var2.removePreference(this.findPreference(this.getString(2131755334)));
      }

      this.findPreference(this.getString(2131755313)).setEnabled(Settings.getInstance(this.getActivity()).isNightModeEnable());
   }

   public void onPause() {
      super.onPause();
      DefaultBrowserPreference var1 = (DefaultBrowserPreference)this.findPreference(this.getString(2131755303));
      if (var1 != null) {
         var1.onFragmentPause();
      }

      this.getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
   }

   public boolean onPreferenceTreeClick(PreferenceScreen var1, Preference var2) {
      Resources var3 = this.getResources();
      String var4 = var2.getKey();
      TelemetryWrapper.settingsClickEvent(var4);
      if (var4.equals(var3.getString(2131755310))) {
         DialogUtils.showRateAppDialog(this.getActivity());
      } else if (var4.equals(var3.getString(2131755327))) {
         if (!this.debugingFirebase()) {
            DialogUtils.showShareAppDialog(this.getActivity());
         }
      } else if (var4.equals(var3.getString(2131755295))) {
         this.startActivity(InfoActivity.getAboutIntent(this.getActivity()));
      } else if (var4.equals(var3.getString(2131755313))) {
         Settings.getInstance(this.getActivity()).setNightModeSpotlight(true);
         this.startActivity(AdjustBrightnessDialog.Intents.INSTANCE.getStartIntentFromSetting(this.getActivity()));
      } else if (var4.equals(var3.getString(2131755303))) {
         TelemetryWrapper.clickDefaultBrowserInSetting();
      }

      return super.onPreferenceTreeClick(var1, var2);
   }

   public void onResume() {
      super.onResume();
      this.getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
      DefaultBrowserPreference var1 = (DefaultBrowserPreference)this.findPreference(this.getString(2131755303));
      if (var1 != null) {
         var1.onFragmentResume();
      }

   }

   public void onSharedPreferenceChanged(SharedPreferences var1, String var2) {
      if (var2.equals(this.getString(2131755312))) {
         if (!this.localeUpdated) {
            this.localeUpdated = true;
            String var3 = ((ListPreference)this.findPreference(this.getString(2131755312))).getValue();
            LocaleManager var4 = LocaleManager.getInstance();
            Locale var5;
            if (TextUtils.isEmpty(var3)) {
               var4.resetToSystemLocale(this.getActivity());
               var5 = var4.getCurrentLocale(this.getActivity());
            } else {
               var5 = Locales.parseLocaleCode(var3);
               var4.setSelectedLocale(this.getActivity(), var3);
            }

            TelemetryWrapper.settingsLocaleChangeEvent(var2, String.valueOf(var5), TextUtils.isEmpty(var3));
            var4.updateConfiguration(this.getActivity(), var5);
            FirebaseHelper.onLocaleUpdate(this.getActivity());
            this.getActivity().onConfigurationChanged(this.getActivity().getResources().getConfiguration());
            this.getActivity().setResult(1);
            this.getFragmentManager().beginTransaction().replace(2131296374, new SettingsFragment()).commit();
         }
      } else {
         if (!var2.equals(this.getString(2131755331))) {
            TelemetryWrapper.settingsEvent(var2, String.valueOf(var1.getAll().get(var2)), false);
         }

         if (!var2.equals(this.getString(2131755329))) {
            var2.equals(this.getString(2131755330));
         }

      }
   }
}
