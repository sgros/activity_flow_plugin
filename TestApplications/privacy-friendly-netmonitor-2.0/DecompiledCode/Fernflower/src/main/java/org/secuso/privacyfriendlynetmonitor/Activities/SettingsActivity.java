package org.secuso.privacyfriendlynetmonitor.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.view.MenuItem;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;

public class SettingsActivity extends BaseActivity {
   private static OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new OnPreferenceChangeListener() {
      public boolean onPreferenceChange(Preference var1, Object var2) {
         String var5 = var2.toString();
         Log.d("NetMonitor", var5);
         if (var1 instanceof ListPreference) {
            ListPreference var3 = (ListPreference)var1;
            int var4 = var3.findIndexOfValue(var5);
            CharSequence var6;
            if (var4 >= 0) {
               var6 = var3.getEntries()[var4];
            } else {
               var6 = null;
            }

            var1.setSummary(var6);
         } else {
            var1.setSummary(var5);
         }

         return true;
      }
   };

   private static void bindPreferenceSummaryToValue(Preference var0) {
      var0.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
      sBindPreferenceSummaryToValueListener.onPreferenceChange(var0, PreferenceManager.getDefaultSharedPreferences(var0.getContext()).getString(var0.getKey(), ""));
   }

   private static boolean isXLargeTablet(Context var0) {
      boolean var1;
      if ((var0.getResources().getConfiguration().screenLayout & 15) >= 4) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected int getNavigationDrawerID() {
      return 2131296410;
   }

   protected boolean isValidFragment(String var1) {
      boolean var2;
      if (!PreferenceFragment.class.getName().equals(var1) && !SettingsActivity.GeneralPreferenceFragment.class.getName().equals(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      RunStore.setContext(this);
      this.setContentView(2131427368);
      this.overridePendingTransition(0, 0);
   }

   @TargetApi(11)
   public static class GeneralPreferenceFragment extends PreferenceFragment {
      public void onCreate(Bundle var1) {
         super.onCreate(var1);
         this.addPreferencesFromResource(2131820544);
      }

      public boolean onOptionsItemSelected(MenuItem var1) {
         if (var1.getItemId() == 16908332) {
            this.startActivity(new Intent(this.getActivity(), SettingsActivity.class));
            return true;
         } else {
            return super.onOptionsItemSelected(var1);
         }
      }
   }
}
