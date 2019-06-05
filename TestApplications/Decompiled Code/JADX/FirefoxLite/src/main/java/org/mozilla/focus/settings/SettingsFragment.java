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
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.nightmode.AdjustBrightnessDialog.Intents;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    private static int debugClicks;
    private boolean hasContentPortal = false;
    private boolean localeUpdated;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.hasContentPortal = AppConfigWrapper.hasNewsPortal(context);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(C0769R.xml.settings);
        PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference("root_preferences");
        if (!(AppConstants.isDevBuild() || AppConstants.isFirebaseBuild() || AppConstants.isNightlyBuild())) {
            preferenceScreen.removePreference(findPreference(getString(C0769R.string.pref_key_category_development)));
        }
        if (!this.hasContentPortal) {
            preferenceScreen.removePreference(findPreference(getString(C0769R.string.pref_s_news)));
        }
        findPreference(getString(C0769R.string.pref_key_night_mode_brightness)).setEnabled(Settings.getInstance(getActivity()).isNightModeEnable());
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Resources resources = getResources();
        String key = preference.getKey();
        TelemetryWrapper.settingsClickEvent(key);
        if (key.equals(resources.getString(C0769R.string.pref_key_give_feedback))) {
            DialogUtils.showRateAppDialog(getActivity());
        } else if (key.equals(resources.getString(C0769R.string.pref_key_share_with_friends))) {
            if (!debugingFirebase()) {
                DialogUtils.showShareAppDialog(getActivity());
            }
        } else if (key.equals(resources.getString(C0769R.string.pref_key_about))) {
            startActivity(InfoActivity.getAboutIntent(getActivity()));
        } else if (key.equals(resources.getString(C0769R.string.pref_key_night_mode_brightness))) {
            Settings.getInstance(getActivity()).setNightModeSpotlight(true);
            startActivity(Intents.INSTANCE.getStartIntentFromSetting(getActivity()));
        } else if (key.equals(resources.getString(C0769R.string.pref_key_default_browser))) {
            TelemetryWrapper.clickDefaultBrowserInSetting();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private boolean debugingFirebase() {
        debugClicks++;
        if (debugClicks <= 19) {
            return false;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", FirebaseWrapper.getFcmToken());
        startActivity(Intent.createChooser(intent, "This token is only for QA to test in Nightly and debug build"));
        return true;
    }

    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        DefaultBrowserPreference defaultBrowserPreference = (DefaultBrowserPreference) findPreference(getString(C0769R.string.pref_key_default_browser));
        if (defaultBrowserPreference != null) {
            defaultBrowserPreference.onFragmentResume();
        }
    }

    public void onPause() {
        super.onPause();
        DefaultBrowserPreference defaultBrowserPreference = (DefaultBrowserPreference) findPreference(getString(C0769R.string.pref_key_default_browser));
        if (defaultBrowserPreference != null) {
            defaultBrowserPreference.onFragmentPause();
        }
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        if (!str.equals(getString(C0769R.string.pref_key_locale))) {
            if (!str.equals(getString(C0769R.string.pref_key_telemetry))) {
                TelemetryWrapper.settingsEvent(str, String.valueOf(sharedPreferences.getAll().get(str)), false);
            }
            if (!str.equals(getString(C0769R.string.pref_key_storage_clear_browsing_data))) {
                str.equals(getString(C0769R.string.pref_key_storage_save_downloads_to));
            }
        } else if (!this.localeUpdated) {
            Object currentLocale;
            this.localeUpdated = true;
            String value = ((ListPreference) findPreference(getString(C0769R.string.pref_key_locale))).getValue();
            LocaleManager instance = LocaleManager.getInstance();
            if (TextUtils.isEmpty(value)) {
                instance.resetToSystemLocale(getActivity());
                currentLocale = instance.getCurrentLocale(getActivity());
            } else {
                currentLocale = Locales.parseLocaleCode(value);
                instance.setSelectedLocale(getActivity(), value);
            }
            TelemetryWrapper.settingsLocaleChangeEvent(str, String.valueOf(currentLocale), TextUtils.isEmpty(value));
            instance.updateConfiguration(getActivity(), currentLocale);
            FirebaseHelper.onLocaleUpdate(getActivity());
            getActivity().onConfigurationChanged(getActivity().getResources().getConfiguration());
            getActivity().setResult(1);
            getFragmentManager().beginTransaction().replace(2131296374, new SettingsFragment()).commit();
        }
    }
}
