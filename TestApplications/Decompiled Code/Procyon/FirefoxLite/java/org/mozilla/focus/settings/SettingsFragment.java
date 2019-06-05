// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.settings;

import java.util.Locale;
import android.app.Fragment;
import org.mozilla.focus.utils.FirebaseHelper;
import org.mozilla.focus.locale.Locales;
import android.text.TextUtils;
import org.mozilla.focus.locale.LocaleManager;
import android.preference.ListPreference;
import android.content.SharedPreferences;
import android.content.res.Resources;
import org.mozilla.rocket.nightmode.AdjustBrightnessDialog;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.utils.DialogUtils;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.preference.Preference;
import org.mozilla.focus.widget.DefaultBrowserPreference;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.AppConstants;
import android.preference.PreferenceScreen;
import android.os.Bundle;
import org.mozilla.focus.utils.AppConfigWrapper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences$OnSharedPreferenceChangeListener;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences$OnSharedPreferenceChangeListener
{
    private static int debugClicks;
    private boolean hasContentPortal;
    private boolean localeUpdated;
    
    public SettingsFragment() {
        this.hasContentPortal = false;
    }
    
    private boolean debugingFirebase() {
        ++SettingsFragment.debugClicks;
        if (SettingsFragment.debugClicks > 19) {
            final Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", FirebaseWrapper.getFcmToken());
            this.startActivity(Intent.createChooser(intent, (CharSequence)"This token is only for QA to test in Nightly and debug build"));
            return true;
        }
        return false;
    }
    
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.hasContentPortal = AppConfigWrapper.hasNewsPortal(context);
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.addPreferencesFromResource(2132017154);
        final PreferenceScreen preferenceScreen = (PreferenceScreen)this.findPreference((CharSequence)"root_preferences");
        if (!AppConstants.isDevBuild() && !AppConstants.isFirebaseBuild() && !AppConstants.isNightlyBuild()) {
            preferenceScreen.removePreference(this.findPreference((CharSequence)this.getString(2131755298)));
        }
        if (!this.hasContentPortal) {
            preferenceScreen.removePreference(this.findPreference((CharSequence)this.getString(2131755334)));
        }
        this.findPreference((CharSequence)this.getString(2131755313)).setEnabled(Settings.getInstance((Context)this.getActivity()).isNightModeEnable());
    }
    
    public void onPause() {
        super.onPause();
        final DefaultBrowserPreference defaultBrowserPreference = (DefaultBrowserPreference)this.findPreference((CharSequence)this.getString(2131755303));
        if (defaultBrowserPreference != null) {
            defaultBrowserPreference.onFragmentPause();
        }
        this.getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
    }
    
    public boolean onPreferenceTreeClick(final PreferenceScreen preferenceScreen, final Preference preference) {
        final Resources resources = this.getResources();
        final String key = preference.getKey();
        TelemetryWrapper.settingsClickEvent(key);
        if (key.equals(resources.getString(2131755310))) {
            DialogUtils.showRateAppDialog((Context)this.getActivity());
        }
        else if (key.equals(resources.getString(2131755327))) {
            if (!this.debugingFirebase()) {
                DialogUtils.showShareAppDialog((Context)this.getActivity());
            }
        }
        else if (key.equals(resources.getString(2131755295))) {
            this.startActivity(InfoActivity.getAboutIntent((Context)this.getActivity()));
        }
        else if (key.equals(resources.getString(2131755313))) {
            Settings.getInstance((Context)this.getActivity()).setNightModeSpotlight(true);
            this.startActivity(AdjustBrightnessDialog.Intents.INSTANCE.getStartIntentFromSetting((Context)this.getActivity()));
        }
        else if (key.equals(resources.getString(2131755303))) {
            TelemetryWrapper.clickDefaultBrowserInSetting();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
    
    public void onResume() {
        super.onResume();
        this.getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        final DefaultBrowserPreference defaultBrowserPreference = (DefaultBrowserPreference)this.findPreference((CharSequence)this.getString(2131755303));
        if (defaultBrowserPreference != null) {
            defaultBrowserPreference.onFragmentResume();
        }
    }
    
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String s) {
        if (!s.equals(this.getString(2131755312))) {
            if (!s.equals(this.getString(2131755331))) {
                TelemetryWrapper.settingsEvent(s, String.valueOf(sharedPreferences.getAll().get(s)), false);
            }
            if (!s.equals(this.getString(2131755329))) {
                s.equals(this.getString(2131755330));
            }
            return;
        }
        if (this.localeUpdated) {
            return;
        }
        this.localeUpdated = true;
        final String value = ((ListPreference)this.findPreference((CharSequence)this.getString(2131755312))).getValue();
        final LocaleManager instance = LocaleManager.getInstance();
        Locale obj;
        if (TextUtils.isEmpty((CharSequence)value)) {
            instance.resetToSystemLocale((Context)this.getActivity());
            obj = instance.getCurrentLocale((Context)this.getActivity());
        }
        else {
            obj = Locales.parseLocaleCode(value);
            instance.setSelectedLocale((Context)this.getActivity(), value);
        }
        TelemetryWrapper.settingsLocaleChangeEvent(s, String.valueOf(obj), TextUtils.isEmpty((CharSequence)value));
        instance.updateConfiguration((Context)this.getActivity(), obj);
        FirebaseHelper.onLocaleUpdate((Context)this.getActivity());
        this.getActivity().onConfigurationChanged(this.getActivity().getResources().getConfiguration());
        this.getActivity().setResult(1);
        this.getFragmentManager().beginTransaction().replace(2131296374, (Fragment)new SettingsFragment()).commit();
    }
}
