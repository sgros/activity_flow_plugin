package org.secuso.privacyfriendlynetmonitor.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import org.secuso.privacyfriendlynetmonitor.Assistant.Const;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.C0501R;

public class SettingsActivity extends BaseActivity {
    private static OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new C04931();

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.SettingsActivity$1 */
    static class C04931 implements OnPreferenceChangeListener {
        C04931() {
        }

        public boolean onPreferenceChange(Preference preference, Object obj) {
            String obj2 = obj.toString();
            Log.d(Const.LOG_TAG, obj2);
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int findIndexOfValue = listPreference.findIndexOfValue(obj2);
                preference.setSummary(findIndexOfValue >= 0 ? listPreference.getEntries()[findIndexOfValue] : null);
            } else {
                preference.setSummary(obj2);
            }
            return true;
        }
    }

    @TargetApi(11)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(C0501R.xml.preferences);
        }

        public boolean onOptionsItemSelected(MenuItem menuItem) {
            if (menuItem.getItemId() != 16908332) {
                return super.onOptionsItemSelected(menuItem);
            }
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
    }

    /* Access modifiers changed, original: protected */
    public int getNavigationDrawerID() {
        return C0501R.C0499id.nav_settings;
    }

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 4;
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        RunStore.setContext(this);
        setContentView((int) C0501R.layout.activity_settings);
        overridePendingTransition(0, 0);
    }

    /* Access modifiers changed, original: protected */
    public boolean isValidFragment(String str) {
        return PreferenceFragment.class.getName().equals(str) || GeneralPreferenceFragment.class.getName().equals(str);
    }
}
